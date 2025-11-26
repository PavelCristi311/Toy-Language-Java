package model.stmts;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.StmtException;
import model.prg.PrgState;

import java.io.IOException;

public interface IStmt {
    PrgState execute(PrgState state) throws StmtException, IOException, ADTException, ExpException;

    IStmt deepCopy();
}
