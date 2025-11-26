package model.stmts;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.StmtException;
import model.expressions.IExp;
import model.prg.PrgState;
import model.type.RefType;
import model.values.IValue;
import model.values.RefValue;

import java.io.IOException;

public class newHM implements IStmt {
    String varName;
    IExp expression;

    public newHM(String varName, IExp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, IOException, ADTException, ExpException {
        if (!state.getSymTable().isDefined(varName)) throw new StmtException("The variable is not defined!");
        if (!(state.getSymTable().getType(varName) instanceof RefType))
            throw new StmtException("The variable is not of RefType!");
        IValue val = expression.eval(state.getSymTable(), state.getHeap());
        if (!(val.getType().equals(((RefValue) state.getSymTable().getValue(varName)).getLocationType())))
            throw new StmtException("The expression is not of RefType!");
        state.getHeap().put(state.getHeap().getNextKey(), val);
        state.getSymTable().update(varName, new RefValue(state.getHeap().getLastKey(), val.getType()));
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new newHM(varName, expression.deepCopy());
    }

    public String toString() {
        return "new(" + varName + "," + expression.toString() + ")";
    }
}
