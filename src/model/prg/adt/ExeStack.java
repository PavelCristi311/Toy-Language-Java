package model.prg.adt;

import exceptions.ADTException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExeStack<T> implements MyIStack<T> {
    private final Stack<T> tail;

    public ExeStack() {
        this.tail = new Stack<>();
    }

    @Override
    public void push(T elem) {
        this.tail.push(elem);
    }

    @Override
    public T pop() throws ADTException {
        if (tail.isEmpty()) throw new ADTException("The stack is empty! ");
        return this.tail.pop();
    }

    @Override
    public boolean isEmpty() {
        return this.tail.isEmpty();
    }

    @Override
    public T top() throws ADTException {
        if (tail.isEmpty()) throw new ADTException("The stack is empty! ");
        T result = tail.pop();
        tail.push(result);
        return result;
    }

    public List<T> getReverse() {
        List<T> list = new ArrayList<>();
        Stack<T> tempStack = new Stack<>();

        // Copy elements to temp stack
        while (!tail.isEmpty()) {
            tempStack.push(tail.pop());
        }

        // Restore original stack and build list
        while (!tempStack.isEmpty()) {
            T stmt = tempStack.pop();
            list.add(stmt);
            tail.push(stmt);
        }

        return list;
    }

    @Override
    public String toString() {
        if (tail.isEmpty()) return "Execution Stack is empty! \n";
        Stack<T> copyTail = new Stack<>();
        copyTail.addAll(tail);
        StringBuilder result = new StringBuilder();
        result.append("Execution Stack: \n    ");
        for (T elem : copyTail.reversed()) {
            result.append(elem).append(" \n    ");
        }
        return result.toString();
    }
}
