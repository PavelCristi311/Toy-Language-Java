package model.stmts;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.StmtException;
import exceptions.TypeException;
import model.expressions.IExp;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.type.IType;
import model.type.RefType;
import model.values.IValue;
import model.values.RefValue;

import java.io.IOException;

public class wH implements IStmt {
    String varName;
    IExp expression;

    public wH(String givenName, IExp givenExp) {
        varName = givenName;
        expression = givenExp;

    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, IOException, ADTException, ExpException {
        if (!state.getSymTable().isDefined(varName)) throw new StmtException("The variable is not defined! ");
        if (!(state.getSymTable().getType(varName) instanceof RefType))
            throw new StmtException("The variable is not of RefType! ");
        RefValue val = (RefValue) state.getSymTable().getValue(varName);
        if (!(state.getHeap().isDefined(val.getAddress()))) throw new StmtException("The address is not in the heap! ");
        IValue eval = expression.eval(state.getSymTable(), state.getHeap());
        if (!eval.getType().equals(val.getLocationType()))
            throw new StmtException("The expression type is different from the variable location type! ");
        state.getHeap().update(val.getAddress(), eval);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new wH(varName, expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        IType typ1,typ2;
        typ1 = typeEnv.getType(varName);
        typ2 = expression.typecheck(typeEnv);
        if (!(typ1 instanceof RefType)) throw new TypeException("The variable type is not ref!\n");
        if (!(typ2.equals(((RefType) typ1).getInner()))) throw new TypeException("The expression type is different from the variable location type!\n");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "wH(" + varName + "," + expression.toString() + ")";
    }
}
