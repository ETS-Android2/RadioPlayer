package com.kanika.radioplayerapp.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.kanika.radioplayerapp.Network.Url;
import com.kanika.radioplayerapp.R;
import com.kanika.radioplayerapp.Service.RadioService;
import com.kanika.radioplayerapp.Ui.Fragment.HomeFragment;
import com.kanika.radioplayerapp.Ui.Fragment.PlayListFragment;
import com.kanika.radioplayerapp.databinding.ActivityMainBinding;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private int count = 0;
    private static String TAG = "MainActivity";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    private DrawableImageViewTarget imageViewTarget;
    private boolean mBound = false;
    private RadioService musicService;
    private static final int READ_PHONE_STATE_REQUEST_CODE = 22;
    private BroadcastReceiver broadcastReceiver;
    private Snackbar snackbar;
    private AudioManager audio;
    private InterstitialAd interstitialAd;

    //bind local Service to play music on background
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            RadioService.MusicBinder mServiceBinder = (RadioService.MusicBinder) iBinder;
            musicService = mServiceBinder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.exit(0);
        }
    };

    //set bottom navigation bar
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    if (getCurrentFragment() instanceof HomeFragment) {
                        return true;
                    }
                    loadFragment(HomeFragment.newInstance(), HomeFragment.class.getName());
                    binding.bottomNavView.getMenu().setGroupCheckable(0, true, true);
                    return true;

                case R.id.bottom_playlist:
                    if (getCurrentFragment() instanceof PlayListFragment) {
                        return true;
                    }
                    loadFragment(PlayListFragment.newInstance(), PlayListFragment.class.getName());
                    binding.bottomNavView.getMenu().setGroupCheckable(0, true, true);
                    return true;

            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        init();
    }

    private void init() {
        setBottomBarIconColor();
        binding.bottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(HomeFragment.newInstance(), HomeFragment.class.getName());
        binding.bottomNavView.getMenu().setGroupCheckable(0, true, true);
        imageViewTarget = new DrawableImageViewTarget(binding.gifImageView);
        snackbar = Snackbar.make(findViewById(R.id.rl_main), "No internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        processPhoneListenerPermission();

        //Display  toggle button status  as per Player status using brodcast receiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
                if (tm != null) {
                    if (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {
                        if (musicService.isPlaying()) {
                            musicService.stop();
                            binding.playStopBtn.setImageResource(R.drawable.ic_play);
                        }
                    }
                }

                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                    if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                        if (snackbar.isShown()) {
                            snackbar.dismiss();
                        }
                        binding.playStopBtn.setEnabled(true);
                    } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                        binding.playStopBtn.setEnabled(false);
                        snackbar.show();
                    }
                }
                int playerState = intent.getIntExtra("state", 0);
                if (playerState == PlaybackStateCompat.STATE_BUFFERING) {
                    Glide.with(MainActivity.this).load(R.drawable.not_playing).into(imageViewTarget);
                    binding.playStopBtn.setImageResource(R.drawable.ic_pause);
                } else if (playerState == PlaybackStateCompat.STATE_PLAYING) {
                    binding.playStopBtn.setImageResource(R.drawable.ic_pause);
                    Glide.with(MainActivity.this).load(R.drawable.playing).into(imageViewTarget);
                    int musicVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (musicVolume == 0) {
                        Toast.makeText(MainActivity.this, "Volume is muted", Toast.LENGTH_LONG).show();
                    }
                } else if (playerState == PlaybackStateCompat.STATE_PAUSED) {
                    binding.playStopBtn.setImageResource(R.drawable.ic_play);
                    Glide.with(MainActivity.this).load(R.drawable.not_playing).into(imageViewTarget);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, filter);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //tap on toogle button click event
        binding.playStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count == 1) {
                    showInterstitial(v);
                } else {
                    playStop(v);
                }

            }
        });
    }

    //set change bottom navigation selected button color
    private void setBottomBarIconColor() {
        ColorStateList iconColorStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor("#999999"),
                        Color.parseColor("#FF6200EE"),
                });

        binding.bottomNavView.setItemIconTintList(iconColorStates);
        binding.bottomNavView.setItemTextColor(iconColorStates);
    }

    //get current fragment
    public Fragment getCurrentFragment() {
        if (binding.containerLayout != null && binding.containerLayout.getVisibility() == View.VISIBLE) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_layout);
            return f;
        }
        return null;
    }

    //Load FRagment
    private void loadFragment(Fragment fragment, String name) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_layout, fragment);
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(name, 0);
        if (!fragmentPopped)
            transaction.addToBackStack(name);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //register LocalBroadcastReceiver
        Intent intent = new Intent(MainActivity.this, RadioService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter("com.kanika.radioplayerapp.PLAYER_STATUS")
        );

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        loadAd();
    }

    //set toggle button status while app in background
    @Override
    protected void onResume() {
        super.onResume();
        if (musicService != null && musicService.isPlaying()) {
            binding.playStopBtn.setImageResource(R.drawable.ic_pause);
            Glide.with(MainActivity.this).load(R.drawable.playing).into(imageViewTarget);
        }
    }

    //unregistered receiver while app is destroyed
    @Override
    protected void onDestroy() {
        Log.i(TAG, "activity destroy called");
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        Log.i(TAG, "activity destroyed");
        super.onDestroy();
    }

    //play music
    public void playStop(View view) {
        if (!musicService.isPlaying()) {
            musicService.play(Url.AUDIO_STREAM_URL);
        } else {
            musicService.stop();
        }
        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        binding.playStopBtn.startAnimation(animFadein);
    }

    //ask for  phone read permission
    private void processPhoneListenerPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_PHONE_STATE_REQUEST_CODE) {
            if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "Permission not granted.\nThe player will not be able to pause music when phone is ringing.", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //load ads while play audio streming on tap on toggle button
    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                AD_UNIT_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MainActivity.this.interstitialAd = interstitialAd;
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                        if (!musicService.isPlaying()) {
                                            musicService.play(Url.AUDIO_STREAM_URL);
                                        } else {
                                            musicService.stop();
                                        }
                                        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                        binding.playStopBtn.startAnimation(animFadein);
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your refer
                                        // ence to null so you don't
                                        // show it a second time.
                                        MainActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        // Show the ad if it's ready.
                                        if (interstitialAd != null) {
                                            interstitialAd.show(MainActivity.this);
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;
                    }
                });

    }

    //display ads if its loded.
    private void showInterstitial(View v) {
        // Show the ad if it's ready.
        if (interstitialAd != null) {
            interstitialAd.show(this);
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    playStop(v);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }

            });
        }

    }

    //handle back pressed
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1) {
            Log.e("Home1", "======>");
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exit)
                    .setMessage(R.string.are_you_sure_want)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }).create().show();
        } else {
            String fragName = getSupportFragmentManager().getBackStackEntryAt(count - 2).getName();
            if (fragName.equalsIgnoreCase("com.kanika.radioplayerapp.Ui.Fragment.HomeFragment")) {
                binding.bottomNavView.getMenu().setGroupCheckable(0, true, true);
                binding.bottomNavView.getMenu().getItem(0).setChecked(true);
            } else if (fragName.equalsIgnoreCase("com.kanika.radioplayerapp.Ui.Fragment.PlayListFragment")) {
                binding.bottomNavView.getMenu().setGroupCheckable(0, true, true);
                binding.bottomNavView.getMenu().getItem(1).setChecked(true);
            }
            getSupportFragmentManager().popBackStack();
        }
    }


}