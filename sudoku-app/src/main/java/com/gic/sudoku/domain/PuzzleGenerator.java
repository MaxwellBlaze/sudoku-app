package com.gic.sudoku.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Generates a valid complete puzzle via randomised backtracking.
 * Remove cells in random order, only keep removal if puzzle still has only one unique solution.
 * Repeat until 30 clues left.
  */
public final class PuzzleGenerator {
    private static final int CELLS = Board.SIZE * Board.SIZE;
    private static final int CLUES = 30;
    //safety guardrail, generation very unlikely to fail, more for bugs
    private static final int MAX_ATTEMPTS = 10;
    private static final int EMPTY = 0;

    private final Random random;
    private final SolutionCounter counter = new SolutionCounter();

    public PuzzleGenerator(Random random) {
        this.random = random;
    }

    public GameState generate() {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int[][] grid = new int[Board.SIZE][Board.SIZE];
            fill(grid, 0);
            Board solution = Board.fromValues(grid);
            if (removeCells(grid) == CLUES) {
                return new GameState(Board.fromValues(grid), solution);
            }
        }
        throw new IllegalStateException("Could not generate a unique puzzle with " + CLUES + " clues");
    }

    //fill cells with shuffled nums
    private boolean fill(int[][] grid, int index) {
        if (index == CELLS) {
            return true;
        }
        int row = index / Board.SIZE;
        int col = index % Board.SIZE;
        for (int value : shuffledRange(1, Board.SIZE + 1)) {
            if (SudokuRules.canPlace(grid, row, col, value)) {
                grid[row][col] = value;
                if (fill(grid, index + 1)) {
                    return true;
                }
                //backtrack on invalid placement
                grid[row][col] = EMPTY;
            }
        }
        return false;
    }

    //removes clue one by one, only keeps removal if solution is still unique
    private int removeCells(int[][] grid) {
        int clues = CELLS;
        for (int index : shuffledRange(0, CELLS)) {
            if (clues == CLUES) {
                break;
            }
            int row = index / Board.SIZE;
            int col = index % Board.SIZE;
            int removed = grid[row][col];
            grid[row][col] = EMPTY;
            if (counter.countSolutions(grid, 2) == 1) {
                clues--;
            } else {
                grid[row][col] = removed;
            }
        }
        //returns number of clues left after removal
        return clues;
    }

    //helper to shuffle nums
    private List<Integer> shuffledRange(int from, int to) {
        List<Integer> values = new ArrayList<>();
        for (int value = from; value < to; value++) {
            values.add(value);
        }
        Collections.shuffle(values, random);
        return values;
    }
}