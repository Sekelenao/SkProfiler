package io.github.sekelenao.skprofiler.util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class ArrayViews {

    private ArrayViews() {
        throw new AssertionError("You cannot instantiate this class");
    }

    public static <T> List<T> ranged(T[] array, int from, int to) {
        Objects.requireNonNull(array);
        Assertions.checkPosition(from, array.length);
        Assertions.checkPosition(to, array.length);
        return new AbstractList<>() {

            @Override
            public T get(int index) {
                Objects.checkIndex(index, size());
                return array[from + index];
            }

            @Override
            public int size() {
                return to - from;
            }

        };
    }

    public static <T> List<T> filtered(T[] array, Predicate<? super T> predicate) {
        Assertions.requireNonNulls(array, predicate);
        return new AbstractList<>() {

            private final int[] filteredIndices = IntStream.range(0, array.length)
                .filter(i -> predicate.test(array[i]))
                .toArray();

            @Override
            public T get(int index) {
                Objects.checkIndex(index, size());
                return array[filteredIndices[index]];
            }

            @Override
            public int size() {
                return filteredIndices.length;
            }

        };
    }

}
