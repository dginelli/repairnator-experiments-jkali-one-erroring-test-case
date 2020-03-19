package llc.zombodb.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class TestXXXArrayMergeSortIterator {

    @Test
    public void testLong() throws Exception {
        for (int cnt=0; cnt<1000; cnt++) {
            Random rnd = new Random();
            int MAX = 5000;

            long[][] longs = new long[10][];
            int[] lengths = new int[longs.length];
            for (int i = 0; i < longs.length; i++) {
                int many = Math.abs(rnd.nextInt()) % MAX;

                long[] values = new long[many];
                lengths[i] = values.length;
                for (int j = 0; j < many; j++)
                    values[j] = rnd.nextLong() % (MAX * 10);
                Arrays.sort(values);
                longs[i] = values;
            }

            LongArrayMergeSortIterator itr = new LongArrayMergeSortIterator(longs, lengths);
            assertSorted(itr, itr.getTotal());
        }
    }

    @Test
    public void testNumberArray() throws Exception {
        for (int cnt=0; cnt<1000; cnt++) {
            Random rnd = new Random();
            int MAX = 5000;


            NumberArrayLookup[] longs = new NumberArrayLookup[10];
            for (int i = 0; i < longs.length; i++) {
                int many = Math.abs(rnd.nextInt()) % MAX;

                long[] values = new long[many];
                long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
                if (many > 0) {
                    values[0] = Long.MAX_VALUE;
                    for (int j = 0; j < many; j++) {
                        if (j > 0)
                            values[j] = rnd.nextLong() % (MAX * 10);
                        if (values[j] < min)
                            min = values[j];
                        if (values[j] > max)
                            max = values[j];
                    }
                }
                longs[i] = new NumberArrayLookup(min, max);
                longs[i].setAll(values, many);

            }

            NumberArrayLookupMergeSortIterator itr = new NumberArrayLookupMergeSortIterator(longs);
            assertSorted(itr, itr.getTotal());
        }
    }

    private void assertSorted(LongIterator itr, int total) {
        long prev = -1;
        boolean first = true;
        int processed = 0;
        while (itr.hasNext()) {
            long value = itr.next();
            if (!first) {
                if (!(value >= prev)) {
                    System.err.println("NOT SORTED: current=" + value + ", prev=" + prev);
                }
                assertTrue(value >= prev);
            }

            prev = value;
            first = false;
            processed++;
        }

        assertTrue(total == processed);
    }

    @Test
    public void testString() throws Exception {
        for (int cnt=0; cnt<1000; cnt++) {
            Random rnd = new Random();
            int MAX = 250;

            String[][] strings = new String[10][];
            int[] lengths = new int[strings.length];
            for (int i = 0; i < strings.length; i++) {
                int many = Math.abs(rnd.nextInt()) % MAX;

                String[] values = new String[many];
                lengths[i] = values.length;
                for (int j = 0; j < many; j++)
                    values[j] = String.valueOf(rnd.nextDouble());
                Arrays.sort(values);
                strings[i] = values;
            }

            StringArrayMergeSortIterator itr = new StringArrayMergeSortIterator(strings, lengths);
            String prev = null;
            boolean first = true;
            int processed = 0, total = itr.getTotal();
            while (itr.hasNext()) {
                String value = itr.next();
                if (!first) {
                    if (!(value.compareTo(prev) >= 0)) {
                        System.err.println("NOT SORTED: current=" + value + ", prev=" + prev);
                    }
                    assertTrue(value.compareTo(prev) >= 0);
                }

                prev = value;
                first = false;
                processed++;
            }

            assertTrue(total == processed);
        }
    }
}
