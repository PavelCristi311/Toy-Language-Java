package model.expressions;

import exceptions.ADTException;
import exceptions.TypeException;
import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIHeap;
import model.type.IType;
import model.values.IValue;

public class VarExp implements IExp {
    private final String id;

    public VarExp(String givenId) {
        id = givenId;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dict, MyIHeap<Integer, IValue> hp) throws ADTException {
        return dict.getValue(id);
    }

    @Override
    public IExp deepCopy() {
        return new VarExp(id);
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        return typeEnv.getType(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
