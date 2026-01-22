import java.util.Scanner;

public class Iris {
    static String LINE = "________________________" +
            "____________________________________\n";

    private static void printBox(String content) {
        System.out.print(LINE);
        System.out.print(content);
        System.out.print(LINE);
    }

    private static Task processTask(String inp) {
        String[] parts = inp.split(" ");
        Task ret;
        if (parts[0].equals("todo")) {
            String rest = inp.substring("todo".length()).trim();
            ret = new Todo(rest);
        } else if (parts[0].equals("deadline")) {
            String rest = inp.substring("deadline".length()).trim();
            String[] restSplit = rest.split("/by");
            ret = new Deadline(restSplit[0].trim(), restSplit[1].trim());
        } else {
            String rest = inp.substring("event".length()).trim();
            String[] restSplitFrom = rest.split("/from");
            String[] restSplitTo = restSplitFrom[1].split("/to");
            ret = new Event(restSplitFrom[0].trim(),
                    restSplitTo[0].trim(),
                    restSplitTo[1].trim());
        }
        return ret;
    }

    private static int processInput(String inp, Task[] taskList, int index) throws IrisException {
        String[] parts = inp.split(" ");
        if (inp.equals("list")) {
            String listMsg = "Your tasks, printed:\n";
            for (int i = 0; i < index; i++) {
                listMsg += (i + 1) + "." + taskList[i].toString() + "\n";
            }
            printBox(listMsg);
        } else if (parts[0].equals("mark")) {
            String markMsg = "You have done the task. Good job!\n";
            int taskNum = Integer.parseInt(parts[1]);
            Task task = taskList[taskNum - 1];
            task.markDone();
            markMsg += "  " + task + "\n";
            printBox(markMsg);
        } else if (parts[0].equals("unmark")) {
            String unmarkMsg = "OK, I have marked it as not done.\n";
            int taskNum = Integer.parseInt(parts[1]);
            Task task = taskList[taskNum - 1];
            task.markUndone();
            unmarkMsg += "  " + task + "\n";
            printBox(unmarkMsg);
        } else {
            Task newTask = processTask(inp);
            taskList[index] = newTask;
            index += 1;
            String addMessage = "Okay. I've added this task:\n  " + newTask
                    + "\nNow you have " + index + " tasks in the list.\n";
            printBox(addMessage);
        }
        return index;
    }

    public static void main(String[] args) {
        String greetMsg =
                "Hello! I'm Iris!\n"
                + "What can I do for you?\n";
        printBox(greetMsg);

        Scanner sc = new Scanner(System.in);

        Task[] taskList = new Task[100];
        int index = 0;
        while (true) {
            String inp = sc.nextLine();
            if (inp.equals("bye")) {
                break;
            }
            try {
                index = processInput(inp, taskList, index);
            } catch (IrisException ie) {
                printBox(ie.getMessage() + "\n");
            }
        }
        String exitMsg = "Bye. Hope to see you again soon!\n"
        + LINE;
        System.out.print(exitMsg);
    }
}
