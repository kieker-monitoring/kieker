import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class TracerWrap {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		String uDir;
		uDir = System.getProperty("user.dir");
		final File libDir = new File(uDir + File.separatorChar
				+ File.separatorChar + "lib" + File.separatorChar);
		final File[] libs = libDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return name.endsWith(".jar");
			}
		});

		final File binDir = new File(uDir + File.separatorChar
				+ File.separatorChar + "dist" + File.separatorChar);
		final File[] bins = binDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return name.endsWith(".jar");
			}
		});
		System.out.println(bins.length + " " + libs.length);

		final URL urls[] = new URL[bins.length + libs.length + 1];
		try {
			urls[0] = new File(uDir).getCanonicalFile().toURI().toURL();
		} catch (final MalformedURLException e1) {
			e1.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		for (int i = 1; i < libs.length + 1; i++) {
			try {
				urls[i] = libs[i - 1].getCanonicalFile().toURI().toURL();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = libs.length + 1; i < bins.length + libs.length + 1; i++) {
			try {
				urls[i] = bins[i - libs.length - 1].getCanonicalFile().toURI()
						.toURL();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			final URLClassLoader cl = URLClassLoader.newInstance(urls,
					TracerWrap.class.getClassLoader());
			for (final URL u : cl.getURLs()) {
				System.out.println(u);
			}
			System.out.println(cl);
			final Class<?> mainclass = cl
					.loadClass("kieker.tools.traceAnalysis.TraceAnalysisTool");
			System.out.println(cl + " " + mainclass.getClassLoader());
			final Method main = mainclass.getMethod("main", String[].class);
			main.invoke(null, (Object) args);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
