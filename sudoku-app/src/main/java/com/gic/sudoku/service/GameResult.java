package com.gic.sudoku.service;

import com.gic.sudoku.domain.Hint;
import com.gic.sudoku.domain.Position;
import com.gic.sudoku.domain.Violation;

/** Represents outcome of executing a Command */
public sealed interface GameResult {
    record MoveAccepted() implements GameResult {}
    record CellCleared() implements GameResult { }
    record CellPreFilled(Position position) implements GameResult {}
    record CellAlreadyEmpty() implements GameResult {}
    record HintGiven(Hint hint) implements GameResult {}
    record NoHintAvailable() implements GameResult {}
    record NoViolations() implements GameResult {}
    record ViolationFound(Violation violation) implements GameResult {}
    record GameWon() implements GameResult {}
    record Quit() implements GameResult {}
}
