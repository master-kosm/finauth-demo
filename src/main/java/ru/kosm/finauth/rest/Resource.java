package ru.kosm.finauth.rest;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.core.AppContext;

@Path("/finauth")
public class Resource {
	
	private final static transient Logger logger = LogManager.getLogger(Resource.class);
	
	private final AppContext appContext;
	
	public Resource(final AppContext appContext) {
		this.appContext = appContext;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> process(Map<String, Object> request) {
		logger.trace("Received request");
		Map<String, Object> response = appContext.getProcessor().process(request);
		logger.trace("Sending out response");
		
		return response;
	}
}
