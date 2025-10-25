package model.values;

import model.type.IType;
import model.type.IntType;

public class IntValue implements IValue {
    private final int value;

    public IntValue(int val) {
        this.value = val;
    }

    @Override
    public IType getType() {
        return new IntType();
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
