package com.bshara.cryptoserver.Main;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.bshara.cryptoserver.Entities.SignedMessage;
import com.bshara.cryptoserver.Entities.TwoWayKeys;
import com.bshara.cryptoserver.Entities.WebMessage;
import com.bshara.cryptoserver.Entities.WebMessageWithKey;
import com.bshara.cryptoserver.Utils.CryptoUtil;
import com.bshara.cryptoserver.Utils.JSONUtil;

// http://localhost:8080/JRA2/main?a=5

@Path("/main")
public class Server extends Application {

	public Server() {
		// TODO: find a way to use log4j.properties instead of this line
		BasicConfigurator.configure();
	}

	@PersistenceContext

	private static final String OK = "OK";
	private static final String ERROR = "Error";

	private static Logger logger = Logger.getLogger(Server.class);

	private static HashMap<String, KeyPair> keys = new HashMap<String, KeyPair>();
	private static HashMap<String, TwoWayKeys> twoWaykeys = new HashMap<String, TwoWayKeys>();

	private static int idCnt = 0;

	/*
	 * @OPTIONS
	 * 
	 * @Path("/getsample") public Response getOptions() { return
	 * Response.ok().header("Access-Control-Allow-Origin", "*")
	 * .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
	 * .header("Access-Control-Allow-Headers",
	 * "Content-Type, Accept, X-Requested-With").build(); }
	 * 
	 */
	@POST
	@Path("clear")
	@Consumes("text/plain")
	public String clearAllKeys() {
		logger.info("Clearing all keys");
		keys.clear();
		Server.idCnt = 0;

		return JSONUtil.toJSONString(new WebMessage(OK));

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllKeys() throws Exception {

		ArrayList<String> keysIDs = new ArrayList<String>();

		for (Entry<String, KeyPair> key : keys.entrySet()) {
			keysIDs.add(key.getKey());
		}

		String res = JSONUtil.toJSONString(keysIDs);

		return Response.ok(res).header("Access-Control-Allow-Origin", "*").build();

		// return JSONUtil.toJSONString(keysIDs);

		// gave IDs instead of the actual key
		// return JSONUtil.toJSONString(keys);
	}

	@POST
	@Path("connectionRequest/{userId}")
	@Consumes("text/plain")
	public Response clientRequestConnection(@PathParam("userId") String userId, @QueryParam("pass") String pass,
			@QueryParam("publickey") String encodeUserPublicKey) throws Exception {

		WebMessageWithKey clr;
		if (isUserVerified(userId, pass)) {

			// Generate key pair
			KeyPair pair = CryptoUtil.generateKeyPair(2048);

			// encode public key to send it to the user
			String serverDecodedPublicKey = Base64.getUrlEncoder().encodeToString(pair.getPublic().getEncoded());

			// get the users public key
			PublicKey userPublicKey = CryptoUtil.getKeyFromEncodedBase64UrlRSA(encodeUserPublicKey);

			// store your keys along with the users public key
			TwoWayKeys twk = new TwoWayKeys(pair, userPublicKey);

			// TODO: store in database instead
			twoWaykeys.put(userId, twk);

			// send the user your public key with an OK message
			clr = new WebMessageWithKey(OK, serverDecodedPublicKey);

		} else {
			// send the user an error message
			clr = new WebMessageWithKey(ERROR, "");
		}

		return Response.ok(JSONUtil.toJSONString(clr)).build();
	}

	// TODO: implement this after adding a database
	private boolean isUserVerified(String userId, String pass) {
		return true;
	}

	// TODO: implement this after adding a database
	private boolean isUserLoggedIn(String userId) {
		return true;
	}

	@POST
	@Path("send/{userId}")
	@Consumes("text/plain")
	public Response send(@PathParam("userId") String userId, @QueryParam("message") String message) throws Exception {

		WebMessage clr;
		if (isUserLoggedIn(userId)) {

			// get key pair related to this user
			TwoWayKeys twk = twoWaykeys.get(userId);

			SignedMessage signedMessage = CryptoUtil.decryptAndVerify(message, twk.getMyKeys().getPrivate(),
					twk.getOthersPublicKey());

			if (signedMessage.isVerified()) {

				/* apply different command */

				// send the user the same message to indicate success
				// TODO: expand this to handle multiple commands
				clr = new WebMessage(CryptoUtil.signAndEncrypt(signedMessage.getContent(), twk.getMyKeys().getPrivate(), twk.getOthersPublicKey()));
			} else {
				clr = new WebMessage(ERROR);
			}

		} else {
			// send the user an error message
			clr = new WebMessage(ERROR);
		}

		// TODO: encrypt the response using the users public key
		return Response.ok(JSONUtil.toJSONString(clr)).build();
	}

	/*
	 * @GET
	 * 
	 * @Path("key/{keyId}")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response getPublicKey() throws
	 * Exception {
	 * 
	 * ArrayList<String> keysIDs = new ArrayList<String>();
	 * 
	 * for (Entry<String, KeyPair> key : keys.entrySet()) {
	 * keysIDs.add(key.getKey()); }
	 * 
	 * String res = JSONUtil.toJSONString(keysIDs);
	 * 
	 * return Response.ok(res).header("Access-Control-Allow-Origin", "*").build();
	 * 
	 * // return JSONUtil.toJSONString(keysIDs);
	 * 
	 * // gave IDs instead of the actual key // return JSONUtil.toJSONString(keys);
	 * }
	 * 
	 * 
	 * 
	 * @POST
	 * 
	 * @Path("decrypt/{keyId}")
	 * 
	 * @Consumes("text/plain") public String decryptMessage(@PathParam("keyId")
	 * String keyId, @QueryParam("encryptedData") String encryptedData) throws
	 * Exception {
	 * 
	 * Key key = getKey(keyId); if (key == null) return JSONUtil.toJSONString(new
	 * Message("-1"));
	 * 
	 * String decryptedMessage = CryptoUtil.decrypt(encryptedData,
	 * key.getKeyPair().getPrivate()); return JSONUtil.toJSONString(new
	 * Message(decryptedMessage)); }
	 * 
	 * @POST
	 * 
	 * @Path("encrypt/{keyId}")
	 * 
	 * @Consumes("text/plain") public String encryptMessage(@PathParam("keyId")
	 * String keyId, @QueryParam("data") String data) throws Exception {
	 * 
	 * logger.info("encryptMessage : keyID : " + keyId);
	 * logger.info(keys.entrySet()); Key key = getKey(keyId);
	 * 
	 * if (key == null) return JSONUtil.toJSONString(new Message("-1"));
	 * 
	 * String encryptedMessage = CryptoUtil.encrypt(data,
	 * key.getKeyPair().getPublic()); return JSONUtil.toJSONString(new
	 * Message(encryptedMessage)); }
	 * 
	 * @POST
	 * 
	 * @Path("generate")
	 * 
	 * @Consumes("text/plain") public String generateKey(@QueryParam("size") int
	 * size) throws Exception { logger.info("Generated a key with size= " + size);
	 * 
	 * KeyPair pair = CryptoUtil.generateKeyPair(size); String id = getNewKeyID();
	 * Key key = new Key(id, pair); keys.put(id, key);
	 * 
	 * return JSONUtil.toJSONString(new Message(key.getKeyId()));
	 * 
	 * }
	 * 
	 * @POST
	 * 
	 * @Path("sign/{keyId}")
	 * 
	 * @Consumes("text/plain") public String sign(@PathParam("keyId") String
	 * keyId, @QueryParam("data") String data) throws Exception {
	 * 
	 * Key key = getKey(keyId);
	 * 
	 * if (key == null) return JSONUtil.toJSONString(new Message("-1"));
	 * 
	 * String signature = CryptoUtil.sign(data, key.getKeyPair().getPrivate());
	 * 
	 * return JSONUtil.toJSONString(new Message(signature));
	 * 
	 * }
	 * 
	 * @POST
	 * 
	 * @Path("verify/{keyId}")
	 * 
	 * @Consumes("text/plain") public String verify(@PathParam("keyId") String
	 * keyId, @QueryParam("data") String data,
	 * 
	 * @QueryParam("signature") String signature) throws Exception {
	 * 
	 * Key key = getKey(keyId); if (key == null) return JSONUtil.toJSONString(new
	 * Message("-1"));
	 * 
	 * boolean isCorrect = CryptoUtil.verify(data, signature,
	 * key.getKeyPair().getPublic());
	 * 
	 * return JSONUtil.toJSONString(new Message(isCorrect ? "true" : "false")); }
	 * 
	 * private String getNewKeyID() { Server.idCnt += 1; return Server.idCnt + ""; }
	 * 
	 * private Key getKey(String keyId) {
	 * 
	 * return keys.get(keyId); }
	 * 
	 */

}
