package model.prg.adt;

public interface MyIHeap<K, V> extends MyIDictionary<K, V> {
    K getNextKey();

    K getLastKey();
}
