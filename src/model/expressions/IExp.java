package model.expressions;

import exceptions.ADTException;
import exceptions.ExpException;
import model.prg.adt.MyIDictionary;
import model.values.IValue;

public interface IExp {
    IValue eval(MyIDictionary<String, IValue> dict) throws ExpException, ADTException;

    IExp deepCopy();
}
