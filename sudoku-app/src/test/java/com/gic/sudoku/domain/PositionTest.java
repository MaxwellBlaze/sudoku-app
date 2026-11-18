package com.gic.sudoku.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @ParameterizedTest
    @CsvSource({"0,0,A1", "0,8,A9", "8,0,I1", "8,8,I9", "1,3,B4"})
    void toString_testRender(int row, int col, String expected) {
        assertEquals(expected, new Position(row, col).toString());
    }

    @ParameterizedTest
    @CsvSource({"-1,0", "9,0", "0,-1", "0,9"})
    void constructor_testWithInvalidRange(int row, int col) {
        assertThrows(IllegalArgumentException.class, () -> new Position(row, col));
    }

    @ParameterizedTest
    @CsvSource({"0,0", "8,8"})
    void constructor_testValidRange(int row, int col) {
        assertDoesNotThrow(() -> new Position(row, col));
    }
}