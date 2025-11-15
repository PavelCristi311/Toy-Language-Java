package model.expressions;

import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIHeap;
import model.values.IValue;

public class ValueExp implements IExp {
    private final IValue value;

    public ValueExp(IValue newValue) {
        this.value = newValue;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dict, MyIHeap<Integer, IValue> hp) {
        return value;
    }

    @Override
    public IExp deepCopy() {
        return new ValueExp(value.deepCopy());
    }

    public String toString() {
        return value.toString();
    }
}
