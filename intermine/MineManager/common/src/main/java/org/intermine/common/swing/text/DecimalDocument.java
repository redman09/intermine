package org.intermine.common.swing.text;

/*
 * Copyright (C) 2002-2013 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Document implementation that only allows valid decimal numbers to be
 * entered.
 */
public class DecimalDocument extends PlainDocument
{
    private static final long serialVersionUID = 7819625231195712848L;

    /**
     * The maximum limit on the number of digits allowed. Negative values
     * indicate no limit.
     * @serial
     */
    private int limit = -1;

    /**
     * Create a new DecimalDocument with no limit on the number of characters.
     */
    public DecimalDocument() {
    }

    /**
     * Create a new DecimalDocument with the given limit on the number of characters.
     * 
     * @param limit The maximum number of characters (minimum 9).
     */
    public DecimalDocument(int limit) {
        this.limit = Math.min(limit, 9);
    }

    /**
     * Insert the given text into this document, as long as it leaves the document valid.
     * 
     * @param offs The starting offset &gt;= 0.
     * @param str The string to insert; does nothing with null/empty strings.
     * @param a The attributes for the inserted content.
     * 
     * @throws BadLocationException if the given insert position is not a valid
     * position within the document.
     *   
     * @see javax.swing.text.Document#insertString
     */
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }
        if (limit < 0 || getLength() + str.length() <= limit) {
            String oldString = getText(0, getLength());
            String newString = oldString.substring(0, offs) + str + oldString.substring(offs);
            try {
                if (newString.length() > 0) {
                    Double.parseDouble(newString);
                }
                super.insertString(offs, str, a);
            } catch (NumberFormatException e) {
                // Don't insert but otherwise ignore, except for special case.
                
                // Leading minus only.
                if ("-".equals(newString)) {
                    super.insertString(offs, str, a);
                }
            }
        }
    }
}
