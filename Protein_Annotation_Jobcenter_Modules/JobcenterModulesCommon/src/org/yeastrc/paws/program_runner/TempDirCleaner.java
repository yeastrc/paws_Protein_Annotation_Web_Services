package org.yeastrc.paws.program_runner;

import java.io.File;

import org.apache.log4j.Logger;
import org.yeastrc.paws.config.GetConfigFromFileServiceImpl;

public class TempDirCleaner {

	private static Logger log = Logger.getLogger(TempDirCleaner.class);

	/**
	 * Remove the tempDir used to run the program
	 * @param tempDir
	 * @return 
	 * @throws Exception if there is a problem
	 */
	public static void cleanDirectory( File tempDir )  throws Throwable {

		final String method = "cleanDirectory( ... )";
		
		if ( ! GetConfigFromFileServiceImpl.getDeleteTempFilesAfterSuccessfulProcessing() ) {
			
			log.info( "Not cleaning up directory since config property '"
					+ GetConfigFromFileServiceImpl.CONFIG_PROPERTY_DELETE_TEMP_FILES_AFTER_SUCCESSFUL_PROCESSING
					+ "' is set to 'no'"
					);
			
			return;
		}
		
		

		try {

			if ( !tempDir.exists() ) {

				log.info( method + ": exiting since tempDir does not exist.  tempDir = " + tempDir );
				return;
			}
			if ( !tempDir.isDirectory() ) {


				log.info( method + ":  exiting since tempDir is not a directory. tempDir = " + tempDir );
				return;
			}

			log.info( method + ": tempDir = " + tempDir );


			for( File file : tempDir.listFiles() ) {
				try {
					if ( ! file.delete() ) {

						log.error( "Cleanup of tempDir,  deletion of file failed, file: " + file.getAbsolutePath() );
					}

				} catch( Exception e2 ) {

					log.error( "Cleanup of tempDir, deletion of file failed, file: " + file.getAbsolutePath(), e2 );
				}
			}

			if ( ! tempDir.delete() ) {

				log.error( "Cleanup of tempDir, deletion of tempDir failed, tempDir: " + tempDir.getAbsolutePath() );
			}

		} catch( Exception e ) {

			log.error( "Cleanup of tempDir, deletion of file failed, tempDir: " + tempDir.getAbsolutePath(), e );
		}

	}

}
