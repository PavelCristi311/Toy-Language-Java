package model.expressions;

import model.prg.adt.MyIDictionary;
import model.values.IValue;

public class ValueExp implements IExp {
    private final IValue value;

    public ValueExp(IValue newValue) {
        this.value = newValue;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dict) {
        return value;
    }

    public String toString() {
        return value.toString();
    }
}
