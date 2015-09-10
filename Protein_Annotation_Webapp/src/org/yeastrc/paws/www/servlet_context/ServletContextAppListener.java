package org.yeastrc.paws.www.servlet_context;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.yeastrc.paws.www.constants.ConfigSystemsKeysConstants;
import org.yeastrc.paws.www.constants.WebConstants;
import org.yeastrc.paws.www.dao.ConfigSystemDAO;


/**
 * This class is loaded and the method "contextInitialized" is called when the web application is first loaded by the container
 *
 */
public class ServletContextAppListener extends HttpServlet implements ServletContextListener {

	private static Logger log = Logger.getLogger( ServletContextAppListener.class );
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {

		ServletContext context = event.getServletContext();

		String contextPath = context.getContextPath();

		context.setAttribute( WebConstants.APP_CONTEXT_CONTEXT_PATH, contextPath );

		CurrentContext.setCurrentWebAppContext( contextPath );
		
		
		

		log.warn( "INFO:  !!!!!!!!!!!!!!!   Start up of web app  'PAWS' Protein Annotation Webservices  !!!!!!!!!!!!!!!!!!!! " );

		log.warn( "INFO: Application context values set.  Key = " + WebConstants.APP_CONTEXT_CONTEXT_PATH + ": value = " + contextPath
				+ "" );

		
		
		//  Validate retrieve one value from config_system table on web app startup
		
		
		
		String sendResultsURL = null;
		
		try {
			sendResultsURL = ConfigSystemDAO.getInstance().getValueFromKey( ConfigSystemsKeysConstants.SERVER_URL_FOR_MODULE_DATA_KEY );
			
		} catch (Exception e) {

			String msg = "Failed to get sendResultsURL from ConfigSystemDAO for key '" + ConfigSystemsKeysConstants.SERVER_URL_FOR_MODULE_DATA_KEY + "'." ;

			log.error( msg, e );
			
			throw new RuntimeException( msg, e );
		}
		
		if ( sendResultsURL == null ) {
			
			String msg = "Failed to get sendResultsURL from ConfigSystemDAO for key '" + ConfigSystemsKeysConstants.SERVER_URL_FOR_MODULE_DATA_KEY + "'." ;

			log.error( msg );
			
			throw new RuntimeException( msg );
		}
		

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {

		//ServletContext context = event.getServletContext();


	}



}
