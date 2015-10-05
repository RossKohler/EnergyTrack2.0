package com.example.database;

import java.util.List;
import java.util.Map;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.sqlcontainer.RowItem;
import com.vaadin.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.data.util.sqlcontainer.query.generator.DefaultSQLGenerator;
import com.vaadin.data.util.sqlcontainer.query.generator.StatementHelper;
import com.vaadin.data.util.sqlcontainer.query.generator.filter.QueryBuilder;

public class FirebirdGenerator extends DefaultSQLGenerator {

    public enum Version {
        V1_0,
        V1_5,
        V2_0,
        V2_1,
        V2_5
    };
    
    private Version version;
    
    public FirebirdGenerator() {
        int latest = Version.values().length - 1;
        version = Version.values()[latest];
        
    }

    public FirebirdGenerator(Version version) {
        this.version = version;
    }

    /**
     * Construct a FirebirdGenerator with the specified identifiers for start and
     * end of quoted strings. The identifiers may be different depending on the
     * database engine and it's settings.
     * 
     * @param quoteStart
     *            the identifier (character) denoting the start of a quoted
     *            string
     * @param quoteEnd
     *            the identifier (character) denoting the end of a quoted string
     */
    public FirebirdGenerator(String quoteStart, String quoteEnd) {
        super(quoteStart, quoteEnd);
    }
    
    public Version getVersion() {
        return version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.addon.sqlcontainer.query.generator.DefaultSQLGenerator#
     * generateSelectQuery(java.lang.String, java.util.List,
     * com.vaadin.addon.sqlcontainer.query.FilteringMode, java.util.List, int,
     * int, java.lang.String)
     */
    @Override
    public StatementHelper generateSelectQuery(String tableName, List<Filter> filters, List<OrderBy> orderBys, int offset, int pagelength, String toSelect) {
        if (tableName == null || tableName.trim().equals("")) {
            throw new IllegalArgumentException("Table name must be given.");
        }
        
        toSelect = toSelect == null ? "*" : toSelect;
        StatementHelper sh = new StatementHelper();
        StringBuffer query = new StringBuffer();

        /* Row count request is handled here */
        
        if ("COUNT(*)".equalsIgnoreCase(toSelect)) {
            boolean derivedTablesSupported = false;
            switch (version) {
                case V2_5:
                case V2_1:
                case V2_0:
                    derivedTablesSupported = true;
                    query.append(String.format("SELECT COUNT(*) AS \"rowcount\" FROM (SELECT * FROM %s", tableName));
                    break;
                case V1_5:
                case V1_0:
                default:
                    /* THIS COULD BE INCREDIBLY SLOW 
                     * See http://www.firebirdfaq.org/faq5/ and http://www.firebirdfaq.org/faq198/ */
                    query.append(String.format("SELECT COUNT(*) AS \"rowcount\" FROM %s", tableName));
                    break;
            }
            
            if (filters != null && !filters.isEmpty()) {
                query.append(QueryBuilder.getWhereStringForFilters(filters, sh));
            }
            if (derivedTablesSupported) {
                query.append(")");
            }
            sh.setQueryString(query.toString());
            return sh;
        }

        if (offset == 0 && pagelength == 0) {
            query.append("SELECT ").append(toSelect).append(" FROM ").append(tableName);
        } else {
            query.append(String.format("SELECT FIRST %d SKIP %d %s FROM %s", pagelength, offset, toSelect, tableName));
        }
        if (filters != null) {
            query.append(QueryBuilder.getWhereStringForFilters(filters, sh));
        }
        if (orderBys != null) {
            for (OrderBy o : orderBys) {
                generateOrderBy(query, o, orderBys.indexOf(o) == 0);
            }
            
        }
        sh.setQueryString(query.toString());
        return sh;
    }

}