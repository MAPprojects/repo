package com.company.utils;

public interface Observer<E> {
    void notifyEvent(ListEvent<E> e);
}
