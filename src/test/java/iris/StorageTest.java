package iris;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {

    @Test
    void readTasksFromFile_validTodoLine_taskCreated() throws Exception {
        // Arrange
        Storage.ensureDataFileExists();

        try (FileWriter writer = new FileWriter("data/taskdata.txt")) {
            writer.write("T | 0 | read book\n");
        }

        // Act
        TaskList list = Storage.readTasksFromFile();

        // Assert
        assertEquals(1, list.size());
        Task t = list.get(0);
        assertTrue(t instanceof Todo);
        assertFalse(t.isDone());
        assertTrue(t.toString().contains("read book"));
    }
}

