package com.gic.sudoku.cli;

import com.gic.sudoku.domain.Position;
import com.gic.sudoku.service.Command;

/** Turns valid user input into a Command. 
 * Case-sensitive, trims leading and trailing whitespace.
 * Accepts only "{row}{col} {value}", {row}{col} clear", "hint", "check" and "quit"
*/
public final class InputParser {
    
    private static final String CELL_COMMAND = "[A-I][1-9] ([1-9]|clear)";
    private static final String CLEAR = "clear";

    public ParseResult parse(String input) {
        String trimmed = input.strip();
        return switch (trimmed) {
            case "hint" -> success(new Command.Hint());
            case "check" -> success(new Command.Check());
            case "quit" -> success(new Command.Quit());
            default -> parseCellCommand(trimmed);
        };
    }

    private ParseResult parseCellCommand(String input) {
        if (!input.matches(CELL_COMMAND)) {
            return invalid();
        }
        Position position = new Position(input.charAt(0) - 'A', input.charAt(1) - '1');
        String action = input.substring(3);
        if (action.equals(CLEAR)) {
            return success(new Command.Clear(position));
        }
        return success(new Command.Fill(position, Integer.parseInt(action)));
    }

    private ParseResult success(Command command) {
        return new ParseResult.Success(command);
    }

    private ParseResult invalid() {
        return new ParseResult.Failure(Messages.INVALID_INPUT);
    }
}