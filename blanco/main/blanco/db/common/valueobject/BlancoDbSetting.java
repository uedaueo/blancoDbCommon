package blanco.db.common.valueobject;

/**
 * blancoDbが起動するための設定情報。
 */
public class BlancoDbSetting {
    /**
     * 出力先ディレクトリ。
     *
     * フィールド: [TargetDir]。
     */
    private String fTargetDir;

    /**
     * 基準となるパッケージ名。
     *
     * フィールド: [BasePackage]。
     */
    private String fBasePackage;

    /**
     * SQL 定義書の処理中にエラーが発生した場合に処理中断するかどうか。2012.01 時点では Java のみ対応済み。
     *
     * フィールド: [failonerror]。
     * デフォルト: [false]。
     */
    private boolean fFailonerror = false;

    /**
     * ロギングを行うかどうか。
     *
     * フィールド: [logging]。
     */
    private boolean fLogging;

    /**
     * ロギングのモード。0:ノーマルなdebugログを報告。1.パフォーマンス報告付き報告。2:SQLIDのみ報告。
     *
     * フィールド: [loggingMode]。
     * デフォルト: [0]。
     */
    private int fLoggingMode = 0;

    /**
     * SQL のロギングを行うかどうか。logging と異なり、loggingsql では より可読性の高いログを出力します。
     *
     * フィールド: [loggingsql]。
     * デフォルト: [false]。
     */
    private boolean fLoggingsql = false;

    /**
     * JDBC接続の際に利用するJDBCドライバ・クラス名
     *
     * フィールド: [jdbcdriver]。
     */
    private String fJdbcdriver;

    /**
     * JDBC接続の際に利用するJDBC URI名
     *
     * フィールド: [jdbcurl]。
     */
    private String fJdbcurl;

    /**
     * JDBC接続の際に利用するユーザ名
     *
     * フィールド: [jdbcuser]。
     */
    private String fJdbcuser;

    /**
     * JDBC接続の際に利用するパスワード
     *
     * フィールド: [jdbcpassword]。
     */
    private String fJdbcpassword;

    /**
     * JDBCドライバの jar ファイル名。指定した場合には該当ファイルを利用して接続を試みる。複数ファイルあるばあいには「;」で区切る。
     *
     * フィールド: [jdbcdriverfile]。
     */
    private String fJdbcdriverfile;

    /**
     * JDBC接続の際に利用するスキーマ名 主にOracleの際に利用されます。
     *
     * フィールド: [schema]。
     */
    private String fSchema;

    /**
     * 接続確立後に得られる実際のJDBCドライバの名称。
     *
     * フィールド: [DriverName]。
     * デフォルト: [-1]。
     */
    private int fDriverName = -1;

    /**
     * ランタイムパッケージ名。null以外の場合には、その指定されたパッケージへとランタイムクラスを生成します。
     *
     * フィールド: [RuntimePackage]。
     */
    private String fRuntimePackage;

    /**
     * ステートメントタイムアウト値。-1は無指定を示す。デフォルト値は-1。
     *
     * フィールド: [StatementTimeout]。
     * デフォルト: [-1]。
     */
    private int fStatementTimeout = -1;

    /**
     * ソースコード自動生成時にSQL定義のSQL文を実行するかどうかを設定するフラグ。デフォルトは iterator。iterator:検索型のみSQL文を実行して検証する。none:SQL文は実行しない。
     *
     * フィールド: [executeSql]。
     * デフォルト: [2]。
     */
    private int fExecuteSql = 2;

    /**
     * 自動生成するソースファイルの文字エンコーディングを指定します。
     *
     * フィールド: [encoding]。
     */
    private String fEncoding;

    /**
     * 文字列について、Microsoft Windows 3.1日本語版のユニコードへと変換するかどうか.
     *
     * フィールド: [convertStringToMsWindows31jUnicode]。
     * デフォルト: [false]。
     */
    private boolean fConvertStringToMsWindows31jUnicode = false;

    /**
     * ランタイムを使用してインタフェイスやアノテーションを設定します。
     *
     * フィールド: [useRuntime]。
     * デフォルト: [false]。
     */
    private boolean fUseRuntime = false;

