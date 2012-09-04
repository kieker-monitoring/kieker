package org.rosuda.JRclient;

// JRclient library - client interface to Rserve, see http://www.rosuda.org/Rserve/
// Copyright (C) 2004 Simon Urbanek
// --- for licensing information see LICENSE file in the original JRclient distribution ---

import java.util.Vector;

/**
 * representation of a factor variable. In R there is no actual xpression
 * type called "factor", instead it is coded as an int vector with a list
 * attribute. The parser code of REXP converts such constructs directly into
 * the RFactor objects and defines an own XT_FACTOR type
 * 
 * @version $Id: RFactor.java 2746 2007-05-10 17:14:14Z urbanek $
 */
@SuppressWarnings("rawtypes")
public class RFactor extends Object {
	/** IDs (content: Integer) each entry corresponds to a case, ID specifies the category */

	Vector id;
	/** values (content: String), ergo category names */
	Vector val;

	/** create a new, empty factor var */
	public RFactor() {
		this.id = new Vector();
		this.val = new Vector();
	}

	/**
	 * create a new factor variable, based on the supplied arrays.
	 * 
	 * @param i
	 *            array if IDs (0..v.length-1)
	 * @param v
	 *            values - cotegory names
	 */
	@SuppressWarnings("unchecked")
	public RFactor(final int[] i, final String[] v) {
		this.id = new Vector();
		this.val = new Vector();
		int j;
		if ((i != null) && (i.length > 0)) {
			for (j = 0; j < i.length; j++) {
				this.id.addElement(new Integer(i[j]));
			}
		}
		if ((v != null) && (v.length > 0)) {
			for (j = 0; j < v.length; j++) {
				this.val.addElement(v[j]);
			}
		}
	}

	/**
	 * special constructor used by REXP parser to save some re-indexing
	 * and performing automatic index conversion
	 * 
	 * @param i
	 *            index array
	 * @param v
	 *            vector of xpressions which should be all strings
	 */
	@SuppressWarnings("unchecked")
	public RFactor(final int[] i, final Vector v) {
		this.id = new Vector();
		this.val = new Vector();
		int j;
		if ((i != null) && (i.length > 0)) {
			for (j = 0; j < i.length; j++) {
				this.id.addElement(new Integer(i[j] - 1));
			}
		}
		if ((v != null) && (v.size() > 0)) {
			for (j = 0; j < v.size(); j++) {
				final REXP x = (REXP) v.elementAt(j);
				this.val.addElement(((x == null) || (x.Xt != REXP.XT_STR)) ? null : (String) x.cont);
			}
		}
		;
	};

	/**
	 * add a new element (by name)
	 * 
	 * @param v
	 *            value
	 */
	@SuppressWarnings("unchecked")
	public void add(final String v) {
		int i = this.val.indexOf(v);
		if (i < 0) {
			i = this.val.size();
			this.val.addElement(v);
		}
		;
		this.id.addElement(new Integer(i));
	}

	/**
	 * returns name for a specific ID
	 * 
	 * @param i
	 *            ID
	 * @return name. may throw exception if out of range
	 */
	public String at(final int i) {
		if ((i < 0) || (i >= this.id.size())) {
			return null;
		}
		return (String) this.val.elementAt(((Integer) this.id.elementAt(i)).intValue());
	}

	/** returns the number of caes */
	public int size() {
		return this.id.size();
	}

	/** displayable representation of the factor variable */
	@Override
	public String toString() {
		// return "{"+((val==null)?"<null>;":("levels="+val.size()+";"))+((id==null)?"<null>":("cases="+id.size()))+"}";
		final StringBuffer sb = new StringBuffer("{levels=(");
		if (this.val == null) {
			sb.append("null");
		} else {
			for (int i = 0; i < this.val.size(); i++) {
				sb.append((i > 0) ? ",\"" : "\"");
				sb.append((String) this.val.elementAt(i));
				sb.append("\"");
			}
		}
		;
		sb.append("),ids=(");
		if (this.id == null) {
			sb.append("null");
		} else {
			for (int i = 0; i < this.id.size(); i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(this.id.elementAt(i));
			}
		}
		;
		sb.append(")}");
		return sb.toString();
	}
}
