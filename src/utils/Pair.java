package utils;

public class Pair {
    public int x;
    public int y;

    public Pair() {
        this(-1, -1);
    }

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Pair clone() {
        return new Pair(this.x, this.y);
    }
}
