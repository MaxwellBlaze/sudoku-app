package com.gic.sudoku.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    // Sample puzzle, 0 means empty
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

    // pre-filled 5
    private static final Position A1 = new Position(0, 0);
    // empty
    private static final Position A3 = new Position(0, 2);
    // pre-filled 9 
    private static final Position I9 = new Position(8, 8);

    //fromValues - start
    @Test
    void fromValues_nonZeroValues_becomePreFilledCells() {
        Board board = Board.fromValues(PUZZLE);
        assertEquals(5, board.valueAt(A1));
        assertTrue(board.isPreFilledAt(A1));
        assertEquals(9, board.valueAt(I9));
        assertTrue(board.isPreFilledAt(I9));
    }

    @Test
    void fromValues_zeros_becomeEmptyMutableCells() {
        Board board = Board.fromValues(PUZZLE);
        assertTrue(board.isEmptyAt(A3));
        assertFalse(board.isPreFilledAt(A3));
    }

    @Test
    void fromValues_changesToInput_doNotAffectBoard() {
        int[][] values = copyOf(PUZZLE);
        Board board = Board.fromValues(values);
        values[0][2] = 4;
        assertTrue(board.isEmptyAt(A3));
    }

    @Test
    void fromValues_null_throws() {
        assertThrows(NullPointerException.class, () -> Board.fromValues(null));
    }

    @Test
    void fromValues_wrongRowCount_throws() {
        int[][] eightRows = new int[8][9];

        assertThrows(IllegalArgumentException.class, () -> Board.fromValues(eightRows));
    }

    @Test
    void fromValues_wrongColumnCount_throws() {
        int[][] values = copyOf(PUZZLE);
        values[4] = new int[10];

        assertThrows(IllegalArgumentException.class, () -> Board.fromValues(values));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 10})
    void fromValues_outOfRangeValue_throws(int invalid) {
        int[][] values = copyOf(PUZZLE);
        values[0][2] = invalid;

        assertThrows(IllegalArgumentException.class, () -> Board.fromValues(values));
    }
    //fromValues - end

    //place/clear - start
    @Test
    void place_onEmptyCell_setsValue() {
        Board board = Board.fromValues(PUZZLE);
        board.place(A3, 4);
        assertEquals(4, board.valueAt(A3));
        assertFalse(board.isEmptyAt(A3));
    }

    @Test
    void place_onUserFilledCell_overwritesValue() {
        Board board = Board.fromValues(PUZZLE);
        board.place(A3, 4);
        board.place(A3, 2);
        assertEquals(2, board.valueAt(A3));
    }

    @Test
    void place_onPreFilledCell_throwsAndKeepsValue() {
        Board board = Board.fromValues(PUZZLE);
        assertThrows(IllegalStateException.class, () -> board.place(A1, 6));
        assertEquals(5, board.valueAt(A1));
    }

    @Test
    void clear_onUserFilledCell_emptiesIt() {
        Board board = Board.fromValues(PUZZLE);
        board.place(A3, 4);
        board.clear(A3);
        assertTrue(board.isEmptyAt(A3));
    }

    @Test
    void clear_onPreFilledCell_throwsAndKeepsValue() {
        Board board = Board.fromValues(PUZZLE);
        assertThrows(IllegalStateException.class, () -> board.clear(A1));
        assertEquals(5, board.valueAt(A1));
    }

    @Test
    void valueAt_nullPosition_throws() {
        Board board = Board.fromValues(PUZZLE);
        assertThrows(NullPointerException.class, () -> board.valueAt(null));
    }
    //place/clear - end

    //toValues - start
    @Test void toValues_convertsBoardToValuesArray() {
        Board board = Board.fromValues(PUZZLE);
        assertArrayEquals(PUZZLE, board.toValues());
    }

    @Test void toValues_modifyingInputArrayDoesNotAffectBoard() {
        int[][] values = copyOf(PUZZLE);
        Board board = Board.fromValues(values);
        values[0][0] = 2;
        assertEquals(5, board.valueAt(A1));
    }
    //toValues - end

    //creates copy of input puzzle
    private static int[][] copyOf(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }
}
