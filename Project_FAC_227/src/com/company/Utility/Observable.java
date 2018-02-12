package com.company.Utility;

public interface Observable<E> {
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
}
