package model.prg.adt;

import exceptions.ADTException;
import model.type.IType;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTable implements MyIDictionary<StringValue, BufferedReader> {
    private HashMap<StringValue, BufferedReader> dict;

    public FileTable() {
        dict = new HashMap<>();
    }

    @Override
    public void put(StringValue key, BufferedReader value) {
        if (this.isDefined(key)) throw new ADTException("The given key is already defined!");
        dict.put(key, value);
    }

    @Override
    public void remove(StringValue key) throws IOException {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        //noinspection EmptyTryBlock
        try (BufferedReader r = dict.remove(key)) {
        }
    }

    @Override
    public boolean isDefined(StringValue key) {
        return dict.containsKey(key);
    }

    @Override
    public BufferedReader getValue(StringValue key) {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        return dict.get(key);
    }

    @Override
    public void update(StringValue key, BufferedReader value) {
        if (!this.isDefined(key)) throw new ADTException("The given key is not defined! ");
        dict.replace(key, value);
    }

    @Override
    public IType getType(StringValue key) {
        return null;
    }

    @Override
    public Map<StringValue, BufferedReader> getContent() {
        return dict;
    }

    @Override
    public void setContent(Map<StringValue, BufferedReader> map) {
        dict = (HashMap<StringValue, BufferedReader>) map;
    }

    public String toString() {
        if (dict.isEmpty()) return "The File Table is empty! \n";
        StringBuilder result = new StringBuilder("The Symbol Table is as following: \n    ");
        for (Map.Entry<StringValue, BufferedReader> e : dict.entrySet())
            result.append("Key: ").append(e.getKey()).append(" - Value: ").append(e.getValue()).append("\n    ");
        return result.toString();
    }


}
