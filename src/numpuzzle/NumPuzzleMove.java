package numpuzzle;

import game.Move;

public enum NumPuzzleMove implements Move {
    STAY, UP, DOWN, LEFT, RIGHT;

    @Override
    public char toChar() {
        switch (this) {
            case UP:
                return '↑';
            case DOWN:
                return '↓';
            case LEFT:
                return '←';
            case RIGHT:
                return '→';
            default:
                return '○';
        }
    }

    public NumPuzzleMove reverse() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return this;
        }
    }

    public static NumPuzzleMove fromChar(char c) {
        switch (c) {
            case '↑':
                return UP;
            case '↓':
                return DOWN;
            case '←':
                return LEFT;
            case '→':
                return RIGHT;
            default:
                return STAY;
        }
    }
}
