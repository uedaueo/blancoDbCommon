/**
 * This class will be made into a separate product.
 */
package blanco.db.common;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import blanco.commons.sql.format.BlancoSqlFormatter;
import blanco.commons.sql.format.BlancoSqlFormatterException;
import blanco.commons.sql.format.BlancoSqlRule;
import blanco.commons.util.BlancoNameAdjuster;
import blanco.db.common.resourcebundle.BlancoDbCommonResourceBundle;
import blanco.db.common.stringgroup.BlancoDbDriverNameStringGroup;
import blanco.db.common.stringgroup.BlancoDbSqlInfoScrollStringGroup;
import blanco.db.common.stringgroup.BlancoDbSqlInfoTypeStringGroup;
import blanco.db.common.util.BlancoDbUtil;
import blanco.db.common.util.BlancoDbXmlSerializer;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataTableStructure;

/**
 * Generates XML intermediate file for the single table accessor based on table information obtained from a relational database.
 * 
 * The process of getting meta information from the relational database itself  is not handled by this class, but by another product, blancoDbMetaData.
 */
public abstract class BlancoDbTableMeta2Xml implements IBlancoDbProgress {
    /**
     * Prefix to be attached to the single table class.
     */
    public static final String CLASS_PREFIX = "Simple";

    /**
     * Various resource bundles.
     */
    private final BlancoDbCommonResourceBundle fBundle = new BlancoDbCommonResourceBundle();

    /**
     * Configuration information about blandoDb.
     */
    private BlancoDbSetting fDbSetting = null;

    /**
     * Whether to format auto-generated SQL statements.
     * 
     * As of 2006.12.01, the default is false.
     */
    private boolean fFormatSql = false;

    /**
     * Whether to format auto-generated SQL statements.
     * 
     * @param argFormatSql
     */
    public void setFormatSql(final boolean argFormatSql) {
        fFormatSql = argFormatSql;
    }

