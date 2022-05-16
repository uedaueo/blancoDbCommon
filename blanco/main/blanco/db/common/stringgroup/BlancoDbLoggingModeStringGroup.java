package blanco.db.common.stringgroup;

/**
 * BlancoDbSettingで利用されるロギングモードの一覧。
 */
public class BlancoDbLoggingModeStringGroup {
    /**
     * No.1 説明:ノーマルなdebugログを報告。
     */
    public static final int DEBUG = 1;

    /**
     * No.2 説明:パフォーマンス報告付き報告。メモリ使用量などが報告されます。
     */
    public static final int PERFORMANCE = 2;

    /**
     * No.3 説明:SQLIDのみを報告。
     */
    public static final int SQLID = 3;

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
        // 説明:ノーマルなdebugログを報告。
        if ("debug".equals(argCheck)) {
            return true;
        }
        // No.2
        // 説明:パフォーマンス報告付き報告。メモリ使用量などが報告されます。
        if ("performance".equals(argCheck)) {
            return true;
        }
        // No.3
        // 説明:SQLIDのみを報告。
        if ("sqlid".equals(argCheck)) {
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
        // 説明:ノーマルなdebugログを報告。
        if ("debug".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.2
        // 説明:パフォーマンス報告付き報告。メモリ使用量などが報告されます。
        if ("performance".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.3
        // 説明:SQLIDのみを報告。
        if ("sqlid".equalsIgnoreCase(argCheck)) {
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
        // 説明:ノーマルなdebugログを報告。
        if ("debug".equals(argCheck)) {
            return DEBUG;
        }
        // No.2
        // 説明:パフォーマンス報告付き報告。メモリ使用量などが報告されます。
        if ("performance".equals(argCheck)) {
            return PERFORMANCE;
        }
        // No.3
        // 説明:SQLIDのみを報告。
        if ("sqlid".equals(argCheck)) {
            return SQLID;
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
        // 説明:ノーマルなdebugログを報告。
        if (argCheck == DEBUG) {
            return "debug";
        }
        // No.2
        // 説明:パフォーマンス報告付き報告。メモリ使用量などが報告されます。
        if (argCheck == PERFORMANCE) {
            return "performance";
        }
        // No.3
        // 説明:SQLIDのみを報告。
        if (argCheck == SQLID) {
            return "sqlid";
        }
        // Undefined.
        if (argCheck == NOT_DEFINED) {
            return "";
        }

        // None of them were applicable.
        throw new IllegalArgumentException("The given value (" + argCheck + ") is a value that is not defined in the string group [BlancoDbLoggingMode].");
    }
}
