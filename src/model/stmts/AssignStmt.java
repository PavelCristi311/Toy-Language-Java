package model.stmts;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.StmtException;
import exceptions.TypeException;
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
    public PrgState execute(PrgState state) throws StmtException, ADTException, ExpException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();
        if (symTbl.isDefined(id)) {
            IValue val = exp.eval(symTbl, state.getHeap());
            IType typeId = symTbl.getValue(id).getType();
            if ((val.getType()).equals(typeId)) {
                symTbl.update(id, val);
            } else
                throw new StmtException("Declared type of variable" + id + " and type of the assigned expression do not match.");

        } else throw new StmtException("The used variable" + id + " was not declared before.");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(id, exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        IType typevar = typeEnv.getType(id);
        IType typexp = exp.typecheck(typeEnv);
        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new TypeException("Assignment: right hand side and left hand side have different types \n");
    }

    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }

    public IExp getExp() {
        return exp;
    }
}
