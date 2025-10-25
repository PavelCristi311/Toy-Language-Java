package model.values;

import model.type.BoolType;
import model.type.IType;

public class BoolValue implements IValue {
    private final boolean value;

    public BoolValue(boolean val) {
        value = val;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
