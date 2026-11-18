package com.gic.sudoku.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.gic.sudoku.domain.Hint;
import com.gic.sudoku.domain.Position;
import com.gic.sudoku.domain.Violation;
import com.gic.sudoku.domain.ViolationType;

public class MessagesTest {
    @Test
    void violation_row() {
        Violation violation = new Violation(new Position(0, 2), 3, ViolationType.ROW);
        assertEquals("Number 3 already exists in Row A.", Messages.violation(violation));
    }

    @Test
    void violation_column() {
        Violation violation = new Violation(new Position(2, 0), 5, ViolationType.COLUMN);
        assertEquals("Number 5 already exists in Column 1.", Messages.violation(violation));
    }

    @Test
    void violation_box() {
        Violation violation = new Violation(new Position(1, 2), 8, ViolationType.BOX);
        assertEquals("Number 8 already exists in the same 3x3 subgrid.", Messages.violation(violation));
    }

    @Test
    void hint() {
        assertEquals("Hint: Cell E5 = 5", Messages.hint(new Hint(new Position(4, 4), 5)));
    }

    @Test
    void preFilled() {
        assertEquals("Invalid move. A1 is pre-filled.", Messages.preFilled(new Position(0, 0)));
    }
}
