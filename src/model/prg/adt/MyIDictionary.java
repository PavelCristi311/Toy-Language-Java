package model.prg.adt;

import model.type.IType;

import java.io.IOException;
import java.util.Map;

public interface MyIDictionary<K, V> {
    void put(K key, V value);

    void remove(K key) throws IOException;

    boolean isDefined(K key);

    V getValue(K key);

    void update(K key, V value);

    IType getType(K key);

    Map<K, V> getContent();

    void setContent(Map<K, V> map);
}
