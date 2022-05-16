package blanco.db.common.valueobject;

import java.util.List;

import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * SQL定義書における動的条件式関数定義に関する情報
 */
public class BlancoDbDynamicConditionFunctionStructure {
    /**
     * 関数タグ名
     *
     * フィールド: [tag]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fTag = "";

    /**
     * 関数名
     *
     * フィールド: [function]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fFunction = "";

    /**
     * パラメータ数
     *
     * フィールド: [paramNum]。
     * デフォルト: [0]。
     */
    private Integer fParamNum = 0;

    /**
     * 自動生成時にチェックを行う
     *
     * フィールド: [doTest]。
     * デフォルト: [true]。
     */
    private Boolean fDoTest = true;

    /**
     * パラメータ1の型
     *
     * フィールド: [paramType01]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fParamType01 = "";

    /**
     * パラメータ2の型
     *
     * フィールド: [paramType02]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fParamType02 = "";

    /**
     * パラメータ3の型
     *
     * フィールド: [paramType03]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fParamType03 = "";

    /**
     * パラメータ4の型
     *
     * フィールド: [paramType04]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fParamType04 = "";

    /**
     * パラメータ5の型
     *
     * フィールド: [paramType05]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fParamType05 = "";

    /**
     * パラメータ6の型
     *
     * フィールド: [paramType06]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fParamType06 = "";

    /**
     * パラメータ7の型
     *
     * フィールド: [paramType07]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fParamType07 = "";

    /**
     * パラメータ8の型
     *
     * フィールド: [paramType08]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fParamType08 = "";

    /**
     * パラメータ9の型
     *
     * フィールド: [paramType09]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fParamType09 = "";

    /**
     * パラメータ10の型
     *
     * フィールド: [paramType10]。
     * デフォルト: [&quot;&quot;]。
     */
    private String fParamType10 = "";

    /**
     * この関数の引数をtableカラム同様の型として表すクラス
     *
     * フィールド: [dbColumnList]。
     * デフォルト: [new java.util.ArrayList&lt;&gt;()]。
     */
    private List<BlancoDbMetaDataColumnStructure> fDbColumnList = new java.util.ArrayList<>();

    /**
     * フィールド [tag] の値を設定します。
     *
     * フィールドの説明: [関数タグ名]。
     *
     * @param argTag フィールド[tag]に設定する値。
     */
    public void setTag(final String argTag) {
        fTag = argTag;
    }

    /**
     * フィールド [tag] の値を取得します。
     *
     * フィールドの説明: [関数タグ名]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[tag]から取得した値。
     */
    public String getTag() {
        return fTag;
    }

    /**
     * フィールド [function] の値を設定します。
     *
     * フィールドの説明: [関数名]。
     *
     * @param argFunction フィールド[function]に設定する値。
     */
    public void setFunction(final String argFunction) {
        fFunction = argFunction;
    }

    /**
     * フィールド [function] の値を取得します。
     *
     * フィールドの説明: [関数名]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[function]から取得した値。
     */
    public String getFunction() {
        return fFunction;
    }

    /**
     * フィールド [paramNum] の値を設定します。
     *
     * フィールドの説明: [パラメータ数]。
     *
     * @param argParamNum フィールド[paramNum]に設定する値。
     */
    public void setParamNum(final Integer argParamNum) {
        fParamNum = argParamNum;
    }

    /**
     * フィールド [paramNum] の値を取得します。
     *
     * フィールドの説明: [パラメータ数]。
     * デフォルト: [0]。
     *
     * @return フィールド[paramNum]から取得した値。
     */
    public Integer getParamNum() {
        return fParamNum;
    }

    /**
     * フィールド [doTest] の値を設定します。
     *
     * フィールドの説明: [自動生成時にチェックを行う]。
     *
     * @param argDoTest フィールド[doTest]に設定する値。
     */
    public void setDoTest(final Boolean argDoTest) {
        fDoTest = argDoTest;
    }

    /**
     * フィールド [doTest] の値を取得します。
     *
     * フィールドの説明: [自動生成時にチェックを行う]。
     * デフォルト: [true]。
     *
     * @return フィールド[doTest]から取得した値。
     */
    public Boolean getDoTest() {
        return fDoTest;
    }

