package utils;

public class Helper {
    /**
     * Prints the state of a Number Puzzle Game
     * @param h Height
     * @param w Width
     * @param state The state represented in String
     */
    public static void printState(int h, int w, String state) {
        assert state.length() >= h * w : "The state has wrong length";
        int steps = state.length() - h * w;

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                int val = state.charAt(r * w + c + steps) - '0';
                System.out.print("\t");
                if (val == 0) {
                    System.out.print("*");
                } else {
                    System.out.print(val);
                }
            }
            System.out.println();
        }

        System.out.printf("Moves: %s\n", steps == 0 ? "none" : state.substring(0, steps));
    }
}
