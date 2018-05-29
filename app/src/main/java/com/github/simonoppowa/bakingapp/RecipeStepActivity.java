package com.github.simonoppowa.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

import static com.github.simonoppowa.bakingapp.RecipeActivity.RECIPE_STEP_KEY;

public class RecipeStepActivity extends AppCompatActivity {

    private SimpleExoPlayer mSimpleExoPlayer;

    @BindView(R.id.recipe_step_exo_player)
    SimpleExoPlayerView mSimpleExoPlayerView;

    private Timeline.Window mWindow;
    private DataSource.Factory mMediaDataSourceFactor;
    private DefaultTrackSelector mTrackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter mBandwidthMeter;

    private RecipeStep mRecipeStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        //setting up Libraries
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        //getting RecipeStep from Intent
        Intent recipeStepIntent = getIntent();

        mRecipeStep = recipeStepIntent.getParcelableExtra(RECIPE_STEP_KEY);

        if (mRecipeStep == null) {
            throw new NullPointerException("No RecipeStep was passed to RecipeStepActivity");
        }

        //setting title
        String title = (mRecipeStep.getId() + 1) + ". " + mRecipeStep.getShortDescription();
        setTitle(title);

        //setting up Player
        shouldAutoPlay = true;
        mBandwidthMeter = new DefaultBandwidthMeter();
        mMediaDataSourceFactor = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this,
                        "BakingApp"),
                (TransferListener<? super DataSource>) mBandwidthMeter);
        mWindow = new Timeline.Window();

        if(mRecipeStep.getVideoURL() != null && !mRecipeStep.getVideoURL().isEmpty()) {
            setUpExoPlayer();
        }

    }

    private void setUpExoPlayer() {

        mSimpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(mBandwidthMeter);

        mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, mTrackSelector, new DefaultLoadControl());

        mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

        mSimpleExoPlayer.setPlayWhenReady(shouldAutoPlay);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mRecipeStep.getVideoURL()),
                mMediaDataSourceFactor, extractorsFactory, null, null);

        mSimpleExoPlayer.prepare(mediaSource);
    }
}
