package model.stmts;

import exceptions.StmtException;
import model.expressions.IExp;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.type.IType;
import model.values.IValue;

public class AssignStmt implements IStmt {
    String id;
    IExp exp;

    public AssignStmt(String givenId, IExp givenExp) {
        id = givenId;
        exp = givenExp;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        if (symTbl.isDefined(id)) {
            IValue val = exp.eval(symTbl);
            IType typeId = symTbl.getType(id);
            if ((val.getType()).equals(typeId)) {
                symTbl.update(id, val);
            } else
                throw new StmtException("Declared type of variable" + id + " and type of the assigned expression do not match.");

        } else throw new StmtException("The used variable" + id + " was not declared before.");
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(id, exp);
    }

    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }

    public IExp getExp(){
        return exp;
    }
}
