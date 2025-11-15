package model.prg.adt;

import exceptions.ADTException;
import model.type.IType;
import model.values.IValue;

import java.util.HashMap;
import java.util.Map;

public class SymTable implements MyIDictionary<String, IValue> {
    private HashMap<String, IValue> dict;

    public SymTable() {
        dict = new HashMap<>();
    }

    @Override
    public void put(String key, IValue value) {
        if (this.isDefined(key)) throw new ADTException("The given key is already defined! ");
        dict.put(key, value);
    }

    @Override
    public void remove(String key) {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        dict.remove(key);
    }

    @Override
    public boolean isDefined(String key) {
        return dict.containsKey(key);
    }

    @Override
    public IValue getValue(String key) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        return dict.get(key);
    }

    @Override
    public IType getType(String key) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        return dict.get(key).getType();
    }

    @Override
    public Map<String, IValue> getContent() {
        return dict;
    }

    @Override
    public void setContent(Map<String, IValue> map) {
        dict = (HashMap<String, IValue>) map;
    }

    @Override
    public void update(String key, IValue value) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        dict.replace(key, value);
    }

    @Override
    public String toString() {
        if (dict.isEmpty()) return "The Symbol Table is empty! \n";
        StringBuilder result = new StringBuilder("The Symbol Table is as following: \n    ");
        for (Map.Entry<String, IValue> e : dict.entrySet())
            result.append("Key: ").append(e.getKey()).append(" - Value: ").append(e.getValue()).append("\n    ");
        return result.toString();
    }
}
