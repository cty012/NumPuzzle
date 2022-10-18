import game.Game;
import numpuzzle.NumPuzzleGame;
import solutions.AStarSolution;
import solutions.Solution;
import utils.Helper;

public class Main {
    public static void main(String[] args) {
        assert args.length >= 2 : "At least 2 arguments required";
        int h = Integer.parseInt(args[0]);
        int w = Integer.parseInt(args[1]);

        Game game = new NumPuzzleGame(h, w);
        game.restart();
        Helper.printState(h, w, game.getState());

        Solution solution = new AStarSolution();
        System.out.println(solution.solve(game));
    }
}
