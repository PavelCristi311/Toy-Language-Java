package model.stmts;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.StmtException;
import exceptions.TypeException;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.type.IType;

import java.io.IOException;

public interface IStmt {
    PrgState execute(PrgState state) throws StmtException, IOException, ADTException, ExpException;

    IStmt deepCopy();

    MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException;
}
