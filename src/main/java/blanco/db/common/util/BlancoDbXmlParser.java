/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.common.util;

import blanco.commons.util.BlancoNameAdjuster;
import blanco.commons.util.BlancoStringUtil;
import blanco.commons.util.BlancoXmlUtil;
import blanco.db.common.resourcebundle.BlancoDbCommonResourceBundle;
import blanco.db.common.stringgroup.BlancoDbSqlInfoScrollStringGroup;
import blanco.db.common.stringgroup.BlancoDbSqlInfoTypeStringGroup;
import blanco.db.common.valueobject.BlancoDbDynamicConditionFunctionStructure;
import blanco.db.common.valueobject.BlancoDbDynamicConditionStructure;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.dbmetadata.BlancoDbMetaDataUtil;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the intermediate XML of the SQL definition document and turns it into a Java object.
 *
 * @author IGA Tosiki
 */
public class BlancoDbXmlParser {
	/**
	 * How to deal with structual problems in the behavior of SQL input parameters in C#.NET.
	 *
	 * FIXME: Externalize this variable eventually.
	 */
	private static final boolean IS_FORCE_CS_DOTNET_STRING_AS_NVARCHAR = false;

	/**
     * An accessor to blancoDb resource bundle information.
     */
    private final BlancoDbCommonResourceBundle fBundle = new BlancoDbCommonResourceBundle();

    /**
     * The element name in which common information is stored.
     */
    private static final String ELEMENT_COMMON = "blancodb-common";

    /**
     * The element name in which SQL input parameter is stored.
     */
    private static final String ELEMENT_INPARAMETERS = "blancodb-inparameters";

    /**
     * The element name in which SQL dynamic condition definition is stored.
     */
    private static final String ELEMENT_DYNAMICCONDITIONS = "blancodb-dynamicconditions";

    /**
     * The element name in which SQL dynamic condition function definition is stored.
     */
    private static final String ELEMENT_DYNAMICFUNCTIONS = "blancodb-dynamicfunctions";

    /**
     * The element name in which SQL output parameter is stored.
     */
    private static final String ELEMENT_OUTPARAMETERS = "blancodb-outparameters";

    /**
     * The element name in which SQL statement is stored.
     */
    private static final String ELEMENT_QUERY = "blancodb-query";

    /**
     * Collects SQL definition document information using the intermediate XML of the SQL definition document as input.
     *
     * @param fileSqlForm
     *            An XML intermediate file of the SQL definition you want to process.
     * @return A list of SQL definitions after parsing.
     * @throws SQLException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public List<BlancoDbSqlInfoStructure> parse(final File fileSqlForm)
            throws SQLException, SAXException, IOException,
            ParserConfigurationException, TransformerException {
        // blancoDb definition information.
        final List<BlancoDbSqlInfoStructure> resultBlancoDbDef = new ArrayList<BlancoDbSqlInfoStructure>();

        // Parses an XML file as a DOM.
        InputStream inStream = null;
        final DOMResult result;
        try {
            inStream = new BufferedInputStream(new FileInputStream(fileSqlForm));
            result = BlancoXmlUtil.transformStream2Dom(inStream);
        } finally {
            inStream.close();
        }

        // Gets the root node.
        final Node nodeRootNode = result.getNode();
        if (nodeRootNode == null) {
            // It is not an XML file.
            return resultBlancoDbDef;
        }

        final Element eleWorkbook = BlancoXmlUtil.getElement(nodeRootNode,
                "workbook");
        if (eleWorkbook == null) {
            return resultBlancoDbDef;
        }

        // Expands the sheet element.
        final NodeList listSheet = eleWorkbook.getElementsByTagName("sheet");
        if (listSheet == null) {
            // Cannot find the sheet.
            return resultBlancoDbDef;
        }

        // Performs the processes for each sheet element.
        final int nodeLength = listSheet.getLength();
        for (int index = 0; index < nodeLength; index++) {
            final Node nodeSheet = listSheet.item(index);
            if (nodeSheet instanceof Element == false) {
                continue;
            }
            final Element eleSheet = (Element) nodeSheet;
            expandSheet(eleSheet, resultBlancoDbDef);
        }

        return resultBlancoDbDef;
    }

    /**
     * Expands the given sheet element.
     *
     * @param eleSheet
     *            A sheet element.
     * @param resultBlancoDbDef
     *            blancoDb definition information.
     */
    private void expandSheet(final Element eleSheet,
            final List<BlancoDbSqlInfoStructure> resultBlancoDbDef) {
        // Expands the common information at first.
        final BlancoDbSqlInfoStructure fSqlInfo = expandCommon(eleSheet);
        if (fSqlInfo == null) {
            // This is not appropriate for a SQL definition document, so skips the process.
            return;
        }

        // Expands the SQL input parameter.
        expandInParameter(eleSheet, fSqlInfo);

        // Expands the SQL dynamic condition function definition.
        expandDynamicConditionFunction(eleSheet, fSqlInfo);

        // Expands the SQL dynamic condition definition.
        expandDynamicCondition(eleSheet, fSqlInfo);

        // Expands the SQL output parameter.
        expandOutParameter(eleSheet, fSqlInfo);

        // Expands the SQL statement.
        expandQuery(eleSheet, fSqlInfo);

        // Once with all the information in place, checks the validity of the collected information.
        if (fSqlInfo.getQuery() == null || fSqlInfo.getQuery().length() == 0) {
            // If the SQL statement cannot be obtained, it will be treated as an error.
            throw new IllegalArgumentException(fBundle
                    .getXml2javaclassErr001(fSqlInfo.getName()));
        }

        // Adds information to the list of SQL definitions after parsing.
        resultBlancoDbDef.add(fSqlInfo);
    }

