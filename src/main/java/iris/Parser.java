package iris;

import java.time.LocalDate;

/**
 * Handles parsing of user input commands, tasks, and indices.
 * Passes output to be managed by execution system.
 */
public class Parser {
    private static final String TODO = "todo";
    private static final String DEADLINE = "deadline";
    private static final String EVENT = "event";
    private static final String BY_SEPARATOR = "/by";
    private static final String FROM_SEPARATOR = "/from";
    private static final String TO_SEPARATOR = "/to";
    private static final String DELETE = "delete";
    private static final String MARK = "mark";
    private static final String UNMARK = "unmark";

    /**
     * Creates task from user input after parsing user input.
     *
     * @param inp user input string
     * @return the created Task
     * @throws IrisException if the input is invalid
     */
    public static Task processTask(String inp) throws IrisException {
        String[] parts = inp.trim().split("\\s+");
        Task ret;
        boolean isComponentMissing;
        if (parts[0].equals(TODO)) {
            String rest = inp.substring(TODO.length()).trim();
            isComponentMissing = rest.isEmpty();
            if (isComponentMissing) {
                throw new IrisException("Todo description cannot be empty.");
            }
            ret = new Todo(rest);
        } else if (parts[0].equals(DEADLINE)) {
            String rest = inp.substring(DEADLINE.length());
            String[] restSplit = rest.split(BY_SEPARATOR);
            if (restSplit.length < 2) {
                throw new IrisException("Deadline task must have a /by and a deadline after that.");
            }
            isComponentMissing = restSplit[0].trim().isEmpty()
                    || restSplit[1].trim().isEmpty();
            if (isComponentMissing) {
                throw new IrisException("Both deadline description and due date cannot be empty.");
            }
            ret = new Deadline(restSplit[0].trim(), LocalDate.parse(restSplit[1].trim()));
        } else if (parts[0].equals(EVENT)){
            String rest = inp.substring(EVENT.length());
            String[] restSplitFrom = rest.split(FROM_SEPARATOR);
            if (restSplitFrom.length < 2) {
                throw new IrisException("Event task must have a /from and a beginning time.");
            }
            String[] restSplitTo = restSplitFrom[1].split(TO_SEPARATOR);
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
                    LocalDate.parse(restSplitTo[0].trim()),
                    LocalDate.parse(restSplitTo[1].trim()));
        } else {
            throw new IrisException("Unknown task type: " + parts[0]);
        }
        return ret;
    }

    /**
     * Parses the second user input into an integer.
     * Only called if the command modifies or deletes tasks.
     *
     * @param inp the user input
     * @param taskList the list of tasks, a wrapper for ArrayList<Task>
     * @return index, 0-indexed
     * @throws IrisException if task number is empty, or invalid second argument
     */
    public static int parseIndexOrThrow(String inp, TaskList taskList) throws IrisException {
        inp = inp.trim();
        String[] parts = inp.split("\\s+");
        boolean isInputMissingArgument = (parts.length < 2);
        String command = parts[0];
        if (isInputMissingArgument && command.equals(DELETE)) {
            throw new IrisException("Please specify a task number to delete, e.g. delete 3");
        } else if (isInputMissingArgument && command.equals(MARK)) {
            throw new IrisException("Please specify a task number to mark, e.g. mark 2");
        } else if (isInputMissingArgument && command.equals(UNMARK)) {
            throw new IrisException("Please specify a task number to unmark, e.g. unmark 2");
        }

        String intAsString = parts[1];
        int taskNum;
        try {
            taskNum = Integer.parseInt(intAsString);
        } catch (NumberFormatException nfe) {
            throw new IrisException("Task number must be a number. " +
                    "You put " + intAsString + " after " + command + ".");
        }
        if (taskNum < 1 || taskNum > taskList.size()) {
            throw new IrisException("Task number must be between 1 and " + taskList.size() + ".");
        }
        return taskNum - 1;
    }

    /**
     * Parses the first component of user input.
     * The execution handles the logic.
     *
     * @param inp the user input
     * @return a string that represents command type
     */
    public static String getCommand(String inp) {
        inp = inp.trim();
        if (inp.isEmpty()) {
            return "";
        }
        return inp.split("\\s+")[0];
    }

    /**
     * Parses if a given command is of Task creation.
     *
     * @param command the user command
     * @return boolean that indicates if this is a Task creation
     */
    public static boolean isTaskCreationCommand(String command) {
        return command.equals(TODO)
                || command.equals(DEADLINE)
                || command.equals(EVENT);
    }

    /**
     * Extracts keyword from find commands. Returns
     * everything after the "find" clause.
     *
     * @param inp the user input
     * @return keyword string
     * @throws IrisException IrisException to be thrown if user input is empty
     */
    public static String parseFindKeywordOrThrow(String inp) throws IrisException {
        String keyword = inp.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new IrisException("Please provide a keyword to find, e.g. find book");
        }
        return keyword;
    }
}
