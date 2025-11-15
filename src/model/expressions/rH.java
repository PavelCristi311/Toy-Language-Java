package model.expressions;

import exceptions.ADTException;
import exceptions.ExpException;
import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIHeap;
import model.values.IValue;
import model.values.RefValue;

public class rH implements IExp {
    IExp expression;

    public rH(IExp givenExp) {
        expression = givenExp;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dict, MyIHeap<Integer, IValue> hp) throws ExpException, ADTException {
        IValue eval = expression.eval(dict, hp);
        if (!(eval instanceof RefValue)) throw new ExpException("The expression is not a RefValue! ");
        return hp.getValue(((RefValue) eval).getAddress());
    }

    @Override
    public IExp deepCopy() {
        return new rH(expression.deepCopy());
    }

    public String toString() {
        return "rH(" + expression.toString() + ")";
    }
}
