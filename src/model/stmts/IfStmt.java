package model.stmts;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.StmtException;
import exceptions.TypeException;
import model.expressions.IExp;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.type.BoolType;
import model.type.IType;
import model.values.BoolValue;
import model.values.IValue;

public class IfStmt implements IStmt {
    IExp exp;
    IStmt thenS;
    IStmt elseS;

    public IfStmt(IExp givenExp, IStmt givenTS, IStmt givenES) {
        exp = givenExp;
        thenS = givenTS;
        elseS = givenES;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, ExpException, ADTException {
        MyIDictionary<String, IValue> dict = state.getSymTable();
        if (exp.eval(dict, state.getHeap()).getType() instanceof BoolType) {
            BoolValue v = (BoolValue) exp.eval(dict, state.getHeap());
            if (v.getValue())
                state.getExeStack().push(thenS);
            else
                state.getExeStack().push(elseS);

        } else throw new StmtException("Conditional expression is not boolean.");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        IType typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            thenS.typecheck(typeEnv.deepCopy());
            elseS.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new TypeException("The condition of IF has not the type bool\n");
    }

    @Override
    public String toString() {
        return "if (   " + exp.toString() + ") then " + thenS.toString() + " else " + elseS.toString();
    }
}
