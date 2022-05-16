/*
 * blancoDb
 * Copyright (C) 2004-2012 Yasuo Nakanishi
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import blanco.dbmetadata.BlancoDbMetaDataTable;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataTableStructure;

/**
 * Gets information from a database table.
 * 
 * @author Toshiki IGA
 */
public class BlancoDbTableParser {
    /**
     * Gets the list of information of a table.
     * 
     * @param conn
     * @param schema
     *            If null, no schema is specified.
     * @return
     * @throws SQLException
     */
    public List<BlancoDbMetaDataTableStructure> parse(final Connection conn, final String schema) throws SQLException {
        final DatabaseMetaData metadata = conn.getMetaData();

        final List<BlancoDbMetaDataTableStructure> listTables = BlancoDbMetaDataTable.getTables(metadata, schema, null,
                new String[] { "TABLE" });

        // Gets the minimum necessary meta information about the table.
        for (int indexTable = 0; indexTable < listTables.size(); indexTable++) {
            final BlancoDbMetaDataTableStructure tableStructure = listTables.get(indexTable);

            tableStructure.setColumns(BlancoDbMetaDataTable.getColumns(metadata, tableStructure.getCatalog(), schema,
                    tableStructure.getName()));

            tableStructure.setPrimaryKeys(BlancoDbMetaDataTable.getPrimaryKeys(metadata, tableStructure.getCatalog(),
                    schema, tableStructure.getName()));

            // Foreign key-related meta information is omitted for the purpose of speeding up the process.
        }

        return listTables;
    }
}
