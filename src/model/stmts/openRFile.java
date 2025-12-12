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

import java.io.*;

import static java.lang.IO.print;

public class openRFile implements IStmt {
    IExp exp;

    public openRFile(IExp givenE) {
        exp = givenE;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, ExpException, ADTException {
        if (!exp.eval(state.getSymTable(), state.getHeap()).getType().equals(new StringType()))
            throw new StmtException("The expression is not of String Type! \n");
        if (state.getFileTable().isDefined((StringValue) exp.eval(state.getSymTable(), state.getHeap())))
            throw new StmtException("The file is already open!");
        try {
            BufferedReader bfrdWrt = new BufferedReader(new FileReader(((StringValue) exp.eval(state.getSymTable(), state.getHeap())).getValue()));
            state.getFileTable().put(((StringValue) exp.eval(state.getSymTable(), state.getHeap())), bfrdWrt);
        } catch (Exception e) {
            print(e.getMessage());
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new openRFile(exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        IType typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new StringType())) {
            return typeEnv;
        } else
            throw new TypeException("The expression of open file is not of type String\n");
    }

    @Override
    public String toString() {
        return "openRFile(" + exp.toString() + ")";
    }
}
