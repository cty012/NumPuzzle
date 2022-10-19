package tests;

import game.Game;
import slidingpuz.SlidingPuzGame;
import slidingpuz.SlidingPuzMove;
import utils.Helper;

import java.util.Random;

public class TestSlidingPuzzle {
    public static void main(String[] args) {
        assert args.length >= 2 : "At least 2 arguments required";
        int h = Integer.parseInt(args[0]);
        int w = Integer.parseInt(args[1]);
        int shuffles = args.length >= 3 ? Integer.parseInt(args[2]) : 0;
        int numMoves = args.length >= 4 ? Integer.parseInt(args[3]) : 0;

        // USED FOR TESTING
        Game game = new SlidingPuzGame(h, w);
        System.out.println(game.getState());

        // SHUFFLE
        Random random = new Random();
        SlidingPuzMove[] allMoves = {
                SlidingPuzMove.UP, SlidingPuzMove.DOWN,
                SlidingPuzMove.LEFT, SlidingPuzMove.RIGHT };
        String[] allMovesMsg = { "UP", "DOWN", "LEFT", "RIGHT" };

        for (int i = 0; i < shuffles; i++) {
            game.restart();
            System.out.printf("\nTest %d:\n", i + 1);
            Helper.printState(h, w, game.getState());
            System.out.println();

            // Move randomly j times
            for (int j = 0; j < numMoves; j++) {
                int moveId = random.nextInt(4);
                game.move(allMoves[moveId]);
                System.out.printf("Move %s:\n", allMovesMsg[moveId]);
                Helper.printState(h, w, game.getState());
            }
        }
    }
}
