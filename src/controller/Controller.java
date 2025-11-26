package controller;

import model.prg.PrgState;
import model.values.IValue;
import model.values.RefValue;
import repo.IRepo;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.lang.IO.print;

public class Controller {
    private final IRepo repo;
    ExecutorService executor;

    public Controller(IRepo givenRep) {
        repo = givenRep;
    }

    public IRepo getRepo() {
        return repo;
    }

    public void oneStepForAllPrg(List<PrgState> prgList) {
        prgList.forEach(repo::logPrgStateExec);

        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                .collect(Collectors.toList());
        try {
            List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                                try {
                                    return future.get();
                                } catch (Exception e) {
                                    print("Exception during one step for all programs :" + e.getMessage());
                                    return null;
                                }
                            }
                    ).filter(Objects::nonNull)
                    .toList();
            prgList.addAll(newPrgList);
            prgList.forEach(repo::logPrgStateExec);
            repo.setPrgList(prgList);
        } catch (Exception e) {
            print("Exception during one step for all programs :" + e.getMessage());
        }
    }

    public void allStep() {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        while (!prgList.isEmpty()) {
            List<Integer> SymTblAddr = new ArrayList<>();
            for (PrgState prg : repo.getPrgList()) {
                SymTblAddr.addAll(getAddrFromSymTable(prg.getSymTable().getContent().values()));
            }
            prgList.forEach(prgState -> prgState.getHeap().setContent((HashMap<Integer, IValue>) safeGarbageCollector(SymTblAddr, getAddrFromHeap(repo.getPrgList().getFirst().getHeap().getContent().values()), repo.getPrgList().getFirst().getHeap().getContent())));
            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdownNow();
        repo.setPrgList(prgList);
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

    List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap) {
        return heap.entrySet()
                .stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddress();
                })
                .collect(Collectors.toList());
    }

    List<Integer> getAddrFromHeap(Collection<IValue> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddress();
                })
                .collect(Collectors.toList());
    }

    Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapAddr, Map<Integer, IValue> heap) {
        return heap.entrySet()
                .stream()
                .filter(e -> symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

