package org.yeastrc.paws.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import org.jobcenter.constants.JobStatusValuesConstants;
import org.jobcenter.constants.RunMessageTypesConstants;
import org.jobcenter.exceptions.JobcenterModuleInterfaceInvalidCharactersInStringException;
import org.jobcenter.exceptions.JobcenterSystemErrorException;
import org.jobcenter.job_client_module_interface.ModuleInterfaceClientMainInterface;
import org.jobcenter.job_client_module_interface.ModuleInterfaceClientServices;
import org.jobcenter.job_client_module_interface.ModuleInterfaceJobProgress;
import org.jobcenter.job_client_module_interface.ModuleInterfaceRequest;
import org.jobcenter.job_client_module_interface.ModuleInterfaceResponse;
import org.yeastrc.paws.base.constants.JobcenterConstants;
import org.yeastrc.paws.config.GetConfigFromFileServiceImpl;
import org.yeastrc.paws.server_communication.SendResultsToServer;

/**
 *
 *
 */
public class RunProgramJobMgrModule implements ModuleInterfaceClientMainInterface {


	private static Logger log = Logger.getLogger(RunProgramJobMgrModule.class);


	private static final String MODULE_LABEL = "Run Program";

	private RunProgramMainSendResults runProgramMainSendResults;

	//////////////////////////////////

	//  Values from config properties file

	private File tempBaseDirectory;


	//  has a shutdown request been received.  Volatile since the shutdown request will be on a different thread

	private volatile boolean shutdownRequested;


	//  Has a stopRunningAfterProcessingJob request been received.
	//      Volatile since the stopRunningAfterProcessingJob request will be on a different thread

	private volatile boolean stopRunningAfterProcessingJobRequested = false;


	/////////////////////////////////

	//  has the module been initialized

	private boolean initialized = false;

	/////////////////////////////////


	//////////////////////////////////////////////////////////////////////////


//	Job Manager lifecycle methods


	/**
	 * Called when the object is instantiated.
	 * The object will not be used to process requests if this method throws an exception
	 *
	 * @param moduleInstanceNumber - The instance number of this module in this client,
	 *         incremented for each time the module instantiated while the client is running,
	 *         reset to 1 when the client is restarted.
	 *         This can be useful for separating logging or other file resources between
	 *         instances of the same module in the same client.
	 * @throws Throwable
	 */
	@Override
	public void init(int moduleInstanceNumber) throws Throwable {

//		ClassLoader thisClassLoader = this.getClass().getClassLoader();
//
//		InputStream temp =  thisClassLoader.getResourceAsStream( LOG4J_CONFIG_FILENAME );
//
//		//  Configure log4j
//		new DOMConfigurator().doConfigure(temp, LogManager.getLoggerRepository());

		log.info( "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Starting " + MODULE_LABEL + " .....  Called init()" );


//		URL log4jFile = thisClassLoader.getResource( LOG4J_CONFIG_FILENAME );
//
//		log.info( "log4j file '" + LOG4J_CONFIG_FILENAME + "' load path = " + log4jFile.getFile() );


		String tempBaseDirectoryString = GetConfigFromFileServiceImpl.getTempRootDirectory();

		tempBaseDirectory = new File( tempBaseDirectoryString );

		if ( ! tempBaseDirectory.exists() ) {

			String msg = "tempBaseDirectory (" + tempBaseDirectory.getAbsolutePath() + " does not exist.";

			log.error( msg );

			throw new Exception( msg );
		}


		if ( ! tempBaseDirectory.canWrite() ) {

			String msg = "tempBaseDirectory (" + tempBaseDirectory.getAbsolutePath() + " is not writeable.";

			log.error( msg );

			throw new Exception( msg );
		}

		initialized = true;
	}



	/**
	 * Called when the object will no longer be used
	 */
	@Override
	public void destroy() {

		log.info( "destroy() called " );

		if ( runProgramMainSendResults != null ) {
			try {
				runProgramMainSendResults.destroy();

			} catch ( Throwable ex ) {

				log.error(" Exception while running " + MODULE_LABEL + ", calling 'pawsMain.destroy();'.  This error may have already been logged and emailed.  Exception = " + ex.toString(), ex );
			}

			runProgramMainSendResults = null;
		}


		log.info( "destroy()  exiting " );
	}




	@Override
	/**
	 * Called on a separate thread when a shutdown request comes from the operating system.
	 * If this is not heeded, the process may be killed by the operating system after some time has passed ( controlled by the operating system )
	 */
	public void shutdown() {


		log.info( "shutdown() called " );


		shutdownRequested = true;


		if ( runProgramMainSendResults != null ) {

			runProgramMainSendResults.shutdown();
		}
		
		SendResultsToServer.shutdown();


		log.info( "shutdown() exitting " );


	}


	@Override
	/**
	 * Called on a separate thread when a request comes from the user via updating the control file.
	 * The user has indicated that they want the job manager client to to stop processing new jobs
	 * and complete the jobs that are running.
	 *
	 * The module needs this information since if it is in an endless retry mode to submit the next job or
	 *   other efforts, it needs to fail the job and return.
	 */
	public void stopRunningAfterProcessingJob() {


		stopRunningAfterProcessingJobRequested = true;

	}

	/**
	 * called before and possibly after each request is processed to clear temp parameters and stored errors
	 */
	@Override
	public void reset() throws Throwable {

		if ( ! initialized ) {

			throw new Exception( "Module has not been initialized" );
		}

		if ( runProgramMainSendResults != null ) {

			runProgramMainSendResults.reset();
		}

	}


