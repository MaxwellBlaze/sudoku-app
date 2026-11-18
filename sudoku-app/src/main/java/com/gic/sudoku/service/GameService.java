package com.gic.sudoku.service;

import java.util.Optional;

import com.gic.sudoku.domain.Board;
import com.gic.sudoku.domain.GameState;
import com.gic.sudoku.domain.Position;
import com.gic.sudoku.domain.PuzzleGenerator;
import com.gic.sudoku.domain.SudokuValidator;
import com.gic.sudoku.domain.Violation;
import com.gic.sudoku.domain.Hint;

public final class GameService {

    private final PuzzleGenerator generator;
    private GameState state;
    private final GameStore store;
    private final SudokuValidator sudokuValidator = new SudokuValidator();

    public GameService(PuzzleGenerator generator, GameState state, GameStore store) {
        this.generator = generator;
        this.state = state;
        this.store = store;
    }

    public void startNewGame() {
        state = generator.generate();
    }

    public Board board() {
        return state.board();
    }

    public GameResult execute(Command command) {
        return switch (command) {
            case Command.Fill(Position position, int value) -> fill(position, value);
            case Command.Clear(Position position) -> clear(position);
            case Command.Hint() -> hint();
            case Command.Check() -> check();
            case Command.Quit() -> new GameResult.Quit();
        };
    }

    private GameResult fill(Position position, int value) {
        if (board().isPreFilledAt(position)) {
            return new GameResult.CellPreFilled(position);
        }
        board().place(position, value);
        if (state.isWon()) {
            //delete save file once complete
            store.delete();
            return new GameResult.GameWon();
        }
        store.save(state);
        return new GameResult.MoveAccepted();
    }

    private GameResult clear(Position position) {
        if (board().isPreFilledAt(position)) {
            return new GameResult.CellPreFilled(position);
        }
        if (board().isEmptyAt(position)) {
            return new GameResult.CellAlreadyEmpty();
        }
        board().clear(position);
        store.save(state);
        return new GameResult.CellCleared();
    }

    /** Returns the answer to the first empty cell when reading in row major order.
     * Skips cells with wrong answer.
    */
    private GameResult hint() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Position position = new Position(row, col);
                if (board().isEmptyAt(position)) {
                    Hint hint = new Hint(position, state.solution().valueAt(position));
                    return new GameResult.HintGiven(hint);
                }
            }
        }
        return new GameResult.NoHintAvailable();
    }

    private GameResult check() {
        Optional<Violation> violation = sudokuValidator.findFirstViolation(board());
        if (violation.isPresent()) {
            return new GameResult.ViolationFound(violation.get());
        }
        return new GameResult.NoViolations();
    } 
}