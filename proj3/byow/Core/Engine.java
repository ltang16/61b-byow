package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Font;
import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SHIFT;

/**
 * Contains the entire process of generating a world, allowing user interactivity, and enabling
 * input-determined pseudorandomness.
 */
public class Engine {

    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    private static final Path SAVEDGAME = Path.of("savedgame.txt");
    private static final Font MENUTITLE = new Font("Monaco", Font.BOLD, 60);
    private static final Font MENUOPS = new Font("Monaco", Font.BOLD, 42);
    private static final Font ENV = new Font("Monaco", Font.BOLD, 24);
    private static final Font LOS = new Font("Monaco", Font.BOLD, 16);

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT + 3, 0, 0);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(MENUTITLE);
        StdDraw.text(40, 30, "LIGHT EXPLORATION");
        StdDraw.setFont(MENUOPS);
        StdDraw.text(40, 25, "[N] New World");
        StdDraw.text(40, 20, "[L] Load World");
        StdDraw.text(40, 15, "[Q] Quit");
        StdDraw.show();
        InteractiveWorld iw = null;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char next = StdDraw.nextKeyTyped();
                next = Character.toLowerCase(next);
                if (next == 'n') {
                    StdDraw.setFont(ENV);
                    StdDraw.text(40, 10, "Please enter a seed: ");
                    StdDraw.text(40, 5, "Press 'S' when finished.");
                    StdDraw.show();
                    StringBuilder seedString = new StringBuilder(19);
                    boolean entered = false;
                    while (!entered) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char n = Character.toLowerCase(StdDraw.nextKeyTyped());
                            if (n == 's') {
                                entered = true;
                            } else if (Character.isDigit(n)) {
                                seedString.append(n);
                            }
                        }
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setFont(MENUTITLE);
                        StdDraw.text(40, 30, "LIGHT EXPLORATION");
                        StdDraw.setFont(MENUOPS);
                        StdDraw.text(40, 25, "[N] New World");
                        StdDraw.text(40, 20, "[L] Load World");
                        StdDraw.text(40, 15, "[Q] Quit");
                        StdDraw.setFont(ENV);
                        StdDraw.text(40, 10, "Please enter a seed: " + seedString.toString());
                        StdDraw.text(40, 5, "Press 'S' when finished.");
                        StdDraw.show();
                    }
                    StdDraw.clear(Color.BLACK);
                    long seed;
                    if (seedString.length() == 0) {
                        seed = 0;
                    } else {
                        seed = Long.parseLong(seedString.toString());
                    }
                    iw = new InteractiveWorld(seed);
                } else if (next == 'l') {
                    iw = loadWorld();
                } else if (next == 'q') {
                    System.exit(0);
                } else {
                    continue;
                }
                runGame(iw);
            }
        }
    }

    /**
     * Contains the entire process of accepting input from the keyboard and altering the given
     * InteractiveWorld object accordingly.
     * @param iw the given InteractiveWorld object
     */
    private void runGame(InteractiveWorld iw) {
        StdDraw.clear(Color.BLACK);
        ter.renderFrame(iw.getWorld());
        mouseInteractivity(iw);
        StdDraw.setFont(ENV);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH - 5, HEIGHT + 1, iw.getEnv().toUpperCase());
        StdDraw.setFont(LOS);
        String los;
        if (iw.isActiveLOS()) {
            los = "It's nighttime, so you need your flashlight!";
        } else {
            los = "It's daytime. You can see everything perfectly!";
        }
        StdDraw.text(40, HEIGHT + 1, los);
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char next = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (next == 'w' || next == 'a' || next == 's' || next == 'd') {
                    iw.moveInDirection(next);
                } else if (next == 't') {
                    iw.toggleLOS();
                } else if (StdDraw.isKeyPressed(VK_SHIFT) && StdDraw.isKeyPressed(VK_SEMICOLON)) {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.setFont(MENUOPS);
                    StdDraw.text(40, 25, "Quit and save world?");
                    StdDraw.setFont(ENV);
                    StdDraw.text(40, 20, "[Q] Quit and Save");
                    StdDraw.text(40, 17, "Or, press any other key to keep exploring!");
                    StdDraw.show();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char n = Character.toLowerCase(StdDraw.nextKeyTyped());
                            if (n == 'q') {
                                saveWorld(iw);
                                System.exit(0);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            mouseInteractivity(iw);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setFont(ENV);
            StdDraw.text(WIDTH - 5, HEIGHT + 1, iw.getEnv().toUpperCase());
            StdDraw.setFont(LOS);
            StdDraw.text(WIDTH - 5, HEIGHT + 2, "Location:");
            String los2;
            if (iw.isActiveLOS()) {
                los2 = "It's nighttime, so your vision is limited.";
            } else {
                los2 = "It's daytime. You can see everything perfectly!";
            }
            StdDraw.text(40, HEIGHT + 1, los2);
            StdDraw.text(40, HEIGHT + 2, "[ T ] toggle lighting mode");
            StdDraw.show();
            StdDraw.pause(10);
        }
    }

    /**
     * Creates mouse interactivity
     * Depending on the environment of your world, and the tileset your mouse is hovering over
     * this function will return whether it's hovering over a wall, floor, or an outside tile.
     * @param iw The world the mouse is hovering in
     */
    private void mouseInteractivity(InteractiveWorld iw) {
        String envType = iw.getEnv();
        int xCoor = (int) StdDraw.mouseX();
        int yCoor = (int) StdDraw.mouseY();
        ter.renderFrame(iw.getWorld());
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(LOS);
        StdDraw.text(15, HEIGHT + 2, "This is:");
        if (xCoor < WIDTH && yCoor < HEIGHT) {
            if (iw.getWorld()[xCoor][yCoor].equals(Tileset.AVATAR)) {
                StdDraw.text(15, HEIGHT + 1, "You.");
            } else if (iw.getWorld()[xCoor][yCoor].equals(Tileset.NOTHING)) {
                StdDraw.text(15, HEIGHT + 1, "The unknown.");
            } else if (iw.getWorld()[xCoor][yCoor].equals(Tileset.WALL)) {
                StdDraw.text(15, HEIGHT + 1, "A wall.");
            } else if (iw.getWorld()[xCoor][yCoor].equals(Tileset.FLOOR)) {
                StdDraw.text(15, HEIGHT + 1, "The floor.");
            } else if (iw.getWorld()[xCoor][yCoor].equals(Tileset.FLOWER)) {
                StdDraw.text(15, HEIGHT + 1, "A thick barrier of flowers.");
            } else if (iw.getWorld()[xCoor][yCoor].equals(Tileset.GRASS)) {
                if (envType.equals("meadow")) {
                    StdDraw.text(15, HEIGHT + 1, "The grass underfoot.");
                } else if (envType.equals("river")) {
                    StdDraw.text(15, HEIGHT + 1, "A grassy bank at the edge of the water.");
                }
            } else if (iw.getWorld()[xCoor][yCoor].equals(Tileset.TREE)) {
                StdDraw.text(15, HEIGHT + 1, "An imposing tree.");
            } else if (iw.getWorld()[xCoor][yCoor].equals(Tileset.WATER)) {
                if (envType.equals("river")) {
                    StdDraw.text(15, HEIGHT + 1, "The rushing water of the river.");
                } else if (envType.equals("island")) {
                    StdDraw.text(15, HEIGHT + 1, "The endless ocean.");
                }
            } else if (iw.getWorld()[xCoor][yCoor].equals(Tileset.MOUNTAIN)) {
                if (envType.equals("island")) {
                    StdDraw.text(15, HEIGHT + 1, "The rocky beach just beyond the water's edge.");
                } else if (envType.equals("castle")) {
                    StdDraw.text(15, HEIGHT + 1, "The mountains beyond the castle grounds.");
                } else {
                    StdDraw.text(15, HEIGHT + 1, "The mountainous area beyond the river.");
                }
            } else if (iw.getWorld()[xCoor][yCoor].equals(Tileset.SAND)) {
                StdDraw.text(15, HEIGHT + 1, "The soft sand of the island.");
            }
        } else {
            StdDraw.text(15, HEIGHT + 1, "The unknown.");
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        input = input.toLowerCase();
        int startIndex;
        InteractiveWorld iw;
        if (input.charAt(0) == 'n') {
            long seed = getSeedHelper(input);
            iw = new InteractiveWorld(seed);
            startIndex = input.indexOf('s') + 1;
        } else {
            startIndex = 1;
            iw = loadWorld();
        }
        for (int i = startIndex; i < input.length(); i++) {
            char nextInput = input.charAt(i);
            if (nextInput == 'w' || nextInput == 'a' || nextInput == 's' || nextInput == 'd') {
                iw.moveInDirection(nextInput);
            } else if (nextInput == 't') {
                iw.toggleLOS();
            } else if (nextInput == ':') {
                if (input.charAt(i + 1) == 'q') {
                    saveWorld(iw);
                    break;
                }
            }
        }
        return iw.getWorld();
    }

    /*Helper function to parse input and return a seed*/
    private long getSeedHelper(String input) {
        StringBuilder s = new StringBuilder();
        int i = 0;
        while (i < input.length() - 1) {
            if (input.charAt(i) == 'n') {
                i++;
            } else if (input.charAt(i) == 's') {
                break;
            }
            while (input.charAt(i) != 's') {
                s.append(input.charAt(i));
                i++;
            }
        }
        return Long.parseLong(s.toString());
    }

    /**
     * Helper method for saving the relevant details of the provided world to savedgame.txt.
     * @param iw the provided InteractiveWorld we want to save
     */
    private void saveWorld(InteractiveWorld iw) {
        try {
            String save = iw.saveContents();
            Files.writeString(SAVEDGAME, save);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Gets the necessary information to load the most-recently saved InteractiveWorld, and
     * creates an identical object from the information stored in savedgame.txt.
     * @return InteractiveWorld from savedgame.txt
     */
    private InteractiveWorld loadWorld() {
        String[] saved = new String[6];
        try {
            saved = Files.readString(SAVEDGAME).split(", ");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        long s = Long.parseLong(saved[0]);
        int x = Integer.parseInt(saved[1]);
        int y = Integer.parseInt(saved[2]);
        String dF = saved[3];
        boolean aLOS =  Boolean.parseBoolean(saved[4]);
        String eT = saved[5];
        InteractiveWorld iw = new InteractiveWorld(s, x, y, dF, aLOS, eT);
        return iw;
    }
}
