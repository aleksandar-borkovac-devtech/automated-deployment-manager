package nl.tranquilizedquality.adm.commons.gwt.ext.server.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import nl.tranquilizedquality.adm.commons.domain.PagingSearchCommand;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;

/**
 * Test for {@link AbstractService}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 feb. 2011
 */
public class AbstractServiceTest {

    /** Service that will be tested. */
    public AbstractService abstractService;

    /**
     * Test user class.
     * 
     * @author Salomo Petrus (salomo.petrus@tr-quality.com)
     * @since 3 feb. 2011
     */
    private class TestUser {

        /** The name of the user. */
        private String name;

        /**
         * @return the name
         */
        @SuppressWarnings("unused")
        public String getName() {
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        @SuppressWarnings("unused")
        public void setName(final String name) {
            this.name = name;
        }

    };

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        abstractService = new AbstractService() {

            @Override
            public PagingSearchCommand createPagingSearchCommand(final PagingLoadConfig config, final String defaultSortField) {
                return super.createPagingSearchCommand(config, defaultSortField);
            }

            @Override
            public List<String> createErrorList(final Errors errors) {
                return super.createErrorList(errors);
            }

        };
    }

    @Test
    public void testCreatePagingSearchCommandAsc() {
        final PagingLoadConfig config = new BasePagingLoadConfig();
        config.setLimit(10);
        config.setOffset(0);
        config.setSortDir(SortDir.ASC);
        config.setSortField("name");

        final PagingSearchCommand pagingSearchCommand = abstractService.createPagingSearchCommand(config, "name");

        final Integer maxResults = pagingSearchCommand.getMaxResults();
        final String orderBy = pagingSearchCommand.getOrderBy();
        final Integer start = pagingSearchCommand.getStart();
        final boolean asc = pagingSearchCommand.isAsc();

        assertEquals(new Integer(10), maxResults);
        assertEquals("name", orderBy);
        assertEquals(new Integer(0), start);
        assertTrue(asc);
    }

    @Test
    public void testCreatePagingSearchCommandDesc() {
        final PagingLoadConfig config = new BasePagingLoadConfig();
        config.setLimit(10);
        config.setOffset(0);
        config.setSortDir(SortDir.DESC);
        config.setSortField(null);

        final PagingSearchCommand pagingSearchCommand = abstractService.createPagingSearchCommand(config, "name");

        final Integer maxResults = pagingSearchCommand.getMaxResults();
        final String orderBy = pagingSearchCommand.getOrderBy();
        final Integer start = pagingSearchCommand.getStart();
        final boolean asc = pagingSearchCommand.isAsc();

        assertEquals(new Integer(10), maxResults);
        assertEquals("name", orderBy);
        assertEquals(new Integer(0), start);
        assertFalse(asc);
    }

    @Test
    public void testCreateErrorList() {
        final TestUser testUser = new TestUser();
        final Errors errors = new BindException(testUser, testUser.getClass().getName());
        errors.rejectValue("name", "errorcode", "default");
        errors.reject("errorcode", "default message");

        final List<String> errorList = abstractService.createErrorList(errors);

        assertEquals(2, errorList.size());
    }
}
