package com.gic.sudoku.domain;

/** Represents the types of violations.
 * Duplicate number in row, col or 3x3 box
 */
public enum ViolationType {
    ROW,
    COLUMN,
    BOX
}