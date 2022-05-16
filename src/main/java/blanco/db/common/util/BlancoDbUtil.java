/*
 * blancoDb Enterprise Edition
 * Copyright (C) 2004-2005 Tosiki Iga
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.common.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import blanco.commons.util.BlancoStringUtil;
import blanco.db.common.stringgroup.BlancoDbDriverNameStringGroup;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.dbmetadata.BlancoDbMetaDataUtil;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataKeyStructure;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataTableStructure;
import blanco.xml.bind.BlancoXmlBindingUtil;
import blanco.xml.bind.BlancoXmlUnmarshaller;
import blanco.xml.bind.valueobject.BlancoXmlDocument;
import blanco.xml.bind.valueobject.BlancoXmlElement;

/**
 * Common utility class for blancoDb.
 */
public class BlancoDbUtil {
    /**
     * A method to check if it is a primary key.
     * 
     * @param table
     * @param columnStructure
     * @return
     */
    public static boolean isPrimaryKey(
            final BlancoDbMetaDataTableStructure table,
            final BlancoDbMetaDataColumnStructure columnStructure) {
        for (int index = 0; index < table.getPrimaryKeys().size(); index++) {
            final BlancoDbMetaDataKeyStructure columnLook = (BlancoDbMetaDataKeyStructure) table
                    .getPrimaryKeys().get(index);
            if (columnLook.getPkcolumnName().equals(columnStructure.getName())) {
                // Primary key.
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the root folder of the runtime package.
     * 
     * If not specified, the base directory will be used.
     * 
     * @param dbSetting
     * @return
     */
    public static String getRuntimePackage(final BlancoDbSetting dbSetting) {
        String runtimePackage = dbSetting.getBasePackage();
        if (dbSetting.getRuntimePackage() != null) {
            runtimePackage = dbSetting.getRuntimePackage();
        }
        return runtimePackage;
    }

    /**
     * Attempts to establish a database connection.
     * 
     * @param connDef
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("deprecation")
    public static Connection connect(final BlancoDbSetting dbSetting)
            throws SQLException, ClassNotFoundException {
        System.out.println("Opens a database connection.");

        try {
            ClassLoader loader = null;
            if (BlancoStringUtil.null2Blank(dbSetting.getJdbcdriverfile())
                    .length() > 0) {
                List<String> urlList = new ArrayList<String>();
                final StringTokenizer token = new StringTokenizer(dbSetting
                        .getJdbcdriverfile(), ";");
                for (; token.hasMoreTokens();) {
                    final String file = token.nextToken();
                    if (BlancoStringUtil.null2Blank(file).trim().length() > 0) {
                        urlList.add(file);
                    }
                }

                final URL[] urlArray = new URL[urlList.size()];
                int index = 0;
                for (String look : urlList) {
                    try {
                        urlArray[index++] = new File(look).toURL();
                    } catch (MalformedURLException e) {
                        throw new IllegalArgumentException(
                                "JDBC connection establishing: Failed to get URL of jar file.", e);
                    }
                }
                loader = BlancoDbMetaDataUtil.loadDriverClass(urlArray,
                        dbSetting.getJdbcdriver());
            }

            System.out.println("  Driver:" + dbSetting.getJdbcdriver());
            System.out.println("  URL:" + dbSetting.getJdbcurl());
            System.out.println("  User:" + dbSetting.getJdbcuser());
            Connection conn = null;
            if (loader == null) {
                conn = BlancoDbMetaDataUtil.connect(dbSetting.getJdbcdriver(),
                        dbSetting.getJdbcurl(), dbSetting.getJdbcuser(),
                        dbSetting.getJdbcpassword());
            } else {
                conn = BlancoDbMetaDataUtil.connect(dbSetting.getJdbcdriver(),
                        dbSetting.getJdbcurl(), dbSetting.getJdbcuser(),
                        dbSetting.getJdbcpassword(), loader);
            }

            // Sets auto-commit to OFF.
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException ex) {
            System.out.println("Failed to establish a JDBC connection: " + ex.toString());
            throw ex;
        } catch (ClassNotFoundException ex) {
            System.out.println("Failed to establish a JDBC connection: " + ex.toString());
            throw ex;
        }
    }

    public static void close(final Connection conn) {
        if (conn != null) {
            try {
                System.out.println("Closes the database connection. (closes after rollbacks)");
                try {
                    conn.rollback();
                } finally {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Failed to open a JDBC connection: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets database version information and etc.
     */
    public static void getDatabaseVersionInfo(final Connection conn,
            final BlancoDbSetting dbSetting) {
        System.out.println("Gets the basic information about the JDBC driver.");
        try {
            final DatabaseMetaData databaseMetaData = conn.getMetaData();

            String driverName = databaseMetaData.getDriverName();
            final String _driverVersion = databaseMetaData.getDriverVersion();
            System.out.println("  DriverName:" + driverName);
            System.out.println("  DriverVersion:" + _driverVersion);

            try {
                System.out.println("  DatabaseMajorVersion:"
                        + databaseMetaData.getDatabaseMajorVersion());
                System.out.println("  DatabaseMinorVersion:"
                        + databaseMetaData.getDatabaseMinorVersion());
            } catch (java.lang.AbstractMethodError er) {
                // SQL Server 2000 JDBC Driver does not support this method.
                // System.out.println(er.toString());
            } catch (SQLException ex) {
            }
            try {
                System.out.println("  JDBCMajorVersion:"
                        + databaseMetaData.getJDBCMajorVersion());
                System.out.println("  JDBCMinorVersion:"
                        + databaseMetaData.getJDBCMinorVersion());
            } catch (java.lang.AbstractMethodError er) {
                // SQL Server 2000 JDBC Driver does not support this method.
                // System.out.println(er.toString());
            } catch (SQLException ex) {
                // Oracle 9i JDBC Driver does not support this method.
            }

            dbSetting.setDriverName(new BlancoDbDriverNameStringGroup()
                    .convertToInt(driverName));
            if (BlancoDbDriverNameStringGroup.NOT_DEFINED == dbSetting
                    .getDriverName()) {
                System.out.println("Unknown JDBC driver: " + driverName);
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
            return;
        }
    }

    /**
     * Loads classpath information from Eclipse Java project.
     * 
     * Uses the contents of the ".classpath" file as input.
     * 
     * @param fileClasspath
     *            A ".classpath" file as input.
     * @param dbSetting
     *            DB information to be output.
     */
    public static void readClasspathEntryFromEclipseJavaProject(
            final File fileClasspath, final BlancoDbSetting dbSetting) {
        if (fileClasspath == null) {
            return;
        }
        if (fileClasspath.exists() == false) {
            return;
        }

        final BlancoXmlUnmarshaller unmarshaller = new BlancoXmlUnmarshaller();
        final BlancoXmlDocument doc = unmarshaller.unmarshal(fileClasspath);
        final List<BlancoXmlElement> classpathEntryList = BlancoXmlBindingUtil
                .getElementsByTagName(BlancoXmlBindingUtil
                        .getDocumentElement(doc), "classpathentry");

        for (BlancoXmlElement entry : classpathEntryList) {
            String kind = BlancoXmlBindingUtil.getAttribute(entry, "kind");
            if ("lib".equals(kind) == false) {
                continue;
            }
            if (dbSetting.getJdbcdriverfile() == null) {
                dbSetting.setJdbcdriverfile("");
            }
            if (dbSetting.getJdbcdriverfile().length() > 0) {
                dbSetting
                        .setJdbcdriverfile(dbSetting.getJdbcdriverfile() + ";");
            }
            dbSetting.setJdbcdriverfile(dbSetting.getJdbcdriverfile()
                    + fileClasspath.getParentFile().getAbsolutePath() + "/"
                    + BlancoXmlBindingUtil.getAttribute(entry, "path"));
        }
    }

    /**
     * Gets the base package, preferably BlancoDbSqlInfoStructure, and if it is not there, uses BlancoDbSetting to determine it.
     * 
     * @param sqlInfo
     * @param dbSetting
     * @return
     */
    public static String getBasePackage(final BlancoDbSqlInfoStructure sqlInfo,
            final BlancoDbSetting dbSetting) {
        String basePackage = sqlInfo.getPackage();
        if (BlancoStringUtil.null2Blank(basePackage).length() == 0) {
            basePackage = dbSetting.getBasePackage();
        }

        return basePackage;
    }
}
