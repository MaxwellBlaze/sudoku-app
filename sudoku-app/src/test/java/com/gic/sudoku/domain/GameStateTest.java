package com.gic.sudoku.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateTest {
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

    private static final Position A3 = new Position(0, 2);
    private static GameState gameState;
    
    @BeforeEach
    void setup() {
        int[][] puzzle = new int[Board.SIZE][];
        for (int row = 0; row < Board.SIZE; row++) {
            puzzle[row] = SOLUTION[row].clone();
        }
        puzzle[0][2] = 0;
        gameState = new GameState(Board.fromValues(puzzle), Board.fromValues(SOLUTION));
    }

    @Test
    void isWon_withEmptyCell() {
        assertFalse(gameState.isWon());
    }

    @Test
    void isWon_withWrongValue() {
        gameState.board().place(A3, 1);
        assertFalse(gameState.isWon());
    }

    @Test
    void isWon_whenBoardMatchesSolution() {
        gameState.board().place(A3, 4);
        assertTrue(gameState.isWon());
    }
}
