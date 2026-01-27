import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;

public class Storage {
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "data/taskdata.txt";

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

    private static Task buildTaskFromLine(String line) throws Exception {
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


    public static TaskList readTasksFromFile() {
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
                } catch (Exception e) {
                    System.out.println("WARNING: " + e.getMessage() + ", skipped task " + line);
                }
            }
        } catch (FileNotFoundException e) {
            // treat as empty list
        }

        return taskList;
    }

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
