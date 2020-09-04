package BasicExamples;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

public class EncodingExample {

	public static void main(String[] args) {

		try {
			String url3 = URLEncoder.encode("a b c", "UTF-8");
			System.out.println(url3);
			String url2 = URLDecoder.decode(url3, "UTF-8");
			System.out.println(url2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
}
