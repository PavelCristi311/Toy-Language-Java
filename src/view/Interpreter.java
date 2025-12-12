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

import java.util.ArrayList;

import static java.lang.IO.print;

public class Interpreter {
    ArrayList<Controller> Clist= new ArrayList<>();
    ArrayList<IStmt> PrgL=new ArrayList<>();

    public Interpreter() {
        int currentProg=0;
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0","exit"));
        // int v;
        // v=2;
        // Print(v) is represented as:
        IStmt ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))), new PrintStmt(new VarExp("v"))));
        TypeEnv ex1env=new TypeEnv();
        try {
            ex1.typecheck(ex1env);
            PrgState prg1 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex1);
            Repository rep1 = new Repository();
            rep1.add(prg1);
            Controller c1 = new Controller(rep1);
            Clist.add(c1);
            PrgL.add(ex1);
            menu.addCommand(new RunExample(""+ ++currentProg, ex1.toString(), c1));
        }catch(Exception e){
            print("Program 1 could not be initialized : "+e.getMessage());
        }

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
        TypeEnv ex2env=new TypeEnv();
        try {
            ex2.typecheck(ex2env);
            PrgState prg2 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex2);
            Repository rep2 = new Repository();
            rep2.add(prg2);
            Controller c2 = new Controller(rep2);
            Clist.add(c2);
            PrgL.add(ex2);
            menu.addCommand(new RunExample(""+ ++currentProg, ex2.toString(), c2));
        }catch(Exception e){
            print("Program 2 could not be initialized : "+e.getMessage());
        }

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
        TypeEnv ex3env=new TypeEnv();
        try {
            ex3.typecheck(ex3env);
            PrgState prg3 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex3);
            Repository rep3 = new Repository();
            rep3.add(prg3);
            Controller c3 = new Controller(rep3);
            Clist.add(c3);
            PrgL.add(ex3);
            menu.addCommand(new RunExample(""+ ++currentProg, ex3.toString(), c3));
        }catch(Exception e){
            print("Program 3 could not be initialized : "+e.getMessage());
        }

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
        TypeEnv ex4env=new TypeEnv();
        try {
            ex4.typecheck(ex4env);
            PrgState prg4 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex4);
            Repository rep4 = new Repository();
            rep4.add(prg4);
            Controller c4 = new Controller(rep4);
            Clist.add(c4);
            PrgL.add(ex4);
            menu.addCommand(new RunExample(""+ ++currentProg, ex4.toString(), c4));
        }catch(Exception e){
            print("Program 4 could not be initialized : "+e.getMessage());
        }

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
        TypeEnv ex5env=new TypeEnv();
        try {
            ex5.typecheck(ex5env);
            PrgState prg5 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex5);
            Repository rep5 = new Repository();
            rep5.add(prg5);
            Controller c5 = new Controller(rep5);
            Clist.add(c5);
            PrgL.add(ex5);
            menu.addCommand(new RunExample(""+ ++currentProg, ex5.toString(), c5));
        }catch(Exception e){
            print("Program 5 could not be initialized : "+e.getMessage());
        }

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
        TypeEnv ex6env=new TypeEnv();
        try {
            ex6.typecheck(ex6env);
            PrgState prg6 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex6);
            Repository rep6 = new Repository();
            rep6.add(prg6);
            Controller c6 = new Controller(rep6);
            Clist.add(c6);
            PrgL.add(ex6);
            menu.addCommand(new RunExample(""+ ++currentProg, ex6.toString(), c6));
        }catch(Exception e){
            print("Program 6 could not be initialized : "+e.getMessage());
        }

        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new newHM("v", new ValueExp(new IntValue(20))), new CompStmt(
                new VarDeclStmt("a", new RefType(new RefType(new IntType()))), new CompStmt(
                new newHM("a", new VarExp("v")), new CompStmt(
                new VarDeclStmt("b", new RefType(new RefType(new RefType(new IntType())))), new CompStmt(
                new newHM("b", new VarExp("a")), new CompStmt(
                new newHM("v", new ValueExp(new IntValue(30))),
                new PrintStmt(new rH(new rH(new VarExp("a")))))))))));
        TypeEnv ex7env=new TypeEnv();
        try {
            ex7.typecheck(ex7env);
            PrgState prg7 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex7);
            Repository rep7 = new Repository();
            rep7.add(prg7);
            Controller c7 = new Controller(rep7);
            Clist.add(c7);
            PrgL.add(ex7);
            menu.addCommand(new RunExample(""+ ++currentProg, ex7.toString(), c7));
        }catch(Exception e){
            print("Program 7 could not be initialized : "+e.getMessage());
        }

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
        TypeEnv ex8env=new TypeEnv();
        try {
            ex8.typecheck(ex8env);
            PrgState prg8 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex8);
            Repository rep8 = new Repository();
            rep8.add(prg8);
            Controller c8 = new Controller(rep8);
            Clist.add(c8);
            PrgL.add(ex8);
            menu.addCommand(new RunExample(""+ ++currentProg, ex8.toString(), c8));
        }catch(Exception e){
            print("Program 8 could not be initialized : "+e.getMessage());
        }

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
        TypeEnv ex9env=new TypeEnv();
        try {
            ex9.typecheck(ex9env);
            PrgState prg9 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex9);
            Repository rep9 = new Repository();
            rep9.add(prg9);
            Controller c9 = new Controller(rep9);
            Clist.add(c9);
            PrgL.add(ex9);
            menu.addCommand(new RunExample(""+ ++currentProg, ex9.toString(), c9));
        }catch(Exception e){
            print("Program 9 could not be initialized : "+e.getMessage());
        }

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

        TypeEnv ex10env=new TypeEnv();
        try {
            ex10.typecheck(ex10env);
            PrgState prg10 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex10);
            Repository rep10 = new Repository();
            rep10.add(prg10);
            Controller c10 = new Controller(rep10);
            Clist.add(c10);
            PrgL.add(ex10);
            menu.addCommand(new RunExample(""+ ++currentProg, ex10.toString(), c10));
        }catch(Exception e){
            print("Program 10 could not be initialized : "+e.getMessage());
        }

        IStmt ex11=new NopStmt();
        TypeEnv ex11env=new TypeEnv();
        try {
            ex11.typecheck(ex11env);
            PrgState prg11 = new PrgState(PrgState.getNextId(), new ExeStack<>(), new SymTable(), new OutList(), new FileTable(), new Heap(), ex11);
            Repository rep11 = new Repository();
            rep11.add(prg11);
            Controller c11 = new Controller(rep11);
            Clist.add(c11);
            PrgL.add(ex11);
            menu.addCommand(new RunExample(""+ ++currentProg, ex11.toString(), c11));
        }catch(Exception e){
            print("Program 11 could not be initialized : "+e.getMessage());
        }

    }


    public ArrayList<Controller> getClist() {
        return Clist;
    }

    public ArrayList<IStmt> getPrgL() {
        return PrgL;
    }
}