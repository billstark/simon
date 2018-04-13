package com.zhuohan.a4;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhuohan.a4.model.SettingsModel;
import com.zhuohan.a4.model.SimonModel;

import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.content.Context;

public class GameActivity extends AppCompatActivity implements Observer {

    private final String CONTINUE = "Continue";
    private final String RETRY = "Retry";
    private final String START = "Start";

    private final float LOW_OPACITY = 0.1f;
    private final float FULL_OPACITY = 1.0f;
    private final int NUM_OF_ITERATION = 3;
    private final int ANIMATION_OFFSET = 20;
    private final int EASY_ANIM_DURATION = 700;
    private final int NORMAL_ANIM_DURATION = 500;
    private final int HARD_ANIM_DURATION = 200;

    private SimonModel simonModel;
    private SettingsModel settingsModel;
    private ArrayList<Button> buttons;
    private GridLayout layout;

    private TextView scoreText;
    private TextView score;
    private TextView hintText;
    private Button quitButton;
    private Button startButton;

    private int seq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getLayout();
        setupModel();
        createButtons();
        displayButtons();
        connectUIElements();
        update(simonModel, new Object());
    }

    public void backToMain(View view) {
        finish();
    }

    public void startClicked(View view) {
        simonModel.newRound();
    }

    private void getLayout() {
        this.layout = (GridLayout) findViewById(R.id.gameLayout);
    }

    private void setupModel() {
        settingsModel = SettingsModel.getInstance();
        simonModel = new SimonModel(settingsModel.getNumOfButtons());
        simonModel.addObserver(this);
    }

    private void createButtons() {
        buttons = new ArrayList<Button>();
        for (int i = 0; i < settingsModel.getNumOfButtons(); i++) {
            Button button = new Button(this);
            final int index = i;
            button.setId(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    simonModel.verifyButton(index);
                }
            });
            buttons.add(button);
        }
    }

    private void displayButtons() {
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(1, GridLayout.TOP),
                    GridLayout.spec(1, GridLayout.CENTER));
            params.setGravity(Gravity.CENTER_HORIZONTAL);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, FULL_OPACITY);
            button.setLayoutParams(params);
            button.setHeight(200);
            layout.addView(button);
        }
    }

    private void connectUIElements() {
        scoreText = (TextView) findViewById(R.id.scoreText);
        score = (TextView) findViewById(R.id.score);
        hintText = (TextView) findViewById(R.id.hintText);
        quitButton = (Button) findViewById(R.id.quitButton);
        startButton = (Button) findViewById(R.id.startButton);
    }

    @Override
    public void update(Observable observable, Object o) {
        SimonModel.GameState state = simonModel.getGameState();
        updateScore();
        updateButtons();
        updateGameButtons();
        updateHint();
        if (state == SimonModel.GameState.COMPUTER) {
            blink();
        }
    }

    private void updateScore() {
        int currentScore = simonModel.getScore();
        score.setText(String.valueOf(currentScore));
    }

    private void updateHint() {
        switch (simonModel.getGameState()) {
            case START:
                hintText.setText("Click START to start the game! Or you can quit by clicking QUIT");
                break;
            case COMPUTER:
                hintText.setText("Watch carefully! Number of remaining buttons: " +
                        simonModel.getRemainingNum());
                break;
            case HUMAN:
                hintText.setText("Click the buttons in the sequence that you just saw.");
                break;
            case LOSE:
                hintText.setText("Oops! Perhaps the order is not correct! You may try it again!");
                break;
            case WIN:
                hintText.setText("Great job! You have completed the current challenge! Why not try another one?");
                break;
            default:
                hintText.setText("Unknown game state.");
                break;
        }
    }

    private void updateButtons() {
        SimonModel.GameState state = simonModel.getGameState();
        switch (state) {
            case WIN:
                quitButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                startButton.setText(CONTINUE);
                break;
            case LOSE:
                quitButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                startButton.setText(RETRY);
                break;
            case START:
                quitButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                startButton.setText(START);
                break;
            default:
                quitButton.setVisibility(View.GONE);
                startButton.setVisibility(View.GONE);
                break;
        }
    }

    private void updateGameButtons() {
        SimonModel.GameState state = simonModel.getGameState();
        if (state == SimonModel.GameState.HUMAN) {
            setGameButtonsClickable(true);
            return;
        }
        setGameButtonsClickable(false);
    }

    private void setGameButtonsClickable(boolean val) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setClickable(val);
        }
    }

    private void blink() {
        Button btn = buttons.get(simonModel.getButton());
        Animation anim = new AlphaAnimation(FULL_OPACITY, LOW_OPACITY);
        setAnimationSpeed(anim);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        btn.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {

            private int count;

            @Override
            public void onAnimationStart(Animation animation) {
                count = 0;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                simonModel.nextButton();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (count > NUM_OF_ITERATION) { animation.cancel(); }
                count++;
            }
        });
    }

    private void setAnimationSpeed(Animation anim) {
        switch (settingsModel.getDifficulty()) {
            case EASY:
                anim.setDuration(EASY_ANIM_DURATION);
                anim.setStartOffset(ANIMATION_OFFSET);
                break;
            case NORMAL:
                anim.setDuration(NORMAL_ANIM_DURATION);
                anim.setStartOffset(ANIMATION_OFFSET);
                break;
            case HARD:
                anim.setDuration(HARD_ANIM_DURATION);
                anim.setStartOffset(ANIMATION_OFFSET);
                break;
            default:
        }
    }

}
