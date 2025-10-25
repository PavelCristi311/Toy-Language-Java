package model.stmts;

import exceptions.StmtException;
import model.prg.PrgState;

public interface IStmt {
    PrgState execute(PrgState state) throws StmtException;

    IStmt deepCopy();
}
