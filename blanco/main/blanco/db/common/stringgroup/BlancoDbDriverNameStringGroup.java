package blanco.db.common.stringgroup;

/**
 * JDBCドライバ名の一覧。
 */
public class BlancoDbDriverNameStringGroup {
    /**
     * No.1 説明:SQL Server 2000用のJDBCドライバ
     */
    public static final int SQLSERVER_2000 = 1;

    /**
     * No.2 説明:SQL Server 2005用のJDBCドライバ。SQL Server 2000にも接続可能。
     */
    public static final int SQLSERVER_2005 = 2;

    /**
     * No.3 説明:SQL Server 2008用のJDBCドライバ。
     */
    public static final int SQLSERVER_2_0 = 3;

    /**
     * No.4 説明:SQL Server 2008用のJDBCドライバ。
     */
    public static final int SQLSERVER_3_0 = 4;

    /**
     * No.5 説明:Oracle用のJDBCドライバ。
     */
    public static final int ORACLE = 5;

    /**
     * No.6 説明:PostgreSQL用のJDBCドライバ。
     */
    public static final int POSTGRESQL = 6;

    /**
     * No.7 説明:MySQL用のJDBCドライバ。
     */
    public static final int MYSQL = 7;

    /**
     * No.8 説明:SQLite 用の JDBC ドライバ。
     */
    public static final int SQLITE = 8;

    /**
     * Undefined. A string or constant other than a string group that is undefined.
     */
    public static final int NOT_DEFINED = -1;

    /**
     * Determines if a string is part of a string group.
     *
     * @param argCheck A string to be checked.
     * @return true is the string is part of a string group, false otherwise.
     */
    public boolean match(final String argCheck) {
        // No.1
        // 説明:SQL Server 2000用のJDBCドライバ
        if ("SQLServer".equals(argCheck)) {
            return true;
        }
        // No.2
        // 説明:SQL Server 2005用のJDBCドライバ。SQL Server 2000にも接続可能。
        if ("Microsoft SQL Server 2005 JDBC Driver".equals(argCheck)) {
            return true;
        }
        // No.3
        // 説明:SQL Server 2008用のJDBCドライバ。
        if ("Microsoft SQL Server JDBC Driver 2.0".equals(argCheck)) {
            return true;
        }
        // No.4
        // 説明:SQL Server 2008用のJDBCドライバ。
        if ("Microsoft SQL Server JDBC Driver 3.0".equals(argCheck)) {
            return true;
        }
        // No.5
        // 説明:Oracle用のJDBCドライバ。
        if ("Oracle JDBC driver".equals(argCheck)) {
            return true;
        }
        // No.6
        // 説明:PostgreSQL用のJDBCドライバ。
        if ("PostgreSQL Native Driver".equals(argCheck)) {
            return true;
        }
        // No.7
        // 説明:MySQL用のJDBCドライバ。
        if ("MySQL-AB JDBC Driver".equals(argCheck)) {
            return true;
        }
        // No.8
        // 説明:SQLite 用の JDBC ドライバ。
        if ("SQLiteJDBC".equals(argCheck)) {
            return true;
        }
        return false;
    }

