package model.expressions;

import exceptions.ExpException;
import model.prg.adt.MyIDictionary;
import model.type.BoolType;
import model.values.BoolValue;
import model.values.IValue;

import java.util.Objects;

public class LogicExp implements IExp {
    IExp e1;
    IExp e2;
    String op;

    public LogicExp(IExp ge1, IExp ge2, String gop) {
        e1 = ge1;
        e2 = ge2;
        op = gop;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dict) throws ExpException {
        if (!(e1.eval(dict).getType() instanceof BoolType))
            throw new ExpException("The first expression is not boolean! ");
        if (!(e2.eval(dict).getType() instanceof BoolType))
            throw new ExpException("The second expression is not boolean! ");
        if (!Objects.equals(op, "||") && !Objects.equals(op, "or") && !Objects.equals(op, "OR") && !Objects.equals(op, "&&") && !Objects.equals(op, "and") && !Objects.equals(op, "AND"))
            throw new ExpException("Invalid provided operator! ");
        BoolValue nr1 = (BoolValue) e1.eval(dict);
        BoolValue nr2 = (BoolValue) e2.eval(dict);
        if (Objects.equals(op, "||") || Objects.equals(op, "or") || Objects.equals(op, "OR")) {
            return new BoolValue(nr1.getValue() || nr2.getValue());
        } else {
            return new BoolValue(nr1.getValue() && nr2.getValue());
        }
    }

    @Override
    public IExp deepCopy() {
        return new LogicExp(e1.deepCopy(), e2.deepCopy(), op);
    }

    public String toString() {
        return e1.toString() + " " + op + " " + e2.toString();
    }
}
