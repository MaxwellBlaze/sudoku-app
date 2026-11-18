package com.gic.sudoku.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gic.sudoku.domain.Board;
import com.gic.sudoku.domain.Position;

public class BoardRendererTest {

    // Sample puzzle from spec, 0 means empty
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

    private BoardRenderer renderer;

    @BeforeEach
    void setup() {
        renderer = new BoardRenderer();
    }

    @Test
    void render_samplePuzzle_matchesSpecFormat() {
        String expected = String.join(
            System.lineSeparator(),
            "  1 2 3 4 5 6 7 8 9",
            "A 5 3 _ _ 7 _ _ _ _",
            "B 6 _ _ 1 9 5 _ _ _",
            "C _ 9 8 _ _ _ _ 6 _",
            "D 8 _ _ _ 6 _ _ _ 3",
            "E 4 _ _ 8 _ 3 _ _ 1",
            "F 7 _ _ _ 2 _ _ _ 6",
            "G _ 6 _ _ _ _ 2 8 _",
            "H _ _ _ 4 1 9 _ _ 5",
            "I _ _ _ _ 8 _ _ 7 9"
        );
        assertEquals(expected, renderer.render(Board.fromValues(PUZZLE)));
    }

    @Test
    void render_userFilledCell_showsItsValue() {
        Board board = Board.fromValues(PUZZLE);
        board.place(new Position(0, 2), 4);
        List<String> lines = renderLines(board);
        assertEquals("A 5 3 4 _ 7 _ _ _ _", lines.get(1));
    }

    @Test
    void render_clearedCell_showsUnderscore() {
        Board board = Board.fromValues(PUZZLE);
        Position a3 = new Position(0, 2);
        board.place(a3, 4);
        board.clear(a3);
        List<String> lines = renderLines(board);
        assertEquals("A 5 3 _ _ 7 _ _ _ _", lines.get(1));
    }

    @Test
    void render_fullBoard_hasNoUnderscores() {
        String rendered = renderer.render(Board.fromValues(SOLUTION));
        assertFalse(rendered.contains("_"));
    }

    //helper
    private List<String> renderLines(Board board) {
        return renderer.render(board).lines().toList();
    }
}