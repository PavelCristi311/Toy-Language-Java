package model.stmts;

import model.expressions.IExp;
import model.prg.PrgState;
import model.prg.adt.MyIList;

import static java.lang.IO.print;

public class PrintStmt implements IStmt {
    IExp exp;

    public PrintStmt(IExp e) {
        exp = e;
    }

    @Override
    public PrgState execute(PrgState state) {
        print(exp.eval(state.getSymTable()));
        print("\n\n");
        MyIList<String> out = state.getOut();
        out.add(exp.eval(state.getSymTable()).toString());
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return null;
    }

    public String toString() {
        return ("print(" + exp.toString() + ")");
    }
}
