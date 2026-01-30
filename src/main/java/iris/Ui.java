package iris;

/**
 * An abstraction to help formatting output.
 * Prints error messages, changes to tasks, and
 * tasks in TaskList.
 * Also formats them into boxes.
 */
public class Ui {
    static String LINE = "________________________" +
            "____________________________________\n";

    private static void showBox(String content) {
        System.out.print(LINE);
        System.out.print(content);
        System.out.print(LINE);
    }

    /**
     * Prints all Tasks in TaskList.
     *
     * @param taskList Wrapper for ArrayList of Tasks
     */
    public static void showTaskList(TaskList taskList) {
        String listMsg = "Your tasks, printed:\n";
        for (int i = 0; i < taskList.size(); i++) {
            listMsg += (i + 1) + "." + taskList.get(i) + "\n";
        }
        showBox(listMsg);
    }

    /**
     * Prints a deleted Task.
     *
     * @param task deleted task
     * @param taskList TaskList with one task deleted
     */
    public static void showDeleteTask(Task task, TaskList taskList) {
        String deleteMsg = "Noted. I have removed this task:\n";
        deleteMsg += "  " + task + "\n"
                + "Now you have " + taskList.size() + " tasks in the list.\n";
        showBox(deleteMsg);
    }

    /**
     * Prints a marked Task.
     *
     * @param task marked task
     */
    public static void showMarkTask(Task task) {
        String markMsg = "You have done the task. Good job!\n";
        markMsg += "  " + task + "\n";
        showBox(markMsg);
    }

    /**
     * Prints an unmarked Task.
     *
     * @param task unmarked task
     */
    public static void showUnmarkTask(Task task) {
        String unmarkMsg = "OK, I have marked it as not done.\n";
        unmarkMsg += "  " + task + "\n";
        showBox(unmarkMsg);
    }

    /**
     * Prints a task that was just added.
     *
     * @param task added task
     * @param taskList TaskList with one added task
     */
    public static void showNewTask(Task task, TaskList taskList) {
        String addMessage = "Okay. I've added this task:\n  " + task
                + "\nNow you have " + taskList.size() + " tasks in the list.\n";
        showBox(addMessage);
    }

    /**
     * Prints the greeting message.
     */
    public static void showGreetMsg() {
        String greetMsg =
                "Hello! I'm Iris!\nWhat can I do for you?\n";
        showBox(greetMsg);
    }

    /**
     * Prints an IrisException: an Exception due to a mismatched
     * user input.
     *
     * @param ie IrisException that is passed and caught
     */
    public static void showIrisException(IrisException ie) {
        showBox(ie.getMessage() + "\n");
    }

    /**
     * Prints an IrisException without the box.
     *
     * @param ie IrisException that is passed and caught
     */
    public static void showIrisExceptionWithoutBox(IrisException ie) {
        System.out.println(ie.getMessage());
    }

    /**
     * Prints the exit message.
     */
    public static void showExitMsg() {
        String exitMsg = "Bye. Hope to see you again soon!\n"
                + LINE;
        System.out.print(exitMsg);
    }
}
