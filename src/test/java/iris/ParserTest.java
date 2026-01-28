package iris;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void processTask_todo_success() throws Exception {
        Task t = Parser.processTask("todo read book");
        assertTrue(t instanceof Todo);
        assertTrue(t.toString().contains("read book"));
    }

    @Test
    public void processTask_todo_empty_throws() {
        assertThrows(IrisException.class, () -> Parser.processTask("todo"));
    }

    @Test
    public void processTask_deadline_success() throws Exception {
        Task t = Parser.processTask("deadline return book /by 2026-01-28");

        assertTrue(t instanceof Deadline, "Expected Deadline but got: " + t.getClass() + " with toString: " + t);

        assertTrue(t.toString().contains("return book"),
                "Expected description to appear, but toString was: " + t);
    }

}

