package com.gic.sudoku.persistence;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import com.gic.sudoku.domain.Board;
import com.gic.sudoku.domain.GameState;
import com.gic.sudoku.domain.Position;
import com.gic.sudoku.service.GameStore;

/** Saves the game by storing the original puzzle, current game state and solution */
public final class FileGameStore implements GameStore {
    private static final int SIZE = Board.SIZE;
    private static final String ROW_FORMAT = "[0-9]( [0-9]){8}";

    private final Path file;

    public FileGameStore(Path file) {
        this.file = file;
    }

    public void save(GameState game) {
        List<String> lines = new ArrayList<>();
        // original puzzle
        addGrid(lines, game.board(), true);
        lines.add("");
        // current state
        addGrid(lines, game.board(), false);
        lines.add("");
        // solution
        addGrid(lines, game.solution(), false);
        try {
            Files.createDirectories(file.toAbsolutePath().getParent());
            Files.write(file, lines);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save game to " + file, e);
        }
    }

    public Optional<GameState> load() {
        try {
            List<String> rows = Files.readAllLines(file).stream()
                    .filter(line -> !line.isBlank())
                    .toList();
            if (rows.size() != SIZE * 3 || !rows.stream().allMatch(row -> row.matches(ROW_FORMAT))) {
                return Optional.empty();
            }
            return Optional.of(toGame(rows));
            //handle missing or malformed save files
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void delete() {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not delete saved game " + file, e);
        }
    }

    private void addGrid(List<String> lines, Board board, boolean cluesOnly) {
        for (int row = 0; row < SIZE; row++) {
            StringJoiner cells = new StringJoiner(" ");
            for (int col = 0; col < SIZE; col++) {
                Position position = new Position(row, col);
                if (board.isEmptyAt(position) || (cluesOnly && !board.isPreFilledAt(position))) {
                    cells.add("0");
                } else {
                    cells.add(String.valueOf(board.valueAt(position)));
                }
            }
            lines.add(cells.toString());
        }
    }

    private int[][] gridAt(List<String> rows, int first) {
        int[][] grid = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            String[] cells = rows.get(first + row).split(" ");
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = Integer.parseInt(cells[col]);
            }
        }
        return grid;
    }

    //reconstructs the game state using the save file data
    private GameState toGame(List<String> rows) {
        //clues
        Board board = Board.fromValues(gridAt(rows, 0));
        //get current state from save file
        int[][] current = gridAt(rows, SIZE);
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Position position = new Position(row, col);
                if (!board.isPreFilledAt(position) && current[row][col] != 0) {
                    //add player inputs to board
                    board.place(position, current[row][col]);
                }
            }
        }
        //get solution from save file
        return new GameState(board, Board.fromValues(gridAt(rows, SIZE * 2)));
    }
}
