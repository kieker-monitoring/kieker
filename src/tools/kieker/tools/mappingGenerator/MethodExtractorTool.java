/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.mappingGenerator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import kieker.tools.mappingGenerator.filters.composite.NoInterfaceNoSuperclassFilter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: This tool should be (re-)activated
// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/247
/**
 * This Program extracts all method signatures from a given input path in
 * classpath style.
 * 
 * @author Robert von Massow
 * 
 */
public class MethodExtractorTool {

	// private static final Log log = LogFactory.getLog(MethodExtractorTool.class);

	private static CommandLine cmdl = null;
	private static final CommandLineParser CMDL_PARSER = new BasicParser();
	private static final HelpFormatter CMD_HELP_FORMATTER = new HelpFormatter();
	private static final Options CMDL_OPTS = new Options();
	private static String filter = null;
	private static String cp = null;
	private static String mappingFile = null;

	static {
		MethodExtractorTool.initializeOptions();
	}

	@SuppressWarnings("static-access")
	private final static void initializeOptions() {
		MethodExtractorTool.CMDL_OPTS.addOption(OptionBuilder.withArgName("classpath").hasArg().withLongOpt("searchpath").isRequired(true)
				.withDescription("Classpath to analyze. Multiple classpath elements can be separated by '" + File.pathSeparator + "'.").withValueSeparator('=')
				.create("c"));
		MethodExtractorTool.CMDL_OPTS.addOption(OptionBuilder.withArgName("classname").hasArg().withLongOpt("filter-classname").isRequired(false)
				.withDescription("Classname of the filter to use.\n Defaults to " + NoInterfaceNoSuperclassFilter.class.getName()).withValueSeparator('=')
				.create("f"));
		MethodExtractorTool.CMDL_OPTS.addOption(OptionBuilder.withArgName("filename").hasArg().withLongOpt("mapping-file").isRequired(true)
				.withDescription("Name of mapping file to be written").withValueSeparator('=').create("m"));
	}

	private static boolean parseArgs(final String[] args) {
		try {
			MethodExtractorTool.cmdl = MethodExtractorTool.CMDL_PARSER.parse(MethodExtractorTool.CMDL_OPTS, args);
		} catch (final ParseException e) {
			System.err.println("Error parsing arguments: " + e.getMessage());
			MethodExtractorTool.printUsage();
			return false;
		}
		return true;
	}

	private static void printUsage() {
		MethodExtractorTool.CMD_HELP_FORMATTER.printHelp(MethodExtractorTool.class.getName(), MethodExtractorTool.CMDL_OPTS);
	}

	private static boolean initFromArgs() {
		MethodExtractorTool.cp = MethodExtractorTool.cmdl.getOptionValue("searchpath");
		MethodExtractorTool.filter = MethodExtractorTool.cmdl.getOptionValue("filter-classname", NoInterfaceNoSuperclassFilter.class.getName());
		MethodExtractorTool.mappingFile = MethodExtractorTool.cmdl.getOptionValue("mapping-file");
		return true;
	}

	public static void main(final String[] args) throws ClassNotFoundException {
		if (!MethodExtractorTool.parseArgs(args) || !MethodExtractorTool.initFromArgs()) {
			System.exit(1);
		}

		final MethodExtractor extrInstance = new MethodExtractor(MethodExtractorTool.cp, MethodExtractorTool.filter, MethodExtractorTool.mappingFile);
		if (!extrInstance.execute()) {
			System.err.println("An error occured. See 'kieker.log' for details.");
			System.exit(1);
		}
	}
}

class MethodExtractor extends ClassLoader {

	private static final Log LOG = LogFactory.getLog(MethodExtractor.class);
	private String filtername = null;
	private String cp = null;
	private String mappingFile = null;
	private final NameIdMap map = new NameIdMap();

	public MethodExtractor(final String cp, final String filtername, final String mappingFile) {
		this.cp = cp;
		this.filtername = filtername;
		this.mappingFile = mappingFile;
	}

	@SuppressWarnings("unchecked")
	public boolean execute() {
		boolean retval = false;
		try {
			final String[] elem = this.cp.split(File.pathSeparator);
			Class<IMethodFilter> filter = null;
			try {
				filter = (Class<IMethodFilter>) this.loadClass(this.filtername);
			} catch (final Exception e) {
				MethodExtractor.LOG.error("Unable to load filter or no filter provided (" + this.filtername + "), using default filter...");
				e.printStackTrace();
			}
			final Vector<File> directories = new Vector<File>();
			final Vector<File> jars = new Vector<File>();
			for (final String string : elem) {
				final File f = new File(string);
				if (f.isDirectory()) {
					directories.add(f);
				} else if (f.getName().endsWith(".jar")) {
					jars.add(f);
				}
			}
			this.analyzeDirectory(directories, filter);
			this.analyzeJars(jars, filter);
			this.map.writeMapToFile(this.mappingFile);
			retval = true;
		} catch (final IOException ex) {
			MethodExtractor.LOG.error("IOException: ", ex);
			retval = false;
		}
		return retval;
	}

