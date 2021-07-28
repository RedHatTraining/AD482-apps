package com.redhat.customers.producer;

import java.util.List;

public class CircularList<T> implements Iterable<T> {
    private List<T> l;

    public CircularList(List<T> l) {
        this.l = l;
    }

    @Override
    public CircularIterator<T> iterator() {
        return new CircularIterator<T>() {
            int pos = -1;

            @Override
            public boolean hasNext() {
                return !l.isEmpty();
            }

            @Override
            public T next() {
                pos = nextIndex();
                return l.get(pos);
            }

            @Override
            public int nextIndex() {
                if (pos == l.size() - 1) {
                    return 0;
                }

                return pos + 1;
            }
        };
    }
}
