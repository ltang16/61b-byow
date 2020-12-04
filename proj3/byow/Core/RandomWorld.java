package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.LinkedList;
import java.util.Random;

/**
 * Builds an entirely randomized world of Rooms.
 */
public class RandomWorld {

    private TETile[][] world;
    private long SEED;
    private Random RANDOM;

    public RandomWorld(long s) {
        this.SEED = s;
        this.RANDOM = new Random(this.SEED);
        makeNewWorld();
        Room start = getStartRoom();
        start.makeRoom(world);
        LinkedList<Room> rooms = new LinkedList<>();
        rooms.add(start);
        while (!rooms.isEmpty()) {
            Room cur = rooms.remove();
            sideloop:
            for (int i = 0; i < 4; i++) {
                Index bottomLeft = null; Index topLeft = null;
                Index bottomRight = null; Index topRight = null; Room newRoom;
                if (i == 0) {
                    boolean right = RANDOM.nextBoolean();
                    if (right) {
                        int x = cur.gettL().getX() + RANDOM.nextInt(cur.getLength() - 2);
                        bottomLeft = new Index(x, cur.gettR().getY());
                    } else {
                        int x = cur.gettR().getX() - RANDOM.nextInt(cur.getLength() - 2);
                        bottomRight = new Index(x, cur.gettR().getY());
                    }
                } else if (i == 1) {
                    boolean above = RANDOM.nextBoolean();
                    if (above) {
                        int y = cur.getbR().getY() + RANDOM.nextInt(cur.getHeight() - 2);
                        bottomLeft = new Index(cur.gettR().getX(), y);
                    } else {
                        int y = cur.gettR().getY() - RANDOM.nextInt(cur.getHeight() - 2);
                        topLeft = new Index(cur.gettR().getX(), y);
                    }
                } else if (i == 2) {
                    boolean right = RANDOM.nextBoolean();
                    if (right) {
                        int x = cur.getbL().getX() + RANDOM.nextInt(cur.getLength() - 2);
                        topLeft = new Index(x, cur.getbL().getY());
                    } else {
                        int x = cur.getbR().getX() - RANDOM.nextInt(cur.getLength() - 2);
                        topRight = new Index(x, cur.getbL().getY());
                    }
                } else {
                    boolean above = RANDOM.nextBoolean();
                    if (above) {
                        int y = cur.getbL().getY() + RANDOM.nextInt(cur.getHeight() - 2);
                        bottomRight = new Index(cur.gettL().getX(), y);
                    } else {
                        int y = cur.gettL().getY() - RANDOM.nextInt(cur.getHeight() - 2);
                        topRight = new Index(cur.gettL().getX(), y);
                    }
                }
                newRoom = randomRoom(bottomLeft, topLeft, bottomRight, topRight);
                int xStart = newRoom.getbL().getX();
                int xEnd = newRoom.getbR().getX();
                int yStart = newRoom.getbL().getY();
                int yEnd = newRoom.gettL().getY();
                if (i == 0) {
                    yStart += 1;
                } else if (i == 1) {
                    xStart += 1;
                } else if (i == 2) {
                    yEnd -= 1;
                } else {
                    xEnd -= 1;
                }
                for (int x = xStart; x <= xEnd; x++) {
                    for (int y = yStart; y <= yEnd; y++) {
                        if (x < 0 || x >= Engine.WIDTH || y < 0 || y >= Engine.HEIGHT) {
                            continue sideloop;
                        } else if (world[x][y] != Tileset.NOTHING) {
                            continue sideloop;
                        }
                    }
                }
                newRoom.makeRoom(world);
                makeEntrance(i, cur, newRoom);
                rooms.add(newRoom);
            }
        }
    }

