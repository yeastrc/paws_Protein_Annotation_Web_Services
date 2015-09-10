package org.yeastrc.paws.utils;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SystemPropertiesAccessor {
	
	private static Logger log = Logger.getLogger(SystemPropertiesAccessor.class); 


	private static boolean isDevEnv = false;
	
	private static boolean isTestEnv = false;
	
	private static boolean isLoadAll = false;
	
	private static boolean isMassUpload = false;
	
	private static boolean isStartLineSet = false;
	
	private static int startLine = -1;
	
	private static boolean isR3dFilesPerOutputSet = false;

	private static int r3dFilesPerOutput = -1;
	
	static {
		
		Properties prop = System.getProperties();
		
		
		String devEnv = prop.getProperty("devEnv"); 
		
		if ( "Y".equals(devEnv ) )
		{
			isDevEnv = true;
		}		
		
		String testEnv = prop.getProperty("testEnv"); 
		
		if ( "Y".equals(testEnv ) )
		{
			isTestEnv = true;
		}		

		String loadAll= prop.getProperty("loadAll");

		if ( "Y".equals( loadAll ) ) {
			isLoadAll = true;
		}
		
		String massUpload= prop.getProperty("massUpload");

		if ( "Y".equals( massUpload ) ) {
			isMassUpload = true;
		}
		
		String startLineString= prop.getProperty("startLine");
		
		if ( ! StringUtils.isEmpty( startLineString ) ) {

			try {
				
				startLine = Integer.parseInt( startLineString );
				
				isStartLineSet = true;
				
			} catch ( Exception ex ) {
				
				String msg = "System property '-DstartLine' was set to non-numeric value: |" + startLineString + "|";
				
				log.error( msg, ex );
				
				System.out.println( msg );
			}
			
		}
		
		
		String r3dFilesPerOutputString= prop.getProperty("r3dFilesPerOutput");
		
		if ( ! StringUtils.isEmpty( r3dFilesPerOutputString ) ) {

			try {
				
				r3dFilesPerOutput = Integer.parseInt( r3dFilesPerOutputString );
				
				isR3dFilesPerOutputSet = true;
				
			} catch ( Exception ex ) {
				
				String msg = "System property '-Dr3dFilesPerOutput' was set to non-numeric value: |" + r3dFilesPerOutputString + "|";
				
				log.error( msg, ex );
				
				System.out.println( msg );
			}
			
		}
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	/**
	 * @return true if -DdevEnv=Y
	 */
	public static boolean isDevEnvironment()
	{
		return isDevEnv;
	}
	
	/**
	 * @return true if -DtestEnv=Y
	 */
	public static boolean isTestEnvironment()
	{
		return isTestEnv;
	}
	
	/**
	 * @return true if -DloadAll=Y
	 */
	public static boolean isLoadAll()
	{
		return isLoadAll;
	}
	
	/**
	 * @return true if -DmassUpload=Y
	 */
	public static boolean isMassUpload()
	{
		return isMassUpload;
	}

	/**
	 * @return true if -DstartLine=### ( a line to start on )
	 */
	public static boolean isStartLineSet() {
		return isStartLineSet;
	}

	/**
	 * @return the start number (###) if -DstartLine=### ( a line to start on ), -1 otherwise
	 */
	public static int getStartLine() {
		return startLine;
	}	
	
	
	/**
	 * @return true if -Dr3dFilesPerOutput=### ( the number of R3D files to put in an output file )
	 */
	public static boolean isR3dFilesPerOutputSet() {
		return isR3dFilesPerOutputSet;
	}

	/**
	 * @return the number of R3D files to put in an output file (###) if -Dr3dFilesPerOutput=### , -1 otherwise
	 */
	public static int getR3dFilesPerOutput() {
		return r3dFilesPerOutput;
	}
}