    /**
     * Generates XML intermediate file for the single table accessor based on table information obtained from a relational database.
     * 
     * The process of getting meta information from the relational database itself  is not handled by this class, but by another product, blancoDbMetaData.
     * 
     * @param connDef
     *            Database connection information.
     * @param blancoSqlDirectory
     *            Output destination directory.
     * @throws SQLException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws ClassNotFoundException
     */
    public void process(final BlancoDbSetting dbSetting,
            final File blancoSqlDirectory) throws SQLException {
        System.out.println(BlancoDbCommonConstants.PRODUCT_NAME + " ("
                + BlancoDbCommonConstants.VERSION + ") Auto-generation of the single table accessor: Start.");
        fDbSetting = dbSetting;
        Connection conn = null;
        try {
            conn = BlancoDbUtil.connect(dbSetting);
            BlancoDbUtil.getDatabaseVersionInfo(conn, dbSetting);

            final List<BlancoDbMetaDataTableStructure> listTables = new BlancoDbTableParser()
                    .parse(conn, dbSetting.getSchema());

            serializeTable(conn, listTables,
                    blancoSqlDirectory.getAbsolutePath());

        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.toString(), e);
        } finally {
            BlancoDbUtil.close(conn);
            conn = null;
            System.out.println("Auto-generation of the single table accessor: End.");
        }
    }

    /**
     * Writes the information collected by the table unit to an XML file.
     * 
     * @param dbInfoCollector
     * @param listTables
     * @param outputDirectoryName
     * @throws SQLException
     */
    private void serializeTable(final Connection conn,
            final List<BlancoDbMetaDataTableStructure> listTables,
            final String outputDirectoryName) throws SQLException {

        for (int index = 0; index < listTables.size(); index++) {
            final List<BlancoDbSqlInfoStructure> resultSqlInfo = new ArrayList<BlancoDbSqlInfoStructure>();

            final BlancoDbMetaDataTableStructure table = listTables.get(index);
            if (progress(index + 1, listTables.size(), table.getName()) == false) {
                break;
            }

            try {
                System.out.println("Processes the table [" + table.getName() + "].");
                processEveryTable(listTables, table, resultSqlInfo);
            } catch (StringIndexOutOfBoundsException ex) {
                System.out.println("An exception occurred in the process of processing table [" + table.getName()
                        + "]: " + ex.toString());
                ex.printStackTrace();

                // If an exception occurs, we have no choice but to process the following table.
                conn.rollback();
                continue;
            }

            // Outputs SQL information in units of tables to XML files.
            BlancoDbXmlSerializer.serialize(resultSqlInfo,
                    new File(outputDirectoryName + "/SimpleTable"
                            + BlancoNameAdjuster.toClassName(table.getName())
                            + ".xml"));
        }
    }

    /**
     * Outputs a table for each.
     * 
     * @param service
     * @param collector
     * @param metadata
     * @param document
     * @param eleRoot
     * @param table
     * @throws SQLException
     */
    private void processEveryTable(
            final List<BlancoDbMetaDataTableStructure> listTables,
            final BlancoDbMetaDataTableStructure table,
            final List<BlancoDbSqlInfoStructure> resultSqlInfo)
            throws SQLException {
        generateSelect(listTables, table, resultSqlInfo);

        // It is determined in the method whether the updatable cursor is available or not.
        generateSelectUpdatable(listTables, table, resultSqlInfo);

        generateSelectColumn(listTables, table, resultSqlInfo);

        // 2005.11.11 SelectAll method is back.
        generateSelectAll(listTables, table, resultSqlInfo);

        generateInsert(listTables, table, resultSqlInfo, false);
        generateInsert(listTables, table, resultSqlInfo, true);

        generateUpdate(listTables, table, resultSqlInfo);

        generateDelete(listTables, table, resultSqlInfo);
    }

    /**
     * For table name only, first converts it to a class name.
     * 
     * @param table
     * @return
     */
    private String getBaseClassName(final BlancoDbMetaDataTableStructure table) {
        return BlancoNameAdjuster.toClassName(table.getName());
    }

    /**
     * Generates an accessor to search for a single line.
     * 
     * @param collector
     * @param metadata
     * @param table
     * @param document
     * @param eleRoot
     * @throws SQLException
     */
    private void generateSelect(
            final List<BlancoDbMetaDataTableStructure> listTables,
            final BlancoDbMetaDataTableStructure table,
            final List<BlancoDbSqlInfoStructure> resultSqlInfo)
            throws SQLException {

        final String name = CLASS_PREFIX + getBaseClassName(table) + "Select";

        final BlancoDbSqlInfoStructure sqlInfo = new BlancoDbSqlInfoStructure();
        sqlInfo.setName(name);
        sqlInfo.setType(BlancoDbSqlInfoTypeStringGroup.ITERATOR);
        sqlInfo.setSingle(true);
        sqlInfo.setScroll(new BlancoDbSqlInfoScrollStringGroup()
                .convertToInt(fBundle.getSelectScroll()));
        sqlInfo.setUpdatable(false);

        final StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");

        boolean isFirstColumn = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);

            if (isSkipTypeForSimpleTable(columnStructure)) {
                // It is a type that should be skipped as a single table.
                continue;
            }

            if (isFirstColumn) {
                isFirstColumn = false;
            } else {
                sql.append(", ");
            }
            sql.append(columnStructure.getName());
        }

        if (isFirstColumn) {
            // None of the columns were processed. Aborts the process.
            return;
        }

        sql.append("\n  FROM " + escapeSqlName(table.getName()));

        boolean isFirstPrimaryKey = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);
            if (BlancoDbUtil.isPrimaryKey(table, columnStructure)) {
                if (isSkipTypeForSimpleTable(columnStructure)) {
                    // It is a type that should be skipped as a single table.
                    // Binaries and readers cannot be used as keys.
                    continue;
                }

                if (isFirstPrimaryKey) {
                    isFirstPrimaryKey = false;
                    sql.append("\n WHERE ");
                } else {
                    sql.append("\n   AND ");
                }

                final BlancoDbMetaDataColumnStructure wrkStructure = cloneColumnStructure(columnStructure);
                if (fFormatSql) {
                    wrkStructure.setName("inParam"
                            + BlancoNameAdjuster.toClassName(wrkStructure
                                    .getName()));
                } else {
                    wrkStructure.setName(BlancoNameAdjuster
                            .toParameterName(wrkStructure.getName()));
                }

                sql.append(columnStructure.getName() + " = #"
                        + wrkStructure.getName());

                sqlInfo.getInParameterList().add(wrkStructure);
            }
        }

        if (isFirstPrimaryKey) {
            // When no primary key has been processed, no WHERE has been created.
            // It is considered dangerous to continue the process and aborts the process.
            return;
        }

        sqlInfo.setQuery(sql.toString());
        if (fFormatSql) {
            try {
                sqlInfo.setQuery(getSqlFormatter().format(sqlInfo.getQuery()));
            } catch (BlancoSqlFormatterException e) {
                // We have no choice but to keep going.
                e.printStackTrace();
            }
        }

        // Adds it to the root node at the very end.
        resultSqlInfo.add(sqlInfo);
    }

    /**
     * Generates an updatable search.
     * 
     * @param collector
     * @param metadata
     * @param table
     * @param document
     * @param eleRoot
     * @throws SQLException
     */
    private void generateSelectUpdatable(
            final List<BlancoDbMetaDataTableStructure> listTables,
            final BlancoDbMetaDataTableStructure table,
            final List<BlancoDbSqlInfoStructure> resultSqlInfo)
            throws SQLException {

        switch (fDbSetting.getDriverName()) {
        case BlancoDbDriverNameStringGroup.SQLSERVER_2000:
        case BlancoDbDriverNameStringGroup.SQLSERVER_2005:
        case BlancoDbDriverNameStringGroup.ORACLE:
        case BlancoDbDriverNameStringGroup.POSTGRESQL:
            // A database that supports updatable search as blancoDb. It is processable.
            break;
        default:
            // Cannot process.
            return;
        }

        final String name = CLASS_PREFIX + getBaseClassName(table)
                + "SelectUpdatable";

        final BlancoDbSqlInfoStructure sqlInfo = new BlancoDbSqlInfoStructure();
        sqlInfo.setName(name);
        sqlInfo.setType(BlancoDbSqlInfoTypeStringGroup.ITERATOR);
        sqlInfo.setSingle(false);
        sqlInfo.setScroll(new BlancoDbSqlInfoScrollStringGroup()
                .convertToInt(fBundle.getSelectUpdatableScroll()));
        sqlInfo.setUpdatable(true);

        final StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");

        boolean isFirstColumn = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);
            // To make it working with updatable, gets all columns.

            if (isSkipTypeForSimpleTable(columnStructure)) {
                // It is a type that should be skipped as a single table.
                continue;
            }

            if (isFirstColumn) {
                isFirstColumn = false;
            } else {
                sql.append(", ");
            }
            sql.append(columnStructure.getName());
        }

        if (isFirstColumn) {
            // None of the columns were processed. Aborts the process.
            return;
        }

        sql.append("\n  FROM " + escapeSqlName(table.getName()));

        switch (fDbSetting.getDriverName()) {
        case BlancoDbDriverNameStringGroup.SQLSERVER_2000:
        case BlancoDbDriverNameStringGroup.SQLSERVER_2005:
            sql.append(" WITH (UPDLOCK)");
            break;
        }

        boolean isFirstPrimaryKey = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);
            if (BlancoDbUtil.isPrimaryKey(table, columnStructure)) {
                if (isSkipTypeForSimpleTable(columnStructure)) {
                    // It is a type that should be skipped as a single table.
                    // Binaries cannot be used as search keys.
                    continue;
                }

                if (isFirstPrimaryKey) {
                    isFirstPrimaryKey = false;
                    sql.append("\n WHERE ");
                } else {
                    sql.append("\n   AND ");
                }

                final BlancoDbMetaDataColumnStructure wrkStructure = cloneColumnStructure(columnStructure);
                if (fFormatSql) {
                    wrkStructure.setName("inParam"
                            + BlancoNameAdjuster.toClassName(wrkStructure
                                    .getName()));
                } else {
                    wrkStructure.setName(BlancoNameAdjuster
                            .toParameterName(wrkStructure.getName()));
                }

                sql.append(columnStructure.getName() + " = #"
                        + wrkStructure.getName());

                sqlInfo.getInParameterList().add(wrkStructure);
            }
        }

        if (isFirstPrimaryKey) {
            // When no primary key has been processed, no WHERE has been created.
            // It is considered dangerous to continue the process and aborts the process.
            return;
        }

        switch (fDbSetting.getDriverName()) {
        case BlancoDbDriverNameStringGroup.ORACLE:
        case BlancoDbDriverNameStringGroup.POSTGRESQL:
            sql.append(" FOR UPDATE");
            break;
        }

        sqlInfo.setQuery(sql.toString());
        if (fFormatSql) {
            try {
                sqlInfo.setQuery(getSqlFormatter().format(sqlInfo.getQuery()));
            } catch (BlancoSqlFormatterException e) {
                // We have no choice but to keep going.
                e.printStackTrace();
            }
        }

        // Adds it to the root node at the very end.
        resultSqlInfo.add(sqlInfo);
    }

    /**
     * Generates separate Iterator for the types that are mapped to InputStream and Reader.
     * 
     * Outside of this Iterator, the types mapped to InputStream and Reader will be skipped in generation as items or conditions.
     * 
     * @param collector
     * @param metadata
     * @param table
     * @param document
     * @param eleRoot
     * @throws SQLException
     */
    private void generateSelectColumn(
            final List<BlancoDbMetaDataTableStructure> listTables,
            final BlancoDbMetaDataTableStructure table,
            final List<BlancoDbSqlInfoStructure> resultSqlInfo)
            throws SQLException {

        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);

            if (isSkipTypeForSimpleTable(columnStructure) == false) {
                // It will process binaries and readers "only" here.
                // Note that the condition is reversed in this area.
                continue;
            }

            final String name = CLASS_PREFIX + getBaseClassName(table)
                    + "Column"
                    + BlancoNameAdjuster.toClassName(columnStructure.getName());

            final BlancoDbSqlInfoStructure sqlInfo = new BlancoDbSqlInfoStructure();
            sqlInfo.setName(name);
            sqlInfo.setType(BlancoDbSqlInfoTypeStringGroup.ITERATOR);

            // Whether to output accessors to BINARY and ASCIISTREAM columns with single row constraints.
            // In SQL Server 2000, it is known that if generation is done with single row constraint, the stream of the first search result will be closed when the next() + next() in the getSingleRow method of the single row constraint is called twice.
            // With this in mind, we want to make it a non-single row constraint, which defaults to false.
            sqlInfo.setSingle(fBundle.getSimpleColBinaryAsciiSelectSinglerow()
                    .equals("true"));
            sqlInfo.setScroll(new BlancoDbSqlInfoScrollStringGroup()
                    .convertToInt(fBundle.getSelectScroll()));
            sqlInfo.setUpdatable(false);

            final StringBuffer sql = new StringBuffer();
            sql.append("SELECT ");
            sql.append(columnStructure.getName());
            sql.append("\n FROM " + escapeSqlName(table.getName()));

            boolean isFirstPrimaryKey = true;
            for (int indexPrimaryKey = 0; indexPrimaryKey < table.getColumns()
                    .size(); indexPrimaryKey++) {
                final BlancoDbMetaDataColumnStructure columnPrimaryKey = table
                        .getColumns().get(indexPrimaryKey);

                if (isSkipTypeForSimpleTable(columnPrimaryKey)) {
                    // It is a type that should be skipped as a condition.
                    continue;
                }

                if (BlancoDbUtil.isPrimaryKey(table, columnPrimaryKey) == false) {
                    continue;
                }

                if (isFirstPrimaryKey) {
                    isFirstPrimaryKey = false;
                    sql.append("\n WHERE ");
                } else {
                    sql.append("\n   AND ");
                }

                final BlancoDbMetaDataColumnStructure wrkStructure = cloneColumnStructure(columnPrimaryKey);
                if (fFormatSql) {
                    wrkStructure.setName("inParam"
                            + BlancoNameAdjuster.toClassName(wrkStructure
                                    .getName()));
                } else {
                    wrkStructure.setName(BlancoNameAdjuster
                            .toParameterName(wrkStructure.getName()));
                }

                sql.append(columnPrimaryKey.getName() + " = #"
                        + wrkStructure.getName());

                sqlInfo.getInParameterList().add(wrkStructure);
            }

            if (isFirstPrimaryKey) {
                // When no primary key has been processed, no WHERE has been created.
                // It is considered dangerous to continue the process and aborts the process.
                return;
            }

            sqlInfo.setQuery(sql.toString());
            if (fFormatSql) {
                try {
                    sqlInfo.setQuery(getSqlFormatter().format(
                            sqlInfo.getQuery()));
                } catch (BlancoSqlFormatterException e) {
                    // We have no choice but to keep going.
                    e.printStackTrace();
                }
            }

            // Adds it to the root node at the very end.
            resultSqlInfo.add(sqlInfo);
        }
    }

    /**
     * Generates an Iterator to search all items.
     * 
     * @param collector
     * @param metadata
     * @param table
     * @param document
     * @param eleRoot
     * @throws SQLException
     */
    private void generateSelectAll(
            final List<BlancoDbMetaDataTableStructure> listTables,
            final BlancoDbMetaDataTableStructure table,
            final List<BlancoDbSqlInfoStructure> resultSqlInfo)
            throws SQLException {

        final String name = CLASS_PREFIX + getBaseClassName(table)
                + "SelectAll";

        final BlancoDbSqlInfoStructure sqlInfo = new BlancoDbSqlInfoStructure();
        sqlInfo.setName(name);
        sqlInfo.setType(BlancoDbSqlInfoTypeStringGroup.ITERATOR);
        sqlInfo.setSingle(false);
        sqlInfo.setScroll(new BlancoDbSqlInfoScrollStringGroup()
                .convertToInt(fBundle.getSelectAllScroll()));
        sqlInfo.setUpdatable(false);

        final StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");

        boolean isFirstColumn = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);

            if (isSkipTypeForSimpleTable(columnStructure)) {
                // It is a type that should be skipped as a single table.
                continue;
            }

            if (isFirstColumn) {
                isFirstColumn = false;
            } else {
                sql.append(", ");
            }
            sql.append(columnStructure.getName());

        }

        if (isFirstColumn) {
            // None of the columns were processed. Aborts the process.
            return;
        }

        sql.append("\n  FROM " + escapeSqlName(table.getName()));

        boolean isFirstPrimaryKey = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);

            if (isSkipTypeForSimpleTable(columnStructure)) {
                // It is a type that should be skipped as a single table.
                continue;
            }

            if (BlancoDbUtil.isPrimaryKey(table, columnStructure)) {
                if (isFirstPrimaryKey) {
                    isFirstPrimaryKey = false;
                    sql.append("\n ORDER BY ");
                } else {
                    sql.append(", ");
                }
                sql.append(columnStructure.getName());
            }
        }

        if (isFirstPrimaryKey) {
            // When no primary key has been processed, no WHERE has been created.
            // It is considered dangerous to continue the process and aborts the process.
            return;
        }

        sqlInfo.setQuery(sql.toString());
        if (fFormatSql) {
            try {
                sqlInfo.setQuery(getSqlFormatter().format(sqlInfo.getQuery()));
            } catch (BlancoSqlFormatterException e) {
                // We have no choice but to keep going.
                e.printStackTrace();
            }
        }

        // Adds it to the root node at the very end.
        resultSqlInfo.add(sqlInfo);
    }

    /**
     * Generates an Invoker to perform the insertion.
     * 
     * @param collector
     * @param metadata
     * @param table
     * @param document
     * @param eleRoot
     * @param isIgnoreNullable
     * @throws SQLException
     */
    private void generateInsert(
            final List<BlancoDbMetaDataTableStructure> listTables,
            final BlancoDbMetaDataTableStructure table,
            final List<BlancoDbSqlInfoStructure> resultSqlInfo,
            final boolean isIgnoreNullable) throws SQLException {

        final String name = CLASS_PREFIX + getBaseClassName(table) + "Insert"
                + (isIgnoreNullable ? "NoNulls" : "");

        final BlancoDbSqlInfoStructure sqlInfo = new BlancoDbSqlInfoStructure();
        sqlInfo.setName(name);
        sqlInfo.setType(BlancoDbSqlInfoTypeStringGroup.INVOKER);
        sqlInfo.setSingle(true);

        final StringBuffer sql = new StringBuffer();
        sql.append("INSERT");
        sql.append("\n  INTO " + escapeSqlName(table.getName()));
        sql.append("\n       (");

        boolean isNullableColumnExist = false;
        boolean isFirstColumn = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);
            if (isIgnoreNullable
                    && columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                isNullableColumnExist = true;
                continue;
            }

            if (isSkipTypeForSimpleTable(columnStructure)) {
                // It is a type that should be skipped as a single table.
                continue;
            }

            if (isFirstColumn) {
                isFirstColumn = false;
            } else {
                sql.append(", ");
            }
            sql.append(columnStructure.getName());
        }

        if (isFirstColumn) {
            // None of the columns were processed.
            // In the case of this combination, skips the generation.
            return;
        }

        sql.append(")");
        sql.append("\nVALUES");
        sql.append("\n       (");

        boolean isFirstPrimaryKey = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);
            if (isIgnoreNullable
                    && columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                continue;
            }

            if (isSkipTypeForSimpleTable(columnStructure)) {
                // It is a type that should be skipped as a single table.
                continue;
            }

            if (isFirstPrimaryKey) {
                isFirstPrimaryKey = false;
            } else {
                sql.append(", ");
            }

            final BlancoDbMetaDataColumnStructure wrkStructure = cloneColumnStructure(columnStructure);
            if (fFormatSql) {
                wrkStructure
                        .setName("inParam"
                                + BlancoNameAdjuster.toClassName(wrkStructure
                                        .getName()));
            } else {
                wrkStructure.setName(BlancoNameAdjuster
                        .toParameterName(wrkStructure.getName()));
            }

            sql.append("#" + wrkStructure.getName());

            sqlInfo.getInParameterList().add(wrkStructure);
        }
        sql.append(")");

        if (isFirstPrimaryKey) {
            // When no primary key has been processed, no WHERE has been created.
            // It is considered dangerous to continue the process and aborts the process.
            return;
        }

        sqlInfo.setQuery(sql.toString());
        if (fFormatSql) {
            try {
                sqlInfo.setQuery(getSqlFormatter().format(sqlInfo.getQuery()));
            } catch (BlancoSqlFormatterException e) {
                // We have no choice but to keep going.
                e.printStackTrace();
            }
        }

        if (isIgnoreNullable == false || isNullableColumnExist) {
            // Adds to XML only if a NULL-allowed column has been processed.
            resultSqlInfo.add(sqlInfo);
        }
    }

    /**
     * Generates an Invoker that performs the update.
     * 
     * @param collector
     * @param metadata
     * @param table
     * @param document
     * @param eleRoot
     * @throws SQLException
     */
    private void generateUpdate(
            final List<BlancoDbMetaDataTableStructure> listTables,
            final BlancoDbMetaDataTableStructure table,
            final List<BlancoDbSqlInfoStructure> resultSqlInfo)
            throws SQLException {

        final String name = CLASS_PREFIX + getBaseClassName(table) + "Update";

        final BlancoDbSqlInfoStructure sqlInfo = new BlancoDbSqlInfoStructure();
        sqlInfo.setName(name);
        sqlInfo.setType(BlancoDbSqlInfoTypeStringGroup.INVOKER);
        sqlInfo.setSingle(true);

        final StringBuffer sql = new StringBuffer();
        sql.append("UPDATE " + escapeSqlName(table.getName()));
        sql.append("\n   SET ");

        boolean isFirstColumn = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);
            if (BlancoDbUtil.isPrimaryKey(table, columnStructure)) {
                continue;
            }

            if (isSkipTypeForSimpleTable(columnStructure)) {
                // It is a type that should be skipped as a single table.
                continue;
            }

            if (isFirstColumn) {
                isFirstColumn = false;
            } else {
                sql.append(", ");
            }

            final BlancoDbMetaDataColumnStructure wrkStructure = cloneColumnStructure(columnStructure);
            if (fFormatSql) {
                wrkStructure
                        .setName("inParam"
                                + BlancoNameAdjuster.toClassName(wrkStructure
                                        .getName()));
            } else {
                wrkStructure.setName(BlancoNameAdjuster
                        .toParameterName(wrkStructure.getName()));
            }

            sql.append(columnStructure.getName() + " = #"
                    + wrkStructure.getName());

            sqlInfo.getInParameterList().add(wrkStructure);
        }

        if (isFirstColumn) {
            // None of the columns were processed.
            // In the case of this combination, skips the generation.
            return;
        }

        sql.append("\n WHERE ");

        boolean isFirstPrimaryKey = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);
            if (BlancoDbUtil.isPrimaryKey(table, columnStructure) == false) {
                continue;
            }

            if (isSkipTypeForSimpleTable(columnStructure)) {
                // It is a type that should be skipped as a single table.
                continue;
            }

            if (isFirstPrimaryKey) {
                isFirstPrimaryKey = false;
            } else {
                sql.append("\n   AND ");
            }

            final BlancoDbMetaDataColumnStructure wrkStructure = cloneColumnStructure(columnStructure);
            wrkStructure.setName("where"
                    + BlancoNameAdjuster.toClassName(wrkStructure.getName()));

            sql.append(columnStructure.getName() + " = #"
                    + wrkStructure.getName());

            sqlInfo.getInParameterList().add(wrkStructure);
        }

        if (isFirstPrimaryKey) {
            // When no primary key has been processed, no WHERE has been created.
            // It is considered dangerous to continue the process and aborts the process.
            return;
        }

        sqlInfo.setQuery(sql.toString());
        if (fFormatSql) {
            try {
                sqlInfo.setQuery(getSqlFormatter().format(sqlInfo.getQuery()));
            } catch (BlancoSqlFormatterException e) {
                // We have no choice but to keep going.
                e.printStackTrace();
            }
        }

        // Adds it to the root node at the very end.
        resultSqlInfo.add(sqlInfo);
    }

    /**
     * Generates an Invoker that performs the deletion.
     * 
     * @param collector
     * @param metadata
     * @param table
     * @param document
     * @param eleRoot
     * @throws SQLException
     */
    private void generateDelete(
            final List<BlancoDbMetaDataTableStructure> listTables,
            final BlancoDbMetaDataTableStructure table,
            final List<BlancoDbSqlInfoStructure> resultSqlInfo)
            throws SQLException {

        final String name = CLASS_PREFIX + getBaseClassName(table) + "Delete";

        final BlancoDbSqlInfoStructure sqlInfo = new BlancoDbSqlInfoStructure();
        sqlInfo.setName(name);
        sqlInfo.setType(BlancoDbSqlInfoTypeStringGroup.INVOKER);
        sqlInfo.setSingle(true);

        final StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM " + escapeSqlName(table.getName()));
        sql.append("\n WHERE ");

        boolean isFirstColumn = true;
        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = table
                    .getColumns().get(indexCol);
            if (BlancoDbUtil.isPrimaryKey(table, columnStructure) == false) {
                continue;
            }

            if (isSkipTypeForSimpleTable(columnStructure)) {
                // It is a type that should be skipped as a single table.
                continue;
            }

            if (isFirstColumn) {
                isFirstColumn = false;
            } else {
                sql.append("\n   AND ");
            }

            final BlancoDbMetaDataColumnStructure wrkStructure = cloneColumnStructure(columnStructure);
            if (fFormatSql) {
                wrkStructure
                        .setName("inParam"
                                + BlancoNameAdjuster.toClassName(wrkStructure
                                        .getName()));
            } else {
                wrkStructure.setName(BlancoNameAdjuster
                        .toParameterName(wrkStructure.getName()));
            }

            sql.append(columnStructure.getName() + " = #"
                    + wrkStructure.getName());

            sqlInfo.getInParameterList().add(wrkStructure);
        }

        if (isFirstColumn) {
            // None of the columns were processed.
            // In the case of this combination, skips the generation.
            return;
        }

        sqlInfo.setQuery(sql.toString());
        if (fFormatSql) {
            try {
                sqlInfo.setQuery(getSqlFormatter().format(sqlInfo.getQuery()));
            } catch (BlancoSqlFormatterException e) {
                // We have no choice but to keep going.
                e.printStackTrace();
            }
        }

        // Adds it to the root node at the very end.
        resultSqlInfo.add(sqlInfo);
    }

    /**
     * If the given SQL name (table name or column name) contains characters (spaces) that should be escaped, escapes the table name itself with double quotes.
     * 
     * @param tableName
     * @return
     */
    public String escapeSqlName(final String tableName) {
        if (tableName.indexOf(" ") >= 0) {
            return "\"" + tableName + "\"";
        }
        return tableName;
    }

    private static BlancoDbMetaDataColumnStructure cloneColumnStructure(
            final BlancoDbMetaDataColumnStructure argColumnStructure) {
        final BlancoDbMetaDataColumnStructure columnStructureWrk = new BlancoDbMetaDataColumnStructure();
        columnStructureWrk.setName(argColumnStructure.getName());
        columnStructureWrk.setDataType(argColumnStructure.getDataType());
        columnStructureWrk.setNullable(argColumnStructure.getNullable());
        return columnStructureWrk;
    }

    /**
     * Gets the SQL formatter.
     * 
     * @return The SQL formatter.
     */
    private static BlancoSqlFormatter getSqlFormatter() {
        return new BlancoSqlFormatter(new BlancoSqlRule());
    }

    /**
     * Determines if a type should be skipped as a single table operation.
     * 
     * Note: This method is variable depending on the relational database or database API.
     * 
     * TODO: If you want to use this class in a non-Java language, you need to override this method.
     * 
     * @param argTypeName
     *            Type name. Excluding package names.
     * @return true if the type should be skipped.
     */
    protected boolean isSkipTypeForSimpleTable(
            final BlancoDbMetaDataColumnStructure columnStructure) {
        switch (columnStructure.getDataType()) {
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
        case Types.BLOB:
            return true;
        case Types.LONGVARCHAR:
        case Types.CLOB:
        case Types.NCHAR:
        case Types.NVARCHAR:
        case Types.LONGNVARCHAR:
            return true;
        case Types.JAVA_OBJECT:
        case Types.DISTINCT:
        case Types.STRUCT:
        case Types.ARRAY:
        case Types.NULL:
        case Types.OTHER:
        case Types.REF:
        case Types.DATALINK:
        case Types.ROWID: // For now, maps outside the scope of support.
            return true;
        default:
            return false;
        }
    }
}