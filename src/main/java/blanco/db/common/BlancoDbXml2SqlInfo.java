/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.common;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import blanco.db.common.valueobject.BlancoDbDynamicConditionStructure;
import blanco.dbmetadata.BlancoDbMetaDataUtil;
import org.xml.sax.SAXException;

import blanco.commons.util.BlancoBigDecimalUtil;
import blanco.db.common.resourcebundle.BlancoDbCommonResourceBundle;
import blanco.db.common.stringgroup.BlancoDbExecuteSqlStringGroup;
import blanco.db.common.stringgroup.BlancoDbSqlInfoTypeStringGroup;
import blanco.db.common.util.BlancoDbQueryParserUtil;
import blanco.db.common.util.BlancoDbXmlParser;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.dbmetadata.BlancoDbMetaDataSql;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * Constructs various information structures related to SQL definitions using intermediate files of SQL definitions as input.
 *
 * In this class, for search type SQL statements, actually executes the SQL statement. By executing the SQL statement, we can get a list of columns in the search results, checks the validity of SQL input parameters, or checks if the SQL statement can be executed in the first place.
 *
 * @author Yasuo Nakanishi
 */
public class BlancoDbXml2SqlInfo {
    /**
     * An accessor to blancoDb resource bundle information.
     */
    private final BlancoDbCommonResourceBundle fBundle = new BlancoDbCommonResourceBundle();

    /**
     * Constructs various information structures related to SQL definitions using intermediate XML files of SQL definitions as input.
     *
     * @param conn
     *            Database connection.
     * @param dbSetting
     *            blancoDb settings.
     * @param fileSqlForm
     *            An intermediate XML file to use as input.
     * @return A list of the SQL definition information structure.
     * @throws SQLException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public List<BlancoDbSqlInfoStructure> process(final Connection conn,
            final BlancoDbSetting dbSetting, final File fileSqlForm)
            throws SQLException, SAXException, IOException,
            ParserConfigurationException, TransformerException {

        final List<BlancoDbSqlInfoStructure> blancoDbDef = new BlancoDbXmlParser()
                .parse(fileSqlForm);

        for (int index = 0; index < blancoDbDef.size(); index++) {
            final BlancoDbSqlInfoStructure sqlInfo = blancoDbDef.get(index);

            if (sqlInfo.getType() == BlancoDbSqlInfoTypeStringGroup.ITERATOR && BlancoDbExecuteSqlStringGroup.NONE != dbSetting.getExecuteSql()) {
                // Of the collected items, for the search type, it will be connect to the database and a trial run will be performed.
                processIterator(conn, sqlInfo, dbSetting);
            }
        }

        return blancoDbDef;
    }

    public void processIterator(final Connection conn,
            final BlancoDbSqlInfoStructure sqlInfo,
            final BlancoDbSetting dbSetting) {
        try {
            final BlancoDbQueryParserUtil parserUtil = new BlancoDbQueryParserUtil(sqlInfo);

            // Executes the SQL statement to get the result set, and gets the list of columns of search results from here.
            // This is the implementation part of the core function of blancoDb, which is to use the JDBC function to execute a SQL statement and get a list of columns of search results.

            List<BlancoDbMetaDataColumnStructure> nativeParam = parserUtil.convertSqlInParameter2NativeParameter(sqlInfo);
            if (dbSetting.getExecuteSql() == BlancoDbExecuteSqlStringGroup.NONE) {
                // In the case of none, sets param to null.
                nativeParam = null;
            }

            // If param is set to a value, SQL will be executed.
            final List<BlancoDbMetaDataColumnStructure> listResult = BlancoDbMetaDataSql
                    .getResultSetMetaData(conn,
                            parserUtil.getNaturalSqlStringForJava(sqlInfo.getDynamicConditionList()),
                            nativeParam);

            // Saves the list of columns in the search result set as post-collection information.
            sqlInfo.setResultSetColumnList(listResult);

        } catch (SQLException e) {
            throw new IllegalArgumentException(fBundle.getXml2javaclassErr002(
                    sqlInfo.getName(), e.getSQLState(),
                    BlancoBigDecimalUtil.toBigDecimal(e.getErrorCode()),
                    e.toString()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(fBundle.getXml2javaclassErr017(
                    sqlInfo.getName(), e.getMessage()));
        }
    }
}
