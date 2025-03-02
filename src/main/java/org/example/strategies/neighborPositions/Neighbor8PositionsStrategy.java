package org.example.strategies.neighborPositions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.example.utils.Position;

import lombok.ToString;

/**
 * Selects 8 neighbor positions: up, down, left, right, up-right, up-left, down-right, down-left
 */
@ToString
public class Neighbor8PositionsStrategy implements NeighborPositionsStrategy, Serializable {

    @Override
    public List<Position> getNeighbourPositions(int x, int y, int rows, int cols) {
        List<Position> neighbourPositions = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newX = x + i;
                int newY = y + j;
                if (newX >= 0 && newX < cols && newY >= 0 && newY < rows) {
                    neighbourPositions.add(new Position(newX, newY));
                }
            }
        }
        return neighbourPositions;
    }
    
}
