package com.zhuohan.a4.model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Zhuohan on 11/30/17.
 */

public class SimonModel extends Observable {

    private final String EMPTY_STRING = " ";
    private final String COMMA = ", ";
    private final String DEBUG_PREFIX = "[DEBUG]";
    private final String WARNING_PREFIX = "[WARNING]";
    private final String SUCCESS_START = "starting";
    private final String SUCCESS_START_ENDING = "button game";
    private final String LOSE_NOTICE = "reset length and score after loss";
    private final String NEXT_BUTTON_WARNING = "nextButton called in";
    private final String VERIFY_WARNING = "verifyButton called in";

    private final String START_STRING = "START";
    private final String HUMAN_STRING = "HUMAN";
    private final String COMPUTER_STRING = "COMPUTER";
    private final String WIN_STRING = "WIN";
    private final String LOSE_STRING = "LOSE";
    private final String UNKNOWN_STRING = "Unknown State";

    private final String INDEX = "index";
    private final String BUTTON = "button";
    private final String PUSHED = "pushed";
    private final String SEQUENCE = "sequence";
    private final String WRONG = "wrong.";
    private final String CORRECT = "correct.";
    private final String NEW_SEQUENCE = "new sequence:";
    private final String NEXT_BUTTON = "next button:";
    private final String STATE = "Simon.gameState:";
    private final String NEW_ROUND = "new round";
    private final String VERIFY_BUTTON = "verify button:";
    private final String NEW_SCORE = "new score";
    private final String NEW_LENGTH = "new length";

    public enum GameState {
        START, COMPUTER, HUMAN, LOSE, WIN;
    }

    private GameState gameState;
    private int score;
    private int length;
    private int buttons;

    private ArrayList<Integer> sequence;
    private int index;
    private int remainingNum;

    private boolean debug;

    /**
     * Initial function of Simon model.
     * @param numOfButtons The number of buttons for the game
     * @param debug whether to enable debug mode
     */
    private void init(int numOfButtons, boolean debug) {
        if (numOfButtons < 0) { numOfButtons = 1; }

        this.debug = debug;
        this.length = 1;
        this.buttons = numOfButtons;
        this.gameState = GameState.START;
        this.score = 0;
        this.sequence = new ArrayList<Integer>();
        this.remainingNum = 0;

        if (this.debug) {
            System.out.println(DEBUG_PREFIX +
                    EMPTY_STRING +
                    SUCCESS_START +
                    EMPTY_STRING +
                    this.buttons +
                    EMPTY_STRING +
                    SUCCESS_START_ENDING);
        }
    }

    /**
     * Constructor of Simon model without debug mode.
     * @param numOfButtons takes in number of buttons in the game
     */
    public SimonModel(int numOfButtons) { init(numOfButtons, false); }

    /**
     * Constructor of Simon model
     * @param numOfButtons the number of buttons
     * @param debug whether to enable debug mode
     */
    public SimonModel(int numOfButtons, boolean debug) { init(numOfButtons, debug); }

    /**
     * Gets the number of buttons in the game.
     * @return an integer that represents the number of buttons
     */
    public int getNumButtons() { return this.buttons; }

    /**
     * Gets the current score for player
     * @return an integer that represents the score
     */
    public int getScore() { return this.score; }

    public int getButton() {
        return sequence.get(index);
    }

    public int getRemainingNum() { return remainingNum; }

    /**
     * Gets the string of current state
     * @return a string that represents the current state.
     */
    public String getStateAsString() {
        switch (getGameState()) {
            case START:
                return START_STRING;
            case COMPUTER:
                return COMPUTER_STRING;
            case HUMAN:
                return HUMAN_STRING;
            case WIN:
                return WIN_STRING;
            case LOSE:
                return LOSE_STRING;
            default:
                return UNKNOWN_STRING;
        }
    }

    /**
     * Gets the current game state of the game
     * @return an enum that represents the current game state
     */
    public GameState getGameState() { return this.gameState; }

