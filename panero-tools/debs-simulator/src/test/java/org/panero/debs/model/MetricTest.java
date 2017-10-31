package org.panero.debs.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MetricTest {
    @Test
    public void test_create_object() throws Exception {
        final String test = "2967740693 ,1379879533 ,82.042 ,0 ,1 ,0 ,12";
        final Metric measure = Metric.create().parseLine(test).and().buildIt();
        assertEquals(2967740693L, measure.getId().longValue());
        assertEquals(1379879533L, measure.getTimestamp().longValue());
        assertEquals(82.042d, measure.getValue(), 0);
        assertEquals(0, measure.getProperty().intValue());
        assertEquals(1, measure.getPlugId().intValue());
        assertEquals(0, measure.getHouseholdId().intValue());
        assertEquals(12, measure.getHouseId().intValue());
    }
}
