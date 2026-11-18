package com.gic.sudoku.cli;

import com.gic.sudoku.service.Command;

public sealed interface ParseResult {
    
    record Success(Command command) implements ParseResult {}
    record Failure(String message) implements ParseResult {}
}
