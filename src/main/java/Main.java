import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String type = getType();
        String input = getInput();
        String output = getOutput();
        Operations op = new Operations();
        op.operation(input, output, type);
    }

    public static String getType() {
        System.out.println("Operation type: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
    public static String getInput() {
        System.out.println("Link to the input file");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
    public static String getOutput() {
        System.out.println("Link to the output file");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}
