package blanco.db.common.stringgroup;

/**
 * SQL情報のスクロール方向。
 */
public class BlancoDbSqlInfoScrollStringGroup {
    /**
     * No.1 説明:前方のみ。
     */
    public static final int TYPE_FORWARD_ONLY = 1;

    /**
     * No.2 説明:インセンシティブ。
     */
    public static final int TYPE_SCROLL_INSENSITIVE = 2;

    /**
     * No.3 説明:センシティブ。
     */
    public static final int TYPE_SCROLL_SENSITIVE = 3;

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
        // 説明:前方のみ。
        if ("forward_only".equals(argCheck)) {
            return true;
        }
        // No.2
        // 説明:インセンシティブ。
        if ("insensitive".equals(argCheck)) {
            return true;
        }
        // No.3
        // 説明:センシティブ。
        if ("sensitive".equals(argCheck)) {
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
        // 説明:前方のみ。
        if ("forward_only".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.2
        // 説明:インセンシティブ。
        if ("insensitive".equalsIgnoreCase(argCheck)) {
            return true;
        }
        // No.3
        // 説明:センシティブ。
        if ("sensitive".equalsIgnoreCase(argCheck)) {
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
        // 説明:前方のみ。
        if ("forward_only".equals(argCheck)) {
            return TYPE_FORWARD_ONLY;
        }
        // No.2
        // 説明:インセンシティブ。
        if ("insensitive".equals(argCheck)) {
            return TYPE_SCROLL_INSENSITIVE;
        }
        // No.3
        // 説明:センシティブ。
        if ("sensitive".equals(argCheck)) {
            return TYPE_SCROLL_SENSITIVE;
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
        // 説明:前方のみ。
        if (argCheck == TYPE_FORWARD_ONLY) {
            return "forward_only";
        }
        // No.2
        // 説明:インセンシティブ。
        if (argCheck == TYPE_SCROLL_INSENSITIVE) {
            return "insensitive";
        }
        // No.3
        // 説明:センシティブ。
        if (argCheck == TYPE_SCROLL_SENSITIVE) {
            return "sensitive";
        }
        // Undefined.
        if (argCheck == NOT_DEFINED) {
            return "";
        }

        // None of them were applicable.
        throw new IllegalArgumentException("The given value (" + argCheck + ") is a value that is not defined in the string group [BlancoDbSqlInfoScroll].");
    }
}
