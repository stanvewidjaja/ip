
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;

public class Iris {


    private Task processTask(String inp) throws IrisException {
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

    private int parseInt(String intAsString, ArrayList<Task> taskList) throws IrisException {
        int taskNum;
        try {
            taskNum = Integer.parseInt(intAsString);
        } catch (NumberFormatException nfe) {
            throw new IrisException("Task number must be a number. You put " + intAsString + " after mark.");
        }
        if (taskNum < 1 || taskNum > taskList.size()) {
            throw new IrisException("Task number must be between 1 and " + taskList.size() + ".");
        }
        return taskNum;
    }

    private void processInput(String inp, ArrayList<Task> taskList) throws IrisException {
        String[] parts = inp.split(" ");
        String command = parts[0];
        boolean isTaskCommand = command.equals("todo")
                || command.equals("deadline")
                || command.equals("event");
        if (inp.equals("list")) {
            Ui.showTaskList(taskList);
        } else if (parts[0].equals("delete")) {
            if (parts.length < 2) {
                throw new IrisException("Please specify a task number to delete, e.g. delete 3");
            }
            Task task = taskList.remove(parseInt(parts[1], taskList) - 1);
            Ui.showDeleteTask(task, taskList);
            Storage.saveTasksToFile(taskList);
        } else if (parts[0].equals("mark")) {
            if (parts.length < 2) {
                throw new IrisException("Please specify a task number to mark, e.g. mark 2");
            }
            Task task = taskList.get(parseInt(parts[1], taskList) - 1);
            task.markDone();
            Ui.showMarkTask(task);
            Storage.saveTasksToFile(taskList);
        } else if (parts[0].equals("unmark")) {
            if (parts.length < 2) {
                throw new IrisException("Please specify a task number to mark, e.g. unmark 2");
            }

            Task task = taskList.get(parseInt(parts[1], taskList) - 1);
            task.markUndone();
            Ui.showUnmarkTask(task);
            Storage.saveTasksToFile(taskList);
        } else if (isTaskCommand) {
            Task newTask = processTask(inp);
            taskList.add(newTask);
            Ui.showNewTask(newTask, taskList);
            Storage.saveTasksToFile(taskList);
        } else {
            throw new IrisException("I don't recognize that command.");
        }
    }

    public static void main(String[] args) {
        Storage.ensureDataFileExists();
        Ui.showGreetMsg();

        Scanner sc = new Scanner(System.in);

        ArrayList<Task> taskList = Storage.readTasksFromFile();
        while (true) {
            String inp = sc.nextLine();
            if (inp.equals("bye")) {
                break;
            }
            try {
                Iris iris = new Iris();
                iris.processInput(inp, taskList);
            } catch (IrisException ie) {
                Ui.showIrisException(ie);
            }
        }

        Ui.showExitMsg();
    }
}
