package iris;

import java.util.Scanner;

/**
 * Our main class.
 * It handles execution (for now), and contains the entry point
 * into the program.
 */
public class Iris {
    private static void processInput(String inp, TaskList taskList) throws IrisException {
        String command = Parser.getCommand(inp);
        boolean isTaskCommand = Parser.isTaskCreationCommand(command);

        if (command.equals("list")) {
            Ui.showTaskList(taskList);
        } else if (command.equals("delete")) {
            int index = Parser.parseIndexOrThrow(inp, taskList);
            Task task = taskList.remove(index);
            Ui.showDeleteTask(task, taskList);
            Storage.saveTasksToFile(taskList);
        } else if (command.equals("mark")) {
            int index = Parser.parseIndexOrThrow(inp, taskList);
            Task task = taskList.get(index);
            task.markDone();
            Ui.showMarkTask(task);
            Storage.saveTasksToFile(taskList);
        } else if (command.equals("unmark")) {
            int index = Parser.parseIndexOrThrow(inp, taskList);
            Task task = taskList.get(index);
            task.markUndone();
            Ui.showUnmarkTask(task);
            Storage.saveTasksToFile(taskList);
        } else if (isTaskCommand) {
            Task newTask = Parser.processTask(inp);
            taskList.add(newTask);
            Ui.showNewTask(newTask, taskList);
            Storage.saveTasksToFile(taskList);
        } else if (command.isEmpty()) {
            throw new IrisException("Please enter a command.");
        } else {
            throw new IrisException("I don't recognize that command.");
        }
    }

    public static void main(String[] args) {
        Storage.ensureDataFileExists();
        Ui.showGreetMsg();

        Scanner sc = new Scanner(System.in);

        TaskList taskList = Storage.readTasksFromFile();
        while (true) {
            String inp = sc.nextLine();
            if (inp.equals("bye")) {
                break;
            }
            try {
                processInput(inp, taskList);
            } catch (IrisException ie) {
                Ui.showIrisException(ie);
            }
        }

        sc.close();
        Ui.showExitMsg();
    }
}
