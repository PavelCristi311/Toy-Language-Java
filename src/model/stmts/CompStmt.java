package model.stmts;

import exceptions.ADTException;
import exceptions.TypeException;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.prg.adt.MyIStack;
import model.type.IType;

public class CompStmt implements IStmt {
    private final IStmt first;
    private final IStmt second;

    public CompStmt(IStmt first, IStmt second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public PrgState execute(PrgState state) {
        MyIStack<IStmt> stk = state.getExeStack();
        stk.push(second);
        stk.push(first);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CompStmt(first.deepCopy(), second.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        return second.typecheck(first.typecheck(typeEnv));
    }

    public String toString() {
        return "(" + first.toString() + ";" + second.toString() + ")";
    }
}
