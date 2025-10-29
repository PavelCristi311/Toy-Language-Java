package model.values;

import model.type.IType;

public interface IValue {
    IType getType();

    IValue deepCopy();
}
