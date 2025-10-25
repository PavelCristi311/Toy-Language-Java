package model.stmts;

import exceptions.StmtException;
import model.prg.PrgState;
import model.prg.adt.MyIDictionary;
import model.type.IType;
import model.type.IntType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class VarDeclStmt implements IStmt {
    private final String id;
    private final IType type;

    public VarDeclStmt(String newId, IType newType) {
        this.id = newId;
        this.type = newType;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        if (symTable.isDefined(id)) throw new StmtException("ID already defined! \n");
        else {
            if (this.type.equals(new IntType()))
                symTable.put(id, new IntValue(0));
            else
                symTable.put(id, new BoolValue(false));
        }
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(id, type);
    }

    @Override
    public String toString() {
        return type.toString() + " " + id;
    }
}
