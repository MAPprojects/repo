package com.company.Utility;

public interface Observer<E> {
    void notifyOnEvent(Event<E> event);
}
