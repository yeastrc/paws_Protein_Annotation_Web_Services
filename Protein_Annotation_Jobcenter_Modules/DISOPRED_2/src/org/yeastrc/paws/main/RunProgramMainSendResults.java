package org.yeastrc.paws.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.paws.base.constants.ModuleRunProgramStatusConstants;
import org.yeastrc.paws.config.GetConfigFromFileServiceImpl;
import org.yeastrc.paws.constants.DisopredFilenameConstants;
import org.yeastrc.paws.program_runner.Copy_ncbirc_file_to_TempDir;
import org.yeastrc.paws.program_runner.ProgramRunner;
import org.yeastrc.paws.program_runner.DisopredResultsParser;
import org.yeastrc.paws.program_runner.TempDirCleaner;
import org.yeastrc.paws.server_communication.GetAnnotationDataPartsForTrackingId;
import org.yeastrc.paws.server_communication.GetAnnotationDataPartsForTrackingId.GetAnnotationDataPartsForTrackingIdResult;
import org.yeastrc.paws.server_communication.SendResultsToServer;
import org.yeastrc.paws.utils.GetJSONForObject;
import org.yeastrc.paws.dto.DisopredParsedResults;



/**
 *
 *
 */
public class RunProgramMainSendResults {


	private static Logger log = Logger.getLogger(RunProgramMainSendResults.class);

//	private static final int WAIT_FOR_COMPLETE_COUNTER = 5;
//
//	private static final int WAIT_FOR_COMPLETE_TIME_EACH_COUNT = 5 * 1000; // 5 seconds


	//  has a shutdown request been received.  Volatile since the shutdown request will be on a different thread

//	private volatile boolean shutdownRequested;


//	private ClassLoader thisClassLoader = this.getClass().getClassLoader();


	//  Private constructor

	private RunProgramMainSendResults() {


	}


	/**
	 * @return
	 */
	public static RunProgramMainSendResults getInstance() {

		return new RunProgramMainSendResults();
	}

	/**
	 *
	 */
	public void init() throws Throwable {



	}


	/**
	 * Called when the object will no longer be used
	 */
	public void destroy() {

	}


	/**
	 * Called on a separate thread when a shutdown request comes from the operating system.
	 * If this is not heeded, the process may be killed by the operating system after some time has passed ( controlled by the operating system )
	 */
	public void shutdown() {


		log.info( "shutdown() called " );

//		shutdownRequested = true;

		awaken();


		log.info( "shutdown() exitting " );
	}


	/**
	 * awaken thread to process request
	 */
	private void awaken() {

		synchronized (this) {

			notify();
		}

	}

	/**
	 * called before and possibly after each request is processed to clear input parameters and stored errors
	 */
	public void reset() throws Throwable {


//		restartExperimentId = 0;
//
//		restartImageNumber = 0;
	}


