package model.stmts;

import model.prg.PrgState;

public class NopStmt implements IStmt {
    @Override
    public PrgState execute(PrgState state) {
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NopStmt();
    }

    public String toString() {
        return "\n";
    }
}
