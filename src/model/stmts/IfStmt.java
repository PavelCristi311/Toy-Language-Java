package model.stmts;

import exceptions.StmtException;
import model.expressions.IExp;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.type.BoolType;
import model.values.BoolValue;
import model.values.IValue;

public class IfStmt implements IStmt {
    IExp exp;
    IStmt thenS;
    IStmt elseS;

    public IfStmt(IExp givenExp, IStmt givenTS, IStmt givenES) {
        exp = givenExp;
        thenS = givenTS;
        elseS = givenES;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        MyIDictionary<String, IValue> dict = state.getSymTable();
        if (exp.eval(dict).getType() instanceof BoolType) {
            BoolValue v = (BoolValue) exp.eval(dict);
            if (v.getValue())
                state.getExeStack().push(thenS);
            else
                state.getExeStack().push(elseS);

        } else throw new StmtException("Conditional expression is not boolean.");
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(exp, thenS, elseS);
    }

    @Override
    public String toString() {
        return "if (" + exp.toString() + ") then " + thenS.toString() + " else " + elseS.toString();
    }
}
