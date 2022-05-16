package blanco.db.common.valueobject;

import java.util.List;
import java.util.Map;

import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * SQL情報に関する構造。SQL定義書を抽象的にあらわしたものにも相当します。オリジナル作者 Yasuo Nakanishi
 */
public class BlancoDbSqlInfoStructure {
    /**
     * SQL定義ID。
     *
     * フィールド: [name]。
     */
    private String fName;

    /**
     * 基準パッケージ。
     *
     * フィールド: [package]。
     */
    private String fPackage;

    /**
     * SQLのタイプ。xmlのquery-typeに相当。BlancoDbSqlInfoTypeStringGroupの中から、ITERATOR(検索型), INVOKER(実行型), CALLER(呼出型)のいずれかの値を選びます。
     *
     * フィールド: [type]。
     */
    private int fType;

    /**
     * このクエリの詳細。
     *
     * フィールド: [description]。
     */
    private String fDescription;

    /**
     * SQL定義のSQL文そのもの。
     *
     * フィールド: [query]。
     */
    private String fQuery;

    /**
     * SQL入力パラメータのリスト。
     *
     * フィールド: [inParameterList]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure&gt;()]。
     */
    private List<BlancoDbMetaDataColumnStructure> fInParameterList = new java.util.ArrayList<blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure>();

    /**
     * (caller)呼出型SQLのSQL出力パラメータのリスト。Callerの場合にのみ利用されます。
     *
     * フィールド: [outParameterList]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure&gt;()]。
     */
    private List<BlancoDbMetaDataColumnStructure> fOutParameterList = new java.util.ArrayList<blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure>();

    /**
     * (iterator)検索型SQLの検索結果の列リスト。Iteratorの場合にのみ利用されます。
     *
     * フィールド: [resultSetColumnList]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure&gt;()]。
     */
    private List<BlancoDbMetaDataColumnStructure> fResultSetColumnList = new java.util.ArrayList<blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure>();

    /**
     * 一行制約があるかどうか。xmlのsingleに相当。
     *
     * フィールド: [single]。
     * デフォルト: [false]。
     */
    private boolean fSingle = false;

    /**
     * (iterator)検索型SQLのカーソルスクロールの属性。xmlのscrollに相当。forward_onlyなどが格納される。
     *
     * フィールド: [scroll]。
     */
    private int fScroll;

    /**
     * (iterator)検索型SQLの更新可能属性。xmlのupdatableに相当。
     *
     * フィールド: [updatable]。
     * デフォルト: [false]。
     */
    private boolean fUpdatable = false;

    /**
     * 動的SQLかどうか。
     *
     * フィールド: [dynamic-sql]。
     * デフォルト: [false]。
     */
    private boolean fDynamicSql = false;

    /**
     * パラメータにBeanを利用するかどうか。
     *
     * フィールド: [use-bean-parameter]。
     * デフォルト: [false]。
     */
    private boolean fUseBeanParameter = false;

    /**
     * ステートメント・タイムアウト (秒)
     *
     * フィールド: [statementTimeout]。
     * デフォルト: [-1]。
     */
    private int fStatementTimeout = -1;

    /**
     * 接続方式：処理系標準(STANDARD)または独自実装(ORIGINAL)
     *
     * フィールド: [connectionMethod]。
     * デフォルト: [&quot;STANDARD&quot;]。
     */
    private String fConnectionMethod = "STANDARD";

    /**
     * 接続先：デフォルト(DEFAULT)または代替接続(ALTERNATIVE)
     *
     * フィールド: [connectTo]。
     * デフォルト: [&quot;DEFAULT&quot;]。
     */
    private String fConnectTo = "DEFAULT";

    /**
     * SQL動的条件式定義のリスト
     *
     * フィールド: [dynamicConditionList]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.db.common.valueobject.BlancoDbDynamicConditionStructure&gt;()]。
     */
    private List<BlancoDbDynamicConditionStructure> fDynamicConditionList = new java.util.ArrayList<blanco.db.common.valueobject.BlancoDbDynamicConditionStructure>();

    /**
     * SQL動的条件式関数定義のマップ
     *
     * フィールド: [dynamicConditionFunctionMap]。
     * デフォルト: [new java.util.HashMap&lt;java.lang.String, blanco.db.common.valueobject.BlancoDbDynamicConditionFunctionStructure&gt;()]。
     */
    private Map<String, BlancoDbDynamicConditionFunctionStructure> fDynamicConditionFunctionMap = new java.util.HashMap<java.lang.String, blanco.db.common.valueobject.BlancoDbDynamicConditionFunctionStructure>();

