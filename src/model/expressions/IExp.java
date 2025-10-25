package model.expressions;

import model.prg.adt.MyIDictionary;
import model.values.IValue;

public interface IExp {
    IValue eval(MyIDictionary<String, IValue> dict);
}
