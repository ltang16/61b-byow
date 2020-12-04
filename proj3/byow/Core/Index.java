package byow.Core;

/** Small helper class for storing indices in TETile[][]. */
public class Index {

    private int x;
    private int y;

    /**
     * Creates an object that stores the indices in the TETile array.
     * @param col the "x-coordinate"
     * @param row the "y-coordinate"
     */
    public Index(int col, int row) {
        this.x = col;
        this.y = row;
    }

    /**
     * Returns the x-coordinate.
     * @return int x
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate.
     * @return int y
     */
    public int getY() {
        return y;
    }
}
