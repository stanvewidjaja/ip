package iris;

import java.util.Scanner;

/**
 * Our main class.
 * It handles execution (for now), and contains the entry point
 * into the program.
 */
public class Iris {
    private TaskList taskList;

    private static String handleMarkLike(String inp, TaskList taskList, String markOrUnmark) throws IrisException {
        int index = Parser.parseIndexOrThrow(inp, taskList);
        Task task = taskList.get(index);
        boolean isMark = markOrUnmark.equals("mark");

        if (isMark) {
            task.markDone();
            Storage.saveTasksToFile(taskList);
            return Ui.renderMarkTask(task);
        } else {
            task.markUndone();
            Storage.saveTasksToFile(taskList);
            return Ui.renderUnmarkTask(task);
        }
    }

    private static String handleDelete(String inp, TaskList taskList) throws IrisException {
        int index = Parser.parseIndexOrThrow(inp, taskList);
        Task task = taskList.remove(index);
        Storage.saveTasksToFile(taskList);
        return Ui.renderDeleteTask(task, taskList);
    }

    private static String handleFind(String inp, TaskList taskList) throws IrisException {
        String keyword = Parser.parseFindKeywordOrThrow(inp);
        TaskList found = taskList.findByKeyword(keyword);
        return Ui.renderFoundTasksList(found);
    }

    private static String handleCreateTask(String inp, TaskList taskList) throws IrisException {
        Task newTask = Parser.processTask(inp);
        taskList.add(newTask);
        Storage.saveTasksToFile(taskList);
        return Ui.renderNewTask(newTask, taskList);
    }

    private static String processInput(String inp, TaskList taskList) throws IrisException {
        String command = Parser.getCommand(inp);
        if (command.isEmpty()) {
            throw new IrisException("Please enter a command.");
        }

        boolean isTaskCommand = Parser.isTaskCreationCommand(command);
        boolean isMarkLikeCommand = Parser.isMarkLikeCommand(command);

        if (command.equals("bye")) {
            return Ui.renderExitMsg();
        } else if (command.equals("list")) {
            return Ui.renderTaskList(taskList);
        } else if (command.equals("find")) {
            return handleFind(inp, taskList);
        } else if (command.equals("delete")) {
            return handleDelete(inp, taskList);
        } else if (isMarkLikeCommand) {
            return handleMarkLike(inp, taskList, command);
        } else if (isTaskCommand) {
            return handleCreateTask(inp, taskList);
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
