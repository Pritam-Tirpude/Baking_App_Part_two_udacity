package pritam.project.com.cakemaker.fragments;

import android.app.Dialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pritam.project.com.cakemaker.R;
import pritam.project.com.cakemaker.model.Steps;

/**
 * Created by Pritam on 12-01-2018.
 */

public class RecipeStepDetailViewFragment extends Fragment {

    public static final String TOTAL_RECIPES_BUNDLE = "total_recipes_bundle";
    public static final String TOTAL_STEPS = "total_steps";
    public static final String STEP_INDEX = "step_index";
    public static final String RECIPE_TITLE = "recipe_title";

    public static final String SELECTED_POSITION = "selected_position";
    public static final String PLAYER_READY = "player_ready";

    public static final String STEP_BACK_STACK = "step_back_stack";

    public static final String KEY_PASS = "key_pass";
    public static final String TAG = RecipeStepDetailViewFragment.class.getSimpleName();

    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();

    @BindView(R.id.simple_player_view)
    SimpleExoPlayerView mSimpleExoPlayerView;

    @BindView(R.id.recipe_step_detail_text)
    TextView mTextViewFullDescription;

    @BindView(R.id.thumbImage)
    ImageView mImageViewThumbnail;

    private SimpleExoPlayer mExoPlayer;
    private boolean mPlayWhenReady;
    private boolean mExoPlayerFullscreen = false;
    private long mPlayerPosition;

    private ArrayList<Steps> mSteps = new ArrayList<>();
    private int selectedIndex;
    private String mRecipeName;
    private String mVideoUrl;
    private String mImageUrl;
    private Uri mVideoUri;

    private Dialog mFullScreenDialog;

    public RecipeStepDetailViewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_step_detail_view_items, container, false);
        ButterKnife.bind(this, view);

        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            mSteps = getArguments().getParcelableArrayList(TOTAL_STEPS);
            selectedIndex = getArguments().getInt(STEP_INDEX);
            mRecipeName = getArguments().getString(RECIPE_TITLE);
        } else {
            savedInstanceState = getActivity().getIntent().getExtras();
            mSteps = savedInstanceState.getParcelableArrayList(TOTAL_STEPS);
            selectedIndex = savedInstanceState.getInt(STEP_INDEX);
            mRecipeName = savedInstanceState.getString(RECIPE_TITLE);
        }

        mVideoUrl = mSteps.get(selectedIndex).getVideoURL();

        mImageUrl = mSteps.get(selectedIndex).getThumbnailURL();

        if (TextUtils.isEmpty(mImageUrl)) {
            Uri buildUri = Uri.parse(mImageUrl).buildUpon().build();
            Picasso.with(getContext()).load(buildUri).into(mImageViewThumbnail);
        }

        if (TextUtils.isEmpty(mVideoUrl)) {
            mSimpleExoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.question_mark));
        } else {
            mVideoUri = Uri.parse(mVideoUrl);
            initializePlayer(mVideoUri);
        }

        String description = mSteps.get(selectedIndex).getDescription();
        mTextViewFullDescription.setText(description.toString());

        getActivity().setTitle(mRecipeName + " Steps");


        if (getResources().getBoolean(R.bool.twoPaneMode)){
            initializePlayer(mVideoUri);
        }else{
            int orient = getResources().getConfiguration().orientation;
            if (orient == Configuration.ORIENTATION_LANDSCAPE) {
                initFullscreenDialog();
                openFullscreenDialog();
            }
        }

        Log.d(TAG, "Showing the container");

        return view;
    }

    private void openFullscreenDialog() {

        ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
        mFullScreenDialog.addContentView(mSimpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void closeFullscreenDialog() {
        ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TOTAL_STEPS, mSteps);
        outState.putInt(STEP_INDEX, selectedIndex);
        outState.putString(RECIPE_TITLE, mRecipeName);
        outState.putLong(SELECTED_POSITION, mPlayerPosition);
        outState.putBoolean(PLAYER_READY, mPlayWhenReady);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mSimpleExoPlayerView.setPlayer(mExoPlayer);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("Cake Maker");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory, extractorsFactory,
                    null, null);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoUri != null) {
            initializePlayer(mVideoUri);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mPlayerPosition != 0) {
            mExoPlayer.seekTo(mPlayerPosition);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
