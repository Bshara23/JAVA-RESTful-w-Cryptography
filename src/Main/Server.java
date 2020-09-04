package Main;

import java.io.IOException;
import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import Entities.Key;
import Entities.Message;
import Utils.CryptoUtil;
import Utils.JSONUtil;

// http://localhost:8080/JRA2/main?a=5

@Path("/main")
public class Server {
	@PersistenceContext

	// TODO: change to a HashMap
	private static ArrayList<Key> keys = new ArrayList<Key>();
	private static int idCnt = 0;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllKeys() {
		ArrayList<String> keysIDs = new ArrayList<String>();

		for (Key key : keys)
			keysIDs.add(key.getKeyId());

		return JSONUtil.toJSONString(keysIDs);

		// gave IDs instead of the actual key
		// return JSONUtil.toJSONString(keys);
	}

	@POST
	@Path("clear")
	@Consumes("text/plain")
	public String clearAllKeys() {
		System.out.println("Clearing all keys");
		keys.clear();
		Server.idCnt = 0;

		return JSONUtil.toJSONString(new Message("Success"));

	}

	@POST
	@Path("decrypt/{keyId}")
	@Consumes("text/plain")
	public String decryptMessage(@PathParam("keyId") String keyId, @QueryParam("encryptedData") String encryptedData)
			throws Exception {

		Key key = getKey(keyId);
		if (key == null)
			return JSONUtil.toJSONString(new Message("-1"));

		String decryptedMessage = CryptoUtil.decrypt(encryptedData, key.getKeyPair().getPrivate());
		return JSONUtil.toJSONString(new Message(decryptedMessage));
	}

	@POST
	@Path("encrypt/{keyId}")
	@Consumes("text/plain")
	public String encryptMessage(@PathParam("keyId") String keyId, @QueryParam("data") String data) throws Exception {

		Key key = getKey(keyId);
		if (key == null)
			return JSONUtil.toJSONString(new Message("-1"));

		String encryptedMessage = CryptoUtil.encrypt(data, key.getKeyPair().getPublic());
		return JSONUtil.toJSONString(new Message(encryptedMessage));
	}

	@POST
	@Path("generate")
	@Consumes("text/plain")
	public String generateKey(@QueryParam("size") int size) throws Exception {
		System.out.println("Generated a key with size= " + size);

		KeyPair pair = CryptoUtil.generateKeyPair(size);
		Key key = new Key(getNewKeyID(), pair);
		keys.add(key);

		return JSONUtil.toJSONString(new Message(key.getKeyId()));

	}

	@POST
	@Path("sign/{keyId}")
	@Consumes("text/plain")
	public String sign(@PathParam("keyId") String keyId, @QueryParam("data") String data) throws Exception {

		Key key = getKey(keyId);
		if (key == null)
			return JSONUtil.toJSONString(new Message("-1"));

		String signature = CryptoUtil.sign(data, key.getKeyPair().getPrivate());

		return JSONUtil.toJSONString(new Message(signature));

	}

	@POST
	@Path("verify/{keyId}")
	@Consumes("text/plain")
	public String verify(@PathParam("keyId") String keyId, @QueryParam("data") String data,
			@QueryParam("signature") String signature) throws Exception {

		Key key = getKey(keyId);
		if (key == null)
			return JSONUtil.toJSONString(new Message("-1"));

		boolean isCorrect = CryptoUtil.verify(data, signature, key.getKeyPair().getPublic());

		return JSONUtil.toJSONString(new Message(isCorrect ? "true" : "false"));
	}

	private String getNewKeyID() {
		Server.idCnt += 1;
		return Server.idCnt + "";
	}

	// TODO: change to a HashMap search
	@SuppressWarnings("unused")
	private Key getKey(String keyId) {

		for (int i = 0; i < keys.size(); i++) {

			Key tmp = keys.get(i);
			if (tmp.getKeyId().equals(keyId))
				return tmp;
		}

		return null;
	}

}
