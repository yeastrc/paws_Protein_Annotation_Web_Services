package org.yeastrc.paws.program_runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.yeastrc.paws.constants.CharsetForReadingProgramResultFiles;
import org.yeastrc.paws.constants.DisopredFilenameConstants;
import org.yeastrc.paws.constants.DisorderedOrderedTypeConstants;
import org.yeastrc.paws.dto.DisopredParsedDisorderedResiduesPredictionEntry;
import org.yeastrc.paws.dto.DisopredParsedResults;



public class DisopredResultsParser {

	private static Logger log = Logger.getLogger(DisopredResultsParser.class);


	public static final int DISOPRED_HEADER_LINE_COUNT = 5;



	public static final String DISORDERED_REGIONS_TYPE_ORDERED = ".";
	public static final String DISORDERED_REGIONS_TYPE_DISORDERED = "*";

	
	public static final String SCORE_NA_STRING = "NA";


	/**
	 * @param tempDirectory
	 * @return
	 * @throws Exception
	 */
	public static DisopredParsedResults parseResults( File tempDirectory ) throws Exception {
		
		
		DisopredParsedResults disopredParsedResults = new DisopredParsedResults();
		

		List<DisopredParsedDisorderedResiduesPredictionEntry> disopredParsedDisorderedResiduesPredictionEntryList = new ArrayList<DisopredParsedDisorderedResiduesPredictionEntry>();
		disopredParsedResults.setEntries(disopredParsedDisorderedResiduesPredictionEntryList);

		File disopredOutputFile = new File( tempDirectory, DisopredFilenameConstants.DISOPRED_OUTPUT_DISORDERED_FILENAME );


		List<String> fileHeaderLines = new ArrayList<String>();
		disopredParsedResults.setFileHeaderLines( fileHeaderLines );
		
		String filenameToRead = DisopredFilenameConstants.DISOPRED_OUTPUT_DISORDERED_FILENAME;
		
		disopredParsedResults.setFilename( filenameToRead );


		if ( !disopredOutputFile.exists() )
			throw new Exception( "Could not find results file at: " + disopredOutputFile.getAbsolutePath() );

		log.info( "NOW: Parsing output file '" + disopredOutputFile.getAbsolutePath() + "'." );


		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;

		try {

			fileInputStream = new FileInputStream( disopredOutputFile );
			
			inputStreamReader = new InputStreamReader( fileInputStream, CharsetForReadingProgramResultFiles.CHARSET_FOR_READING_PROGRAM_RESULT_FILES );

			br = new BufferedReader( inputStreamReader );
			

			String line = null;

			////////

			///  First skip over header section

			for ( int counter = 0; counter < DISOPRED_HEADER_LINE_COUNT ; counter++ ) {

				if( ( line =  br.readLine() ) == null ) {

					String msg = "ERROR: Skipping over " + DISOPRED_HEADER_LINE_COUNT 
							+ " header lines and encountered end of file processing disopredOutputFile: " 
							+ disopredOutputFile.getAbsolutePath();

					log.error( msg );
					throw new Exception(msg);
				}
				

				//  Save header lines as comment lines

				fileHeaderLines.add( line );
			}




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


				//  dataLine:  "    1 M * 0.96",  after split, index zero === "", index 1 === "1", index 2 === "M", ...

				//          The dataLine may also start with the position like "3096 M * 0.96", 
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



				int position = 0;

				try {
					position = Integer.parseInt( positionString );

				} catch ( Exception e ) {

					String msg = "Position parse failed for position string: |" + positionString + "|,\n dataLine: " + dataLine
							+ "\n output file '" + disopredOutputFile.getAbsolutePath() + "'.";

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
								+ "\n output file '" + disopredOutputFile.getAbsolutePath() + "'.";

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
								+ "\n output file '" + disopredOutputFile.getAbsolutePath() + "'.";

						log.error( msg );
						throw new Exception(msg, e);
					}
				}
				
				
				
				String outputType = null;

				if ( DISORDERED_REGIONS_TYPE_DISORDERED.equals( type ) ) {

					outputType = DisorderedOrderedTypeConstants.DISORDERED_TYPE;
					
				} else if ( DISORDERED_REGIONS_TYPE_ORDERED.equals( type ) ) {

					outputType = DisorderedOrderedTypeConstants.ORDERED_TYPE;

				} else {

					String msg = "Unknown 'type' in record, type = |" + type + "|, dataLine: " + dataLine;

					log.error( msg );

					throw new Exception(msg);
				}

				
				DisopredParsedDisorderedResiduesPredictionEntry entry = new DisopredParsedDisorderedResiduesPredictionEntry();

				entry.setTypeRaw( type );
				entry.setType( outputType );
				entry.setPosition( position );
				entry.setAminoAcid( aminoAcid );
				
				entry.setScore1( score1 );
				entry.setScore1NA( score1NA );
				entry.setScore2( score2 );
				entry.setScore2NA( score2NA );
				
				disopredParsedDisorderedResiduesPredictionEntryList.add( entry );

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



		return disopredParsedResults;
	}


	

}
