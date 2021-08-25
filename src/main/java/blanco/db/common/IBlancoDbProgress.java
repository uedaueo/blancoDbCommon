/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.common;

public interface IBlancoDbProgress {
    /**
     * It will be called back each time each file is processed.
     * 
     * @param progressCurrent
     *            Current progress.
     * @param progressTotal
     *            Total count.
     * @param progressMessage
     *            File name, table name, etc. that is currently be processed.
     * @return false if you want to interrupt.
     */
    boolean progress(final int progressCurrent, final int progressTotal,
            final String progressItem);
}
