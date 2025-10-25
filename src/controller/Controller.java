package controller;

import exceptions.ADTException;
import exceptions.RepoException;
import exceptions.StmtException;
import model.prg.PrgState;
import model.prg.adt.MyIStack;
import model.stmts.CompStmt;
import model.stmts.IStmt;
import model.visualizer.TreeLayout;
import model.visualizer.TreePanel;
import repo.IRepo;
import model.visualizer.Node;

import javax.swing.*;

import java.awt.*;

import static java.lang.IO.print;

public class Controller {
    private final IRepo repo;

    public Controller(IRepo givenRep) {
        repo = givenRep;
    }

    public IRepo getRepo() {
        return repo;
    }

    public PrgState oneStep(int index) throws ADTException, StmtException, RepoException {
        PrgState state = repo.getPrg(index);
        MyIStack<IStmt> stk = state.getExeStack();
        if (stk.isEmpty()) throw new ADTException("Execution stack of the program is empty! ");
        IStmt crtStmt = stk.pop();
        PrgState newState = crtStmt.execute(state);
        print(newState);
        return newState;
    }

    public void allStep(int index) throws StmtException, InterruptedException {
        Node root=new Node("Program");
        Node backupRoot=root;
        TreePanel panel = new TreePanel(backupRoot);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setPreferredSize(new Dimension(1600, 1200));
        frame.add(new JScrollPane(panel));
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        TreeLayout.apply(backupRoot);
        panel.fitToContent();
        PrgState prg = repo.getPrg(index);
        print("Initial state: \n");
        print(prg.toString());
        int count = 1;
        while (!prg.getExeStack().isEmpty()) {
            Thread.sleep(1000);
            IStmt crtStmt = prg.getExeStack().pop();
            if(!(crtStmt instanceof CompStmt)) {
                if (root.left == null) root.left = new Node(crtStmt.toString());
                else root.right=new Node(crtStmt.toString());
            }
            else {
                root.right = new Node(crtStmt.toString());
                root = root.right;
            }
            TreeLayout.apply(backupRoot);
            panel.fitToContent();
            panel.repaint();
            crtStmt.execute(prg);
            print("Step " + count + ":\n");
            print(prg.toString());
            count += 1;

        }
    }

    public void displayPrgState(PrgState prg) {
        print(prg.toString());
    }

}
