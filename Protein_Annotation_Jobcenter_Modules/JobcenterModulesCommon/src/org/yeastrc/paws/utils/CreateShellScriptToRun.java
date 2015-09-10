package org.yeastrc.paws.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.log4j.Logger;
import org.yeastrc.paws.constants.ShellScriptToRunConstants;

/**
 * 
 *
 */
public class CreateShellScriptToRun {

	private static Logger log = Logger.getLogger(CreateShellScriptToRun.class);


	/**
	 * @param commandToRun
	 * @param commandRunDirectory
	 * @return full path the shell script to run including name
	 * @throws Exception
	 */
	public static String createShellScriptToRun( 
			String commandToRun,
			File commandRunDirectory
			 ) throws Exception {
		
		String directoryRunCommandInString = commandRunDirectory.getAbsolutePath();
		
		
		String shellScriptFilename = ShellScriptToRunConstants.SHELL_SCRIPT_TO_RUN_FILENAME;
		
		File shellScriptToRunFile = new File( commandRunDirectory, shellScriptFilename );
		
		String shellScriptToRunFileWithPath = shellScriptToRunFile.getAbsolutePath(); //  Returned from this method
		
		
		
		
		StringBuilder commandRunInRunScriptSB = new StringBuilder();

		
		commandRunInRunScriptSB.append( commandToRun );

		commandRunInRunScriptSB.append( " > " );
		commandRunInRunScriptSB.append( ShellScriptToRunConstants.SYSOUT_FILENAME );

		commandRunInRunScriptSB.append( " 2> " );
		commandRunInRunScriptSB.append( ShellScriptToRunConstants.SYSERR_FILENAME );

		String finalCommandRunInRunScriptString = commandRunInRunScriptSB.toString();
		
		BufferedWriter writer = null;
		
		try {
			

			writer = new BufferedWriter( new FileWriter( shellScriptToRunFile ) );
			
			//  Build the full shell script
			
			writer.write( "#!/bin/bash" );
			writer.newLine();
			writer.newLine();
			
			writer.write( "cd " );
			writer.write( directoryRunCommandInString );
			writer.newLine();
			
			writer.write( "RETVAL=$?" );
			writer.newLine();
			
			writer.write( "if [ $RETVAL -ne 0 ]; then" );
			writer.newLine();
			
			writer.write( "	echo failed cd " );
			writer.write( directoryRunCommandInString );
			writer.newLine();
			
			writer.write( "   exit 1" );
			writer.newLine();
			
			writer.write( "fi" );
			writer.newLine();
			writer.newLine();
			
			writer.write( finalCommandRunInRunScriptString );

			writer.newLine();

			writer.newLine();
			
			writer.write( "RETVAL=$?" );
			writer.newLine();
			
			writer.write( "if [ $RETVAL -ne 0 ]; then" );
			writer.newLine();
			
			writer.write( "	echo failed run of command RETVAL: $RETVAL" );
			writer.newLine();
			
			writer.write( "   exit $RETVAL" );
			writer.newLine();
			
			writer.write( "fi" );
			writer.newLine();
			
			writer.write( "" );
			writer.newLine();
			
			
			
			
		} finally {
			
			if ( writer != null ) {
				
				writer.close();
			}
		}
		
		
		if ( ! shellScriptToRunFile.setExecutable( true ) ) {
			
			String msg = "Failed to change generated shell script to executable";
			
			log.error( msg );
			
			throw new Exception( msg );
		}
		
		return shellScriptToRunFileWithPath;
		
	}
}
