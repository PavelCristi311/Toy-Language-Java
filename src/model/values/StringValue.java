package model.values;

import model.type.IType;
import model.type.StringType;

import java.util.Objects;

public class StringValue implements IValue {
    private final String value;

    public StringValue(String givenS) {
        value = givenS;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public IValue deepCopy() {
        return new StringValue(value);
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }

    public boolean equals(StringValue another) {
        return Objects.equals(another.getValue(), this.value);
    }
}