    /**
     * 出力先フォルダの書式を指定します。&lt;br&gt;
     *
     * blanco: [targetdir]/main<br>
     * maven: [targetdir]/main/java<br>
     * free: [targetdir](targetdirが無指定の場合はblanco/main)
     * フィールド: [targetStyle]。
     * デフォルト: [&quot;blanco&quot;]。
     */
    private String fTargetStyle = "blanco";

    /**
     * 行末記号をしていします。LF=0x0a, CR=0x0d, CFLF=0x0d0x0a とします。LFがデフォルトです。
     *
     * フィールド: [lineSeparator]。
     * デフォルト: [&quot;LF&quot;]。
     */
    private String fLineSeparator = "LF";

    /**
     * 自動生成に関してより詳細な情報を出力します。
     *
     * フィールド: [verbose]。
     * デフォルト: [false]。
     */
    private boolean fVerbose = false;

    /**
     * micronaut向けに Row クラスに Introspected アノテーションを付与します
     *
     * フィールド: [addIntrospected]。
     * デフォルト: [false]。
     */
    private boolean fAddIntrospected = false;

    /**
     * finalizeメソッドを生成しません。Java9以降は非推奨となっているのでデフォルト値を True とします。
     *
     * フィールド: [noFinalize]。
     * デフォルト: [true]。
     */
    private boolean fNoFinalize = true;

    /**
     * フィールド [TargetDir] の値を設定します。
     *
     * フィールドの説明: [出力先ディレクトリ。]。
     *
     * @param argTargetDir フィールド[TargetDir]に設定する値。
     */
    public void setTargetDir(final String argTargetDir) {
        fTargetDir = argTargetDir;
    }

    /**
     * フィールド [TargetDir] の値を取得します。
     *
     * フィールドの説明: [出力先ディレクトリ。]。
     *
     * @return フィールド[TargetDir]から取得した値。
     */
    public String getTargetDir() {
        return fTargetDir;
    }

    /**
     * フィールド [BasePackage] の値を設定します。
     *
     * フィールドの説明: [基準となるパッケージ名。]。
     *
     * @param argBasePackage フィールド[BasePackage]に設定する値。
     */
    public void setBasePackage(final String argBasePackage) {
        fBasePackage = argBasePackage;
    }

    /**
     * フィールド [BasePackage] の値を取得します。
     *
     * フィールドの説明: [基準となるパッケージ名。]。
     *
     * @return フィールド[BasePackage]から取得した値。
     */
    public String getBasePackage() {
        return fBasePackage;
    }

    /**
     * フィールド [failonerror] の値を設定します。
     *
     * フィールドの説明: [SQL 定義書の処理中にエラーが発生した場合に処理中断するかどうか。2012.01 時点では Java のみ対応済み。]。
     *
     * @param argFailonerror フィールド[failonerror]に設定する値。
     */
    public void setFailonerror(final boolean argFailonerror) {
        fFailonerror = argFailonerror;
    }

    /**
     * フィールド [failonerror] の値を取得します。
     *
     * フィールドの説明: [SQL 定義書の処理中にエラーが発生した場合に処理中断するかどうか。2012.01 時点では Java のみ対応済み。]。
     * デフォルト: [false]。
     *
     * @return フィールド[failonerror]から取得した値。
     */
    public boolean getFailonerror() {
        return fFailonerror;
    }

    /**
     * フィールド [logging] の値を設定します。
     *
     * フィールドの説明: [ロギングを行うかどうか。]。
     *
     * @param argLogging フィールド[logging]に設定する値。
     */
    public void setLogging(final boolean argLogging) {
        fLogging = argLogging;
    }

    /**
     * フィールド [logging] の値を取得します。
     *
     * フィールドの説明: [ロギングを行うかどうか。]。
     *
     * @return フィールド[logging]から取得した値。
     */
    public boolean getLogging() {
        return fLogging;
    }

