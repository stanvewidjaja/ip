package iris;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Handles all I/O processes.
 * Ensures a data file exists, reconstructs taskList from the data file,
 * and writes to it.
 */
public class Storage {
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "data/taskdata.txt";
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String DONE_FLAG = "1";

    /**
     * Checks if data file exists. If not, create it.
     */
    public static void ensureDataFileExists() {
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

    private static Task buildTaskFromLine(String line) throws IrisException {
        String[] parts = line.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        String type = parts[0];
        boolean isDone = parts[1].equals(DONE_FLAG);

        Task task;

        if (type.equals(TODO_TYPE)) {
            task = new Todo(parts[2]);
        } else if (type.equals(DEADLINE_TYPE)) {
            task = new Deadline(parts[2], LocalDate.parse(parts[3]));
        } else if (type.equals(EVENT_TYPE)) {
            task = new Event(parts[2], LocalDate.parse(parts[3]), LocalDate.parse(parts[4]));
        } else {
            throw new IrisException("WARNING: Unknown task type, skipped task " + line);
        }

        if (isDone) {
            task.markDone();
        }

        return task;
    }

    /**
     * Reconstructs the TaskList wrapper of ArrayList<Task>
     * from data file.
     * Skips corrupted lines.
     *
     * @return TaskList containing all reconstructed tasks.
     */
    public static TaskList readTasksFromFile() {
        ensureDataFileExists();
        TaskList taskList = new TaskList();

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
                } catch (IrisException ie) {
                    Ui.showIrisExceptionWithoutBox(ie);
                }
            }
        } catch (FileNotFoundException e) {
            // treat as empty list
        }

        return taskList;
    }

    /**
     * Writes TaskList to the data file. Saves data externally.
     *
     * @param taskList The wrapper TaskList.
     */
    public static void saveTasksToFile(TaskList taskList) {
        File file = new File(DATA_FILE);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (int i = 0; i < taskList.size(); i++) {
                writer.println(taskList.get(i).toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}
