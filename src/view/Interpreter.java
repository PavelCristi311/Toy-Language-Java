package view;

import controller.Controller;
import model.expressions.*;
import model.prg.PrgState;
import model.prg.adt.*;
import model.stmts.*;
import model.type.BoolType;
import model.type.IntType;
import model.type.RefType;
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

        //Example:
        // Ref int v
        // new(v,20);
        // Ref Ref int a;
        // new(a,v);
        // print(v);
        // print(a)
        //At the end of execution: Heap={1->20, 2->(1,int)}, SymTable={v->(1,int), a->(2,Ref int)} and Out={(1,int),(2,Ref int)}

        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new newHM("v", new ValueExp(new IntValue(20))), new CompStmt(
                new VarDeclStmt("a", new RefType(new RefType(new IntType()))), new CompStmt(
                new newHM("a", new VarExp("v")), new CompStmt(
                new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("a")))))));

        //Example:
        // Ref int v;
        // new(v,20);
        // print(rH(v));
        // wH(v,30);
        // print(rH(v)+5);
        //At the end of execution: Heap={1->30}, SymTable={v->(1,int)} and Out={20, 35}

        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new newHM("v", new ValueExp(new IntValue(20))), new CompStmt(
                new PrintStmt(new rH(new VarExp("v"))), new CompStmt(
                new wH("v", new ValueExp(new IntValue(30))),
                new PrintStmt(new ArithExp('+', new rH(new VarExp("v")), new ValueExp(new IntValue(5))))))));

        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new newHM("v", new ValueExp(new IntValue(20))), new CompStmt(
                new VarDeclStmt("a", new RefType(new RefType(new IntType()))), new CompStmt(
                new newHM("a", new VarExp("v")), new CompStmt(
                new VarDeclStmt("b", new RefType(new RefType(new RefType(new IntType())))), new CompStmt(
                new newHM("b", new VarExp("a")), new CompStmt(
                new newHM("v", new ValueExp(new IntValue(30))),
                new PrintStmt(new rH(new rH(new VarExp("a")))))))))));
        //int x;
        //x=5;
        //while(x>0){
        //  print(x);
        //  x=x-1;
        //}

        IStmt ex8 = new CompStmt(new VarDeclStmt("x", new IntType()), new CompStmt(
                new AssignStmt("x", new ValueExp(new IntValue(5))), new WhileStmt(
                new RelationalExp(">", new VarExp("x"), new ValueExp(new IntValue(0))),
                new CompStmt(new PrintStmt(new VarExp("x")),
                        new AssignStmt("x", new ArithExp('-', new VarExp("x"), new ValueExp(new IntValue(1))))))));

        //Example:
        //Ref int v;
        //new(v,20);
        //Ref Ref int a;
        //new(a,v);
        //new(v,30);
        //print(rH(rH(a)))

        IStmt ex9 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new newHM("v", new ValueExp(new IntValue(20))), new CompStmt(
                new VarDeclStmt("a", new RefType(new RefType(new IntType()))), new CompStmt(
                new newHM("a", new VarExp("v")), new CompStmt(
                new newHM("v", new ValueExp(new IntValue(30))),
                new PrintStmt(new rH(new rH(new VarExp("a")))))))));


//      Example:
//        int v;
//        Ref int a;
//        v=10;
//        new(a,22);
//        fork(wH(a,30);v=32;print(v);print(rH(a)));
//        print(v);
//        print(rH(a))
//      At the end:
//        Id=1
//        SymTable_1={v->10,a->(1,int)}
//        Id=10
//        SymTable_10={v->32,a->(1,int)}
//        Heap={1->30}
//        Out={10,30,32,30}

        IStmt ex10 = new CompStmt(new VarDeclStmt("v", new IntType()), new CompStmt(
                new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(
                new AssignStmt("v", new ValueExp(new IntValue(10))), new CompStmt(
                new newHM("a", new ValueExp(new IntValue(22))), new CompStmt(
                new forkStmt(new CompStmt(new wH("a", new ValueExp(new IntValue(30))), new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(32))), new CompStmt(
                        new PrintStmt(new VarExp("v")), new PrintStmt(new rH(new VarExp("a")))))
                )), new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new rH(new VarExp("a"))))
        )))));



        PrgState prg1 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex1);
        PrgState prg2 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex2);
        PrgState prg3 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex3);
        PrgState prg4 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex4);
        PrgState prg5 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex5);
        PrgState prg6 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex6);
        PrgState prg7 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex7);
        PrgState prg8 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex8);
        PrgState prg9 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex9);
        PrgState prg10 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex10);

        Repository rep1 = new Repository();
        rep1.add(prg1);
        Repository rep2 = new Repository();
        rep2.add(prg2);
        Repository rep3 = new Repository();
        rep3.add(prg3);
        Repository rep4 = new Repository();
        rep4.add(prg4);
        Repository rep5 = new Repository();
        rep5.add(prg5);
        Repository rep6 = new Repository();
        rep6.add(prg6);
        Repository rep7 = new Repository();
        rep7.add(prg7);
        Repository rep8 = new Repository();
        rep8.add(prg8);
        Repository rep9 = new Repository();
        rep9.add(prg9);
        Repository rep10 = new Repository();
        rep10.add(prg10);

        Controller c1 = new Controller(rep1);
        Controller c2 = new Controller(rep2);
        Controller c3 = new Controller(rep3);
        Controller c4 = new Controller(rep4);
        Controller c5 = new Controller(rep5);
        Controller c6 = new Controller(rep6);
        Controller c7 = new Controller(rep7);
        Controller c8 = new Controller(rep8);
        Controller c9 = new Controller(rep9);
        Controller c10 = new Controller(rep10);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), c1));
        menu.addCommand(new RunExample("2", ex2.toString(), c2));
        menu.addCommand(new RunExample("3", ex3.toString(), c3));
        menu.addCommand(new RunExample("4", ex4.toString(), c4));
        menu.addCommand(new RunExample("5", ex5.toString(), c5));
        menu.addCommand(new RunExample("6", ex6.toString(), c6));
        menu.addCommand(new RunExample("7", ex7.toString(), c7));
        menu.addCommand(new RunExample("8", ex8.toString(), c8));
        menu.addCommand(new RunExample("9", ex9.toString(), c9));
        menu.addCommand(new RunExample("10", ex10.toString(), c10));

        menu.show();
    }
}