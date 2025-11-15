package repo;

import exceptions.RepoException;
import model.prg.PrgState;

import java.io.IOException;
import java.util.List;

public interface IRepo {
    PrgState getCrtPrg();

    void add(PrgState pS);

    int getCurrentIndex();

    void setCurrentIndex(int index);

    void next();

    void remove(int index);

    PrgState getPrg(int index);

    void logCrtPrgStateExec() throws RepoException, IOException;

    void logIndPrgStateExec(int index) throws RepoException, IOException;

    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> list);
}
