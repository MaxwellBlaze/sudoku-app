package com.gic.sudoku.domain;

/** Represents each Cell on the Board */
public final class Cell {
    private static final int EMPTY = 0;
    private int value;
    private final boolean preFilled;

    private Cell(int value, boolean preFilled) {
        this.value = value;
        this.preFilled = preFilled;
    }

    public static Cell empty() { 
        return new Cell(EMPTY, false); 
    }

    public static Cell preFilled(int value) { 
        return new Cell(requireDigit(value), true);
    }

    public void place(int newValue) { 
        requireMutable();
        value = requireDigit(newValue);
    }

    public void clear() {
        requireMutable();
        value = EMPTY;
    }

    public int value() {
        return value;
    }

    public boolean isEmpty() {
        return value == EMPTY;
    }

    public boolean isPreFilled() {
        return preFilled;
    }

    private void requireMutable() {
        if (preFilled) {
            throw new IllegalStateException("Pre-filled cells cannot be changed.");
        }
    }

    private static int requireDigit(int value) {
        if (value < 1 || value > 9) {
            throw new IllegalArgumentException("Cell value must be 1-9, was " + value);
        }
        return value;
    }
}
