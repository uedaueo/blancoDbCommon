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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSetMetaData;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import blanco.commons.util.BlancoNameAdjuster;
import blanco.commons.util.BlancoStringUtil;
import blanco.db.common.stringgroup.BlancoDbSqlInfoScrollStringGroup;
import blanco.db.common.stringgroup.BlancoDbSqlInfoTypeStringGroup;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.dbmetadata.BlancoDbMetaDataUtil;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * Writes the SQL information list of blancoDb to an XML file.
 * 
 * @author IGA Tosiki
 */
public class BlancoDbXmlSerializer {
    /**
     * Writes the SQL information list of blancoDb to an XML file.
     * 
     * @param resultList
     * @param fileOutput
     */
    public static void serialize(
            final List<BlancoDbSqlInfoStructure> resultList,
            final File fileOutput) {
        if (resultList == null) {
            throw new IllegalArgumentException(
                    "The conversion process to XML has been given null as an argument (resultList).");
        }

        final DocumentBuilderFactory documentFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex1) {
            System.out.println("An exception was thrown during document creation:" + ex1.toString());
            ex1.printStackTrace();
            return;
        }

        final Document document = documentBuilder.newDocument();
        final Element eleRoot = document.createElement("workbook");
        document.appendChild(eleRoot);

        for (int index = 0; index < resultList.size(); index++) {
            final BlancoDbSqlInfoStructure sqlInfo = resultList.get(index);
            append(sqlInfo, document, eleRoot);
        }

        OutputStream outStream = null;
        try {
            outStream = new BufferedOutputStream(new FileOutputStream(
                    fileOutput));
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty("standalone", "yes");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(document), new StreamResult(
                    outStream));
            outStream.flush();
            outStream.close();
            outStream = null;
        } catch (TransformerException ex) {
            System.out.println("A conversion exception occurred when saving the XML document:" + ex.toString());
            ex.printStackTrace();
            return;
        } catch (IOException ex3) {
            System.out.println("An I/O exception occurred when saving the XML document:" + ex3.toString());
            ex3.printStackTrace();
            return;
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Adds the given SQL information to the XML element.
     * 
     * @param sqlInfo
     * @param document
     * @param eleRoot
     */
    private static void append(final BlancoDbSqlInfoStructure sqlInfo,
            final Document document, final Element eleRoot) {

        final Element eleQuery = document.createElement("sheet");
        final Element eleCommon = document.createElement("blancodb-common");
        eleQuery.appendChild(eleCommon);

        appendElementWithText(document, eleCommon, "name", sqlInfo.getName());
        appendElementWithText(document, eleCommon, "package", BlancoStringUtil
                .null2Blank(sqlInfo.getPackage()));
        appendElementWithText(document, eleCommon, "query-type",
                new BlancoDbSqlInfoTypeStringGroup().convertToString(sqlInfo
                        .getType()));
        if (sqlInfo.getDescription() != null) {
            appendElementWithText(document, eleCommon, "description", sqlInfo.getDescription());
        }
        appendElementWithText(document, eleCommon, "single", sqlInfo
                .getSingle() ? "true" : "false");

        if (sqlInfo.getType() == BlancoDbSqlInfoTypeStringGroup.ITERATOR) {
            // Adds the scroll and updatable attributes only for the search type.
            if (sqlInfo.getScroll() != BlancoDbSqlInfoScrollStringGroup.NOT_DEFINED) {
                appendElementWithText(document, eleCommon, "scroll",
                        new BlancoDbSqlInfoScrollStringGroup()
                                .convertToString(sqlInfo.getScroll()));
            }
            appendElementWithText(document, eleCommon, "updatable", sqlInfo
                    .getUpdatable() ? "true" : "false");
        }

        appendElementWithText(document, eleCommon, "dynamic-sql", sqlInfo
                .getDynamicSql() ? "true" : "false");
        appendElementWithText(document, eleCommon, "use-bean-parameter",
                sqlInfo.getUseBeanParameter() ? "true" : "false");
        appendElementWithText(document, eleCommon, "statementTimeout", String
                .valueOf(sqlInfo.getStatementTimeout()));

        final Element queryRoot = document.createElement("blancodb-query");
        eleQuery.appendChild(queryRoot);
        final Element queryLine = document.createElement("query-line");
        queryRoot.appendChild(queryLine);

        final Element parameters = document
                .createElement("blancodb-inparameters");
        eleQuery.appendChild(parameters);

        for (int indexCol = 0; indexCol < sqlInfo.getInParameterList().size(); indexCol++) {
            final BlancoDbMetaDataColumnStructure columnStructure = sqlInfo
                    .getInParameterList().get(indexCol);

            final Element parameter = document.createElement("inparameter");

            appendElementWithText(document, parameter, "name",
                    BlancoNameAdjuster.toParameterName(columnStructure
                            .getName()));
            appendElementWithText(document, parameter, "type",
                    BlancoDbMetaDataUtil
                            .convertJdbcDataTypeToString(columnStructure
                                    .getDataType()));
            appendElementWithText(
                    document,
                    parameter,
                    "nullable",
                    (columnStructure.getNullable() == ResultSetMetaData.columnNoNulls ? "NoNulls"
                            : "Nullable"));

            parameters.appendChild(parameter);
        }

        // TODO: Output parameter expansion is currently not implemented yet.

        if (sqlInfo.getQuery() == null) {
            throw new IllegalArgumentException("Internal error: SQL statement is missing from SQL information.");
        }
        final CDATASection cdata = document.createCDATASection(sqlInfo
                .getQuery());
        queryLine.appendChild(cdata);

        // Adds it to the root node at the very end.
        eleRoot.appendChild(eleQuery);
    }

    /**
     * Adds an element with text to an element.
     * 
     * @param document
     * @param eleTarget
     * @param tagName
     * @param elementData
     */
    private static final void appendElementWithText(final Document document,
            final Element eleTarget, final String tagName,
            final String elementData) {
        if (document == null) {
            throw new IllegalArgumentException(
                    "Internal error: appendElementWithText: Null was given for document.");
        }
        if (eleTarget == null) {
            throw new IllegalArgumentException(
                    "Internal error: appendElementWithText: Null was given for eleTarget.");
        }
        if (tagName == null) {
            throw new IllegalArgumentException(
                    "Internal error: appendElementWithText: Null was given for tagName.");
        }
        if (elementData == null) {
            throw new IllegalArgumentException(
                    "Internal error: appendElementWithText: Null was given for elementData.");
        }

        final Element eleWork = document.createElement(tagName);
        eleTarget.appendChild(eleWork);
        eleWork.appendChild(document.createTextNode(elementData));
    }
}
