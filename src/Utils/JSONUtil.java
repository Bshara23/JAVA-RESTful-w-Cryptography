package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JSONUtil {

	public static String getTextFromUrl(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		int response = conn.getResponseCode();

		if (response != 200) {
			throw new RuntimeException("HttpResponseCode=" + response);
		} else {
			Scanner sc = new Scanner(url.openStream());
			StringBuilder sb = new StringBuilder();
			while (sc.hasNext()) {
				sb.append(sc.nextLine());
			}
			sc.close();
			return sb.toString();
		}
		
	}
	
	
	// TODO
	public static String getJSONFromUrl(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		int response = conn.getResponseCode();

		if (response != 200) {
			throw new RuntimeException("HttpResponseCode=" + response);
		} else {
			Scanner sc = new Scanner(url.openStream());
			StringBuilder sb = new StringBuilder();
			while (sc.hasNext()) {
				sb.append(sc.nextLine());
			}
			sc.close();
			return sb.toString();
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T getObjFromUrl(String urlStr, @SuppressWarnings("rawtypes") Class cls) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		int response = conn.getResponseCode();

		if (response != 200) {
			throw new RuntimeException("HttpResponseCode=" + response);
		} else {
			Scanner sc = new Scanner(url.openStream());
			StringBuilder sb = new StringBuilder();
			while (sc.hasNext()) {
				sb.append(sc.nextLine());
			}
			sc.close();
			
			
			ObjectMapper mapper = new ObjectMapper();
			
			String jsonString = sb.toString();
			T fetchedObj = (T) mapper.readValue(jsonString, cls);
			return fetchedObj;
		}
		
	}
	

	public static <T> ArrayList<T> getArrayList(String urlStr, @SuppressWarnings("rawtypes") Class cls) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		int response = conn.getResponseCode();

		if (response != 200) {
			throw new RuntimeException("HttpResponseCode=" + response);
		} else {
			Scanner sc = new Scanner(url.openStream());
			StringBuilder sb = new StringBuilder();
			while (sc.hasNext()) {
				sb.append(sc.nextLine());
			}
			sc.close();
			
			
			ObjectMapper mapper = new ObjectMapper();
			
			String jsonString = sb.toString();

			ArrayList<T> myObjects = mapper.readValue(jsonString, new TypeReference<ArrayList<T>>(){});

			return myObjects;
		}
		
	}
	

	
	@SuppressWarnings("unchecked")
	public static <T> T postToUrl(String urlStr, @SuppressWarnings("rawtypes") Class cls) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.connect();
	
		int response = conn.getResponseCode();
		StringBuilder sb = new StringBuilder();


		InputStreamReader in = new InputStreamReader(conn.getInputStream());
		BufferedReader br = new BufferedReader(in);
		String text = "";
		while ((text = br.readLine()) != null) {
			sb.append(text);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = sb.toString();
		
		T fetchedObj = (T) mapper.readValue(jsonString, cls);
		return fetchedObj;
				
	}
	
	public static String toJSONString(Object obj) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			return ow.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String htmlBasic(Object... arr) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html> <body> <h1>");

		for (Object object : arr) {
			sb.append(object + "<br>");
		}

		sb.append("</h1></body></html>");

		return sb.toString();
	}

	public String getTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}
}
