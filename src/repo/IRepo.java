package repo;

import model.prg.PrgState;

public interface IRepo {
    PrgState getCrtPrg();

    void add(PrgState pS);

    int getCurrentIndex();

    void setCurrentIndex(int index);

    void next();

    void remove(int index);

    PrgState getPrg(int index);
}
