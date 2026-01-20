import java.util.Scanner;

public class Iris {
    public static void main(String[] args) {
        String message = "";
        String LINE = "________________________" +
                "____________________________________\n";
        message += LINE;
        message += "Hello! I'm Iris!\n";
        message += "What can I do for you?\n";
        message += LINE;

        System.out.println(message);

        Scanner sc = new Scanner(System.in);
        while (true) {
            String inp = sc.nextLine();
            if (inp.equals("bye")) {
                break;
            }
            System.out.println(LINE + inp + "\n" + LINE);
        }
        String exit_msg = "Bye. Hope to see you again soon!\n"
        + LINE;
        System.out.println(exit_msg);
    }
}
