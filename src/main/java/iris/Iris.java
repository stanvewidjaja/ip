package iris;

import java.util.Scanner;

/**
 * Our main class.
 * It handles execution (for now), and contains the entry point
 * into the program.
 */
public class Iris {
    private TaskList taskList;

    private static String processInput(String inp, TaskList taskList) throws IrisException {
        assert taskList != null : "taskList must be initialized (call init() before getResponse())";

        String command = Parser.getCommand(inp);
        assert command != null : "Parser.getCommand should not return null";

        boolean isTaskCommand = Parser.isTaskCreationCommand(command);

        if (command.equals("bye")) {
            return Ui.renderExitMsg();
        } else if (command.equals("list")) {
            return Ui.renderTaskList(taskList);
        } else if (command.equals("find")) {
            String keyword = Parser.parseFindKeywordOrThrow(inp);
            TaskList found = taskList.findByKeyword(keyword);
            return Ui.renderFoundTasksList(found);
        } else if (command.equals("delete")) {
            int index = Parser.parseIndexOrThrow(inp, taskList);
            Task task = taskList.remove(index);
            Storage.saveTasksToFile(taskList);
            return Ui.renderDeleteTask(task, taskList);
        } else if (command.equals("mark")) {
            int index = Parser.parseIndexOrThrow(inp, taskList);
            Task task = taskList.get(index);
            task.markDone();
            Storage.saveTasksToFile(taskList);
            return Ui.renderMarkTask(task);
        } else if (command.equals("unmark")) {
            int index = Parser.parseIndexOrThrow(inp, taskList);
            Task task = taskList.get(index);
            task.markUndone();
            Storage.saveTasksToFile(taskList);
            return Ui.renderUnmarkTask(task);
        } else if (isTaskCommand) {
            Task newTask = Parser.processTask(inp);
            taskList.add(newTask);
            Storage.saveTasksToFile(taskList);
            return Ui.renderNewTask(newTask, taskList);
        } else if (command.isEmpty()) {
            throw new IrisException("Please enter a command.");
        } else {
            throw new IrisException("I don't recognize that command.");
        }
    }

    public String init() {
        Storage.ensureDataFileExists();
        taskList = Storage.readTasksFromFile();
        return Ui.renderGreetMsg();
    }

    public String getResponse(String input) {
        try {
            return processInput(input, taskList);
        } catch (IrisException ie) {
            return Ui.renderIrisException(ie);
        }
    }
}
