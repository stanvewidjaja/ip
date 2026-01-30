package iris;

public class Ui {
    static String LINE = "________________________" +
            "____________________________________\n";

    private static void showBox(String content) {
        System.out.print(LINE);
        System.out.print(content);
        System.out.print(LINE);
    }

    public static void showTaskList(TaskList taskList) {
        String listMsg = "Your tasks, printed:\n";
        for (int i = 0; i < taskList.size(); i++) {
            listMsg += (i + 1) + "." + taskList.get(i) + "\n";
        }
        showBox(listMsg);
    }

    public static void showDeleteTask(Task task, TaskList taskList) {
        String deleteMsg = "Noted. I have removed this task:\n";
        deleteMsg += "  " + task + "\n"
                + "Now you have " + taskList.size() + " tasks in the list.\n";
        showBox(deleteMsg);
    }

    public static void showMarkTask(Task task) {
        String markMsg = "You have done the task. Good job!\n";
        markMsg += "  " + task + "\n";
        showBox(markMsg);
    }

    public static void showUnmarkTask(Task task) {
        String unmarkMsg = "OK, I have marked it as not done.\n";
        unmarkMsg += "  " + task + "\n";
        showBox(unmarkMsg);
    }

    public static void showNewTask(Task task, TaskList taskList) {
        String addMessage = "Okay. I've added this task:\n  " + task
                + "\nNow you have " + taskList.size() + " tasks in the list.\n";
        showBox(addMessage);
    }

    public static void showGreetMsg() {
        String greetMsg =
                "Hello! I'm Iris!\nWhat can I do for you?\n";
        showBox(greetMsg);
    }

    public static void showIrisException(IrisException ie) {
        showBox(ie.getMessage() + "\n");
    }

    public static void showExitMsg() {
        String exitMsg = "Bye. Hope to see you again soon!\n"
                + LINE;
        System.out.print(exitMsg);
    }

    /**
     * Prints found tasks in a "found" TaskList
     * that contains tasks with a certain keyword.
     *
     * @param found TaskList containing tasks with a keyword
     */
    public static void showFoundTasksList(TaskList found) {
        if (found.size() == 0) {
            showBox("(No matching tasks found)");
        }

        String foundMsg = "Here are the matching tasks in your list:\n";
        for (int i = 0; i < found.size(); i++) {
            foundMsg += (i + 1) + "." + found.get(i) + "\n";
        }
        showBox(foundMsg);
    }
}
