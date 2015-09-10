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
import org.yeastrc.paws.dto.DisopredParsedDisorderedResiduesPredictions;
import org.yeastrc.paws.dto.DisopredParsedProteinBindingDisorderedResiduesPredictionEntry;
import org.yeastrc.paws.dto.DisopredParsedProteinBindingDisorderedResiduesPredictions;
import org.yeastrc.paws.dto.DisopredParsedResults;



/**
 * 
 *
 */
public class DisopredResultsParser {

	private static Logger log = Logger.getLogger(DisopredResultsParser.class);

	
	///  values for the .diso file
	
	//  From file header:
//	# Disordered residues are marked with asterisks (*)
//	#    Ordered residues are marked with dots (.)

	public static final String DISO_FILE_DISORDERED_REGIONS_TYPE_ORDERED = ".";
	public static final String DISO_FILE_DISORDERED_REGIONS_TYPE_DISORDERED = "*";

	///  values for the .pbdat file
	
	//  From file header:
//	#                  ----- DISOPRED version 3.1 -----
//	#     Protein binding site prediction within disordered regions
//	#   Protein-binding disordered residues are marked with carets (^)
//	# Disordered residues not binding proteins are marked with dashes (-)
//	#            Ordered amino acids are marked with dots (.)
	
	public static final String PBDAT_FILE_DISORDERED_REGIONS_TYPE_ORDERED = ".";
	public static final String PBDAT_FILE_DISORDERED_BINDING_REGIONS_TYPE_DISORDERED = "^";
	public static final String PBDAT_FILE_DISORDERED_NON_BINDING_REGIONS_TYPE_DISORDERED = "-";

	
	public static final String PROBABILITY_NA_STRING = "NA";


	/**
	 * @param tempDirectory
	 * @return
	 * @throws Exception
	 */
	public static DisopredParsedResults parseResults( File tempDirectory ) throws Exception {
		
		
		DisopredParsedResults disopredParsedResults = new DisopredParsedResults();
		
		DisopredParsedDisorderedResiduesPredictions disopredParsedDisorderedResiduesPredictions =
				parseDisorderedResiduePedictions( tempDirectory );
		
		disopredParsedResults.setDisopredParsedDisorderedResiduesPredictions( disopredParsedDisorderedResiduesPredictions );
		
		
		DisopredParsedProteinBindingDisorderedResiduesPredictions disopredParsedProteinBindingDisorderedResiduesPredictions =
				parseProteinBindingDisorderedResiduePedictions( tempDirectory );
		
		disopredParsedResults.setDisopredParsedProteinBindingDisorderedResiduesPredictions( disopredParsedProteinBindingDisorderedResiduesPredictions );
		
		
		return disopredParsedResults;
	}


