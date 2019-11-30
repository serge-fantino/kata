package com.kmsf.implementation;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

public class HeadTailList<T> implements Iterable<T> {

    private Optional<T> head = Optional.empty();
    private Optional<HeadTailList<T>> tail = Optional.empty();

    public HeadTailList() {}

    public HeadTailList(T head) {
        this(head, new HeadTailList<>());
    }

    protected HeadTailList(T head, HeadTailList<T> tail) {
        this.head = Optional.of(head);
        this.tail = Optional.of(tail);
    }

    public HeadTailList<T> add(T i) {
        return new HeadTailList<T>(i, this);
    }

    public T head() {
        return this.head.orElseThrow();
    }

    public HeadTailList<T> tail() {
        return tail.get();
    }

    public boolean isEmpty() {
        return head.isEmpty();
    }

    public int size() {
        if (head.isEmpty()) return 0;
        if (tail().isEmpty()) return 1;
        return 1+tail().size();
    }

    public boolean contains(T element) {
        if (head.isEmpty()) return false;
        return head.get().equals(element) || tail.map(l -> l.contains(element)).orElse(false);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private HeadTailList<T> state = HeadTailList.this;
            @Override
            public boolean hasNext() {
                return !state.isEmpty();
            }

            @Override
            public T next() {
                var head = state.head();
                state = state.tail();
                return head;
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==this) return true;
        if (!(obj instanceof HeadTailList)) return false;
        HeadTailList list = (HeadTailList) obj;
        return list.head.equals(this.head) && list.tail.equals(this.tail);
    }

    @Override
    public int hashCode() {
        if (isEmpty()) return "empty".hashCode();
        return Objects.hash(head(), tail());
    }

    @Override
    public String toString() {
        if (isEmpty()) return "";
        if (tail().isEmpty()) return head().toString();
        return tail().toString()+";"+head();
    }
}
