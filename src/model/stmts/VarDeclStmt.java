package model.stmts;

import exceptions.ADTException;
import exceptions.StmtException;
import exceptions.TypeException;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.type.IType;
import model.values.IValue;

public class VarDeclStmt implements IStmt {
    private final String id;
    private final IType type;

    public VarDeclStmt(String newId, IType newType) {
        this.id = newId;
        this.type = newType;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, ADTException {
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        if (symTable.isDefined(id)) throw new StmtException("ID already defined! \n");
        else {
            symTable.put(id, this.type.defaultValue());
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(id, type.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws TypeException, ADTException {
        typeEnv.put(id, type);
        return typeEnv;
    }

    @Override
    public String toString() {
        return type.toString() + " " + id;
    }
}
