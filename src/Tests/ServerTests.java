package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Main.Key;
import Main.MyUtil;

class ServerTests {

	@BeforeEach
	void setUp() throws Exception {
		
	}

	@AfterEach
	void tearDown() throws Exception {
		
	}

	@Test
	void basicGetTest() throws IOException {

		Key expected = new Key("abc", "123");
		String url1 = "http://localhost:8080/JRA2/main";
		Key fetched = MyUtil.getObjFromUrl(url1, Key.class);
		
		
		assertTrue(expected.equals(fetched));
	}
	
	@Test
	void getAllKeys() {
		
	}

}
