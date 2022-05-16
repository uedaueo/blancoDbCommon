package blanco.db.common.stringgroup;

/**
 * 自動生成の際にSQL文実行をおこなうかどうかを指定するためのフラグ。
 */
public class BlancoDbExecuteSqlStringGroup {
    /**
     * No.1 説明:SQL文は実行しません。
     */
    public static final int NONE = 1;

    /**
     * No.2 説明:検索型(iterator)の場合に、SQL文を実際に実行します。
     */
    public static final int ITERATOR = 2;

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
        // 説明:SQL文は実行しません。
        if ("none".equals(argCheck)) {
            return true;
        }
        // No.2
        // 説明:検索型(iterator)の場合に、SQL文を実際に実行します。
        if ("iterator".equals(argCheck)) {
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
        // 説明:SQL文は実行しません。
        if ("none".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.2
        // 説明:検索型(iterator)の場合に、SQL文を実際に実行します。
        if ("iterator".equalsIgnoreCase(argCheck)) {
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
        // 説明:SQL文は実行しません。
        if ("none".equals(argCheck)) {
            return NONE;
        }
        // No.2
        // 説明:検索型(iterator)の場合に、SQL文を実際に実行します。
        if ("iterator".equals(argCheck)) {
            return ITERATOR;
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
        // 説明:SQL文は実行しません。
        if (argCheck == NONE) {
            return "none";
        }
        // No.2
        // 説明:検索型(iterator)の場合に、SQL文を実際に実行します。
        if (argCheck == ITERATOR) {
            return "iterator";
        }
        // Undefined.
        if (argCheck == NOT_DEFINED) {
            return "";
        }

        // None of them were applicable.
        throw new IllegalArgumentException("The given value (" + argCheck + ") is a value that is not defined in the string group [BlancoDbExecuteSql].");
    }
}
