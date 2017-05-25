package org.yeastrc.paws.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.paws.base.exception.IllegalConfigurationException;
import org.yeastrc.paws.base.utils.ProcessYesNoFlag;

/**
 *
 *
 */
public class GetConfigFromFileServiceImpl {

    private static final Logger log = Logger.getLogger( GetConfigFromFileServiceImpl.class );

    private static final String CONFIG_FILE_NAME = "run_program.properties";

	public static final String CONFIG_PROPERTY_DELETE_TEMP_FILES_AFTER_SUCCESSFUL_PROCESSING = "delete.temp.files.after.successful.processing";

	public static final String CONFIG_PROPERTY_TEMP_ROOT_DIRECTORY = "temp.root.directory";

	public static final String CONFIG_PROPERTY_PROGRAM_TO_RUN_COMMAND = "run.program.command";

	
	public static final String CONFIG_PROPERTY_FORCE_SPECIFY_TEMP_DIR_NAME = "force.specify.temp.dir.name";



	////////////////////////////////////////////


    private static final String INITIALIZATION_FAILED_MESSAGE = "Initialization failed";

    private static Properties configProps = null;


    private static boolean initializationCompleted = false;


    //  Stored as read from configuration file


    private static boolean deleteTempFilesAfterSuccessfulProcessing;

	private static String tempRootDirectory;

	private static String runProgramCommand;
	
	private static String forceSpecifyTempDirName;



	static {   //  Static initializer run when class is loaded

    	GetConfigFromFileServiceImpl instance = new GetConfigFromFileServiceImpl();

    	instance.getProperties();

    	instance.getAndValidateProperties();
    }


    /**
     *
     */
    private void getProperties() {

		ClassLoader thisClassLoader = this.getClass().getClassLoader();

		URL propURL = thisClassLoader.getResource( CONFIG_FILE_NAME );

		if ( propURL == null ) {

			String msg = "Config file '" + CONFIG_FILE_NAME + "' not found ";

			log.error( msg );

			throw new RuntimeException( msg );
		}

		log.info( "Config file = '" + propURL.getFile() + "'." );

		InputStream props;

		try {
			props = new FileInputStream( propURL.getFile() );

			configProps = new Properties();

			configProps.load(props);

		} catch (Exception ex) {

			String msg = "Error reading Config file '" + CONFIG_FILE_NAME + "'";

			log.error( msg + ex.toString(), ex );

			throw new RuntimeException( msg, ex );

		}
    }


	/**
	 * @param configKey
	 * @return
	 * @throws Exception
	 */
	private static String getConfigValue(String configKey) throws Exception {

		String configValue = null;

		try {

			configValue = configProps.getProperty( configKey );

		} catch (Exception ex) {
			log.error("getConfigValue(), exception = " + ex.toString(), ex );

			throw ex;
		}


		if ( configValue == null ) {

			String msg = "Config_system file entry '" + configKey + "' is missing.";

			throw new IllegalConfigurationException( msg  );
		}

		return configValue;
	}




