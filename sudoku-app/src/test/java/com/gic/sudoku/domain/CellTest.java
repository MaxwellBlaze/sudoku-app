package com.gic.sudoku.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    //factories - start
    @Test
    void empty_hasNoValueAndIsMutable() {
        Cell cell = Cell.empty();
        assertTrue(cell.isEmpty());
        assertFalse(cell.isPreFilled());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, -1})
    void factories_rejectOutOfRangeValues(int value) {
        assertThrows(IllegalArgumentException.class, () -> Cell.preFilled(value));
    }
    //factories - end

    //place - start
    @Test
    void place_onEmptyCell_setsValue() {
        Cell cell = Cell.empty();
        cell.place(8);
        assertEquals(8, cell.value());
    }

    @Test
    void place_onFilledCell_overwritesValue() {
        Cell cell = Cell.empty();
        cell.place(4);
        cell.place(8);
        assertEquals(8, cell.value());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    void place_outOfRange_throwsAndKeepsValue(int value) {
        Cell cell = Cell.empty();
        cell.place(4);
        assertThrows(IllegalArgumentException.class, () -> cell.place(value));
        assertEquals(4, cell.value());
    }

    @Test
    void place_onPreFilledCell_throwsAndKeepsValue() {
        Cell cell = Cell.preFilled(2);
        assertThrows(IllegalStateException.class, () -> cell.place(7));
        assertEquals(2, cell.value());
    }
    //place - end

    //clear - start
    @Test
    void clear_onFilledCell_emptiesIt() {
        Cell cell = Cell.empty();
        cell.place(4);
        cell.clear();
        assertTrue(cell.isEmpty());
    }

    @Test
    void clear_onEmptyCell_staysEmpty() {
        Cell cell = Cell.empty();
        cell.clear();
        assertTrue(cell.isEmpty());
    }

    @Test
    void clear_onPreFilledCell_throwsAndKeepsValue() {
        Cell cell = Cell.preFilled(5);
        assertThrows(IllegalStateException.class, cell::clear);
        assertEquals(5, cell.value());
    }
    //clear - end
}
