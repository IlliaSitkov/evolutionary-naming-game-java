package org.example.strategies.neighborPositions;

import java.util.ArrayList;
import java.util.List;

import org.example.utils.Position;

import lombok.ToString;

@ToString
public class Neighbor8PositionsStrategy implements NeighborPositionsStrategy {

    @Override
    public List<Position> getNeighbourPositions(int x, int y, int size) {
        List<Position> neighbourPositions = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newX = x + i;
                int newY = y + j;
                if (newX >= 0 && newX < size && newY >= 0 && newY < size) {
                    neighbourPositions.add(new Position(newX, newY));
                }
            }
        }
        return neighbourPositions;
    }
    
}