    /**
     * フィールド [loggingMode] の値を設定します。
     *
     * フィールドの説明: [ロギングのモード。0:ノーマルなdebugログを報告。1.パフォーマンス報告付き報告。2:SQLIDのみ報告。]。
     *
     * @param argLoggingMode フィールド[loggingMode]に設定する値。
     */
    public void setLoggingMode(final int argLoggingMode) {
        fLoggingMode = argLoggingMode;
    }

    /**
     * フィールド [loggingMode] の値を取得します。
     *
     * フィールドの説明: [ロギングのモード。0:ノーマルなdebugログを報告。1.パフォーマンス報告付き報告。2:SQLIDのみ報告。]。
     * デフォルト: [0]。
     *
     * @return フィールド[loggingMode]から取得した値。
     */
    public int getLoggingMode() {
        return fLoggingMode;
    }

    /**
     * フィールド [loggingsql] の値を設定します。
     *
     * フィールドの説明: [SQL のロギングを行うかどうか。logging と異なり、loggingsql では より可読性の高いログを出力します。]。
     *
     * @param argLoggingsql フィールド[loggingsql]に設定する値。
     */
    public void setLoggingsql(final boolean argLoggingsql) {
        fLoggingsql = argLoggingsql;
    }

    /**
     * フィールド [loggingsql] の値を取得します。
     *
     * フィールドの説明: [SQL のロギングを行うかどうか。logging と異なり、loggingsql では より可読性の高いログを出力します。]。
     * デフォルト: [false]。
     *
     * @return フィールド[loggingsql]から取得した値。
     */
    public boolean getLoggingsql() {
        return fLoggingsql;
    }

    /**
     * フィールド [jdbcdriver] の値を設定します。
     *
     * フィールドの説明: [JDBC接続の際に利用するJDBCドライバ・クラス名]。
     *
     * @param argJdbcdriver フィールド[jdbcdriver]に設定する値。
     */
    public void setJdbcdriver(final String argJdbcdriver) {
        fJdbcdriver = argJdbcdriver;
    }

    /**
     * フィールド [jdbcdriver] の値を取得します。
     *
     * フィールドの説明: [JDBC接続の際に利用するJDBCドライバ・クラス名]。
     *
     * @return フィールド[jdbcdriver]から取得した値。
     */
    public String getJdbcdriver() {
        return fJdbcdriver;
    }

    /**
     * フィールド [jdbcurl] の値を設定します。
     *
     * フィールドの説明: [JDBC接続の際に利用するJDBC URI名]。
     *
     * @param argJdbcurl フィールド[jdbcurl]に設定する値。
     */
    public void setJdbcurl(final String argJdbcurl) {
        fJdbcurl = argJdbcurl;
    }

    /**
     * フィールド [jdbcurl] の値を取得します。
     *
     * フィールドの説明: [JDBC接続の際に利用するJDBC URI名]。
     *
     * @return フィールド[jdbcurl]から取得した値。
     */
    public String getJdbcurl() {
        return fJdbcurl;
    }

    /**
     * フィールド [jdbcuser] の値を設定します。
     *
     * フィールドの説明: [JDBC接続の際に利用するユーザ名]。
     *
     * @param argJdbcuser フィールド[jdbcuser]に設定する値。
     */
    public void setJdbcuser(final String argJdbcuser) {
        fJdbcuser = argJdbcuser;
    }

    /**
     * フィールド [jdbcuser] の値を取得します。
     *
     * フィールドの説明: [JDBC接続の際に利用するユーザ名]。
     *
     * @return フィールド[jdbcuser]から取得した値。
     */
    public String getJdbcuser() {
        return fJdbcuser;
    }

    /**
     * フィールド [jdbcpassword] の値を設定します。
     *
     * フィールドの説明: [JDBC接続の際に利用するパスワード]。
     *
     * @param argJdbcpassword フィールド[jdbcpassword]に設定する値。
     */
    public void setJdbcpassword(final String argJdbcpassword) {
        fJdbcpassword = argJdbcpassword;
    }

    /**
     * フィールド [jdbcpassword] の値を取得します。
     *
     * フィールドの説明: [JDBC接続の際に利用するパスワード]。
     *
     * @return フィールド[jdbcpassword]から取得した値。
     */
    public String getJdbcpassword() {
        return fJdbcpassword;
    }

