package org.example.strategies.neighborPositions;

import java.util.List;

import org.example.utils.Position;

/**
 * Neighbor positions selection strategy
 */
public interface NeighborPositionsStrategy {
    List<Position> getNeighbourPositions(int x, int y, int rows, int cols);
}
