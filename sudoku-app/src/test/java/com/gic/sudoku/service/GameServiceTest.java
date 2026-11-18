package com.gic.sudoku.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gic.sudoku.domain.Board;
import com.gic.sudoku.domain.GameState;
import com.gic.sudoku.domain.Hint;
import com.gic.sudoku.domain.Position;
import com.gic.sudoku.domain.PuzzleGenerator;
import com.gic.sudoku.domain.Violation;
import com.gic.sudoku.domain.ViolationType;

public class GameServiceTest {

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

    private static final Position A1 = new Position(0, 0);
    private static final Position A3 = new Position(0, 2);
    private static final Position A4 = new Position(0, 3);

    private GameService service;
    private GameStore store;

    @BeforeEach
    void setUp() {
        int[][] puzzle = new int[Board.SIZE][];
        for (int row = 0; row < Board.SIZE; row++) {
            puzzle[row] = SOLUTION[row].clone();
        }
        puzzle[0][2] = 0;
        puzzle[0][3] = 0;
        store = mock(GameStore.class);
        service = new GameService(new PuzzleGenerator(new Random(1)),
        new GameState(Board.fromValues(puzzle), Board.fromValues(SOLUTION)), store);
    }

    //startNewGame - start
    @Test
    void startNewGame_replacesBoard() {
        int[][] before = service.board().toValues();
        service.startNewGame();
        assertFalse(Arrays.deepEquals(before, service.board().toValues()));
    }
    //startNewGame - end

    //fill - start
    @Test
    void fill_emptyCell_isAccepted() {
        assertEquals(new GameResult.MoveAccepted(), service.execute(new Command.Fill(A3, 9)));
        assertEquals(9, service.board().valueAt(A3));
    }

    @Test
    void fill_preFilledCell_isRejectedAndUnchanged() {
        assertEquals(new GameResult.CellPreFilled(A1), service.execute(new Command.Fill(A1, 1)));
        assertEquals(5, service.board().valueAt(A1));
    }

    @Test
    void fill_lastCellCorrectly_winGame() {
        service.execute(new Command.Fill(A3, 4));
        assertEquals(new GameResult.GameWon(), service.execute(new Command.Fill(A4, 6)));
    }

    @Test
    void fill_lastCellWrongly_doesNotWin() {
        service.execute(new Command.Fill(A3, 4));
        assertEquals(new GameResult.MoveAccepted(), service.execute(new Command.Fill(A4, 1)));
    }
    //fill - end

    //clear - start
    @Test
    void clear_userFilledCell_isCleared() {
        service.execute(new Command.Fill(A3, 9));
        assertEquals(new GameResult.CellCleared(), service.execute(new Command.Clear(A3)));
        assertTrue(service.board().isEmptyAt(A3));
    }

    @Test
    void clear_emptyCell_reportsAlreadyEmpty() {
        assertEquals(new GameResult.CellAlreadyEmpty(), service.execute(new Command.Clear(A3)));
    }

    @Test
    void clear_preFilledCell_isRejectedAndUnchanged() {
        assertEquals(new GameResult.CellPreFilled(A1), service.execute(new Command.Clear(A1)));
        assertEquals(5, service.board().valueAt(A1));
    }
    //clear - end

    //hint - start
    @Test
    void hint_returnsFirstEmptyCellWithSolutionValue() {
        GameResult expected = new GameResult.HintGiven(new Hint(A3, 4));
        assertEquals(expected, service.execute(new Command.Hint()));
    }

    @Test
    void hint_skipsWronglyFilledCell() {
        service.execute(new Command.Fill(A3, 9));
        GameResult expected = new GameResult.HintGiven(new Hint(A4, 6));
        assertEquals(expected, service.execute(new Command.Hint()));
    }

    @Test
    void hint_doesNotChangBoard() {
        service.execute(new Command.Hint());
        assertTrue(service.board().isEmptyAt(A3));
    }

    @Test
    void hint_noEmptyCells_returnsNoHint() {
        service.execute(new Command.Fill(A3, 4));
        service.execute(new Command.Fill(A4, 1));
        assertEquals(new GameResult.NoHintAvailable(), service.execute(new Command.Hint()));
    }
    //hint - end

    //check - start
    @Test
    void check_noViolations() {
        assertEquals(new GameResult.NoViolations(), service.execute(new Command.Check()));
    }

    @Test
    void check_hasViolation() {
        service.execute(new Command.Fill(A3, 9));
        GameResult expected = new GameResult.ViolationFound(new Violation(A3, 9, ViolationType.ROW));
        assertEquals(expected, service.execute(new Command.Check()));
    }
    //check - end
    
    //quit - start
    @Test
    void quit_returnQuit() {
        assertEquals(new GameResult.Quit(), service.execute(new Command.Quit()));
    }
    //quit - end

    //test persistence - start
    @Test
    void fill_savesGame() {
        service.execute(new Command.Fill(A3, 9));
        verify(store).save(any());
    }

    @Test
    void rejectedFill_doesNotSave() {
        service.execute(new Command.Fill(A1, 1));
        verify(store, never()).save(any());
    }

    @Test
    void clear_savesGame() {
        service.execute(new Command.Fill(A3, 9));
        service.execute(new Command.Clear(A3));
        verify(store, times(2)).save(any());
    }

    @Test
    void winningMove_deletesSave() {
        service.execute(new Command.Fill(A3, 4));
        //last remaining cell
        service.execute(new Command.Fill(A4, 6));
        verify(store).delete();
    }
    //test persistence - end
}