    /**
     * フィールド [jdbcdriverfile] の値を設定します。
     *
     * フィールドの説明: [JDBCドライバの jar ファイル名。指定した場合には該当ファイルを利用して接続を試みる。複数ファイルあるばあいには「;」で区切る。]。
     *
     * @param argJdbcdriverfile フィールド[jdbcdriverfile]に設定する値。
     */
    public void setJdbcdriverfile(final String argJdbcdriverfile) {
        fJdbcdriverfile = argJdbcdriverfile;
    }

    /**
     * フィールド [jdbcdriverfile] の値を取得します。
     *
     * フィールドの説明: [JDBCドライバの jar ファイル名。指定した場合には該当ファイルを利用して接続を試みる。複数ファイルあるばあいには「;」で区切る。]。
     *
     * @return フィールド[jdbcdriverfile]から取得した値。
     */
    public String getJdbcdriverfile() {
        return fJdbcdriverfile;
    }

    /**
     * フィールド [schema] の値を設定します。
     *
     * フィールドの説明: [JDBC接続の際に利用するスキーマ名 主にOracleの際に利用されます。]。
     *
     * @param argSchema フィールド[schema]に設定する値。
     */
    public void setSchema(final String argSchema) {
        fSchema = argSchema;
    }

    /**
     * フィールド [schema] の値を取得します。
     *
     * フィールドの説明: [JDBC接続の際に利用するスキーマ名 主にOracleの際に利用されます。]。
     *
     * @return フィールド[schema]から取得した値。
     */
    public String getSchema() {
        return fSchema;
    }

    /**
     * フィールド [DriverName] の値を設定します。
     *
     * フィールドの説明: [接続確立後に得られる実際のJDBCドライバの名称。]。
     *
     * @param argDriverName フィールド[DriverName]に設定する値。
     */
    public void setDriverName(final int argDriverName) {
        fDriverName = argDriverName;
    }

    /**
     * フィールド [DriverName] の値を取得します。
     *
     * フィールドの説明: [接続確立後に得られる実際のJDBCドライバの名称。]。
     * デフォルト: [-1]。
     *
     * @return フィールド[DriverName]から取得した値。
     */
    public int getDriverName() {
        return fDriverName;
    }

    /**
     * フィールド [RuntimePackage] の値を設定します。
     *
     * フィールドの説明: [ランタイムパッケージ名。null以外の場合には、その指定されたパッケージへとランタイムクラスを生成します。]。
     *
     * @param argRuntimePackage フィールド[RuntimePackage]に設定する値。
     */
    public void setRuntimePackage(final String argRuntimePackage) {
        fRuntimePackage = argRuntimePackage;
    }

    /**
     * フィールド [RuntimePackage] の値を取得します。
     *
     * フィールドの説明: [ランタイムパッケージ名。null以外の場合には、その指定されたパッケージへとランタイムクラスを生成します。]。
     *
     * @return フィールド[RuntimePackage]から取得した値。
     */
    public String getRuntimePackage() {
        return fRuntimePackage;
    }

    /**
     * フィールド [StatementTimeout] の値を設定します。
     *
     * フィールドの説明: [ステートメントタイムアウト値。-1は無指定を示す。デフォルト値は-1。]。
     *
     * @param argStatementTimeout フィールド[StatementTimeout]に設定する値。
     */
    public void setStatementTimeout(final int argStatementTimeout) {
        fStatementTimeout = argStatementTimeout;
    }

    /**
     * フィールド [StatementTimeout] の値を取得します。
     *
     * フィールドの説明: [ステートメントタイムアウト値。-1は無指定を示す。デフォルト値は-1。]。
     * デフォルト: [-1]。
     *
     * @return フィールド[StatementTimeout]から取得した値。
     */
    public int getStatementTimeout() {
        return fStatementTimeout;
    }

    /**
     * フィールド [executeSql] の値を設定します。
     *
     * フィールドの説明: [ソースコード自動生成時にSQL定義のSQL文を実行するかどうかを設定するフラグ。デフォルトは iterator。iterator:検索型のみSQL文を実行して検証する。none:SQL文は実行しない。]。
     *
     * @param argExecuteSql フィールド[executeSql]に設定する値。
     */
    public void setExecuteSql(final int argExecuteSql) {
        fExecuteSql = argExecuteSql;
    }

