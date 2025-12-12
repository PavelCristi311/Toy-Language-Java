package model.expressions;

import exceptions.TypeException;
import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIHeap;
import model.type.IType;
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

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException {
        return value.getType();
    }

    public String toString() {
        return value.toString();
    }
}
