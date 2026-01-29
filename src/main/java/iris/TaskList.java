package iris;

import java.util.ArrayList;

/**
 * A wrapper for an ArrayList of Tasks.
 * All operations related to our list of tasks
 * will be done here.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<Task>();
    }

    /**
     * Add the input task into tasks.
     * @param t added task
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Remove the task with index equal to the passed param.
     * The parameter is 0-indexed.
     *
     * @param index index of removed task
     * @return removed Task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Retrieves the task with index equal to the passed param.
     * The parameter is 0-indexed.
     *
     * @param index index of retrieved task
     * @return retrieved Task
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in tasks.
     *
     * @return number of tasks in current ArrayList
     */
    public int size() {
        return tasks.size();
    }
}