    /**
     * フィールド [executeSql] の値を取得します。
     *
     * フィールドの説明: [ソースコード自動生成時にSQL定義のSQL文を実行するかどうかを設定するフラグ。デフォルトは iterator。iterator:検索型のみSQL文を実行して検証する。none:SQL文は実行しない。]。
     * デフォルト: [2]。
     *
     * @return フィールド[executeSql]から取得した値。
     */
    public int getExecuteSql() {
        return fExecuteSql;
    }

    /**
     * フィールド [encoding] の値を設定します。
     *
     * フィールドの説明: [自動生成するソースファイルの文字エンコーディングを指定します。]。
     *
     * @param argEncoding フィールド[encoding]に設定する値。
     */
    public void setEncoding(final String argEncoding) {
        fEncoding = argEncoding;
    }

    /**
     * フィールド [encoding] の値を取得します。
     *
     * フィールドの説明: [自動生成するソースファイルの文字エンコーディングを指定します。]。
     *
     * @return フィールド[encoding]から取得した値。
     */
    public String getEncoding() {
        return fEncoding;
    }

    /**
     * フィールド [convertStringToMsWindows31jUnicode] の値を設定します。
     *
     * フィールドの説明: [文字列について、Microsoft Windows 3.1日本語版のユニコードへと変換するかどうか.]。
     *
     * @param argConvertStringToMsWindows31jUnicode フィールド[convertStringToMsWindows31jUnicode]に設定する値。
     */
    public void setConvertStringToMsWindows31jUnicode(final boolean argConvertStringToMsWindows31jUnicode) {
        fConvertStringToMsWindows31jUnicode = argConvertStringToMsWindows31jUnicode;
    }

    /**
     * フィールド [convertStringToMsWindows31jUnicode] の値を取得します。
     *
     * フィールドの説明: [文字列について、Microsoft Windows 3.1日本語版のユニコードへと変換するかどうか.]。
     * デフォルト: [false]。
     *
     * @return フィールド[convertStringToMsWindows31jUnicode]から取得した値。
     */
    public boolean getConvertStringToMsWindows31jUnicode() {
        return fConvertStringToMsWindows31jUnicode;
    }

    /**
     * フィールド [useRuntime] の値を設定します。
     *
     * フィールドの説明: [ランタイムを使用してインタフェイスやアノテーションを設定します。]。
     *
     * @param argUseRuntime フィールド[useRuntime]に設定する値。
     */
    public void setUseRuntime(final boolean argUseRuntime) {
        fUseRuntime = argUseRuntime;
    }

    /**
     * フィールド [useRuntime] の値を取得します。
     *
     * フィールドの説明: [ランタイムを使用してインタフェイスやアノテーションを設定します。]。
     * デフォルト: [false]。
     *
     * @return フィールド[useRuntime]から取得した値。
     */
    public boolean getUseRuntime() {
        return fUseRuntime;
    }

    /**
     * フィールド [targetStyle] の値を設定します。
     *
     * フィールドの説明: [出力先フォルダの書式を指定します。<br>]。
     * blanco: [targetdir]/main<br>
     * maven: [targetdir]/main/java<br>
     * free: [targetdir](targetdirが無指定の場合はblanco/main)
     *
     * @param argTargetStyle フィールド[targetStyle]に設定する値。
     */
    public void setTargetStyle(final String argTargetStyle) {
        fTargetStyle = argTargetStyle;
    }

    /**
     * フィールド [targetStyle] の値を取得します。
     *
     * フィールドの説明: [出力先フォルダの書式を指定します。<br>]。
     * blanco: [targetdir]/main<br>
     * maven: [targetdir]/main/java<br>
     * free: [targetdir](targetdirが無指定の場合はblanco/main)
     * デフォルト: [&quot;blanco&quot;]。
     *
     * @return フィールド[targetStyle]から取得した値。
     */
    public String getTargetStyle() {
        return fTargetStyle;
    }

