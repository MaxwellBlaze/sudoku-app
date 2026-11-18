package com.gic.sudoku.service;

import com.gic.sudoku.domain.Position;

public sealed interface Command {
    public record Fill(Position position, int value) implements Command {}
    public record Clear(Position position) implements Command {}
    public record Check() implements Command {}
    public record Hint() implements Command {}
    public record Quit() implements Command {}
}