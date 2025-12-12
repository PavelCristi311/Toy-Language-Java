package model.prg;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.PrgException;
import exceptions.StmtException;
import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIHeap;
import model.prg.adt.MyIList;
import model.prg.adt.MyIStack;
import model.stmts.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class PrgState {

    MyIStack<IStmt> exeStack;
    MyIDictionary<String, IValue> symTable;
    MyIList<String> out;
    MyIDictionary<StringValue, BufferedReader> fileTable;
    final int id;
    static int lastId = 0;

    MyIHeap<Integer, IValue> heap;
    IStmt originalProgram;

    public PrgState(int givenId, MyIStack<IStmt> stk, MyIDictionary<String, IValue> symtbl, MyIList<String> ot, MyIDictionary<StringValue, BufferedReader> givenFileTable, MyIHeap<Integer, IValue> givenHeap, IStmt prg) {
        id = givenId;
        exeStack = stk;
        symTable = symtbl;
        out = ot;
        fileTable = givenFileTable;
        heap = givenHeap;
        originalProgram = prg.deepCopy();
        stk.push(prg);
    }

    public int getId() {
        return id;
    }

    synchronized public static int getNextId() {
        return ++lastId;
    }

    public boolean isNotCompleted() {
        return !exeStack.isEmpty();
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

    public MyIDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(MyIDictionary<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public MyIHeap<Integer, IValue> getHeap() {
        return heap;
    }

    public void setHeap(MyIHeap<Integer, IValue> heap) {
        this.heap = heap;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public void setOriginalProgram(IStmt originalProgram) {
        this.originalProgram = originalProgram;
    }

    public PrgState oneStep() throws PrgException, ADTException, StmtException, IOException, ExpException {
        if (exeStack.isEmpty()) throw new PrgException("The execution stack is empty! ");
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    public String toString() {
        return "------------------------------------------------------------------------------------------------------------------------------------------------------\n" + "ID: " + id + "\n" + exeStack + "\n" + symTable + "\n" + out + "\n" + fileTable + "\n" + heap + "\n" + "------------------------------------------------------------------------------------------------------------------------------------------------------\n\n\n\n";
    }

}
