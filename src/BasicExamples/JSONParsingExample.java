package BasicExamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import Entities.Key;
import Entities.Message;

public class JSONParsingExample {

	public static void main(String[] args) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(new Message("abcaaw"));
		System.out.println(json);

		ObjectMapper mapper = new ObjectMapper();

		Message msg = mapper.readValue(json, Message.class);

		System.out.println(msg);

	}
}
