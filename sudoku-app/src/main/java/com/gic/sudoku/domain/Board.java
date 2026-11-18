package com.gic.sudoku.domain;

/** Represents the sudoku board */
public final class Board {
    public static final int SIZE = 9;
    private static final int EMPTY = 0;
    private final Cell[][] cells = new Cell[SIZE][SIZE];

    private Board() {

    }
    
    public static Board fromValues(int[][] values) {
        if (values.length != SIZE) {
            throw new IllegalArgumentException("Board must have " + SIZE + " rows");
        }
        Board board = new Board();
        for (int row = 0; row < SIZE; row++) {
            if (values[row].length != SIZE) {
                throw new IllegalArgumentException("Each row must have " + SIZE + " columns");
            }
            for (int col = 0; col < SIZE; col++) {
                board.cells[row][col] = toPuzzleCell(values[row][col]);
            }
        }
        return board;
    }

    private static Cell toPuzzleCell(int value) {
        if (value == EMPTY) {
            return Cell.empty();
        }
        return Cell.preFilled(value);
    }

    public int valueAt(Position position) {
        return cellAt(position).value();
    }
    
    public boolean isEmptyAt(Position position) {
        return cellAt(position).isEmpty();
    }
    
    public boolean isPreFilledAt(Position position) {
        return cellAt(position).isPreFilled();
    }
    
    public void place(Position position, int value) {
        cellAt(position).place(value);
    }
    
    public void clear(Position position) {
        cellAt(position).clear();
    }

    private Cell cellAt(Position position) {
        return cells[position.row()][position.col()];
    }

    public int[][] toValues() {
        int[][] values = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                values[row][col] = cells[row][col].value();
            }
        }
        return values;
    }
}