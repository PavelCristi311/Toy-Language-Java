package view;

import controller.Controller;

import static java.lang.IO.print;

public class RunExample extends Command {
    private final Controller ctr;

    public RunExample(String key, String desc, Controller ctr) {
        super(key, desc);
        this.ctr = ctr;
    }

    @Override
    public void execute() {
        try {
            ctr.allStep();
        } catch (Exception e) {
            print(e.getMessage());
        }
    }
}
