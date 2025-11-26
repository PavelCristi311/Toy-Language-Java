package model.stmts;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.StmtException;
import model.expressions.IExp;
import model.prg.PrgState;
import model.values.BoolValue;
import model.values.IValue;

import java.io.IOException;

public class WhileStmt implements IStmt {
    IExp expression;
    IStmt stmt;

    public WhileStmt(IExp givenExp, IStmt givenStmt) {
        expression = givenExp;
        stmt = givenStmt;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, IOException, ExpException, ADTException {
        IValue eval = expression.eval(state.getSymTable(), state.getHeap());
        if (!(eval instanceof BoolValue)) throw new StmtException("The expression is not of Bool type! ");
        if (((BoolValue) eval).getValue()) {
            state.getExeStack().push(deepCopy());
            state.getExeStack().push(stmt);
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(expression.deepCopy(), stmt.deepCopy());
    }

    public String toString() {
        return "while(" + expression.toString() + ")" + stmt.toString();
    }
}
