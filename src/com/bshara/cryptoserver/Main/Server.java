package com.bshara.cryptoserver.Main;

import java.util.ArrayList;
import java.util.Base64;
import java.util.TreeMap;

import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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

import com.bshara.cryptoserver.Utils.JSONUtil;
import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.Keys.Key;
import com.bshara.cryptoserver.Utils.RSA_ICE;
import com.bshara.cryptoserver.Utils.Entities.MessageBuilder;
import com.bshara.cryptoserver.Utils.Entities.StatusedMessage;

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


	private static ArrayList<String> groupChat = new ArrayList<String>();


	CorsFilter dds;

	
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

}
