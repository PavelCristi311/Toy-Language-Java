package model.stmts;

import exceptions.StmtException;
import model.expressions.IExp;
import model.prg.PrgState;
import model.type.StringType;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class closeRFile implements IStmt {
    IExp exp;

    public closeRFile(IExp givenE) {
        exp = givenE;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, IOException {
        if (!exp.eval(state.getSymTable()).getType().equals(new StringType()))
            throw new StmtException("The expression is not of String Type! \n");
        BufferedReader bfrdWrt = state.getFileTable().getValue((StringValue) exp.eval(state.getSymTable()));
        bfrdWrt.close();
        state.getFileTable().remove((StringValue) exp.eval(state.getSymTable()));
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new closeRFile(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp.toString() + ")";
    }
}
