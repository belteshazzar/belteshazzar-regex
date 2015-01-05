package com.belteshazzar.regex;

/*
 * Copyright 1999-2007 Sun Microsystems, Inc. All Rights Reserved. DO NOT ALTER
 * OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 only, as published by
 * the Free Software Foundation. Sun designates this particular file as subject
 * to the "Classpath" exception as provided by Sun in the LICENSE file that
 * accompanied this code.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License version 2 for more
 * details (a copy is included in the LICENSE file that accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this work; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara, CA
 * 95054 USA or visit www.sun.com if you need additional information or have any
 * questions.
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

/**
 * A compiled representation of a regular expression.
 * 
 * <p>
 * A regular expression, specified as a string, must first be compiled into an
 * instance of this class. The resulting pattern can then be used to create a
 * {@link Matcher} object that can match arbitrary {@link java.lang.List
 * <Comparable<T>> </code>character sequences<code>} against the regular
 * expression. All of the state involved in performing a match resides in the
 * matcher, so many matchers can share the same pattern.
 * 
 * <p>
 * A typical invocation sequence is thus
 * 
 * <blockquote>
 * 
 * <pre>
 * Pattern p = Pattern.{@link #compile compile}("a*b");
 * Matcher m = p.{@link #matcher matcher}("aaaaab");
 * boolean b = m.{@link Matcher#matches matches}();
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * A {@link #matches matches} method is defined by this class as a convenience
 * for when a regular expression is used just once. This method compiles an
 * expression and matches an input sequence against it in a single invocation.
 * The statement
 * 
 * <blockquote>
 * 
 * <pre>
 * boolean b = Pattern.matches(&quot;a*b&quot;, &quot;aaaaab&quot;);
 * </pre>
 * 
 * </blockquote>
 * 
 * is equivalent to the three statements above, though for repeated matches it
 * is less efficient since it does not allow the compiled pattern to be reused.
 * 
 * <p>
 * Instances of this class are immutable and are safe for use by multiple
 * concurrent threads. Instances of the {@link Matcher} class are not safe for
 * such use.
 * 
 * 
 * <a name="sum">
 * <h4>Summary of regular-expression constructs</h4>
 * 
 * <table border="0" cellpadding="1" cellspacing="0" * summary="Regular expression constructs, and what they match">
 * 
 * <tr align="left">
 * <th bgcolor="#CCCCFF" align="left" id="construct">Construct</th>
 * <th bgcolor="#CCCCFF" align="left" id="matches">Matches</th>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="characters">Characters</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct characters"><i>x</i></td>
 * <td headers="matches">The character <i>x</i></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\\</tt></td>
 * <td headers="matches">The backslash character</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\0</tt><i>n</i></td>
 * <td headers="matches">The character with octal value <tt>0</tt><i>n</i> (0�
 * <tt><=</tt>�<i>n</i>�<tt><=</tt>�7)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\0</tt><i>nn</i></td>
 * <td headers="matches">The character with octal value <tt>0</tt><i>nn</i> (0�
 * <tt><=</tt>�<i>n</i>�<tt><=</tt>�7)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\0</tt><i>mnn</i></td>
 * <td headers="matches">The character with octal value <tt>0</tt><i>mnn</i> (0�
 * <tt><=</tt>�<i>m</i>�<tt><=</tt>�3, 0�<tt><=</tt>�<i>n</i>�<tt><=</tt>�7)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\x</tt><i>hh</i></td>
 * <td headers="matches">The character with hexadecimal�value�<tt>0x</tt>
 * <i>hh</i></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt></tt><i>hhhh</i></td>
 * <td headers="matches">The character with hexadecimal�value�<tt>0x</tt>
 * <i>hhhh</i></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="matches"><tt>\t</tt></td>
 * <td headers="matches">The tab character (<tt>'\u0009'</tt>)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\n</tt></td>
 * <td headers="matches">The newline (line feed) character (<tt>'\u000A'</tt>)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\r</tt></td>
 * <td headers="matches">The carriage-return character (<tt>'\u000D'</tt>)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\f</tt></td>
 * <td headers="matches">The form-feed character (<tt>'\u000C'</tt>)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\a</tt></td>
 * <td headers="matches">The alert (bell) character (<tt>'\u0007'</tt>)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\e</tt></td>
 * <td headers="matches">The escape character (<tt>'\u001B'</tt>)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct characters"><tt>\c</tt><i>x</i></td>
 * <td headers="matches">The control character corresponding to <i>x</i></td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="classes">Character classes</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct classes"><tt>[abc]</tt></td>
 * <td headers="matches"><tt>a</tt>, <tt>b</tt>, or <tt>c</tt> (simple class)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct classes"><tt>[^abc]</tt></td>
 * <td headers="matches">Any character except <tt>a</tt>, <tt>b</tt>, or
 * <tt>c</tt> (negation)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct classes"><tt>[a-zA-Z]</tt></td>
 * <td headers="matches"><tt>a</tt> through <tt>z</tt> or <tt>A</tt> through
 * <tt>Z</tt>, inclusive (range)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct classes"><tt>[a-d[m-p]]</tt></td>
 * <td headers="matches"><tt>a</tt> through <tt>d</tt>, or <tt>m</tt> through
 * <tt>p</tt>: <tt>[a-dm-p]</tt> (union)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct classes"><tt>[a-z&&[def]]</tt></td>
 * <td headers="matches"><tt>d</tt>, <tt>e</tt>, or <tt>f</tt> (intersection)
 * </tr>
 * <tr>
 * <td valign="top" headers="construct classes"><tt>[a-z&&[^bc]]</tt></td>
 * <td headers="matches"><tt>a</tt> through <tt>z</tt>, except for <tt>b</tt>
 * and <tt>c</tt>: <tt>[ad-z]</tt> (subtraction)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct classes"><tt>[a-z&&[^m-p]]</tt></td>
 * <td headers="matches"><tt>a</tt> through <tt>z</tt>, and not <tt>m</tt>
 * through <tt>p</tt>: <tt>[a-lq-z]</tt>(subtraction)</td>
 * </tr>
 * <tr>
 * <th>�</th>
 * </tr>
 * 
 * <tr align="left">
 * <th colspan="2" id="predef">Predefined character classes</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct predef"><tt>.</tt></td>
 * <td headers="matches">Any character (may or may not match <a href="#lt">line
 * terminators</a>)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct predef"><tt>\d</tt></td>
 * <td headers="matches">A digit: <tt>[0-9]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct predef"><tt>\D</tt></td>
 * <td headers="matches">A non-digit: <tt>[^0-9]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct predef"><tt>\s</tt></td>
 * <td headers="matches">A whitespace character: <tt>[ \t\n\x0B\f\r]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct predef"><tt>\S</tt></td>
 * <td headers="matches">A non-whitespace character: <tt>[^\s]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct predef"><tt>\w</tt></td>
 * <td headers="matches">A word character: <tt>[a-zA-Z_0-9]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct predef"><tt>\W</tt></td>
 * <td headers="matches">A non-word character: <tt>[^\w]</tt></td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="posix">POSIX character classes</b> (US-ASCII only)<b></th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Lower}</tt></td>
 * <td headers="matches">A lower-case alphabetic character: <tt>[a-z]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Upper}</tt></td>
 * <td headers="matches">An upper-case alphabetic character:<tt>[A-Z]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{ASCII}</tt></td>
 * <td headers="matches">All ASCII:<tt>[\x00-\x7F]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Alpha}</tt></td>
 * <td headers="matches">An alphabetic character:<tt>[\p{Lower}\p{Upper}]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Digit}</tt></td>
 * <td headers="matches">A decimal digit: <tt>[0-9]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Alnum}</tt></td>
 * <td headers="matches">An alphanumeric character:<tt>[\p{Alpha}\p{Digit}]</tt>
 * </td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Punct}</tt></td>
 * <td headers="matches">Punctuation: One of
 * <tt>!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~</tt></td>
 * </tr>
 * <!-- <tt>[\!"#\$%&'\(\)\*\+,\-\./:;\<=\>\?@\[\\\]\^_`\{\|\}~]</tt>
 * <tt>[\X21-\X2F\X31-\X40\X5B-\X60\X7B-\X7E]</tt> -->
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Graph}</tt></td>
 * <td headers="matches">A visible character: <tt>[\p{Alnum}\p{Punct}]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Print}</tt></td>
 * <td headers="matches">A printable character: <tt>[\p{Graph}\x20]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Blank}</tt></td>
 * <td headers="matches">A space or a tab: <tt>[ \t]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Cntrl}</tt></td>
 * <td headers="matches">A control character: <tt>[\x00-\x1F\x7F]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{XDigit}</tt></td>
 * <td headers="matches">A hexadecimal digit: <tt>[0-9a-fA-F]</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct posix"><tt>\p{Space}</tt></td>
 * <td headers="matches">A whitespace character: <tt>[ \t\n\x0B\f\r]</tt></td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2">java.lang.Character classes (simple <a href="#jcc">java
 * character type</a>)</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top"><tt>\p{javaLowerCase}</tt></td>
 * <td>Equivalent to java.lang.Character.isLowerCase()</td>
 * </tr>
 * <tr>
 * <td valign="top"><tt>\p{javaUpperCase}</tt></td>
 * <td>Equivalent to java.lang.Character.isUpperCase()</td>
 * </tr>
 * <tr>
 * <td valign="top"><tt>\p{javaWhitespace}</tt></td>
 * <td>Equivalent to java.lang.Character.isWhitespace()</td>
 * </tr>
 * <tr>
 * <td valign="top"><tt>\p{javaMirrored}</tt></td>
 * <td>Equivalent to java.lang.Character.isMirrored()</td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="unicode">Classes for Unicode blocks and categories</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct unicode"><tt>\p{InGreek}</tt></td>
 * <td headers="matches">A character in the Greek�block (simple <a
 * href="#ubc">block</a>)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct unicode"><tt>\p{Lu}</tt></td>
 * <td headers="matches">An uppercase letter (simple <a
 * href="#ubc">category</a>)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct unicode"><tt>\p{Sc}</tt></td>
 * <td headers="matches">A currency symbol</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct unicode"><tt>\P{InGreek}</tt></td>
 * <td headers="matches">Any character except one in the Greek block (negation)</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct unicode"><tt>[\p{L}&&[^\p{Lu}]]�</tt></td>
 * <td headers="matches">Any letter except an uppercase letter (subtraction)</td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="bounds">Boundary matchers</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct bounds"><tt>^</tt></td>
 * <td headers="matches">The beginning of a line</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct bounds"><tt>$</tt></td>
 * <td headers="matches">The end of a line</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct bounds"><tt>\b</tt></td>
 * <td headers="matches">A word boundary</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct bounds"><tt>\B</tt></td>
 * <td headers="matches">A non-word boundary</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct bounds"><tt>\A</tt></td>
 * <td headers="matches">The beginning of the input</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct bounds"><tt>\G</tt></td>
 * <td headers="matches">The end of the previous match</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct bounds"><tt>\Z</tt></td>
 * <td headers="matches">The end of the input but for the final <a
 * href="#lt">terminator</a>, if�any</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct bounds"><tt>\z</tt></td>
 * <td headers="matches">The end of the input</td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="greedy">Greedy quantifiers</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct greedy"><i>X</i><tt>?</tt></td>
 * <td headers="matches"><i>X</i>, once or not at all</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct greedy"><i>X</i><tt>*</tt></td>
 * <td headers="matches"><i>X</i>, zero or more times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct greedy"><i>X</i><tt>+</tt></td>
 * <td headers="matches"><i>X</i>, one or more times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct greedy"><i>X</i><tt>{</tt><i>n</i>
 * <tt>}</tt></td>
 * <td headers="matches"><i>X</i>, exactly <i>n</i> times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct greedy"><i>X</i><tt>{</tt><i>n</i>
 * <tt>,}</tt></td>
 * <td headers="matches"><i>X</i>, at least <i>n</i> times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct greedy"><i>X</i><tt>{</tt><i>n</i>
 * <tt>,</tt><i>m</i><tt>}</tt></td>
 * <td headers="matches"><i>X</i>, at least <i>n</i> but not more than <i>m</i>
 * times</td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="reluc">Reluctant quantifiers</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct reluc"><i>X</i><tt>??</tt></td>
 * <td headers="matches"><i>X</i>, once or not at all</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct reluc"><i>X</i><tt>*?</tt></td>
 * <td headers="matches"><i>X</i>, zero or more times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct reluc"><i>X</i><tt>+?</tt></td>
 * <td headers="matches"><i>X</i>, one or more times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct reluc"><i>X</i><tt>{</tt><i>n</i>
 * <tt>}?</tt></td>
 * <td headers="matches"><i>X</i>, exactly <i>n</i> times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct reluc"><i>X</i><tt>{</tt><i>n</i>
 * <tt>,}?</tt></td>
 * <td headers="matches"><i>X</i>, at least <i>n</i> times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct reluc"><i>X</i><tt>{</tt><i>n</i>
 * <tt>,</tt><i>m</i><tt>}?</tt></td>
 * <td headers="matches"><i>X</i>, at least <i>n</i> but not more than <i>m</i>
 * times</td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="poss">Possessive quantifiers</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct poss"><i>X</i><tt>?+</tt></td>
 * <td headers="matches"><i>X</i>, once or not at all</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct poss"><i>X</i><tt>*+</tt></td>
 * <td headers="matches"><i>X</i>, zero or more times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct poss"><i>X</i><tt>++</tt></td>
 * <td headers="matches"><i>X</i>, one or more times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct poss"><i>X</i><tt>{</tt><i>n</i>
 * <tt>}+</tt></td>
 * <td headers="matches"><i>X</i>, exactly <i>n</i> times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct poss"><i>X</i><tt>{</tt><i>n</i>
 * <tt>,}+</tt></td>
 * <td headers="matches"><i>X</i>, at least <i>n</i> times</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct poss"><i>X</i><tt>{</tt><i>n</i>
 * <tt>,</tt><i>m</i><tt>}+</tt></td>
 * <td headers="matches"><i>X</i>, at least <i>n</i> but not more than <i>m</i>
 * times</td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="logical">Logical operators</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct logical"><i>XY</i></td>
 * <td headers="matches"><i>X</i> followed by <i>Y</i></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct logical"><i>X</i><tt>|</tt><i>Y</i></td>
 * <td headers="matches">Either <i>X</i> or <i>Y</i></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct logical"><tt>(</tt><i>X</i><tt>)</tt></td>
 * <td headers="matches">X, as a <a href="#cg">capturing group</a></td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="backref">Back references</th>
 * </tr>
 * 
 * <tr>
 * <td valign="bottom" headers="construct backref"><tt>\</tt><i>n</i></td>
 * <td valign="bottom" headers="matches">Whatever the <i>n</i><sup>th</sup> <a
 * href="#cg">capturing group</a> matched</td>
 * </tr>
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="quot">Quotation</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct quot"><tt>\</tt></td>
 * <td headers="matches">Nothing, but quotes the following character</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct quot"><tt>\Q</tt></td>
 * <td headers="matches">Nothing, but quotes all characters until <tt>\E</tt></td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct quot"><tt>\E</tt></td>
 * <td headers="matches">Nothing, but ends quoting started by <tt>\Q</tt></td>
 * </tr>
 * <!-- Metachars: !$()*+.<>?[\]^{|} -->
 * 
 * <tr>
 * <th>�</th>
 * </tr>
 * <tr align="left">
 * <th colspan="2" id="special">Special constructs (non-capturing)</th>
 * </tr>
 * 
 * <tr>
 * <td valign="top" headers="construct special"><tt>(?:</tt><i>X</i><tt>)</tt></td>
 * <td headers="matches"><i>X</i>, as a non-capturing group</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct special"><tt>(?idmsux-idmsux)�</tt></td>
 * <td headers="matches">Nothing, but turns match flags <a
 * href="#CASE_INSENSITIVE">i</a> <a href="#UNIX_LINES">d</a> <a
 * href="#MULTILINE">m</a> <a href="#DOTALL">s</a> <a href="#UNICODE_CASE">u</a>
 * <a href="#COMMENTS">x</a> on - off</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct special"><tt>(?idmsux-idmsux:</tt>
 * <i>X</i><tt>)</tt>��</td>
 * <td headers="matches"><i>X</i>, as a <a href="#cg">non-capturing group</a>
 * with the given flags <a href="#CASE_INSENSITIVE">i</a> <a
 * href="#UNIX_LINES">d</a> <a href="#MULTILINE">m</a> <a href="#DOTALL">s</a>
 * <a href="#UNICODE_CASE">u</a > <a href="#COMMENTS">x</a> on - off</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct special"><tt>(?=</tt><i>X</i><tt>)</tt></td>
 * <td headers="matches"><i>X</i>, via zero-width positive lookahead</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct special"><tt>(?!</tt><i>X</i><tt>)</tt></td>
 * <td headers="matches"><i>X</i>, via zero-width negative lookahead</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct special"><tt>(?<=</tt><i>X</i><tt>)</tt></td>
 * <td headers="matches"><i>X</i>, via zero-width positive lookbehind</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct special"><tt>(?<!</tt><i>X</i><tt>)</tt></td>
 * <td headers="matches"><i>X</i>, via zero-width negative lookbehind</td>
 * </tr>
 * <tr>
 * <td valign="top" headers="construct special"><tt>(?></tt><i>X</i><tt>)</tt></td>
 * <td headers="matches"><i>X</i>, as an independent, non-capturing group</td>
 * </tr>
 * 
 * </table>
 * 
 * <hr>
 * 
 * 
 * <a name="bs">
 * <h4>Backslashes, escapes, and quoting</h4>
 * 
 * <p>
 * The backslash character (<tt>'\'</tt>) serves to introduce escaped
 * constructs, as defined in the table above, as well as to quote characters
 * that otherwise would be interpreted as unescaped constructs. Thus the
 * expression <tt>\\</tt> matches a single backslash and <tt>\{</tt> matches a
 * left brace.
 * 
 * <p>
 * It is an error to use a backslash prior to any alphabetic character that does
 * not denote an escaped construct; these are reserved for future extensions to
 * the regular-expression language. A backslash may be used prior to a
 * non-alphabetic character regardless of whether that character is part of an
 * unescaped construct.
 * 
 * <p>
 * Backslashes within string literals in Java source code are interpreted as
 * required by the <a href="http://java.sun.com/docs/books/jls">Java Language
 * Specification</a> as either <a href=
 * "http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#100850"
 * >Unicode escapes</a> or other <a href=
 * "http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#101089"
 * >character escapes</a>. It is therefore necessary to double backslashes in
 * string literals that represent regular expressions to protect them from
 * interpretation by the Java bytecode compiler. The string literal
 * <tt>"\b"</tt>, for example, matches a single backspace character when
 * interpreted as a regular expression, while <tt>"\\b"</tt> matches a word
 * boundary. The string literal <tt>"\(hello\)"</tt> is illegal and leads to a
 * compile-time error; in order to match the string <tt>(hello)</tt> the string
 * literal <tt>"\\(hello\\)"</tt> must be used.
 * 
 * <a name="cc">
 * <h4>Character Classes</h4>
 * 
 * <p>
 * Character classes may appear within other character classes, and may be
 * composed by the union operator (implicit) and the intersection operator (
 * <tt>&&</tt>). The union operator denotes a class Pattern contains every
 * character that is in at least one of its operand classes. The intersection
 * operator denotes a class that contains every character that is in both of its
 * operand classes.
 * 
 * <p>
 * The precedence of character-class operators is as follows, from highest to
 * lowest:
 * 
 * <blockquote>
 * <table border="0" cellpadding="1" cellspacing="0" * summary="Precedence of character class operators.">
 * <tr>
 * <th>1����</th>
 * <td>Literal escape����</td>
 * <td><tt>\x</tt></td>
 * </tr>
 * <tr>
 * <th>2����</th>
 * <td>Grouping</td>
 * <td><tt>[...]</tt></td>
 * </tr>
 * <tr>
 * <th>3����</th>
 * <td>Range</td>
 * <td><tt>a-z</tt></td>
 * </tr>
 * <tr>
 * <th>4����</th>
 * <td>Union</td>
 * <td><tt>[a-e][i-u]</tt></td>
 * </tr>
 * <tr>
 * <th>5����</th>
 * <td>Intersection</td>
 * <td><tt>[a-z&&[aeiou]]</tt></td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * <p>
 * Note that a different set of metacharacters are in effect inside a character
 * class than outside a character class. For instance, the regular expression
 * <tt>.</tt> loses its special meaning inside a character class, while the
 * expression <tt>-</tt> becomes a range forming metacharacter.
 * 
 * <a name="lt">
 * <h4>Line terminators</h4>
 * 
 * <p>
 * A <i>line terminator</i> is a one- or two-character sequence that marks the
 * end of a line of the input character sequence. The following are recognized
 * as line terminators:
 * 
 * <ul>
 * 
 * <li>A newline (line feed) character�(<tt>'\n'</tt>),
 * 
 * <li>A carriage-return character followed immediately by a newline character�(
 * <tt>"\r\n"</tt>),
 * 
 * <li>A standalone carriage-return character�(<tt>'\r'</tt>),
 * 
 * <li>A next-line character�(<tt>'\u0085'</tt>),
 * 
 * <li>A line-separator character�(<tt>'\u2028'</tt>), or
 * 
 * <li>A paragraph-separator character�(<tt>'\u2029</tt>).
 * 
 * </ul>
 * <p>
 * If {@link #UNIX_LINES} mode is activated, then the only line terminators
 * recognized are newline characters.
 * 
 * <p>
 * The regular expression <tt>.</tt> matches any character except a line
 * terminator unless the {@link #DOTALL} flag is specified.
 * 
 * <p>
 * By default, the regular expressions <tt>^</tt> and <tt>$</tt> ignore line
 * terminators and only match at the beginning and the end, respectively, of the
 * entire input sequence. If {@link #MULTILINE} mode is activated then
 * <tt>^</tt> matches at the beginning of input and after any line terminator
 * except at the end of input. When in {@link #MULTILINE} mode <tt>$</tt>
 * matches just before a line terminator or the end of the input sequence.
 * 
 * <a name="cg">
 * <h4>Groups and capturing</h4>
 * 
 * <p>
 * Capturing groups are numbered by counting their opening parentheses from left
 * to right. In the expression <tt>((A)(B(C)))</tt>, for example, there are four
 * such groups:
 * </p>
 * 
 * <blockquote>
 * <table cellpadding=1 cellspacing=0 summary="Capturing group numberings">
 * <tr>
 * <th>1����</th>
 * <td><tt>((A)(B(C)))</tt></td>
 * </tr>
 * <tr>
 * <th>2����</th>
 * <td><tt>(A)</tt></td>
 * </tr>
 * <tr>
 * <th>3����</th>
 * <td><tt>(B(C))</tt></td>
 * </tr>
 * <tr>
 * <th>4����</th>
 * <td><tt>(C)</tt></td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * <p>
 * Group zero always stands for the entire expression.
 * 
 * <p>
 * Capturing groups are so named because, during a match, each subsequence of
 * the input sequence that matches such a group is saved. The captured
 * subsequence may be used later in the expression, via a back reference, and
 * may also be retrieved from the matcher once the match operation is complete.
 * 
 * <p>
 * The captured input associated with a group is always the subsequence that the
 * group most recently matched. If a group is evaluated a second time because of
 * quantification then its previously-captured value, if any, will be retained
 * if the second evaluation fails. Matching the string <tt>"aba"</tt> against
 * the expression <tt>(a(b)?)+</tt>, for example, leaves group two set to
 * <tt>"b"</tt>. All captured input is discarded at the beginning of each match.
 * 
 * <p>
 * Groups beginning with <tt>(?</tt> are pure, <i>non-capturing</i> groups that
 * do not capture text and do not count towards the group total.
 * 
 * 
 * <h4>Unicode support</h4>
 * 
 * <p>
 * This class is in conformance with Level 1 of <a
 * href="http://www.unicode.org/reports/tr18/"><i>Unicode Technical Standard
 * #18: Unicode Regular Expression Guidelines</i></a>, plus RL2.1 Canonical
 * Equivalents.
 * 
 * <p>
 * Unicode escape sequences such as <tt>\u2014</tt> in Java source code are
 * processed as described in <a href=
 * "http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#100850"
 * >\u00A73.3</a> of the Java Language Specification. Such escape sequences are
 * also implemented directly by the regular-expression parser so that Unicode
 * escapes can be used in expressions that are read from files or from the
 * keyboard. Thus the strings <tt>"\u2014"</tt> and <tt>"\\u2014"</tt>, while
 * not equal, compile into the same pattern, which matches the character with
 * hexadecimal value <tt>0x2014</tt>.
 * 
 * <a name="ubc">
 * <p>
 * Unicode blocks and categories are written with the <tt>\p</tt> and
 * <tt>\P</tt> constructs as in Perl. <tt>\p{</tt><i>prop</i><tt>}</tt> matches
 * if the input has the property <i>prop</i>, while <tt>\P{</tt><i>prop</i>
 * <tt>}</tt> does not match if the input has that property. Blocks are
 * specified with the prefix <tt>In</tt>, as in <tt>InMongolian</tt>. Categories
 * may be specified with the optional prefix <tt>Is</tt>: Both <tt>\p{L}</tt>
 * and <tt>\p{IsL}</tt> denote the category of Unicode letters. Blocks and
 * categories can be used both inside and outside of a character class.
 * 
 * <p>
 * The supported categories are those of <a
 * href="http://www.unicode.org/unicode/standard/standard.html"> <i>The Unicode
 * Standard</i></a> in the version specified by the {@link java.lang.Character
 * Character} class. The category names are those defined in the Standard, both
 * normative and informative. The block names supported by <code>Pattern</code>
 * are the valid block names accepted and defined by
 * {@link java.lang.Character.UnicodeBlock#forName(String) UnicodeBlock.forName}
 * .
 * 
 * <a name="jcc">
 * <p>
 * Categories that behave like the java.lang.Character boolean
 * is<i>methodname</i> methods (except for the deprecated ones) are available
 * through the same <tt>\p{</tt><i>prop</i><tt>}</tt> syntax where the specified
 * property has the name <tt>java<i>methodname</i></tt>.
 * 
 * <h4>Comparison to Perl 5</h4>
 * 
 * <p>
 * The <code>Pattern</code> engine performs traditional NFA-based matching with
 * ordered alternation as occurs in Perl 5.
 * 
 * <p>
 * Perl constructs not supported by this class:
 * </p>
 * 
 * <ul>
 * 
 * <li>
 * <p>
 * The conditional constructs <tt>(?{</tt><i>X</i><tt>})</tt> and <tt>(?(</tt>
 * <i>condition</i><tt>)</tt><i>X</i><tt>|</tt><i>Y</i><tt>)</tt>,
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * The embedded code constructs <tt>(?{</tt><i>code</i><tt>})</tt> and
 * <tt>(??{</tt><i>code</i><tt>})</tt>,
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * The embedded comment syntax <tt>(?#comment)</tt>, and
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * The preprocessing operations <tt>\l</tt> <tt></tt>, <tt>\L</tt>, and
 * <tt>\U</tt>.
 * </p>
 * </li>
 * 
 * </ul>
 * 
 * <p>
 * Constructs supported by this class but not by Perl:
 * </p>
 * 
 * <ul>
 * 
 * <li>
 * <p>
 * Possessive quantifiers, which greedily match as much as they can and do not
 * back off, even when doing so would allow the overall match to succeed.
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * Character-class union and intersection as described <a href="#cc">above</a>.
 * </p>
 * </li>
 * 
 * </ul>
 * 
 * <p>
 * Notable differences from Perl:
 * </p>
 * 
 * <ul>
 * 
 * <li>
 * <p>
 * In Perl, <tt>\1</tt> through <tt>\9</tt> are always interpreted as back
 * references; a backslash-escaped number greater than <tt>9</tt> is treated as
 * a back reference if at least that many subexpressions exist, otherwise it is
 * interpreted, if possible, as an octal escape. In this class octal escapes
 * must always begin with a zero. In this class, <tt>\1</tt> through <tt>\9</tt>
 * are always interpreted as back references, and a larger number is accepted as
 * a back reference if at least that many subexpressions exist at that point in
 * the regular expression, otherwise the parser will drop digits until the
 * number is smaller or equal to the existing number of groups or it is one
 * digit.
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * Perl uses the <tt>g</tt> flag to request a match that resumes where the last
 * match left off. This functionality is provided implicitly by the
 * {@link Matcher} class: Repeated invocations of the {@link Matcher#find find}
 * method will resume where the last match left off, unless the matcher is
 * reset.
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * In Perl, embedded flags at the top level of an expression affect the whole
 * expression. In this class, embedded flags always take effect at the point at
 * which they appear, whether they are at the top level or within a group; in
 * the latter case, flags are restored at the end of the group just as in Perl.
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * Perl is forgiving about malformed matching constructs, as in the expression
 * <tt>*a</tt>, as well as dangling brackets, as in the expression <tt>abc]</tt>
 * , and treats them as literals. This class also accepts dangling brackets but
 * is strict about dangling metacharacters like +, ? and *, and will throw a
 * {@link PatternSyntaxException} if it encounters them.
 * </p>
 * </li>
 * 
 * </ul>
 * 
 * 
 * <p>
 * For a more precise description of the behavior of regular expression
 * constructs, please see <a href="http://www.oreilly.com/catalog/regex3/">
 * <i>Mastering Regular Expressions, 3nd Edition</i>, Jeffrey E. F. Friedl,
 * O'Reilly and Associates, 2006.</a>
 * </p>
 * 
 * @see java.lang.String#split(String, int)
 * @see java.lang.String#split(String)
 * 
 * @author Mike McCloskey
 * @author Mark Reinhold
 * @author JSR-51 Expert Group
 * @since 1.4
 * @spec JSR-51
 */

