package model.stmts;

import exceptions.StmtException;
import model.prg.PrgState;
import model.prg.adt.*;

import java.io.IOException;

public class forkStmt implements IStmt {
    IStmt stmt;

    public forkStmt(IStmt givenStmt) {
        stmt = givenStmt;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, IOException {
        ExeStack<IStmt> newStack = new ExeStack<>();
        SymTable newSymTable = new SymTable();
        newSymTable.setContent(state.getSymTable().deepCopy());
        Heap newHeap = (Heap) state.getHeap();
        FileTable newFileTable = (FileTable) state.getFileTable();
        OutList newOutList = (OutList) state.getOut();
        return new PrgState(PrgState.getNextId() * 10, newStack, newSymTable, newOutList, newFileTable, newHeap, stmt);
    }

    @Override
    public IStmt deepCopy() {
        return null;
    }

    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }
}
