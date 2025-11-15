package model.prg;

import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIHeap;
import model.prg.adt.MyIList;
import model.prg.adt.MyIStack;
import model.stmts.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;

public class PrgState {


    MyIStack<IStmt> exeStack;
    MyIDictionary<String, IValue> symTable;
    MyIList<String> out;
    MyIDictionary<StringValue, BufferedReader> fileTable;


    MyIHeap<Integer, IValue> heap;
    IStmt originalProgram;

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, IValue> symtbl, MyIList<String> ot, MyIDictionary<StringValue, BufferedReader> givenFileTable, MyIHeap<Integer, IValue> givenHeap, IStmt prg) {
        exeStack = stk;
        symTable = symtbl;
        out = ot;
        fileTable = givenFileTable;
        heap = givenHeap;
        originalProgram = prg.deepCopy();
        stk.push(prg);
    }

    public boolean isNotCompleted(){
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

    public String toString() {
        return "------------------------------------------------------------------------------------------------------------------------------------------------------\n" + exeStack + "\n" + symTable + "\n" + out + "\n" + fileTable + "\n" + heap + "\n" + "------------------------------------------------------------------------------------------------------------------------------------------------------\n\n\n\n";
    }

}