    /**
     * MySQL専用。検索型の場合に先頭のSELECT の後ろに  MAX_EXECUTION_TIME オプティマイザ・ヒントが設定されます。デフォルトは60000ms。実行時に変更可能。
     *
     * フィールド: [useTimeoutHintMySQL]。
     * デフォルト: [false]。
     */
    private boolean fUseTimeoutHintMySQL = false;

    /**
     * フィールド [name] の値を設定します。
     *
     * フィールドの説明: [SQL定義ID。]。
     *
     * @param argName フィールド[name]に設定する値。
     */
    public void setName(final String argName) {
        fName = argName;
    }

    /**
     * フィールド [name] の値を取得します。
     *
     * フィールドの説明: [SQL定義ID。]。
     *
     * @return フィールド[name]から取得した値。
     */
    public String getName() {
        return fName;
    }

    /**
     * フィールド [package] の値を設定します。
     *
     * フィールドの説明: [基準パッケージ。]。
     *
     * @param argPackage フィールド[package]に設定する値。
     */
    public void setPackage(final String argPackage) {
        fPackage = argPackage;
    }

    /**
     * フィールド [package] の値を取得します。
     *
     * フィールドの説明: [基準パッケージ。]。
     *
     * @return フィールド[package]から取得した値。
     */
    public String getPackage() {
        return fPackage;
    }

    /**
     * フィールド [type] の値を設定します。
     *
     * フィールドの説明: [SQLのタイプ。xmlのquery-typeに相当。BlancoDbSqlInfoTypeStringGroupの中から、ITERATOR(検索型), INVOKER(実行型), CALLER(呼出型)のいずれかの値を選びます。]。
     *
     * @param argType フィールド[type]に設定する値。
     */
    public void setType(final int argType) {
        fType = argType;
    }

    /**
     * フィールド [type] の値を取得します。
     *
     * フィールドの説明: [SQLのタイプ。xmlのquery-typeに相当。BlancoDbSqlInfoTypeStringGroupの中から、ITERATOR(検索型), INVOKER(実行型), CALLER(呼出型)のいずれかの値を選びます。]。
     *
     * @return フィールド[type]から取得した値。
     */
    public int getType() {
        return fType;
    }

    /**
     * フィールド [description] の値を設定します。
     *
     * フィールドの説明: [このクエリの詳細。]。
     *
     * @param argDescription フィールド[description]に設定する値。
     */
    public void setDescription(final String argDescription) {
        fDescription = argDescription;
    }

    /**
     * フィールド [description] の値を取得します。
     *
     * フィールドの説明: [このクエリの詳細。]。
     *
     * @return フィールド[description]から取得した値。
     */
    public String getDescription() {
        return fDescription;
    }

    /**
     * フィールド [query] の値を設定します。
     *
     * フィールドの説明: [SQL定義のSQL文そのもの。]。
     *
     * @param argQuery フィールド[query]に設定する値。
     */
    public void setQuery(final String argQuery) {
        fQuery = argQuery;
    }

    /**
     * フィールド [query] の値を取得します。
     *
     * フィールドの説明: [SQL定義のSQL文そのもの。]。
     *
     * @return フィールド[query]から取得した値。
     */
    public String getQuery() {
        return fQuery;
    }

    /**
     * フィールド [inParameterList] の値を設定します。
     *
     * フィールドの説明: [SQL入力パラメータのリスト。]。
     *
     * @param argInParameterList フィールド[inParameterList]に設定する値。
     */
    public void setInParameterList(final List<BlancoDbMetaDataColumnStructure> argInParameterList) {
        fInParameterList = argInParameterList;
    }

    /**
     * フィールド [inParameterList] の値を取得します。
     *
     * フィールドの説明: [SQL入力パラメータのリスト。]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure&gt;()]。
     *
     * @return フィールド[inParameterList]から取得した値。
     */
    public List<BlancoDbMetaDataColumnStructure> getInParameterList() {
        return fInParameterList;
    }

    /**
     * フィールド [outParameterList] の値を設定します。
     *
     * フィールドの説明: [(caller)呼出型SQLのSQL出力パラメータのリスト。Callerの場合にのみ利用されます。]。
     *
     * @param argOutParameterList フィールド[outParameterList]に設定する値。
     */
    public void setOutParameterList(final List<BlancoDbMetaDataColumnStructure> argOutParameterList) {
        fOutParameterList = argOutParameterList;
    }

    /**
     * フィールド [outParameterList] の値を取得します。
     *
     * フィールドの説明: [(caller)呼出型SQLのSQL出力パラメータのリスト。Callerの場合にのみ利用されます。]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure&gt;()]。
     *
     * @return フィールド[outParameterList]から取得した値。
     */
    public List<BlancoDbMetaDataColumnStructure> getOutParameterList() {
        return fOutParameterList;
    }