    /**
     * フィールド [paramType01] の値を設定します。
     *
     * フィールドの説明: [パラメータ1の型]。
     *
     * @param argParamType01 フィールド[paramType01]に設定する値。
     */
    public void setParamType01(final String argParamType01) {
        fParamType01 = argParamType01;
    }

    /**
     * フィールド [paramType01] の値を取得します。
     *
     * フィールドの説明: [パラメータ1の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[paramType01]から取得した値。
     */
    public String getParamType01() {
        return fParamType01;
    }

    /**
     * フィールド [paramType02] の値を設定します。
     *
     * フィールドの説明: [パラメータ2の型]。
     *
     * @param argParamType02 フィールド[paramType02]に設定する値。
     */
    public void setParamType02(final String argParamType02) {
        fParamType02 = argParamType02;
    }

    /**
     * フィールド [paramType02] の値を取得します。
     *
     * フィールドの説明: [パラメータ2の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[paramType02]から取得した値。
     */
    public String getParamType02() {
        return fParamType02;
    }

    /**
     * フィールド [paramType03] の値を設定します。
     *
     * フィールドの説明: [パラメータ3の型]。
     *
     * @param argParamType03 フィールド[paramType03]に設定する値。
     */
    public void setParamType03(final String argParamType03) {
        fParamType03 = argParamType03;
    }

    /**
     * フィールド [paramType03] の値を取得します。
     *
     * フィールドの説明: [パラメータ3の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[paramType03]から取得した値。
     */
    public String getParamType03() {
        return fParamType03;
    }

    /**
     * フィールド [paramType04] の値を設定します。
     *
     * フィールドの説明: [パラメータ4の型]。
     *
     * @param argParamType04 フィールド[paramType04]に設定する値。
     */
    public void setParamType04(final String argParamType04) {
        fParamType04 = argParamType04;
    }

    /**
     * フィールド [paramType04] の値を取得します。
     *
     * フィールドの説明: [パラメータ4の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[paramType04]から取得した値。
     */
    public String getParamType04() {
        return fParamType04;
    }

    /**
     * フィールド [paramType05] の値を設定します。
     *
     * フィールドの説明: [パラメータ5の型]。
     *
     * @param argParamType05 フィールド[paramType05]に設定する値。
     */
    public void setParamType05(final String argParamType05) {
        fParamType05 = argParamType05;
    }

    /**
     * フィールド [paramType05] の値を取得します。
     *
     * フィールドの説明: [パラメータ5の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[paramType05]から取得した値。
     */
    public String getParamType05() {
        return fParamType05;
    }

    /**
     * フィールド [paramType06] の値を設定します。
     *
     * フィールドの説明: [パラメータ6の型]。
     *
     * @param argParamType06 フィールド[paramType06]に設定する値。
     */
    public void setParamType06(final String argParamType06) {
        fParamType06 = argParamType06;
    }

    /**
     * フィールド [paramType06] の値を取得します。
     *
     * フィールドの説明: [パラメータ6の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[paramType06]から取得した値。
     */
    public String getParamType06() {
        return fParamType06;
    }

    /**
     * フィールド [paramType07] の値を設定します。
     *
     * フィールドの説明: [パラメータ7の型]。
     *
     * @param argParamType07 フィールド[paramType07]に設定する値。
     */
    public void setParamType07(final String argParamType07) {
        fParamType07 = argParamType07;
    }

    /**
     * フィールド [paramType07] の値を取得します。
     *
     * フィールドの説明: [パラメータ7の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[paramType07]から取得した値。
     */
    public String getParamType07() {
        return fParamType07;
    }

    /**
     * フィールド [paramType08] の値を設定します。
     *
     * フィールドの説明: [パラメータ8の型]。
     *
     * @param argParamType08 フィールド[paramType08]に設定する値。
     */
    public void setParamType08(final String argParamType08) {
        fParamType08 = argParamType08;
    }

    /**
     * フィールド [paramType08] の値を取得します。
     *
     * フィールドの説明: [パラメータ8の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[paramType08]から取得した値。
     */
    public String getParamType08() {
        return fParamType08;
    }

    /**
     * フィールド [paramType09] の値を設定します。
     *
     * フィールドの説明: [パラメータ9の型]。
     *
     * @param argParamType09 フィールド[paramType09]に設定する値。
     */
    public void setParamType09(final String argParamType09) {
        fParamType09 = argParamType09;
    }

