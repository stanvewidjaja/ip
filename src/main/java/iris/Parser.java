package iris;

import java.time.LocalDate;

public class Parser {
    public static Task processTask(String inp) throws IrisException {
        String[] parts = inp.trim().split(" ");
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
            ret = new Deadline(restSplit[0].trim(), LocalDate.parse(restSplit[1].trim()));
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
                    LocalDate.parse(restSplitTo[0].trim()),
                    LocalDate.parse(restSplitTo[1].trim()));
        }
        return ret;
    }

    public static int parseIndexOrThrow(String inp, TaskList taskList) throws IrisException {
        inp = inp.trim();
        String[] parts = inp.split("\\s+");
        boolean isInputMissingArgument = (parts.length < 2);
        String command = parts[0];
        if (isInputMissingArgument && command.equals("delete")) {
            throw new IrisException("Please specify a task number to delete, e.g. delete 3");
        } else if (isInputMissingArgument && command.equals("mark")) {
            throw new IrisException("Please specify a task number to mark, e.g. mark 2");
        } else if (isInputMissingArgument && command.equals("unmark")) {
            throw new IrisException("Please specify a task number to mark, e.g. unmark 2");
        }
        return parseIntFromIndexCommand(inp, taskList);
    }

    private static int parseIntFromIndexCommand(String inp, TaskList taskList) throws IrisException {
        inp = inp.trim();
        String intAsString = inp.split("\\s+")[1];

        int taskNum;
        try {
            taskNum = Integer.parseInt(intAsString);
        } catch (NumberFormatException nfe) {
            throw new IrisException("Task number must be a number. You put " + intAsString + " after mark.");
        }
        if (taskNum < 1 || taskNum > taskList.size()) {
            throw new IrisException("Task number must be between 1 and " + taskList.size() + ".");
        }
        return taskNum - 1;
    }

    public static String getCommand(String inp) {
        inp = inp.trim();
        if (inp.isEmpty()) {
            return "";
        }
        return inp.split("\\s+")[0];
    }

    public static boolean isTaskCreationCommand(String command) {
        return command.equals("todo")
                || command.equals("deadline")
                || command.equals("event");
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
