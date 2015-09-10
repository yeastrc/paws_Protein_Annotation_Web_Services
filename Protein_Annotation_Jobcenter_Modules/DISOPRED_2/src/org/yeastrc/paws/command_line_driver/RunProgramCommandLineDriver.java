package org.yeastrc.paws.command_line_driver;

import java.io.File;

import org.yeastrc.paws.main.RunProgramMain;


/**
 * This class is for launching the program from the command line.
 *
 */
public class RunProgramCommandLineDriver {

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {

		
		throw new Exception( "Not currently supported." );
		
//		if ( args.length < 2 ) {
//			
//			System.out.println( "Arguments are:  <input request id> <input and output base directory> ");
//			
//			System.exit( 1 );
//		}
//
//		String requestIdString = args[ 0 ];
//		String inputOutputBaseDirString = args[ 1 ];
//		
//		int requestId = 0;
//		
//		try {
//			
//			requestId = Integer.parseInt( requestIdString );
//		} catch ( Exception ex ) {
//			
//			System.out.println( "Unable to parse request id |" + requestIdString + "|" );
//			System.exit( 1 );
//		}
//		
//		File inputOutputBaseDir = new File( inputOutputBaseDirString );
//		
//		if ( ! inputOutputBaseDir.exists() ) {
//			
//			System.out.println( "Input Output base dir does not exist: " + inputOutputBaseDir.getAbsolutePath() );
//			System.exit( 1 );
//		}
//
//		System.out.println("Running with request id = " + requestId + ", Input Output base dir = " + inputOutputBaseDir.getAbsolutePath() );
//		
//		RunPhiliusMain runPhiliusMain = RunPhiliusMain.getInstance();
//		
//		runPhiliusMain.init();
//		
//		runPhiliusMain.processInputFile( requestId, inputOutputBaseDir, inputOutputBaseDir, -1 /* fake jobcenterRequestId */ );
//		

	}

}