    /**
     * Creates a blank world by assigning the TETile array and setting all tiles equal to
     * the NOTHING tile.
     */
    private void makeNewWorld() {
        world = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int i = 0; i < Engine.WIDTH; i++) {
            for (int j = 0; j < Engine.HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Starts off the world by generating a randomized room at a random location.
     * @return the starting Room
     */
    private Room getStartRoom() {
        Index bL = new Index(RANDOM.nextInt(Engine.WIDTH - 9), RANDOM.nextInt(Engine.HEIGHT - 9));
        int h = RANDOM.nextInt(7) + 4;
        int l = RANDOM.nextInt(7) + 4;
        Room start = new Room(bL, null, null, null, h, l);
        return start;
    }

    /**
     * Builds a Room object of random dimensions given the bottomLeft index. This can randomly
     * produce hallways or regular rooms.
     * @param bottomLeft the Index containing the bottom-left corner of the room. may be null
     * @param topLeft the Index containing the top-left corner of the room. may be null
     * @param bottomRight the Index containing the bottom-right corner of the room. may be null
     * @param topRight the Index containing the top-right corner of the room. may be null
     * @return the new Room
     */
    private Room randomRoom(Index bottomLeft, Index topLeft, Index bottomRight, Index topRight) {
        int height;
        int length;
        boolean hallway = RANDOM.nextBoolean();
        if (hallway) {
            boolean vert = RANDOM.nextBoolean();
            if (vert) {
                length = 3;
                height = RANDOM.nextInt(9) + 4;
            } else {
                height = 3;
                length = RANDOM.nextInt(9) + 4;
            }
        } else {
            height = RANDOM.nextInt(7) + 4;
            length = RANDOM.nextInt(7) + 4;
        }
        Room r = new Room(bottomLeft, topLeft, bottomRight, topRight, height, length);
        return r;
    }

    /**
     * Alters world to create an entrance at the specified location.
     * @param i int representing the "side" of the loop we're on
     * @param cur the current Room
     * @param newRoom the new Room to be made
     */
    private void makeEntrance(int i, Room cur, Room newRoom) {
        int x;
        int y;
        if (i == 0 || i == 2) {
            int leftEnd;
            int rightEnd;
            if (cur.getbL().getX() >= newRoom.getbL().getX()) {
                leftEnd = cur.getbL().getX();
            } else {
                leftEnd = newRoom.getbL().getX();
            }
            if (cur.gettR().getX() >= newRoom.gettR().getX()) {
                rightEnd = newRoom.gettR().getX();
            } else {
                rightEnd = cur.gettR().getX();
            }
            if (rightEnd - leftEnd == 2) {
                x = leftEnd + 1;
            } else {
                x = RANDOM.nextInt(rightEnd - leftEnd - 1) + leftEnd + 1;
            }
            if (i == 0) {
                y = newRoom.getbL().getY();
            } else {
                y = newRoom.gettR().getY();
            }
        } else {
            int lowerEnd;
            int upperEnd;
            if (cur.gettR().getY() >= newRoom.gettR().getY()) {
                upperEnd = newRoom.gettR().getY();
            } else {
                upperEnd = cur.gettR().getY();
            }
            if (cur.getbL().getY() >= newRoom.getbL().getY()) {
                lowerEnd = cur.getbL().getY();
            } else {
                lowerEnd = newRoom.getbL().getY();
            }
            if (upperEnd - lowerEnd == 2) {
                y = lowerEnd + 1;
            } else {
                y = RANDOM.nextInt(upperEnd - lowerEnd - 1) + lowerEnd + 1;
            }
            if (i == 1) {
                x = newRoom.getbL().getX();
            } else {
                x = newRoom.gettR().getX();
            }
        }
        world[x][y] = Tileset.FLOOR;
    }

    /**
     * Returns the (hopefully finished) TETile array.
     * @return TETile[][] world
     */
    public TETile[][] getWorld() {
        return world;
    }

    /**
     * Returns this instance's Random object for use in InteractiveWorld.
     * @return Random RANDOM
     */
    public Random getRandom() {
        return RANDOM;
    }
}
