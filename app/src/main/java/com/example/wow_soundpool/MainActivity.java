package com.example.wow_soundpool;

import android.annotation.TargetApi;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    final int[] buttonsIds = {R.id.alexstrasza, R.id.thrall, R.id.guldan, R.id.akama, R.id.archimonde, R.id.kiljaeden, R.id.sylvanas, R.id.malfurion,
            R.id.illidan, R.id.kelThuzad, R.id.khadgar, R.id.gallywix, R.id.lady_vashj, R.id.lichKing, R.id.mogu, R.id.genn, R.id.cthun, R.id.velen,
            R.id.cho, R.id.varian, R.id.deathwing, R.id.arthas, R.id.chen, R.id.tirion, R.id.voljin, R.id.nathanos, R.id.ragnaros, R.id.garrosh, R.id.kael_thas,
            R.id.jaina, R.id.grom, R.id.anduin, R.id.tauren, R.id.shaofpride};
    Button button_back;
    Animation animationRotateCenter;
    View.OnClickListener onClickListener;
    private SoundPool soundPool;
    private AssetManager assetManager;
    private int StreamID;
    private int mCount = 1;

    {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapButton(view.getId());
                playSound(loadSound(named(getResources().getResourceEntryName(view.getId()))));
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_back = findViewById(R.id.button_back);
        animationRotateCenter = AnimationUtils.loadAnimation(this, R.anim.rotate);
        button_back.startAnimation(animationRotateCenter);
        setSoundPool();

        for (int buttonsId : buttonsIds) {
            ImageButton button = findViewById(buttonsId);
            button.setOnClickListener(onClickListener);

            System.out.println(getResources().getResourceEntryName(buttonsId) + "  =>  " + (buttonsId));
        }
    }

    private void setSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            createOldSoundpool();
        } else {
            createNewSoundPool();
        }
        assetManager = getAssets();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(1).build();
    }

    @SuppressWarnings("deprecation")
    private void createOldSoundpool() {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    private void playSound(final int sound) {
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                StreamID = soundPool.play(sound, 1, 1, 1, 0, 1f);
            }
        });
    }

    private int loadSound(String fileName) {
        if (mCount > 7) {
            mCount = 1;
        }
        AssetFileDescriptor descriptor;
        String soundName = fileName + mCount + ".ogg";
        try {
            descriptor = assetManager.openFd(soundName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), " this file is gone T_T  => " + soundName, Toast.LENGTH_SHORT).show();
            return -1;
        }
        mCount++;
        return soundPool.load(descriptor, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPool.autoPause();
    }

    public void didTapButton(int id) {
        ImageButton button = findViewById(id);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        button.startAnimation(myAnim);
    }

    public String named(String id) {
        return id + "/";
    }

    public void OnSoundClick(View view) {
        Runtime.getRuntime().gc();
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce_once));
        soundPool.autoPause();
    }

    public void onBack(View view) {
        Runtime.getRuntime().gc();
        finish();
    }
}
