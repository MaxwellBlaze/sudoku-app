Sudoku CLI

Introduction:
A command-line Sudoku game written in Java 21. Each game is a randomly generated puzzle with 30 clues and exactly one solution. You can fill and clear cells by typing commands, ask for hints, check your grid for rule violations and quit at any time to resume later.

Environment Requirement:
- JDK 21 (any distribution)
- Maven 3.9+ (only for running tests and needs internet to download plugins, and to run the app via maven instead of jdk)

How to run the game:
- Using JDK
    macOS/Linux commands:
        "javac -d out $(find src/main/java -name "*.java")"
        "java -cp out com.gic.sudoku.SudokuApp"
    windows command prompt:
        "dir /s /b src\main\java\*.java > sources.txt"
        "javac -d out @sources.txt"
        "java -cp out com.gic.sudoku.SudokuApp"
- Using Maven
    "mvn compile exec:java"

How to run tests:
- Use Maven
    "mvn test"
Use these commands to open coverage report in browser
    - for mac
    "open target/site/jacoco/index.html"
    - for windows
    "start target\site\jacoco\index.html"
    - for linux
    "xdg-open target/site/jacoco/index.html"

How to play:
The grid has rows A–I and columns 1–9. _ represents an empty cell.
There are five commands: fill, clear, hint, check and quit.
Fill all the cells with the correct answer to complete the puzzle and win.
When the last cell is filled correctly, the game automatically ends and a new game can be started.
Commands are case-sensitive, and leading and trailing spaces are ignored.

App Design:
There are four layers: cli, service, persistence and domain.

cli -> service -> domain
        |
        v
    persistence

cli - handles console I/O, parsing, rendering
service - handles game rules
persistence - handles save file
domain - handles the board, rules, puzzle generation, validation

Assumptions:
- The same puzzle may be generated twice in a row, there is no check to prevent it.
- Commands are case-sensitive, leading and trailing whitespace is ignored. 
- Only the five command forms are accepted.
- hint only reveals empty cells. It doesn't place the value. Wrongly filled cells are left to check.
- check reports one violation at a time, in reading order (A1 → I9).
- The game saves whenever a number is placed or a cell is cleared, so a new game is not saved until these actions occur.
- Which cell check reports. It reports the first user-entered cell that duplicates another value in its row, column or box. Pre-filled clues are never reported, because the player can't change them.
- Order of checks. When a cell clashes in more than one way, the row is reported first, then the column, then the box.
- check detects duplicates only. A wrong value that doesn't duplicate anything passes check.
- "Press any key to play again" is implemented as "Press Enter to play again".
- Clearing an already-empty cell prints "Cell is already empty." and the grid, like other fill and clear results.
- Positions are 0-based internally (row 0 = A, column 0 = column 1). Only Position.toString() and the parser follow the spec labels.