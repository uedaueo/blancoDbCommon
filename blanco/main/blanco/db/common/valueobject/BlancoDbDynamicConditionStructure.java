package blanco.db.common.valueobject;

import java.util.List;

import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * SQL定義書における動的条件式定義に関する情報
 */
public class BlancoDbDynamicConditionStructure {
    /**
     * タグ名
     *
     * フィールド: [tag]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fTag = "";

    /**
     * キー文字列
     *
     * フィールド: [key]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fKey = "";

    /**
     * 条件句タイプ
     *
     * フィールド: [condition]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fCondition = "";

    /**
     * 対象item
     *
     * フィールド: [item]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fItem = "";

    /**
     * 比較演算子
     *
     * フィールド: [comparison]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fComparison = "";

    /**
     * 論理演算子（先導）
     *
     * フィールド: [logical]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fLogical = "";

    /**
     * 値の型
     *
     * フィールド: [type]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fType = "";

    /**
     * 補足説明
     *
     * フィールド: [note]。
     * デフォルト: [new java.util.ArrayList&lt;java.lang.String&gt;()]。
     */
    private List<String> fNote = new java.util.ArrayList<java.lang.String>();

    /**
     * この動的条件句が対象とするitemを表すクラス
     *
     * フィールド: [dbColumn]。
     */
    private BlancoDbMetaDataColumnStructure fDbColumn;

    /**
     * 条件句FUNCTIONの場合の関数情報
     *
     * フィールド: [function]。
     */
    private BlancoDbDynamicConditionFunctionStructure fFunction;

    /**
     * フィールド [tag] の値を設定します。
     *
     * フィールドの説明: [タグ名]。
     *
     * @param argTag フィールド[tag]に設定する値。
     */
    public void setTag(final String argTag) {
        fTag = argTag;
    }

    /**
     * フィールド [tag] の値を取得します。
     *
     * フィールドの説明: [タグ名]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[tag]から取得した値。
     */
    public String getTag() {
        return fTag;
    }

    /**
     * フィールド [key] の値を設定します。
     *
     * フィールドの説明: [キー文字列]。
     *
     * @param argKey フィールド[key]に設定する値。
     */
    public void setKey(final String argKey) {
        fKey = argKey;
    }

    /**
     * フィールド [key] の値を取得します。
     *
     * フィールドの説明: [キー文字列]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[key]から取得した値。
     */
    public String getKey() {
        return fKey;
    }

    /**
     * フィールド [condition] の値を設定します。
     *
     * フィールドの説明: [条件句タイプ]。
     *
     * @param argCondition フィールド[condition]に設定する値。
     */
    public void setCondition(final String argCondition) {
        fCondition = argCondition;
    }

    /**
     * フィールド [condition] の値を取得します。
     *
     * フィールドの説明: [条件句タイプ]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[condition]から取得した値。
     */
    public String getCondition() {
        return fCondition;
    }

    /**
     * フィールド [item] の値を設定します。
     *
     * フィールドの説明: [対象item]。
     *
     * @param argItem フィールド[item]に設定する値。
     */
    public void setItem(final String argItem) {
        fItem = argItem;
    }

    /**
     * フィールド [item] の値を取得します。
     *
     * フィールドの説明: [対象item]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[item]から取得した値。
     */
    public String getItem() {
        return fItem;
    }

    /**
     * フィールド [comparison] の値を設定します。
     *
     * フィールドの説明: [比較演算子]。
     *
     * @param argComparison フィールド[comparison]に設定する値。
     */
    public void setComparison(final String argComparison) {
        fComparison = argComparison;
    }

    /**
     * フィールド [comparison] の値を取得します。
     *
     * フィールドの説明: [比較演算子]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[comparison]から取得した値。
     */
    public String getComparison() {
        return fComparison;
    }

    /**
     * フィールド [logical] の値を設定します。
     *
     * フィールドの説明: [論理演算子（先導）]。
     *
     * @param argLogical フィールド[logical]に設定する値。
     */
    public void setLogical(final String argLogical) {
        fLogical = argLogical;
    }

    /**
     * フィールド [logical] の値を取得します。
     *
     * フィールドの説明: [論理演算子（先導）]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[logical]から取得した値。
     */
    public String getLogical() {
        return fLogical;
    }

    /**
     * フィールド [type] の値を設定します。
     *
     * フィールドの説明: [値の型]。
     *
     * @param argType フィールド[type]に設定する値。
     */
    public void setType(final String argType) {
        fType = argType;
    }

    /**
     * フィールド [type] の値を取得します。
     *
     * フィールドの説明: [値の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[type]から取得した値。
     */
    public String getType() {
        return fType;
    }

