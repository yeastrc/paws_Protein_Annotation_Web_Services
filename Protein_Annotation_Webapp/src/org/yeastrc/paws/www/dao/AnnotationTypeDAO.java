package org.yeastrc.paws.www.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.yeastrc.paws.www.db.DBConnectionFactory;

/**
 * Table annotation_type
 *
 */
public class AnnotationTypeDAO {

	private static final Logger log = Logger.getLogger(AnnotationTypeDAO.class);

	private AnnotationTypeDAO() { }
	public static AnnotationTypeDAO getInstance() { return new AnnotationTypeDAO(); }
	
	

	/**
	 * Null if no record found
	 * 
	 * @param annotationType
	 * @return
	 * @throws Exception
	 */
	public Integer getAnnotationTypeIdByAnnotationType( String annotationType ) throws Exception {
		
		
		
		Integer result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		final String sql = "SELECT id FROM annotation_type WHERE type = ?";

		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

//			CREATE TABLE IF NOT EXISTS `paws`.`annotation_type` (
//					  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
//					  `type` VARCHAR(45) NOT NULL,

			
			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setString( counter, annotationType );
			
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {

				result = rs.getInt( "id" );
			}
			
			
		} catch ( Exception e ) {
			
			log.error( "ERROR: database connection: '" + DBConnectionFactory.PAWS + "' sql: " + sql, e );
			
			throw e;
			
		} finally {
			
			// be sure database handles are closed
			if( rs != null ) {
				try { rs.close(); } catch( Throwable t ) { ; }
				rs = null;
			}
			
			if( pstmt != null ) {
				try { pstmt.close(); } catch( Throwable t ) { ; }
				pstmt = null;
			}
			
			if( conn != null ) {
				try { conn.close(); } catch( Throwable t ) { ; }
				conn = null;
			}
			
		}
		
		
		return result;
	}
	
}
