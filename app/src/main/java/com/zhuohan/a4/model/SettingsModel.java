package com.zhuohan.a4.model;

import java.util.Observable;

/**
 * Created by BillStark on 12/1/17.
 */

public class SettingsModel extends Observable {

    private final int MAX_NUM_OF_BUTTONS = 6;
    private final int MIN_NUM_OF_BUTTONS = 1;
    private final int DEFAULT_NUM_OF_BUTTONS = 4;

    public enum gameDifficulty {
        EASY, NORMAL, HARD
    }

    private static final SettingsModel modelInstance = new SettingsModel();
    public static SettingsModel getInstance() {
        return modelInstance;
    }

    private gameDifficulty difficulty;
    private int numOfButtons;

    SettingsModel() {
        this.difficulty = gameDifficulty.NORMAL;
        this.numOfButtons = DEFAULT_NUM_OF_BUTTONS;
    }

    public int getNumOfButtons() {
        return this.numOfButtons;
    }

    public gameDifficulty getDifficulty() {
        return this.difficulty;
    }

    public void changeNumOfButtons(int num) {
        if (num < MIN_NUM_OF_BUTTONS || MAX_NUM_OF_BUTTONS < num) { return; }
        this.numOfButtons = num;
        setChangedAndNotify();
    }

    public void changeDifficulty(int difficulty) {
        switch (difficulty) {
            case 0:
                this.difficulty = gameDifficulty.EASY;
                break;
            case 1:
                this.difficulty = gameDifficulty.NORMAL;
                break;
            case 2:
                this.difficulty = gameDifficulty.HARD;
                break;
            default:
                return;
        }
        setChangedAndNotify();
    }

    private void setChangedAndNotify() {
        setChanged();
        notifyObservers();
    }

}
