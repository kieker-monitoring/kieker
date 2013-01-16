package kieker.examples.userguide.spring;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class BookstoreClient {

	public static void main(final String[] args) throws IOException {
		for (int i = 0; i < 5; i++) {
			System.out.println("BookstoreClient: Starting HTTP GET request: " + i);
	        final URL url = new URL("http://localhost:9293/bookstore/search/any/");
	        final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	        System.out.println(in.readLine());
	        in.close();
		}
	}
}