public final class Pattern<T> implements java.io.Serializable
{

	/**
	 * Regular expression modifier values. Instead of being passed as arguments,
	 * they can also be passed as inline modifiers. For example, the following
	 * statements have the same effect.
	 * 
	 * <pre>
	 * RegExp r1 = RegExp.compile(&quot;abc&quot;, Pattern.I | Pattern.M);
	 * RegExp r2 = RegExp.compile(&quot;(?im)abc&quot;, 0);
	 * </pre>
	 * 
	 * The flags are duplicated so that the familiar Perl match flag names are
	 * available.
	 */

	/*
	 * Pattern has only two serialized components: The pattern string and the
	 * flags, which are all that is needed to recompile the pattern when it is
	 * deserialized.
	 */

	/** use serialVersionUID from Merlin b59 for interoperability */
	private static final long serialVersionUID = 5073258162644648461L;

	/**
	 * The original regular-expression pattern string.
	 * 
	 * @serial
	 */
	private List<Comparable<T>> pattern;

	/**
	 * Boolean indicating this Pattern is compiled; this is necessary in order
	 * to lazily compile deserialized Patterns.
	 */
	private transient volatile boolean compiled = false;

	/**
	 * The starting point of state machine for the find operation. This allows a
	 * match to start anywhere in the input.
	 */
	transient Node root;

