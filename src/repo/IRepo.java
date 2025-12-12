package repo;

import exceptions.RepoException;
import model.prg.PrgState;

import java.io.IOException;
import java.util.List;

public interface IRepo {

    void add(PrgState pS);

    int getCurrentIndex();

    void setCurrentIndex(int index) throws RepoException;

    void next() throws RepoException;

    void remove(int index) throws RepoException;

    PrgState getPrg(int index) throws RepoException;

    void logIndPrgStateExec(int index) throws RepoException, IOException;

    void logPrgStateExec(PrgState prg);

    List<PrgState> getPrgList();

    void setPrgList(List<PrgState> list);

    void setLogFilePath(String absolutePath);
}
