package model.prg.adt;

import java.util.ArrayList;

public class OutList implements MyIList<String> {
    private final ArrayList<String> outputList;

    public OutList() {
        outputList = new ArrayList<>();
    }

    @Override
    public void add(String elem) {
        outputList.add(elem);
    }

    @Override
    public String toString() {
        if (outputList.isEmpty()) return "The Output List is empty! \n";
        StringBuilder result = new StringBuilder("The Output List is as following: \n    ");
        for (String elem : outputList)
            result.append(elem).append("\n    ");
        return result.toString();
    }
}
