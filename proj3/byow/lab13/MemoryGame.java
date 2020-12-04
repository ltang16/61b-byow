package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder s = new StringBuilder();
        while (n > 0) {
            int index = rand.nextInt(26);
            s.append(CHARACTERS[index]);
            n -= 1;
        }
        return s.toString();
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.text((double) width - 2, (double) height - 2, s);
        StdDraw.show();
        //TODO: If game is not over, display relevant game information at the top of the screen
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(Character.toString(letters.charAt(i)));
            StdDraw.pause(1000);
            StdDraw.clear(Color.BLACK);
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        StringBuilder userString = new StringBuilder();
        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                userString.append(c);
                drawFrame(userString.toString());
                n -= 1;
            }
        }
        return userString.toString();
    }

    public void startGame() {
        round = 1;
        gameOver = false;
        while (!gameOver) {
            StringBuilder r = new StringBuilder();
            r.append("Round: ");
            r.append(round);
            drawFrame(r.toString());
            StdDraw.pause(1500);
            String random = generateRandomString(round);
            flashSequence(random);
            StdDraw.show();
            String userString = solicitNCharsInput(round);
            if (random.equals(userString)) {
                drawFrame("Correct!");
                StdDraw.show();
                StdDraw.pause(1500);
                round += 1;
            } else {
                drawFrame("Game over! You made it to round " + round + ".");
                gameOver = true;
            }
        }
    }

}