	/**
	 * The root of object tree for a match operation. The pattern is matched at
	 * the beginning. This may include a find that uses BnM or a First node.
	 */
	transient Node matchRoot;

	/**
	 * Temporary storage used by parsing pattern slice.
	 */
	transient Patternable[] buffer;

	/**
	 * Temporary storage used while parsing group references.
	 */
	transient GroupHead[] groupNodes;

	/**
	 * Temporary null terminated code point array used by pattern compiling.
	 */
	private transient List<Patternable> temp;

	/**
	 * The number of capturing groups in this Pattern. Used by matchers to
	 * allocate storage needed to perform a match.
	 */
	transient int capturingGroupCount;

	/**
	 * The local variable count used by parsing tree. Used by matchers to
	 * allocate storage needed to perform a match.
	 */
	transient int localCount;

	/**
	 * Index into the pattern string that keeps track of how much has been
	 * parsed.
	 */
	private transient int cursor;

	/**
	 * Holds the length of the pattern string.
	 */
	private transient int patternLength;

	/**
     * 
     */
	public Pattern()
	{
		pattern = new LinkedList<Comparable<T>>();
	}

	public Pattern(List<Comparable<T>> regex)
	{
		pattern = regex;
		compile();
	}

	/**
	 * Returns the regular expression from which this pattern was compiled. </p>
	 * 
	 * @return The source of this pattern
	 */
	public List<Comparable<T>> pattern()
	{
		return pattern;
	}

	/**
	 * <p>
	 * Returns the string representation of this pattern. This is the regular
	 * expression from which this pattern was compiled.
	 * </p>
	 * 
	 * @return The string representation of this pattern
	 * @since 1.5
	 */
	public String toString()
	{
		return pattern.toString();
	}

	/**
	 * Creates a matcher that will match the given input against this pattern.
	 * </p>
	 * 
	 * @param input
	 *            The character sequence to be matched
	 * 
	 * @return A new matcher for this pattern
	 */
	public Matcher<T> matcher(List<T> input)
	{
		if (!compiled)
		{
			synchronized (this)
			{
				if (!compiled) compile();
			}
		}
		Matcher<T> m = new Matcher<T>(this, input);
		return m;
	}

	/**
	 * Compiles the given regular expression and attempts to match the given
	 * input against it.
	 * 
	 * <p>
	 * An invocation of this convenience method of the form
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * Pattern.matches(regex, input);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * behaves in exactly the same way as the expression
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * Pattern.compile(regex).matcher(input).matches()
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * <p>
	 * If a pattern is to be used multiple times, compiling it once and reusing
	 * it will be more efficient than invoking this method each time.
	 * </p>
	 * 
	 * @param regex
	 *            The expression to be compiled
	 * 
	 * @param input
	 *            The character sequence to be matched
	 * 
	 * @throws PatternSyntaxException
	 *             If the expression's syntax is invalid
	 */
	public boolean matches(List<Comparable<T>> regex, List<T> input)
	{
		Pattern<T> p = new Pattern<T>(regex);
		Matcher<T> m = p.matcher(input);
		return m.matches();
	}

	/**
	 * Splits the given input sequence around matches of this pattern.
	 * 
	 * <p>
	 * The array returned by this method contains each substring of the input
	 * sequence that is terminated by another subsequence that matches this
	 * pattern or is terminated by the end of the input sequence. The substrings
	 * in the array are in the order in which they occur in the input. If this
	 * pattern does not match any subsequence of the input then the resulting
	 * array has just one element, namely the input sequence in string form.
	 * 
	 * <p>
	 * The <tt>limit</tt> parameter controls the number of times the pattern is
	 * applied and therefore affects the length of the resulting array. If the
	 * limit <i>n</i> is greater than zero then the pattern will be applied at
	 * most <i>n</i>�-�1 times, the array's length will be no greater than
	 * <i>n</i>, and the array's last entry will contain all input beyond the
	 * last matched delimiter. If <i>n</i> is non-positive then the pattern will
	 * be applied as many times as possible and the array can have any length.
	 * If <i>n</i> is zero then the pattern will be applied as many times as
	 * possible, the array can have any length, and trailing empty strings will
	 * be discarded.
	 * 
	 * <p>
	 * The input <tt>"boo:and:foo"</tt>, for example, yields the following
	 * results with these parameters:
	 * 
	 * <blockquote>
	 * <table cellpadding=1 cellspacing=0 * summary="Split examples showing regex, limit, and result">
	 * <tr>
	 * <th>
	 * <P align="left">
	 * <i>Regex����</i></th>
	 * <th>
	 * <P align="left">
	 * <i>Limit����</i></th>
	 * <th>
	 * <P align="left">
	 * <i>Result����</i></th>
	 * </tr>
	 * <tr>
	 * <td align=center>:</td>
	 * <td align=center>2</td>
	 * <td><tt>{ "boo", "and:foo" }</tt></td>
	 * </tr>
	 * <tr>
	 * <td align=center>:</td>
	 * <td align=center>5</td>
	 * <td><tt>{ "boo", "and", "foo" }</tt></td>
	 * </tr>
	 * <tr>
	 * <td align=center>:</td>
	 * <td align=center>-2</td>
	 * <td><tt>{ "boo", "and", "foo" }</tt></td>
	 * </tr>
	 * <tr>
	 * <td align=center>o</td>
	 * <td align=center>5</td>
	 * <td><tt>{ "b", "", ":and:f", "", "" }</tt></td>
	 * </tr>
	 * <tr>
	 * <td align=center>o</td>
	 * <td align=center>-2</td>
	 * <td><tt>{ "b", "", ":and:f", "", "" }</tt></td>
	 * </tr>
	 * <tr>
	 * <td align=center>o</td>
	 * <td align=center>0</td>
	 * <td><tt>{ "b", "", ":and:f" }</tt></td>
	 * </tr>
	 * </table>
	 * </blockquote>
	 * 
	 * 
	 * @param input
	 *            The character sequence to be split
	 * 
	 * @param limit
	 *            The result threshold, as described above
	 * 
	 * @return The array of strings computed by splitting the input around
	 *         matches of this pattern
	 */
	public String[] split(List<T> input, int limit)
	{
		int index = 0;
		boolean matchLimited = limit > 0;
		ArrayList<String> matchList = new ArrayList<String>();
		Matcher<T> m = matcher(input);

		// Add segments before each match found
		while (m.find())
		{
			if (!matchLimited || matchList.size() < limit - 1)
			{
				String match = input.subList(index, m.start()).toString();
				matchList.add(match);
				index = m.end();
			}
			else if (matchList.size() == limit - 1)
			{ // last one
				String match = input.subList(index, input.size()).toString();
				matchList.add(match);
				index = m.end();
			}
		}

		// If no match was found, return this
		if (index == 0) return new String[]
		{ input.toString() };

		// Add remaining segment
		if (!matchLimited || matchList.size() < limit) matchList.add(input
				.subList(index, input.size()).toString());

		// Construct result
		int resultSize = matchList.size();
		if (limit == 0) while (resultSize > 0
				&& matchList.get(resultSize - 1).equals(""))
			resultSize--;
		String[] result = new String[resultSize];
		return matchList.subList(0, resultSize).toArray(result);
	}

	/**
	 * Splits the given input sequence around matches of this pattern.
	 * 
	 * <p>
	 * This method works as if by invoking the two-argument {@link
	 * #split(java.lang.List<Comparable<T>>, int) split} method with the given
	 * input sequence and a limit argument of zero. Trailing empty strings are
	 * therefore not included in the resulting array.
	 * </p>
	 * 
	 * <p>
	 * The input <tt>"boo:and:foo"</tt>, for example, yields the following
	 * results with these expressions:
	 * 
	 * <blockquote>
	 * <table cellpadding=1 cellspacing=0 * summary="Split examples showing regex and result">
	 * <tr>
	 * <th>
	 * <P align="left">
	 * <i>Regex����</i></th>
	 * <th>
	 * <P align="left">
	 * <i>Result</i></th>
	 * </tr>
	 * <tr>
	 * <td align=center>:</td>
	 * <td><tt>{ "boo", "and", "foo" }</tt></td>
	 * </tr>
	 * <tr>
	 * <td align=center>o</td>
	 * <td><tt>{ "b", "", ":and:f" }</tt></td>
	 * </tr>
	 * </table>
	 * </blockquote>
	 * 
	 * 
	 * @param input
	 *            The character sequence to be split
	 * 
	 * @return The array of strings computed by splitting the input around
	 *         matches of this pattern
	 */
	public String[] split(List<T> input)
	{
		return split(input, 0);
	}

	/**
	 * Returns a literal pattern <code>String</code> for the specified
	 * <code>String</code>.
	 * 
	 * <p>
	 * This method produces a <code>String</code> that can be used to create a
	 * <code>Pattern</code> that would match the string <code>s</code> as if it
	 * were a literal pattern.
	 * </p>
	 * Metacharacters or escape sequences in the input sequence will be given no
	 * special meaning.
	 * 
	 * @param s
	 *            The string to be literalized
	 * @return A literal string replacement
	 * @since 1.5
	 */
	public  String quote(String s)
	{
		int slashEIndex = s.indexOf("\\E");
		if (slashEIndex == -1) return "\\Q" + s + "\\E";

		StringBuilder sb = new StringBuilder(s.length() * 2);
		sb.append("\\Q");
		slashEIndex = 0;
		int current = 0;
		while ((slashEIndex = s.indexOf("\\E", current)) != -1)
		{
			sb.append(s.substring(current, slashEIndex));
			current = slashEIndex + 2;
			sb.append("\\E\\\\E\\Q");
		}
		sb.append(s.substring(current, s.length()));
		sb.append("\\E");
		return sb.toString();
	}

