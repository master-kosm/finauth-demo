package ru.kosm.finauth;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import ru.kosm.finauth.core.AppContext;
import ru.kosm.finauth.rest.Resource;

/** Main application class
 * 
 * @author kosm
 */
public class FinauthApp {
	
	/** Default timeout the HTTP server waits before force shutdown, value = {@value} */
	private final static int shutdownTimeout = 5000; // ms
	
	private final static transient Logger logger = LogManager.getLogger(FinauthApp.class);
	
	private final HttpServer httpServer;
	private final AppContext appContext = new AppContext();
	
	public final static int defaultPort = 8800;
	public final static String defaultHost = "localhost";
	
	/** The constructor initializes the application
	 */
	public FinauthApp() {
		final int port = Integer.getInteger("ListenPort", defaultPort);
		final String host = System.getProperty("BindHost", defaultHost);
				
		// Using the embedded HTTP server Grizzly 2
		URI baseUri = UriBuilder.fromUri("http://" + host + "/").port(port).build();
		ResourceConfig config = new ResourceConfig().register(JacksonFeature.class);
		config.registerInstances(new Resource(appContext));
		httpServer = GrizzlyHttpServerFactory.createHttpServer(baseUri, config, false);
		
		logger.info("Finauth demo application started.");
	}
	
	/** Start the listening HTTP server
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		httpServer.start();
	}
	
	/** Stop the listening HTTP server. The application is also stopped.
	 */
	public void stop() {
		logger.info("About to stop the application.");
		appContext.dispose();
		httpServer.shutdown(shutdownTimeout, TimeUnit.MILLISECONDS);
		logger.info("Stopped.");
	}

	public static void main(String[] args) {
		try {
			final FinauthApp app = new FinauthApp();
			
			// Exiting the app by SIGINT (Ctrl-C)
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					app.stop();
				}
			});
			app.start();
		}
		catch (Exception e) {
			logger.error("Uncaught exception", e);
		}
	}

}
