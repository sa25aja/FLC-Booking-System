package test;

import org.junit.jupiter.api.*;
import core.*;
import logic.*;

import static org.junit.jupiter.api.Assertions.*;

public class SystemManagerTest {

    private SystemManager manager;
    private Session s1, s2;
    private User u1;

    @BeforeEach
    void init() {
        manager = new SystemManager();

        s1 = new Session("Yoga", "Saturday", "Morning", 10, 5, 1);
        s2 = new Session("Zumba", "Saturday", "Afternoon", 12, 5, 1);

        manager.addSession(s1);
        manager.addSession(s2);

        u1 = new User(1, "TestUser");
    }

    // 1. Successful reservation
    @Test
    void shouldCreateReservation() {

        Booking r = manager.createReservation(u1, s1);

        assertNotNull(r);
        assertEquals(1, s1.summary().contains("1/4") ? 1 : 0);
    }

    // 2. Prevent duplicate reservation
    @Test
    void shouldNotAllowDuplicateReservation() {

        manager.createReservation(u1, s1);
        Booking second = manager.createReservation(u1, s1);

        assertNull(second);
    }

    // 3. Capacity should not exceed 4
    @Test
    void shouldRespectCapacityLimit() {

        for (int i = 1; i <= 5; i++) {
            manager.createReservation(new User(i, "U" + i), s1);
        }

        // count via summary string
        assertTrue(s1.summary().contains("4/4"));
    }

    // 4. Change reservation successfully
    @Test
    void shouldUpdateReservation() {

        Booking r = manager.createReservation(u1, s1);

        boolean result = manager.updateReservation(r.getId(), s2);

        assertTrue(result);
    }

    // 5. Rating should be recorded only once per reservation
    @Test
    void shouldRecordSingleCompletion() {

        Booking r = manager.createReservation(u1, s1);

        manager.finishSession(r.getId(), 5, "Good");
        manager.finishSession(r.getId(), 2, "Ignored");

        assertEquals(1, s1.getParticipantCount());
    }
}