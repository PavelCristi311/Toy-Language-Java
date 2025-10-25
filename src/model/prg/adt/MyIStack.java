package model.prg.adt;

public interface MyIStack<T> {
    void push(T elem);

    T pop();

    boolean isEmpty();

    T top();
}