	/**
	 * Recompile the Pattern instance from a stream. The original pattern string
	 * is read in and the object tree is recompiled from it.
	 */
	private void readObject(java.io.ObjectInputStream s)
			throws java.io.IOException, ClassNotFoundException
	{

		// Read in all fields
		s.defaultReadObject();

		// Initialize counts
		capturingGroupCount = 1;
		localCount = 0;

		// if length > 0, the Pattern is lazily compiled
		compiled = false;
		if (pattern.size() == 0)
		{
			root = new Start(lastAccept);
			matchRoot = lastAccept;
			compiled = true;
		}
	}

	/**
	 * This private constructor is used to create all Patterns. The pattern
	 * string and match flags are all that is needed to completely describe a
	 * Pattern. An empty pattern string results in an object tree with only a
	 * Start node and a LastNode node.
	 */
	public Pattern<T> compile()
	{

		// Reset group index count
		capturingGroupCount = 1;
		localCount = 0;

		if (pattern.size() > 0)
		{
			doCompile();
		}
		else
		{
			root = new Start(lastAccept);
			matchRoot = lastAccept;
		}
		compiled = true;
		
		return this;
	}

	/**
	 * Copies regular expression to an int array and invokes the parsing of the
	 * expression which will create the object tree.
	 */
	@SuppressWarnings("unchecked")
	private void doCompile()
	{

		// Copy pattern to int array for convenience
		// Use double zero to terminate pattern
		temp = new LinkedList<Patternable>();

		for (Comparable<T> c : pattern)
		{
			if (c instanceof Pattern.Patternable) temp.add((Patternable) c);
			else temp.add(new ElementPatternable(c));
		}
		patternLength = temp.size();
		temp.add(new PatternElementPatternable(PatternElement.END_MARKER));
		temp.add(new PatternElementPatternable(PatternElement.END_MARKER));

		// Allocate all temporary objects here.
		buffer = (Patternable[])Array.newInstance(Patternable.class,32);//new Patternable[32];
		groupNodes = (GroupHead[])Array.newInstance(GroupHead.class, 10);

		// Start recursive descent parsing
		matchRoot = expr(lastAccept);
		// Check extra pattern characters
		if (patternLength != cursor)
		{
			if (peek().patternElement() == PatternElement.CLOSE_BRACKET)
			{
				throw error("Unmatched closing ')'");
			}
			else
			{
				throw error("Unexpected internal error");
			}
		}

		printObjectTree(matchRoot);

		if (matchRoot instanceof Pattern.Begin || matchRoot instanceof Pattern.First)
		{
			root = matchRoot;
		}
		else
		{
			root = new Start(matchRoot);
		}

		// Release temporary storage
		temp = null;
		buffer = null;
		groupNodes = null;
		patternLength = 0;
	}

	/**
	 * Used to print out a subtree of the Pattern to help with debugging.
	 */
	@SuppressWarnings( "unchecked" )
	private void printObjectTree(Node node)
	{
		while (node != null)
		{
			if (node instanceof Pattern.Prolog)
			{
				System.out.println(node);
				printObjectTree(((Prolog) node).loop);
				System.out.println("**** end contents prolog loop");
			}
			else if (node instanceof Pattern.Loop)
			{
				System.out.println(node);
				printObjectTree(((Loop) node).body);
				System.out.println("**** end contents Loop body");
			}
			else if (node instanceof Pattern.Curly)
			{
				System.out.println(node);
				printObjectTree(((Curly) node).atom);
				System.out.println("**** end contents Curly body");
			}
			else if (node instanceof Pattern.GroupCurly)
			{
				System.out.println(node);
				printObjectTree(((GroupCurly) node).atom);
				System.out.println("**** end contents GroupCurly body");
			}
			else if (node instanceof Pattern.GroupTail)
			{
				System.out.println(node);
				System.out.println("Tail next is " + node.next);
				return;
			}
			else
			{
				System.out.println(node);
			}
			node = node.next;
			if (node != null) System.out.println("->next:");
			if (node == accept)
			{
				System.out.println("Accept Node");
				node = null;
			}
		}
	}

	/**
	 * Used to accumulate information about a subtree of the object graph so
	 * that optimizations can be applied to the subtree.
	 */
	 final class TreeInfo
	{
		int minLength;
		int maxLength;
		boolean maxValid;
		boolean deterministic;

		TreeInfo()
		{
			reset();
		}

		void reset()
		{
			minLength = 0;
			maxLength = 0;
			maxValid = true;
			deterministic = true;
		}
	}

	/*
	 * The following private methods are mainly used to improve the readability
	 * of the code. In order to let the Java compiler easily inline them, we
	 * should not put many assertions or error checks in them.
	 */

	/**
	 * Match next character, signal error if failed.
	 */
	private void accept(PatternElement ch, String s)
	{
		PatternElement testChar = temp.get(cursor++).patternElement();

		if (ch != testChar)
		{
			throw error(s);
		}
	}

	/**
	 * Peek the next character, and do not advance the cursor.
	 */
	private Patternable peek()
	{
		Patternable ch = temp.get(cursor);

		return ch;
	}

	/**
	 * Read the next character, and advance the cursor by one.
	 */
	private Patternable read()
	{
		Patternable ch = temp.get(cursor++);

		return ch;
	}

	/**
	 * Advance the cursor by one, and peek the next character.
	 */
	private Patternable next()
	{
		Patternable ch = temp.get(++cursor);

		return ch;
	}

	/**
	 * Read the character after the next one, and advance the cursor by two.
	 */
	private Patternable skip()
	{
		int i = cursor;
		Patternable ch = temp.get(i + 1);
		cursor = i + 2;
		return ch;
	}

	/**
	 * Unread one next character, and retreat cursor by one.
	 */
	private void unread()
	{
		cursor--;
	}

	/**
	 * Internal method used for handling all syntax errors. The pattern is
	 * displayed with a pointer to aid in locating the syntax error.
	 */
	private PatternSyntaxException error(String s)
	{
		return new PatternSyntaxException(s, pattern.toString(), cursor - 1);
	}

	/**
	 * The following methods handle the main parsing. They are sorted according
	 * to their precedence order, the lowest one first.
	 */

	/**
	 * The expression is parsed with branch nodes added for alternations. This
	 * may be called recursively to parse sub expressions that may contain
	 * alternations.
	 */
	@SuppressWarnings("unchecked")
	private Node expr(Node end)
	{
		Node prev = null;
		Node firstTail = null;
		Node branchConn = null;

		for (;;)
		{
			Node node = sequence(end);
			Node nodeTail = root; // double return
			if (prev == null)
			{
				prev = node;
				firstTail = nodeTail;
			}
			else
			{
				// Branch
				if (branchConn == null)
				{
					branchConn = new BranchConn();
					branchConn.next = end;
				}
				if (node == end)
				{
					// if the node returned from sequence() is "end"
					// we have an empty expr, set a null atom into
					// the branch to indicate to go "next" directly.
					node = null;
				}
				else
				{
					// the "tail.next" of each atom goes to branchConn
					nodeTail.next = branchConn;
				}
				if (prev instanceof Pattern.Branch)
				{
					((Branch) prev).add(node);
				}
				else
				{
					if (prev == end)
					{
						prev = null;
					}
					else
					{
						// replace the "end" with "branchConn" at its tail.next
						// when put the "prev" into the branch as the first
						// atom.
						firstTail.next = branchConn;
					}
					prev = new Branch(prev, node, branchConn);
				}
			}
			if (peek().patternElement() != PatternElement.PIPE)
			{
				return prev;
			}
			next();
		}
	}

	/**
	 * Parsing of sequences between alternations.
	 */
	private Node sequence(Node end)
	{
		Node head = null;
		Node tail = null;
		Node node = null;
		LOOP: for (;;)
		{
			PatternElement ch = peek().patternElement();
			switch (ch)
			{
				case OPEN_BRACKET:// '(':
					// Because group handles its own closure,
					// we need to treat it differently
					node = group0();
					// Check for comment or flag group
					if (node == null) continue;
					if (head == null) head = node;
					else tail.next = node;
					// Double return: Tail was returned in root
					tail = root;
					continue;
				case OPEN_SQUARE:// '[':
					node = clazz(true);
					break;
				case SLASH:// '\\':
					ch = next().patternElement();
					if (ch == PatternElement.LOWER_P
							|| ch == PatternElement.UPPER_P)
					{
						boolean comp = (ch == PatternElement.UPPER_P);
						node = family().maybeComplement(comp);
					}
					else
					{
						throw error("SLASH must be followed by LOWER_P or UPPER_P");
					}
					break;
				case CARET:// '^':
					next();
					node = new Begin();

					break;
				case DOLLAR:// '$':
					next();
					node = new Dollar();
					break;
				case DOT:// '.':
					next();
					node = new Dot();
					break;
				case PIPE:// '|':
				case CLOSE_BRACKET:// ')':
					break LOOP;
				case CLOSE_SQUARE:// ']': // Now interpreting dangling ] and }
									// as literals
				case CLOSE_BRACE:// '}':
					node = atom();
					break;
				case QUESTION:// '?':
				case STAR:// '*':
				case PLUS:// '+':
					next();
					throw error("Dangling meta character '" + ch + "'");
				case END_MARKER:// 0:
					if (cursor >= patternLength)
					{
						break LOOP;
					}
					// Fall through
				default:
					node = atom();
					break;
			}

			node = closure(node);

			if (head == null)
			{
				head = tail = node;
			}
			else
			{
				tail.next = node;
				tail = node;
			}
		}
		if (head == null)
		{
			return end;
		}
		tail.next = end;
		root = tail; // double return
		return head;
	}

	/**
	 * Parse and add a new Single or Slice.
	 */
	private Node atom()
	{
		int first = 0;
		int prev = -1;
		Patternable ch = peek();
		for (;;)
		{
			switch (ch.patternElement())
			{
				case STAR:
				case PLUS:
				case QUESTION:
				case OPEN_BRACE:
					if (first > 1)
					{
						cursor = prev; // Unwind one character
						first--;
					}
					break;
				case DOLLAR:
				case DOT:
				case CARET:
				case OPEN_BRACKET:
				case OPEN_SQUARE:
				case PIPE:
				case CLOSE_BRACKET:
					break;
				case SLASH:
					ch = next();
					if (ch.patternElement() == PatternElement.LOWER_P
							|| ch.patternElement() == PatternElement.UPPER_P)
					{ // Property
						if (first > 0)
						{ // Slice is waiting; handle it first
							unread();
							break;
						}
						else
						{ // No slice; just return the family node
							boolean comp = (ch.patternElement() == PatternElement.UPPER_P);
							ch = next(); // Consume { if present
							return family().maybeComplement(comp);
						}
					}
					else
					{
						throw error("SLASH must be followed by LOWER_P or UPPER_P");
					}
				case END_MARKER:
					if (cursor >= patternLength)
					{
						break;
					}
					// Fall through
				default:
					prev = cursor;
					append(ch, first);
					first++;
					ch = next();
					continue;
			}
			break;
		}

		if (first == 1)
		{
			return newSingle(buffer[0]);
		}
		else
		{
			return newSlice(buffer, first);
		}
	}

	private void append(Patternable ch, int len)
	{
		if (len >= buffer.length)
		{
			@SuppressWarnings("unchecked")
			Patternable[] tmp = (Patternable[])Array.newInstance(Patternable.class, len + len); //new Patternable[len + len];
			System.arraycopy(buffer, 0, tmp, 0, len);
			buffer = tmp;
		}
		System.err.println(buffer);
		System.err.println(ch);
		buffer[len] = ch;
	}

	/**
	 * Parse a character class, and return the node that matches it.
	 * 
	 * Consumes a ] on the way out if consume is true. Usually consume is true
	 * except for the case of [abc&&def] where def is a separate right hand node
	 * with "understood" brackets.
	 */
	private CharProperty clazz(boolean consume)
	{
		CharProperty prev = null;
		CharProperty node = null;
		boolean include = true;
		boolean firstInClass = true;
		Patternable ch = next();
		for (;;)
		{
			switch (ch.patternElement())
			{
				case CARET:
					// Negates if first char in a class, otherwise literal
					if (firstInClass)
					{
						if (temp.get(cursor - 1).patternElement() != PatternElement.OPEN_SQUARE) break;
						ch = next();
						include = !include;
						continue;
					}
					else
					{
						// ^ not first in class, treat as literal
						break;
					}
				case OPEN_SQUARE:
					firstInClass = false;
					node = clazz(true);
					if (prev == null) prev = node;
					else prev = union(prev, node);
					ch = peek();
					continue;
				case AND:
					firstInClass = false;
					ch = next();
					if (ch.patternElement() == PatternElement.AND)
					{
						ch = next();
						CharProperty rightNode = null;
						while (ch.patternElement() != PatternElement.CLOSE_SQUARE
								&& ch.patternElement() != PatternElement.AND)
						{
							if (ch.patternElement() == PatternElement.CLOSE_SQUARE)
							{
								if (rightNode == null) rightNode = clazz(true);
								else rightNode = union(rightNode, clazz(true));
							}
							else
							{ // abc&&def
								unread();
								rightNode = clazz(false);
							}
							ch = peek();
						}
						if (rightNode != null) node = rightNode;
						if (prev == null)
						{
							if (rightNode == null) throw error("Bad class syntax");
							else prev = rightNode;
						}
						else
						{
							prev = intersection(prev, node);
						}
					}
					else
					{
						// treat as a literal &
						unread();
						break;
					}
					continue;
				case END_MARKER:
					firstInClass = false;
					if (cursor >= patternLength) throw error("Unclosed character class");
					break;
				case CLOSE_SQUARE:
					firstInClass = false;
					if (prev != null)
					{
						if (consume) next();
						return prev;
					}
					break;
				default:
					firstInClass = false;
					break;
			}
			node = range();
			if (include)
			{
				if (prev == null)
				{
					prev = node;
				}
				else
				{
					if (prev != node) prev = union(prev, node);
				}
			}
			else
			{
				if (prev == null)
				{
					prev = node.complement();
				}
				else
				{
					if (prev != node) prev = setDifference(prev, node);
				}
			}
			ch = peek();
		}
	}

