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
 * Query parsing utility class used by blancoDb
 *
 * Interprets and converts SQL.
 *
 * @author Tosiki Iga
 */
public class BlancoDbQueryParserUtil {
    /**
     * Regular expression strings to be determined as SQL input parameter.
     */
    private static final String SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER_ONLY = "#[a-zA-Z0-9.\\-_\\P{InBasicLatin}]*\\b|#.*$";
    private static final String SZ_PARAMETER_FOR_DYNAMIC_CLAUSE_PARAMETER = "\\$\\{[a-zA-Z0-9.\\-_\\P{InBasicLatin}]*\\}|\\$\\{.*\\}$";
    private static final String SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER =
            SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER_ONLY + "|" + SZ_PARAMETER_FOR_DYNAMIC_CLAUSE_PARAMETER;
    /**
     * Map of SQL input parameter <br>
     * TODO: We currently use a map, but this does not ensure order.
     */
    private final Map<String, List<Integer>> fMapForSqlInputParameters = new Hashtable<>();

    /**
     * The original SQL string.
     */
    private String fOriginalSqlQueryString = "";

    /**
     * The structure of the SQL information that is processed by this method.
     */
    protected BlancoDbSqlInfoStructure fSqlInfo = null;

    /**
     * An accessor to blancoDb resource bundle information.
     */
    private final BlancoDbCommonResourceBundle fBundle = new BlancoDbCommonResourceBundle();

    @SuppressWarnings("unchecked")
    public BlancoDbQueryParserUtil(final BlancoDbSqlInfoStructure argSqlInfo) {
        // Stores the SQL information structure.
        this.fSqlInfo = argSqlInfo;
        // Stores the parameters.
        fOriginalSqlQueryString = this.fSqlInfo.getQuery();

//        System.out.println("blancoDbCommon#BlancoDbQueryParserUtil sql = " + fOriginalSqlQueryString);

        // Creates a regular expression string instance.
        // TODO: Inappropriate situations may occur when processing with regular expressions.
        final Matcher matcher = Pattern.compile(
                SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER).matcher(
                fOriginalSqlQueryString);

        for (int index = 1; matcher.find(); index++) {
            int numPlace = 1;
            String name = matcher.group();
//            System.out.println("blancoDbCommon: BlancoDbQueryParserUtil : " + name);
            if (name.startsWith("#")) {
                // In the case of InParameter, removes the leading "#".
                name = name.substring(1);
            } else {
                // In the case of DynamicClause, removes "${}".
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
     * Converts the SQL input parameters to an int array.
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
     * Prototype: Gets the int based on the key.
     *
     * @param key
     * @return
     */
    public List<Integer> getSqlParameters(final String key) {
        // Note that the Iterator is created from the map.
        return fMapForSqlInputParameters.get(key);
    }

    /**
     * Natural SQL statements used to actually publish to the JDBC.
     *
     * Used to obtain meta information from SQL statements. In other words, this method is used for automatic source code generation for DotNet and other non-Java versions.
     *
     * @return
     */
    public String getNaturalSqlStringForJava(List<BlancoDbDynamicConditionStructure> dynamicConditionList) throws IllegalArgumentException {
        /*
         * For InParameter
         */
        String naturalSql = fOriginalSqlQueryString.replaceAll(
                SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER_ONLY, "?");
        /*
         * For DynamicClause
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
     * Transforms SQL with dynamic conditional clauses into a natural form.
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
     * Gets the minimum value of the required "?".
     * 2 for BEWTEEN and NOT BETWEEN.
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
     * Removes "${}" from the DynamicClause tag.
     * @param name
     * @return
     */
    public String stripTagName(String name) {
        String tag = name.substring(2);
        tag = tag.substring(0, tag.length() - 1);
        return tag;
    }

    /**
     * Converts SQL input parameters of SQL definition into ones of SQL statements execused on the actual JDBC.
     *
     * @param sqlInfo
     * @return
     */
    public List<BlancoDbMetaDataColumnStructure> convertSqlInParameter2NativeParameter(
            final BlancoDbSqlInfoStructure sqlInfo) {
        // Expands the same names into multiple names to correspond to "?" in the actual SQL.
        int maxNativeCol = 0;
        final Map<String, BlancoDbMetaDataColumnStructure> hashCol = new HashMap<String, BlancoDbMetaDataColumnStructure>();

        /*
         * For InParameter
         */
        for (int indexCol = 0; indexCol < sqlInfo.getInParameterList().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = sqlInfo
                    .getInParameterList().get(indexCol);
            final List<Integer> listNativeCol = this.getSqlParameters(columnStructure.getName());
            if (listNativeCol == null) {
                throw new IllegalArgumentException("SQL input parameter ["
                        + columnStructure.getName() + "] of SQL definition ID ["
                        + sqlInfo.getName() + "] is not connected.");
            }

            for (int indexSearch = 0; indexSearch < listNativeCol.size(); indexSearch++) {
                maxNativeCol = Math.max(listNativeCol.get(indexSearch),
                        maxNativeCol);
                hashCol.put(String.valueOf(listNativeCol.get(indexSearch)),
                        columnStructure);
            }
        }

//        /*
//         * Creates a parserUtil.
//         */
//        BlancoDbQueryParserUtil parserUtil = new BlancoDbQueryParserUtil(fSqlInfo);
//
        /*
         * For DynamicClause
         */
        for (BlancoDbDynamicConditionStructure dynamicClause : sqlInfo.getDynamicConditionList()) {
            final BlancoDbMetaDataColumnStructure columnStructure = dynamicClause.getDbColumn();
            final List<Integer> listNativeCol = this.getSqlParameters(columnStructure.getName());
            if (listNativeCol == null) {
                throw new IllegalArgumentException("SQL input parameter ["
                        + columnStructure.getName() + "] of SQL definition ID ["
                        + sqlInfo.getName() + "] is not connected.");
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
                throw new IllegalArgumentException("Unexpected exception occurred during SQL input parameter expansion for SQL definition ID ["
                        + sqlInfo.getName() + "]. Unables to get the ("
                        + indexNativeCol + ")th input parameter.");
            }
            nativeParam.add(objLook);
        }

        return nativeParam;
    }

    /**
     * Gets the structure class of the dynamic conditional clause information from the tag name.
     * @param argTag
     * @return Returns null if not found.
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
     * Replaces only the static input parameters with "?" to create a natural SQL.
     * @param escapedQuery
     * @return
     */
    public String getNaturalSqlStringOnlyStatic(String escapedQuery) {
        /*
         * For InParameter
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
     * Converts comparison operator symbols in the definition to types that can be used in SQL.
     * @param term
     * @return
     */
    public String convertComparisonTermToSQL(String term) {
        return this.mapComparison.get(term);
    }
}