    /**
     * Determines if a string is part of a string group in a case-insentive manner.
     *
     * @param argCheck A string to be checked.
     * @return true is the string is part of a string group, false otherwise.
     */
    public boolean matchIgnoreCase(final String argCheck) {
        // No.1
        // 説明:SQL Server 2000用のJDBCドライバ
        if ("SQLServer".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.2
        // 説明:SQL Server 2005用のJDBCドライバ。SQL Server 2000にも接続可能。
        if ("Microsoft SQL Server 2005 JDBC Driver".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.3
        // 説明:SQL Server 2008用のJDBCドライバ。
        if ("Microsoft SQL Server JDBC Driver 2.0".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.4
        // 説明:SQL Server 2008用のJDBCドライバ。
        if ("Microsoft SQL Server JDBC Driver 3.0".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.5
        // 説明:Oracle用のJDBCドライバ。
        if ("Oracle JDBC driver".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.6
        // 説明:PostgreSQL用のJDBCドライバ。
        if ("PostgreSQL Native Driver".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.7
        // 説明:MySQL用のJDBCドライバ。
        if ("MySQL-AB JDBC Driver".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.8
        // 説明:SQLite 用の JDBC ドライバ。
        if ("SQLiteJDBC".equalsIgnoreCase(argCheck)) {
            return true;
        }
        return false;
    }

    /**
     * Converts a string to a constant.
     *
     * Returns NOT_DEFINED if the constant is undefined or if the given string is outside the string group.
     *
     * @param argCheck A string to be converted.
     * @return The value after conversion to a constant.
     */
    public int convertToInt(final String argCheck) {
        // No.1
        // 説明:SQL Server 2000用のJDBCドライバ
        if ("SQLServer".equals(argCheck)) {
            return SQLSERVER_2000;
        }
        // No.2
        // 説明:SQL Server 2005用のJDBCドライバ。SQL Server 2000にも接続可能。
        if ("Microsoft SQL Server 2005 JDBC Driver".equals(argCheck)) {
            return SQLSERVER_2005;
        }
        // No.3
        // 説明:SQL Server 2008用のJDBCドライバ。
        if ("Microsoft SQL Server JDBC Driver 2.0".equals(argCheck)) {
            return SQLSERVER_2_0;
        }
        // No.4
        // 説明:SQL Server 2008用のJDBCドライバ。
        if ("Microsoft SQL Server JDBC Driver 3.0".equals(argCheck)) {
            return SQLSERVER_3_0;
        }
        // No.5
        // 説明:Oracle用のJDBCドライバ。
        if ("Oracle JDBC driver".equals(argCheck)) {
            return ORACLE;
        }
        // No.6
        // 説明:PostgreSQL用のJDBCドライバ。
        if ("PostgreSQL Native Driver".equals(argCheck)) {
            return POSTGRESQL;
        }
        // No.7
        // 説明:MySQL用のJDBCドライバ。
        if ("MySQL-AB JDBC Driver".equals(argCheck)) {
            return MYSQL;
        }
        // No.8
        // 説明:SQLite 用の JDBC ドライバ。
        if ("SQLiteJDBC".equals(argCheck)) {
            return SQLITE;
        }

        // No matching constants were found.
        return NOT_DEFINED;
    }

    /**
     * Converts a constant to a string.
     *
     * Converts to a string corresponding to a constant.
     *
     * @param argCheck A constant to be converted.
     * @return The value after conversion to a string, or a zero-length string if NOT_DEFINED.
     */
    public String convertToString(final int argCheck) {
        // No.1
        // 説明:SQL Server 2000用のJDBCドライバ
        if (argCheck == SQLSERVER_2000) {
            return "SQLServer";
        }
        // No.2
        // 説明:SQL Server 2005用のJDBCドライバ。SQL Server 2000にも接続可能。
        if (argCheck == SQLSERVER_2005) {
            return "Microsoft SQL Server 2005 JDBC Driver";
        }
        // No.3
        // 説明:SQL Server 2008用のJDBCドライバ。
        if (argCheck == SQLSERVER_2_0) {
            return "Microsoft SQL Server JDBC Driver 2.0";
        }
        // No.4
        // 説明:SQL Server 2008用のJDBCドライバ。
        if (argCheck == SQLSERVER_3_0) {
            return "Microsoft SQL Server JDBC Driver 3.0";
        }
        // No.5
        // 説明:Oracle用のJDBCドライバ。
        if (argCheck == ORACLE) {
            return "Oracle JDBC driver";
        }
        // No.6
        // 説明:PostgreSQL用のJDBCドライバ。
        if (argCheck == POSTGRESQL) {
            return "PostgreSQL Native Driver";
        }
        // No.7
        // 説明:MySQL用のJDBCドライバ。
        if (argCheck == MYSQL) {
            return "MySQL-AB JDBC Driver";
        }
        // No.8
        // 説明:SQLite 用の JDBC ドライバ。
        if (argCheck == SQLITE) {
            return "SQLiteJDBC";
        }
        // Undefined.
        if (argCheck == NOT_DEFINED) {
            return "";
        }

        // None of them were applicable.
        throw new IllegalArgumentException("The given value (" + argCheck + ") is a value that is not defined in the string group [BlancoDbDriverName].");
    }
}
