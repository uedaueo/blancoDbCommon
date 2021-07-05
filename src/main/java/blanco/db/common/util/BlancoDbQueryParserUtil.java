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

import blanco.db.common.resourcebundle.BlancoDbCommonResourceBundle;
import blanco.db.common.valueobject.BlancoDbDynamicConditionFunctionStructure;
import blanco.db.common.valueobject.BlancoDbDynamicConditionStructure;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * blancoDbが利用するQueryパース・ユーティリティクラス
 *
 * SQLの解釈および変換などを目的とします。
 *
 * @author Tosiki Iga
 */
public class BlancoDbQueryParserUtil {
    /**
     * SQL入力パラメータとして判定するための正規表現文字列。
     */
    private static final String SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER_ONLY = "#[a-zA-Z0-9.\\-_\\P{InBasicLatin}]*\\b|#.*$";
    private static final String SZ_PARAMETER_FOR_DYNAMIC_CLAUSE_PARAMETER = "\\$\\{[a-zA-Z0-9.\\-_\\P{InBasicLatin}]*\\}|\\$\\{.*\\}$";
    private static final String SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER =
            SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER_ONLY + "|" + SZ_PARAMETER_FOR_DYNAMIC_CLAUSE_PARAMETER;
    /**
     * SQL入力パラメータのマップ <br>
     * TODO マップを利用していますが、これだと順序性が確保されません。
     */
    private final Map<String, List<Integer>> fMapForSqlInputParameters = new Hashtable<>();

    /**
     * オリジナルのSQL文字列
     */
    private String fOriginalSqlQueryString = "";

    /**
     * このメソッドが処理対象としているSQL情報の構造体。
     */
    protected BlancoDbSqlInfoStructure fSqlInfo = null;

    /**
     * blancoDbリソースバンドル情報へのアクセサ。
     */
    private final BlancoDbCommonResourceBundle fBundle = new BlancoDbCommonResourceBundle();

    @SuppressWarnings("unchecked")
    public BlancoDbQueryParserUtil(final BlancoDbSqlInfoStructure argSqlInfo) {
        // SQL情報の構造体を記憶します。
        this.fSqlInfo = argSqlInfo;
        // パラメータを記憶します。
        fOriginalSqlQueryString = this.fSqlInfo.getQuery();

//        System.out.println("blancoDbCommon#BlancoDbQueryParserUtil sql = " + fOriginalSqlQueryString);

        // 正規表現文字列インスタンスを生成します。
        // TODO 正規表現による処理において不適切な状況が発生する可能性があります。
        final Matcher matcher = Pattern.compile(
                SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER).matcher(
                fOriginalSqlQueryString);

        for (int index = 1; matcher.find(); index++) {
            int numPlace = 1;
            String name = matcher.group();
//            System.out.println("blancoDbCommon: BlancoDbQueryParserUtil : " + name);
            if (name.startsWith("#")) {
                // InParameter の場合は先頭の＃を除去します。
                name = name.substring(1);
            } else {
                // DynamicClause の場合は ${} を取り除きます。
                name = this.stripTagName(name);
                numPlace = this.getMinimumPlaceholders(name);
            }
            if (!fMapForSqlInputParameters.containsKey(name)) {
                fMapForSqlInputParameters.put(name, new ArrayList<>());
            }
            for (int n = 0; n < numPlace; n++) {
                if (n > 0) {
                    index++;
                }
                fMapForSqlInputParameters.get(name).add(index);
            }
        }

        for (Iterator<String> ite = fMapForSqlInputParameters.keySet().iterator(); ite
                .hasNext();) {
            final String key = ite.next();
            final List<Integer> list = fMapForSqlInputParameters.get(key);
            fMapForSqlInputParameters.put(key, list);
        }
    }

    /**
     * 入力されたSQL入力パラメータをint配列に変換します。
     *
     * @param sqlInputParameterFoundList
     * @return
     */
    @SuppressWarnings("unchecked")
    private int[] convertListToArray(final List sqlInputParameterFoundList) {
        final int[] convertedIntArray = new int[sqlInputParameterFoundList
                .size()];
        final Iterator ite = sqlInputParameterFoundList.iterator();
        for (int index = 0; ite.hasNext(); index++) {
            convertedIntArray[index] = ((Integer) ite.next()).intValue();
        }
        return convertedIntArray;
    }

    /**
     * 試作：キーを元に、intをゲットします。
     *
     * @param key
     * @return
     */
    public List<Integer> getSqlParameters(final String key) {
        // マップからIteratorを作成している点に注意。
        return fMapForSqlInputParameters.get(key);
    }

