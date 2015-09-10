package org.yeastrc.paws.program_runner;

import java.io.File;

import org.apache.log4j.Logger;
import org.yeastrc.paws.config.GetConfigFromFileServiceImpl;
import org.yeastrc.paws.dto.RunSystemCommandResponse;
import org.yeastrc.paws.utils.CreateShellScriptToRun;
import org.yeastrc.paws.utils.RunSystemCommand;

/**
 * Run the Program
 * 
 *
 */
public class ProgramRunner {

	private static Logger log = Logger.getLogger(ProgramRunner.class);


	/**
	 * Begin execution of the Program
	 * 
	 * @param programInputFile
	 * @param tempDirectory
	 * @return 
	 * @throws Exception if there is a problem
	 */
	public static RunSystemCommandResponse runProgram( File programInputFile, File tempDirectory, int numberOfThreadsForRunningJob ) throws Throwable {

		RunSystemCommandResponse runProgramSystemCommandResponse = null; //  Response from running the program on the command line

		String runProgramCommand =  
				GetConfigFromFileServiceImpl.getRunProgramCommand()
				+ " "
				+ programInputFile.getName()
				+ " "
				+ numberOfThreadsForRunningJob; // numberOfThreadsForRunningJob as second parameter to the shell script
		
		String shellScriptToRunFileWithPath = CreateShellScriptToRun.createShellScriptToRun( runProgramCommand, tempDirectory );
		
		try {

			
			log.info( "About to run the Program.  Command to Run = " + runProgramCommand
					+ ", using shell script " + shellScriptToRunFileWithPath );

			// execute Program

			runProgramSystemCommandResponse = 
					RunSystemCommand.runCmd( shellScriptToRunFileWithPath, true /* throwExceptionOnCommandFailure */ );


		} catch ( Throwable e ) {

			String message = "Exception running command: " + runProgramCommand
					+ "/n on file '" + programInputFile.getAbsolutePath() + "'.  Exception = " + e.toString();
			log.error( message, e );

			throw e;
		}

		return runProgramSystemCommandResponse;
	}


}
