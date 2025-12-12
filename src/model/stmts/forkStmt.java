package model.stmts;

import exceptions.ADTException;
import exceptions.StmtException;
import exceptions.TypeException;
import model.prg.PrgState;
import model.prg.adt.*;
import model.type.IType;

import java.io.IOException;

public class forkStmt implements IStmt {
    IStmt stmt;

    public forkStmt(IStmt givenStmt) {
        stmt = givenStmt;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, IOException {
        ExeStack<IStmt> newStack = new ExeStack<>();
        SymTable newSymTable;
        newSymTable = (SymTable) state.getSymTable().deepCopy();
        Heap newHeap = (Heap) state.getHeap();
        FileTable newFileTable = (FileTable) state.getFileTable();
        OutList newOutList = (OutList) state.getOut();
        return new PrgState(PrgState.getNextId() * 10, newStack, newSymTable, newOutList, newFileTable, newHeap, stmt);
    }

    @Override
    public IStmt deepCopy() {
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        return stmt.typecheck(typeEnv.deepCopy());
    }

    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }
}
