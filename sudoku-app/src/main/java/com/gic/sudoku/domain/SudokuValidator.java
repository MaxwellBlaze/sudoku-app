package com.gic.sudoku.domain;

import java.util.Optional;

/** Finds first user entered cell with repeat number in row, col or 3x3 box, skips prefilled cells. */
public final class SudokuValidator {
    private static final int EMPTY = 0;

    public Optional<Violation> findFirstViolation(Board board) {
        int[][] grid = board.toValues();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Position position = new Position(row, col);
                if (board.isEmptyAt(position) || board.isPreFilledAt(position)) {
                    continue;
                }
                Optional<ViolationType> type = violationAt(grid, row, col);
                if (type.isPresent()) {
                    return Optional.of(new Violation(position, grid[row][col], type.get()));
                }
            }
        }
        return Optional.empty();
    }

    private Optional<ViolationType> violationAt(int[][] grid, int row, int col) {
        int value = grid[row][col];
        //temporarily set to empty to skip check with self
        grid[row][col] = EMPTY;
        Optional<ViolationType> type = violationType(grid, row, col, value);
        grid[row][col] = value;
        return type;
    }

    private Optional<ViolationType> violationType(int[][] grid, int row, int col, int value) {
        if (SudokuRules.inRow(grid, row, value)) {
            return Optional.of(ViolationType.ROW);
        }
        if (SudokuRules.inColumn(grid, col, value)) {
            return Optional.of(ViolationType.COLUMN);
        }
        if (SudokuRules.inBox(grid, row, col, value)) {
            return Optional.of(ViolationType.BOX);
        }
        return Optional.empty();
    }
}
