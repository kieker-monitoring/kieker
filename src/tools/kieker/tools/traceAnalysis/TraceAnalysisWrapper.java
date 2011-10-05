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

package kieker.tools.traceAnalysis;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Build classpath and run trace analysis tool. Simple convenience class.
 * 
 * @author Robert von Massow
 * 
 */
public class TraceAnalysisWrapper {
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		String uDir;
		uDir = System.getProperty("user.dir");
		final File libDir = new File(uDir + File.separatorChar + File.separatorChar + "lib" + File.separatorChar);
		final File[] libs = libDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return name.endsWith(".jar");
			}
		});

		final File binDir = new File(uDir + File.separatorChar + File.separatorChar + "dist" + File.separatorChar);
		final File[] bins = binDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return name.endsWith(".jar");
			}
		});

		final URL urls[] = new URL[bins.length + libs.length + 1];
		try {
			urls[0] = new File(uDir).getCanonicalFile().toURI().toURL();
		} catch (final MalformedURLException e1) {
			e1.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		for (int i = 1; i < (libs.length + 1); i++) {
			try {
				urls[i] = libs[i - 1].getCanonicalFile().toURI().toURL();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		for (int i = libs.length + 1; i < (bins.length + libs.length + 1); i++) {
			try {
				urls[i] = bins[i - libs.length - 1].getCanonicalFile().toURI().toURL();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		try {
			final URLClassLoader cl = URLClassLoader.newInstance(urls, TraceAnalysisWrapper.class.getClassLoader());
			final Class<?> mainclass = cl.loadClass("kieker.tools.traceAnalysis.TraceAnalysisTool");
			final Method main = mainclass.getMethod("main", String[].class);
			main.invoke(null, (Object) args);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}

	}
}
