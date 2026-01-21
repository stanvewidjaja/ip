import java.util.Scanner;

public class Iris {
    static String LINE = "________________________" +
            "____________________________________\n";

    private static void printBox(String content) {
        System.out.print(LINE);
        System.out.println(content);
        System.out.print(LINE);
    }

    public static void main(String[] args) {
        String greetMsg =
                "Hello! I'm Iris!\n"
                + "What can I do for you?";
        printBox(greetMsg);

        Scanner sc = new Scanner(System.in);

        String[] taskList = new String[100];
        Boolean[] doneList = new Boolean[100];
        int index = 0;
        while (true) {
            String inp = sc.nextLine();
            String[] parts = inp.split(" ");
            if (inp.equals("bye")) {
                break;
            } else if (inp.equals("list")) {
                System.out.print(LINE);
                for (int i = 0; i < index; i++) {
                    String markBox = doneList[i] ? "[X]" : "[ ]";
                    System.out.println((i + 1) + ". " + markBox + " " + taskList[i]);
                }
                System.out.print(LINE);
            } else if (parts[0].equals("mark")) {
                System.out.print(LINE);
                System.out.println("You have done the task. Good job!");
                int taskNum = Integer.parseInt(parts[1]);
                doneList[taskNum - 1] = true;
                System.out.println("[X] " + taskList[taskNum - 1]);
                System.out.print(LINE);
            } else if (parts[0].equals("unmark")) {
                System.out.print(LINE);
                System.out.println("OK, I have marked it as not done.");
                int taskNum = Integer.parseInt(parts[1]);
                doneList[taskNum - 1] = false;
                System.out.println("[ ] " + taskList[taskNum - 1]);
                System.out.print(LINE);
            } else {
                taskList[index] = inp;
                doneList[index] = false;
                index += 1;
                System.out.print(LINE + "added: " + inp + "\n" + LINE);
            }
        }
        String exitMsg = "Bye. Hope to see you again soon!\n"
        + LINE;
        System.out.print(exitMsg);
    }
}
