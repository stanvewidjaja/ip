import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Iris {
    static String LINE = "________________________" +
            "____________________________________\n";

    private void printBox(String content) {
        System.out.print(LINE);
        System.out.print(content);
        System.out.print(LINE);
    }

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

    private void processInput(String inp, ArrayList<Task> taskList) throws IrisException {
        String[] parts = inp.split(" ");
        String command = parts[0];
        boolean isTaskCommand = command.equals("todo")
                || command.equals("deadline")
                || command.equals("event");
        if (inp.equals("list")) {
            String listMsg = "Your tasks, printed:\n";
            for (int i = 0; i < taskList.size(); i++) {
                listMsg += (i + 1) + "." + taskList.get(i).toString() + "\n";
            }
            printBox(listMsg);
        } else if (parts[0].equals("delete")) {
            String deleteMsg = "Noted. I have removed this task:\n";
            if (parts.length < 2) {
                throw new IrisException("Please specify a task number to delete, e.g. delete 3");
            }
            int taskNum;
            try {
                taskNum = Integer.parseInt(parts[1]);
            } catch (NumberFormatException nfe) {
                throw new IrisException("Task number must be a number. You put " + parts[1] + " after mark.");
            }
            if (taskNum < 1 || taskNum > taskList.size()) {
                throw new IrisException("Task number must be between 1 and " + taskList.size() + ".");
            }
            Task task = taskList.remove(taskNum - 1);
            deleteMsg += "  " + task + "\n"
                    + "Now you have " + taskList.size() + " tasks in the list.\n";
            printBox(deleteMsg);
            saveTasksToFile(taskList);
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
            if (taskNum < 1 || taskNum > taskList.size()) {
                throw new IrisException("Task number must be between 1 and " + taskList.size() + ".");
            }
            Task task = taskList.get(taskNum - 1);
            task.markDone();
            markMsg += "  " + task + "\n";
            printBox(markMsg);
            saveTasksToFile(taskList);
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
            if (taskNum < 1 || taskNum > taskList.size()) {
                throw new IrisException("Task number must be between 1 and " + taskList.size() + ".");
            }
            Task task = taskList.get(taskNum - 1);
            task.markUndone();
            unmarkMsg += "  " + task + "\n";
            printBox(unmarkMsg);
            saveTasksToFile(taskList);
        } else if (isTaskCommand) {
            Task newTask = processTask(inp);
            taskList.add(newTask);
            String addMessage = "Okay. I've added this task:\n  " + newTask
                    + "\nNow you have " + taskList.size() + " tasks in the list.\n";
            printBox(addMessage);
            saveTasksToFile(taskList);
        } else {
            throw new IrisException("I don't recognize that command.");
        }
    }

    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "data/taskdata.txt";

    private void ensureDataFileExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(DATA_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating data file");
        }
    }

    private Task buildTaskFromLine(String line) throws Exception {
        String[] parts = line.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");

        Task task;

        if (type.equals("T")) {
            task = new Todo(parts[2]);
        } else if (type.equals("D")) {
            task = new Deadline(parts[2], LocalDate.parse(parts[3]));
        } else if (type.equals("E")) {
            task = new Event(parts[2], LocalDate.parse(parts[3]), LocalDate.parse(parts[4]));
        } else {
            throw new Exception("Unknown task type");
        }

        if (isDone) {
            task.markDone();
        }

        return task;
    }


    private ArrayList<Task> readTasksFromFile() {
        ArrayList<Task> taskList = new ArrayList<Task>();

        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return taskList;
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                try {
                    Task task = buildTaskFromLine(line);
                    taskList.add(task);
                } catch (Exception e) {
                    System.out.println("WARNING: " + e.getMessage() + ", skipped task " + line);
                }
            }
        } catch (FileNotFoundException e) {
            // treat as empty list
        }

        return taskList;
    }

    private static void saveTasksToFile(ArrayList<Task> taskList) {
        File file = new File(DATA_FILE);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Task task : taskList) {
                writer.println(task.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Iris iris = new Iris();
        iris.ensureDataFileExists();

        String greetMsg =
                "Hello! I'm Iris!\n"
                + "What can I do for you?\n";
        iris.printBox(greetMsg);

        Scanner sc = new Scanner(System.in);

        ArrayList<Task> taskList = iris.readTasksFromFile();
        while (true) {
            String inp = sc.nextLine();
            if (inp.equals("bye")) {
                break;
            }
            try {
                iris.processInput(inp, taskList);
            } catch (IrisException ie) {
                iris.printBox(ie.getMessage() + "\n");
            }
        }
        String exitMsg = "Bye. Hope to see you again soon!\n"
        + LINE;
        System.out.print(exitMsg);
    }
}