	/**
	 * @param trackingId
	 * @param tempBaseDirectory
	 * @param tempDirectoryString
	 * @param numberOfThreadsForRunningJob
	 * @param jobcenterRequestId
	 * @param serverBaseURL
	 * @throws Throwable
	 */
	public void processRequest( int trackingId, 
			File tempBaseDirectory, 
			String tempDirectoryString, 
			int numberOfThreadsForRunningJob,
			int jobcenterRequestId,
			String serverBaseURL )throws Throwable {

		boolean forceSpecifyTempDirNameSpecified = false;
		if ( StringUtils.isNotEmpty( GetConfigFromFileServiceImpl.getForceSpecifyTempDirName() ) ) {
			//  If ForceSpecifyTempDirName populated, use it
			tempDirectoryString = GetConfigFromFileServiceImpl.getForceSpecifyTempDirName();
			forceSpecifyTempDirNameSpecified = true;
		} else {
			
		}
		
		File tempDirectory = new File( tempBaseDirectory, tempDirectoryString );

		try {

			GetAnnotationDataPartsForTrackingIdResult getAnnotationDataPartsForTrackingIdResult =
					GetAnnotationDataPartsForTrackingId.getProteinSequenceForTrackingId( trackingId, serverBaseURL );
			
			if ( getAnnotationDataPartsForTrackingIdResult.isAlreadyComputed() ) {
				//  Already processed so skip
				if ( log.isInfoEnabled() ) {
					log.info( "Annotation Data already computed for trackingId " + trackingId + " so early exit." );
				}
				return;  //  EARLY EXIT
			}

			//  create temp directory
			
			if ( forceSpecifyTempDirNameSpecified ) {
				// Validate the dir exists
				if ( ! tempDirectory.exists() ) {
					String msg = "temp directory does not exist: " + tempDirectory.getAbsolutePath();
					log.error( msg );
					throw new Exception(msg);
				}
			} else {
				//  Only create temp dir if "Force..." is not specified

				if ( ! tempDirectory.mkdir() ) {
					String msg = "Failed to make temp directory: " + tempDirectory.getAbsolutePath();
					log.error( msg );
					throw new Exception(msg);
				}
			}
			
			//  Create input file to program
			File programInputFile = createInputFile( getAnnotationDataPartsForTrackingIdResult.getSequence(), tempDirectory) ;
			
			//  Copy the 'ncbirc' file from config to the temp dir
			
			Copy_ncbirc_file_to_TempDir.copy_ncbirc_file_to_TempDir(tempDirectory);
			
			
			///  Run program in temp directory
			
//			RunSystemCommandResponse runSystemCommandResponse =
					ProgramRunner.runProgram( programInputFile, tempDirectory, numberOfThreadsForRunningJob );  //  Throws Exception if return code not zero
			
			
			DisopredParsedResults disopredParsedResults = DisopredResultsParser.parseResults( tempDirectory );
			
			if ( log.isInfoEnabled() ) {
				
				log.info( "After Parse results, before convert parsed results into JSON.  jobcenterRequestId: " + jobcenterRequestId );
				
			}

			// build the JSON data structure for result

			String disopredParsedResultsString = GetJSONForObject.getJSONForObject( disopredParsedResults );

			
			
			if ( log.isInfoEnabled() ) {
				
				log.info( "After convert parsed results into JSON, before Send results SUCCESS.  jobcenterRequestId: " + jobcenterRequestId );
				
			}

			
			//////  Send result

			SendResultsToServer.send( trackingId, 
					disopredParsedResultsString /* annotationData */, 
					ModuleRunProgramStatusConstants.STATUS_SUCCESS, 
					serverBaseURL );
			
			if ( log.isInfoEnabled() ) {
				
				log.info( "After before Send results for Success.  jobcenterRequestId: " + jobcenterRequestId );
				
			}
			
		} catch ( Throwable t ) {

			log.error( "jobcenterRequestId = " + jobcenterRequestId + ".  Exception processing trackingId " + trackingId + ".  Exception: " + t.toString(), t );
			
			try {

				if ( log.isInfoEnabled() ) {
					
					log.info( "before Send results FAIL.  jobcenterRequestId: " + jobcenterRequestId );
					
				}
				
				//////  Send result

				//////  Send result
				SendResultsToServer.send( trackingId, 
						null /* annotationData */, 
						ModuleRunProgramStatusConstants.STATUS_FAIL, serverBaseURL );
			
				if ( log.isInfoEnabled() ) {
					
					log.info( "after Send results FAIL.  jobcenterRequestId: " + jobcenterRequestId );
					
				}

				
			} catch( Exception e ) {
				
				String msg = "Failed to send failed status to server";
				
				log.error( msg, e );
			}
			
			throw t;

		} finally {
			
			TempDirCleaner.cleanDirectory( tempDirectory );
		}
		

	}


	/**
	 * @param sequence
	 * @param tempDirectory
	 * @return
	 * @throws Throwable
	 * @throws IOException
	 */
	private File createInputFile(String sequence, File tempDirectory) throws Throwable, IOException {
		
		
		// the file holding the sequence, to be processed by the program
		
		File programInputFile = new File( tempDirectory, DisopredFilenameConstants.DISOPRED_INPUT_FILENAME );

		if ( log.isDebugEnabled() ) {
			log.debug( "Creating file to run the program on, = '" + programInputFile.getAbsolutePath() + "'." );
		}

		FileWriter fw = null;
		
		try {
			// write out the FASTA file
			fw = new FileWriter( programInputFile );
			fw.write( sequence + "\n" );
			


		} catch ( Throwable e ) {

			String message = "Exception creating program input file '" + programInputFile.getAbsolutePath() + "' to run the program on.  Exception = " + e.toString();
			log.error( message, e );

			throw e;
			
		} finally {
			
			if ( fw != null ) {
				
				fw.close(); 
				fw = null;
			}
			
		}
		return programInputFile;
	}



}
