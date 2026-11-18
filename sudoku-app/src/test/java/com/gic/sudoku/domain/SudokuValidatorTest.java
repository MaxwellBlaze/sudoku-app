package com.gic.sudoku.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SudokuValidatorTest {

    //sample from spec, 0 is empty
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

    private static final Position A3 = new Position(0, 2);
    private static final Position A4 = new Position(0, 3);
    private static final Position A6 = new Position(0, 5);
    private static final Position B3 = new Position(1, 2);
    private static final Position C1 = new Position(2, 0);

    private final SudokuValidator validator = new SudokuValidator();
    private Board board;

    @BeforeEach
    void setUp() {
        board = Board.fromValues(PUZZLE);
    }

    //no violation - start
    @Test
    void noViolation_originalPuzzle() {
        assertEquals(Optional.empty(), validator.findFirstViolation(board));
    }

    @Test
    void noViolation_UserInputCorrect() {
        board.place(A3, 4);
        assertEquals(Optional.empty(), validator.findFirstViolation(board));
    }
    //no violation - end

    //check violations - start
    @Test
    void violation_duplicateInRow() {
        board.place(A3, 3);
        assertViolation(A3, 3, ViolationType.ROW);
    }

    @Test
    void violation_duplicateInColumn() {
        board.place(C1, 5);
        assertViolation(C1, 5, ViolationType.COLUMN);
    }

    @Test
    void violation_duplicateInLeftBoxOnly() {
        board.place(A3, 6);
        assertViolation(A3, 6, ViolationType.BOX);
    }

    @Test
    void violation_duplicateInMidBoxOnly() {
        board.place(A6, 1);
        assertViolation(A6, 1, ViolationType.BOX);
    }
    //check violations - end

    //priority of violations - start
    @Test
    void duplicateInColumnAndBox_reportsColumnFirst() {
        board.place(B3, 8);
        assertViolation(B3, 8, ViolationType.COLUMN);
    }

    @Test
    void clashWithLaterClue_reportsUserCellNotClue() {
        board.place(A3, 7);
        assertViolation(A3, 7, ViolationType.ROW);
    }

    @Test
    void twoClashingUserCells_reportsFirstInRowMajorOrder() {
        board.place(A3, 4);
        board.place(A4, 4);
        assertViolation(A3, 4, ViolationType.ROW);
    }

    @Test
    void severalViolations_reportsEarliestCell() {
        board.place(new Position(8, 0), 2);
        board.place(A3, 3);
        assertViolation(A3, 3, ViolationType.ROW);
    }
    //priority of violations - end

    //findFirstViolation - start
    @Test
    void findFirstViolation_doesNotChangeBoard() {
        board.place(A3, 3);
        validator.findFirstViolation(board);
        assertEquals(3, board.valueAt(A3));
    }
    //findFirstViolation - start

    private void assertViolation(Position position, int value, ViolationType type) {
        Optional<Violation> expected = Optional.of(new Violation(position, value, type));
        assertEquals(expected, validator.findFirstViolation(board));
    }
}