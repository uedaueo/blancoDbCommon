package blanco.db.common.stringgroup;

/**
 * SQL情報の型。
 */
public class BlancoDbSqlInfoTypeStringGroup {
    /**
     * No.1 説明:検索型。
     */
    public static final int ITERATOR = 1;

    /**
     * No.2 説明:更新型。
     */
    public static final int INVOKER = 2;

    /**
     * No.3 説明:呼出型。
     */
    public static final int CALLER = 3;

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
        // 説明:検索型。
        if ("iterator".equals(argCheck)) {
            return true;
        }
        // No.2
        // 説明:更新型。
        if ("invoker".equals(argCheck)) {
            return true;
        }
        // No.3
        // 説明:呼出型。
        if ("caller".equals(argCheck)) {
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
        // 説明:検索型。
        if ("iterator".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.2
        // 説明:更新型。
        if ("invoker".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.3
        // 説明:呼出型。
        if ("caller".equalsIgnoreCase(argCheck)) {
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
        // 説明:検索型。
        if ("iterator".equals(argCheck)) {
            return ITERATOR;
        }
        // No.2
        // 説明:更新型。
        if ("invoker".equals(argCheck)) {
            return INVOKER;
        }
        // No.3
        // 説明:呼出型。
        if ("caller".equals(argCheck)) {
            return CALLER;
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
        // 説明:検索型。
        if (argCheck == ITERATOR) {
            return "iterator";
        }
        // No.2
        // 説明:更新型。
        if (argCheck == INVOKER) {
            return "invoker";
        }
        // No.3
        // 説明:呼出型。
        if (argCheck == CALLER) {
            return "caller";
        }
        // Undefined.
        if (argCheck == NOT_DEFINED) {
            return "";
        }

        // None of them were applicable.
        throw new IllegalArgumentException("The given value (" + argCheck + ") is a value that is not defined in the string group [BlancoDbSqlInfoType].");
    }
}
