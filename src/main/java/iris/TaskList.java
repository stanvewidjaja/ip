package iris;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<Task>();
    }

    public void add(Task t) {
        tasks.add(t);
    }

    public Task remove(int index) {
        return tasks.remove(index);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public TaskList findByKeyword(String keyword) {
        TaskList found = new TaskList();
        String needle = keyword.toLowerCase();

        for (int i = 0; i < this.size(); i++) {
            Task t = this.get(i);
            if (t.getDescription().toLowerCase().contains(needle)) {
                found.add(t);
            }
        }
        return found;
    }

}
