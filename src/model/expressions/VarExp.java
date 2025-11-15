package model.expressions;

import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIHeap;
import model.values.IValue;

public class VarExp implements IExp {
    private final String id;

    public VarExp(String givenId) {
        id = givenId;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dict, MyIHeap<Integer, IValue> hp) {
        return dict.getValue(id);
    }

    @Override
    public IExp deepCopy() {
        return new VarExp(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
