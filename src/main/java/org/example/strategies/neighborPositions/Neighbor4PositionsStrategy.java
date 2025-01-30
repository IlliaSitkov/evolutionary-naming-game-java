package org.example.strategies.neighborPositions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.example.utils.Position;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Neighbor4PositionsStrategy implements NeighborPositionsStrategy, Serializable {

    private final boolean isDiagonal;

    @Override
    public List<Position> getNeighbourPositions(int x, int y, int rows, int cols) {
        if (isDiagonal) {
            return getDiagonalNeighbourPositions(x, y, rows, cols);
        } else {
            return getOrthogonalNeighbourPositions(x, y, rows, cols);
        }
    }

    private List<Position> getOrthogonalNeighbourPositions(int x, int y, int rows, int cols) {
        List<Position> neighbourPositions = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            if (i == 0) continue;
            int newX = x + i;
            int newY = y;
            if (isValidPosition(newX, newY, rows, cols)) {
                neighbourPositions.add(new Position(newX, newY));
            }
        }
        for (int j = -1; j <= 1; j++) {
            if (j == 0) continue;
            int newX = x;
            int newY = y + j;
            if (isValidPosition(newX, newY, rows, cols)) {
                neighbourPositions.add(new Position(newX, newY));
            }
        }
        return neighbourPositions;
    }

    private List<Position> getDiagonalNeighbourPositions(int x, int y, int rows, int cols) {
        List<Position> neighbourPositions = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            if (i == 0) continue;
            int newX = x + i;
            int newY = y + i;
            if (isValidPosition(newX, newY, rows, cols)) {
                neighbourPositions.add(new Position(newX, newY));
            }
        }
        for (int i = -1; i <= 1; i++) {
            if (i == 0) continue;
            int newX = x + i;
            int newY = y - i;
            if (isValidPosition(newX, newY, rows, cols)) {
                neighbourPositions.add(new Position(newX, newY));
            }
        }
        return neighbourPositions;   
    }

    private boolean isValidPosition(int x, int y, int rows, int cols) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }
    
}
