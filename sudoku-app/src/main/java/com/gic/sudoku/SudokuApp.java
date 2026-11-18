package com.gic.sudoku;

import java.nio.file.Path;
import java.util.Random;

import com.gic.sudoku.cli.BoardRenderer;
import com.gic.sudoku.cli.InputParser;
import com.gic.sudoku.cli.SudokuCli;
import com.gic.sudoku.domain.GameState;
import com.gic.sudoku.domain.PuzzleGenerator;
import com.gic.sudoku.persistence.FileGameStore;
import com.gic.sudoku.service.GameService;
import com.gic.sudoku.service.GameStore;

public class SudokuApp 
{
    public static void main(String[] args) {
        PuzzleGenerator generator = new PuzzleGenerator(new Random());
        GameStore store = new FileGameStore(Path.of("saves", "savegame.txt"));
        GameState state = store.load().orElseGet(generator::generate);
        GameService service = new GameService(generator, state, store);
        SudokuCli cli = new SudokuCli(System.in, System.out, new InputParser(), new BoardRenderer(), service);
        cli.start();
    }
}