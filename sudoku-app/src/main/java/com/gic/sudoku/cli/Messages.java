package com.gic.sudoku.cli;

import com.gic.sudoku.domain.Hint;
import com.gic.sudoku.domain.Position;
import com.gic.sudoku.domain.Violation;

/** Contains all user facing message strings */
public final class Messages {
    public static final String WELCOME = "Welcome to Sudoku!";
    public static final String PUZZLE_HEADER = "Here is your puzzle:";
    public static final String CURRENT_GRID_HEADER = "Current grid:";
    public static final String PROMPT = "Enter command (e.g., A3 4, C5 clear, hint, check, quit):";
    public static final String INVALID_INPUT = "Invalid input. Use A3 4, C5 clear, hint, check or quit.";
    public static final String MOVE_ACCEPTED = "Move accepted.";
    public static final String CELL_CLEARED = "Cell cleared.";
    public static final String CELL_ALREADY_EMPTY = "Cell is already empty.";
    public static final String NO_VIOLATIONS = "No rule violations detected.";
    public static final String NO_HINT = "No hint available. Use check to find wrong cells.";
    public static final String WIN = "You have successfully completed the Sudoku puzzle!";
    public static final String PLAY_AGAIN = "Press Enter to play again...";
    public static final String GOODBYE = "Good bye!";

    private Messages() {
    }

    public static String hint(Hint hint) {
        return "Hint: Cell " + hint.position() + " = " + hint.value();
    }

    public static String preFilled(Position position) {
        return "Invalid move. " + position + " is pre-filled.";
    }

    public static String violation(Violation violation) {
        Position position = violation.position();
        String prefix = "Number " + violation.value() + " already exists in ";
        return switch (violation.type()) {
            case ROW -> prefix + "Row " + (char) ('A' + position.row()) + ".";
            case COLUMN -> prefix + "Column " + (position.col() + 1) + ".";
            case BOX -> prefix + "the same 3x3 subgrid.";
        };
    }
}