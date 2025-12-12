package model.stmts;

import exceptions.ADTException;
import exceptions.TypeException;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.type.IType;

public class NopStmt implements IStmt {
    @Override
    public PrgState execute(PrgState state) {
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NopStmt();
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        return typeEnv;
    }

    public String toString() {
        return "nop";
    }
}
