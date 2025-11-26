package model.prg.adt;

import exceptions.ADTException;

public interface MyIStack<T> {
    void push(T elem);

    T pop() throws ADTException;

    boolean isEmpty();

    T top() throws ADTException;
}

