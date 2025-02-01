package io.github.sekelenao.skprofiler.util;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Objects;
import java.util.RandomAccess;

public class ArrayView<T> extends AbstractList<T> implements Iterable<T>, RandomAccess {

    private final T[] array;

    private final int from;

    private final int to;

    public ArrayView(T[] array, int from, int to) {
        Assertions.checkPosition(from, array.length);
        Assertions.checkPosition(to, array.length);
        Assertions.isLowerOrEqualThan(from, to);
        this.array = Objects.requireNonNull(array);
        this.from = from;
        this.to = to;
    }

    @Override
    public T get(int index) {
        Objects.checkIndex(index, size());
        return array[from + index];
    }

    @Override
    public int size() {
        return to - from;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ArrayView<?> otherView && otherView.array == array
            && otherView.from == from && otherView.to == to;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array) ^ Integer.hashCode(from) ^ Integer.hashCode(to);
    }

}
