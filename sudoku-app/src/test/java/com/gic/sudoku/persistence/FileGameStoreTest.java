package com.gic.sudoku.persistence;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.gic.sudoku.domain.Board;
import com.gic.sudoku.domain.GameState;
import com.gic.sudoku.domain.Position;

public class FileGameStoreTest {

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

    //clue
    private static final Position A1 = new Position(0, 0);
    // player entry
    private static final Position A3 = new Position(0, 2);

    @TempDir
    Path dir;

    private Path file;
    private FileGameStore store;
    private GameState game;

    @BeforeEach
    void setUp() {
        // saves does not yet exist
        file = dir.resolve("saves/savegame.txt");   
        store = new FileGameStore(file);
        game = new GameState(Board.fromValues(PUZZLE), Board.fromValues(SOLUTION));
        game.board().place(A3, 4);
    }

    @Test
    void load_noSaveFile() {
        assertEquals(Optional.empty(), store.load());
    }

    @Test
    void saveThenLoad_restoresGame() {
        store.save(game);
        GameState loaded = store.load().orElseThrow();
        assertArrayEquals(game.board().toValues(), loaded.board().toValues());
        assertArrayEquals(game.solution().toValues(), loaded.solution().toValues());
        assertTrue(loaded.board().isPreFilledAt(A1));
        assertFalse(loaded.board().isPreFilledAt(A3));
    }

    @Test
    void save_writesOriginalCurrentAndSolutionBlocks() throws IOException {
        store.save(game);
        List<String> lines = Files.readAllLines(file);
        //29 lines in total, 3 x 9 lines + 2 line separators
        assertEquals(29, lines.size());
        //original puzzle
        assertEquals("5 3 0 0 7 0 0 0 0", lines.get(0));
        // current state
        assertEquals("5 3 4 0 7 0 0 0 0", lines.get(10));
        // solution
        assertEquals("5 3 4 6 7 8 9 1 2", lines.get(20));
    }

    @Test
    void save_replacesPreviousSave() {
        store.save(game);
        game.board().clear(A3);
        store.save(game);
        assertTrue(store.load().orElseThrow().board().isEmptyAt(A3));
    }

    @Test
    void delete_removesSave() {
        store.save(game);
        store.delete();
        assertFalse(Files.exists(file));
    }

    @Test
    void load_malformedFile_isEmpty() throws IOException {
        store.save(game);
        Files.writeString(file, "this is a test file");
        assertEquals(Optional.empty(), store.load());
    }
}