    /**
     * JDBCに実際に発行する際に利用されるナチュラルなSQL文
     *
     * SQL文からメタ情報を取得する際に利用されます。つまり、DotNet版など
     * Java版以外の版のソースコード自動生成の際に、このメソッドが利用されます。
     *
     * @return
     */
    public String getNaturalSqlStringForJava(List<BlancoDbDynamicConditionStructure> dynamicConditionList) throws IllegalArgumentException {
        /*
         * InParameter 分
         */
        String naturalSql = fOriginalSqlQueryString.replaceAll(
                SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER_ONLY, "?");
        /*
         * DynamicClause 分
         */
        final Matcher matcher = Pattern.compile(
                SZ_PARAMETER_FOR_DYNAMIC_CLAUSE_PARAMETER).matcher(
                naturalSql);
        for (int index = 1; matcher.find(); index++) {
            String name = matcher.group();
            String tag = this.stripTagName(name);
            naturalSql = this.createDynamicClauseNatural(dynamicConditionList, tag, naturalSql);
        }
        System.out.println("blancoDbCommon: getNaturalSqlStringForJava : " + naturalSql);
        return naturalSql;
    }

    /**
     * 動的条件句があるSQLを自然な形に変形します。
     * @param argDynamicConditionList
     * @param argTag
     * @param argQuery
     * @return
     */
    private String createDynamicClauseNatural(
            final List<BlancoDbDynamicConditionStructure> argDynamicConditionList,
            final String argTag,
            final String argQuery
    ) throws IllegalArgumentException {
        String query = argQuery;
        for (BlancoDbDynamicConditionStructure conditionStructure : argDynamicConditionList) {
            if (conditionStructure.getTag().equals(argTag)) {
                StringBuffer sb = new StringBuffer();
                String condition = conditionStructure.getCondition();
                if ("LITERAL".equals(condition)) {
                    sb.append(" " + conditionStructure.getItem() + " ");
                } else if ("FUNCTION".equals(condition)) {
                    BlancoDbDynamicConditionFunctionStructure function = conditionStructure.getFunction();
                    if (function.getDoTest()) {
                        sb.append(" " + function.getFunction() + " ");
                    }
                } else if ("ORDERBY".equals(condition)) {
                    sb.append(" ORDER BY " + conditionStructure.getItem() + " ");
                } else if ("BETWEEN".equals(condition)) {
                    sb.append(conditionStructure.getLogical() + " ( " + conditionStructure.getItem() + " BETWEEN ? AND ? )");
                } else if ("NOT BETWEEN".equals(condition)) {
                    sb.append(conditionStructure.getLogical() + " ( " + conditionStructure.getItem() + " NOT BETWEEN ? AND ? )");
                } else if ("IN".equals(condition)) {
                    sb.append(conditionStructure.getLogical() + " ( " + conditionStructure.getItem() + " IN ( ? ) ");
                    sb.append(" )");
                } else if ("NOT IN".equals(condition)) {
                    sb.append(conditionStructure.getLogical() + " ( " + conditionStructure.getItem() + " NOT IN ( ? ) ");
                    sb.append(" )");
                } else if ("COMPARE".equals(condition)) {
                    sb.append(conditionStructure.getLogical() + " ( " + conditionStructure.getItem() + " " + this.convertComparisonTermToSQL(conditionStructure.getComparison()) + " ? )");
                } else {
                    throw new IllegalArgumentException("[ " + condition + " ]");
                }
                query = argQuery.replace("${" + argTag + "}", sb.toString());
                break;
            }
        }
        return query;
    }

    /**
     * 必要な ? の最小値を取得する。
     * BEWTEEN, NOT BETWEEN の場合は 2。
     * @param argTag
     * @return
     */
    private int getMinimumPlaceholders(
            final String argTag
    ) {
        int numPlace = 1;
        BlancoDbDynamicConditionStructure found = this.getConditionStructureByTag(argTag);
        if (found != null) {
            if ("BETWEEN".equals(found.getCondition()) ||
                    ("NOT BETWEEN".equals(found.getCondition()))) {
                numPlace = 2;
            } else if ("FUNCTION".equals(found.getCondition())) {
                if (found.getFunction().getDoTest()) {
                    numPlace = found.getFunction().getParamNum();
                }
            }
        }
        return numPlace;
    }

    /**
     * DynamicClause タグの ${} を取り除きます。
     * @param name
     * @return
     */
    public String stripTagName(String name) {
        String tag = name.substring(2);
        tag = tag.substring(0, tag.length() - 1);
        return tag;
    }