	private void analyzeJars(final Vector<File> jars, final Class<IMethodFilter> filter) {
		IMethodFilter f = null;
		if (filter != null) {
			try {
				f = filter.newInstance();
			} catch (final InstantiationException e) {
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (f == null) {
			f = new NullFilter();
		}
		for (final File file : jars) {
			try {
				final JarFile jar = new JarFile(file.getAbsoluteFile().getName());
				this.analyseJarFile(jar, f);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void analyseJarFile(final JarFile jar, final IMethodFilter filter) {
		final Enumeration<JarEntry> es = jar.entries();
		while (es.hasMoreElements()) {
			final JarEntry e = es.nextElement();
			MethodExtractor.LOG.error(e.getName());
			if (e.getName().endsWith(".class")) {
				this.analyzeJarClassEntry(jar, e, "", filter);
			}
		}
	}

	private void analyzeJarClassEntry(final JarFile jar, final JarEntry e, final String prefix, final IMethodFilter filter) {
		final long size = e.getSize();
		if ((size == -1) || (size > Integer.MAX_VALUE)) {
			MethodExtractor.LOG.error("Size of file \"" + jar.getName() + "/" + e.getName() + " out of range: size");
			return;
		}
		final byte[] data = new byte[(int) size];
		try {
			if (jar.getInputStream(e).read(data) == -1) {
				throw new IOException("Unexpected end of file.");
			}
			final String name = e.getName().substring(0, e.getName().length() - 6).replaceAll("/", ".");
			Class<?> clazz = null;
			try {
				clazz = this.defineClass(name, data, 0, data.length);
				this.analyzeClass(clazz, filter);
			} catch (final LinkageError err) {
				MethodExtractor.LOG.error("Linkage error", err);
			}
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}

	private void analyzeDirectory(final Vector<File> dirs, final Class<IMethodFilter> filter) {
		IMethodFilter f = null;
		if (filter != null) {
			try {
				f = filter.newInstance();
			} catch (final InstantiationException e) {
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			}
			if (f == null) {
				f = new NullFilter();
			}
		}
		for (final File dir : dirs) {
			this.descendAndAnalyseDir(dir, "", f);
		}
	}

	/**
	 * recursively descent the "classpath" and analyze the classes that a found
	 * in it.
	 * 
	 * @param dir
	 *            current system directory
	 * @param packagePrefix
	 *            the packageprefix
	 */
	private void descendAndAnalyseDir(final File dir, final String packagePrefix, final IMethodFilter filter) {
		final File[] classes = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return name.endsWith(".class");
			}
		});
		final File[] descArray = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File f, final String name) {
				return new File(f, name).isDirectory();
			}
		});
		this.analyseClasses(classes, packagePrefix, filter);
		for (final File file : descArray) {
			this.descendAndAnalyseDir(file,
					(packagePrefix + file.getName().replaceFirst(dir.getName(), "")).replaceAll(File.separatorChar == '\\' ? "\\\\" : File.separator, ".") + ".",
					filter);
		}
	}

	/**
	 * extract all methods from the given classes
	 * 
	 * @param classes
	 *            the classes in the current package
	 * @param packagePrefix
	 *            the current package
	 */
	private void analyseClasses(final File[] classes, final String packagePrefix, final IMethodFilter filter) {
		for (final File file : classes) {
			int begIndex = file.getName().lastIndexOf(File.separatorChar);
			begIndex = begIndex == -1 ? 0 : begIndex;
			final int endIndex = file.getName().lastIndexOf('.');
			try {
				final Class<?> c = super.loadClass(packagePrefix + file.getName().substring(begIndex, endIndex));
				this.analyzeClass(c, filter);
			} catch (final ClassNotFoundException e) {
				MethodExtractor.LOG.error(packagePrefix + file.getName().substring(begIndex, endIndex), e);
				MethodExtractor.LOG.error("packagePrefix: " + packagePrefix);
				MethodExtractor.LOG.error("file.getName().substring(begIndex, endIndex): " + file.getName().substring(begIndex, endIndex));
			}
		}
	}

	private void analyzeClass(final Class<?> c, final IMethodFilter filter) {
		final Method[] m = c.getDeclaredMethods();
		for (final Method method : m) {
			if (!filter.accept(method, c)) {
				continue;
			}
			/*
			 * See ticket http://samoa.informatik.uni-kiel.de/kieker/trac/ticket/246
			 * currently not used
			 * final int mod = method.getModifiers();
			 * String prefix = "";
			 * if (Modifier.isPublic(mod)) {
			 * prefix += "public ";
			 * }
			 * if (Modifier.isPrivate(mod)) {
			 * prefix += "private ";
			 * }
			 * if (Modifier.isProtected(mod)) {
			 * prefix += "protected ";
			 * }
			 * if (Modifier.isNative(mod)) {
			 * prefix += "native ";
			 * }
			 * if (Modifier.isAbstract(mod)) {
			 * prefix += "abstract ";
			 * }
			 * if (Modifier.isStatic(mod)) {
			 * prefix += "static ";
			 * }
			 * if (Modifier.isFinal(mod)) {
			 * prefix += "final ";
			 * }
			 */
			this.map.registerName(/* + prefix */method.getDeclaringClass().getName() + "." + method.getName() + "(" + this.concat(method.getParameterTypes()) + ")");
			method.getAnnotation(Override.class);
		}
	}

	/**
	 * format a parameter list
	 * 
	 * @param parameterTypes
	 *            the method parameters
	 * @return formated parameter list
	 */
	private String concat(final Class<?>[] parameterTypes) {
		final StringBuilder ret = new StringBuilder();
		for (final Class<?> class1 : parameterTypes) {
			ret.append(class1.getCanonicalName() + ",");
		}
		if (ret.length() == 0) {
			return "";
		}
		return ret.delete(ret.length() - 1, ret.length()).toString();
	}
}

class NullFilter implements IMethodFilter {

	@Override
	public boolean accept(final Method m, final Class<?> c) {
		return true;
	}
}
