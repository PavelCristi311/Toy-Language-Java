package model.stmts;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.StmtException;
import exceptions.TypeException;
import model.expressions.IExp;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.type.IType;
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
    public PrgState execute(PrgState state) throws StmtException, IOException, ExpException, ADTException {
        if (!exp.eval(state.getSymTable(), state.getHeap()).getType().equals(new StringType()))
            throw new StmtException("The expression is not of String Type! \n");
        BufferedReader bfrdWrt = state.getFileTable().getValue((StringValue) exp.eval(state.getSymTable(), state.getHeap()));
        bfrdWrt.close();
        state.getFileTable().remove((StringValue) exp.eval(state.getSymTable(), state.getHeap()));
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new closeRFile(exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        IType typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new StringType()))
            return typeEnv;
        else
            throw new TypeException("The expression of close file is not of type String\n");
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp.toString() + ")";
    }
}
