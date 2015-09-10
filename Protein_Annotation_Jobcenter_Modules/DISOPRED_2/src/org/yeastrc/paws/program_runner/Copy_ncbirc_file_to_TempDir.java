package org.yeastrc.paws.program_runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.yeastrc.paws.constants.Filename_ncbirc_file_Constants;

/**
 * 
 *
 */
public class Copy_ncbirc_file_to_TempDir {

	private static final Logger log = Logger.getLogger(Copy_ncbirc_file_to_TempDir.class);
	
	/**
	 * @param tempDir
	 * @throws Exception 
	 */
	public static void copy_ncbirc_file_to_TempDir( File tempDir ) throws Exception {
		
		

		ClassLoader thisClassLoader = new Copy_ncbirc_file_to_TempDir().getClass().getClassLoader();

		URL fileURL = thisClassLoader.getResource( Filename_ncbirc_file_Constants.NCBIRC_FILENAME_IN_CONFIG );

		if ( fileURL == null ) {

			String msg = "'ncbirc' file '" + Filename_ncbirc_file_Constants.NCBIRC_FILENAME_IN_CONFIG + "' not found ";

			log.error( msg );

			throw new RuntimeException( msg );
		}

		log.info( "'ncbirc' file = '" + fileURL.getFile() + "'." );

		InputStream ncbircInputStream = null;
		OutputStream ncbircOutputStream = null;
		
		File tempDirFile = new File( tempDir, Filename_ncbirc_file_Constants.NCBIRC_FILENAME_TO_CREATE );

		try {
			ncbircInputStream = new FileInputStream( fileURL.getFile() );
			
			ncbircOutputStream = new FileOutputStream( tempDirFile );
				
			byte[] bytes = new byte[ 10000 ];
			int bytesRead = 0;
			
			while ( ( bytesRead = ncbircInputStream.read( bytes ) ) > 0 ) {
			
				ncbircOutputStream.write(bytes, 0, bytesRead );
			}
			

		} catch (Exception ex) {

			String msg = "Error copying 'ncbirc' file from " + fileURL.getFile()
					+ ", to " + tempDirFile.getAbsolutePath();

			log.error( msg + ex.toString(), ex );

			throw new RuntimeException( msg, ex );

		} finally {
			
			if ( ncbircInputStream != null ) {
				
				ncbircInputStream.close();
			}
			
			if ( ncbircOutputStream != null ) {
				
				ncbircOutputStream.close();
			}
		}
		
	}
}
