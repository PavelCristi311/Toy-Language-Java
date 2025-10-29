package controller;

import exceptions.ADTException;
import exceptions.RepoException;
import model.prg.PrgState;
import model.prg.adt.MyIStack;
import model.stmts.CompStmt;
import model.stmts.IStmt;
import model.stmts.IfStmt;
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

    public PrgState oneStep() throws ADTException, RepoException {
        try {
            PrgState state = repo.getCrtPrg();
            repo.logCrtPrgStateExec();
            MyIStack<IStmt> stk = state.getExeStack();
            try {
                IStmt crtStmt = stk.pop();
                PrgState newState = crtStmt.execute(state);
                repo.logCrtPrgStateExec();
                print(newState);
                return newState;
            } catch (Exception e) {
                print("Failed to perform operation! Error : " + e.getMessage() + "\n");
            }
        } catch (Exception e) {
            print("Failed to perform operation! Error : " + e.getMessage() + "\n");
        }
        return null;
    }

    public PrgState oneStep(int index) throws ADTException, RepoException {
        try {
            PrgState state = repo.getPrg(index);
            repo.logIndPrgStateExec(index);
            MyIStack<IStmt> stk = state.getExeStack();
            try {
                IStmt crtStmt = stk.pop();
                PrgState newState = crtStmt.execute(state);
                repo.logIndPrgStateExec(index);
                print(newState);
                return newState;
            } catch (Exception e) {
                print("Failed to perform operation! Error : " + e.getMessage() + "\n");
            }
        } catch (Exception e) {
            print("Failed to perform operation! Error : " + e.getMessage() + "\n");
        }
        return null;
    }

    public void allStep() {
        Node root = new Node("Execution/Statement Tree: ");
        Node backupRoot = root;
        TreePanel panel = new TreePanel(backupRoot);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.setPreferredSize(new Dimension(1600, 1200));
        frame.add(new JScrollPane(panel));
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        TreeLayout.apply(backupRoot);
        panel.fitToContent();

        try {
            PrgState prg = repo.getCrtPrg();
            print("------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            print("| Initial state: |\n");
            print(prg.toString());
            repo.logCrtPrgStateExec();

            int count = 1;
            while (!prg.getExeStack().isEmpty()) {

                Thread.sleep(1000);

                IStmt crtStmt = prg.getExeStack().pop();

                if (!(crtStmt instanceof CompStmt)) {

                    if (root.left == null) {
                        root.left = new Node(crtStmt.toString());
                        if (crtStmt instanceof IfStmt) {
                            try {
                                crtStmt.execute(prg);
                                repo.logCrtPrgStateExec();
                            } catch (Exception e) {
                                print("Failed to execute statement! Error: " + e.getMessage() + "\n");
                                break;
                            }
                            try {
                                crtStmt = prg.getExeStack().pop();
                                root.left.left = new Node(crtStmt.toString());
                            } catch (Exception e) {
                                print("Invalid IfStatement !" + "\n");
                                break;
                            }
                        }
                    } else {
                        root.right = new Node(crtStmt.toString());
                        if (crtStmt instanceof IfStmt) {
                            try {
                                crtStmt.execute(prg);
                                repo.logCrtPrgStateExec();
                            } catch (Exception e) {
                                print("Failed to execute statement! Error: " + e.getMessage() + "\n");
                                break;
                            }
                            try {
                                crtStmt = prg.getExeStack().pop();
                                root.left.left = new Node(crtStmt.toString());
                            } catch (Exception e) {
                                print("Invalid IfStatement !" + "\n");
                                break;
                            }
                        }
                        root = root.right;
                    }
                } else {
                    root.right = new Node(crtStmt.toString());
                    root = root.right;
                }

                TreeLayout.apply(backupRoot);
                panel.fitToContent();
                panel.repaint();
                try {
                    crtStmt.execute(prg);
                    repo.logCrtPrgStateExec();
                } catch (Exception e) {
                    print("Failed to execute statement! Error: " + e.getMessage() + "\n");
                    break;
                }
                print("------------------------------------------------------------------------------------------------------------------------------------------------------\n");
                print("| Step " + count + ": |\n");
                print(prg.toString());
                count += 1;
            }
        } catch (Exception e) {
            print("Failed to run program ! Error: " + e.getMessage() + "\n");
        }
    }

    public void allStep(int index) {
        Node root = new Node("Execution/Statement Tree: ");
        Node backupRoot = root;
        TreePanel panel = new TreePanel(backupRoot);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.setPreferredSize(new Dimension(1600, 1200));
        frame.add(new JScrollPane(panel));
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        TreeLayout.apply(backupRoot);
        panel.fitToContent();

        try {
            PrgState prg = repo.getPrg(index);
            print("Initial state: \n");
            print(prg.toString());
            repo.logIndPrgStateExec(index);

            int count = 1;
            while (!prg.getExeStack().isEmpty()) {

                Thread.sleep(1000);

                IStmt crtStmt = prg.getExeStack().pop();

                if (!(crtStmt instanceof CompStmt)) {

                    if (root.left == null) {
                        root.left = new Node(crtStmt.toString());
                        if (crtStmt instanceof IfStmt) {
                            try {
                                crtStmt.execute(prg);
                                repo.logIndPrgStateExec(index);
                            } catch (Exception e) {
                                print("Failed to execute statement! Error: " + e.getMessage() + "\n");
                                break;
                            }
                            try {
                                crtStmt = prg.getExeStack().pop();
                                root.left.left = new Node(crtStmt.toString());
                            } catch (Exception e) {
                                print("Invalid IfStatement !" + "\n");
                                break;
                            }
                        }
                    } else {
                        root.right = new Node(crtStmt.toString());
                        if (crtStmt instanceof IfStmt) {
                            try {
                                crtStmt.execute(prg);
                                repo.logIndPrgStateExec(index);
                            } catch (Exception e) {
                                print("Failed to execute statement! Error: " + e.getMessage() + "\n");
                                break;
                            }
                            try {
                                crtStmt = prg.getExeStack().pop();
                                root.left.left = new Node(crtStmt.toString());
                            } catch (Exception e) {
                                print("Invalid IfStatement !" + "\n");
                                break;
                            }
                        }
                        root = root.right;
                    }
                } else {
                    root.right = new Node(crtStmt.toString());
                    root = root.right;
                }

                TreeLayout.apply(backupRoot);
                panel.fitToContent();
                panel.repaint();
                try {
                    crtStmt.execute(prg);
                    repo.logIndPrgStateExec(index);
                } catch (Exception e) {
                    print("Failed to execute statement! Error: " + e.getMessage() + "\n");
                    break;
                }
                print("Step " + count + ":\n");
                print(prg.toString());
                count += 1;
            }
        } catch (Exception e) {
            print("Failed to run program ! Error: " + e.getMessage() + "\n");
        }
    }

    public void displayPrgState(PrgState prg) {
        print(prg.toString());
    }

    public void addPrg(PrgState prg) {
        try {
            repo.add(prg);
        } catch (Exception e) {
            print("Failed to add program! Error : " + e.getMessage() + "\n");
        }
    }

    public void removePrg(int index) {
        try {
            repo.remove(index);
        } catch (Exception e) {
            print("Failed to remove program! Error : " + e.getMessage() + "\n");
        }
    }

}