    /**
     * フィールド [lineSeparator] の値を設定します。
     *
     * フィールドの説明: [行末記号をしていします。LF=0x0a, CR=0x0d, CFLF=0x0d0x0a とします。LFがデフォルトです。]。
     *
     * @param argLineSeparator フィールド[lineSeparator]に設定する値。
     */
    public void setLineSeparator(final String argLineSeparator) {
        fLineSeparator = argLineSeparator;
    }

    /**
     * フィールド [lineSeparator] の値を取得します。
     *
     * フィールドの説明: [行末記号をしていします。LF=0x0a, CR=0x0d, CFLF=0x0d0x0a とします。LFがデフォルトです。]。
     * デフォルト: [&quot;LF&quot;]。
     *
     * @return フィールド[lineSeparator]から取得した値。
     */
    public String getLineSeparator() {
        return fLineSeparator;
    }

    /**
     * フィールド [verbose] の値を設定します。
     *
     * フィールドの説明: [自動生成に関してより詳細な情報を出力します。]。
     *
     * @param argVerbose フィールド[verbose]に設定する値。
     */
    public void setVerbose(final boolean argVerbose) {
        fVerbose = argVerbose;
    }

    /**
     * フィールド [verbose] の値を取得します。
     *
     * フィールドの説明: [自動生成に関してより詳細な情報を出力します。]。
     * デフォルト: [false]。
     *
     * @return フィールド[verbose]から取得した値。
     */
    public boolean getVerbose() {
        return fVerbose;
    }

    /**
     * フィールド [addIntrospected] の値を設定します。
     *
     * フィールドの説明: [micronaut向けに Row クラスに Introspected アノテーションを付与します]。
     *
     * @param argAddIntrospected フィールド[addIntrospected]に設定する値。
     */
    public void setAddIntrospected(final boolean argAddIntrospected) {
        fAddIntrospected = argAddIntrospected;
    }

    /**
     * フィールド [addIntrospected] の値を取得します。
     *
     * フィールドの説明: [micronaut向けに Row クラスに Introspected アノテーションを付与します]。
     * デフォルト: [false]。
     *
     * @return フィールド[addIntrospected]から取得した値。
     */
    public boolean getAddIntrospected() {
        return fAddIntrospected;
    }

    /**
     * フィールド [noFinalize] の値を設定します。
     *
     * フィールドの説明: [finalizeメソッドを生成しません。Java9以降は非推奨となっているのでデフォルト値を True とします。]。
     *
     * @param argNoFinalize フィールド[noFinalize]に設定する値。
     */
    public void setNoFinalize(final boolean argNoFinalize) {
        fNoFinalize = argNoFinalize;
    }

    /**
     * フィールド [noFinalize] の値を取得します。
     *
     * フィールドの説明: [finalizeメソッドを生成しません。Java9以降は非推奨となっているのでデフォルト値を True とします。]。
     * デフォルト: [true]。
     *
     * @return フィールド[noFinalize]から取得した値。
     */
    public boolean getNoFinalize() {
        return fNoFinalize;
    }

