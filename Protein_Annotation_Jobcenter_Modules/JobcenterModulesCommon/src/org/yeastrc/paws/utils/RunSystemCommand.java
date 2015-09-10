package org.yeastrc.paws.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.yeastrc.paws.dto.RunSystemCommandResponse;

public class RunSystemCommand {


	private static Logger log = Logger.getLogger(RunSystemCommand.class);

//	RunSystemCommand.runCmd(commandToRun, false /* throwExceptionOnCommandFailure */ );


	/**
	 * @param cmd
	 * @param throwExceptionOnCommandFailure
	 * @return true if successful, false otherwise
	 * @throws Exception
	 */
	public static RunSystemCommandResponse runCmd( String cmd, boolean throwExceptionOnCommandFailure )
	throws Throwable
	{
		log.info( "runCmd: running shell command:  " + cmd );
		
		RunSystemCommandResponse runSystemCommandResponse = new RunSystemCommandResponse();

		runSystemCommandResponse.setCommandSuccessful( false );


		BufferedReader readerStdOut = null;
		BufferedReader readerStdErr = null;

		OutputStream outStrToProcess = null;

		try {


			Runtime run = Runtime.getRuntime() ;

			Process process = run.exec(cmd) ;


			readerStdOut = new BufferedReader( new InputStreamReader( process.getInputStream() ) ) ;
			readerStdErr = new BufferedReader( new InputStreamReader( process.getErrorStream() ) ) ;

			outStrToProcess = process.getOutputStream();

			process.waitFor() ;
			
			int exitValue = process.exitValue();

			runSystemCommandResponse.setCommandExitCode( exitValue );
			

			StringBuilder stdOutSB = new StringBuilder();

			String stdOutLine ;
			while ( (  stdOutLine = readerStdOut.readLine() ) != null )
			{
				stdOutSB.append( stdOutLine );
				stdOutSB.append("\n");
			}

			String stdOut = stdOutSB.toString();
			
			runSystemCommandResponse.setStdOut( stdOut );


			StringBuilder stdErrSB = new StringBuilder();

			String stdErrLine ;
			while ( (  stdErrLine = readerStdErr.readLine() ) != null )
			{
				stdErrSB.append( stdErrLine );
				stdErrSB.append("\n");
			}

			String stdErr = stdErrSB.toString();
			
			runSystemCommandResponse.setStdErr( stdErr );
			
			

			if ( exitValue != 0 ) {

				String message = "runCmd: execute command returned non-zero.  exitValue = " + exitValue + ", command = " + cmd
					+ "\nStdOut = \n" + stdOut
					+ "\nStdErr = \n" + stdErr;

				log.error(message);

//				System.out.println( "Error in Run Philius Module.  Error in running system command: " + message );

				if ( throwExceptionOnCommandFailure ) {

					throw new Exception( message );

				}

				runSystemCommandResponse.setCommandSuccessful( false );

			} else {

				runSystemCommandResponse.setCommandSuccessful( true );
			}

		} catch ( Throwable t ) {

			log.error( "Exception running command |" + cmd + "|.  Exception: " + t.toString(), t );

			throw t;

		} finally {

			if ( readerStdOut != null ) {

				try {
					readerStdOut.close();
				} catch ( Throwable t ) {
					log.info( "Exception closing stdout from process: " + t.toString(), t );
				}
				readerStdOut = null;
			}

			if ( readerStdErr != null ) {
				try {
					readerStdErr.close();
				} catch ( Throwable t ) {
					log.info( "Exception closing stderr from process: " + t.toString(), t );
				}
				readerStdErr = null;
			}


			if ( outStrToProcess != null ) {
				try {
					outStrToProcess.close();
				} catch ( Throwable t ) {
					log.info( "Exception closing stdin to process: " + t.toString(), t );
				}
				outStrToProcess = null;
			}

		}

		return runSystemCommandResponse;

	}

}
