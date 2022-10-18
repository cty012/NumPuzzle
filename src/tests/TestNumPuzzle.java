package tests;

import game.Game;
import numpuzzle.NumPuzzleGame;
import numpuzzle.NumPuzzleMove;

import java.util.Random;

public class TestNumPuzzle {
    public static void printState(int h, int w, String state) {
        assert state.length() == h * w + 2 : "The state has wrong length";
        int steps = state.charAt(0) * 65536 + state.charAt(1);

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                int val = state.charAt(r * w + c + 2) - '0';
                System.out.print(" ");
                if (val == 0) {
                    System.out.print("*");
                } else {
                    System.out.print(val);
                }
            }
            System.out.println();
        }
        System.out.printf("(%d steps)\n", steps);
    }

    public static void main(String[] args) {
        assert args.length >= 2 : "At least 2 arguments required";
        int h = Integer.parseInt(args[0]);
        int w = Integer.parseInt(args[1]);
        int shuffles = args.length >= 3 ? Integer.parseInt(args[2]) : 0;
        int numMoves = args.length >= 4 ? Integer.parseInt(args[3]) : 0;
        // USED FOR TESTING
        Game game = new NumPuzzleGame(h, w);
        System.out.println(game.getState());
        // SHUFFLE
        Random random = new Random();
        NumPuzzleMove[] allMoves = {
                NumPuzzleMove.UP, NumPuzzleMove.DOWN,
                NumPuzzleMove.LEFT, NumPuzzleMove.RIGHT };
        String[] allMovesMsg = { "UP", "DOWN", "LEFT", "RIGHT" };
        for (int i = 0; i < shuffles; i++) {
            game.restart();
            System.out.printf("\nTest %d:\n", i + 1);
            printState(h, w, game.getState());
            System.out.println();
            // Move randomly j times
            for (int j = 0; j < numMoves; j++) {
                int moveId = random.nextInt(4);
                game.move(allMoves[moveId]);
                System.out.printf("Move %s:\n", allMovesMsg[moveId]);
                printState(h, w, game.getState());
            }
        }
    }
}
