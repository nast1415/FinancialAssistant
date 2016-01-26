package ru.spbau.mit.starlab.financialassistant.fragments;

import org.junit.Test;

import java.lang.String;
import java.util.Calendar;

import ru.spbau.mit.starlab.financialassistant.fragments.ShowStatisticsFragment;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ShowStatisticsTest {
    public static ShowStatisticsFragment fragment = new ShowStatisticsFragment();
    @Test
    public void testFindMinDate() throws Exception {
        String[] dates = new String[10];
        for (int i = 0; i < 5; i++) {
            dates[i] = "0" + (i + 6) + ".01.2016";
        }
        for (int i = 5; i < 10; i++) {
            dates[i] = "0" + (i - 4) + ".01.2016";
        }
        Calendar resCal = fragment.findMinDate(dates);
        assertEquals(2016, resCal.get(Calendar.YEAR));
        assertEquals(0, resCal.get(Calendar.MONTH));
        assertEquals(1, resCal.get(Calendar.DATE));
    }

    @Test
    public void testGetSumOnDay() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2016, 0, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String[] dates = {"01.01.2016", "02.02.2016", "01.01.2016", "01.02.2016", "02.01.2016"};
        double[] sums = {1, 2, 3, 4, 5};
        assertEquals(4, fragment.getSumOnDay(cal, dates, sums));
    }
}