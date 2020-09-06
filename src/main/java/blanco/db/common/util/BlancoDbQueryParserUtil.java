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
    private static final String SZ_PARAMETER_FOR_DYNAMIC_CLAUSE_PARAMETER = "\\$\\{[a-zA-Z0-9.\\-_\\P{InBasicLatin}]*\\}\\b|\\$\\{.*\\}$";
    private static final String SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER =
            SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER_ONLY + "|" + SZ_PARAMETER_FOR_DYNAMIC_CLAUSE_PARAMETER;
    /**
     * SQL入力パラメータのマップ <br>
     * TODO マップを利用していますが、これだと順序性が確保されません。
     */
    @SuppressWarnings("unchecked")
    private final Map fMapForSqlInputParameters = new Hashtable();

    /**
     * オリジナルのSQL文字列
     */
    private String fOriginalSqlQueryString = "";

    /**
     * このメソッドが処理対象としているSQL情報の構造体。
     */
    protected BlancoDbSqlInfoStructure fSqlInfo = null;

    @SuppressWarnings("unchecked")
    public BlancoDbQueryParserUtil(final BlancoDbSqlInfoStructure argSqlInfo) {
        // SQL情報の構造体を記憶します。
        this.fSqlInfo = argSqlInfo;
        // パラメータを記憶します。
        fOriginalSqlQueryString = this.fSqlInfo.getQuery();

        // 正規表現文字列インスタンスを生成します。
        // TODO 正規表現による処理において不適切な状況が発生する可能性があります。
        final Matcher matcher = Pattern.compile(
                SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER).matcher(
                fOriginalSqlQueryString);

        for (int index = 1; matcher.find(); index++) {
            int numPlace = 1;
            String name = matcher.group();
            if (name.startsWith("#")) {
                // InParameter の場合は先頭の＃を除去します。
                name = name.substring(1);
            } else {
                // DynamicClause の場合は ${} を取り除きます。
                name = this.stripTagName(name);
                numPlace = this.getMinimumPlaceholders(name);
            }
            if (fMapForSqlInputParameters.containsKey(name) == false) {
                fMapForSqlInputParameters.put(name, new ArrayList());
            }
            for (int n = 0; n < numPlace; n++) {
                if (n > 0) {
                    index++;
                }
                ((List) fMapForSqlInputParameters.get(name))
                        .add(new Integer(index));
            }
        }

        for (Iterator ite = fMapForSqlInputParameters.keySet().iterator(); ite
                .hasNext();) {
            final String key = (String) ite.next();
            final List list = (List) fMapForSqlInputParameters.get(key);
            fMapForSqlInputParameters.put(key, convertListToArray(list));
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
    public int[] getSqlParameters(final String key) {
        // マップからIteratorを作成している点に注意。
        return (int[]) fMapForSqlInputParameters.get(key);
    }

    /**
     * JDBCに実際に発行する際に利用されるナチュラルなSQL文
     *
     * SQL文からメタ情報を取得する際に利用されます。つまり、DotNet版など
     * Java版以外の版のソースコード自動生成の際に、このメソッドが利用されます。
     *
     * @return
     */
    public String getNaturalSqlStringForJava(List<BlancoDbDynamicConditionStructure> dynamicConditionList) {
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
    ) {
        String query = argQuery;
        for (BlancoDbDynamicConditionStructure conditionStructure : argDynamicConditionList) {
            if (conditionStructure.getTag().equals(argTag)) {
                StringBuffer sb = new StringBuffer();
                String condition = conditionStructure.getCondition();
                if ("ITEMONLY".equals(condition)) {
                    sb.append(" ? ");
                } else if ("BETWEEN".equals(condition)) {
                    sb.append(conditionStructure.getLogical() + " ( " + conditionStructure.getItem() + " BETWEEN ? AND ? )");
                } else if ("IN".equals(condition)) {
                    sb.append(conditionStructure.getLogical() + " ( " + conditionStructure.getItem() + " IN ( ? ) ");
                    sb.append(" )");
                } else if ("COMPARE".equals(condition)) {
                    sb.append(conditionStructure.getLogical() + " ( " + conditionStructure.getItem() + " " + conditionStructure.getComparison() + " ? )");
                }
                query = argQuery.replace("${" + argTag + "}", sb.toString());
                break;
            }
        }
        return query;
    }

    private int getMinimumPlaceholders(
            final String argTag
    ) {
        int numPlace = 1;
        BlancoDbDynamicConditionStructure found = this.getConditionStructureByTag(argTag);
        if (found != null && "BETWEEN".equals(found.getCondition())) {
            numPlace = 2;
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
            final int[] listNativeCol = this.getSqlParameters(columnStructure.getName());
            if (listNativeCol == null) {
                throw new IllegalArgumentException("SQL定義ID["
                        + sqlInfo.getName() + "]の SQL入力パラメータ["
                        + columnStructure.getName() + "]が結びついていません.");
            }

            for (int indexSearch = 0; indexSearch < listNativeCol.length; indexSearch++) {
                maxNativeCol = Math.max(listNativeCol[indexSearch],
                        maxNativeCol);
                hashCol.put(String.valueOf(listNativeCol[indexSearch]),
                        columnStructure);
            }
        }

        /*
         * DynamicClause 分
         */
        for (BlancoDbDynamicConditionStructure dynamicClause : sqlInfo.getDynamicConditionList()) {
            final BlancoDbMetaDataColumnStructure columnStructure = dynamicClause.getDbCoumn();

            final int[] listNativeCol = this.getSqlParameters(columnStructure.getName());
            if (listNativeCol == null) {
                throw new IllegalArgumentException("SQL定義ID["
                        + sqlInfo.getName() + "]の SQL入力パラメータ["
                        + columnStructure.getName() + "]が結びついていません.");
            }

            for (int indexSearch = 0; indexSearch < listNativeCol.length; indexSearch++) {
                maxNativeCol = Math.max(listNativeCol[indexSearch],
                        maxNativeCol);
                hashCol.put(String.valueOf(listNativeCol[indexSearch]),
                        columnStructure);
            }
        }

        final List<BlancoDbMetaDataColumnStructure> nativeParam = new ArrayList<BlancoDbMetaDataColumnStructure>();
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

    public String getNaturalSqlStringOnlyStatic(String escapedQuery) {
        /*
         * InParameter 分
         */
        String naturalSql = fOriginalSqlQueryString.replaceAll(
                SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER_ONLY, "?");
        return naturalSql;
    }
}
