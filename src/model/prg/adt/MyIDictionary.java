package model.prg.adt;

import model.type.IType;

public interface MyIDictionary<K, V> {
    void put(K key, V value);

    boolean isDefined(K key);

    V getValue(K key);

    IType getType(K key);

    void update(K key, V value);
}
