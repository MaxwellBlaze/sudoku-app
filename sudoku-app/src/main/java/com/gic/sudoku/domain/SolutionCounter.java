package com.gic.sudoku.domain;

/** Count number of solutions via backtracking, stops once limit is reached */
public final class SolutionCounter {
    private static final int EMPTY = 0;
    private static final int NONE = -1;

    public int countSolutions(int[][] grid, int limit) {
        int index = firstEmptyIndex(grid);
        //if no empty index, a complete solution has been found
        if (index == NONE) {
            return 1;
        }
        int row = index / Board.SIZE;
        int col = index % Board.SIZE;
        int found = 0;
        for (int value = 1; value <= Board.SIZE && found < limit; value++) {
            if (SudokuRules.canPlace(grid, row, col, value)) {
                grid[row][col] = value;
                found += countSolutions(grid, limit - found);
                //backtrack
                grid[row][col] = EMPTY;
            }
        }
        return found;
    }

    //Index (row * 9 + col) of the first empty cell in row-major order
    private int firstEmptyIndex(int[][] grid) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (grid[row][col] == EMPTY) {
                    //essentially convert int[9][9] index to int[81] index
                    return row * Board.SIZE + col;
                }
            }
        }
        return NONE;
    }
}