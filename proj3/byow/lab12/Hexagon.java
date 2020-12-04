package byow.lab12;

import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

public class Hexagon {
    private int size;
    private Position topLeft;
    private TETile tileType;
    private List<Position> tilePos = new ArrayList<>();

    public Hexagon(int size, Position topLeft, TETile tileType) {
        this.size = size;
        this.topLeft = topLeft;
        this.tileType = tileType;
        getHexagonTiles();
    }

    public int getSize() {
        return size;
    }

    public Position getTopLeft() {
        return topLeft;
    }

    public TETile getTileType() {
        return tileType;
    }

    public List<Position> getTilePos() {
        return tilePos;
    }

    private void getHexagonTiles() {
        for (int row = 0; row < 2 * size; row++) {
            Position rowStart = getRowStart(row);
            int rowLength = getRowLength(row);
            for (int i = 0; i < rowLength; i++) {
                tilePos.add(new Position(rowStart.getX() + i, rowStart.getY()));
            }
        }
    }

    /* Row is zero-indexed*/
    private int getRowLength(int row) {
        if (row < size) {
            return size + 2 * row;
        } else {
            return size + 2 * (size - 1) - (row - size) * 2;
        }
    }

    /* Row is zero-indexed*/
    private Position getRowStart(int row) {
        if (row < size) {
            int x = topLeft.getX() - row;
            int y = topLeft.getY() - row;
            return new Position(x, y);
        } else {
            int x = topLeft.getX() - (size - 1) + (row - size);
            int y = topLeft.getY() - row;
            return new Position(x, y);
        }
    }
}