    /**
     * フィールド [paramType09] の値を取得します。
     *
     * フィールドの説明: [パラメータ9の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[paramType09]から取得した値。
     */
    public String getParamType09() {
        return fParamType09;
    }

    /**
     * フィールド [paramType10] の値を設定します。
     *
     * フィールドの説明: [パラメータ10の型]。
     *
     * @param argParamType10 フィールド[paramType10]に設定する値。
     */
    public void setParamType10(final String argParamType10) {
        fParamType10 = argParamType10;
    }

    /**
     * フィールド [paramType10] の値を取得します。
     *
     * フィールドの説明: [パラメータ10の型]。
     * デフォルト: [&quot;&quot;]。
     *
     * @return フィールド[paramType10]から取得した値。
     */
    public String getParamType10() {
        return fParamType10;
    }

    /**
     * フィールド [dbColumnList] の値を設定します。
     *
     * フィールドの説明: [この関数の引数をtableカラム同様の型として表すクラス]。
     *
     * @param argDbColumnList フィールド[dbColumnList]に設定する値。
     */
    public void setDbColumnList(final List<BlancoDbMetaDataColumnStructure> argDbColumnList) {
        fDbColumnList = argDbColumnList;
    }

    /**
     * フィールド [dbColumnList] の値を取得します。
     *
     * フィールドの説明: [この関数の引数をtableカラム同様の型として表すクラス]。
     * デフォルト: [new java.util.ArrayList&lt;&gt;()]。
     *
     * @return フィールド[dbColumnList]から取得した値。
     */
    public List<BlancoDbMetaDataColumnStructure> getDbColumnList() {
        return fDbColumnList;
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
        buf.append("blanco.db.common.valueobject.BlancoDbDynamicConditionFunctionStructure[");
        buf.append("tag=" + fTag);
        buf.append(",function=" + fFunction);
        buf.append(",paramNum=" + fParamNum);
        buf.append(",doTest=" + fDoTest);
        buf.append(",paramType01=" + fParamType01);
        buf.append(",paramType02=" + fParamType02);
        buf.append(",paramType03=" + fParamType03);
        buf.append(",paramType04=" + fParamType04);
        buf.append(",paramType05=" + fParamType05);
        buf.append(",paramType06=" + fParamType06);
        buf.append(",paramType07=" + fParamType07);
        buf.append(",paramType08=" + fParamType08);
        buf.append(",paramType09=" + fParamType09);
        buf.append(",paramType10=" + fParamType10);
        buf.append(",dbColumnList=" + fDbColumnList);
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
    public void copyTo(final BlancoDbDynamicConditionFunctionStructure target) {
        if (target == null) {
            throw new IllegalArgumentException("Bug: BlancoDbDynamicConditionFunctionStructure#copyTo(target): argument 'target' is null");
        }

        // No needs to copy parent class.

        // Name: fTag
        // Type: java.lang.String
        target.fTag = this.fTag;
        // Name: fFunction
        // Type: java.lang.String
        target.fFunction = this.fFunction;
        // Name: fParamNum
        // Type: java.lang.Integer
        target.fParamNum = this.fParamNum;
        // Name: fDoTest
        // Type: java.lang.Boolean
        target.fDoTest = this.fDoTest;
        // Name: fParamType01
        // Type: java.lang.String
        target.fParamType01 = this.fParamType01;
        // Name: fParamType02
        // Type: java.lang.String
        target.fParamType02 = this.fParamType02;
        // Name: fParamType03
        // Type: java.lang.String
        target.fParamType03 = this.fParamType03;
        // Name: fParamType04
        // Type: java.lang.String
        target.fParamType04 = this.fParamType04;
        // Name: fParamType05
        // Type: java.lang.String
        target.fParamType05 = this.fParamType05;
        // Name: fParamType06
        // Type: java.lang.String
        target.fParamType06 = this.fParamType06;
        // Name: fParamType07
        // Type: java.lang.String
        target.fParamType07 = this.fParamType07;
        // Name: fParamType08
        // Type: java.lang.String
        target.fParamType08 = this.fParamType08;
        // Name: fParamType09
        // Type: java.lang.String
        target.fParamType09 = this.fParamType09;
        // Name: fParamType10
        // Type: java.lang.String
        target.fParamType10 = this.fParamType10;
        // Name: fDbColumnList
        // Type: java.util.List
        // Field[fDbColumnList] is an unsupported type[java.util.Listblanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure].
    }
}
