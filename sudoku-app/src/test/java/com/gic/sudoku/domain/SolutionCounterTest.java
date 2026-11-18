package com.gic.sudoku.domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SolutionCounterTest {

    //sample from spec
    private static final int[][] PUZZLE = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    private static final int[][] SOLUTION = {
        {5, 3, 4, 6, 7, 8, 9, 1, 2},
        {6, 7, 2, 1, 9, 5, 3, 4, 8},
        {1, 9, 8, 3, 4, 2, 5, 6, 7},
        {8, 5, 9, 7, 6, 1, 4, 2, 3},
        {4, 2, 6, 8, 5, 3, 7, 9, 1},
        {7, 1, 3, 9, 2, 4, 8, 5, 6},
        {9, 6, 1, 5, 3, 7, 2, 8, 4},
        {2, 8, 7, 4, 1, 9, 6, 3, 5},
        {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    private final SolutionCounter counter = new SolutionCounter();

    @Test
    void puzzle_hasOneSolution() {
        assertEquals(1, counter.countSolutions(PUZZLE, 2));
    }

    @Test
    void solution_hasOneSolution() {
        assertEquals(1, counter.countSolutions(SOLUTION, 2));
    }

    @Test
    void emptyGrid_stopsAtLimit() {
        assertEquals(2, counter.countSolutions(new int[9][9], 2));
        assertEquals(5, counter.countSolutions(new int[9][9], 5));
    }

    @Test
    void limitSetTo1_stopsAtFirstSolution() {
        assertEquals(1, counter.countSolutions(new int[9][9], 1));
    }

    @Test
    void noValidSolution() {
        int[][] grid = copyOf(SOLUTION);
        grid[0][0] = 0;
        grid[1][1] = 5;
        assertEquals(0, counter.countSolutions(grid, 2));
    }

    @Test
    void countSolutions_doesNotChangeInput() {
        int[][] grid = copyOf(PUZZLE);
        counter.countSolutions(grid, 2);
        assertArrayEquals(PUZZLE, grid);
    }

    private static int[][] copyOf(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int row = 0; row < source.length; row++) {
            copy[row] = source[row].clone();
        }
        return copy;
    }
}
