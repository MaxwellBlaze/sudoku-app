package com.gic.sudoku.domain;

/** Sudoku rule helper to check for duplicate number in row, col or 3x3 box.
 * Used by SudokuValidator, PuzzleGenerator and SolutionCounter.
 */
final class SudokuRules {
    private static final int BOX_SIZE = 3;

    private SudokuRules() {
    
    }

    static boolean canPlace(int[][] grid, int row, int col, int value) {
        return !inRow(grid, row, value) && !inColumn(grid, col, value) && !inBox(grid, row, col, value);
    }

    static boolean inRow(int[][] grid, int row, int value) {
        for (int col = 0; col < Board.SIZE; col++) {
            if (grid[row][col] == value) {
                return true;
            }
        }
        return false;
    }

    static boolean inColumn(int[][] grid, int col, int value) {
        for (int row = 0; row < Board.SIZE; row++) {
            if (grid[row][col] == value) {
                return true;
            }
        }
        return false;
    }

    static boolean inBox(int[][] grid, int row, int col, int value) {
        int top = (row / BOX_SIZE) * BOX_SIZE;
        int left = (col / BOX_SIZE) * BOX_SIZE;
        for (int r = top; r < top + BOX_SIZE; r++) {
            for (int c = left; c < left + BOX_SIZE; c++) {
                if (grid[r][c] == value) {
                    return true;
                }
            }
        }
        return false;
    }
}