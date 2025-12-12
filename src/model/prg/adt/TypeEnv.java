package model.prg.adt;

import exceptions.ADTException;
import model.type.IType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeEnv implements MyIDictionary<String,IType>{
    private HashMap<String, IType> dict;

    public TypeEnv() {
        dict = new HashMap<>();
    }

    @Override
    public void put(String key, IType value) throws ADTException {
        if (this.isDefined(key)) throw new ADTException("The given key is already defined! ");
        dict.put(key, value);
    }

    @Override
    public void remove(String key) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        dict.remove(key);
    }

    @Override
    public boolean isDefined(String key) {
        return dict.containsKey(key);
    }

    @Override
    public IType getValue(String key) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        return dict.get(key);
    }

    @Override
    public IType getType(String key) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        return dict.get(key);
    }

    @Override
    public HashMap<String, IType> getContent() {
        return dict;
    }

    @Override
    public void setContent(HashMap<String, IType> map) {
        dict = map;
    }

    @Override
    public void update(String key, IType value) throws ADTException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        dict.replace(key, value);
    }

    @Override
    public String toString() {
        if (dict.isEmpty()) return "The Symbol Table is empty! \n";
        StringBuilder result = new StringBuilder("The Symbol Table is as following: \n    ");
        for (Map.Entry<String, IType> e : dict.entrySet())
            result.append("Key: ").append(e.getKey()).append(" - Value: ").append(e.getValue()).append("\n    ");
        return result.toString();
    }

    public MyIDictionary<String, IType> deepCopy() {
        HashMap<String, IType> hash = dict.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().deepCopy(),
                        (v1, _) -> v1,
                        HashMap::new));
        TypeEnv newTable = new TypeEnv();
        newTable.setContent(hash);
        return newTable;
    }
}

