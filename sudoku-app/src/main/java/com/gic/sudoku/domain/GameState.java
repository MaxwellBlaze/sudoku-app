package com.gic.sudoku.domain;

/** Represents the current board and the solution.
 * BoardRenderer renders and GameStore persists based on this
 */
public record GameState(Board board, Board solution) {

    //if current board matches solution, game is won
    public boolean isWon() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Position position = new Position(row, col);
                if (board.valueAt(position) != solution.valueAt(position)) {
                    return false;
                }
            }
        }
        return true;
    }
}
