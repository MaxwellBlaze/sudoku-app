package com.gic.sudoku.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.gic.sudoku.domain.Hint;
import com.gic.sudoku.domain.Position;
import com.gic.sudoku.domain.Violation;
import com.gic.sudoku.service.Command;
import com.gic.sudoku.service.GameResult;
import com.gic.sudoku.service.GameService;

/** Acts as the console adapter */
public final class SudokuCli {
    private final Scanner in;
    private final PrintStream out;
    private final InputParser parser;
    private final BoardRenderer renderer;
    private final GameService service;

    public SudokuCli(InputStream in, PrintStream out, InputParser parser, 
        BoardRenderer renderer, GameService service) {
        this.in = new Scanner(in);
        this.out = out;
        this.parser = parser;
        this.renderer = renderer;
        this.service = service;
    }

    public void start() {
        out.println(Messages.WELCOME);
        out.println();
        printGrid(Messages.PUZZLE_HEADER);

        boolean running = true;
        while (running) {
            out.println(Messages.PROMPT);
            if (!in.hasNextLine()) {
                return;
            }
            running = handle(parser.parse(in.nextLine()));
        }
    }

    //handles when game should stop or continue
    private boolean handle(ParseResult result) {
        switch (result) {
            case ParseResult.Failure(String message) -> printInfo(message);
            case ParseResult.Success(Command command) -> {
                return show(service.execute(command));
            }
        }
        return true;
    }

    private boolean show(GameResult result) {
        switch (result) {
            case GameResult.MoveAccepted() -> printMove(Messages.MOVE_ACCEPTED);
            case GameResult.CellCleared() -> printMove(Messages.CELL_CLEARED);
            case GameResult.CellAlreadyEmpty() -> printMove(Messages.CELL_ALREADY_EMPTY);
            case GameResult.CellPreFilled(Position position) -> printMove(Messages.preFilled(position));
            case GameResult.HintGiven(Hint hint) -> printInfo(Messages.hint(hint));
            case GameResult.NoHintAvailable() -> printInfo(Messages.NO_HINT);
            case GameResult.NoViolations() -> printInfo(Messages.NO_VIOLATIONS);
            case GameResult.ViolationFound(Violation violation) -> printInfo(Messages.violation(violation));
            case GameResult.GameWon() -> {
                return playAgain();
            }
            case GameResult.Quit() -> {
                out.println(Messages.GOODBYE);
                return false;
            }
        }
        return true;
    }

    private boolean playAgain() {
        printMove(Messages.MOVE_ACCEPTED);
        out.println(Messages.WIN);
        out.println(Messages.PLAY_AGAIN);
        if (!in.hasNextLine()) {
            return false;
        }
        in.nextLine();
        service.startNewGame();
        out.println();
        printGrid(Messages.PUZZLE_HEADER);
        return true;
    }

    private void printMove(String message) {
        out.println();
        out.println(message);
        out.println();
        printGrid(Messages.CURRENT_GRID_HEADER);
    }

    private void printInfo(String message) {
        out.println(message);
        out.println();
    }

    private void printGrid(String header) {
        out.println(header);
        out.println(renderer.render(service.board()));
        out.println();
    }
}