package com.gic.sudoku.cli;

import com.gic.sudoku.domain.Board;
import com.gic.sudoku.domain.Position;
import java.util.StringJoiner;

/** Renders a Board according the spec format */
public final class BoardRenderer {
    private static final String HEADER = "  1 2 3 4 5 6 7 8 9";
    private static final String EMPTY_SYMBOL = "_";

    public String render(Board board) {
        StringJoiner lines = new StringJoiner(System.lineSeparator());
        lines.add(HEADER);
        for (int row = 0; row < Board.SIZE; row++) {
            lines.add(renderRow(board, row));
        }
        return lines.toString();
    }

    private String renderRow(Board board, int row) {
        StringBuilder line = new StringBuilder();
        line.append(rowLabel(row));
        for (int col = 0; col < Board.SIZE; col++) {
            line.append(' ');
            line.append(symbolAt(board, new Position(row, col)));
        }
        return line.toString();
    }

    private char rowLabel(int row) {
        return (char) ('A' + row);
    }

    private String symbolAt(Board board, Position position) {
        if (board.isEmptyAt(position)) {
            return EMPTY_SYMBOL;
        }
        return String.valueOf(board.valueAt(position));
    }
}