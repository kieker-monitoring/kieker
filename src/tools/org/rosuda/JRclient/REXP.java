package org.rosuda.JRclient;

// JRclient library - client interface to Rserve, see http://www.rosuda.org/Rserve/
// Copyright (C) 2004 Simon Urbanek
// --- for licensing information see LICENSE file in the original JRclient distribution ---

import java.util.Vector;

/**
 * representation of R-eXpressions in Java
 * 
 * @version $Id: REXP.java 2746 2007-05-10 17:14:14Z urbanek $
 */
@SuppressWarnings("serial")
public class REXP extends Object implements java.io.Serializable {
	/** xpression type: NULL */
	public static final int XT_NULL = 0;
	/** xpression type: integer */
	public static final int XT_INT = 1;
	/** xpression type: double */
	public static final int XT_DOUBLE = 2;
	/** xpression type: String */
	public static final int XT_STR = 3;
	/** xpression type: language construct (currently content is same as list) */
	public static final int XT_LANG = 4;
	/** xpression type: symbol (content is symbol name: String) */
	public static final int XT_SYM = 5;
	/** xpression type: RBool */
	public static final int XT_BOOL = 6;
	/**
	 * xpression type: S4 object
	 * 
	 * @since Rserve 0.5
	 */
	public static final int XT_S4 = 7;
	/** xpression type: generic vector (RList) */
	public static final int XT_VECTOR = 16;
	/** xpression type: dotted-pair list (RList) */
	public static final int XT_LIST = 17;
	/**
	 * xpression type: closure (there is no java class for that type (yet?). currently the body of the closure is stored in the content part of the REXP. Please note
	 * that this may change in the future!)
	 */
	public static final int XT_CLOS = 18;
	/**
	 * xpression type: symbol name
	 * 
	 * @since Rserve 0.5
	 */
	public static final int XT_SYMNAME = 19;
	/**
	 * xpression type: dotted-pair list (w/o tags)
	 * 
	 * @since Rserve 0.5
	 */
	public static final int XT_LIST_NOTAG = 20;
	/**
	 * xpression type: dotted-pair list (w tags)
	 * 
	 * @since Rserve 0.5
	 */
	public static final int XT_LIST_TAG = 21;
	/**
	 * xpression type: language list (w/o tags)
	 * 
	 * @since Rserve 0.5
	 */
	public static final int XT_LANG_NOTAG = 22;
	/**
	 * xpression type: language list (w tags)
	 * 
	 * @since Rserve 0.5
	 */
	public static final int XT_LANG_TAG = 23;
	/** xpression type: expression vector */
	public static final int XT_VECTOR_EXP = 26;
	/** xpression type: string vector */
	public static final int XT_VECTOR_STR = 27;
	/** xpression type: int[] */
	public static final int XT_ARRAY_INT = 32;
	/** xpression type: double[] */
	public static final int XT_ARRAY_DOUBLE = 33;
	/** xpression type: String[] (currently not used, Vector is used instead) */
	public static final int XT_ARRAY_STR = 34;
	/** internal use only! this constant should never appear in a REXP */
	public static final int XT_ARRAY_BOOL_UA = 35;
	/** xpression type: RBool[] */
	public static final int XT_ARRAY_BOOL = 36;
	/**
	 * xpression type: raw (byte[])
	 * 
	 * @since Rserve 0.4-?
	 */
	public static final int XT_RAW = 37;
	/**
	 * xpression type: Complex[]
	 * 
	 * @since Rserve 0.5
	 */
	public static final int XT_ARRAY_CPLX = 38;
	/** xpression type: unknown; no assumptions can be made about the content */
	public static final int XT_UNKNOWN = 48;

	/** xpression type: RFactor; this XT is internally generated (ergo is does not come from Rsrv.h) to support RFactor class which is built from XT_ARRAY_INT */
	public static final int XT_FACTOR = 127;

	/** used for transport only - has attribute */
	private static final int XT_HAS_ATTR = 128;

	/** xpression type */
	int Xt;
	/** attribute xpression or <code>null</code> if none */
	REXP attr;
	/** content of the xpression - its object type is dependent of {@link #Xt} */
	Object cont;

	/** cached binary length; valid only if positive */
	long cachedBinaryLength = -1;

	/** construct a new, empty (NULL) expression w/o attribute */
	public REXP() {
		this.Xt = 0;
		this.attr = null;
		this.cont = null;
	}

	/**
	 * construct a new xpression of type t and content o, but no attribute
	 * 
	 * @param t
	 *            xpression type (XT_...)
	 * @param o
	 *            content
	 */
	public REXP(final int t, final Object o) {
		this.Xt = t;
		this.cont = o;
		this.attr = null;
	}

	/**
	 * construct a new xpression of type t, content o and attribute at
	 * 
	 * @param t
	 *            xpression type
	 * @param o
	 *            content
	 * @param at
	 *            attribute
	 */
	public REXP(final int t, final Object o, final REXP at) {
		this.Xt = t;
		this.cont = o;
		this.attr = at;
	}

	/**
	 * construct a new xpression of type XT_ARRAY_DOUBLE and content val
	 * 
	 * @param val
	 *            array of doubles to store in the REXP
	 */
	public REXP(final double[] val) {
		this(XT_ARRAY_DOUBLE, val);
	}

	/**
	 * construct a new xpression of type XT_ARRAY_INT and content val
	 * 
	 * @param val
	 *            array of integers to store in the REXP
	 */
	public REXP(final int[] val) {
		this(XT_ARRAY_INT, val);
	}

	/**
	 * construct a new xpression of type XT_ARRAY_INT and content val
	 * 
	 * @param val
	 *            array of integers to store in the REXP
	 */
	public REXP(final String[] val) {
		this(XT_ARRAY_STR, val);
	}