    /**
     * Gets the string representation of this value object.
     *
     * <P>Precautions for use</P>
     * <UL>
     * <LI>Only the shallow range of the object will be subject to the stringification process.
     * <LI>Do not use this method if the object has a circular reference.
     * </UL>
     *
     * @return String representation of a value object.
     */
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("blanco.db.common.valueobject.BlancoDbSetting[");
        buf.append("TargetDir=" + fTargetDir);
        buf.append(",BasePackage=" + fBasePackage);
        buf.append(",failonerror=" + fFailonerror);
        buf.append(",logging=" + fLogging);
        buf.append(",loggingMode=" + fLoggingMode);
        buf.append(",loggingsql=" + fLoggingsql);
        buf.append(",jdbcdriver=" + fJdbcdriver);
        buf.append(",jdbcurl=" + fJdbcurl);
        buf.append(",jdbcuser=" + fJdbcuser);
        buf.append(",jdbcpassword=" + fJdbcpassword);
        buf.append(",jdbcdriverfile=" + fJdbcdriverfile);
        buf.append(",schema=" + fSchema);
        buf.append(",DriverName=" + fDriverName);
        buf.append(",RuntimePackage=" + fRuntimePackage);
        buf.append(",StatementTimeout=" + fStatementTimeout);
        buf.append(",executeSql=" + fExecuteSql);
        buf.append(",encoding=" + fEncoding);
        buf.append(",convertStringToMsWindows31jUnicode=" + fConvertStringToMsWindows31jUnicode);
        buf.append(",useRuntime=" + fUseRuntime);
        buf.append(",targetStyle=" + fTargetStyle);
        buf.append(",lineSeparator=" + fLineSeparator);
        buf.append(",verbose=" + fVerbose);
        buf.append(",addIntrospected=" + fAddIntrospected);
        buf.append(",noFinalize=" + fNoFinalize);
        buf.append("]");
        return buf.toString();
    }

    /**
     * Copies this value object to the specified target.
     *
     * <P>Cautions for use</P>
     * <UL>
     * <LI>Only the shallow range of the object will be subject to the copying process.
     * <LI>Do not use this method if the object has a circular reference.
     * </UL>
     *
     * @param target target value object.
     */
    public void copyTo(final BlancoDbSetting target) {
        if (target == null) {
            throw new IllegalArgumentException("Bug: BlancoDbSetting#copyTo(target): argument 'target' is null");
        }

        // No needs to copy parent class.

        // Name: fTargetDir
        // Type: java.lang.String
        target.fTargetDir = this.fTargetDir;
        // Name: fBasePackage
        // Type: java.lang.String
        target.fBasePackage = this.fBasePackage;
        // Name: fFailonerror
        // Type: boolean
        target.fFailonerror = this.fFailonerror;
        // Name: fLogging
        // Type: boolean
        target.fLogging = this.fLogging;
        // Name: fLoggingMode
        // Type: int
        target.fLoggingMode = this.fLoggingMode;
        // Name: fLoggingsql
        // Type: boolean
        target.fLoggingsql = this.fLoggingsql;
        // Name: fJdbcdriver
        // Type: java.lang.String
        target.fJdbcdriver = this.fJdbcdriver;
        // Name: fJdbcurl
        // Type: java.lang.String
        target.fJdbcurl = this.fJdbcurl;
        // Name: fJdbcuser
        // Type: java.lang.String
        target.fJdbcuser = this.fJdbcuser;
        // Name: fJdbcpassword
        // Type: java.lang.String
        target.fJdbcpassword = this.fJdbcpassword;
        // Name: fJdbcdriverfile
        // Type: java.lang.String
        target.fJdbcdriverfile = this.fJdbcdriverfile;
        // Name: fSchema
        // Type: java.lang.String
        target.fSchema = this.fSchema;
        // Name: fDriverName
        // Type: int
        target.fDriverName = this.fDriverName;
        // Name: fRuntimePackage
        // Type: java.lang.String
        target.fRuntimePackage = this.fRuntimePackage;
        // Name: fStatementTimeout
        // Type: int
        target.fStatementTimeout = this.fStatementTimeout;
        // Name: fExecuteSql
        // Type: int
        target.fExecuteSql = this.fExecuteSql;
        // Name: fEncoding
        // Type: java.lang.String
        target.fEncoding = this.fEncoding;
        // Name: fConvertStringToMsWindows31jUnicode
        // Type: boolean
        target.fConvertStringToMsWindows31jUnicode = this.fConvertStringToMsWindows31jUnicode;
        // Name: fUseRuntime
        // Type: boolean
        target.fUseRuntime = this.fUseRuntime;
        // Name: fTargetStyle
        // Type: java.lang.String
        target.fTargetStyle = this.fTargetStyle;
        // Name: fLineSeparator
        // Type: java.lang.String
        target.fLineSeparator = this.fLineSeparator;
        // Name: fVerbose
        // Type: boolean
        target.fVerbose = this.fVerbose;
        // Name: fAddIntrospected
        // Type: boolean
        target.fAddIntrospected = this.fAddIntrospected;
        // Name: fNoFinalize
        // Type: boolean
        target.fNoFinalize = this.fNoFinalize;
    }
}
