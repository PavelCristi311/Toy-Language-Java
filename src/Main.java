import controller.Controller;
import model.expressions.ArithExp;
import model.expressions.LogicExp;
import model.expressions.ValueExp;
import model.expressions.VarExp;
import model.prg.PrgState;
import model.prg.adt.ExeStack;
import model.prg.adt.FileTable;
import model.prg.adt.OutList;
import model.prg.adt.SymTable;
import model.stmts.*;
import model.type.BoolType;
import model.type.IntType;
import model.type.StringType;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;
import repo.Repository;
import view.View;

void main() {
    // int v;
    // v=2;
    // Print(v) is represented as:
    IStmt ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
            new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))), new PrintStmt(new VarExp("v"))));

    // int a;
    // int b;
    // a=2+3*5;
    // b=a+1;
    // Print(b)
    // is represented as:
    IStmt ex2 = new CompStmt(new VarDeclStmt("a", new IntType()),
            new CompStmt(new VarDeclStmt("b", new IntType()),
                    new CompStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntValue(2)), new ArithExp('*', new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                            new CompStmt(new AssignStmt("b", new ArithExp('+', new VarExp("a"), new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));

    // bool a ;
    // int v;
    // a=true;
    // If a Then v=2;
    // Else v=3;
    // Print(v);
    // is represented as:
    IStmt ex3 = new CompStmt(new VarDeclStmt("a", new BoolType()),
            new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                            new CompStmt(new IfStmt(new VarExp("a"), new AssignStmt("v", new ValueExp(new IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new VarExp("v"))))));

    // bool c;
    // print(c);
    // is represented as:
    IStmt ex4 = new CompStmt(new VarDeclStmt("c", new BoolType()), new PrintStmt(new VarExp("c")));

    // bool d;
    // int e;
    // d=true;
    // e=6;
    // if(d || e) then print(e);
    // else print(d);
    // is represented as:
    IStmt ex5 = new CompStmt(new VarDeclStmt("d", new BoolType()),
            new CompStmt(new VarDeclStmt("e", new IntType()),
                    new CompStmt(new AssignStmt("d", new ValueExp(new BoolValue(false))),
                            new CompStmt(new AssignStmt("e", new ValueExp(new IntValue(6))), new IfStmt(new LogicExp(new VarExp("d"), new VarExp("e"), "||"),
                                    new PrintStmt(new VarExp("e")), new PrintStmt(new VarExp("d")))))));

    // print(o);
    // print(j);
    IStmt ex6 = new CompStmt(new PrintStmt(new VarExp("o")), new PrintStmt(new VarExp("j")));

    // string varf;
    // varf=test.in;
    // openRFile(varf);
    // int varc;
    // readFile(varf,varc);
    // print(varc);
    // readFile(varf,varc);
    // print(varc);
    // closeRFile(varf)

    IStmt ex7 = new CompStmt(new VarDeclStmt("varf", new StringType()), new CompStmt(
            new AssignStmt("varf", new ValueExp(new StringValue("test.in"))), new CompStmt(
            new openRFile(new VarExp("varf")), new CompStmt(
            new VarDeclStmt("varc", new IntType()), new CompStmt(
            new readFile(new VarExp("varf"), "varc"), new CompStmt(
            new PrintStmt(new VarExp("varc")), new CompStmt(
            new readFile(new VarExp("varf"), "varc"), new CompStmt(
            new PrintStmt(new VarExp("varc")), new closeRFile(new VarExp("varf"))))))))));

    PrgState prg1 = new PrgState(new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), ex1);
    PrgState prg2 = new PrgState(new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), ex2);
    PrgState prg3 = new PrgState(new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), ex3);
    PrgState prg4 = new PrgState(new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), ex4);
    PrgState prg5 = new PrgState(new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), ex5);
    PrgState prg6 = new PrgState(new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), ex6);
    PrgState prg7 = new PrgState(new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), ex7);


    Repository rep = new Repository();
    Controller con = new Controller(rep);
    con.addPrg(prg1);
    con.addPrg(prg2);
    con.addPrg(prg3);
    con.addPrg(prg4);
    con.addPrg(prg5);
    con.addPrg(prg6);
    con.addPrg(prg7);

    View v = new View(con);

    v.mainView();
}