    /**
     * Analyzes the given common elements and expands the information.
     *
     * @param eleSheet
     *            A sheet object.
     * @return An abstract query object.
     */
    private BlancoDbSqlInfoStructure expandCommon(final Element eleSheet) {
        final Element elementCommon = BlancoXmlUtil.getElement(eleSheet,
                ELEMENT_COMMON);
        if (elementCommon == null) {
            // If ELEMENT_COMMON is not found, skips this sheet.
            return null;
        }

        final BlancoDbSqlInfoStructure fSqlInfo = new BlancoDbSqlInfoStructure();

        fSqlInfo.setName(BlancoStringUtil.null2Blank(BlancoXmlUtil
                .getTextContent(elementCommon, "name")));
        if (fSqlInfo.getName().length() == 0) {
            // This definition does not need to be processed. It should not be processed.
            // In the past, the process was skipped even when query-type was unspecified, but now it is treated as an error.
            // name is not found.
            return null;
        }

        fSqlInfo.setPackage(BlancoXmlUtil.getTextContent(elementCommon,
                "package"));

        final String queryType = BlancoStringUtil.null2Blank(BlancoXmlUtil
                .getTextContent(elementCommon, "query-type"));
        fSqlInfo.setType(new BlancoDbSqlInfoTypeStringGroup()
                .convertToInt(queryType));
        if (fSqlInfo.getType() == BlancoDbSqlInfoTypeStringGroup.NOT_DEFINED) {
            // Cannot process. Aborts it.
            throw new IllegalArgumentException("Unsupported query type ["
                    + fSqlInfo.getType() + "] was given. Aborts the process.");
        }

        fSqlInfo.setDescription(BlancoStringUtil.null2Blank(BlancoXmlUtil
                .getTextContent(elementCommon, "description")));
        fSqlInfo.setSingle(BlancoStringUtil.null2Blank(
                BlancoXmlUtil.getTextContent(elementCommon, "single")).equals(
                "true"));

        fSqlInfo.setConnectionMethod(BlancoStringUtil.null2Blank(
                BlancoXmlUtil.getTextContent(elementCommon, "connectionMethod")));

        fSqlInfo.setConnectTo(BlancoStringUtil.null2Blank(
                BlancoXmlUtil.getTextContent(elementCommon, "connectTo")));

        if (fSqlInfo.getType() == BlancoDbSqlInfoTypeStringGroup.ITERATOR) {
            // Loads scrolling and updatable attributes only for search type.
            fSqlInfo.setScroll(new BlancoDbSqlInfoScrollStringGroup()
                    .convertToInt(BlancoStringUtil.null2Blank(BlancoXmlUtil
                            .getTextContent(elementCommon, "scroll"))));
            fSqlInfo.setUpdatable(BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(elementCommon, "updatable"))
                    .equals("true"));
        } else {
            fSqlInfo.setScroll(BlancoDbSqlInfoScrollStringGroup.NOT_DEFINED);
            fSqlInfo.setUpdatable(false);
        }

