package com.bshara.cryptoserver.Main;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
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

import org.apache.catalina.filters.CorsFilter;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.alibaba.fastjson.JSON;
import com.bshara.cryptoserver.Entities.SignedMessage;
import com.bshara.cryptoserver.Entities.TwoWayKeys;
import com.bshara.cryptoserver.Entities.WebMessage;
import com.bshara.cryptoserver.Entities.WebMessageWithKey;
import com.bshara.cryptoserver.Utils.CryptoUtil;
import com.bshara.cryptoserver.Utils.JSONUtil;
import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.RSA_ICE;
import com.bshara.cryptoserver.Utils.Entities.CryptoMessager;
import com.bshara.cryptoserver.Utils.Entities.DummyKeys;
import com.bshara.cryptoserver.Utils.Entities.MessageBuilder;
import com.bshara.cryptoserver.Utils.Entities.StatusedMessage;
import com.bshara.cryptoserver.Utils.Keys.Key;

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

	private static TreeMap<String, TreeMap<String, Object>> users = new TreeMap<String, TreeMap<String, Object>>();
	private static ArrayList<String> groupChat = new ArrayList<String>();

	static {

		TreeMap<String, Object> data = MessageBuilder.createMessage().add("publicKey", DummyKeys.clientPublicKey)
				.add("password", "123").build();

		users.put("bshara", data);

	}

	private static int idCnt = 0;
	CorsFilter dds;

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
	public Response clearAllKeys() {
		logger.info("Clearing all keys");
		keys.clear();
		Server.idCnt = 0;

		return Response.ok(JSONUtil.toJSONString(new WebMessage(OK))).header("Access-Control-Allow-Origin", "*")
				.build();

	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("login/{username}")
	@Consumes("text/plain")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(HttpServletRequest request, @PathParam("username") String username,
			@QueryParam("msg") String msg) throws Exception {

		String str = new String(Base64.getUrlDecoder().decode(msg));
		TreeMap<String, Object> signedEncryptedMessage = JSON.parseObject(str, TreeMap.class);
		TreeMap<String, Object> decryptedMessage = CryptoMessager.Decrypt(signedEncryptedMessage,
				DummyKeys.serverPrivateKey, DummyKeys.clientPublicKey);

		if (!users.containsKey(username)) {
			return Response.ok(JSONUtil.toJSONString(new StatusedMessage(ERROR, "user doesn't exist"))).build();
		}

		String password = (String) decryptedMessage.get("password");
		String storedPassword = (String) users.get(username).get("password");

		if (storedPassword.equals(password)) {
			return Response.ok(JSONUtil.toJSONString(new StatusedMessage(OK, "login success"))).build();

		}

		return Response.ok(JSONUtil.toJSONString(new StatusedMessage(ERROR, "login fail"))).build();
	}

	@POST
	@Path("allmessages")
	@Consumes("text/plain")

	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMessages(HttpServletRequest request) throws Exception {

		StatusedMessage res = new StatusedMessage(OK, JSONUtil.toJSONString(groupChat));
		return Response.ok(JSONUtil.toJSONString(res)).build();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("chatx/{username}")
	@Consumes("text/plain")
	@Produces(MediaType.APPLICATION_JSON)
	public Response chat(HttpServletRequest request, @PathParam("username") String username,
			@QueryParam("msg") String msg) throws Exception {

		Key k = Keys.INSTANCE.getKey(0);
		int myD = k.d;
		int e = k.e;
		int myN = k.n;
		int n = k.n;

		logger.log(Priority.DEBUG, msg);

		groupChat.add(msg);

		String c = new String(Base64.getUrlDecoder().decode(msg));
		TreeMap<String, String> map = RSA_ICE.Decrypt(c, myD, myN, e, n);

		String decryptedM = map.get("m");
		String isSignValid = map.get("sign");

		logger.log(Priority.DEBUG, decryptedM);
		logger.log(Priority.DEBUG, isSignValid);

		return Response.ok(JSONUtil.toJSONString(new StatusedMessage(ERROR, "login fail"))).build();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("chat/{username}")
	@Consumes("text/plain")
	@Produces(MediaType.APPLICATION_JSON)
	public Response chat_old(HttpServletRequest request, @PathParam("username") String username,
			@QueryParam("msg") String msg) throws Exception {

		String str = new String(Base64.getUrlDecoder().decode(msg));
		TreeMap<String, Object> signedEncryptedMessage = JSON.parseObject(str, TreeMap.class);
		TreeMap<String, Object> decryptedMessage = CryptoMessager.Decrypt(signedEncryptedMessage,
				DummyKeys.serverPrivateKey, DummyKeys.clientPublicKey);

		if (!users.containsKey(username)) {
			return Response.ok(JSONUtil.toJSONString(new StatusedMessage(ERROR, "user doesn't exist"))).build();
		}

		String password = (String) decryptedMessage.get("password");
		String storedPassword = (String) users.get(username).get("password");

		if (storedPassword.equals(password)) {

			if (!decryptedMessage.containsKey("message")) {
				return Response.ok(JSONUtil.toJSONString(new StatusedMessage(ERROR, "No message field"))).build();
			}

			String message = (String) decryptedMessage.get("message");
			TreeMap<String, Object> messageObject = MessageBuilder.createMessage()
					.add("message", "server says:" + message).build();
			TreeMap<String, Object> encryptedMsg = CryptoMessager.Encrypt(messageObject, DummyKeys.serverPrivateKey,
					DummyKeys.clientPublicKey);
			String uriFriendlyFormat = Base64.getUrlEncoder()
					.encodeToString(JSON.toJSONString(encryptedMsg).getBytes());

			return Response.ok(JSONUtil.toJSONString(new StatusedMessage(OK, uriFriendlyFormat))).build();

		}

		return Response.ok(JSONUtil.toJSONString(new StatusedMessage(ERROR, "login fail"))).build();
	}

	@GET
	@Path("a")
	@Produces(MediaType.APPLICATION_JSON)
	public Response a() throws Exception {
		return Response.ok(JSONUtil.toJSONString(new WebMessage("a"))).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllKeys() throws Exception {

		ArrayList<String> keysIDs = new ArrayList<String>();

		for (Entry<String, KeyPair> key : keys.entrySet()) {
			keysIDs.add(key.getKey());
		}

		String res = JSONUtil.toJSONString(keysIDs);

		return Response.ok(res).build();

		// return JSONUtil.toJSONString(keysIDs);

		// gave IDs instead of the actual key
		// return JSONUtil.toJSONString(keys);
	}

	@POST
	@Path("connectionRequest/{userId}")
	@Consumes("text/plain")
	public Response clientRequestConnection(HttpServletRequest request, @PathParam("userId") String userId,
			@QueryParam("pass") String pass, @QueryParam("publickey") String encodeUserPublicKey) throws Exception {

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
				clr = new WebMessage(CryptoUtil.signAndEncrypt(signedMessage.getContent(), twk.getMyKeys().getPrivate(),
						twk.getOthersPublicKey()));
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

}
