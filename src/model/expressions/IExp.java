package model.expressions;

import exceptions.ADTException;
import exceptions.ExpException;
import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIHeap;
import model.values.IValue;

public interface IExp {
    IValue eval(MyIDictionary<String, IValue> dict, MyIHeap<Integer, IValue> hp) throws ExpException, ADTException;

    IExp deepCopy();
}
