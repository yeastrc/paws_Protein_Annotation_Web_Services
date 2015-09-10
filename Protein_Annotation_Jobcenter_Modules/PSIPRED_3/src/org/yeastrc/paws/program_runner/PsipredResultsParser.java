package org.yeastrc.paws.program_runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.yeastrc.paws.constants.CharsetForReadingProgramResultFiles;
import org.yeastrc.paws.constants.PsipredFilenameConstants;
import org.yeastrc.paws.dto.PsipredParsedDisorderedResiduesPredictionEntry;
import org.yeastrc.paws.dto.PsipredParsedResults;



public class PsipredResultsParser {

	private static Logger log = Logger.getLogger(PsipredResultsParser.class);



	
	public static final String SCORE_NA_STRING = "NA";


	/**
	 * @param tempDirectory
	 * @return
	 * @throws Exception
	 */
	public static PsipredParsedResults parseResults( File tempDirectory ) throws Exception {
		
		
		PsipredParsedResults psipredParsedResults = new PsipredParsedResults();
		

		List<PsipredParsedDisorderedResiduesPredictionEntry> psipredParsedDisorderedResiduesPredictionEntryList = new ArrayList<PsipredParsedDisorderedResiduesPredictionEntry>();
		psipredParsedResults.setEntries(psipredParsedDisorderedResiduesPredictionEntryList);


		List<String> fileHeaderLines = new ArrayList<String>();
		psipredParsedResults.setFileHeaderLines( fileHeaderLines );
		
		String filenameToRead = PsipredFilenameConstants.PSIPRED_OUTPUT_FILENAME;
		
		File psipredOutputFile = new File( tempDirectory, filenameToRead );

		psipredParsedResults.setFilename( filenameToRead );

		

		if ( !psipredOutputFile.exists() )
			throw new Exception( "Could not find results file at: " + psipredOutputFile.getAbsolutePath() );

		log.info( "NOW: Parsing output file '" + psipredOutputFile.getAbsolutePath() + "'." );



		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;

		try {

			fileInputStream = new FileInputStream( psipredOutputFile );
			
			inputStreamReader = new InputStreamReader( fileInputStream, CharsetForReadingProgramResultFiles.CHARSET_FOR_READING_PROGRAM_RESULT_FILES );

			br = new BufferedReader( inputStreamReader );
			

			String line = null;




			while ( ( line =  br.readLine() ) != null ) {

				String dataLine = line;

				if ( dataLine.length() == 0 ) {

					continue;
				}

				if ( dataLine.startsWith( "#"  ) ) {
					
					//  Save header lines as comment lines

					fileHeaderLines.add( dataLine );
					
					continue;
				}


				
				//  dataLine:  "    1 M C   0.998  0.001  0.002",  after split, index zero === "", index 1 === "1", index 2 === "M", ...

				//          The dataLine may also start with the position like "3096 M C   0.998  0.001  0.002", 
				//              where index zero === "3096", index 1 === "M", ...
				
				
				String[] dataLineSplit = dataLine.split( "\\s+" );

				String firstPositionInDataLine = dataLineSplit[ 0 ];

				
				int dataPositionOffset = 0;
				
				if ( "".equals( firstPositionInDataLine ) ) {
					
					dataPositionOffset = 1;
				}
				

				String positionString = dataLineSplit[ 0 + dataPositionOffset ];
				String aminoAcid = dataLineSplit[ 1 + dataPositionOffset ];
				String type = dataLineSplit[ 2 + dataPositionOffset ];
				String score1String = dataLineSplit[ 3 + dataPositionOffset ];
				String score2String = dataLineSplit[ 4 + dataPositionOffset ];
				String score3String = dataLineSplit[ 5 + dataPositionOffset ];



				int position = 0;

				try {
					position = Integer.parseInt( positionString );

				} catch ( Exception e ) {

					String msg = "Position parse failed for position string: |" + positionString + "|,\n dataLine: " + dataLine
							+ "\n output file '" + psipredOutputFile.getAbsolutePath() + "'.";

					log.error( msg );
					throw new Exception(msg, e);
				}
				

				BigDecimal score1 = null;
				boolean score1NA = false;
				
				if ( SCORE_NA_STRING.equals( score1String ) ) {
					
					score1NA = true;
					
				} else {

					try {
						score1 = new BigDecimal( score1String );

					} catch ( Exception e ) {

						String msg = "Probability parse failed for probability string: |" + score1String + "|,\n dataLine: " + dataLine
								+ "\n output file '" + psipredOutputFile.getAbsolutePath() + "'.";

						log.error( msg );
						throw new Exception(msg, e);
					}
				}

				BigDecimal score2 = null;
				boolean score2NA = false;
				
				if ( SCORE_NA_STRING.equals( score2String ) ) {
					
					score2NA = true;
					
				} else {

					try {
						score2 = new BigDecimal( score2String );

					} catch ( Exception e ) {

						String msg = "Probability parse failed for probability string: |" + score2String + "|,\n dataLine: " + dataLine
								+ "\n output file '" + psipredOutputFile.getAbsolutePath() + "'.";

						log.error( msg );
						throw new Exception(msg, e);
					}
				}

				BigDecimal score3 = null;
				boolean score3NA = false;
				
				if ( SCORE_NA_STRING.equals( score3String ) ) {
					
					score3NA = true;
					
				} else {

					try {
						score3 = new BigDecimal( score3String );

					} catch ( Exception e ) {

						String msg = "Probability parse failed for probability string: |" + score3String + "|,\n dataLine: " + dataLine
								+ "\n output file '" + psipredOutputFile.getAbsolutePath() + "'.";

						log.error( msg );
						throw new Exception(msg, e);
					}
				}
				

				PsipredParsedDisorderedResiduesPredictionEntry entry = new PsipredParsedDisorderedResiduesPredictionEntry();

				entry.setType( type );
				entry.setPosition( position );
				entry.setAminoAcid( aminoAcid );
				
				entry.setScore1( score1 );
				entry.setScore1NA( score1NA );
				entry.setScore2( score2 );
				entry.setScore2NA( score2NA );
				entry.setScore3( score3 );
				entry.setScore3NA( score3NA );
				
				psipredParsedDisorderedResiduesPredictionEntryList.add( entry );

			}	//end reading lines in the file

		} catch (Exception e) {


			throw e;

		} finally {

			if ( br != null ) {

				try {
					br.close();
				} catch ( Exception ex ) {

				}
			}
			br = null;
			

			if ( inputStreamReader != null ) {

				try {
					inputStreamReader.close();
				} catch ( Exception ex ) {

				}
			}
			inputStreamReader = null;

			if ( fileInputStream != null ) {

				try {
					fileInputStream.close();
				} catch ( Exception ex ) {

				}
			}
		}



		return psipredParsedResults;
	}


	

}