    /**
     * Starts a new round of the game
     */
    public void newRound() {
        if (this.debug) {
            System.out.println(DEBUG_PREFIX + EMPTY_STRING +
                    NEW_ROUND + COMMA + STATE + EMPTY_STRING + getStateAsString());
        }

        if (gameState == GameState.LOSE) {
            System.out.println(DEBUG_PREFIX + EMPTY_STRING + LOSE_NOTICE);
            this.length = 1;
            this.score = 0;
        }

        this.sequence.clear();

        if (debug) {
            System.out.println(DEBUG_PREFIX + EMPTY_STRING + NEW_SEQUENCE + EMPTY_STRING);
        }

        for (int i = 0; i < this.length; i++) {
            int randomIndex = (int)(Math.random() * 100) % this.buttons;
            this.sequence.add(randomIndex);
            if (debug) {
                System.out.print(DEBUG_PREFIX + EMPTY_STRING + EMPTY_STRING);
            }
        }
        if (debug) { System.out.println(); }
        this.index = 0;
        this.gameState = GameState.COMPUTER;
        this.remainingNum = this.sequence.size();
        setChangedAndNotify();
    }

    /**
     * Updates the next button for display.
     */
    public void nextButton() {
        if (this.gameState != GameState.COMPUTER) {
            System.out.println(WARNING_PREFIX + NEXT_BUTTON_WARNING +
                    EMPTY_STRING + getStateAsString());
            return;
        }

        int button = sequence.get(this.index);

        if (debug) {
            System.out.println(DEBUG_PREFIX + EMPTY_STRING +
                    NEXT_BUTTON + EMPTY_STRING +
                    INDEX + this.index + EMPTY_STRING +
                    BUTTON + EMPTY_STRING + button);
        }

        this.index++;
        this.remainingNum--;
        if (this.index >= sequence.size()) {
            this.index = 0;
            this.gameState = GameState.HUMAN;
        }
        setChangedAndNotify();
    }

    /**
     * Verifies whether the button index is correct
     * If is correct, increase the game length. Else, declare lose
     */
    public void verifyButton(int button) {
        if (gameState != GameState.HUMAN) {
            System.out.println(WARNING_PREFIX + EMPTY_STRING +
                    VERIFY_WARNING + EMPTY_STRING + getStateAsString());
            return;
        }

        boolean correct = (button == sequence.get(this.index));

        if (debug) {
            System.out.print(DEBUG_PREFIX + EMPTY_STRING +
                    VERIFY_BUTTON + EMPTY_STRING +
                    INDEX + EMPTY_STRING + this.index +
                    COMMA + EMPTY_STRING + PUSHED + EMPTY_STRING + button +
                    COMMA + EMPTY_STRING + SEQUENCE + EMPTY_STRING +
                    this.sequence.get(this.index));
        }

        index++;

        if (!correct) {
            this.gameState = GameState.LOSE;
            System.out.println(COMMA + EMPTY_STRING + WRONG);
            System.out.println(DEBUG_PREFIX + EMPTY_STRING +
                    STATE + EMPTY_STRING + getStateAsString());
            setChangedAndNotify();
            return;
        }

        if (debug) { System.out.println(COMMA + EMPTY_STRING + CORRECT); }
        if (this.index == sequence.size()) {
            this.gameState = GameState.WIN;
            this.score++;
            this.length++;

            if (debug) {
                System.out.println(DEBUG_PREFIX + EMPTY_STRING +
                        STATE + EMPTY_STRING + getStateAsString());
                System.out.println(DEBUG_PREFIX + EMPTY_STRING +
                        NEW_SCORE + EMPTY_STRING + this.score +
                        COMMA + EMPTY_STRING + NEW_LENGTH + EMPTY_STRING + this.length);
            }

            setChangedAndNotify();
        }
    }

    /**
     * Sets that the model has been changed and we
     * need to update views.
     */
    private void setChangedAndNotify() {
        setChanged();
        notifyObservers();
    }

}
