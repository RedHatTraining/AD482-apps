package com.redhat.customers.producer;

import java.util.Iterator;

public interface CircularIterator<E> extends Iterator<E> {
    int nextIndex();
}
