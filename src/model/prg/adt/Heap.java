package model.prg.adt;

import exceptions.ADTException;
import model.type.IType;
import model.values.IValue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Heap implements MyIHeap<Integer, IValue> {
    private HashMap<Integer, IValue> heap;
    private static int lastKey = 0;

    public Integer getLastKey() {
        return lastKey;
    }

    @Override
    public HashMap<Integer, IValue> getContent() {
        return heap;
    }

    @Override
    public void setContent(HashMap<Integer, IValue> map) {
        heap = map;
    }

    @Override
    public MyIDictionary<Integer, IValue> deepCopy() {
        HashMap<Integer, IValue> hash = heap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().deepCopy(),
                        (v1, v2) -> v1,
                        HashMap::new));
        Heap newHeap = new Heap();
        newHeap.setContent(hash);
        return newHeap;
    }

    synchronized public Integer getNextKey() {
        return ++lastKey;
    }

    public Heap() {
        this.heap = new HashMap<>();
    }

    @Override
    public void put(Integer key, IValue value) {
        heap.put(key, value);
    }

    @Override
    public void remove(Integer key) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        heap.remove(key);
    }

    @Override
    public boolean isDefined(Integer key) {
        return heap.containsKey(key);
    }

    @Override
    public IValue getValue(Integer key) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        return heap.get(key);
    }

    @Override
    public void update(Integer key, IValue value) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        heap.replace(key, value);
    }

    @Override
    public IType getType(Integer key) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        return heap.get(key).getType();
    }

    public String toString() {
        if (heap.isEmpty()) return "The Heap is empty! \n";
        StringBuilder result = new StringBuilder("The Heap is as following: \n    ");
        for (Map.Entry<Integer, IValue> e : heap.entrySet())
            result.append("Key: ").append(e.getKey()).append(" - Value: ").append(e.getValue()).append("\n    ");
        return result.toString();
    }
}