    /**
     * フィールド [resultSetColumnList] の値を設定します。
     *
     * フィールドの説明: [(iterator)検索型SQLの検索結果の列リスト。Iteratorの場合にのみ利用されます。]。
     *
     * @param argResultSetColumnList フィールド[resultSetColumnList]に設定する値。
     */
    public void setResultSetColumnList(final List<BlancoDbMetaDataColumnStructure> argResultSetColumnList) {
        fResultSetColumnList = argResultSetColumnList;
    }

    /**
     * フィールド [resultSetColumnList] の値を取得します。
     *
     * フィールドの説明: [(iterator)検索型SQLの検索結果の列リスト。Iteratorの場合にのみ利用されます。]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure&gt;()]。
     *
     * @return フィールド[resultSetColumnList]から取得した値。
     */
    public List<BlancoDbMetaDataColumnStructure> getResultSetColumnList() {
        return fResultSetColumnList;
    }

    /**
     * フィールド [single] の値を設定します。
     *
     * フィールドの説明: [一行制約があるかどうか。xmlのsingleに相当。]。
     *
     * @param argSingle フィールド[single]に設定する値。
     */
    public void setSingle(final boolean argSingle) {
        fSingle = argSingle;
    }

    /**
     * フィールド [single] の値を取得します。
     *
     * フィールドの説明: [一行制約があるかどうか。xmlのsingleに相当。]。
     * デフォルト: [false]。
     *
     * @return フィールド[single]から取得した値。
     */
    public boolean getSingle() {
        return fSingle;
    }

    /**
     * フィールド [scroll] の値を設定します。
     *
     * フィールドの説明: [(iterator)検索型SQLのカーソルスクロールの属性。xmlのscrollに相当。forward_onlyなどが格納される。]。
     *
     * @param argScroll フィールド[scroll]に設定する値。
     */
    public void setScroll(final int argScroll) {
        fScroll = argScroll;
    }

    /**
     * フィールド [scroll] の値を取得します。
     *
     * フィールドの説明: [(iterator)検索型SQLのカーソルスクロールの属性。xmlのscrollに相当。forward_onlyなどが格納される。]。
     *
     * @return フィールド[scroll]から取得した値。
     */
    public int getScroll() {
        return fScroll;
    }

    /**
     * フィールド [updatable] の値を設定します。
     *
     * フィールドの説明: [(iterator)検索型SQLの更新可能属性。xmlのupdatableに相当。]。
     *
     * @param argUpdatable フィールド[updatable]に設定する値。
     */
    public void setUpdatable(final boolean argUpdatable) {
        fUpdatable = argUpdatable;
    }

    /**
     * フィールド [updatable] の値を取得します。
     *
     * フィールドの説明: [(iterator)検索型SQLの更新可能属性。xmlのupdatableに相当。]。
     * デフォルト: [false]。
     *
     * @return フィールド[updatable]から取得した値。
     */
    public boolean getUpdatable() {
        return fUpdatable;
    }

    /**
     * フィールド [dynamic-sql] の値を設定します。
     *
     * フィールドの説明: [動的SQLかどうか。]。
     *
     * @param argDynamicSql フィールド[dynamic-sql]に設定する値。
     */
    public void setDynamicSql(final boolean argDynamicSql) {
        fDynamicSql = argDynamicSql;
    }

    /**
     * フィールド [dynamic-sql] の値を取得します。
     *
     * フィールドの説明: [動的SQLかどうか。]。
     * デフォルト: [false]。
     *
     * @return フィールド[dynamic-sql]から取得した値。
     */
    public boolean getDynamicSql() {
        return fDynamicSql;
    }

    /**
     * フィールド [use-bean-parameter] の値を設定します。
     *
     * フィールドの説明: [パラメータにBeanを利用するかどうか。]。
     *
     * @param argUseBeanParameter フィールド[use-bean-parameter]に設定する値。
     */
    public void setUseBeanParameter(final boolean argUseBeanParameter) {
        fUseBeanParameter = argUseBeanParameter;
    }

    /**
     * フィールド [use-bean-parameter] の値を取得します。
     *
     * フィールドの説明: [パラメータにBeanを利用するかどうか。]。
     * デフォルト: [false]。
     *
     * @return フィールド[use-bean-parameter]から取得した値。
     */
    public boolean getUseBeanParameter() {
        return fUseBeanParameter;
    }

