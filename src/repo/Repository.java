package repo;

import exceptions.RepoException;
import model.prg.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.IO.print;

public class Repository implements IRepo {
    private final ArrayList<PrgState> prgRepo;
    private int currentIndex = -1;
    private final String logFilePath;

    public Repository() {
        prgRepo = new ArrayList<>();
        print("Please submit the filepath: ");
        logFilePath = new Scanner(System.in).nextLine();
    }

    @Override
    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public void setCurrentIndex(int current) {
        if (current < 0 || current >= prgRepo.size()) throw new RepoException("Invalid provided index! ");
        this.currentIndex = current;
    }

    @Override
    public void next() {
        if (currentIndex + 1 >= prgRepo.size()) throw new RepoException("There are no more programs! ");
        currentIndex += 1;
    }

    @Override
    public void add(PrgState pS) {
        if (currentIndex == -1) currentIndex++;
        prgRepo.add(pS);
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= prgRepo.size()) throw new RepoException("Invalid provided index! ");
        prgRepo.remove(index);
        if (currentIndex == index) currentIndex--;
    }

    @Override
    public PrgState getCrtPrg() {
        if (prgRepo.isEmpty()) throw new RepoException("There are no available programs! ");
        return prgRepo.get(currentIndex);
    }

    @Override
    public PrgState getPrg(int index) {
        if (index < 0 || index >= prgRepo.size()) throw new RepoException("Invalid provided index! ");
        return prgRepo.get(index);
    }

    @Override
    public void logCrtPrgStateExec() throws RepoException, IOException {
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.append(getCrtPrg().toString());
        } catch (IOException e) {
            print("Failed logging! Error :" + e.getMessage());
        }
    }

    @Override
    public void logIndPrgStateExec(int index) throws RepoException, IOException {
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.append(getPrg(index).toString());
        } catch (IOException e) {
            print("Failed logging! Error :" + e.getMessage());
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder("The repository contains the following programs: \n\n");
        for (int i = 0; i < prgRepo.size(); i++) {
            result.append(i + 1);
            result.append('.');
            result.append(this.getPrg(i).toString());
            result.append('\n');
        }
        return result.toString();
    }
}
