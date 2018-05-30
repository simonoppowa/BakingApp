package com.github.simonoppowa.bakingapp.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.simonoppowa.bakingapp.R;
import com.github.simonoppowa.bakingapp.RecipeActivity;
import com.github.simonoppowa.bakingapp.model.Recipe;
import com.github.simonoppowa.bakingapp.model.RecipeStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class RecipeVideoFragment extends Fragment {

    public static final String RECIPE_STEP_KEY  = "recipeStep";
    private static final boolean SHOULD_AUTO_PLAY = false;

    private SimpleExoPlayer mSimpleExoPlayer;

    @Nullable @BindView(R.id.recipe_step_description_textView)
    TextView mDescriptionTextView;

    @BindView(R.id.recipe_step_no_video_image)
    ImageView mDefaultRecipeStepImage;

    @BindView(R.id.recipe_step_exo_player)
    SimpleExoPlayerView mSimpleExoPlayerView;

    private Timeline.Window mWindow;
    private DataSource.Factory mMediaDataSourceFactor;
    private DefaultTrackSelector mTrackSelector;
    private BandwidthMeter mBandwidthMeter;

    private RecipeStep mRecipeStep;

    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_video, container, false);

        //setting up Libraries
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this, view);

        //getting context
        mContext = getActivity();

        //getting RecipeStep
        mRecipeStep = getArguments().getParcelable(RECIPE_STEP_KEY);

        //setting description
        if(mDescriptionTextView != null) {
            mDescriptionTextView.setText(mRecipeStep.getDescription());
        }

        //check if video available
        if(mRecipeStep.getVideoURL() != null && !mRecipeStep.getVideoURL().isEmpty()) {
            showVideoPlayer();
        }

        return view;
    }

    public static RecipeVideoFragment newInstance(RecipeStep recipeStep) {

        RecipeVideoFragment fragment = new RecipeVideoFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelable(RECIPE_STEP_KEY, recipeStep);

        fragment.setArguments(bundle);
        return fragment;
    }

    public void showVideoPlayer() {

        //setting up Player
        mBandwidthMeter = new DefaultBandwidthMeter();
        mMediaDataSourceFactor = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "BakingApp"),
                (TransferListener<? super DataSource>) mBandwidthMeter);
        mWindow = new Timeline.Window();

        mSimpleExoPlayerView.setVisibility(View.VISIBLE);
        mDefaultRecipeStepImage.setVisibility(View.GONE);
        setUpExoPlayer();
    }

    private void setUpExoPlayer() {

        mSimpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(mBandwidthMeter);

        mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, mTrackSelector, new DefaultLoadControl());

        mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

        mSimpleExoPlayer.setPlayWhenReady(SHOULD_AUTO_PLAY);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mRecipeStep.getVideoURL()),
                mMediaDataSourceFactor,
                extractorsFactory,
                null,
                null);

        mSimpleExoPlayer.prepare(mediaSource);

        //setting default video image
        Bitmap videoImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_recipe_image);
        mSimpleExoPlayerView.setDefaultArtwork(videoImage);

    }
    public void releasePlayer() {
        if(mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    public void pausePlayer() {
        if(mSimpleExoPlayer != null) {
            mSimpleExoPlayer.setPlayWhenReady(false);
            mSimpleExoPlayer.getPlaybackState();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
            releasePlayer();
    }
}
