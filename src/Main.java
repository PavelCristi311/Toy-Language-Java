import controller.Controller;
import model.expressions.ArithExp;
import model.expressions.ValueExp;
import model.expressions.VarExp;
import model.prg.PrgState;
import model.prg.adt.ExeStack;
import model.prg.adt.OutList;
import model.prg.adt.SymTable;
import model.stmts.*;
import model.type.BoolType;
import model.type.IntType;
import model.values.BoolValue;
import model.values.IntValue;
import repo.Repository;
import view.View;

void main() throws Exception {
    //int v; v=2;Print(v) is represented as:
    IStmt ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
            new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))), new PrintStmt(new VarExp("v"))));
    //int a;int b; a=2+3*5;b=a+1;Print(b) is represented as:
    IStmt ex2 = new CompStmt(new VarDeclStmt("a", new IntType()),
            new CompStmt(new VarDeclStmt("b", new IntType()),
                    new CompStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntValue(2)), new ArithExp('*', new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                            new CompStmt(new AssignStmt("b", new ArithExp('+', new VarExp("a"), new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));
    //bool a ; int v; a=true;(If a Then v=2 Else v=3);Print(v) is represented as:
    IStmt ex3 = new CompStmt(new VarDeclStmt("a", new BoolType()),
            new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                            new CompStmt(new IfStmt(new VarExp("a"), new AssignStmt("v", new ValueExp(new IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new VarExp("v"))))));


    PrgState prg1 = new PrgState(new ExeStack<>(), new SymTable(), new OutList(), ex1);
    PrgState prg2 = new PrgState(new ExeStack<>(), new SymTable(), new OutList(), ex2);
    PrgState prg3 = new PrgState(new ExeStack<>(), new SymTable(), new OutList(), ex3);


    Repository rep = new Repository();
    rep.add(prg1);
    rep.add(prg2);
    rep.add(prg3);

    Controller con = new Controller(rep);

    View v = new View(con);

    v.mainView();
}