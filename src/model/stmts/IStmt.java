package model.stmts;

import exceptions.StmtException;
import model.prg.PrgState;

import java.io.IOException;

public interface IStmt {
    PrgState execute(PrgState state) throws StmtException, IOException;

    IStmt deepCopy();
}