	/**
	 * Parse a single character or a character range in a character class and
	 * return its representative node.
	 */
	private CharProperty range()
	{
		Patternable ch = peek();
		ch = single();
		if (ch.patternElement() != PatternElement.END_MARKER)
		{
			if (peek().patternElement() == PatternElement.DASH)
			{
				PatternElement endRange = temp.get(cursor + 1).patternElement();
				if (endRange == PatternElement.OPEN_SQUARE)
				{
					return newSingle(ch);
				}
				if (endRange != PatternElement.CLOSE_SQUARE)
				{
					next();
					Patternable m = single();
					if (m.patternElement().compareTo(ch.patternElement()) < 0) throw error("Illegal character range");
					return rangeFor(ch, m);
				}
			}
			return newSingle(ch);
		}
		throw error("Unexpected character '" + ch.patternElement() + "'");
	}

	private Patternable single()
	{
		Patternable ch = peek();
		next();
		return ch;
	}

	/**
	 * Parses a Unicode character family and returns its representative node.
	 */
	private CharProperty family()
	{
		next();

		CharProperty cp = charPropertyNodeFor(temp.get(cursor));
		read();

		return cp;
	}

	private  class CharPropertyName extends CharProperty
	{
		Class<?> c;

		public CharPropertyName(Patternable p)
		{
			c = p.getClassToMatch();
		}

		@Override
		boolean isSatisfiedBy(T ch)
		{
			return c.isInstance(ch);
		}

	}

	/**
	 * Returns a CharProperty matching all characters in a named property.
	 */
	private CharProperty charPropertyNodeFor(Patternable name)
	{
		return new CharPropertyName(name);
	}

	/**
	 * Parses a group and returns the head node of a set of nodes that process
	 * the group. Sometimes a double return system is used where the tail is
	 * returned in root.
	 */
	@SuppressWarnings("unchecked")
	private Node group0()
	{
		boolean capturingGroup = false;
		Node head = null;
		Node tail = null;
		root = null;
		Patternable ch = next();
		if (ch.patternElement() == PatternElement.QUESTION)
		{
			ch = skip();
			switch (ch.patternElement())
			{
				case COLON: // (?:xxx) pure group
					head = createGroup(true);
					tail = root;
					head.next = expr(tail);
					break;
				case EQUALS: // (?=xxx) and (?!xxx) lookahead
				case BANG:
					head = createGroup(true);
					tail = root;
					head.next = expr(tail);
					if (ch.patternElement() == PatternElement.EQUALS)
					{
						head = tail = new Pos(head);
					}
					else
					{
						head = tail = new Neg(head);
					}
					break;
				case GREATER_THAN: // (?>xxx) independent group
					head = createGroup(true);
					tail = root;
					head.next = expr(tail);
					head = tail = new Ques(head, INDEPENDENT);
					break;
				case LESS_THAN: // (?<xxx) look behind
					ch = read();
					head = createGroup(true);
					tail = root;
					head.next = expr(tail);
					tail.next = lookbehindEnd;
					TreeInfo info = new TreeInfo();
					head.study(info);
					if (info.maxValid == false)
					{
						throw error("Look-behind group does not have "
								+ "an obvious maximum length");
					}
					if (ch.patternElement() == PatternElement.EQUALS)
					{
						head = tail = (new Behind(head, info.maxLength,
								info.minLength));
					}
					else if (ch.patternElement() == PatternElement.BANG)
					{
						head = tail = (new NotBehind(head, info.maxLength,
								info.minLength));
					}
					else
					{
						throw error("Unknown look-behind group");
					}
					break;
				case DOLLAR:
				case AT:
					throw error("Unknown group type");
				default: // (?xxx:) inlined match flags
					unread();
					ch = read();
					if (ch.patternElement() == PatternElement.CLOSE_BRACKET)
					{
						return null; // Inline modifier only
					}
					if (ch.patternElement() != PatternElement.COLON)
					{
						throw error("Unknown inline modifier");
					}
					head = createGroup(true);
					tail = root;
					head.next = expr(tail);
					break;
			}
		}
		else
		{ // (xxx) a regular group
			capturingGroup = true;
			head = createGroup(false);
			tail = root;
			head.next = expr(tail);
		}

		accept(PatternElement.CLOSE_BRACKET, "Unclosed group");

		// Check for quantifiers
		Node node = closure(head);
		if (node == head)
		{ // No closure
			root = tail;
			return node; // Dual return
		}
		if (head == tail)
		{ // Zero length assertion
			root = node;
			return node; // Dual return
		}

		if (node instanceof Pattern.Ques)
		{
			Ques ques = (Ques) node;
			if (ques.type == POSSESSIVE)
			{
				root = node;
				return node;
			}
			tail.next = new BranchConn();
			tail = tail.next;
			if (ques.type == GREEDY)
			{
				head = new Branch(head, null, tail);
			}
			else
			{ // Reluctant quantifier
				head = new Branch(null, head, tail);
			}
			root = tail;
			return head;
		}
		else if (node instanceof Pattern.Curly)
		{
			Curly curly = (Curly) node;
			if (curly.type == POSSESSIVE)
			{
				root = node;
				return node;
			}
			// Discover if the group is deterministic
			TreeInfo info = new TreeInfo();
			if (head.study(info))
			{ // Deterministic
				head = root = new GroupCurly(head.next, curly.cmin,
						curly.cmax, curly.type,
						((GroupTail) tail).localIndex,
						((GroupTail) tail).groupIndex, capturingGroup);
				return head;
			}
			else
			{ // Non-deterministic
				int temp = ((GroupHead) head).localIndex;
				Loop loop;
				if (curly.type == GREEDY) loop = new Loop(this.localCount,
						temp);
				else // Reluctant Curly
				loop = new LazyLoop(this.localCount, temp);
				Prolog prolog = new Prolog(loop);
				this.localCount += 1;
				loop.cmin = curly.cmin;
				loop.cmax = curly.cmax;
				loop.body = head;
				tail.next = loop;
				root = loop;
				return prolog; // Dual return
			}
		}
		throw error("Internal logic error");
	}

	/**
	 * Create group head and tail nodes using double return. If the group is
	 * created with anonymous true then it is a pure group and should not affect
	 * group counting.
	 */
	private Node createGroup(boolean anonymous)
	{
		int localIndex = localCount++;
		int groupIndex = 0;
		if (!anonymous) groupIndex = capturingGroupCount++;
		GroupHead head = new GroupHead(localIndex);
		root = new GroupTail(localIndex, groupIndex);
		if (!anonymous && groupIndex < 10) groupNodes[groupIndex] = head;
		return head;
	}

	 final int MAX_REPS = 0x7FFFFFFF;

	 final int GREEDY = 0;

	 final int LAZY = 1;

	 final int POSSESSIVE = 2;

	 final int INDEPENDENT = 3;

	/**
	 * Processes repetition. If the next character peeked is a quantifier then
	 * new nodes must be appended to handle the repetition. Prev could be a
	 * single or a group, so it could be a chain of nodes.
	 */
	private Node closure(Node prev)
	{
		Patternable ch = peek();
		switch (ch.patternElement())
		{
			case QUESTION:
				ch = next();
				if (ch.patternElement() == PatternElement.QUESTION)
				{
					next();
					return new Ques(prev, LAZY);
				}
				else if (ch.patternElement() == PatternElement.PLUS)
				{
					next();
					return new Ques(prev, POSSESSIVE);
				}
				return new Ques(prev, GREEDY);
			case STAR:
				ch = next();
				if (ch.patternElement() == PatternElement.QUESTION)
				{
					next();
					return new Curly(prev, 0, MAX_REPS, LAZY);
				}
				else if (ch.patternElement() == PatternElement.PLUS)
				{
					next();
					return new Curly(prev, 0, MAX_REPS, POSSESSIVE);
				}
				return new Curly(prev, 0, MAX_REPS, GREEDY);
			case PLUS:
				ch = next();
				if (ch.patternElement() == PatternElement.QUESTION)
				{
					next();
					return new Curly(prev, 1, MAX_REPS, LAZY);
				}
				else if (ch.patternElement() == PatternElement.PLUS)
				{
					next();
					return new Curly(prev, 1, MAX_REPS, POSSESSIVE);
				}
				return new Curly(prev, 1, MAX_REPS, GREEDY);
			case OPEN_BRACE:
				System.out.println("open brace");
				ch = temp.get(cursor + 1);
				System.out.println(ch.patternElement());
				if (ch.patternElement() == PatternElement.NUMBER)
				{
					skip();
					int cmin = ch.getCount();
					ch = read();
					int cmax = cmin;
					if (ch.patternElement() == PatternElement.COMMA)
					{
						ch = read();
						cmax = MAX_REPS;
						if (ch.patternElement() == PatternElement.NUMBER)
						{
							cmax = ch.getCount();
							ch = read();
						}
					}
					if (ch.patternElement() != PatternElement.CLOSE_BRACE) throw error("Unclosed counted closure");
					if (((cmin) | (cmax) | (cmax - cmin)) < 0) throw error("Illegal repetition range");
					Curly curly;
					ch = peek();
					if (ch.patternElement() == PatternElement.QUESTION)
					{
						next();
						curly = new Curly(prev, cmin, cmax, LAZY);
					}
					else if (ch.patternElement() == PatternElement.PLUS)
					{
						next();
						curly = new Curly(prev, cmin, cmax, POSSESSIVE);
					}
					else
					{
						curly = new Curly(prev, cmin, cmax, GREEDY);
					}
					return curly;
				}
				else
				{
					throw error("Illegal repetition");
				}
			default:
				return prev;
		}
	}

	/**
	 * Returns a suitably optimized, single character matcher.
	 */
	private CharProperty newSingle(final Comparable<T> ch)
	{
		return new Single(ch); // Match a given BMP character
	}

	/**
	 * Utility method for creating a string slice matcher.
	 */
	private Node newSlice(Patternable[] buf, int count)
	{
		@SuppressWarnings("unchecked")
		Patternable[] tmp = (Patternable[])Array.newInstance(Patternable.class, count); //new Patternable[count];
		for (int i = 0; i < count; i++)
		{
			tmp[i] = buf[i];
		}
		return new Slice(tmp);

	}

	/**
	 * The following classes are the building components of the object tree that
	 * represents a compiled regular expression. The object tree is made of
	 * individual elements that handle constructs in the Pattern. Each type of
	 * object knows how to match its equivalent construct with the match()
	 * method.
	 */

	/**
	 * Base class for all node classes. Subclasses should override the match()
	 * method as appropriate. This class is an accepting node, so its match()
	 * always returns true.
	 */
	class Node extends Object
	{
		Node next;

		Node()
		{
			next = accept;
		}

		/**
		 * This method implements the classic accept node.
		 */
		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			matcher.last = i;
			matcher.groups[0] = matcher.first;
			matcher.groups[1] = matcher.last;
			return true;
		}