	/**
	 * get all attributes of the REXP in the form of a list or <code>null</code> if the object has no attributes.
	 * 
	 * @return attribute xpression or <code>null</code> if there is none associated
	 */
	public RList getAttributes() {
		return (this.attr != null) ? this.attr.asList() : null;
	}

	/**
	 * get a certain attribute
	 * 
	 * @param name
	 *            name of the attribute
	 * @return <code>null</code> if the attribute doesn't exist or the attribute.
	 */
	public REXP getAttribute(final String name) {
		if ((this.attr == null) || (this.attr.cont == null)) {
			return null;
		}
		return this.attr.asList().at(name);
	}

	/**
	 * set an attribute value. The attributes list is created if necessary.
	 * 
	 * @param name
	 *            attribute name
	 * @param value
	 *            attribute value
	 */
	public void setAttribute(final String name, final REXP value) {
		if ((this.attr == null) || (this.attr.asList() == null)) {
			this.attr = new REXP(XT_LIST_TAG, new RList());
		}
		final RList l = this.attr.asList();
		if (l == null) {
			return;
		}
		l.put(name, value);
	}

	/**
	 * get raw content. Use as... methods to retrieve contents of known type.
	 * 
	 * @return content of the REXP
	 * @deprecated please use corresponding <code>as...</code> accessor methods
	 */
	@Deprecated
	public Object getContent() {
		return this.cont;
	}

	/**
	 * get xpression type (see XT_.. constants) of the content. It defines the type of the content object.
	 * 
	 * @return xpression type
	 * @deprecated please use corresponding <code>is...</code> test methods
	 */
	@Deprecated
	public int getType() {
		return this.Xt;
	}

	/**
	 * return the length on the REXP. If the REXP is scalar, the length is 1, for array and vector types the length is the number of elements. For all other types
	 * the length is zero.
	 * 
	 * @return length of the REXP
	 */
	public int length() {
		switch (this.Xt) {
		case XT_INT:
		case XT_DOUBLE:
		case XT_STR:
		case XT_SYM:
		case XT_SYMNAME:
			return 1;
		case XT_VECTOR:
		case XT_LIST:
		case XT_LIST_TAG:
		case XT_LIST_NOTAG:
		case XT_LANG_TAG:
		case XT_LANG_NOTAG:
		case XT_VECTOR_EXP:
			return (this.asList() == null) ? 0 : this.asList().size();
		case XT_ARRAY_INT:
			return (this.cont == null) ? 0 : ((int[]) this.cont).length;
		case XT_ARRAY_DOUBLE:
			return (this.cont == null) ? 0 : ((double[]) this.cont).length;
		case XT_ARRAY_CPLX:
			return (this.cont == null) ? 0 : ((double[]) this.cont).length / 2;
		case XT_ARRAY_BOOL:
			return (this.cont == null) ? 0 : ((RBool[]) this.cont).length;
		case XT_RAW:
			return (this.cont == null) ? 0 : ((byte[]) this.cont).length;
		case XT_ARRAY_STR:
		case XT_VECTOR_STR:
			return (this.cont == null) ? 0 : ((String[]) this.cont).length;
		case XT_FACTOR:
			return (this.cont == null) ? 0 : ((RFactor) this.cont).size();
		}
		return 0;
	}

