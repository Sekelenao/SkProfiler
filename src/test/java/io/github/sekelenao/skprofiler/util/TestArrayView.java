package io.github.sekelenao.skprofiler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class TestArrayView {

    @Test
    @DisplayName("View works as expected")
    void getIsWorking() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayView<Integer> arrayView = new ArrayView<>(array, 1, 4);
        ArrayView<Integer> emptyArrayView = new ArrayView<>(array, 2, 2);
        ArrayView<Integer> arrayView1 = new ArrayView<>(array, 2, 3);
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
    @DisplayName("Equals and hashCode work as expected")
    void equalsAndHashCodeAreWorking() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayView<Integer> arrayView1 = new ArrayView<>(array, 1, 4);
        ArrayView<Integer> arrayView2 = new ArrayView<>(array, 1, 4);
        ArrayView<Integer> arrayView3 = new ArrayView<>(array, 2, 5);
        ArrayView<Integer> arrayViewWithDifferentArray = new ArrayView<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 1, 4);
        assertAll(
            () -> assertEquals(arrayView1, arrayView2),
            () -> assertEquals(arrayView1.hashCode(), arrayView2.hashCode()),
            () -> assertNotEquals(arrayView1, arrayView3),
            () -> assertNotEquals(arrayView1, arrayViewWithDifferentArray),
            () -> assertNotEquals(arrayView1.hashCode(), arrayView3.hashCode())
        );
    }

}