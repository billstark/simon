package com.zhuohan.a4;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.SeekBar;

import com.zhuohan.a4.model.SettingsModel;

import java.util.Observable;
import java.util.Observer;


public class SettingsActivity extends AppCompatActivity implements Observer {

    private final String SETTINGS_TITLE = "Settings";
    private TextView numOfButtons;
    private TextView difficulty;
    private SeekBar buttonSeekBar;
    private SeekBar difficultySeekBar;
    private SettingsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(SETTINGS_TITLE);

        model = SettingsModel.getInstance();
        model.addObserver(this);

        numOfButtons = (TextView) findViewById(R.id.numOfButtons);
        difficulty = (TextView) findViewById(R.id.difficulty);

        buttonSeekBar = (SeekBar) findViewById(R.id.numOfButtonsSeekBar);
        difficultySeekBar = (SeekBar) findViewById(R.id.difficultySeekBar);

        setSeekBar();
        update(model, new Object());
    }

    private void setSeekBar() {
        setNumOfButtonsSeekBar();
        setDifficultySeekBar();
    }

    private void setNumOfButtonsSeekBar() {
        buttonSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                model.changeNumOfButtons(i + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setDifficultySeekBar() {
        difficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                model.changeDifficulty(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private String getDifficultyString() {
        switch (model.getDifficulty()) {
            case EASY:
                return "Easy";
            case NORMAL:
                return "Normal";
            case HARD:
                return "Hard";
            default:
                return "Unknown difficulty";
        }
    }

    private int getDifficultyInt() {
        switch (model.getDifficulty()) {
            case EASY:
                return 0;
            case NORMAL:
                return 1;
            case HARD:
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void update(Observable observable, Object o) {
        numOfButtons.setText(String.valueOf(model.getNumOfButtons()));
        difficulty.setText(getDifficultyString());
        buttonSeekBar.setProgress(model.getNumOfButtons() - 1);
        difficultySeekBar.setProgress(getDifficultyInt());
    }

}