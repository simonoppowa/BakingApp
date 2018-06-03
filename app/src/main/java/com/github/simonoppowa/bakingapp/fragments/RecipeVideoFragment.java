package com.github.simonoppowa.bakingapp.fragments;

import android.content.Context;
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
import com.github.simonoppowa.bakingapp.model.RecipeStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
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
    private static final String PLAYER_POSITION_KEY = "playerPosition";
    private static final String PLAYER_IS_PLAY_WHEN_READY = "isPlayWhenReady";
    private static final boolean SHOULD_AUTO_PLAY = false;

    private SimpleExoPlayer mSimpleExoPlayer;

    @Nullable @BindView(R.id.recipe_step_description_textView)
    TextView mDescriptionTextView;

    @BindView(R.id.recipe_step_no_video_image)
    ImageView mDefaultRecipeStepImage;

    @BindView(R.id.recipe_step_exo_player)
    SimpleExoPlayerView mSimpleExoPlayerView;

    private DataSource.Factory mMediaDataSourceFactor;
    private DefaultTrackSelector mTrackSelector;
    private BandwidthMeter mBandwidthMeter;

    private RecipeStep mRecipeStep;

    private Context mContext;

    private long mPlayerPosition;
    private boolean mIsPlayWhenReady;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_video, container, false);

        mPlayerPosition = 0;
        mIsPlayWhenReady = false;

        //setting up Libraries
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this, view);

        //getting context
        mContext = getActivity();

        //getting RecipeStep
        mRecipeStep = getArguments().getParcelable(RECIPE_STEP_KEY);

        if(mRecipeStep == null) {
            throw new NullPointerException("No RecipeStep was passed to RecipeVideoFragment");
        }

        //setting description
        if(mDescriptionTextView != null) {
            mDescriptionTextView.setText(mRecipeStep.getDescription());
        }

        //checking for rotation
        if(savedInstanceState != null && savedInstanceState.containsKey(PLAYER_POSITION_KEY)
                && savedInstanceState.containsKey(PLAYER_IS_PLAY_WHEN_READY)) {
            //restoring video position before rotation
            mPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION_KEY);
            //restoring video play state before rotation
            mIsPlayWhenReady = savedInstanceState.getBoolean(PLAYER_IS_PLAY_WHEN_READY);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //checking if video available
        if(mRecipeStep.getVideoURL() != null && !mRecipeStep.getVideoURL().isEmpty()) {
            setupExoPlayer();
            //set restored values
            mSimpleExoPlayer.seekTo(mPlayerPosition);
            mSimpleExoPlayer.setPlayWhenReady(mIsPlayWhenReady);
            showVideoPlayer();
        } else {
            showDefaultImage();
        }
    }

    public static RecipeVideoFragment newInstance(RecipeStep recipeStep) {

        RecipeVideoFragment fragment = new RecipeVideoFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelable(RECIPE_STEP_KEY, recipeStep);

        fragment.setArguments(bundle);
        return fragment;
    }

    private void showVideoPlayer() {
        mDefaultRecipeStepImage.setVisibility(View.GONE);
        mSimpleExoPlayerView.setVisibility(View.VISIBLE);
    }

    private void showDefaultImage() {
        mSimpleExoPlayerView.setVisibility(View.GONE);
        mDefaultRecipeStepImage.setVisibility(View.VISIBLE);
    }

    private void setupExoPlayer() {
        if(mSimpleExoPlayer == null) {
            Timber.d("Setting up Player");
            mSimpleExoPlayerView.requestFocus();

            mBandwidthMeter = new DefaultBandwidthMeter();
            mMediaDataSourceFactor = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, "BakingApp"),
                    (TransferListener<? super DataSource>) mBandwidthMeter);

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
        }
    }

    public void releasePlayer() {
        if(mSimpleExoPlayer != null) {
            Timber.d("Releasing player");
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
            mTrackSelector = null;
            mMediaDataSourceFactor = null;
        }
    }

    public void pausePlayer() {
        if(mSimpleExoPlayer != null) {
            mSimpleExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mSimpleExoPlayer != null) {
            //save player position and player state in Bundle
            long playerPosition = mSimpleExoPlayer.getCurrentPosition();
            boolean isPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            outState.putLong(PLAYER_POSITION_KEY, playerPosition);
            outState.putBoolean(PLAYER_IS_PLAY_WHEN_READY, isPlayWhenReady);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mSimpleExoPlayer != null) {
            //saving position and play state
            mPlayerPosition = mSimpleExoPlayer.getCurrentPosition();
            mIsPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
