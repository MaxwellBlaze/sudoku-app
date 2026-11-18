package com.gic.sudoku.domain;

/** represents a cell's cordinates */
public record Position(int row, int col) {
    public Position {
        requireInRange(row, "row");
        requireInRange(col, "col");
    }
    
    private static void requireInRange(int val, String name) {
        if(val < 0 || val > 8) {
            throw new IllegalArgumentException(name + " " + val + " " + "is out of range (0-8).");
        }
    }

    //convert cordinates to match board's format A1-I9
    @Override
    public String toString() {
        return "" + (char) ('A' + row) + (col + 1);
    }
}