    /**
     * SQL定義書のSQL入力パラメータを、実際のJDBC上で実行されるSQL文のSQL入力パラメータへと変換します。
     *
     * @param sqlInfo
     * @return
     */
    public List<BlancoDbMetaDataColumnStructure> convertSqlInParameter2NativeParameter(
            final BlancoDbSqlInfoStructure sqlInfo) {
        // 同一名称を実際のSQL上の ? に対応するために複数に展開します。
        int maxNativeCol = 0;
        final Map<String, BlancoDbMetaDataColumnStructure> hashCol = new HashMap<String, BlancoDbMetaDataColumnStructure>();

        /*
         * InParameter 分
         */
        for (int indexCol = 0; indexCol < sqlInfo.getInParameterList().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = sqlInfo
                    .getInParameterList().get(indexCol);
            final List<Integer> listNativeCol = this.getSqlParameters(columnStructure.getName());
            if (listNativeCol == null) {
                throw new IllegalArgumentException("SQL定義ID["
                        + sqlInfo.getName() + "]の SQL入力パラメータ["
                        + columnStructure.getName() + "]が結びついていません.");
            }

            for (int indexSearch = 0; indexSearch < listNativeCol.size(); indexSearch++) {
                maxNativeCol = Math.max(listNativeCol.get(indexSearch),
                        maxNativeCol);
                hashCol.put(String.valueOf(listNativeCol.get(indexSearch)),
                        columnStructure);
            }
        }

//        /*
//         * parserUtil を作成しておく
//         */
//        BlancoDbQueryParserUtil parserUtil = new BlancoDbQueryParserUtil(fSqlInfo);
//
        /*
         * DynamicClause 分
         */
        for (BlancoDbDynamicConditionStructure dynamicClause : sqlInfo.getDynamicConditionList()) {
            final BlancoDbMetaDataColumnStructure columnStructure = dynamicClause.getDbColumn();
            final List<Integer> listNativeCol = this.getSqlParameters(columnStructure.getName());
            if (listNativeCol == null) {
                throw new IllegalArgumentException("SQL定義ID["
                        + sqlInfo.getName() + "]の SQL入力パラメータ["
                        + columnStructure.getName() + "]が結びついていません.");
            }

            if ("FUNCTION".equals(dynamicClause.getCondition())) {
                final BlancoDbDynamicConditionFunctionStructure dynamicFunction = dynamicClause.getFunction();
                if (!dynamicFunction.getDoTest()) {
                    System.out.println(fBundle.getXml2javaclassInfo001(sqlInfo.getName(), dynamicFunction.getTag()));
                    maxNativeCol = Math.max(listNativeCol.get(0),
                            maxNativeCol);
                    hashCol.put(String.valueOf(listNativeCol.get(0)),
                            columnStructure);
                    continue;
                }
                for (int indexSearch = 0; indexSearch < listNativeCol.size(); indexSearch++) {
                    final BlancoDbMetaDataColumnStructure functionColumnStructure = dynamicFunction.getDbColumnList().get(indexSearch);
                    maxNativeCol = Math.max(listNativeCol.get(indexSearch),
                            maxNativeCol);
                    hashCol.put(String.valueOf(listNativeCol.get(indexSearch)),
                            functionColumnStructure);
                }
            } else {
                for (int indexSearch = 0; indexSearch < listNativeCol.size(); indexSearch++) {
                    maxNativeCol = Math.max(listNativeCol.get(indexSearch),
                            maxNativeCol);
                    hashCol.put(String.valueOf(listNativeCol.get(indexSearch)),
                            columnStructure);
                }
            }
        }

        final List<BlancoDbMetaDataColumnStructure> nativeParam = new ArrayList<>();
        for (int indexNativeCol = 1; indexNativeCol <= maxNativeCol; indexNativeCol++) {
            final BlancoDbMetaDataColumnStructure objLook = hashCol.get(String
                    .valueOf(indexNativeCol));
            if (objLook == null) {
                throw new IllegalArgumentException("SQL定義ID["
                        + sqlInfo.getName() + "]の SQL入力パラメータ展開時に予期せぬ例外が発生. ("
                        + indexNativeCol + ")番目の入力パラメータが取得できません。");
            }
            nativeParam.add(objLook);
        }

        return nativeParam;
    }

    /**
     * tag名から動的条件句情報の構造クラスを取得します。
     * @param argTag
     * @return 見つからなければnullを返します。
     */
    public BlancoDbDynamicConditionStructure getConditionStructureByTag(
            final String argTag) {
        BlancoDbDynamicConditionStructure found = null;
        for (BlancoDbDynamicConditionStructure conditionStructure : this.fSqlInfo.getDynamicConditionList()) {
            if (conditionStructure.getTag().equals(argTag)) {
                found = conditionStructure;
            }
        }
        return found;
    }

    /**
     * 静的入力パラメータ分のみを ? に置き換えて自然なSQLを作成します。
     * @param escapedQuery
     * @return
     */
    public String getNaturalSqlStringOnlyStatic(String escapedQuery) {
        /*
         * InParameter 分
         */
        String naturalSql = escapedQuery.replaceAll(
                SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER_ONLY, "?");
        return naturalSql;
    }

    final private Map<String, String> mapComparison = new HashMap<String, String>() {
        {
            put("EQ", "=");
            put("NE", "<>");
            put("GT", ">");
            put("LT", "<");
            put("GE", ">=");
            put("LE", "<=");
            put("LIKE", "LIKE");
            put("NOT LIKE", "NOT LIKE");
        }
    };

    /**
     * 定義書上の比較演算子記号をSQLで使用可能なタイプに変換します。
     * @param term
     * @return
     */
    public String convertComparisonTermToSQL(String term) {
        return this.mapComparison.get(term);
    }
}
