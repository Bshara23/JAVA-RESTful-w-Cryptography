package com.bshara.cryptoserver.BasicExamples;

import com.bshara.cryptoserver.Entities.WebMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JSONParsingExample {

	public static void main(String[] args) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(new WebMessage("abcaaw"));
		System.out.println(json);

		ObjectMapper mapper = new ObjectMapper();

		WebMessage msg = mapper.readValue(json, WebMessage.class);

		System.out.println(msg);

	}
}
