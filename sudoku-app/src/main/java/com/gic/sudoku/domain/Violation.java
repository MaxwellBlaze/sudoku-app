package com.gic.sudoku.domain;

/** Represents a violation of a rule found by SudokuValidator */
public record Violation(Position position, int value, ViolationType type) {
    
}
