package model.stmts;

import exceptions.ADTException;
import exceptions.ExpException;
import exceptions.StmtException;
import model.expressions.IExp;
import model.prg.PrgState;
import model.type.IntType;
import model.type.StringType;
import model.values.IntValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

import static java.lang.IO.print;

public class readFile implements IStmt {
    IExp exp;
    String var_name;

    public readFile(IExp givenE, String givenS) {
        exp = givenE;
        var_name = givenS;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, IOException, ADTException, ExpException {
        if (!state.getSymTable().isDefined(var_name)) throw new StmtException("The variable name is not defined! \n");
        if (!(state.getSymTable().getValue(var_name).getType() instanceof IntType))
            throw new StmtException("The variable type is not int! \n");
        if (!(exp.eval(state.getSymTable(), state.getHeap()).getType() instanceof StringType))
            throw new StmtException("The expression value is not a string! \n");
        StringValue filepath = (StringValue) exp.eval(state.getSymTable(), state.getHeap());
        BufferedReader bfrdRdr = state.getFileTable().getValue(filepath);
        try {
            String str = bfrdRdr.readLine();
            if (str.isBlank()) state.getSymTable().update(var_name, new IntValue(0));
            else state.getSymTable().update(var_name, new IntValue(Integer.parseInt(str)));
        } catch (Exception e) {
            print("Reading from file failed ! Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new readFile(exp.deepCopy(), var_name);
    }

    @Override
    public String toString() {
        return "readFile(into: " + var_name + ", from: " + exp.toString() + ")";
    }
}
