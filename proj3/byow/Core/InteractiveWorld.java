package byow.Core;

import java.util.Random;
import byow.TileEngine.Tileset;
import byow.TileEngine.TETile;

/** Creates an object similar to RandomWorld, but with added interactivity. */
public class InteractiveWorld {

    private TETile[][] world;
    private Random r;
    private long seed;
    private Index currentPos;
    private String dirFacing;
    private String envType;
    private boolean activeLOS;
    private TETile[][] losWorld;
    private static final String[] ENVIRONMENTS = new String[]{"castle", "river", "island",
                                                              "dungeon", "meadow"};

    /**
     * Initially constructs a RandomWorld object, then takes the initialized TETile world array
     * and stores it in this object to allow interactivity. This is a "fresh start" in that it
     * only requires a seed.
     * @param s the long containing the seed to create the Random object
     */
    public InteractiveWorld(long s) {
        RandomWorld rw = new RandomWorld(s);
        world = rw.getWorld();
        r = rw.getRandom();
        seed = s;
        initializeAvatar();
        envType = ENVIRONMENTS[r.nextInt(ENVIRONMENTS.length)];
        makeEnvironment();
        dirFacing = "up";
        activeLOS = r.nextBoolean();
        losWorld = limitLOS();
    }

    /**
     * Secondary constructor for an InteractiveWorld object that's been saved and is now being
     * loaded.
     * @param s long, seed
     * @param x int, x-coordinate in TETile[][]
     * @param y int, y-coordinate in TETile[][]
     * @param dF String, direction avatar is currently facing
     * @param aLOS boolean, whether or not LOS view is toggled
     * @param eT String, containing environment type
     */
    public InteractiveWorld(long s, int x, int y, String dF, boolean aLOS, String eT) {
        seed = s;
        RandomWorld rw = new RandomWorld(s);
        world = rw.getWorld();
        r = rw.getRandom();
        currentPos = new Index(x, y);
        world[x][y] = Tileset.AVATAR;
        envType = eT;
        makeEnvironment();
        dirFacing = dF;
        activeLOS = aLOS;
        losWorld = limitLOS();
    }

    /**
     * After the world array is created, places the avatar in a random location to start.
     */
    private void initializeAvatar() {
        boolean placed = false;
        while (!placed) {
            int x = r.nextInt(Engine.WIDTH);
            int y = r.nextInt(Engine.HEIGHT);
            if (world[x][y] == Tileset.FLOOR) {
                world[x][y] = Tileset.AVATAR;
                currentPos = new Index(x, y);
                placed = true;
            }
        }
    }

    /**
     * Given user input from either of the interact methods in Engine, updates avatar's position.
     * If the avatar can't move to that location (e.g. it's a wall), nothing happens.
     * @param userChar the char representing the direction to move in. valid inputs are
     *                 one of w, a, s, d
     */
    public void moveInDirection(char userChar) {
        int x = currentPos.getX();
        int y = currentPos.getY();
        TETile floor;
        if (envType.equals("meadow")) {
            floor = Tileset.GRASS;
        } else if (envType.equals("river")) {
            floor = Tileset.WATER;
        } else if (envType.equals("island")) {
            floor = Tileset.SAND;
        } else {
            floor = Tileset.FLOOR;
        }
        if (userChar == 'w') {
            if (world[x][y + 1] == floor) {
                currentPos = new Index(x, y + 1);
                world[x][y + 1] = Tileset.AVATAR;
                world[x][y] = floor;
            }
            dirFacing = "up";
        } else if (userChar == 'a') {
            if (world[x - 1][y] == floor) {
                currentPos = new Index(x - 1, y);
                world[x - 1][y] = Tileset.AVATAR;
                world[x][y] = floor;
            }
            dirFacing = "left";
        } else if (userChar == 's') {
            if (world[x][y - 1] == floor) {
                currentPos = new Index(x, y - 1);
                world[x][y - 1] = Tileset.AVATAR;
                world[x][y] = floor;
            }
            dirFacing = "down";
        } else if (userChar == 'd') {
            if (world[x + 1][y] == floor) {
                currentPos = new Index(x + 1, y);
                world[x + 1][y] = Tileset.AVATAR;
                world[x][y] = floor;
            }
            dirFacing = "right";
        }
        losWorld = limitLOS();
    }

