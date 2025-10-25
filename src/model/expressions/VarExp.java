package model.expressions;

import exceptions.ADTException;
import model.prg.adt.MyIDictionary;
import model.values.IValue;

public class VarExp implements IExp {
    private final String id;

    public VarExp(String givenId) {
        id = givenId;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dict) throws ADTException {
        return dict.getValue(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
