package model.prg;

import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIList;
import model.prg.adt.MyIStack;
import model.stmts.IStmt;
import model.values.IValue;

public class PrgState {


    MyIStack<IStmt> exeStack;
    MyIDictionary<String, IValue> symTable;
    MyIList<String> out;
    IStmt originalProgram;

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, IValue> symtbl, MyIList<String> ot, IStmt prg) {
        exeStack = stk;
        symTable = symtbl;
        out = ot;
        originalProgram = prg.deepCopy();
        stk.push(prg);
    }

    public MyIDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public void setSymTable(MyIDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public MyIStack<IStmt> getExeStack() {
        return exeStack;
    }

    public void setExeStack(MyIStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public MyIList<String> getOut() {
        return out;
    }

    public void setOut(MyIList<String> out) {
        this.out = out;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public void setOriginalProgram(IStmt originalProgram) {
        this.originalProgram = originalProgram;
    }

    public String toString() {
        return exeStack + "\n" + symTable + "\n" + out + "\n\n";
    }

}