	/**
	 * @param tempDirectory
	 * @return
	 * @throws Exception
	 */
	private static DisopredParsedDisorderedResiduesPredictions parseDisorderedResiduePedictions( File tempDirectory ) throws Exception {
		
		
		DisopredParsedDisorderedResiduesPredictions disopredParsedDisorderedResiduesPredictions = new DisopredParsedDisorderedResiduesPredictions();
		
		List<DisopredParsedDisorderedResiduesPredictionEntry> disopredParsedDisorderedResiduesPredictionEntryList = new ArrayList<DisopredParsedDisorderedResiduesPredictionEntry>();
		disopredParsedDisorderedResiduesPredictions.setEntries(disopredParsedDisorderedResiduesPredictionEntryList);

		List<String> fileHeaderLines = new ArrayList<String>();
		disopredParsedDisorderedResiduesPredictions.setFileHeaderLines( fileHeaderLines );
		
		String filenameToRead = DisopredFilenameConstants.DISOPRED_OUTPUT_DISORDERED_FILENAME;
		
		File disopredOutputFile = new File( tempDirectory, filenameToRead );

		disopredParsedDisorderedResiduesPredictions.setFilename( filenameToRead );

		if ( !disopredOutputFile.exists() )
			throw new Exception( "Could not find results file at: " + disopredOutputFile.getAbsolutePath() );

		log.info( "Parsing output file '" + disopredOutputFile.getAbsolutePath() + "'." );


		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;

		try {

			fileInputStream = new FileInputStream( disopredOutputFile );
			
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
				String probabilityString = dataLineSplit[ 3 + dataPositionOffset ];

				
				
				int position = 0;

				try {
					position = Integer.parseInt( positionString );

				} catch ( Exception e ) {

					String msg = "Position parse failed for position string: |" + positionString + "|,\n dataLine: " + dataLine
							+ "\n output file '" + disopredOutputFile.getAbsolutePath() + "'.";

					log.error( msg );
					throw new Exception(msg, e);
				}
				

				BigDecimal probability = null;
				boolean probabilityNA = false;
				
				if ( PROBABILITY_NA_STRING.equals( probabilityString ) ) {
					
					probabilityNA = true;
					
				} else {

					try {
						probability = new BigDecimal( probabilityString );

					} catch ( Exception e ) {

						String msg = "Probability parse failed for probability string: |" + probabilityString + "|,\n dataLine: " + dataLine
								+ "\n output file '" + disopredOutputFile.getAbsolutePath() + "'.";

						log.error( msg );
						throw new Exception(msg, e);
					}
				}
				
				
				
				String outputType = null;

				if ( DISO_FILE_DISORDERED_REGIONS_TYPE_DISORDERED.equals( type ) ) {

					outputType = DisorderedOrderedTypeConstants.DISORDERED_TYPE;
				
				} else if ( DISO_FILE_DISORDERED_REGIONS_TYPE_ORDERED.equals( type ) ) {

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
				entry.setProbability( probability );
				entry.setProbabilityNA( probabilityNA );
				
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



		return disopredParsedDisorderedResiduesPredictions;
	}


	

	/**
	 * @param tempDirectory
	 * @return
	 * @throws Exception
	 */
	private static DisopredParsedProteinBindingDisorderedResiduesPredictions parseProteinBindingDisorderedResiduePedictions( File tempDirectory ) throws Exception {
		
		
		DisopredParsedProteinBindingDisorderedResiduesPredictions disopredParsedProteinBindingDisorderedResiduesPredictions = new DisopredParsedProteinBindingDisorderedResiduesPredictions();
		
		List<DisopredParsedProteinBindingDisorderedResiduesPredictionEntry> disopredParsedProteinBindingDisorderedResiduesPredictionEntryList = new ArrayList<DisopredParsedProteinBindingDisorderedResiduesPredictionEntry>();
		disopredParsedProteinBindingDisorderedResiduesPredictions.setEntries(disopredParsedProteinBindingDisorderedResiduesPredictionEntryList);

		List<String> fileHeaderLines = new ArrayList<String>();
		disopredParsedProteinBindingDisorderedResiduesPredictions.setFileHeaderLines( fileHeaderLines );
		
		String filenameToRead = DisopredFilenameConstants.DISOPRED_OUTPUT_PROTEIN_BINDING_DISORDERED_FILENAME;
		
		File disopredOutputFile = new File( tempDirectory, filenameToRead );

		disopredParsedProteinBindingDisorderedResiduesPredictions.setFilename( filenameToRead );


		if ( !disopredOutputFile.exists() )
			throw new Exception( "Could not find results file at: " + disopredOutputFile.getAbsolutePath() );

		log.info( "Parsing output file '" + disopredOutputFile.getAbsolutePath() + "'." );



		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;

		try {

			fileInputStream = new FileInputStream( disopredOutputFile );
			
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




				//  dataline:  "    1 M * 0.96",  after split, index zero === "", index 1 === "1", index 2 === "M", ...

				String[] dataLineSplit = dataLine.split( "\\s+" );


				String positionString = dataLineSplit[ 1 ];
				String aminoAcid = dataLineSplit[ 2 ];
				String type = dataLineSplit[ 3 ];
				String probabilityString = dataLineSplit[ 4 ];



				int position = 0;

				try {
					position = Integer.parseInt( positionString );

				} catch ( Exception e ) {

					String msg = "Position parse failed for position string: |" + positionString + "|,\n dataLine: " + dataLine
							+ "\n output file '" + disopredOutputFile.getAbsolutePath() + "'.";

					log.error( msg );
					throw new Exception(msg, e);
				}
				

				BigDecimal probability = null;
				boolean probabilityNA = false;
				
				if ( PROBABILITY_NA_STRING.equals( probabilityString ) ) {
					
					probabilityNA = true;
					
				} else {

					try {
						probability = new BigDecimal( probabilityString );

					} catch ( Exception e ) {

						String msg = "Probability parse failed for probability string: |" + probabilityString + "|,\n dataLine: " + dataLine
								+ "\n output file '" + disopredOutputFile.getAbsolutePath() + "'.";

						log.error( msg );
						throw new Exception(msg, e);
					}
				}
				
				String outputType = null;
				
				
				if ( PBDAT_FILE_DISORDERED_BINDING_REGIONS_TYPE_DISORDERED.equals( type ) ) {

					outputType = DisorderedOrderedTypeConstants.DISORDERED_BINDING_TYPE;
				
				} else if ( PBDAT_FILE_DISORDERED_NON_BINDING_REGIONS_TYPE_DISORDERED.equals( type ) ) {

					outputType = DisorderedOrderedTypeConstants.DISORDERED_NON_BINDING_TYPE;
				
				} else if ( PBDAT_FILE_DISORDERED_REGIONS_TYPE_ORDERED.equals( type ) ) {

					outputType = DisorderedOrderedTypeConstants.ORDERED_TYPE;

				} else {

					String msg = "Unknown 'type' in record, type = |" + type + "|, dataLine: " + dataLine;

					log.error( msg );

					throw new Exception(msg);
				}


				DisopredParsedProteinBindingDisorderedResiduesPredictionEntry entry = new DisopredParsedProteinBindingDisorderedResiduesPredictionEntry();

				entry.setTypeRaw( type );
				entry.setType( outputType );
				entry.setPosition( position );
				entry.setAminoAcid( aminoAcid );
				entry.setProbability( probability );
				entry.setProbabilityNA( probabilityNA );
				
				disopredParsedProteinBindingDisorderedResiduesPredictionEntryList.add( entry );

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
		}



		return disopredParsedProteinBindingDisorderedResiduesPredictions;
	}



}
