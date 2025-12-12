package model.stmts;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.TypeException;
import model.expressions.IExp;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIList;
import model.type.IType;

import static java.lang.IO.print;

public class PrintStmt implements IStmt {
    IExp exp;

    public PrintStmt(IExp e) {
        exp = e;
    }

    @Override
    public PrgState execute(PrgState state) throws ExpException, ADTException {
        print(exp.eval(state.getSymTable(), state.getHeap()));
        print("\n\n");
        MyIList<String> out = state.getOut();
        out.add(exp.eval(state.getSymTable(), state.getHeap()).toString());
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }

    public String toString() {
        return ("print(" + exp.toString() + ")");
    }
}