    /**
     * フィールド [statementTimeout] の値を設定します。
     *
     * フィールドの説明: [ステートメント・タイムアウト (秒)]。
     *
     * @param argStatementTimeout フィールド[statementTimeout]に設定する値。
     */
    public void setStatementTimeout(final int argStatementTimeout) {
        fStatementTimeout = argStatementTimeout;
    }

    /**
     * フィールド [statementTimeout] の値を取得します。
     *
     * フィールドの説明: [ステートメント・タイムアウト (秒)]。
     * デフォルト: [-1]。
     *
     * @return フィールド[statementTimeout]から取得した値。
     */
    public int getStatementTimeout() {
        return fStatementTimeout;
    }

    /**
     * フィールド [connectionMethod] の値を設定します。
     *
     * フィールドの説明: [接続方式：処理系標準(STANDARD)または独自実装(ORIGINAL)]。
     *
     * @param argConnectionMethod フィールド[connectionMethod]に設定する値。
     */
    public void setConnectionMethod(final String argConnectionMethod) {
        fConnectionMethod = argConnectionMethod;
    }

    /**
     * フィールド [connectionMethod] の値を取得します。
     *
     * フィールドの説明: [接続方式：処理系標準(STANDARD)または独自実装(ORIGINAL)]。
     * デフォルト: [&quot;STANDARD&quot;]。
     *
     * @return フィールド[connectionMethod]から取得した値。
     */
    public String getConnectionMethod() {
        return fConnectionMethod;
    }

    /**
     * フィールド [connectTo] の値を設定します。
     *
     * フィールドの説明: [接続先：デフォルト(DEFAULT)または代替接続(ALTERNATIVE)]。
     *
     * @param argConnectTo フィールド[connectTo]に設定する値。
     */
    public void setConnectTo(final String argConnectTo) {
        fConnectTo = argConnectTo;
    }

    /**
     * フィールド [connectTo] の値を取得します。
     *
     * フィールドの説明: [接続先：デフォルト(DEFAULT)または代替接続(ALTERNATIVE)]。
     * デフォルト: [&quot;DEFAULT&quot;]。
     *
     * @return フィールド[connectTo]から取得した値。
     */
    public String getConnectTo() {
        return fConnectTo;
    }

    /**
     * フィールド [dynamicConditionList] の値を設定します。
     *
     * フィールドの説明: [SQL動的条件式定義のリスト]。
     *
     * @param argDynamicConditionList フィールド[dynamicConditionList]に設定する値。
     */
    public void setDynamicConditionList(final List<BlancoDbDynamicConditionStructure> argDynamicConditionList) {
        fDynamicConditionList = argDynamicConditionList;
    }

    /**
     * フィールド [dynamicConditionList] の値を取得します。
     *
     * フィールドの説明: [SQL動的条件式定義のリスト]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.db.common.valueobject.BlancoDbDynamicConditionStructure&gt;()]。
     *
     * @return フィールド[dynamicConditionList]から取得した値。
     */
    public List<BlancoDbDynamicConditionStructure> getDynamicConditionList() {
        return fDynamicConditionList;
    }

    /**
     * フィールド [dynamicConditionFunctionMap] の値を設定します。
     *
     * フィールドの説明: [SQL動的条件式関数定義のマップ]。
     *
     * @param argDynamicConditionFunctionMap フィールド[dynamicConditionFunctionMap]に設定する値。
     */
    public void setDynamicConditionFunctionMap(final Map<String, BlancoDbDynamicConditionFunctionStructure> argDynamicConditionFunctionMap) {
        fDynamicConditionFunctionMap = argDynamicConditionFunctionMap;
    }

    /**
     * フィールド [dynamicConditionFunctionMap] の値を取得します。
     *
     * フィールドの説明: [SQL動的条件式関数定義のマップ]。
     * デフォルト: [new java.util.HashMap&lt;java.lang.String, blanco.db.common.valueobject.BlancoDbDynamicConditionFunctionStructure&gt;()]。
     *
     * @return フィールド[dynamicConditionFunctionMap]から取得した値。
     */
    public Map<String, BlancoDbDynamicConditionFunctionStructure> getDynamicConditionFunctionMap() {
        return fDynamicConditionFunctionMap;
    }

    /**
     * フィールド [useTimeoutHintMySQL] の値を設定します。
     *
     * フィールドの説明: [MySQL専用。検索型の場合に先頭のSELECT の後ろに  MAX_EXECUTION_TIME オプティマイザ・ヒントが設定されます。デフォルトは60000ms。実行時に変更可能。]。
     *
     * @param argUseTimeoutHintMySQL フィールド[useTimeoutHintMySQL]に設定する値。
     */
    public void setUseTimeoutHintMySQL(final boolean argUseTimeoutHintMySQL) {
        fUseTimeoutHintMySQL = argUseTimeoutHintMySQL;
    }

