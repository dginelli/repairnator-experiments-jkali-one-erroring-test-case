package com.rbmhtechnology.vind.configure;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Thomas Kurz (tkurz@apache.org)
 * @since 30.06.16.
 */
public class SearchConfigurationTest {

    @Test
    public void testConfiguration() throws URISyntaxException, IOException {
        //test with empty custom config file
        assertNull(SearchConfiguration.get("name"));
        assertEquals("value", SearchConfiguration.get("name", "value"));
        assertEquals("10", SearchConfiguration.get(SearchConfiguration.SEARCH_RESULT_PAGESIZE));
        assertEquals(10, SearchConfiguration.get(SearchConfiguration.SEARCH_RESULT_PAGESIZE, 0));
        assertEquals(2, SearchConfiguration.get("search.result.some", 2));
        assertEquals(false, SearchConfiguration.get(SearchConfiguration.SEARCH_RESULT_PAGESIZE, false));
        assertEquals(false, SearchConfiguration.get(SearchConfiguration.SEARCH_RESULT_FACET_INCLUDE_EMPTY, true));

        //test with custom
        File customConfigFile = new File(Resources.getResource("vind.properties").toURI());
        PrintWriter writer = new PrintWriter(customConfigFile, "UTF-8");
        writer.println("search.result.pagesize=20");
        writer.println("test=123");
        writer.close();

        SearchConfiguration.init();

        PrintWriter cleaner = new PrintWriter(customConfigFile, "UTF-8");
        cleaner.close();

        assertEquals(20, SearchConfiguration.get(SearchConfiguration.SEARCH_RESULT_PAGESIZE, 0));
        assertEquals(false, SearchConfiguration.get(SearchConfiguration.SEARCH_RESULT_FACET_INCLUDE_EMPTY, true));
        assertEquals(123, SearchConfiguration.get("test",0));
    }
    @Test
    public void testSystemVarConfiguration() {
        System.setProperty(SearchConfiguration.VIND_FILE_SYSTEM_PROPERTY,Resources.getResource("system.properties").getPath());

        SearchConfiguration.init();
        assertEquals(1, SearchConfiguration.get(SearchConfiguration.SEARCH_RESULT_PAGESIZE, 0));

        System.clearProperty(SearchConfiguration.VIND_FILE_SYSTEM_PROPERTY);

        SearchConfiguration.init();
    }

}