	/**
	 * Process the request
	 */
	@Override
	public void processRequest( ModuleInterfaceRequest moduleInterfaceRequest, ModuleInterfaceResponse moduleInterfaceResponse, ModuleInterfaceJobProgress moduleInterfaceJobProgress, ModuleInterfaceClientServices moduleInterfaceClientServices  ) throws Throwable {

		final String methodName = "processRequest";

		if ( ! initialized ) {

			throw new Exception( "Module has not been initialized" );
		}
		
		int jobcenterRequestId = moduleInterfaceRequest.getRequestId();
		
		int numberOfThreadsForRunningJob = moduleInterfaceRequest.getNumberOfThreadsForRunningJob();
		

		log.info( "Entering Module Run Disopred 3, RunProgramJobMgrModule::processRequest() called.  jobcenterRequestId: " + jobcenterRequestId
				+ ", numberOfThreadsForRunningJob: " + numberOfThreadsForRunningJob );


		Map<String, String> jobParameters = moduleInterfaceRequest.getJobParameters();

		//////////////////////////

		//  Init here since destroy() after each load to close db connections so don't keep them open while not loading files.

		runProgramMainSendResults = RunProgramMainSendResults.getInstance();

		//////////////////////////


		String tempDirectoryString = jobcenterRequestId + Long.toString( System.currentTimeMillis() );;

		try {


			runProgramMainSendResults.init();

			String trackingIdString = jobParameters.get( JobcenterConstants.JOB_PARAM_TRACKING_ID );
			if ( StringUtils.isEmpty( trackingIdString ) ) {
				String msg = methodName + ": job parameter " + JobcenterConstants.JOB_PARAM_TRACKING_ID + " cannot be empty or null.";
				log.error( msg );
				throw new Exception( msg );
			}

			String serverBaseURL = jobParameters.get( JobcenterConstants.JOB_PARAM_SERVER_BASE_URL );

			if ( StringUtils.isEmpty( serverBaseURL ) ) {

				String msg = methodName + ": job parameter " + JobcenterConstants.JOB_PARAM_SERVER_BASE_URL + " cannot be empty or null.";

				log.error( msg );

				throw new Exception( msg );
			}

			int trackingId = -1;

			try {
				trackingId = Integer.parseInt( trackingIdString );

			} catch( Throwable t ) {
				String msg = methodName + ": job parameter " + JobcenterConstants.JOB_PARAM_TRACKING_ID 
						+ "  is not an integer, is = |" + trackingIdString + "|." ;
				log.error( msg );
				throw new Exception( msg );
			}
			
			
			if ( log.isInfoEnabled() ) {
				log.info( "Parameters from job:  '" 
						+ JobcenterConstants.JOB_PARAM_TRACKING_ID + "' " + " = '" + trackingId 
						+ "', '" + JobcenterConstants.JOB_PARAM_SERVER_BASE_URL + "' = '" +  serverBaseURL
						+ "'.  tempBaseDirectory = " + tempBaseDirectory );
			}


			runProgramMainSendResults.processRequest( trackingId,
					tempBaseDirectory, tempDirectoryString, 
					numberOfThreadsForRunningJob,
					jobcenterRequestId,
					serverBaseURL
					);

			moduleInterfaceResponse.setStatusCode( JobStatusValuesConstants.JOB_STATUS_FINISHED );

			moduleInterfaceResponse.addRunMessage( RunMessageTypesConstants.RUN_MESSAGE_TYPE_MSG , "Successful completion" );





		} catch ( Throwable t ) {

			log.error("General Exception: " + MODULE_LABEL + ".  This error may have already been logged and emailed.  Exception = " + t.toString(), t );

			moduleInterfaceResponse.setStatusCode( JobStatusValuesConstants.JOB_STATUS_HARD_ERROR );



			//  TODO   Improve exception reporting

			ByteArrayOutputStream baos = new ByteArrayOutputStream( 1000 );

			PrintStream printStream = new PrintStream( baos );

			t.printStackTrace( printStream );

			printStream.close();

			String exStackTrace = baos.toString();



			String statusMsg = "Failed Completion. Exception type = " + t.getClass().getName() + ", Exception string = " + t.toString()
					+ "\n" + exStackTrace;



			try {

				//  Allow cleaning of invalid for XML Chars and to ASCII if needed
				moduleInterfaceResponse.addRunMessageAutoCleanStringToASCII( RunMessageTypesConstants.RUN_MESSAGE_TYPE_ERROR, statusMsg, "?" );
				
			} catch ( JobcenterModuleInterfaceInvalidCharactersInStringException ex ) {
				
				//  The passed in string was still invalid after cleaning
				
				String msg = "ERROR:  Failed to add message to jobcenter job completion response due to invalid characters.";

				log.error( msg + "  status message is: " + statusMsg, ex );

				moduleInterfaceResponse.addRunMessageAutoCleanStringToASCII( RunMessageTypesConstants.RUN_MESSAGE_TYPE_ERROR,
						msg + "  Status message is written to log on machine where the job was run.", "?" );
						
			} catch ( JobcenterSystemErrorException ex ) {
				
				//  Jobcenter internals encountered an error while validating the data
				
				String msg = "ERROR:  Failed to add message to jobcenter job completion response due to JobcenterSystemErrorException.";

				log.error( msg + "  status message is: " + statusMsg, ex );

				moduleInterfaceResponse.addRunMessageAutoCleanStringToASCII( RunMessageTypesConstants.RUN_MESSAGE_TYPE_ERROR,
						msg + "  Status message is written to log on machine where the job was run.", "?" );
			}


		}


		log.info( "Exitting Module " + MODULE_LABEL + " , processRequest() ." );

	}






	////////////////////////////////////////////////////////////////////////////////////////////



}
