package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * Constructs a room of the given (randomized) proportions, either a hallway or an actual room.
 */
public class Room {

    /** Various size/location attributes for the room. */
    private int height;
    private int length;
    private Index bottomLeft;
    private Index topLeft;
    private Index bottomRight;
    private Index topRight;

    /**
     * Constructs the room given the bottomLeft and topRight indices, and makes the room
     * by directly altering the world tile array.
     * @param bL Index locating the bottom-left corner of the room
     * @param tL Index locating the top-left corner of the room
     * @param bR Index locating the bottom-right corner of the room
     * @param tR Index locating the top-right corner of the room
     * @param h the height of the room
     * @param l the length of the room
     */
    public Room(Index bL, Index tL, Index bR, Index tR, int h, int l) {
        height = h;
        length = l;
        if (bL != null) {
            bottomLeft = bL;
            topRight = new Index(bL.getX() + l - 1, bL.getY() + h - 1);
            topLeft = new Index(bL.getX(), topRight.getY());
            bottomRight = new Index(topRight.getX(), bL.getY());
        } else if (tL != null) {
            topLeft = tL;
            bottomRight = new Index(tL.getX() + l - 1, tL.getY() - h + 1);
            topRight = new Index(bottomRight.getX(), tL.getY());
            bottomLeft = new Index(tL.getX(), bottomRight.getY());
        } else if (bR != null) {
            bottomRight = bR;
            topLeft = new Index(bR.getX() - l + 1, bR.getY() + h - 1);
            bottomLeft = new Index(topLeft.getX(), bR.getY());
            topRight = new Index(bR.getX(), topLeft.getY());
        } else {
            topRight = tR;
            bottomLeft = new Index(tR.getX() - l + 1, tR.getY() - h + 1);
            topLeft = new Index(bottomLeft.getX(), tR.getY());
            bottomRight = new Index(tR.getX(), bottomLeft.getY());
        }
    }

    /**
     * Writes the contents of the room to the TETile[][] array, therefore building it.
     * @param w the TETile array containing the full world
     */
    public void makeRoom(TETile[][] w) {
        for (int i = bottomLeft.getX(); i <= topRight.getX(); i++) {
            for (int j = bottomLeft.getY(); j <= topRight.getY(); j++) {
                if (i == bottomLeft.getX() || i == topRight.getX()
                        || j == bottomLeft.getY() || j == topRight.getY()) {
                    w[i][j] = Tileset.WALL;
                } else {
                    w[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     * Returns the height of the Room.
     * @return int height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the length of the Room.
     * @return int length
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the Index representing the bottom-left corner of the Room.
     * @return Index bottomLeft
     */
    public Index getbL() {
        return bottomLeft;
    }

    /**
     * Returns the Index representing the top-left corner of the Room.
     * @return Index topLeft
     */
    public Index gettL() {
        return topLeft;
    }

    /**
     * Returns the Index representing the bottom-right corner of the Room.
     * @return Index bottomRight
     */
    public Index getbR() {
        return bottomRight;
    }

    /**
     * Returns the Index representing the top-right corner of the Room.
     * @return Index topRight
     */
    public Index gettR() {
        return topRight;
    }
}