		/**
		 * This method is good for all zero length assertions.
		 */
		boolean study(TreeInfo info)
		{
			if (next != null)
			{
				return next.study(info);
			}
			else
			{
				return info.deterministic;
			}
		}
	}

	 class LastNode extends Node
	{
		/**
		 * This method implements the classic accept nodde with the addition of
		 * a check to see if the match occurred using all of the input.
		 */
		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			if (matcher.acceptMode == Matcher.ENDANCHOR && i != matcher.to) return false;
			matcher.last = i;
			matcher.groups[0] = matcher.first;
			matcher.groups[1] = matcher.last;
			return true;
		}
	}

	/**
	 * Used for REs that can start anywhere within the input string. This
	 * basically tries to match repeatedly at each spot in the input string,
	 * moving forward after each try. An anchored search or a BnM will bypass
	 * this node completely.
	 */
	 class Start extends Node
	{
		int minLength;

		Start(Node node)
		{
			this.next = node;
			TreeInfo info = new TreeInfo();
			next.study(info);
			minLength = info.minLength;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			if (i > matcher.to - minLength)
			{
				matcher.hitEnd = true;
				return false;
			}
			boolean ret = false;
			int guard = matcher.to - minLength;
			for (; i <= guard; i++)
			{
				if (ret = next.match(matcher, i, seq)) break;
				if (i == guard) matcher.hitEnd = true;
			}
			if (ret)
			{
				matcher.first = i;
				matcher.groups[0] = matcher.first;
				matcher.groups[1] = matcher.last;
			}
			return ret;
		}

		boolean study(TreeInfo info)
		{
			next.study(info);
			info.maxValid = false;
			info.deterministic = false;
			return false;
		}
	}

	/**
	 * Node to anchor at the beginning of input. This object implements the
	 * match for a \A sequence, and the caret anchor will use this if not in
	 * multiline mode.
	 */
	 final class Begin extends Node
	{
		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int fromIndex = (matcher.anchoringBounds) ? matcher.from : 0;
			if (i == fromIndex && next.match(matcher, i, seq))
			{
				matcher.first = i;
				matcher.groups[0] = i;
				matcher.groups[1] = matcher.last;
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	/**
	 * Node to anchor at the end of input. This is the absolute end, so this
	 * should not match at the last newline before the end as $ will.
	 */
	 final class End extends Node
	{
		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int endIndex = (matcher.anchoringBounds) ? matcher.to : matcher
					.getTextLength();
			if (i == endIndex)
			{
				matcher.hitEnd = true;
				return next.match(matcher, i, seq);
			}
			return false;
		}
	}

	/**
	 * Node to anchor at the beginning of a line. This is essentially the object
	 * to match for the multiline ^.
	 */
	 final class Caret extends Node
	{
		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int startIndex = matcher.from;
			int endIndex = matcher.to;
			if (!matcher.anchoringBounds)
			{
				startIndex = 0;
				endIndex = matcher.getTextLength();
			}
			// Perl does not match ^ at end of input even after newline
			if (i == endIndex)
			{
				matcher.hitEnd = true;
				return false;
			}
			if (i > startIndex)
			{
				@SuppressWarnings("unchecked")
				PatternElement ch = (seq.get(i - 1) instanceof Pattern.PatternElementPatternable ? ((PatternElementPatternable) seq
						.get(i - 1)).patternElement() : PatternElement.OTHER);
				if (ch != PatternElement.NEW_LINE)
				{
					return false;
				}
			}
			return next.match(matcher, i, seq);
		}
	}

	/**
	 * Node to match the location where the last match ended. This is used for
	 * the \G construct.
	 */
	 final class LastMatch extends Node
	{
		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			if (i != matcher.oldLast) return false;
			return next.match(matcher, i, seq);
		}
	}

	/**
	 * Node to anchor at the end of a line or the end of input based on the
	 * multiline mode.
	 * 
	 * When not in multiline mode, the $ can only match at the very end of the
	 * input, unless the input ends in a line terminator in which it matches
	 * right before the last line terminator.
	 * 
	 * Note that \r\n is considered an atomic line terminator.
	 * 
	 * Like ^ the $ operator matches at a position, it does not match the line
	 * terminators themselves.
	 */
	 final class Dollar extends Node
	{

		Dollar()
		{
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int endIndex = (matcher.anchoringBounds) ? matcher.to : matcher
					.getTextLength();

			if (i < endIndex - 2) return false;
			if (i == endIndex - 2)
			{
				@SuppressWarnings("unchecked")
				PatternElement ch = (seq.get(i) instanceof Pattern.PatternElementPatternable ? ((PatternElementPatternable) seq
						.get(i)).patternElement() : PatternElement.OTHER);
				if (ch != PatternElement.NEW_LINE) return false;
			}

			// Matches before any line terminator; also matches at the
			// end of input
			// Before line terminator:
			// If multiline, we match here no matter what
			// If not multiline, fall through so that the end
			// is marked as hit; this must be a /r/n or a /n
			// at the very end so the end was hit; more input
			// could make this not match here
			if (i < endIndex)
			{
				@SuppressWarnings("unchecked")
				PatternElement ch = (seq.get(i) instanceof Pattern.PatternElementPatternable ? ((PatternElementPatternable) seq
						.get(i)).patternElement() : PatternElement.OTHER);
				if (ch != PatternElement.NEW_LINE)
				{
					return false;
				}
			}
			// Matched at current end so hit end
			matcher.hitEnd = true;
			// If a $ matches because of end of input, then more input
			// could cause it to fail!
			matcher.requireEnd = true;
			return next.match(matcher, i, seq);
		}

		boolean study(TreeInfo info)
		{
			next.study(info);
			return info.deterministic;
		}
	}

	/**
	 * Abstract node class to match one character satisfying some boolean
	 * property.
	 */
	private abstract class CharProperty extends Node
	{
		abstract boolean isSatisfiedBy(T ch);

		CharProperty complement()
		{
			return new CharProperty() {
				boolean isSatisfiedBy(T ch)
				{
					return !CharProperty.this.isSatisfiedBy(ch);
				}
			};
		}

		CharProperty maybeComplement(boolean complement)
		{
			return complement ? complement() : this;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			if (i < matcher.to)
			{
				T ch = seq.get(i);
				return isSatisfiedBy(ch) && next.match(matcher, i + 1, seq);
			}
			else
			{
				matcher.hitEnd = true;
				return false;
			}
		}

		boolean study(TreeInfo info)
		{
			info.minLength++;
			info.maxLength++;
			return next.study(info);
		}
	}

	/**
	 * Optimized version of CharProperty that works only for properties never
	 * satisfied by Supplementary characters.
	 */
	private  abstract class BmpCharProperty extends CharProperty
	{
		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			if (i < matcher.to)
			{
				return isSatisfiedBy(seq.get(i))
						&& next.match(matcher, i + 1, seq);
			}
			else
			{
				matcher.hitEnd = true;
				return false;
			}
		}
	}

	/**
	 * Optimization -- matches a given BMP character
	 */
	 final class Single extends BmpCharProperty
	{
		final Comparable<T> c;

		Single(Comparable<T> c)
		{
			this.c = c;
		}

		boolean isSatisfiedBy(T ch)
		{
			return c.compareTo(ch) == 0;
		}

		public String toString()
		{
			return "Single(" + c + ")";
		}
	}

	/**
	 * Base class for all Slice nodes
	 */
	 class SliceNode extends Node
	{
		Comparable<T>[] buffer;

		SliceNode(Comparable<T>[] buf)
		{
			buffer = buf;
		}

		boolean study(TreeInfo info)
		{
			info.minLength += buffer.length;
			info.maxLength += buffer.length;
			return next.study(info);
		}
	}

	/**
	 * Node class for a case sensitive/BMP-only sequence of literal characters.
	 */
	 final class Slice extends SliceNode
	{
		Slice(Comparable<T>[] buf)
		{
			super(buf);
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			Comparable<T>[] buf = buffer;
			int len = buf.length;
			for (int j = 0; j < len; j++)
			{
				if ((i + j) >= matcher.to)
				{
					matcher.hitEnd = true;
					return false;
				}
				if (buf[j].compareTo(seq.get(i + j)) != 0) return false;
			}
			return next.match(matcher, i + len, seq);
		}
	}

	private boolean inRange(Comparable<T> lower, T ch,
			Comparable<T> upper)
	{

		return lower.compareTo(ch) <= 0 && 0 <= upper.compareTo(ch);
	}

	/**
	 * Returns node for matching characters within an explicit value range.
	 */
	private CharProperty rangeFor(final Comparable<T> lower,
			final Comparable<T> upper)
	{
		return new CharProperty() {
			boolean isSatisfiedBy(T ch)
			{

				return inRange(lower, ch, upper);
			}
		};
	}

	/**
	 * Implements the Unicode category ALL and the dot metacharacter when in
	 * dotall mode.
	 */
	 final class All extends CharProperty
	{
		boolean isSatisfiedBy(T ch)
		{
			return true;
		}
	}

	/**
	 * Node class for the dot metacharacter when dotall is not enabled.
	 */
	 final class Dot extends CharProperty
	{
		@SuppressWarnings("unchecked")
		boolean isSatisfiedBy(T ch)
		{
			return !(ch instanceof Pattern.PatternElementPatternable && ((PatternElementPatternable) ch)
					.patternElement() == PatternElement.NEW_LINE);
		}
	}

	/**
	 * The 0 or 1 quantifier. This one class implements all three types.
	 */
	 final class Ques extends Node
	{
		Node atom;
		int type;

		Ques(Node node, int type)
		{
			this.atom = node;
			this.type = type;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			switch (type)
			{
				case GREEDY:
					return (atom.match(matcher, i, seq) && next.match(matcher,
							matcher.last, seq)) || next.match(matcher, i, seq);
				case LAZY:
					return next.match(matcher, i, seq)
							|| (atom.match(matcher, i, seq) && next.match(
									matcher, matcher.last, seq));
				case POSSESSIVE:
					if (atom.match(matcher, i, seq)) i = matcher.last;
					return next.match(matcher, i, seq);
				default:
					return atom.match(matcher, i, seq)
							&& next.match(matcher, matcher.last, seq);
			}
		}

		boolean study(TreeInfo info)
		{
			if (type != INDEPENDENT)
			{
				int minL = info.minLength;
				atom.study(info);
				info.minLength = minL;
				info.deterministic = false;
				return next.study(info);
			}
			else
			{
				atom.study(info);
				return next.study(info);
			}
		}

		public String toString()
		{
			return super.toString() + " " + atom.toString();
		}

	}

	/**
	 * Handles the curly-brace style repetition with a specified minimum and
	 * maximum occurrences. The * quantifier is handled as a special case. This
	 * class handles the three types.
	 */
	 final class Curly extends Node
	{
		Node atom;
		int type;
		int cmin;
		int cmax;

		Curly(Node node, int cmin, int cmax, int type)
		{
			this.atom = node;
			this.type = type;
			this.cmin = cmin;
			this.cmax = cmax;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int j;
			for (j = 0; j < cmin; j++)
			{
				if (atom.match(matcher, i, seq))
				{
					i = matcher.last;
					continue;
				}
				return false;
			}
			if (type == GREEDY) return match0(matcher, i, j, seq);
			else if (type == LAZY) return match1(matcher, i, j, seq);
			else return match2(matcher, i, j, seq);
		}

		// Greedy match.
		// i is the index to start matching at
		// j is the number of atoms that have matched
		boolean match0(Matcher<T> matcher, int i, int j, List<T> seq)
		{
			if (j >= cmax)
			{
				// We have matched the maximum... continue with the rest of
				// the regular expression
				return next.match(matcher, i, seq);
			}
			int backLimit = j;
			while (atom.match(matcher, i, seq))
			{
				// k is the length of this match
				int k = matcher.last - i;
				if (k == 0) // Zero length match
				break;
				// Move up index and number matched
				i = matcher.last;
				j++;
				// We are greedy so match as many as we can
				while (j < cmax)
				{
					if (!atom.match(matcher, i, seq)) break;
					if (i + k != matcher.last)
					{
						if (match0(matcher, matcher.last, j + 1, seq)) return true;
						break;
					}
					i += k;
					j++;
				}
				// Handle backing off if match fails
				while (j >= backLimit)
				{
					if (next.match(matcher, i, seq)) return true;
					i -= k;
					j--;
				}
				return false;
			}
			return next.match(matcher, i, seq);
		}

		// Reluctant match. At this point, the minimum has been satisfied.
		// i is the index to start matching at
		// j is the number of atoms that have matched
		boolean match1(Matcher<T> matcher, int i, int j, List<T> seq)
		{
			for (;;)
			{
				// Try finishing match without consuming any more
				if (next.match(matcher, i, seq)) return true;
				// At the maximum, no match found
				if (j >= cmax) return false;
				// Okay, must try one more atom
				if (!atom.match(matcher, i, seq)) return false;
				// If we haven't moved forward then must break out
				if (i == matcher.last) return false;
				// Move up index and number matched
				i = matcher.last;
				j++;
			}
		}

		boolean match2(Matcher<T> matcher, int i, int j, List<T> seq)
		{
			for (; j < cmax; j++)
			{
				if (!atom.match(matcher, i, seq)) break;
				if (i == matcher.last) break;
				i = matcher.last;
			}
			return next.match(matcher, i, seq);
		}

		boolean study(TreeInfo info)
		{
			// Save original info
			int minL = info.minLength;
			int maxL = info.maxLength;
			boolean maxV = info.maxValid;
			boolean detm = info.deterministic;
			info.reset();

			atom.study(info);

			int temp = info.minLength * cmin + minL;
			if (temp < minL)
			{
				temp = 0xFFFFFFF; // arbitrary large number
			}
			info.minLength = temp;

			if (maxV & info.maxValid)
			{
				temp = info.maxLength * cmax + maxL;
				info.maxLength = temp;
				if (temp < maxL)
				{
					info.maxValid = false;
				}
			}
			else
			{
				info.maxValid = false;
			}

			if (info.deterministic && cmin == cmax) info.deterministic = detm;
			else info.deterministic = false;

			return next.study(info);
		}
	}

	/**
	 * Handles the curly-brace style repetition with a specified minimum and
	 * maximum occurrences in deterministic cases. This is an iterative
	 * optimization over the Prolog and Loop system which would handle this in a
	 * recursive way. The * quantifier is handled as a special case. If capture
	 * is true then this class saves group settings and ensures that groups are
	 * unset when backing off of a group match.
	 */
	 final class GroupCurly extends Node
	{
		Node atom;
		int type;
		int cmin;
		int cmax;
		int localIndex;
		int groupIndex;
		boolean capture;

		GroupCurly(Node node, int cmin, int cmax, int type, int local,
				int group, boolean capture)
		{
			this.atom = node;
			this.type = type;
			this.cmin = cmin;
			this.cmax = cmax;
			this.localIndex = local;
			this.groupIndex = group;
			this.capture = capture;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int[] groups = matcher.groups;
			int[] locals = matcher.locals;
			int save0 = locals[localIndex];
			int save1 = 0;
			int save2 = 0;

			if (capture)
			{
				save1 = groups[groupIndex];
				save2 = groups[groupIndex + 1];
			}

			// Notify GroupTail there is no need to setup group info
			// because it will be set here
			locals[localIndex] = -1;

			boolean ret = true;
			for (int j = 0; j < cmin; j++)
			{
				if (atom.match(matcher, i, seq))
				{
					if (capture)
					{
						groups[groupIndex] = i;
						groups[groupIndex + 1] = matcher.last;
					}
					i = matcher.last;
				}
				else
				{
					ret = false;
					break;
				}
			}
			if (ret)
			{
				if (type == GREEDY)
				{
					ret = match0(matcher, i, cmin, seq);
				}
				else if (type == LAZY)
				{
					ret = match1(matcher, i, cmin, seq);
				}
				else
				{
					ret = match2(matcher, i, cmin, seq);
				}
			}
			if (!ret)
			{
				locals[localIndex] = save0;
				if (capture)
				{
					groups[groupIndex] = save1;
					groups[groupIndex + 1] = save2;
				}
			}
			return ret;
		}

		// Aggressive group match
		boolean match0(Matcher<T> matcher, int i, int j, List<T> seq)
		{
			int[] groups = matcher.groups;
			int save0 = 0;
			int save1 = 0;
			if (capture)
			{
				save0 = groups[groupIndex];
				save1 = groups[groupIndex + 1];
			}
			for (;;)
			{
				if (j >= cmax) break;
				if (!atom.match(matcher, i, seq)) break;
				int k = matcher.last - i;
				if (k <= 0)
				{
					if (capture)
					{
						groups[groupIndex] = i;
						groups[groupIndex + 1] = i + k;
					}
					i = i + k;
					break;
				}
				for (;;)
				{
					if (capture)
					{
						groups[groupIndex] = i;
						groups[groupIndex + 1] = i + k;
					}
					i = i + k;
					if (++j >= cmax) break;
					if (!atom.match(matcher, i, seq)) break;
					if (i + k != matcher.last)
					{
						if (match0(matcher, i, j, seq)) return true;
						break;
					}
				}
				while (j > cmin)
				{
					if (next.match(matcher, i, seq))
					{
						if (capture)
						{
							groups[groupIndex + 1] = i;
							groups[groupIndex] = i - k;
						}
						i = i - k;
						return true;
					}
					// backing off
					if (capture)
					{
						groups[groupIndex + 1] = i;
						groups[groupIndex] = i - k;
					}
					i = i - k;
					j--;
				}
				break;
			}
			if (capture)
			{
				groups[groupIndex] = save0;
				groups[groupIndex + 1] = save1;
			}
			return next.match(matcher, i, seq);
		}

		// Reluctant matching
		boolean match1(Matcher<T> matcher, int i, int j, List<T> seq)
		{
			for (;;)
			{
				if (next.match(matcher, i, seq)) return true;
				if (j >= cmax) return false;
				if (!atom.match(matcher, i, seq)) return false;
				if (i == matcher.last) return false;
				if (capture)
				{
					matcher.groups[groupIndex] = i;
					matcher.groups[groupIndex + 1] = matcher.last;
				}
				i = matcher.last;
				j++;
			}
		}

		// Possessive matching
		boolean match2(Matcher<T> matcher, int i, int j, List<T> seq)
		{
			for (; j < cmax; j++)
			{
				if (!atom.match(matcher, i, seq))
				{
					break;
				}
				if (capture)
				{
					matcher.groups[groupIndex] = i;
					matcher.groups[groupIndex + 1] = matcher.last;
				}
				if (i == matcher.last)
				{
					break;
				}
				i = matcher.last;
			}
			return next.match(matcher, i, seq);
		}

		boolean study(TreeInfo info)
		{
			// Save original info
			int minL = info.minLength;
			int maxL = info.maxLength;
			boolean maxV = info.maxValid;
			boolean detm = info.deterministic;
			info.reset();

			atom.study(info);

			int temp = info.minLength * cmin + minL;
			if (temp < minL)
			{
				temp = 0xFFFFFFF; // Arbitrary large number
			}
			info.minLength = temp;

			if (maxV & info.maxValid)
			{
				temp = info.maxLength * cmax + maxL;
				info.maxLength = temp;
				if (temp < maxL)
				{
					info.maxValid = false;
				}
			}
			else
			{
				info.maxValid = false;
			}

			if (info.deterministic && cmin == cmax)
			{
				info.deterministic = detm;
			}
			else
			{
				info.deterministic = false;
			}

			return next.study(info);
		}
	}

	/**
	 * A Guard node at the end of each atom node in a Branch. It serves the
	 * purpose of chaining the "match" operation to "next" but not the "study",
	 * so we can collect the TreeInfo of each atom node without including the
	 * TreeInfo of the "next".
	 */
	 final class BranchConn extends Node
	{
		BranchConn()
		{
		};

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			return next.match(matcher, i, seq);
		}

		boolean study(TreeInfo info)
		{
			return info.deterministic;
		}
	}

	/**
	 * Handles the branching of alternations. Note this is also used for the ?
	 * quantifier to branch between the case where it matches once and where it
	 * does not occur.
	 */
	 final class Branch extends Node
	{
		@SuppressWarnings("unchecked")
		Node[] atoms = (Node[])Array.newInstance(Node.class, 2); //new Node[2];
		int size = 2;
		Node conn;

		Branch(Node first, Node second, Node branchConn)
		{
			conn = branchConn;
			atoms[0] = first;
			atoms[1] = second;
		}

		void add(Node node)
		{
			if (size >= atoms.length)
			{
				@SuppressWarnings("unchecked")
				Node[] tmp = (Node[])Array.newInstance(Node.class,atoms.length * 2);
				System.arraycopy(atoms, 0, tmp, 0, atoms.length);
				atoms = tmp;
			}
			atoms[size++] = node;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			for (int n = 0; n < size; n++)
			{
				if (atoms[n] == null)
				{
					if (conn.next.match(matcher, i, seq)) return true;
				}
				else if (atoms[n].match(matcher, i, seq))
				{
					return true;
				}
			}
			return false;
		}

		boolean study(TreeInfo info)
		{
			int minL = info.minLength;
			int maxL = info.maxLength;
			boolean maxV = info.maxValid;

			int minL2 = Integer.MAX_VALUE; // arbitrary large enough num
			int maxL2 = -1;
			for (int n = 0; n < size; n++)
			{
				info.reset();
				if (atoms[n] != null) atoms[n].study(info);
				minL2 = Math.min(minL2, info.minLength);
				maxL2 = Math.max(maxL2, info.maxLength);
				maxV = (maxV & info.maxValid);
			}

			minL += minL2;
			maxL += maxL2;

			info.reset();
			conn.next.study(info);

			info.minLength += minL;
			info.maxLength += maxL;
			info.maxValid &= maxV;
			info.deterministic = false;
			return false;
		}
	}

	/**
	 * The GroupHead saves the location where the group begins in the locals and
	 * restores them when the match is done.
	 * 
	 * The matchRef is used when a reference to this group is accessed later in
	 * the expression. The locals will have a negative value in them to indicate
	 * that we do not want to unset the group if the reference doesn't match.
	 */
	 final class GroupHead extends Node
	{
		int localIndex;

		GroupHead(int localCount)
		{
			localIndex = localCount;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int save = matcher.locals[localIndex];
			matcher.locals[localIndex] = i;
			boolean ret = next.match(matcher, i, seq);
			matcher.locals[localIndex] = save;
			return ret;
		}

		boolean matchRef(Matcher<T> matcher, int i, List<T> seq)
		{
			int save = matcher.locals[localIndex];
			matcher.locals[localIndex] = ~i; // HACK
			boolean ret = next.match(matcher, i, seq);
			matcher.locals[localIndex] = save;
			return ret;
		}
	}

	/**
	 * Recursive reference to a group in the regular expression. It calls
	 * matchRef because if the reference fails to match we would not unset the
	 * group.
	 */
	 final class GroupRef extends Node
	{
		GroupHead head;

		GroupRef(GroupHead head)
		{
			this.head = head;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			return head.matchRef(matcher, i, seq)
					&& next.match(matcher, matcher.last, seq);
		}

		boolean study(TreeInfo info)
		{
			info.maxValid = false;
			info.deterministic = false;
			return next.study(info);
		}
	}

	/**
	 * The GroupTail handles the setting of group beginning and ending locations
	 * when groups are successfully matched. It must also be able to unset
	 * groups that have to be backed off of.
	 * 
	 * The GroupTail node is also used when a previous group is referenced, and
	 * in that case no group information needs to be set.
	 */
	 final class GroupTail extends Node
	{
		int localIndex;
		int groupIndex;

		GroupTail(int localCount, int groupCount)
		{
			localIndex = localCount;
			groupIndex = groupCount + groupCount;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int tmp = matcher.locals[localIndex];
			if (tmp >= 0)
			{ // This is the normal group case.
				// Save the group so we can unset it if it
				// backs off of a match.
				int groupStart = matcher.groups[groupIndex];
				int groupEnd = matcher.groups[groupIndex + 1];

				matcher.groups[groupIndex] = tmp;
				matcher.groups[groupIndex + 1] = i;
				if (next.match(matcher, i, seq))
				{
					return true;
				}
				matcher.groups[groupIndex] = groupStart;
				matcher.groups[groupIndex + 1] = groupEnd;
				return false;
			}
			else
			{
				// This is a group reference case. We don't need to save any
				// group info because it isn't really a group.
				matcher.last = i;
				return true;
			}
		}
	}

	/**
	 * This sets up a loop to handle a recursive quantifier structure.
	 */
	 final class Prolog extends Node
	{
		Loop loop;

		Prolog(Loop loop)
		{
			this.loop = loop;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			return loop.matchInit(matcher, i, seq);
		}

		boolean study(TreeInfo info)
		{
			return loop.study(info);
		}
	}

	/**
	 * Handles the repetition count for a greedy Curly. The matchInit is called
	 * from the Prolog to save the index of where the group beginning is stored.
	 * A zero length group check occurs in the normal match but is skipped in
	 * the matchInit.
	 */
	 class Loop extends Node
	{
		Node body;
		int countIndex; // local count index in matcher locals
		int beginIndex; // group beginning index
		int cmin, cmax;

		Loop(int countIndex, int beginIndex)
		{
			this.countIndex = countIndex;
			this.beginIndex = beginIndex;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			// Avoid infinite loop in zero-length case.
			if (i > matcher.locals[beginIndex])
			{
				int count = matcher.locals[countIndex];

				// This block is for before we reach the minimum
				// iterations required for the loop to match
				if (count < cmin)
				{
					matcher.locals[countIndex] = count + 1;
					boolean b = body.match(matcher, i, seq);
					// If match failed we must backtrack, so
					// the loop count should NOT be incremented
					if (!b) matcher.locals[countIndex] = count;
					// Return success or failure since we are under
					// minimum
					return b;
				}
				// This block is for after we have the minimum
				// iterations required for the loop to match
				if (count < cmax)
				{
					matcher.locals[countIndex] = count + 1;
					boolean b = body.match(matcher, i, seq);
					// If match failed we must backtrack, so
					// the loop count should NOT be incremented
					if (!b) matcher.locals[countIndex] = count;
					else return true;
				}
			}
			return next.match(matcher, i, seq);
		}

		boolean matchInit(Matcher<T> matcher, int i, List<T> seq)
		{
			int save = matcher.locals[countIndex];
			boolean ret = false;
			if (0 < cmin)
			{
				matcher.locals[countIndex] = 1;
				ret = body.match(matcher, i, seq);
			}
			else if (0 < cmax)
			{
				matcher.locals[countIndex] = 1;
				ret = body.match(matcher, i, seq);
				if (ret == false) ret = next.match(matcher, i, seq);
			}
			else
			{
				ret = next.match(matcher, i, seq);
			}
			matcher.locals[countIndex] = save;
			return ret;
		}

		boolean study(TreeInfo info)
		{
			info.maxValid = false;
			info.deterministic = false;
			return false;
		}
	}

	/**
	 * Handles the repetition count for a reluctant Curly. The matchInit is
	 * called from the Prolog to save the index of where the group beginning is
	 * stored. A zero length group check occurs in the normal match but is
	 * skipped in the matchInit.
	 */
	 final class LazyLoop extends Loop
	{
		LazyLoop(int countIndex, int beginIndex)
		{
			super(countIndex, beginIndex);
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			// Check for zero length group
			if (i > matcher.locals[beginIndex])
			{
				int count = matcher.locals[countIndex];
				if (count < cmin)
				{
					matcher.locals[countIndex] = count + 1;
					boolean result = body.match(matcher, i, seq);
					// If match failed we must backtrack, so
					// the loop count should NOT be incremented
					if (!result) matcher.locals[countIndex] = count;
					return result;
				}
				if (next.match(matcher, i, seq)) return true;
				if (count < cmax)
				{
					matcher.locals[countIndex] = count + 1;
					boolean result = body.match(matcher, i, seq);
					// If match failed we must backtrack, so
					// the loop count should NOT be incremented
					if (!result) matcher.locals[countIndex] = count;
					return result;
				}
				return false;
			}
			return next.match(matcher, i, seq);
		}

		boolean matchInit(Matcher<T> matcher, int i, List<T> seq)
		{
			int save = matcher.locals[countIndex];
			boolean ret = false;
			if (0 < cmin)
			{
				matcher.locals[countIndex] = 1;
				ret = body.match(matcher, i, seq);
			}
			else if (next.match(matcher, i, seq))
			{
				ret = true;
			}
			else if (0 < cmax)
			{
				matcher.locals[countIndex] = 1;
				ret = body.match(matcher, i, seq);
			}
			matcher.locals[countIndex] = save;
			return ret;
		}

		boolean study(TreeInfo info)
		{
			info.maxValid = false;
			info.deterministic = false;
			return false;
		}
	}

	/**
	 * Refers to a group in the regular expression. Attempts to match whatever
	 * the group referred to last matched.
	 */
	 class BackRef extends Node
	{
		int groupIndex;

		BackRef(int groupCount)
		{
			super();
			groupIndex = groupCount + groupCount;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int j = matcher.groups[groupIndex];
			int k = matcher.groups[groupIndex + 1];

			int groupSize = k - j;

			// If the referenced group didn't match, neither can this
			if (j < 0) return false;

			// If there isn't enough input left no match
			if (i + groupSize > matcher.to)
			{
				matcher.hitEnd = true;
				return false;
			}

			// Check each new char to make sure it matches what the group
			// referenced matched last time around
			for (int index = 0; index < groupSize; index++)
				if (seq.get(i + index) != seq.get(j + index)) return false;

			return next.match(matcher, i + groupSize, seq);
		}

		boolean study(TreeInfo info)
		{
			info.maxValid = false;
			return next.study(info);
		}
	}

	//  class CIBackRef extends Node {
	// int groupIndex;
	// boolean doUnicodeCase;
	// CIBackRef(int groupCount, boolean doUnicodeCase) {
	// super();
	// groupIndex = groupCount + groupCount;
	// this.doUnicodeCase = doUnicodeCase;
	// }
	// boolean match(Matcher matcher, int i, List<?> seq) {
	// int j = matcher.groups[groupIndex];
	// int k = matcher.groups[groupIndex+1];
	//
	// int groupSize = k - j;
	//
	// // If the referenced group didn't match, neither can this
	// if (j < 0)
	// return false;
	//
	// // If there isn't enough input left no match
	// if (i + groupSize > matcher.to) {
	// matcher.hitEnd = true;
	// return false;
	// }
	//
	// // Check each new char to make sure it matches what the group
	// // referenced matched last time around
	// int x = i;
	// for (int index=0; index<groupSize; index++) {
	// PatternElement c1 = seq.get(x).patternElement();
	// PatternElement c2 = seq.get(j).patternElement();
	// if (c1 != c2) {
	// if (doUnicodeCase) {
	// int cc1 = Character.toUpperCase(c1);
	// int cc2 = Character.toUpperCase(c2);
	// if (cc1 != cc2 &&
	// Character.toLowerCase(cc1) !=
	// Character.toLowerCase(cc2))
	// return false;
	// } else {
	// if (ASCII.toLower(c1) != ASCII.toLower(c2))
	// return false;
	// }
	// }
	// x += Character.charCount(c1);
	// j += Character.charCount(c2);
	// }
	//
	// return next.match(matcher, i+groupSize, seq);
	// }
	// boolean study(TreeInfo info) {
	// info.maxValid = false;
	// return next.study(info);
	// }
	// }

	/**
	 * Searches until the next instance of its atom. This is useful for finding
	 * the atom efficiently without passing an instance of it (greedy problem)
	 * and without a lot of wasted search time (reluctant problem).
	 */
	 final class First extends Node
	{
		Node atom;

		First(Node node)
		{
			this.atom = node;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			for (;;)
			{
				if (i > matcher.to)
				{
					matcher.hitEnd = true;
					return false;
				}
				if (atom.match(matcher, i, seq))
				{
					return next.match(matcher, matcher.last, seq);
				}
				i++;
				matcher.first++;
			}
		}

		boolean study(TreeInfo info)
		{
			atom.study(info);
			info.maxValid = false;
			info.deterministic = false;
			return next.study(info);
		}
	}

	 final class Conditional extends Node
	{
		Node cond, yes, not;

		Conditional(Node cond, Node yes, Node not)
		{
			this.cond = cond;
			this.yes = yes;
			this.not = not;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			if (cond.match(matcher, i, seq))
			{
				return yes.match(matcher, i, seq);
			}
			else
			{
				return not.match(matcher, i, seq);
			}
		}

		boolean study(TreeInfo info)
		{
			int minL = info.minLength;
			int maxL = info.maxLength;
			boolean maxV = info.maxValid;
			info.reset();
			yes.study(info);

			int minL2 = info.minLength;
			int maxL2 = info.maxLength;
			boolean maxV2 = info.maxValid;
			info.reset();
			not.study(info);

			info.minLength = minL + Math.min(minL2, info.minLength);
			info.maxLength = maxL + Math.max(maxL2, info.maxLength);
			info.maxValid = (maxV & maxV2 & info.maxValid);
			info.deterministic = false;
			return next.study(info);
		}
	}

	/**
	 * Zero width positive lookahead.
	 */
	 final class Pos extends Node
	{
		Node cond;

		Pos(Node cond)
		{
			this.cond = cond;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int savedTo = matcher.to;
			boolean conditionMatched = false;

			// Relax transparent region boundaries for lookahead
			if (matcher.transparentBounds) matcher.to = matcher.getTextLength();
			try
			{
				conditionMatched = cond.match(matcher, i, seq);
			}
			finally
			{
				// Reinstate region boundaries
				matcher.to = savedTo;
			}
			return conditionMatched && next.match(matcher, i, seq);
		}
	}

	/**
	 * Zero width negative lookahead.
	 */
	 final class Neg extends Node
	{
		Node cond;

		Neg(Node cond)
		{
			this.cond = cond;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int savedTo = matcher.to;
			boolean conditionMatched = false;

			// Relax transparent region boundaries for lookahead
			if (matcher.transparentBounds) matcher.to = matcher.getTextLength();
			try
			{
				if (i < matcher.to)
				{
					conditionMatched = !cond.match(matcher, i, seq);
				}
				else
				{
					// If a negative lookahead succeeds then more input
					// could cause it to fail!
					matcher.requireEnd = true;
					conditionMatched = !cond.match(matcher, i, seq);
				}
			}
			finally
			{
				// Reinstate region boundaries
				matcher.to = savedTo;
			}
			return conditionMatched && next.match(matcher, i, seq);
		}
	}

	/**
	 * For use with lookbehinds; matches the position where the lookbehind was
	 * encountered.
	 */
	 Node lookbehindEnd = new Node() {
		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			return i == matcher.lookbehindTo;
		}
	};

	/**
	 * Zero width positive lookbehind.
	 */
	 class Behind extends Node
	{
		Node cond;
		int rmax, rmin;

		Behind(Node cond, int rmax, int rmin)
		{
			this.cond = cond;
			this.rmax = rmax;
			this.rmin = rmin;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int savedFrom = matcher.from;
			boolean conditionMatched = false;
			int startIndex = (!matcher.transparentBounds) ? matcher.from : 0;
			int from = Math.max(i - rmax, startIndex);
			// Set end boundary
			int savedLBT = matcher.lookbehindTo;
			matcher.lookbehindTo = i;
			// Relax transparent region boundaries for lookbehind
			if (matcher.transparentBounds) matcher.from = 0;
			for (int j = i - rmin; !conditionMatched && j >= from; j--)
			{
				conditionMatched = cond.match(matcher, j, seq);
			}
			matcher.from = savedFrom;
			matcher.lookbehindTo = savedLBT;
			return conditionMatched && next.match(matcher, i, seq);
		}
	}

	/**
	 * Zero width negative lookbehind.
	 */
	 class NotBehind extends Node
	{
		Node cond;
		int rmax, rmin;

		NotBehind(Node cond, int rmax, int rmin)
		{
			this.cond = cond;
			this.rmax = rmax;
			this.rmin = rmin;
		}

		boolean match(Matcher<T> matcher, int i, List<T> seq)
		{
			int savedLBT = matcher.lookbehindTo;
			int savedFrom = matcher.from;
			boolean conditionMatched = false;
			int startIndex = (!matcher.transparentBounds) ? matcher.from : 0;
			int from = Math.max(i - rmax, startIndex);
			matcher.lookbehindTo = i;
			// Relax transparent region boundaries for lookbehind
			if (matcher.transparentBounds) matcher.from = 0;
			for (int j = i - rmin; !conditionMatched && j >= from; j--)
			{
				conditionMatched = cond.match(matcher, j, seq);
			}
			// Reinstate region boundaries
			matcher.from = savedFrom;
			matcher.lookbehindTo = savedLBT;
			return !conditionMatched && next.match(matcher, i, seq);
		}
	}

	/**
	 * Returns the set union of two CharProperty nodes.
	 */
	private  CharProperty union(final CharProperty lhs,
			final CharProperty rhs)
	{
		return new CharProperty() {
			boolean isSatisfiedBy(T ch)
			{
				return lhs.isSatisfiedBy(ch) || rhs.isSatisfiedBy(ch);
			}
		};
	}

	/**
	 * Returns the set intersection of two CharProperty nodes.
	 */
	private CharProperty intersection(final CharProperty lhs,
			final CharProperty rhs)
	{
		return new CharProperty() {
			boolean isSatisfiedBy(T ch)
			{
				return lhs.isSatisfiedBy(ch) && rhs.isSatisfiedBy(ch);
			}
		};
	}

	/**
	 * Returns the set difference of two CharProperty nodes.
	 */
	private CharProperty setDifference(final CharProperty lhs,
			final CharProperty rhs)
	{
		return new CharProperty() {
			boolean isSatisfiedBy(T ch)
			{
				return !rhs.isSatisfiedBy(ch) && lhs.isSatisfiedBy(ch);
			}
		};
	}

	// /////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * This must be the very first initializer.
	 */
	 final Node accept = new Node();

	 final Node lastAccept = new LastNode();

	abstract class Patternable implements Comparable<T>
	{
		abstract PatternElement patternElement();

		abstract Class<?> getClassToMatch();

		abstract int getCount();
	}

	public Pattern<T> add(char ch)
	{
		if (compiled) throw new IllegalStateException();
		pattern.add(new PatternElementPatternable(PatternElement.getFor(ch)));
		return this;
	}

	public Pattern<T> add(int i)
	{
		if (compiled) throw new IllegalStateException();
		pattern.add(new IntegerPatternable(i));
		return this;
	}

	public Pattern<T> add(Comparable<T> c)
	{
		if (compiled) throw new IllegalStateException();
		pattern.add(c);
		return this;
	}

	public Pattern<T> add(List<Comparable<T>> l)
	{
		if (compiled) throw new IllegalStateException();
		pattern.addAll(l);
		return this;
	}

	 enum PatternElement
	{
		OPEN_BRACKET('('), OPEN_SQUARE('['), DOT('.'), SLASH('\\'), CARET('^'), DOLLAR(
				'$'), CLOSE_BRACKET(')'), PIPE('|'), CLOSE_SQUARE(']'), QUESTION(
				'?'), STAR('*'), CLOSE_BRACE('}'), PLUS('+'), END_MARKER(0), LOWER_P(
				'p'), UPPER_P('P'), OTHER('x'), OPEN_BRACE('{'), AND('&'), AT(
				'@'), COLON(':'), EQUALS('='), BANG('!'), GREATER_THAN('>'), LESS_THAN(
				'<'), DASH('-'), COMMA(','), NEW_LINE('\n'), UNDER_SCORE('_'), NUMBER(
				'1');

		private static final Map<Character, PatternElement> map = new HashMap<Character, PatternElement>();
		static
		{
			for (PatternElement pe : values())
			{
				map.put(pe.ch, pe);
			}
		}

		private char ch;

		public static PatternElement getFor(char ch)
		{
			PatternElement pe = map.get(ch);
			if (pe == null) return OTHER;
			return pe;
		}

		private PatternElement(int ch)
		{
			this.ch = (char) ch;
		}

		@Override
		public String toString()
		{
			return this.name() + "('" + ch + "')";
		}

	}

	 class PatternElementPatternable extends Patternable
	{
		private PatternElement pe;

		public PatternElementPatternable(PatternElement pe)
		{
			this.pe = pe;
		}

		public int compareTo(T o)
		{
			throw new IllegalArgumentException("this should never get called");
		}

		public PatternElement patternElement()
		{
			return pe;
		}

		public int getCount()
		{
			throw new IllegalArgumentException("this should never get called");
		}

		public Class<?> getClassToMatch()
		{
			throw new IllegalArgumentException("this should never get called");
		}
	}

	 class ClassPatternable extends Patternable
	{
		Class<?> cls;

		public ClassPatternable(Class<?> cls)
		{
			this.cls = cls;
		}

		public int compareTo(T o)
		{
			throw new IllegalArgumentException("this should never get called");
		}

		public PatternElement patternElement()
		{
			return PatternElement.OTHER;
		}

		public int getCount()
		{
			throw new IllegalArgumentException("this should never get called");
		}

		public Class<?> getClassToMatch()
		{
			return cls;
		}

	}

	 class ElementPatternable extends Patternable
	{
		private Comparable<T> o;

		public ElementPatternable(Comparable<T> o)
		{
			this.o = o;
		}

		public PatternElement patternElement()
		{
			return PatternElement.OTHER;
		}

		public int compareTo(T o)
		{
			System.out.println("ElementPatternable.compareTo: " + this.o
					+ " = " + o);
			return this.o.compareTo(o);
		}

		public int getCount()
		{
			throw new IllegalArgumentException("this should never get called");
		}

		public Class<?> getClassToMatch()
		{
			throw new IllegalArgumentException("this should never get called");
		}
	}

	 class IntegerPatternable extends Patternable
	{
		int i;

		public IntegerPatternable(int i)
		{
			this.i = i;
		}

		public PatternElement patternElement()
		{
			return PatternElement.NUMBER;
		}

		public int compareTo(T o)
		{
			throw new IllegalArgumentException("this should never get called");
		}

		public int getCount()
		{
			System.out.println("IntegerPatternable.getCount()");
			return i;
		}

		public Class<?> getClassToMatch()
		{
			throw new IllegalArgumentException("this should never get called");
		}
	}

}
