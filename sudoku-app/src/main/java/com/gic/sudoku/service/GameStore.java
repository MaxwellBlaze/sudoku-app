package com.gic.sudoku.service;

import java.util.Optional;
import com.gic.sudoku.domain.GameState;

public interface GameStore {
    void save(GameState game);
    Optional<GameState> load();
    void delete();
}