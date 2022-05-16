/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.common.expander;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;

/**
 * A collection of general information about blancoDb's BlancoCgMethod expansion process.
 * 
 * @author IGA Tosiki
 */
public abstract class BlancoDbAbstractMethod {
    /**
     * Configuration information about blancoDb.
     */
    protected BlancoDbSetting fDbSetting = null;

    /**
     * The structure of the SQL information that is processed by this method.
     */
    protected BlancoDbSqlInfoStructure fSqlInfo = null;

    /**
     * Output destination blancoCg object factory.
     */
    protected BlancoCgObjectFactory fCgFactory = null;

    /**
     * Output destination source code object, used when you want to add an import.
     */
    protected BlancoCgSourceFile fCgSourceFile = null;

    /**
     * The output destination blancoCgClass.
     */
    protected BlancoCgClass fCgClass = null;

    protected BlancoDbAbstractMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        fDbSetting = argDbSetting;
        fSqlInfo = argSqlInfo;
        fCgFactory = argCgFactory;
        fCgSourceFile = argCgSourceFile;
        fCgClass = argCgClass;
    }

    public abstract void expand();
}
