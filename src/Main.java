import game.Game;
import slidingpuz.SlidingPuzGame;
import solutions.AStarSolution;
import solutions.Solution;
import utils.Helper;

public class Main {
    public static void main(String[] args) {
        assert args.length >= 2 : "At least 2 arguments required";
        int h = Integer.parseInt(args[0]);
        int w = Integer.parseInt(args[1]);

        Game game = new SlidingPuzGame(h, w);
        game.restart();
        Helper.printState(h, w, game.getState());

        Solution solution = new AStarSolution(4 * ((h - 1) + (w - 1)));
        System.out.println(solution.solve(game, 2));
    }
}