        fSqlInfo.setDynamicSql(BlancoStringUtil.null2Blank(
                BlancoXmlUtil.getTextContent(elementCommon, "dynamic-sql"))
                .equals("true"));
        fSqlInfo.setUseBeanParameter(BlancoStringUtil.null2Blank(
                BlancoXmlUtil.getTextContent(elementCommon,
                        "use-bean-parameter")).equals("true"));
        fSqlInfo.setUseTimeoutHintMySQL(BlancoStringUtil.null2Blank(
                        BlancoXmlUtil.getTextContent(elementCommon, "useTimeoutHintMySQL"))
                .equals("true"));
        if (fSqlInfo.getUseTimeoutHintMySQL() &&
                fSqlInfo.getType() != BlancoDbSqlInfoTypeStringGroup.ITERATOR) {
            throw new IllegalArgumentException(fBundle.getXml2javaclassErr024(fSqlInfo.getName()));
        }

        {
            // Statement timeout
            final String statementTimeout = BlancoXmlUtil.getTextContent(
                    elementCommon, "statementTimeout");
            if (BlancoStringUtil.null2Blank(statementTimeout).length() > 0) {
                try {
                    fSqlInfo.setStatementTimeout(Integer
                            .parseInt(statementTimeout));
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException(fBundle
                            .getXml2javaclassErr009(fSqlInfo.getName(),
                                    statementTimeout));
                }
            }
        }

        return fSqlInfo;
    }

    /**
     * Analyzes the given sheet and expands the SQL input parameter definition information.
     *
     * @param elementSheet
     *            A sheet object.
     * @param sqlInfo
     *            An abstract query object.
     */
    private void expandInParameter(final Element elementSheet,
            final BlancoDbSqlInfoStructure sqlInfo) {
        final Element elementBlancoDbInparameters = BlancoXmlUtil.getElement(
                elementSheet, ELEMENT_INPARAMETERS);
        if (elementBlancoDbInparameters == null) {
            return;
        }

        final NodeList nodeList = elementBlancoDbInparameters
                .getElementsByTagName("inparameter");
        if (nodeList == null) {
            // There is no SQL input parameter.
            return;
        }
        final int nodeLength = nodeList.getLength();
        for (int index = 0; index < nodeLength; index++) {
            final Node nodeLook = nodeList.item(index);
            if (nodeLook instanceof Element == false) {
                continue;
            }
            final String no = BlancoXmlUtil.getTextContent((Element) nodeLook,
                    "no");
            final String name = BlancoXmlUtil.getTextContent(
                    (Element) nodeLook, "name");
            final String type = BlancoXmlUtil.getTextContent(
                    (Element) nodeLook, "type");
            final String nullable = BlancoStringUtil.null2Blank(BlancoXmlUtil
                    .getTextContent((Element) nodeLook, "nullable"));

            // Currently, there is no place to store description.
            // final String description = BlancoXmlUtil.getTextContent(
            // (Element) nodeLook, "description");

            final String paramNoString = (no == null ? "" : " No.[" + no + "] ");
            if (name == null || name.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr004(sqlInfo.getName(),
                                paramNoString, type));
            }
            if (type == null || type.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr005(sqlInfo.getName(),
                                paramNoString, name));
            }

            final BlancoDbMetaDataColumnStructure columnStructure = new BlancoDbMetaDataColumnStructure();
            columnStructure.setName(name);

            if (nullable.equals("Nullable")) {
                columnStructure.setNullable(ResultSetMetaData.columnNullable);
            } else if (nullable.equals("NoNulls")) {
                columnStructure.setNullable(ResultSetMetaData.columnNoNulls);
            }

            // It will be loaded assuming that it is a new-style data type at first.
            columnStructure.setDataType(BlancoDbMetaDataUtil
                    .convertJdbcDataType2Int(type));
            if (columnStructure.getDataType() == Integer.MIN_VALUE) {
                // If the data type does not match the new-style, it will be loaded as the old-style.
                // In this section, it will derive dataType and nullable from Java types.
                convertOldSqlInputTypeToJdbc(type, columnStructure);
            }

            sqlInfo.getInParameterList().add(columnStructure);
        }
    }

    /**
     * Analyzes the given sheet and expands the SQL dynamic condition definition information.
     *
     * @param elementSheet
     *            A sheet object.
     * @param sqlInfo
     *            An abstract query object.
     */
    private void expandDynamicCondition(final Element elementSheet,
                                   final BlancoDbSqlInfoStructure sqlInfo) {
        final Element elementBlancoDbDynamicConditions = BlancoXmlUtil.getElement(
                elementSheet, ELEMENT_DYNAMICCONDITIONS);
        if (elementBlancoDbDynamicConditions == null) {
            return;
        }

        final NodeList nodeList = elementBlancoDbDynamicConditions
                .getElementsByTagName("dynamicconditions");
        if (nodeList == null) {
            // There is no SQL input parameter.
            return;
        }
        final int nodeLength = nodeList.getLength();
        for (int index = 0; index < nodeLength; index++) {
            final Node nodeLook = nodeList.item(index);
            if (nodeLook instanceof Element == false) {
                continue;
            }

            final BlancoDbDynamicConditionStructure dynamicCondition = new BlancoDbDynamicConditionStructure();

            String tag = BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(
                            (Element) nodeLook, "tag"));
            if (tag.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr010(sqlInfo.getName()));
            }
            dynamicCondition.setTag(tag);

            String key = BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(
                            (Element) nodeLook, "key"));
            if (key.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr011(sqlInfo.getName(), tag));
            }
            dynamicCondition.setKey(key);

            String condition = BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(
                            (Element) nodeLook, "condition")
            );
            if (condition.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr012(sqlInfo.getName(), tag));
            }
            dynamicCondition.setCondition(condition);

            String targetItem = BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(
                            (Element) nodeLook, "item")
            );
            if (targetItem.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr013(sqlInfo.getName(), condition));
            }
            dynamicCondition.setItem(targetItem);

            /* If the conditional clause type is function. */
            if ("FUNCTION".equals(condition)) {
                BlancoDbDynamicConditionFunctionStructure function = sqlInfo.getDynamicConditionFunctionMap().get(targetItem);
                if (function == null) {
                    throw new IllegalArgumentException(fBundle.getXml2javaclassErr023(sqlInfo.getName(), targetItem));
                }
                for (BlancoDbMetaDataColumnStructure columnStructure : function.getDbColumnList()) {
                    columnStructure.setName(dynamicCondition.getTag());
                }
                dynamicCondition.setFunction(function);
            }

            String comparison = BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(
                            (Element) nodeLook, "comparison")
            );
            if ("COMPARE".equals(condition) && comparison.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr014(sqlInfo.getName(), condition));
            }
            dynamicCondition.setComparison(comparison);

            String logical = BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(
                            (Element) nodeLook, "logical")
            );
            /*
             * Unchecks so that DynamicClause can also be placed immediately after WHERE.
             * If you forget to specify the logical operator in the definition during the process, cannot check for errors until runtime in the case of update type.
             */
