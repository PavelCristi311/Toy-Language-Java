package model.prg.adt;

import java.io.IOException;

public interface MyIDictionary<K, V> {
    void put(K key, V value);

    void remove(K key) throws IOException;

    boolean isDefined(K key);

    V getValue(K key);

    void update(K key, V value);
}
