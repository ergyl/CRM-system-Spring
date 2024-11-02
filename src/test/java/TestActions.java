import java.util.Calendar;
import java.util.GregorianCalendar;
import se.yrgo.domain.Action;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestActions {

    @Test
    void futureActionsAreNotOverdue() {
        // 1. We'll set the required by date to be some time tomorrow
        GregorianCalendar requiredBy = new GregorianCalendar();
        requiredBy.add(Calendar.DAY_OF_WEEK, 1);
        Action testAction = new Action("A Test Action", requiredBy, null);

        boolean overdue = testAction.isOverdue();

        assertFalse(overdue);
    }

    @Test
    void pastActionsAreOverdue() {
        // This is Feb 01 in 1980 AD.
        GregorianCalendar requiredBy = new GregorianCalendar(1980, 1, 1, 1, 1, 1);

        Action testAction = new Action("A Test Action", requiredBy, null);

        boolean overdue = testAction.isOverdue();

        assertTrue(overdue);
    }
}
