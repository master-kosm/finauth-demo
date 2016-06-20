package ru.kosm.finauth.rest;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.FinauthApp;
import ru.kosm.finauth.core.AppContext;
import ru.kosm.finauth.domain.User;

@Path("/finauth")
public class Resource {
	
	private final static transient Logger logger = LogManager.getLogger(Resource.class);
	
	private final AppContext appContext;
	
	public Resource(final AppContext appContext) {
		this.appContext = appContext;
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String ping() {
		return "PONG";
	}
	
	@POST
	@Path("/add_user")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public AddUserResponse addUser(Map<String, Object> request) {
		logger.trace("Add user");
		AddUserResponse response = new AddUserResponse();
		/*User user = appContext.getUserManager()
				.addUser(request.getFirstName(), request.getLastName(), request.getLogin());
		response.setUserId(user.getId()); */
		return response;
	}
}