	/**
	 * parses byte buffer for binary representation of xpressions - read one xpression slot (descends recursively for aggregated xpressions such as lists, vectors
	 * etc.)
	 * 
	 * @param x
	 *            xpression object to store the parsed xpression in
	 * @param buf
	 *            buffer containing the binary representation
	 * @param o
	 *            offset in the buffer to start at
	 * @return position just behind the parsed xpression. Can be use for successive calls to {@link #parseREXP} if more than one expression is stored in the binary
	 *         array.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int parseREXP(final REXP x, final byte[] buf, int o) {
		final int xl = Rtalk.getLen(buf, o);
		final boolean hasAtt = ((buf[o] & 128) != 0);
		final boolean isLong = ((buf[o] & 64) != 0);
		final int xt = (buf[o] & 63);
		// System.out.println("parseREXP: type="+xt+", len="+xl+", hasAtt="+hasAtt+", isLong="+isLong);
		if (isLong) {
			o += 4;
		}
		o += 4;
		final int eox = o + xl;

		x.Xt = xt;
		x.attr = null;
		if (hasAtt) {
			o = REXP.parseREXP(x.attr = new REXP(), buf, o);
		}
		if (xt == XT_NULL) {
			x.cont = null;
			return o;
		}
		;
		if (xt == XT_DOUBLE) {
			final long lr = Rtalk.getLong(buf, o);
			x.cont = new Double(Double.longBitsToDouble(lr));
			o += 8;
			if (o != eox) {
				System.err.println("Warning: double SEXP size mismatch\n");
				o = eox;
			}
			;
			return o;
		}
		if (xt == XT_ARRAY_DOUBLE) {
			final int as = (eox - o) / 8;
			int i = 0;
			final double[] d = new double[as];
			while (o < eox) {
				d[i] = Double.longBitsToDouble(Rtalk.getLong(buf, o));
				o += 8;
				i++;
			}
			;
			if (o != eox) {
				System.err.println("Warning: double array SEXP size mismatch\n");
				o = eox;
			}
			;
			x.cont = d;
			return o;
		}
		;
		if (xt == XT_BOOL) {
			x.cont = new RBool(buf[o]);
			o++;
			if (o != eox) {
				if (eox != o + 3) {
					System.err.println("Warning: bool SEXP size mismatch\n");
				}
				o = eox;
			}
			;
			return o;
		}
		;
		if (xt == XT_ARRAY_BOOL_UA) {
			final int as = (eox - o);
			int i = 0;
			x.Xt = XT_ARRAY_BOOL; // XT_ARRAY_BOOL_UA is only old transport type for XT_ARRAY_BOOL
			final RBool[] d = new RBool[as];
			while (o < eox) {
				d[i] = new RBool(buf[o]);
				i++;
				o++;
			}
			;
			x.cont = d;
			return o;
		}
		;
		if (xt == XT_ARRAY_BOOL) {
			final int as = Rtalk.getInt(buf, o);
			o += 4;
			int i = 0;
			final RBool[] d = new RBool[as];
			while ((o < eox) && (i < as)) {
				d[i] = new RBool(buf[o]);
				i++;
				o++;
			}
			// skip the padding
			while ((i & 3) != 0) {
				i++;
				o++;
			}
			;
			x.cont = d;
			return o;
		}
		;
		if (xt == XT_INT) {
			x.cont = new Integer(Rtalk.getInt(buf, o));
			o += 4;
			if (o != eox) {
				System.err.println("Warning: int SEXP size mismatch\n");
				o = eox;
			}
			;
			return o;
		}
		if (xt == XT_ARRAY_INT) {
			final int as = (eox - o) / 4;
			int i = 0;
			final int[] d = new int[as];
			while (o < eox) {
				d[i] = Rtalk.getInt(buf, o);
				o += 4;
				i++;
			}
			;
			if (o != eox) {
				System.err.println("Warning: int array SEXP size mismatch\n");
				o = eox;
			}
			;
			x.cont = d;
			// hack for lists - special lists attached to int are factors
			if (x.attr != null) {
				final REXP ca = x.attr.asList().at("class");
				final REXP ls = x.attr.asList().at("levels");
				if ((ca != null) && (ls != null) && ca.asString().equals("factor")) {
					final RFactor f = new RFactor(d, ls.asStringArray());
					x.cont = f;
					x.Xt = XT_FACTOR;
					// x.attr=null;
				}
			}
			return o;
		}
		if (xt == XT_ARRAY_STR) {
			int c = 0, i = o;
			while (i < eox) {
				if (buf[i++] == 0) {
					c++;
				}
			}
			final String s[] = new String[c];
			if (c > 0) {
				c = 0;
				i = o;
				while (o < eox) {
					if (buf[o] == 0) {
						try {
							s[c] = new String(buf, i, o - i, Rconnection.transferCharset);
						} catch (final java.io.UnsupportedEncodingException ex) {
							s[c] = "";
						}
						c++;
						i = o + 1;
					}
					o++;
				}
			}
			x.cont = s;
			return o;
		}

		if ((xt == XT_LIST_NOTAG) || (xt == XT_LIST_TAG)) {
			final RList l = new RList();
			while (o < eox) {
				final REXP lc = new REXP();
				String name = null;
				o = REXP.parseREXP(lc, buf, o);
				if (xt == XT_LIST_TAG) {
					final REXP ns = new REXP();
					o = REXP.parseREXP(ns, buf, o);
					name = ns.asString();
				}
				if (name == null) {
					l.add(lc);
				} else {
					l.put(name, lc);
				}
			}
			x.cont = l;
			if (o != eox) {
				System.err.println("Warning: int list SEXP size mismatch\n");
				o = eox;
			}
			return o;
		}
		if (xt == XT_VECTOR) {
			final Vector v = new Vector();
			while (o < eox) {
				final REXP xx = new REXP();
				o = REXP.parseREXP(xx, buf, o);
				v.addElement(xx);
			}
			;
			if (o != eox) {
				System.err.println("Warning: int vector SEXP size mismatch\n");
				o = eox;
			}
			;
			x.cont = v;
			// fixup for lists since they're stored as attributes of vectors
			if (x.getAttribute("names") != null) {
				final REXP nam = x.getAttribute("names");
				final RList l = new RList(v, nam.asStringArray());
				x.cont = l;
				// x.Xt=XT_LIST;
				/*
				 * REXP nam = l.at("names");
				 * l.head=((RList)x.attr.cont).head;
				 * l.body=new REXP(XT_VECTOR,v);
				 * x.cont=l;
				 * x.Xt=XT_LIST; x.attr=x.attr.attr;
				 * // one more hack: we're de-vectorizing strings if alone
				 * // so we should invert that in case of list heads
				 * if (l.head.Xt==XT_STR) {
				 * Vector sv=new Vector();
				 * sv.addElement(l.head);
				 * l.head=new REXP(XT_VECTOR,sv,l.head.attr);
				 * l.head.attr=null;
				 * };
				 */
			}
			;
			return o;
		}
		;
		if (xt == XT_VECTOR_STR) {
			final RList v = new RList();
			while (o < eox) {
				final REXP xx = new REXP();
				o = REXP.parseREXP(xx, buf, o);
				v.addElement(xx.asString());
			}
			if (o != eox) {
				System.err.println("Warning: int vector SEXP size mismatch\n");
				o = eox;
			}
			final String sa[] = new String[v.size()];
			int i = 0;
			while (i < sa.length) {
				sa[i] = (String) v.get(i);
				i++;
			}
			x.cont = sa;
			return o;
		}
		if ((xt == XT_STR) || (xt == XT_SYMNAME)) {
			int i = o;
			while ((buf[i] != 0) && (i < eox)) {
				i++;
			}
			try {
				x.cont = new String(buf, o, i - o, Rconnection.transferCharset);
			} catch (final Exception e) {
				System.err.println("unable to convert string\n");
				x.cont = null;
			}
			;
			o = eox;
			return o;
		}
		;
		/*
		 * if (xt==XT_LIST || xt==XT_LANG) {
		 * RList rl=new RList();
		 * rl.head=new REXP();
		 * rl.body=new REXP();
		 * rl.tag=null;
		 * o=parseREXP(rl.head,buf,o); // CAR
		 * o=parseREXP(rl.body,buf,o); // CDR
		 * if (o!=eox) {
		 * // if there is more data then it's presumably the TAG entry
		 * rl.tag=new REXP();
		 * o=parseREXP(rl.tag,buf,o);
		 * if (o!=eox) {
		 * System.out.println("Warning: list SEXP size mismatch\n");
		 * o=eox;
		 * }
		 * };
		 * x.cont=rl;
		 * return o;
		 * };
		 */

		if (xt == XT_SYM) {
			final REXP sym = new REXP();
			o = REXP.parseREXP(sym, buf, o); // PRINTNAME that's all we will use
			String s = null;
			if (sym.Xt == XT_STR) {
				s = (String) sym.cont;
			} else {
				s = sym.toString();
			}
			x.cont = s; // content of a symbol is its printname string (so far)
			o = eox;
			return o;
		}

		if (xt == XT_CLOS) {
			final REXP form = new REXP();
			final REXP body = new REXP();
			o = REXP.parseREXP(form, buf, o);
			o = REXP.parseREXP(body, buf, o);
			if (o != eox) {
				System.err.println("Warning: closure SEXP size mismatch\n");
				o = eox;
			}
			/* curently closures are not coded into their own objects, basically due to lack of demand. */
			x.cont = body;
			return o;
		}

		if (xt == XT_UNKNOWN) {
			x.cont = new Integer(Rtalk.getInt(buf, o));
			o = eox;
			return o;
		}

		if (xt == XT_S4) {
			x.cont = null;
			o = eox;
			return o;
		}

		x.cont = null;
		o = eox;
		System.err.println("unhandled type: " + xt);
		return o;
	}

	/**
	 * Calculates the length of the binary representation of the REXP including all headers. This is the amount of memory necessary to store the REXP via
	 * {@link #getBinaryRepresentation}.
	 * <p>
	 * Please note that currently only XT_[ARRAY_]INT, XT_[ARRAY_]DOUBLE and XT_[ARRAY_]STR are supported! All other types will return 4 which is the size of the
	 * header.
	 * 
	 * @return length of the REXP including headers (4 or 8 bytes)
	 */
	@SuppressWarnings("unused")
	public int getBinaryLength() {
		int l = 0;
		int rxt = this.Xt;
		if ((this.Xt == XT_LIST) || (this.Xt == XT_LIST_TAG) || (this.Xt == XT_LIST_NOTAG)) {
			rxt = ((this.asList() != null) && this.asList().isNamed()) ? XT_LIST_TAG : XT_LIST_NOTAG;
		}
		// System.out.print("len["+xtName(Xt)+"/"+xtName(rxt)+"] ");
		if (this.Xt == XT_VECTOR_STR) {
			rxt = XT_ARRAY_STR; // VECTOR_STR is not really used
		}
		// adjust "names" attribute for vectors
		if ((this.Xt == XT_VECTOR) && (this.asList() != null) && this.asList().isNamed()) {
			this.setAttribute("names", new REXP(this.asList().keys()));
		}
		boolean hasAttr = false;
		RList al = null;
		if (this.attr != null) {
			al = this.attr.asList();
		}
		if ((al != null) && (al.size() > 0)) {
			hasAttr = true;
		}
		if (hasAttr) {
			l += this.attr.getBinaryLength();
		}
		switch (rxt) {
		case XT_NULL:
		case XT_S4:
			break;
		case XT_INT:
			l += 4;
			break;
		case XT_DOUBLE:
			l += 8;
			break;
		case XT_STR:
		case XT_SYMNAME:
			l += (this.cont == null) ? 1 : ((String) this.cont).length() + 1;
			if ((l & 3) > 0) {
				l = l - (l & 3) + 4;
			}
			break;
		case XT_ARRAY_INT:
			l += (this.cont == null) ? 0 : ((int[]) this.cont).length * 4;
			break;
		case XT_ARRAY_DOUBLE:
			l += (this.cont == null) ? 0 : ((double[]) this.cont).length * 8;
			break;
		case XT_ARRAY_CPLX:
			l += (this.cont == null) ? 0 : ((double[]) this.cont).length * 8;
			break;
		case XT_LIST_TAG:
		case XT_LIST_NOTAG:
		case XT_LIST:
		case XT_VECTOR:
			if (this.asList() != null) {
				final RList lst = this.asList();
				int i = 0;
				while (i < lst.size()) {
					final REXP x = lst.at(i);
					l += (x == null) ? 4 : x.getBinaryLength();
					if (rxt == XT_LIST_TAG) {
						final int pl = l;
						final String s = lst.keyAt(i);
						l += 4; // header for a symbol
						l += (s == null) ? 1 : (s.length() + 1);
						if ((l & 3) > 0) {
							l = l - (l & 3) + 4;
							// System.out.println("TAG length: "+(l-pl));
						}
					}
					i++;
				}
				if ((l & 3) > 0) {
					l = l - (l & 3) + 4;
				}
			}
			break;

		case XT_VECTOR_STR:
			if (this.cont != null) {
				final String sa[] = (String[]) this.cont;
				int i = 0;
				// FIXME: this is not quite true - encoding may break this
				while (i < sa.length) {
					l += 4; // header
					l += (sa[i] == null) ? 1 : (sa[i].length() + 1);
					if ((l & 3) > 0) {
						l = l - (l & 3) + 4;
					}
					i++;
				}
				break;
			}
		case XT_ARRAY_STR:
			if (this.cachedBinaryLength < 0) { // if there's no cache, we have to count..
				if (this.cont == null) {
					this.cachedBinaryLength = 4;
				} else {
					final String sa[] = (String[]) this.cont;
					int i = 0, io = 0;
					while (i < sa.length) {
						if (sa[i] != null) {
							try {
								byte b[] = sa[i].getBytes(Rconnection.transferCharset);
								io += b.length;
								b = null;
							} catch (final java.io.UnsupportedEncodingException uex) {
								// FIXME: we should so something ... so far we hope noone's gonna mess with the encoding
							}
						}
						io++;
						i++;
					}
					while ((io & 3) != 0) {
						io++;
					}
					this.cachedBinaryLength = io + 4;
					if (this.cachedBinaryLength > 0xfffff0) {
						this.cachedBinaryLength += 4;
					}
				}
			}
			return l + (int) this.cachedBinaryLength;
		} // switch
		if (l > 0xfffff0) {
			l += 4; // large data need 4 more bytes
		}
		// System.out.println("len:"+(l+4)+" "+xtName(rxt)+"/"+xtName(Xt)+" "+cont);
		return l + 4; // add the header
	}

	/**
	 * Stores the REXP in its binary (ready-to-send) representation including header into a buffer and returns the index of the byte behind the REXP.
	 * <p>
	 * Please note that currently only XT_[ARRAY_]INT, XT_[ARRAY_]DOUBLE and XT_[ARRAY_]STR are supported! All other types will be stored as SEXP of the length 0
	 * without any contents.
	 * 
	 * @param buf
	 *            buffer to store the REXP binary into
	 * @param off
	 *            offset of the first byte where to store the REXP
	 * @return the offset of the first byte behind the stored REXP
	 */
	public int getBinaryRepresentation(final byte[] buf, int off) {
		final int myl = this.getBinaryLength();
		final boolean isLarge = (myl > 0xfffff0);
		boolean hasAttr = false;
		RList al = null;
		if (this.attr != null) {
			al = this.attr.asList();
		}
		if ((al != null) && (al.size() > 0)) {
			hasAttr = true;
		}
		int rxt = this.Xt;
		final int ooff = off;
		if (this.Xt == XT_VECTOR_STR) {
			rxt = XT_ARRAY_STR; // VECTOR_STR is not really used
		}
		if ((this.Xt == XT_LIST) || (this.Xt == XT_LIST_TAG) || (this.Xt == XT_LIST_NOTAG)) {
			rxt = ((this.asList() != null) && this.asList().isNamed()) ? XT_LIST_TAG : XT_LIST_NOTAG;
		}
		// System.out.println("@"+off+": "+xtName(rxt)+"/"+xtName(Xt)+" "+cont+" ("+myl+"/"+buf.length+") att="+hasAttr);
		Rtalk.setHdr(rxt | (hasAttr ? XT_HAS_ATTR : 0), myl - (isLarge ? 8 : 4), buf, off);
		off += (isLarge ? 8 : 4);
		if (hasAttr) {
			off = this.attr.getBinaryRepresentation(buf, off);
		}
		switch (rxt) {
		case XT_S4:
		case XT_NULL:
			break;
		case XT_INT:
			Rtalk.setInt(this.asInt(), buf, off);
			break;
		case XT_DOUBLE:
			Rtalk.setLong(Double.doubleToLongBits(this.asDouble()), buf, off);
			break;
		case XT_ARRAY_INT:
			if (this.cont != null) {
				final int ia[] = (int[]) this.cont;
				int i = 0, io = off;
				while (i < ia.length) {
					Rtalk.setInt(ia[i++], buf, io);
					io += 4;
				}
			}
			break;
		case XT_ARRAY_DOUBLE:
			if (this.cont != null) {
				final double da[] = (double[]) this.cont;
				int i = 0, io = off;
				while (i < da.length) {
					Rtalk.setLong(Double.doubleToLongBits(da[i++]), buf, io);
					io += 8;
				}
			}
			break;
		case XT_ARRAY_STR:
			if (this.cont != null) {
				final String sa[] = (String[]) this.cont;
				int i = 0, io = off;
				while (i < sa.length) {
					if (sa[i] != null) {
						try {
							byte b[] = sa[i].getBytes(Rconnection.transferCharset);
							System.arraycopy(b, 0, buf, io, b.length);
							io += b.length;
							b = null;
						} catch (final java.io.UnsupportedEncodingException uex) {
							// FIXME: we should so something ... so far we hope noone's gonna mess with the encoding
						}
					}
					buf[io++] = 0;
					i++;
				}
				i = io - off;
				while ((i & 3) != 0) {
					buf[io++] = 1;
					i++;
				} // padding if necessary..
			}
			break;
		case XT_LIST_TAG:
		case XT_LIST_NOTAG:
		case XT_LIST:
		case XT_VECTOR: {
			int io = off;
			if (this.asList() != null) {
				final RList lst = this.asList();
				int i = 0;
				while (i < lst.size()) {
					REXP x = lst.at(i);
					if (x == null) {
						x = new REXP(XT_NULL, null);
					}
					io = x.getBinaryRepresentation(buf, io);
					if (rxt == XT_LIST_TAG) {
						io = (new REXP(XT_SYMNAME, lst.keyAt(i))).getBinaryRepresentation(buf, io);
					}
					i++;
				}
			}
			// System.out.println("io="+io+", expected: "+(ooff+myl));
		}
			break;

		case XT_VECTOR_STR:
			if (this.cont != null) {
				final String sa[] = (String[]) this.cont;
				int i = 0, io = off;
				while (i < sa.length) {
					io = (new REXP(XT_STR, sa[i++])).getBinaryRepresentation(buf, io);
				}
			}
			break;
		case XT_SYMNAME:
		case XT_STR:
			REXP.getStringBinaryRepresentation(buf, off, (String) this.cont);
			break;
		}
		return ooff + myl;
	}

	public static int getStringBinaryRepresentation(final byte[] buf, final int off, String s) {
		if (s == null) {
			s = "";
		}
		int io = off;
		try {
			byte b[] = s.getBytes(Rconnection.transferCharset);
			// System.out.println("<str> @"+off+", len "+b.length+" (cont "+buf.length+") \""+s+"\"");
			System.arraycopy(b, 0, buf, io, b.length);
			io += b.length;
			b = null;
		} catch (final java.io.UnsupportedEncodingException uex) {
			// FIXME: we should so something ... so far we hope noone's gonna mess with the encoding
		}
		buf[io++] = 0;
		while ((io & 3) != 0) {
			buf[io++] = 0; // padding if necessary..
		}
		return io;
	}

	/**
	 * returns human-readable name of the xpression type as string. Arrays are denoted by a trailing asterisk (*).
	 * 
	 * @param xt
	 *            xpression type
	 * @return name of the xpression type
	 */
	public static String xtName(final int xt) {
		if (xt == XT_NULL) {
			return "NULL";
		}
		if (xt == XT_INT) {
			return "INT";
		}
		if (xt == XT_STR) {
			return "STRING";
		}
		if (xt == XT_DOUBLE) {
			return "REAL";
		}
		if (xt == XT_BOOL) {
			return "BOOL";
		}
		if (xt == XT_ARRAY_INT) {
			return "INT*";
		}
		if (xt == XT_ARRAY_STR) {
			return "STRING*";
		}
		if (xt == XT_ARRAY_DOUBLE) {
			return "REAL*";
		}
		if (xt == XT_ARRAY_BOOL) {
			return "BOOL*";
		}
		if (xt == XT_ARRAY_CPLX) {
			return "COMPLEX*";
		}
		if (xt == XT_SYM) {
			return "SYMBOL";
		}
		if (xt == XT_SYMNAME) {
			return "SYMNAME";
		}
		if (xt == XT_LANG) {
			return "LANG";
		}
		if (xt == XT_LIST) {
			return "LIST";
		}
		if (xt == XT_LIST_TAG) {
			return "LIST+T";
		}
		if (xt == XT_LIST_NOTAG) {
			return "LIST/T";
		}
		if (xt == XT_LANG_TAG) {
			return "LANG+T";
		}
		if (xt == XT_LANG_NOTAG) {
			return "LANG/T";
		}
		if (xt == XT_CLOS) {
			return "CLOS";
		}
		if (xt == XT_RAW) {
			return "RAW";
		}
		if (xt == XT_S4) {
			return "S4";
		}
		if (xt == XT_VECTOR) {
			return "VECTOR";
		}
		if (xt == XT_VECTOR_STR) {
			return "STRING[]";
		}
		if (xt == XT_VECTOR_EXP) {
			return "EXPR[]";
		}
		if (xt == XT_FACTOR) {
			return "FACTOR";
		}
		if (xt == XT_UNKNOWN) {
			return "UNKNOWN";
		}
		return "<unknown " + xt + ">";
	}

	/**
	 * get content of the REXP as string (if it is either a string or a symbol name). If the length of the character vector is more tahn one, the first element is
	 * returned.
	 * 
	 * @return string content or <code>null</code> if the REXP is no string or symbol name
	 */
	public String asString() {
		return ((this.Xt == XT_STR) || (this.Xt == XT_SYMNAME)) ? (String) this.cont : ((((this.Xt == XT_ARRAY_STR) || (this.Xt == XT_VECTOR_STR))
				&& (this.cont != null) &&
						(((String[]) this.cont).length > 0)) ? ((String[]) this.cont)[0] : null);
	}

	/**
	 * tests whether this <code>REXP</code> is a character vector (aka string array)
	 * 
	 * @return <code>true</code> if this REXP is a character vector
	 * @since Rserve 0.5
	 */
	public boolean isCharacter() {
		return ((this.Xt == XT_STR) || (this.Xt == XT_ARRAY_STR) || (this.Xt == XT_VECTOR_STR));
	}

	/**
	 * tests whether this <code>REXP</code> is a numeric vector (double or integer array or scalar)
	 * 
	 * @return <code>true</code> if this REXP is a numeric vector
	 * @since Rserve 0.5
	 */
	public boolean isNumeric() {
		return ((this.Xt == XT_ARRAY_DOUBLE) || (this.Xt == XT_ARRAY_INT) || (this.Xt == XT_DOUBLE) || (this.Xt == XT_INT));
	}

	/**
	 * tests whether this <code>REXP</code> is a symbol
	 * 
	 * @return <code>true</code> if this REXP is a symbol
	 * @since Rserve 0.5
	 */
	public boolean isSymbol() {
		return ((this.Xt == XT_SYM) || (this.Xt == XT_SYMNAME));
	}

	/**
	 * tests whether this <code>REXP</code> is NULL (either a literal nil object or the payload is <code>null</code>)
	 * 
	 * @return <code>true</code> if this REXP is NULL
	 * @since Rserve 0.5
	 */
	public boolean isNull() {
		return ((this.Xt == XT_NULL) || (this.cont == null));
	}

	/**
	 * tests whether this <code>REXP</code> is a logical vector (aka boolean array or scalar)
	 * 
	 * @return <code>true</code> if this REXP is a logical vector
	 * @since Rserve 0.5
	 */
	public boolean isLogical() {
		return ((this.Xt == XT_BOOL) || (this.Xt == XT_ARRAY_BOOL));
	}

	/**
	 * tests whether this <code>REXP</code> is a vector of complex numbers
	 * 
	 * @return <code>true</code> if this REXP is a vector of complex numbers
	 * @since Rserve 0.5
	 */
	public boolean isComplex() {
		return (this.Xt == XT_ARRAY_CPLX);
	}

	/**
	 * tests whether this <code>REXP</code> is a raw vector
	 * 
	 * @return <code>true</code> if this REXP is a raw vector
	 * @since Rserve 0.5
	 */
	public boolean isRaw() {
		return (this.Xt == XT_RAW);
	}

	/**
	 * get content of the REXP as an array of strings (if it is a character vector).
	 * 
	 * @return string content or <code>null</code> if the REXP is no string or symbol name
	 */
	public String[] asStringArray() {
		return (this.Xt == XT_STR) ? (new String[] { (String) this.cont }) : (((this.Xt == XT_ARRAY_STR) || (this.Xt == XT_VECTOR_STR)) ? (String[]) this.cont
				: null);
	}

	/**
	 * get content of the REXP as int (if it is one)
	 * 
	 * @return int content or 0 if the REXP is no integer
	 */
	public int asInt() {
		if (this.Xt == XT_ARRAY_INT) {
			final int i[] = (int[]) this.cont;
			if ((i != null) && (i.length > 0)) {
				return i[0];
			}
		}
		if ((this.Xt == XT_ARRAY_DOUBLE) || (this.Xt == XT_DOUBLE)) {
			return (int) this.asDouble();
		}
		return (this.Xt == XT_INT) ? ((Integer) this.cont).intValue() : 0;
	}

	/**
	 * get content of the REXP as double (if it is one)
	 * 
	 * @return double content or NaN if the REXP is no double
	 */
	public double asDouble() {
		if (this.Xt == XT_ARRAY_DOUBLE) {
			final double d[] = (double[]) this.cont;
			if ((d != null) && (d.length > 0)) {
				return d[0];
			}
		}
		return (this.Xt == XT_DOUBLE) ? ((Double) this.cont).doubleValue() : Double.NaN;
	}

	/**
	 * synonym for {@link asList}
	 * 
	 * @return a list or <code>null</code>
	 * @deprecated use {@link asList} instead
	 */
	@Deprecated
	public RList asVector() {
		return this.asList();
	}

	/**
	 * get content of the REXP as {@link RFactor} (if it is one)
	 * 
	 * @return {@link RFactor} content or <code>null</code> if the REXP is no factor
	 */
	public RFactor asFactor() {
		return (this.Xt == XT_FACTOR) ? (RFactor) this.cont : null;
	}

	/**
	 * tests whether this <code>REXP</code> is a list (any type, i.e. dotted-pair list or generic vector)
	 * 
	 * @return <code>true</code> if this REXP is a list
	 */
	public boolean isList() {
		return ((this.Xt == XT_VECTOR) || (this.Xt == XT_LIST) || (this.Xt == XT_LIST_TAG) || (this.Xt == XT_LIST_NOTAG));
	}

	/**
	 * returns <code>true</code> if the value is boolean and true
	 * 
	 * @return <code>true</code> if the value is boolean and true
	 */
	public boolean isTrue() {
		return ((this.Xt == XT_BOOL) && (this.cont != null)) ? ((RBool) this.cont).isTRUE() : false;
	}

	/**
	 * returns <code>true</code> if the value is boolean and false
	 * 
	 * @return <code>true</code> if the value is boolean and false
	 */
	public boolean isFalse() {
		return ((this.Xt == XT_BOOL) && (this.cont != null)) ? ((RBool) this.cont).isFALSE() : false;
	}

	/**
	 * tests whether this <code>REXP</code> is a factor
	 * 
	 * @return <code>true</code> if this <code>REXP</code> is a factor
	 */
	public boolean isFactor() {
		return (this.Xt == XT_FACTOR);
	}

	/**
	 * get content of the REXP as {@link RList} (if it is one)
	 * 
	 * @return {@link RList} content or <code>null</code> if the REXP is no list
	 */
	public RList asList() {
		return ((this.Xt == XT_VECTOR) || (this.Xt == XT_LIST) || (this.Xt == XT_LIST_TAG) || (this.Xt == XT_LIST_NOTAG)) ? (RList) this.cont : null;
	}

	/**
	 * get content of the REXP as {@link RBool} (if it is one)
	 * 
	 * @return {@link RBool} content or <code>null</code> if the REXP is no logical value
	 */
	public RBool asBool() {
		return (this.Xt == XT_BOOL) ? (RBool) this.cont : null;
	}

	/**
	 * get content of the REXP as an array of doubles. Array of integers, single double and single integer are automatically converted into such an array if
	 * necessary.
	 * 
	 * @return double[] content or <code>null</code> if the REXP is not a array of doubles or integers
	 */
	public double[] asDoubleArray() {
		if (this.Xt == XT_ARRAY_DOUBLE) {
			return (double[]) this.cont;
		}
		if (this.Xt == XT_DOUBLE) {
			final double[] d = new double[1];
			d[0] = this.asDouble();
			return d;
		}
		if (this.Xt == XT_INT) {
			final double[] d = new double[1];
			d[0] = ((Integer) this.cont).doubleValue();
			return d;
		}
		if (this.Xt == XT_ARRAY_INT) {
			final int[] i = this.asIntArray();
			if (i == null) {
				return null;
			}
			final double[] d = new double[i.length];
			int j = 0;
			while (j < i.length) {
				d[j] = i[j];
				j++;
			}
			return d;
		}
		return null;
	}

	/**
	 * get content of the REXP as an array of integers. Unlike {@link #asDoubleArray} <u>NO</u> automatic conversion is done if the content is not an array of the
	 * correct type, because there is no canonical representation of doubles as integers. A single integer is returned as an array of the length 1.
	 * 
	 * @return double[] content or <code>null</code> if the REXP is not a array of integers
	 */
	public int[] asIntArray() {
		if (this.Xt == XT_ARRAY_INT) {
			return (int[]) this.cont;
		}
		if (this.Xt == XT_INT) {
			final int[] i = new int[1];
			i[0] = this.asInt();
			return i;
		}
		return null;
	}

	/**
	 * returns the content of the REXP as a matrix of doubles (2D-array: m[rows][cols]). This is the same form as used by popular math packages for Java, such as
	 * JAMA. This means that following leads to desired results:<br>
	 * <code>Matrix m=new Matrix(c.eval("matrix(c(1,2,3,4,5,6),2,3)").asDoubleMatrix());</code>
	 * 
	 * @return 2D array of doubles in the form double[rows][cols] or <code>null</code> if the contents is no 2-dimensional matrix of doubles
	 */
	public double[][] asDoubleMatrix() {
		if ((this.Xt != XT_ARRAY_DOUBLE) || (this.attr == null) || (this.attr.Xt != XT_LIST)) {
			return null;
		}
		final REXP dim = this.getAttribute("dim");
		if ((dim == null) || (dim.Xt != XT_ARRAY_INT)) {
			return null; // we need dimension attr
		}
		final int[] ds = dim.asIntArray();
		if ((ds == null) || (ds.length != 2)) {
			return null; // matrix must be 2-dimensional
		}
		final int m = ds[0], n = ds[1];
		final double[][] r = new double[m][n];
		final double[] ct = this.asDoubleArray();
		if (ct == null) {
			return null;
		}
		// R stores matrices as matrix(c(1,2,3,4),2,2) = col1:(1,2), col2:(3,4)
		// we need to copy everything, since we create 2d array from 1d array
		int i = 0, k = 0;
		while (i < n) {
			int j = 0;
			while (j < m) {
				r[j++][i] = ct[k++];
			}
			i++;
		}
		return r;
	}

	/** this is just an alias for {@link #asDoubleMatrix()}. */
	public double[][] asMatrix() {
		return this.asDoubleMatrix();
	}

	/**
	 * displayable contents of the expression. The expression is traversed recursively if aggregation types are used (Vector, List, etc.)
	 * 
	 * @return String descriptive representation of the xpression
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public String toString() {
		final StringBuffer sb =
				new StringBuffer("[" + REXP.xtName(this.Xt) + " ");
		if (this.attr != null) {
			sb.append("\nattr=" + this.attr + "\n ");
		}
		if (this.Xt == XT_DOUBLE) {
			sb.append(this.cont);
		}
		if (this.Xt == XT_INT) {
			sb.append(this.cont);
		}
		if (this.Xt == XT_BOOL) {
			sb.append(this.cont);
		}
		if (this.Xt == XT_FACTOR) {
			sb.append(this.cont);
		}
		if (this.Xt == XT_CLOS) {
			sb.append(this.cont);
		}
		if (this.Xt == XT_ARRAY_DOUBLE) {
			final double[] d = (double[]) this.cont;
			sb.append("(");
			for (int i = 0; i < d.length; i++) {
				sb.append(d[i]);
				if (i < d.length - 1) {
					sb.append(", ");
				}
				if (i == 99) {
					sb.append("... (" + (d.length - 100) + " more values follow)");
					break;
				}
			}
			;
			sb.append(")");
		}
		;
		if (this.Xt == XT_ARRAY_INT) {
			final int[] d = (int[]) this.cont;
			sb.append("(");
			for (int i = 0; i < d.length; i++) {
				sb.append(d[i]);
				if (i < d.length - 1) {
					sb.append(", ");
				}
				if (i == 99) {
					sb.append("... (" + (d.length - 100) + " more values follow)");
					break;
				}
			}
			;
			sb.append(")");
		}
		;
		if (this.Xt == XT_ARRAY_BOOL) {
			final RBool[] d = (RBool[]) this.cont;
			sb.append("(");
			for (int i = 0; i < d.length; i++) {
				sb.append(d[i]);
				if (i < d.length - 1) {
					sb.append(", ");
				}
			}
			;
			sb.append(")");
		}
		;
		if (this.Xt == XT_VECTOR) {
			final Vector v = (Vector) this.cont;
			sb.append("(");
			for (int i = 0; i < v.size(); i++) {
				sb.append(((REXP) v.elementAt(i)).toString());
				if (i < v.size() - 1) {
					sb.append(", ");
				}
			}
			;
			sb.append(")");
		}
		;
		if (this.Xt == XT_STR) {
			sb.append("\"");
			sb.append((String) this.cont);
			sb.append("\"");
		}
		if ((this.Xt == XT_ARRAY_STR) || (this.Xt == XT_VECTOR_STR)) {
			final String[] s = (String[]) this.cont;
			if (s == null) {
				sb.append("NULL");
			} else {
				for (int i = 0; i < s.length; i++) {
					sb.append("\"" + s[i] + "\"");
					if (i < s.length - 1) {
						sb.append(", ");
					}
				}
			}
		}
		if ((this.Xt == XT_SYM) || (this.Xt == XT_SYMNAME)) {
			sb.append((String) this.cont);
		}
		;
		if ((this.Xt == XT_LIST) || (this.Xt == XT_LANG) || (this.Xt == XT_LIST_TAG) || (this.Xt == XT_LIST_NOTAG) || (this.Xt == XT_LANG_TAG)
				|| (this.Xt == XT_LANG_NOTAG)) {
			final RList l = (RList) this.cont;
			if (l == null) {
				sb.append("NULL list");
			} else {
				sb.append("{");
				if (l.isNamed()) {
					for (int i = 0; i < l.size(); i++) {
						sb.append(l.keyAt(i) + "=" + l.at(i).toString());
						if (i < l.size() - 1) {
							sb.append(", ");
						}
					}
				} else {
					for (int i = 0; i < l.size(); i++) {
						sb.append(l.at(i).toString());
						if (i < l.size() - 1) {
							sb.append(", ");
						}
					}
				}
				sb.append("}");
			}
		}
		;
		if (this.Xt == XT_UNKNOWN) {
			sb.append(this.cont);
		}
		sb.append("]");
		return sb.toString();
	};

	public static String quoteString(final String s) {
		// this code uses API introdiced in 1.4 so it needs to be re-written for earlier JDKs
		if (s.indexOf('\\') >= 0) {
			s.replaceAll("\\", "\\\\");
		}
		if (s.indexOf('"') >= 0) {
			s.replaceAll("\"", "\\\"");
		}
		return "\"" + s + "\"";
	}

	public static REXP createDataFrame(final RList l) {
		final REXP x = new REXP(XT_VECTOR, l);
		final REXP fe = l.at(0);
		x.setAttribute("class", new REXP(XT_VECTOR_STR, new String[] { "data.frame" }));
		x.setAttribute("row.names", new REXP(XT_ARRAY_INT, new int[] { -2147483648, -fe.length() }));
		x.setAttribute("names", new REXP(XT_VECTOR_STR, l.keys()));
		return x;
	}
}
