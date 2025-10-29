package view;

import controller.Controller;
import model.expressions.ArithExp;
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

public class Interpreter {
    static void main(String[] args) {
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

        // string varf;
        // varf=test.in;
        // openRFile(varf);
        // int varc;
        // readFile(varf,varc);
        // print(varc);
        // readFile(varf,varc);
        // print(varc);
        // closeRFile(varf)
        IStmt ex4 = new CompStmt(new VarDeclStmt("varf", new StringType()), new CompStmt(
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

        Repository rep1 = new Repository();
        rep1.add(prg1);
        Repository rep2 = new Repository();
        rep2.add(prg2);
        Repository rep3 = new Repository();
        rep3.add(prg3);
        Repository rep4 = new Repository();
        rep4.add(prg4);

        Controller c1 = new Controller(rep1);
        Controller c2 = new Controller(rep2);
        Controller c3 = new Controller(rep3);
        Controller c4 = new Controller(rep4);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), c1));
        menu.addCommand(new RunExample("2", ex2.toString(), c2));
        menu.addCommand(new RunExample("3", ex3.toString(), c3));
        menu.addCommand(new RunExample("4", ex4.toString(), c4));
        menu.show();
    }
}