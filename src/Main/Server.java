package Main;

import java.io.IOException;
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

// http://localhost:8080/JRA2/main?a=5

@Path("/main")
public class Server {
	@PersistenceContext
	private static ArrayList<Key> keys = new ArrayList<Key>();


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllKeys() {
		return MyUtil.toJSONString(keys);
	}
	
	
	
	@POST
	@Path("decrypt/{keyId}")
	@Consumes("text/plain")
	public void decryptKey(@PathParam("keyId") String id, @QueryParam("encryptedData") String encryptedData) {
		System.out.println("POST CALLED");
	    System.out.println("ID="+id+" xxxData="+encryptedData);
	}
	
	@POST
	@Path("encrypt/{keyId}")
	@Consumes("text/plain")
	public void encryptKey(@PathParam("keyId") String id, @QueryParam("data") String data) {
		System.out.println("POST CALLED");
	    System.out.println("ID="+id+" xxxData="+data);
	}
	
	
	@POST
	@Path("generate")
	@Consumes("text/plain")
	public String generateKey(@QueryParam("size") int size) {
	    System.out.println("Generated a key with size="+size);
	    String str = keys.size()+"";
	    Key key = new Key(str, str);
	    keys.add(key);
	    
		return MyUtil.toJSONString(new Message(key.toString()));

	}

	@POST
	@Path("sign/{keyId}")
	@Consumes("text/plain")
	public String sign(@PathParam("keyId") String id, @QueryParam("data") String data) {
		System.out.println("POST CALLED");
	    System.out.println("ID="+id+" xxxData="+data);
	    return MyUtil.toJSONString(new Message("success"));
	}


	/**
	 * Verify the given signature and data using the given key
	 * verify/awd?data=awd&signature=awd 
	 * */
	@POST
	@Path("verify/{keyId}")
	@Consumes("text/plain")
	public String verify(@PathParam("keyId") String id, @QueryParam("data") String data, @QueryParam("signature") String signature) {
		System.out.println("Verify the given signature and data using the given key");
	    System.out.println("ID="+id);
	    System.out.println("data="+data);
	    System.out.println("signature="+signature);
	    
	    return MyUtil.toJSONString(new Message("success"));
	}
	

	
}


