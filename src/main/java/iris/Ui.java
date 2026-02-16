package iris;

/**
 * An abstraction to help formatting output.
 * Returns error messages, changes to tasks, and
 * tasks in TaskList.
 *
 * Returns Strings to be passed to the GUI.
 */
public class Ui {
    public static String renderTaskList(TaskList taskList) {
        String listMsg = "Your tasks, printed:\n";
        for (int i = 0; i < taskList.size(); i++) {
            listMsg += (i + 1) + "." + taskList.get(i) + "\n";
        }
        return listMsg;
    }

    public static String renderDeleteTask(Task task, TaskList taskList) {
        String deleteMsg = "Noted. I have removed this task:\n";
        deleteMsg += "  " + task + "\n"
                + "Now you have " + taskList.size() + " tasks in the list.\n";
        return deleteMsg;
    }

    public static String renderMarkTask(Task task) {
        String markMsg = "You have done the task. Good job!\n";
        markMsg += "  " + task + "\n";
        return markMsg;
    }

    public static String renderUnmarkTask(Task task) {
        String unmarkMsg = "OK, I have marked it as not done.\n";
        unmarkMsg += "  " + task + "\n";
        return unmarkMsg;
    }

    public static String renderNewTask(Task task, TaskList taskList) {
        String addMessage = "Okay. I've added this task:\n  " + task
                + "\nNow you have " + taskList.size() + " tasks in the list.\n";
        return addMessage;
    }

    public static String renderGreetMsg() {
        String greetMsg =
                "Hello! I'm Iris!\nWhat can I do for you?\n"
                + "Type \"help\" to see available commands.\n";
        return greetMsg;
    }

    public static String renderIrisException(IrisException ie) {
        return "IrisException: " + ie.getMessage() + "\n";
    }

    public static String renderExitMsg() {
        return "Bye. Hope to see you again soon!";
    }

    public static String renderFoundTasksList(TaskList found) {
        if (found.size() == 0) {
            return "(No matching tasks found)\n";
        }

        String foundMsg = "Here are the matching tasks in your list:\n";
        for (int i = 0; i < found.size(); i++) {
            foundMsg += (i + 1) + "." + found.get(i) + "\n";
        }
        return foundMsg;
    }

    public static String renderHelpMessage() {
        return """
            Here are the available commands:

            list
              Shows all tasks.

            todo <description>
              Adds a todo task.
              Example: todo read book

            deadline <description> /by <yyyy-mm-dd>
              Adds a deadline task.
              Example: deadline submit report /by 2026-02-20

            event <description> /from <yyyy-mm-dd> /to <yyyy-mm-dd>
              Adds an event task.
              Example: event conference /from 2026-03-01 /to 2026-03-03

            mark <index>      Marks a task
            unmark <index>    Unmarks a task
            delete <index>    Deletes a task
            
            find <keyword>    Searches tasks

            help
              Shows this help message.

            bye
              Exits the program.
            """;
    }

}
