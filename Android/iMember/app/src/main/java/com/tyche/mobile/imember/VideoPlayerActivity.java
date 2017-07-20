package com.tyche.mobile.imember;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity
//        extends YouTubeFailureRecoveryActivity
{

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);
        //YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);


        mVideoView = (VideoView) this.findViewById(R.id.videoView);
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/"+R.raw.angkorwat));
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
        mVideoView.start();
    }

//    @Override
//    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
//        return (YouTubePlayerView) findViewById(R.id.youtube_view);
//    }
//
//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider,
//                                        YouTubePlayer youTubePlayer,
//                                        boolean wasRestored) {
////        if (!wasRestored) {
////            youTubePlayer.cueVideo("wKJ9KzGQq0w");
////
////        }
//    }
}
