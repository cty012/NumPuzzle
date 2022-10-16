package tests;

import numpuzzle.Board;
import numpuzzle.BoardImpl;
import utils.Pair;

public class TestBoard {
    public static void main(String[] args) {
        assert args.length >= 2 : "At least 2 arguments required";
        int h = Integer.parseInt(args[0]);
        int w = Integer.parseInt(args[1]);
        int shuffles = args.length >= 3 ? Integer.parseInt(args[2]) : 0;
        // USED FOR TESTING
        Board board = new BoardImpl(h, w);
        System.out.println(board.getState());
        Pair emptyCell = board.getEmptyCell();
        System.out.printf("Empty cell = (%d, %d)\n", emptyCell.x, emptyCell.y);
        // SHUFFLE
        for (int i = 0; i < shuffles; i++) {
            System.out.printf("\nShuffle %d:\n%s\n", i + 1, board.shuffle().getState());
            emptyCell = board.getEmptyCell();
            System.out.printf("Empty cell = (%d, %d)\n", emptyCell.x, emptyCell.y);
        }
    }
}
