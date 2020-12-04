package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static Random rand = new Random(100);

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int hexagonSize = 4;
        drawHexWorld(hexagonSize, world);

        ter.renderFrame(world);
    }

    private static void drawHexWorld(int hexagonSize, TETile[][] world) {
        Position worldTopLeft = new Position(WIDTH / 2 - hexagonSize / 2, 45);
        for (int col = 0; col < 3; col++) {
            int x = worldTopLeft.getX() - (hexagonSize * 2 - 1) * col;
            int y = worldTopLeft.getY() - hexagonSize * col;
            Position columnTopLeft = new Position(x, y);
            drawColumn(columnTopLeft, world, 5 - col, hexagonSize);
        }
        for (int col = 1; col <= 2; col++) {
            int x = worldTopLeft.getX() + (hexagonSize * 2 - 1) * col;
            int y = worldTopLeft.getY() - hexagonSize * col;
            drawColumn(new Position(x, y), world, 5 - col, hexagonSize);
        }
    }

    private static void drawColumn(Position topLeft, TETile[][] world, int numTiles, int hexagonSize) {
        for (int i = 0; i < numTiles; i++) {
            Position hexTopLeft = new Position(topLeft.getX(), topLeft.getY() - i * hexagonSize * 2);
            Hexagon hex = new Hexagon(hexagonSize, hexTopLeft, randomTile());
            List<Position> hexTiles = hex.getTilePos();
            for (Position pos : hexTiles) {
                world[pos.getX()][pos.getY()] = hex.getTileType();
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = rand.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            default: return Tileset.MOUNTAIN;
        }
    }

}
