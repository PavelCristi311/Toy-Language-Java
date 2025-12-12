package model.prg.adt;

import exceptions.ADTException;
import model.type.IType;

import java.io.IOException;
import java.util.HashMap;

public interface MyIDictionary<K, V> {
    void put(K key, V value) throws ADTException;

    void remove(K key) throws IOException, ADTException;

    boolean isDefined(K key);

    V getValue(K key) throws ADTException;

    void update(K key, V value) throws ADTException;

    IType getType(K key) throws ADTException;

    HashMap<K, V> getContent();

    void setContent(HashMap<K, V> map);

    MyIDictionary<K, V> deepCopy();

}
