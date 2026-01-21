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

        System.out.print(message);

        Scanner sc = new Scanner(System.in);

        String[] taskList = new String[100];
        int index = 0;
        while (true) {
            String inp = sc.nextLine();
            if (inp.equals("bye")) {
                break;
            } else if (inp.equals("list")) {
                System.out.print(LINE);
                for (int i = 0; i < index; i++) {
                    System.out.println((i + 1) + ". " + taskList[i]);
                }
                System.out.print(LINE);
            } else {
                taskList[index] = inp;
                index += 1;
                System.out.print(LINE + "added: " + inp + "\n" + LINE);
            }
        }
        String exitMsg = "Bye. Hope to see you again soon!\n"
        + LINE;
        System.out.print(exitMsg);
    }
}