    /**
     * Changes the environment to a certain theme by switching each tile to a corresponding
     * appropriate tile.
     */
    private void makeEnvironment() {
        TETile wall; TETile floor; TETile outside;
        if (envType.equals("dungeon")) {
            return;
        } else if (envType.equals("meadow")) {
            wall = Tileset.FLOWER;
            floor = Tileset.GRASS;
            outside = Tileset.TREE;
        } else if (envType.equals("river")) {
            wall = Tileset.GRASS;
            floor = Tileset.WATER;
            outside = Tileset.MOUNTAIN;
        } else if (envType.equals("island")) {
            wall = Tileset.MOUNTAIN;
            floor = Tileset.SAND;
            outside = Tileset.WATER;
        } else {
            wall = Tileset.WALL;
            floor = Tileset.FLOOR;
            outside = Tileset.MOUNTAIN;
        }
        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 0; y < Engine.HEIGHT; y++) {
                if (world[x][y] == Tileset.WALL) {
                    world[x][y] = wall;
                } else if (world[x][y] == Tileset.FLOOR) {
                    world[x][y] = floor;
                } else if (world[x][y] == Tileset.NOTHING) {
                    world[x][y] = outside;
                }
            }
        }
    }

    /**
     * Returns the environment type so it can be displayed in the HUD, only for the
     * interactWithKeyboard method.
     * @return String envType
     */
    public String getEnv() {
        return envType;
    }

    /**
     * Limits the tile array to "display" only the tiles that are within the line of sight of the
     * avatar.
     */
    private TETile[][] limitLOS() {
        TETile[][] limitedWorld = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 0; y < Engine.HEIGHT; y++) {
                if (dirFacing.equals("up")) {
                    int diff = y - (currentPos.getY() - 1);
                    if (diff >= 0 && diff <= 7) {
                        if (x < (currentPos.getX() - diff) || x > (currentPos.getX() + diff)) {
                            limitedWorld[x][y] = Tileset.NOTHING;
                        } else {
                            limitedWorld[x][y] = world[x][y];
                        }
                    } else {
                        limitedWorld[x][y] = Tileset.NOTHING;
                    }
                } else if (dirFacing.equals("left")) {
                    int diff = currentPos.getX() + 1 - x;
                    if (diff >= 0 && diff <= 7) {
                        if (y < (currentPos.getY() - diff) || y > (currentPos.getY() + diff)) {
                            limitedWorld[x][y] = Tileset.NOTHING;
                        } else {
                            limitedWorld[x][y] = world[x][y];
                        }
                    } else {
                        limitedWorld[x][y] = Tileset.NOTHING;
                    }
                } else if (dirFacing.equals("down")) {
                    int diff = currentPos.getY() + 1 - y;
                    if (diff >= 0 && diff <= 7) {
                        if (x < (currentPos.getX() - diff) ||  x > (currentPos.getX() + diff)) {
                            limitedWorld[x][y] = Tileset.NOTHING;
                        } else {
                            limitedWorld[x][y] = world[x][y];
                        }
                    } else {
                        limitedWorld[x][y] = Tileset.NOTHING;
                    }
                } else {
                    int diff = x - currentPos.getX() + 1;
                    if (diff >= 0 && diff <= 7) {
                        if (y < (currentPos.getY() - diff) || y > (currentPos.getY() + diff)) {
                            limitedWorld[x][y] = Tileset.NOTHING;
                        } else {
                            limitedWorld[x][y] = world[x][y];
                        }
                    } else {
                        limitedWorld[x][y] = Tileset.NOTHING;
                    }
                }
            }
        }
        return limitedWorld;
    }

    /**
     * "Toggles" the LOS-limited version of the array by changing activeLOS to its opposite value.
     */
    public void toggleLOS() {
        boolean temp = activeLOS;
        activeLOS = !temp;
    }

    /**
     * Returns whether or not the limited-LOS version of the world is currently active. This helps
     * create an informative UI.
     * @return activeLOS
     */
    public boolean isActiveLOS() {
        return activeLOS;
    }

    /**
     * Returns the relevant world array, depending on whether or not the LOS toggle is
     * active.
     * @return TETile[][] world if !activeLOS, TETile[][] LOSworld if activeLOS
     */
    public TETile[][] getWorld() {
        if (activeLOS) {
            return losWorld;
        } else {
            return world;
        }
    }

    /**
     * Returns necessary attributes of this InteractiveWorld instance to be used in saving
     * and loading.
     * @return String containing seed, currentPos, dirFacing, activeLOS
     */
    public String saveContents() {
        String save = seed + ", " + currentPos.getX() + ", " + currentPos.getY() + ", "
                      + dirFacing + ", " + activeLOS + ", " + envType;
        return save;
    }
}
