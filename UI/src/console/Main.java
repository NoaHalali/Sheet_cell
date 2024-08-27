package console;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to the Shticell!");
        Controller controller = new ControllerImpl();
        controller.runSystem();
        System.out.println("Bye Bye!");

    }

}
