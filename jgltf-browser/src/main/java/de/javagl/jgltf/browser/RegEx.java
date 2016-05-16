/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.browser;

/**
 * Utility methods related to regular expressions
 */
class RegEx
{
    /**
     * Returns whether the given string matches a regular expression that
     * was derived from the given simple expression. The regular expression
     * is derived from the given simple expression as follows:
     * <ul>
     *   <li>It will be preceded by <code>"^"</code></li>
     *   <li>All <code>"."</code> will be escaped</li>
     *   <li>All <code>"["</code> will be escaped</li>
     *   <li>All <code>"]"</code> will be escaped</li>
     *   <li>
     *     All <code>"*"</code> will be replaced with 
     *     <code>"[^.]*"</code>
     *   </li>
     * </ul>
     *  
     * @param string The input string
     * @param simpleExpression The simple expression
     * @return Whether the string matches the simple expression
     */
    static boolean matches(String string, String simpleExpression)
    {
        String e = simpleExpression;
        e = e.replaceAll("\\.", "\\\\.");
        e = e.replaceAll("\\[", "\\\\[");
        e = e.replaceAll("\\]", "\\\\]");
        e = e.replaceAll("\\*", "[^.]*");
        boolean result = string.matches(e);
        //System.out.println("Check if "+string);
        //System.out.println("matches  "+simpleExpression);
        //System.out.println("regEx    "+e);
        //System.out.println("gives    "+result);
        return result;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private RegEx()
    {
        // Private constructor to prevent instantiation
    }
}
