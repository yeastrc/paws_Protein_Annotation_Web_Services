package org.yeastrc.paws.www.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.yeastrc.paws.www.db.DBConnectionFactory;

/**
 * Table config_system
 *
 */
public class ConfigSystemDAO {

	private static final Logger log = Logger.getLogger(ConfigSystemDAO.class);

	private ConfigSystemDAO() { }
	public static ConfigSystemDAO getInstance() { return new ConfigSystemDAO(); }
	
	

	/**
	 * Null if no record found
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String getValueFromKey( String key ) throws Exception {
		
		
		
		String result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		final String sql = "SELECT config_value FROM config_system WHERE config_key = ?";

		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

//			CREATE TABLE IF NOT EXISTS `paws`.`config_system` (
//					  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
//					  `config_key` VARCHAR(45) NOT NULL,
//					  `config_value` VARCHAR(4000) NULL,
//					  `comment` VARCHAR(4000) NULL,
//					  `version` INT UNSIGNED NOT NULL DEFAULT 0,

			
			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setString( counter, key );
			
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {

				result = rs.getString( "config_value" );
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
