import game.Game;
import slidingpuz.SlidingPuzGame;
import solutions.AStarSolution;
import solutions.Solution;
import utils.Helper;

public class Main {
    public static void main(String[] args) {
        int h = 3, w = 3;

        // Command line arguments (if specified) will overwrite the default values
        if (args.length >= 2) {
            h = Integer.parseInt(args[0]);
            w = Integer.parseInt(args[1]);
        }

        Game game = new SlidingPuzGame(h, w);
        game.restart();
        System.out.println(game.getFormattedState());
        System.out.println(game.getState());
        System.out.println();

        Solution solution = new AStarSolution(4 * ((h - 1) + (w - 1)));
        System.out.println(solution.solve(game, 3));
    }
}
