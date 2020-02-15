import org.junit.Before;
import org.junit.Test;
import models.Location;

import static org.junit.Assert.assertEquals;

public class LocationTest {
    Location location, test1, test2, test3
        , test4, test5;

    @Before
    public void setup() {
        location = new Location(2, 2);
        test1 = new Location(0, 0);
        test2 = new Location(-2, -4);
        test3 = new Location(2, -1);
        test4 = new Location(0.5, 0);
        test5 = new Location(2, 2);
    }

    @Test
    public void testGetDistance() {
        assertEquals(2 * Math.sqrt(2), location.getDistance(test1), 0.001);
        assertEquals(Math.sqrt(52), location.getDistance(test2), 0.001);
        assertEquals(3, location.getDistance(test3), 0.001);
        assertEquals(2.5, location.getDistance(test4), 0.001);
        assertEquals(0, location.getDistance(test5), 0.001);
    }
}
