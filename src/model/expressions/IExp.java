package model.expressions;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.TypeException;
import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIHeap;
import model.type.IType;
import model.values.IValue;

public interface IExp {
    IValue eval(MyIDictionary<String, IValue> dict, MyIHeap<Integer, IValue> hp) throws ExpException, ADTException;

    IExp deepCopy();

    IType typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException;
}
