package view;

import controller.Controller;

import java.util.Scanner;

import static java.lang.IO.print;

public class View {
    Controller c;

    public View(Controller givenC) {
        c = givenC;
    }

    public void mainView() {
        while (true) {
            print('\n');
            print(c.getRepo().toString());
            print("Choose the program you want to run: ");
            Scanner myObj = new Scanner(System.in);
            try{
                int index = Integer.parseInt(myObj.nextLine()) - 1;
                print("Choose one of the following: \n 1.Run one step of the program \n 2.Run the whole program \n");
                int option = Integer.parseInt(myObj.nextLine());
                if (option == 1) c.oneStep(index);
                else if (option == 2) c.allStep(index);
                else print("Invalid selection! \n");
            } catch (Exception e) {
                print("Invalid input! \n");
            }
            print("If you wish to try again,enter 1.Otherwise,enter 0.");
            try {
                int leave = Integer.parseInt(myObj.nextLine());
                if (leave == 0) break;
                if (leave != 1) {
                    print("Invalid selection,quitting! \n");
                    break;
                }
            } catch (Exception e) {
                print("Invalid input! \n ");
            }
            }
        }
}

