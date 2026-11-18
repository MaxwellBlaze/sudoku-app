package com.gic.sudoku.domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

public class PuzzleGeneratorTest {
    private final GameState game = new PuzzleGenerator(new Random(42)).generate();
    private final int[][] puzzle = game.board().toValues();
    private final int[][] solution = game.solution().toValues();

    @Test
    void puzzle_has30Clues() {
        int clues = 0;
        for (int[] row : puzzle) {
            for (int value : row) {
                if (value != 0) {
                    clues++;
                }
            }
        }
        assertEquals(30, clues);
    }

    @Test
    void clues_matchSolution() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle[row][col] != 0) {
                    assertEquals(solution[row][col], puzzle[row][col]);
                }
            }
        }
    }

    @Test
    void solution_isCompleteAndValid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = solution[row][col];
                assertTrue(value >= 1 && value <= 9, "cell must be filled");
                solution[row][col] = 0;
                assertTrue(SudokuRules.canPlace(solution, row, col, value));
                solution[row][col] = value;
            }
        }
    }

    @Test
    void puzzle_hasUniqueSolution() {
        assertEquals(1, new SolutionCounter().countSolutions(puzzle, 2));
    }

    @Test
    void sameSeed_givesSamePuzzle() {
        int[][] again = new PuzzleGenerator(new Random(42)).generate().board().toValues();
        assertArrayEquals(puzzle, again);
    }

    @Test
    void differentSeed_givesDifferentPuzzle() {
        int[][] other = new PuzzleGenerator(new Random(7)).generate().board().toValues();
        assertFalse(Arrays.deepEquals(puzzle, other));
    }
}