//            if (!"ORDERBY".equals(condition) && logical.length() == 0) {
//                throw new IllegalArgumentException(fBundle
//                        .getXml2javaclassErr015(sqlInfo.getName(), condition));
//            }
            dynamicCondition.setLogical(logical);

            String type = BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(
                            (Element) nodeLook, "type")
            );
            if (!"ORDERBY".equals(condition) && !"LITERAL".equals(condition) && !"FUNCTION".equals(condition) && type.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr016(sqlInfo.getName(), condition));
            }
            dynamicCondition.setType(type);

            sqlInfo.getDynamicConditionList().add(dynamicCondition);

            /*
             * Creates information that this dynamic conditional clause targets as a dbColumn.
             */
            BlancoDbMetaDataColumnStructure columnStructure = new BlancoDbMetaDataColumnStructure();
            dynamicCondition.setDbColumn(columnStructure);
            columnStructure.setName(tag);
            if ("ORDERBY".equals(condition) || "LITERAL".equals(condition)) {
                /* No parameter */
                columnStructure.setTypeName(null);
                columnStructure.setDataType(Types.OTHER);
            } else if ("FUNCTION".equals(condition)) {
                columnStructure.setTypeName(null);
                columnStructure.setDataType(Types.OTHER);
            } else {
                columnStructure.setTypeName(dynamicCondition.getType());
                columnStructure.setDataType(BlancoDbMetaDataUtil
                        .convertJdbcDataType2Int(dynamicCondition.getType()));
                if (columnStructure.getDataType() == Integer.MIN_VALUE) {
                    // If the data type does not match the new-style, it will be loaded as the old-style.
                    // In this section, it will derive dataType and nullable from Java types.
                    convertOldSqlInputTypeToJdbc(type, columnStructure);
                }
            }
            System.out.println("condition = " + condition + ", type = " + type + ", dataType = " + BlancoDbMetaDataUtil.convertJdbcDataTypeToString(columnStructure.getDataType()));
        }
    }

    /**
     * Analyzes the given sheet and expands the SQL dynamic condition function definition information.
     *
     * @param elementSheet
     *            A sheet object.
     * @param sqlInfo
     *            An abstract query object.
     */
    private void expandDynamicConditionFunction(final Element elementSheet,
                                        final BlancoDbSqlInfoStructure sqlInfo) {
        final Element elementBlancoDbDynamicConditionFunctions = BlancoXmlUtil.getElement(
                elementSheet, ELEMENT_DYNAMICFUNCTIONS);
        if (elementBlancoDbDynamicConditionFunctions == null) {
            return;
        }

        final NodeList nodeList = elementBlancoDbDynamicConditionFunctions
                .getElementsByTagName("dynamicfunctions");
        if (nodeList == null) {
            // There is no SQL input parameter.
            return;
        }
        final int nodeLength = nodeList.getLength();
        for (int index = 0; index < nodeLength; index++) {
            final Node nodeLook = nodeList.item(index);
            if (nodeLook instanceof Element == false) {
                continue;
            }

            final BlancoDbDynamicConditionFunctionStructure dynamicFunction = new BlancoDbDynamicConditionFunctionStructure();

            String tag = BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(
                            (Element) nodeLook, "tag"));
            if (tag.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr018(sqlInfo.getName()));
            }
            dynamicFunction.setTag(tag);

            String function = BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(
                            (Element) nodeLook, "function"));
            if (function.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr019(sqlInfo.getName(), tag));
            }
            dynamicFunction.setFunction(function);

            String strParamNum = BlancoStringUtil.null2Blank(
                    BlancoXmlUtil.getTextContent(
                            (Element) nodeLook, "paramNum")
            );
            if (strParamNum.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr020(sqlInfo.getName(), tag));
            }
            int paramNum = 0;
            try {
                paramNum = Integer.parseInt(strParamNum);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(fBundle.getXml2javaclassErr021(sqlInfo.getName(), tag), e);
            }
            dynamicFunction.setParamNum(paramNum);

            dynamicFunction.setDoTest(
                    "true".equals(BlancoStringUtil.null2Blank(
                            BlancoXmlUtil.getTextContent(
                                    (Element) nodeLook, "doTest")))
            );

            Class<? extends BlancoDbDynamicConditionFunctionStructure> clazz = dynamicFunction.getClass();
            for (int i = 1; i <= paramNum; i++) {
                String tagParamType = String.format("paramType%02d", i);

                String strParamType = BlancoStringUtil.null2Blank(
                        BlancoXmlUtil.getTextContent(
                                (Element) nodeLook, tagParamType)
                );
                if (strParamType.length() == 0) {
                    throw new IllegalArgumentException(fBundle
                            .getXml2javaclassErr022(sqlInfo.getName(), tag, String.format("%02d", i)));
                }
                String strMethod = "set" + BlancoNameAdjuster.toClassName(tagParamType);
                try {
                    Method method = clazz.getMethod(strMethod, String.class);
                    method.invoke(dynamicFunction, strParamType);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new IllegalArgumentException(fBundle
                            .getXml2javaclassErr022(sqlInfo.getName(), tag, String.format("%02d", i)), e);
                }
                /* Make BlancoDbMetaDataColumnStructure for Function Parameters */
                BlancoDbMetaDataColumnStructure columnStructure = new BlancoDbMetaDataColumnStructure();
                columnStructure.setTypeName(strParamType);
                columnStructure.setDataType(BlancoDbMetaDataUtil
                        .convertJdbcDataType2Int(strParamType));
                if (columnStructure.getDataType() == Integer.MIN_VALUE) {
                    // If the data type does not match the new-style, it will be loaded as the old-style.
                    // In this section, it will derive dataType and nullable from Java types.
                    convertOldSqlInputTypeToJdbc(strParamType, columnStructure);
                }
                dynamicFunction.getDbColumnList().add(columnStructure);
            }

            /* Stores in sqlInfo. */
            sqlInfo.getDynamicConditionFunctionMap().put(dynamicFunction.getTag(), dynamicFunction);
        }
    }

    /**
     * Analyzes the given sheet and expands the SQL output parameter information.
     *
     * @param elementSheet
     *            A sheet object.
     * @param sqlInfo
     *            An abstract query object.
     */
    private void expandOutParameter(final Element elementSheet,
            final BlancoDbSqlInfoStructure sqlInfo) {
        final Element elementBlancoDbOutparameters = BlancoXmlUtil.getElement(
                elementSheet, ELEMENT_OUTPARAMETERS);
        if (elementBlancoDbOutparameters == null) {
            // There is no SQL output parameter.
            return;
        }

        final NodeList nodeList = elementBlancoDbOutparameters
                .getElementsByTagName("outparameter");
        if (nodeList == null) {
            return;
        }
        final int nodeLength = nodeList.getLength();
        for (int index = 0; index < nodeLength; index++) {
            final Node nodeLook = nodeList.item(index);
            if (nodeLook instanceof Element == false) {
                continue;
            }

            final String no = BlancoXmlUtil.getTextContent((Element) nodeLook,
                    "no");
            final String name = BlancoXmlUtil.getTextContent(
                    (Element) nodeLook, "name");
            final String type = BlancoXmlUtil.getTextContent(
                    (Element) nodeLook, "type");
            final String nullable = BlancoStringUtil.null2Blank(BlancoXmlUtil
                    .getTextContent((Element) nodeLook, "nullable"));

            // Currently, there is no place to store description.

            final String paramNoString = (no == null ? "" : " No.[" + no + "] ");
            if (name == null || name.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr006(sqlInfo.getName(),
                                paramNoString, type));
            }
            if (type == null || type.length() == 0) {
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr007(sqlInfo.getName(),
                                paramNoString, name));
            }
            if (sqlInfo.getType() != BlancoDbSqlInfoTypeStringGroup.CALLER) {
                // If it is not a caller, cannot set the SQL output parameter.
                throw new IllegalArgumentException(fBundle
                        .getXml2javaclassErr008(sqlInfo.getName(),
                                paramNoString, name));
            }

            final BlancoDbMetaDataColumnStructure columnStructure = new BlancoDbMetaDataColumnStructure();
            columnStructure.setName(name);

            if (nullable.equals("Nullable")) {
                columnStructure.setNullable(ResultSetMetaData.columnNullable);
            } else if (nullable.equals("NoNulls")) {
                columnStructure.setNullable(ResultSetMetaData.columnNoNulls);
            }

            // It will be loaded assuming that it is a new-style data type at first.
            columnStructure.setDataType(BlancoDbMetaDataUtil
                    .convertJdbcDataType2Int(type));
            if (columnStructure.getDataType() == Integer.MIN_VALUE) {
                // If the data type does not match the new-style, it will be loaded as the old-style.
                // In this section, it will derive dataType and nullable from Java types.
                convertOldSqlInputTypeToJdbc(type, columnStructure);
            }

            sqlInfo.getOutParameterList().add(columnStructure);
        }
    }

    /**
     * Analyzes the given sheet and expands the information about the SQL statement.
     *
     * @param elementSheet
     *            A sheet object.
     * @param sqlInfo
     *            An abstract query object.
     */
    private void expandQuery(final Element elementSheet,
            final BlancoDbSqlInfoStructure sqlInfo) {
        final Element elementBlancoDbInparameters = BlancoXmlUtil.getElement(
                elementSheet, ELEMENT_QUERY);
        if (elementBlancoDbInparameters == null) {
            return;
        }

        final NodeList nodeList = elementBlancoDbInparameters
                .getElementsByTagName("query-line");
        if (nodeList == null) {
            return;
        }
        final int nodeLength = nodeList.getLength();
        for (int index = 0; index < nodeLength; index++) {
            final String queryLine = BlancoStringUtil.null2Blank(BlancoXmlUtil
                    .getTextContent(nodeList.item(index)));

            String query = sqlInfo.getQuery();
            if (query == null || query.length() == 0) {
                query = "";
            } else {
                // Adds a newline only if the string already exists.
                query = query + "\n";
            }
            sqlInfo.setQuery(query + queryLine);
        }
    }

    /**
     * In older versions of SQL definitions, Java/C#.NET type names are given for SQL input/output parameter type names. This is a routine to replace this type name with the type name of java.sql.Types.
     *
     * This method exists for the purpose of processing the loading of definitions from previous versions.
     *
     * TODO: There are descriptions that depend on the Java and C#.NET.
     *
     * @param type
     * @param columnStructure
     */
    protected void convertOldSqlInputTypeToJdbc(final String type,
            final BlancoDbMetaDataColumnStructure columnStructure) {
        if (type.equals("boolean")) {
            columnStructure.setDataType(Types.BIT);
            columnStructure.setNullable(ResultSetMetaData.columnNoNulls);
        } else if (type.equals("java.lang.Boolean")) {
            columnStructure.setDataType(Types.BIT);
        } else if (type.equals("byte")) {
            columnStructure.setDataType(Types.TINYINT);
            columnStructure.setNullable(ResultSetMetaData.columnNoNulls);
        } else if (type.equals("java.lang.Byte")) {
            columnStructure.setDataType(Types.TINYINT);
        } else if (type.equals("short")) {
            columnStructure.setDataType(Types.SMALLINT);
            columnStructure.setNullable(ResultSetMetaData.columnNoNulls);
        } else if (type.equals("java.lang.Short")) {
            columnStructure.setDataType(Types.SMALLINT);
        } else if (type.equals("int")) {
            columnStructure.setDataType(Types.INTEGER);
            columnStructure.setNullable(ResultSetMetaData.columnNoNulls);
        } else if (type.equals("java.lang.Integer")) {
            columnStructure.setDataType(Types.INTEGER);
        } else if (type.equals("long")) {
            columnStructure.setDataType(Types.BIGINT);
            columnStructure.setNullable(ResultSetMetaData.columnNoNulls);
        } else if (type.equals("java.lang.Long")) {
            columnStructure.setDataType(Types.BIGINT);
        } else if (type.equals("float")) {
            columnStructure.setDataType(Types.REAL);
            columnStructure.setNullable(ResultSetMetaData.columnNoNulls);
        } else if (type.equals("java.lang.Float")) {
            columnStructure.setDataType(Types.REAL);
        } else if (type.equals("double")) {
            columnStructure.setDataType(Types.FLOAT);
            columnStructure.setNullable(ResultSetMetaData.columnNoNulls);
        } else if (type.equals("java.lang.Double")) {
            columnStructure.setDataType(Types.FLOAT);
        } else if (type.equals("java.math.BigDecimal")) {
            columnStructure.setDataType(Types.DECIMAL);
        } else if (type.equals("java.lang.String")) {
            columnStructure.setDataType(Types.VARCHAR);
        } else if (type.equals("java.util.Date")) {
            columnStructure.setDataType(Types.TIMESTAMP);
        } else if (type.equals("java.io.InputStream")) {
            columnStructure.setDataType(Types.LONGVARBINARY);
        } else if (type.equals("java.io.Reader")) {
            columnStructure.setDataType(Types.LONGVARCHAR);
        } else if (type.equals("string")) {
            // A type in the old version of C#.NET definitions.
            columnStructure.setDataType(Types.VARCHAR);
        	if (IS_FORCE_CS_DOTNET_STRING_AS_NVARCHAR) {
        		// FIXME 2012.07.09: Eventually, we would like to change this value so that it can be set externally.
                columnStructure.setDataType(Types.NVARCHAR);
        	}
        } else if (type.equals("string(Unicode)")) {
            // A type in the old version of C#.NET definitions.
            columnStructure.setDataType(Types.NVARCHAR);
        } else if (type.equals("bool")) {
            // A type in the old version of C#.NET definitions.
            columnStructure.setDataType(Types.BIT);
        } else if (type.equals("decimal")) {
            // A type in the old version of C#.NET definitions.
            columnStructure.setDataType(Types.DECIMAL);
        } else if (type.equals("System.DateTime")) {
            // A type in the old version of C#.NET definitions.
            columnStructure.setDataType(Types.TIMESTAMP);
        } else if (type.equals("byte[]")) {
            // A type in the old version of C#.NET definitions.
            columnStructure.setDataType(Types.LONGVARBINARY);
        } else {
            throw new IllegalArgumentException("A type that is not supported by blancoDb [" + type
                    + "] was given.");
        }
    }
}
