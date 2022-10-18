package numpuzzle;

import game.Game;
import game.Move;
import org.jetbrains.annotations.NotNull;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class NumPuzzleGame implements Game {
    private final Board board;
    private int steps;

    public NumPuzzleGame(int height, int width) {
        board = new BoardImpl(height, width);
        steps = 0;
    }

    @Override
    public void restart() {
        board.reset().shuffle();
        steps = 0;
    }

    @Override
    public void loadState(String state) {
        int h = board.getHeight();
        int w = board.getWidth();
        assert state.length() == h * w + 2 : "The state has wrong length";

        steps = state.charAt(0) * 65536 + state.charAt(1);

        for (int i = 0; i < h * w; i++) {
            int r = i / w, c = i % w;
            board.set(r, c, state.charAt(i + 2) - '0');
        }
    }

    @Override
    public String getState() {
        int h = board.getHeight();
        int w = board.getWidth();

        StringBuilder state = new StringBuilder("");
        state.append((char)(steps / 65536));
        state.append((char)(steps % 65536));

        for (int i = 0; i < h * w; i++) {
            int r = i / w, c = i % w;
            state.append((char)(board.get(r, c) + '0'));  // Add '0' for better visualization
        }
        return state.toString();
    }

    @Override
    public boolean move(@NotNull Move direction) {
        Pair emptyCell = board.getEmptyCell();
        Pair target = emptyCell.clone();
        switch ((NumPuzzleMove) direction) {
            case UP:
                target.x++;
                break;
            case DOWN:
                target.x--;
                break;
            case LEFT:
                target.y++;
                break;
            case RIGHT:
                target.y--;
                break;
            default:
                return false;
        }

        // Valid moves will result in valid row and column of target
        if (board.inBound(target.x, target.y)) {
            // Swap target and empty cell
            board.swap(target.x, target.y, emptyCell.x, emptyCell.y);
            steps++;
            return true;
        }

        return false;
    }

    @Override
    public List<Move> getValidMoves() {
        List<Move> moves = new ArrayList<>();
        Pair emptyCell = board.getEmptyCell();
        if (emptyCell.x < board.getHeight() - 1) {
            moves.add(NumPuzzleMove.UP);
        }
        if (emptyCell.x > 0) {
            moves.add(NumPuzzleMove.DOWN);
        }
        if (emptyCell.y < board.getWidth() - 1) {
            moves.add(NumPuzzleMove.LEFT);
        }
        if (emptyCell.y > 0) {
            moves.add(NumPuzzleMove.RIGHT);
        }
        return moves;
    }

    @Override
    public int evaluate() {
        // TODO: change this to a more effective heuristic function
        return steps;
    }
}
