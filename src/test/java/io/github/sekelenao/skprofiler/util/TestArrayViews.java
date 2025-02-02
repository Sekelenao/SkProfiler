package io.github.sekelenao.skprofiler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class TestArrayViews {

    @Test
    @DisplayName("Ranged view works as expected")
    void getIsWorking() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        var arrayView = ArrayViews.ranged(array, 1, 4);
        var emptyArrayView = ArrayViews.ranged(array, 2, 2);
        var arrayView1 = ArrayViews.ranged(array, 2, 3);
        assertAll(
            () -> assertEquals(2, arrayView.getFirst()),
            () -> assertEquals(3, arrayView.get(1)),
            () -> assertEquals(4, arrayView.get(2)),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> arrayView.get(3)),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> arrayView.get(-1)),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> emptyArrayView.get(0)),
            () -> assertEquals(0, emptyArrayView.size()),
            () -> assertEquals(3, arrayView.size()),
            () -> assertEquals(1, arrayView1.size()),
            () -> assertEquals(3, arrayView1.getFirst())
        );
    }


    @Test
    @DisplayName("Filtered view works as expected with various scenarios")
    void filteredViewWorksAsExpected() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        var filteredEvenNumbers = ArrayViews.filtered(array, num -> (num & 1) == 0);
        var filteredGreaterThanFive = ArrayViews.filtered(array, num -> num > 5);
        var filteredEmptyArray = ArrayViews.filtered(new Integer[0], num -> num == 1);
        var filteredAllMatch = ArrayViews.filtered(array, _ -> true);
        var filteredNoneMatch = ArrayViews.filtered(array, _ -> false);

        assertAll(
            () -> assertEquals(5, filteredEvenNumbers.size()),
            () -> assertEquals(2, filteredEvenNumbers.getFirst()),
            () -> assertEquals(4, filteredEvenNumbers.get(1)),
            () -> assertEquals(5, filteredGreaterThanFive.size()),
            () -> assertEquals(6, filteredGreaterThanFive.getFirst()),
            () -> assertEquals(0, filteredEmptyArray.size()),
            () -> assertEquals(10, filteredAllMatch.size()),
            () -> assertEquals(1, filteredAllMatch.getFirst()),
            () -> assertEquals(0, filteredNoneMatch.size()),
            () -> assertEquals(10, filteredEvenNumbers.get(4)),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> filteredAllMatch.get(11)),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> filteredAllMatch.get(-1)),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> filteredNoneMatch.get(0)),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> filteredEvenNumbers.get(5))
        );
    }
}