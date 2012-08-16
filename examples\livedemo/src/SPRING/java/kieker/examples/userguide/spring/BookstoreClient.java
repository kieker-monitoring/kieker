package kieker.examples.userguide.spring;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class BookstoreClient {

	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 5; i++) {
			System.out.println("BookstoreClient: Starting HTTP GET request: " + i);
	        URL url = new URL("http://localhost:9292/bookstore/search/any/");
	        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	        System.out.println(in.readLine());
	        in.close();
		}
	}
}
