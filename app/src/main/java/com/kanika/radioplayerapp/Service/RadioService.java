package com.kanika.radioplayerapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.ShuffleOrder;
import com.google.android.exoplayer2.upstream.DefaultDataSource;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */

//used for   play music in background while app is in background.
public class RadioService extends Service {

    private static String TAG = "MusicService";
    private ConcatenatingMediaSource contentMediaSource;
    private ExoPlayer player;
    private LocalBroadcastManager broadcaster;
    private final Binder mBinder = new MusicBinder();

    @Override
    public void onCreate() {
        player = new ExoPlayer.Builder(this).build();
        broadcaster = LocalBroadcastManager.getInstance(this);
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.e("onPlayerStateChanged: ", "" + playbackState);
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    Intent intent = new Intent("com.kanika.radioplayerapp.PLAYER_STATUS");
                    intent.putExtra("state", PlaybackStateCompat.STATE_BUFFERING);
                    broadcaster.sendBroadcast(intent);
                } else if (playbackState == ExoPlayer.STATE_READY) {
                    Intent intent = new Intent("com.kanika.radioplayerapp.PLAYER_STATUS");
                    if (playWhenReady) {
                        intent.putExtra("state", PlaybackStateCompat.STATE_PLAYING);
                    } else {
                        intent.putExtra("state", PlaybackStateCompat.STATE_PAUSED);
                    }
                    broadcaster.sendBroadcast(intent);
                }
            }
        });
        super.onCreate();
    }

    public class MusicBinder extends Binder {
        public RadioService getService() {
            return RadioService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    //play song using exoplayer
    public void play(String channelUrl) {
        DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);
        contentMediaSource =
                new ConcatenatingMediaSource(
                        /* isAtomic= */ false,
                        /* useLazyPreparation= */ true,
                        new ShuffleOrder.DefaultShuffleOrder(/* length= */ 0));
        MediaItem mediaItem = new MediaItem.Builder().setUri(channelUrl).build();
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
        contentMediaSource.addMediaSource(mediaSource);
        player.setMediaSource(contentMediaSource);
        player.prepare();
        player.setPlayWhenReady(true);
    }

    //stop music
    public void stop() {
        player.setPlayWhenReady(false);
        player.stop();
    }

    //get player status
    public boolean isPlaying() {
        return player.getPlaybackState() == Player.STATE_READY;
    }


}