    /**
     * フィールド [note] の値を設定します。
     *
     * フィールドの説明: [補足説明]。
     *
     * @param argNote フィールド[note]に設定する値。
     */
    public void setNote(final List<String> argNote) {
        fNote = argNote;
    }

    /**
     * フィールド [note] の値を取得します。
     *
     * フィールドの説明: [補足説明]。
     * デフォルト: [new java.util.ArrayList&lt;java.lang.String&gt;()]。
     *
     * @return フィールド[note]から取得した値。
     */
    public List<String> getNote() {
        return fNote;
    }

    /**
     * フィールド [dbColumn] の値を設定します。
     *
     * フィールドの説明: [この動的条件句が対象とするitemを表すクラス]。
     *
     * @param argDbColumn フィールド[dbColumn]に設定する値。
     */
    public void setDbColumn(final BlancoDbMetaDataColumnStructure argDbColumn) {
        fDbColumn = argDbColumn;
    }

    /**
     * フィールド [dbColumn] の値を取得します。
     *
     * フィールドの説明: [この動的条件句が対象とするitemを表すクラス]。
     *
     * @return フィールド[dbColumn]から取得した値。
     */
    public BlancoDbMetaDataColumnStructure getDbColumn() {
        return fDbColumn;
    }

    /**
     * フィールド [function] の値を設定します。
     *
     * フィールドの説明: [条件句FUNCTIONの場合の関数情報]。
     *
     * @param argFunction フィールド[function]に設定する値。
     */
    public void setFunction(final BlancoDbDynamicConditionFunctionStructure argFunction) {
        fFunction = argFunction;
    }

    /**
     * フィールド [function] の値を取得します。
     *
     * フィールドの説明: [条件句FUNCTIONの場合の関数情報]。
     *
     * @return フィールド[function]から取得した値。
     */
    public BlancoDbDynamicConditionFunctionStructure getFunction() {
        return fFunction;
    }

    /**
     * このバリューオブジェクトの文字列表現を取得します。
     *
     * <P>使用上の注意</P>
     * <UL>
     * <LI>オブジェクトのシャロー範囲のみ文字列化の処理対象となります。
     * <LI>オブジェクトが循環参照している場合には、このメソッドは使わないでください。
     * </UL>
     *
     * @return バリューオブジェクトの文字列表現。
     */
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("blanco.db.common.valueobject.BlancoDbDynamicConditionStructure[");
        buf.append("tag=" + fTag);
        buf.append(",key=" + fKey);
        buf.append(",condition=" + fCondition);
        buf.append(",item=" + fItem);
        buf.append(",comparison=" + fComparison);
        buf.append(",logical=" + fLogical);
        buf.append(",type=" + fType);
        buf.append(",note=" + fNote);
        buf.append(",dbColumn=" + fDbColumn);
        buf.append(",function=" + fFunction);
        buf.append("]");
        return buf.toString();
    }

    /**
     * このバリューオブジェクトを指定のターゲットに複写します。
     *
     * <P>使用上の注意</P>
     * <UL>
     * <LI>オブジェクトのシャロー範囲のみ複写処理対象となります。
     * <LI>オブジェクトが循環参照している場合には、このメソッドは使わないでください。
     * </UL>
     *
     * @param target target value object.
     */
    public void copyTo(final BlancoDbDynamicConditionStructure target) {
        if (target == null) {
            throw new IllegalArgumentException("Bug: BlancoDbDynamicConditionStructure#copyTo(target): argument 'target' is null");
        }

        // No needs to copy parent class.

        // Name: fTag
        // Type: java.lang.String
        target.fTag = this.fTag;
        // Name: fKey
        // Type: java.lang.String
        target.fKey = this.fKey;
        // Name: fCondition
        // Type: java.lang.String
        target.fCondition = this.fCondition;
        // Name: fItem
        // Type: java.lang.String
        target.fItem = this.fItem;
        // Name: fComparison
        // Type: java.lang.String
        target.fComparison = this.fComparison;
        // Name: fLogical
        // Type: java.lang.String
        target.fLogical = this.fLogical;
        // Name: fType
        // Type: java.lang.String
        target.fType = this.fType;
        // Name: fNote
        // Type: java.util.List
        // フィールド[fNote]はサポート外の型[java.util.Listjava.lang.String]です。
        // Name: fDbColumn
        // Type: blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure
        // フィールド[fDbColumn]はサポート外の型[blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure]です。
        // Name: fFunction
        // Type: blanco.db.common.valueobject.BlancoDbDynamicConditionFunctionStructure
        // フィールド[fFunction]はサポート外の型[blanco.db.common.valueobject.BlancoDbDynamicConditionFunctionStructure]です。
    }
}
