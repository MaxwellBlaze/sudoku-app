package com.gic.sudoku.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.gic.sudoku.domain.Board;
import com.gic.sudoku.domain.GameState;
import com.gic.sudoku.domain.PuzzleGenerator;
import com.gic.sudoku.service.GameService;
import com.gic.sudoku.service.GameStore;

public class SudokuCliTest {

    private static final String NEWLINE = System.lineSeparator();

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

    @Test
    void start_rendersRequiredMEssages() {
        String expected = String.join(
            NEWLINE,
            "Welcome to Sudoku!",
            "",
            "Here is your puzzle:",
            "  1 2 3 4 5 6 7 8 9",
            "A 5 3 _ _ 7 _ _ _ _",
            "B 6 _ _ 1 9 5 _ _ _",
            "C _ 9 8 _ _ _ _ 6 _",
            "D 8 _ _ _ 6 _ _ _ 3",
            "E 4 _ _ 8 _ 3 _ _ 1",
            "F 7 _ _ _ 2 _ _ _ 6",
            "G _ 6 _ _ _ _ 2 8 _",
            "H _ _ _ 4 1 9 _ _ 5",
            "I _ _ _ _ 8 _ _ 7 9",
            "",
            "Enter command (e.g., A3 4, C5 clear, hint, check, quit):",
            ""
        );
        assertEquals(expected, run(PUZZLE, ""));
    }

    @Test
    void fill_printsMoveAcceptedAndUpdatedGrid() {
        String output = run(PUZZLE, "A3 4");
        assertTrue(output.contains(NEWLINE + "Move accepted." + NEWLINE + NEWLINE + "Current grid:" + NEWLINE));
        assertTrue(output.contains("A 5 3 4 _ 7 _ _ _ _"));
    }

    @Test
    void fillPreFilledCell_printsError() {
        assertTrue(run(PUZZLE, "A1 6").contains("Invalid move. A1 is pre-filled."));
    }

    @Test
    void clearEmptyCell_printsAlreadyEmpty() {
        assertTrue(run(PUZZLE, "A3 clear").contains("Cell is already empty."));
    }

    @Test
    void clearCell_printsCellClearedAndUpdateGrid() {
        String output = run(PUZZLE, "A3 4" + NEWLINE + "A3 clear");
        String afterClear = output.substring(output.indexOf(Messages.CELL_CLEARED));
        assertTrue(output.contains(NEWLINE + "Cell cleared." + NEWLINE + NEWLINE + "Current grid:" + NEWLINE));
        assertTrue(afterClear.contains("A 5 3 _ _ 7 _ _ _ _"));
    }

    @Test
    void check_printsFirstViolation() {
        String output = run(PUZZLE, "A3 3" + NEWLINE + "check");

        assertTrue(output.contains("Number 3 already exists in Row A."));
    }

    @Test
    void check_noConflicts_printsNoViolations() {
        String output = run(PUZZLE, "A3 4" + NEWLINE + "check");
        assertTrue(output.contains("No rule violations detected." + NEWLINE));
    }

    @Test
    void hint_printsFirstEmptyCell() {
        assertTrue(run(PUZZLE, "hint").contains("Hint: Cell A3 = 4"));
    }

    @Test
    void hint_noEmptyCells_printsNoHintAvailable() {
        int[][] almostSolved = copyOf(SOLUTION);
        almostSolved[8][3] = 0;
        String output = run(almostSolved, "I4 1" + NEWLINE + "hint");
        assertTrue(output.contains(Messages.NO_HINT + NEWLINE));
        assertFalse(output.contains(Messages.WIN));
    }

    @Test
    void invalidInput_printsErrorAndPromptsAgain() {
        String output = run(PUZZLE, "a3 4");
        assertTrue(output.endsWith(Messages.INVALID_INPUT + NEWLINE + NEWLINE + Messages.PROMPT + NEWLINE));
    }

    @Test
    void quit_saysGoodbyeAndStopsReading() {
        String output = run(PUZZLE, "quit" + NEWLINE + "hint");
        assertTrue(output.endsWith(Messages.GOODBYE + NEWLINE));
        assertFalse(output.contains("Hint:"));
    }

    @Test
    void lastCorrectMove_winsAndStartsNewGameAfterEnter() {
        int[][] almostSolved = copyOf(SOLUTION);
        almostSolved[8][3] = 0;
        String output = run(almostSolved, "I4 2" + NEWLINE + NEWLINE);
        assertTrue(output.contains(Messages.WIN + NEWLINE + Messages.PLAY_AGAIN + NEWLINE));
        String afterWin = output.substring(output.indexOf(Messages.PLAY_AGAIN));
        assertTrue(afterWin.contains(Messages.PUZZLE_HEADER));
    }

    private String run(int[][] puzzle, String input) {
        GameStore store = mock(GameStore.class);
        GameState game = new GameState(Board.fromValues(puzzle), Board.fromValues(SOLUTION));
        GameService service = new GameService(new PuzzleGenerator(new Random(1)), game, store);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        SudokuCli cli = new SudokuCli(
                new ByteArrayInputStream(input.getBytes()),
                new PrintStream(output),
                new InputParser(),
                new BoardRenderer(),
                service);
        cli.start();
        return output.toString();
    }

    private static int[][] copyOf(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int row = 0; row < source.length; row++) {
            copy[row] = source[row].clone();
        }
        return copy;
    }
}