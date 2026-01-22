import java.util.Scanner;

public class Iris {
    static String LINE = "________________________" +
            "____________________________________\n";

    private static void printBox(String content) {
        System.out.print(LINE);
        System.out.print(content);
        System.out.print(LINE);
    }

    private static Task processTask(String inp) throws IrisException {
        String[] parts = inp.split(" ");
        Task ret;
        boolean isComponentMissing;
        if (parts[0].equals("todo")) {
            String rest = inp.substring("todo".length()).trim();
            isComponentMissing = rest.isEmpty();
            if (isComponentMissing) {
                throw new IrisException("Todo description cannot be empty.");
            }
            ret = new Todo(rest);
        } else if (parts[0].equals("deadline")) {
            String rest = inp.substring("deadline".length());
            String[] restSplit = rest.split("/by");
            if (restSplit.length < 2) {
                throw new IrisException("Deadline task must have a /by and a deadline after that.");
            }
            isComponentMissing = restSplit[0].trim().isEmpty()
                    || restSplit[1].trim().isEmpty();
            if (isComponentMissing) {
                throw new IrisException("Both deadline description and due date cannot be empty.");
            }
            ret = new Deadline(restSplit[0].trim(), restSplit[1].trim());
        } else {
            String rest = inp.substring("event".length());
            String[] restSplitFrom = rest.split("/from");
            if (restSplitFrom.length < 2) {
                throw new IrisException("Event task must have a /from and a beginning time.");
            }
            String[] restSplitTo = restSplitFrom[1].split("/to");
            if (restSplitTo.length < 2) {
                throw new IrisException("Event task must have a /to and an ending time.");
            }
            isComponentMissing = restSplitFrom[0].trim().isEmpty()
                    || restSplitTo[0].trim().isEmpty()
                    || restSplitTo[1].trim().isEmpty();
            if (isComponentMissing) {
                throw new IrisException("Any of these: event description, " +
                        "event from-field (begin time), and event to-field (end time), " +
                        "cannot be empty.");
            }
            ret = new Event(restSplitFrom[0].trim(),
                    restSplitTo[0].trim(),
                    restSplitTo[1].trim());
        }
        return ret;
    }

    private static int processInput(String inp, Task[] taskList, int index) throws IrisException {
        String[] parts = inp.split(" ");
        String command = parts[0];
        boolean isTaskCommand = command.equals("todo")
                || command.equals("deadline")
                || command.equals("event");
        if (inp.equals("list")) {
            String listMsg = "Your tasks, printed:\n";
            for (int i = 0; i < index; i++) {
                listMsg += (i + 1) + "." + taskList[i].toString() + "\n";
            }
            printBox(listMsg);
        } else if (parts[0].equals("mark")) {
            String markMsg = "You have done the task. Good job!\n";
            if (parts.length < 2) {
                throw new IrisException("Please specify a task number to mark, e.g. mark 2");
            }
            int taskNum;
            try {
                taskNum = Integer.parseInt(parts[1]);
            } catch (NumberFormatException nfe) {
                throw new IrisException("Task number must be a number. You put " + parts[1] + " after mark.");
            }
            if (taskNum < 1 || taskNum > index) {
                throw new IrisException("Task number must be between 1 and " + index + ".");
            }
            Task task = taskList[taskNum - 1];
            task.markDone();
            markMsg += "  " + task + "\n";
            printBox(markMsg);
        } else if (parts[0].equals("unmark")) {
            String unmarkMsg = "OK, I have marked it as not done.\n";
            if (parts.length < 2) {
                throw new IrisException("Please specify a task number to mark, e.g. mark 2");
            }
            int taskNum;
            try {
                taskNum = Integer.parseInt(parts[1]);
            } catch (NumberFormatException nfe) {
                throw new IrisException("Task number must be a number. You put " + parts[1] + " after mark.");
            }
            if (taskNum < 1 || taskNum > index) {
                throw new IrisException("Task number must be between 1 and " + index + ".");
            }
            Task task = taskList[taskNum - 1];
            task.markUndone();
            unmarkMsg += "  " + task + "\n";
            printBox(unmarkMsg);
        } else if (isTaskCommand) {
            Task newTask = processTask(inp);
            taskList[index] = newTask;
            index += 1;
            String addMessage = "Okay. I've added this task:\n  " + newTask
                    + "\nNow you have " + index + " tasks in the list.\n";
            printBox(addMessage);
        } else {
            throw new IrisException("I don't recognize that command.");
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