    /**
     * フィールド [useTimeoutHintMySQL] の値を取得します。
     *
     * フィールドの説明: [MySQL専用。検索型の場合に先頭のSELECT の後ろに  MAX_EXECUTION_TIME オプティマイザ・ヒントが設定されます。デフォルトは60000ms。実行時に変更可能。]。
     * デフォルト: [false]。
     *
     * @return フィールド[useTimeoutHintMySQL]から取得した値。
     */
    public boolean getUseTimeoutHintMySQL() {
        return fUseTimeoutHintMySQL;
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
        buf.append("blanco.db.common.valueobject.BlancoDbSqlInfoStructure[");
        buf.append("name=" + fName);
        buf.append(",package=" + fPackage);
        buf.append(",type=" + fType);
        buf.append(",description=" + fDescription);
        buf.append(",query=" + fQuery);
        buf.append(",inParameterList=" + fInParameterList);
        buf.append(",outParameterList=" + fOutParameterList);
        buf.append(",resultSetColumnList=" + fResultSetColumnList);
        buf.append(",single=" + fSingle);
        buf.append(",scroll=" + fScroll);
        buf.append(",updatable=" + fUpdatable);
        buf.append(",dynamic-sql=" + fDynamicSql);
        buf.append(",use-bean-parameter=" + fUseBeanParameter);
        buf.append(",statementTimeout=" + fStatementTimeout);
        buf.append(",connectionMethod=" + fConnectionMethod);
        buf.append(",connectTo=" + fConnectTo);
        buf.append(",dynamicConditionList=" + fDynamicConditionList);
        buf.append(",dynamicConditionFunctionMap=" + fDynamicConditionFunctionMap);
        buf.append(",useTimeoutHintMySQL=" + fUseTimeoutHintMySQL);
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
    public void copyTo(final BlancoDbSqlInfoStructure target) {
        if (target == null) {
            throw new IllegalArgumentException("Bug: BlancoDbSqlInfoStructure#copyTo(target): argument 'target' is null");
        }

        // No needs to copy parent class.

        // Name: fName
        // Type: java.lang.String
        target.fName = this.fName;
        // Name: fPackage
        // Type: java.lang.String
        target.fPackage = this.fPackage;
        // Name: fType
        // Type: int
        target.fType = this.fType;
        // Name: fDescription
        // Type: java.lang.String
        target.fDescription = this.fDescription;
        // Name: fQuery
        // Type: java.lang.String
        target.fQuery = this.fQuery;
        // Name: fInParameterList
        // Type: java.util.List
        // Field[fInParameterList] is an unsupported type[java.util.Listblanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure].
        // Name: fOutParameterList
        // Type: java.util.List
        // Field[fOutParameterList] is an unsupported type[java.util.Listblanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure].
        // Name: fResultSetColumnList
        // Type: java.util.List
        // Field[fResultSetColumnList] is an unsupported type[java.util.Listblanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure].
        // Name: fSingle
        // Type: boolean
        target.fSingle = this.fSingle;
        // Name: fScroll
        // Type: int
        target.fScroll = this.fScroll;
        // Name: fUpdatable
        // Type: boolean
        target.fUpdatable = this.fUpdatable;
        // Name: fDynamicSql
        // Type: boolean
        target.fDynamicSql = this.fDynamicSql;
        // Name: fUseBeanParameter
        // Type: boolean
        target.fUseBeanParameter = this.fUseBeanParameter;
        // Name: fStatementTimeout
        // Type: int
        target.fStatementTimeout = this.fStatementTimeout;
        // Name: fConnectionMethod
        // Type: java.lang.String
        target.fConnectionMethod = this.fConnectionMethod;
        // Name: fConnectTo
        // Type: java.lang.String
        target.fConnectTo = this.fConnectTo;
        // Name: fDynamicConditionList
        // Type: java.util.List
        // Field[fDynamicConditionList] is an unsupported type[java.util.Listblanco.db.common.valueobject.BlancoDbDynamicConditionStructure].
        // Name: fDynamicConditionFunctionMap
        // Type: java.util.Map
        // Field[fDynamicConditionFunctionMap] is an unsupported type[java.util.Mapjava.lang.String, blanco.db.common.valueobject.BlancoDbDynamicConditionFunctionStructure].
        // Name: fUseTimeoutHintMySQL
        // Type: boolean
        target.fUseTimeoutHintMySQL = this.fUseTimeoutHintMySQL;
    }
}
