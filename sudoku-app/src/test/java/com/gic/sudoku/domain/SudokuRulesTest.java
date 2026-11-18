package com.gic.sudoku.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SudokuRulesTest {

    //sample from spec, 0 is empty
    private static final int[][] GRID = {
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

    //inRow - start
    void inRow_valuePresent() {
        assertTrue(SudokuRules.inRow(GRID, 0, 3));    // A2 is 3
    }

    @Test
    void inRow_valueAbsent() {
        assertFalse(SudokuRules.inRow(GRID, 0, 4));
    }
    //inRow - end

    //inColumn - start
    @Test
    void inColumn_valuePresent() {
        assertTrue(SudokuRules.inColumn(GRID, 0, 5));
    }

    @Test
    void inColumn_valueAbsent() {
        assertFalse(SudokuRules.inColumn(GRID, 0, 9));
    }
    //inColumn - end

    //inBox - start
    @Test
    void inBox_leftBox_valuePresent() {
        assertTrue(SudokuRules.inBox(GRID, 0, 2, 6));
    }

    @Test
    void inBox_middleBox_valuePresent() {
        assertTrue(SudokuRules.inBox(GRID, 0, 5, 1)); 
    }

    @Test
    void inBox_rightBox_valuePresent() {
        assertTrue(SudokuRules.inBox(GRID, 8, 6, 5));
    }

    @Test
    void inBox_valueOnlyInNeighbouringBox() {
        assertFalse(SudokuRules.inBox(GRID, 0, 5, 6));
    }
    //inBox - end

    //canPlace - start
    @Test
    void canPlace_noViolation() {
        assertTrue(SudokuRules.canPlace(GRID, 0, 2, 4));
    }

    @Test
    void canPlace_rowViolation() {
        assertFalse(SudokuRules.canPlace(GRID, 0, 2, 3));
    }

    @Test
    void canPlace_columnViolation() {
        assertFalse(SudokuRules.canPlace(GRID, 2, 0, 5));
    }

    @Test
    void canPlace_boxViolation() {
        assertFalse(SudokuRules.canPlace(GRID, 0, 2, 6));
    }
    //canPLace - end
}
