package BasicExamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import Entities.Key;

public class JSONExample {

	public static void main(String[] args) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(new Key("abcaw", "daw"));
		System.out.println(json);

		ObjectMapper mapper = new ObjectMapper();

		Key key = mapper.readValue(json, Key.class);

		System.out.println(key);

	}
}
