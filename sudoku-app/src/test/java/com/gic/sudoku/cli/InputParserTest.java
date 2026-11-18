package com.gic.sudoku.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.gic.sudoku.domain.Position;
import com.gic.sudoku.service.Command;

public class InputParserTest {
    
    private final InputParser parser = new InputParser();

    //valid inputs - start
    @ParameterizedTest
    @CsvSource({
        "A3 4, 0, 2, 4",
        "A1 1, 0, 0, 1",
        "A9 5, 0, 8, 5",
        "I1 7, 8, 0, 7",
        "I9 9, 8, 8, 9"
    })
    void parse_fill_returnsFillCommand(String input, int row, int col, int value) {
        Command expected = new Command.Fill(new Position(row, col), value);
        assertEquals(new ParseResult.Success(expected), parser.parse(input));
    }

    @Test
    void parse_clear_returnsClearCommand() {
        Command expected = new Command.Clear(new Position(2, 4));
        assertEquals(new ParseResult.Success(expected), parser.parse("C5 clear"));
    }

    @Test
    void parse_hint_returnsHintCommand() {
        assertEquals(new ParseResult.Success(new Command.Hint()), parser.parse("hint"));
    }

    @Test
    void parse_check_returnsCheckCommand() {
        assertEquals(new ParseResult.Success(new Command.Check()), parser.parse("check"));
    }

    @Test
    void parse_quit_returnsQuitCommand() {
        assertEquals(new ParseResult.Success(new Command.Quit()), parser.parse("quit"));
    }
    //valid inputs - end

    //leading and trailing whitespaces - start
    @Test
    void parse_leadingAndTrailingSpaces_areIgnored() {
        Command expected = new Command.Fill(new Position(0, 2), 4);
        assertEquals(new ParseResult.Success(expected), parser.parse("   A3 4   "));
    }

    @Test
    void parse_leadingAndTrailingTabsAndNewline_areIgnored() {
        assertEquals(new ParseResult.Success(new Command.Quit()), parser.parse("\tquit\n"));
    }
    //leading and trailing whitespaces - end

    //invalid inputs
    @ParameterizedTest
    @ValueSource(strings = {
            //case-sensitive
            "a3 4", "A3 CLEAR", "A3 Clear", "Hint", "CHECK", "Quit",
            //row/col invalid range
            "J1 4", "A0 4", "A10 4",
            //value invalid range
            "A3 0", "A3 10",
            //wrong format
            "A3  4", "A34", "A3 4 5", "3A 4", "A3\t4", "A3 clear 4", "A 3 4",
            //command not recognised
            "", "   ", "help", "undo", "hint please"
    })
    void parse_invalidInput_returnsFailure(String input) {
        assertEquals(new ParseResult.Failure(Messages.INVALID_INPUT), parser.parse(input));
    }
}