	/**
	 * Validate that the combination of property settings are valid
	 */
	private void getAndValidateProperties() {

		String deleteTempFilesAfterSuccessfulProcessingString = ""; //

		try {
			deleteTempFilesAfterSuccessfulProcessingString = getConfigValue( CONFIG_PROPERTY_DELETE_TEMP_FILES_AFTER_SUCCESSFUL_PROCESSING );
		} catch (Exception e) {

			String message = "The configuration file value for '" + CONFIG_PROPERTY_DELETE_TEMP_FILES_AFTER_SUCCESSFUL_PROCESSING
							+ "' was unable to be retrieved.";

			log.error( message );

			throw new RuntimeException( message, e );
		}

		try {
			tempRootDirectory = getConfigValue( CONFIG_PROPERTY_TEMP_ROOT_DIRECTORY );
		} catch (Exception e) {

			String message = "The configuration file value for '" + CONFIG_PROPERTY_TEMP_ROOT_DIRECTORY
							+ "' was unable to be retrieved.";

			log.error( message );

			throw new RuntimeException( message, e );
		}

		if ( StringUtils.isEmpty( tempRootDirectory ) ) {

			String message = "The configuration file value for '" + CONFIG_PROPERTY_TEMP_ROOT_DIRECTORY
							+ "' cannot be empty or non-existent.";

			log.error( message );

			throw new IllegalConfigurationException( message );
		}


		try {
			runProgramCommand = getConfigValue( CONFIG_PROPERTY_PROGRAM_TO_RUN_COMMAND );
		} catch (Exception e) {

			String message = "The configuration file value for '" + CONFIG_PROPERTY_PROGRAM_TO_RUN_COMMAND
							+ "' was unable to be retrieved.";

			log.error( message );

			throw new RuntimeException( message, e );
		}

		if ( StringUtils.isEmpty( runProgramCommand ) ) {

			String message = "The configuration file value for '" + CONFIG_PROPERTY_PROGRAM_TO_RUN_COMMAND
							+ "' cannot be empty or non-existent.";

			log.error( message );

			throw new IllegalConfigurationException( message );
		}
		
		try {
			//  Coded this way since can be empty or non-existent
			forceSpecifyTempDirName = configProps.getProperty( CONFIG_PROPERTY_FORCE_SPECIFY_TEMP_DIR_NAME );
		} catch (Exception e) {

			String message = "The configuration file value for '" + CONFIG_PROPERTY_FORCE_SPECIFY_TEMP_DIR_NAME
							+ "' was unable to be retrieved.";

			log.error( message );

			throw new RuntimeException( message, e );
		}
		if ( StringUtils.isEmpty( forceSpecifyTempDirName ) ) {
			//  empty or null so set to null
			forceSpecifyTempDirName = null;
		}


		/////////////////////////////////

		//  Start validation


		//  process "yes" "no" fields




		try {

			deleteTempFilesAfterSuccessfulProcessing = ProcessYesNoFlag.processYesNoFlag( deleteTempFilesAfterSuccessfulProcessingString );

		} catch ( Throwable t ) {

			String message = "The configuration file value for '" + CONFIG_PROPERTY_DELETE_TEMP_FILES_AFTER_SUCCESSFUL_PROCESSING
							+ "' is not valid, empty, or non-existent.";

			log.error( message );

			throw new IllegalConfigurationException( message, t );
		}



		log.info( "!!!!!!!!!   Configuration values  !!!!!!!!!!!!" );


		log.info( "deleteTempFilesAfterSuccessfulProcessing  = |" + deleteTempFilesAfterSuccessfulProcessing + "|." );

		log.info( "tempRootDirectory = |" + tempRootDirectory + "|." );
		log.info( "runProgramCommand = |" + runProgramCommand + "|." );


		initializationCompleted = true;

	}




	public static String getTempRootDirectory() {

		if ( ! initializationCompleted ) {

			throw new IllegalStateException( INITIALIZATION_FAILED_MESSAGE );
		}

		return tempRootDirectory;
	}

	public static String getRunProgramCommand() {

		if ( ! initializationCompleted ) {

			throw new IllegalStateException( INITIALIZATION_FAILED_MESSAGE );
		}

		return runProgramCommand;
	}


	public static boolean getDeleteTempFilesAfterSuccessfulProcessing() {
		if ( ! initializationCompleted ) {
			throw new IllegalStateException( INITIALIZATION_FAILED_MESSAGE );
		}
		return deleteTempFilesAfterSuccessfulProcessing;
	}



    public static String getForceSpecifyTempDirName() {
		if ( ! initializationCompleted ) {
			throw new IllegalStateException( INITIALIZATION_FAILED_MESSAGE );
		}
		return forceSpecifyTempDirName;
	}

	
}
