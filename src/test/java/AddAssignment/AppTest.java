package AddAssignment;

import org.junit.Test;
import lab2testing.repository.TemaXMLRepository;
import lab2testing.service.Service;
import lab2testing.validation.TemaValidator;

import static junit.framework.TestCase.assertEquals;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private TemaXMLRepository fileRepository2 =
            new TemaXMLRepository(new TemaValidator(), "teme_test.xml");
    private Service service = new Service(null, fileRepository2, null);

    @Test
    public void addAssignmentWithEmptyId() {
        assert 2 == service.saveTema("", "Scurt.", 10, 2);
    }

    @Test
    public void addAssignmentWithNullId() {
        assert 2 == service.saveTema(null, "Scurt.", 10, 2);
    }

    @Test
    public void addAssignmentWithOkId() {
        assert 0 == service.saveTema("1", "Scurt.", 10, 2);
    }

    @Test
    public void addAssignmentWithEmptyDescription() {
        assert 2 == service.saveTema("2", "", 10, 2);
    }

    @Test
    public void addAssignmentWithNullDescription() {
        assert 2 == service.saveTema("2", null, 10, 2);
    }

    @Test
    public void addAssignmentWithOkDescription() {
        assertEquals(0, service.saveTema("2", "Scurt/2.", 10, 2));
    }

    @Test
    public void addAssignmentWithInvalidStartlines() {
        assert 2 == service.saveTema("3", "Scurt/2.", 10, -1);
    }

    @Test
    public void addAssignmentWithInvalidStartline_() {
        assert 2 == service.saveTema("3", "Scurt/2.", 10, 15);
    }

    @Test
    public void addAssignmentWithInvalidDeadlines() {
        assert 2 == service.saveTema("3", "Scurt/2.", -1, 1);
    }

    @Test
    public void addAssignmentWithInvalidDeadline_() {
        assert 2 == service.saveTema("3", "Scurt/2.", 15, 1);
    }

    @Test
    public void addAssignmentWithIncorrectInverval() {
        assert 2 == service.saveTema("3", "Scurt.", 1, 7);
    }

    @Test
    public void addAssignmentWithOkInterval() {
        assert 0 == service.saveTema("3", "Scurt/2.", 6, 2);
    }

    @Test
    public void addAlreadyExistentAssignment() {
        service.saveTema("100", "Skip", 10, 2);
        assert 0 == service.saveTema("100", "Scurt/2.", 6, 2);
    }

    @Test
    public void addNewAssignment(){
        service.deleteTema("100");
        assert 1 == service.saveTema("100", "Skip", 10, 2);
    }
}
