package org.yeastrc.paws.www.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.yeastrc.paws.www.db.DBConnectionFactory;
import org.yeastrc.paws.www.dto.AnnotationProcessingTrackingDTO;

/**
 * Table annotation_processing_tracking
 *
 */
public class AnnotationProcessingTrackingDAO {

	private static final Logger log = Logger.getLogger(AnnotationProcessingTrackingDAO.class);

	private AnnotationProcessingTrackingDAO() { }
	public static AnnotationProcessingTrackingDAO getInstance() { return new AnnotationProcessingTrackingDAO(); }
	
	

	/**
	 * Null if no record found
	 * 
	 * @param sequenceId
	 * @param annotationTypeId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception
	 */
	public AnnotationProcessingTrackingDTO getAnnotationProcessingTrackingDTOBySequenceIdAnnotationTypeNCBITaxonomyId( int sequenceId, int annotationTypeId, int ncbiTaxonomyId, int jobcenterRequestId ) throws Exception {
		
		
		
		AnnotationProcessingTrackingDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		final String sql = "SELECT * FROM annotation_processing_tracking WHERE sequence_id = ? AND annotation_type_id = ? AND ncbi_taxonomy_id = ? AND jobcenter_request_id = ?";

		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );


			//
			//CREATE TABLE annotation_processing_tracking (
			//  sequence_id INT UNSIGNED NOT NULL,
			//  annotation_type_id INT UNSIGNED NOT NULL,
			//  ncbi_taxonomy_id INT UNSIGNED NOT NULL,
			//  jobcenter_request_id INT UNSIGNED NOT NULL,
			//  run_status ENUM('submitted','complete','fail') NOT NULL,
			//  last_update_date DATETIME NOT NULL,

			
			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, sequenceId );
			counter++;
			pstmt.setInt( counter, annotationTypeId );
			counter++;
			pstmt.setInt( counter, ncbiTaxonomyId );
			counter++;
			pstmt.setInt( counter, jobcenterRequestId );
			
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {

				result = populateFromResultSet(rs);
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
	

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private AnnotationProcessingTrackingDTO populateFromResultSet(ResultSet rs) throws SQLException {
		
		AnnotationProcessingTrackingDTO result = new AnnotationProcessingTrackingDTO();
//		
		result.setSequenceId( rs.getInt( "sequence_id" ) );
		result.setAnnotationTypeId( rs.getInt( "annotation_type_id" ) );
		result.setNcbiTaxonomyId( rs.getInt( "ncbi_taxonomy_id" ) );
		result.setJobcenterRequestId( rs.getInt( "jobcenter_request_id" ) );
		result.setRunStatus( rs.getString( "run_status" ) );
		result.setLastRunDate( rs.getDate( "last_update_date" ) );

		return result;
	}
	
	
	

	/**
	 * @param item
	 * @return
	 * @throws Throwable
	 */
	public void save( AnnotationProcessingTrackingDTO item ) throws Exception {

		Connection connection = null;


		try {


			connection = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			save( item, connection );
			

//		} catch (Exception sqlEx) {
//			log.error("save:Exception '" + sqlEx.toString() + ".\nSQL = " + insertSQL , sqlEx);
//			throw sqlEx;

		} finally {

			
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ex) {
					// ignore
				}
			}
		}


	}
			

	private static String insertSQL = "INSERT INTO annotation_processing_tracking " 
			+ " ( sequence_id, annotation_type_id, ncbi_taxonomy_id, jobcenter_request_id, run_status, last_update_date )"
			+ " VALUES ( ?, ?, ?, ?, ?,  NOW() )";



	//
	//CREATE TABLE annotation_processing_tracking (
	//  sequence_id INT UNSIGNED NOT NULL,
	//  annotation_type_id INT UNSIGNED NOT NULL,
	//  ncbi_taxonomy_id INT UNSIGNED NOT NULL,
	//  jobcenter_request_id INT UNSIGNED NOT NULL,
	//  run_status ENUM('submitted','complete','fail') NOT NULL,
	//  last_update_date DATETIME NOT NULL,


	/**
	 * @param item
	 * @return
	 * @throws Throwable
	 */
	public void save( AnnotationProcessingTrackingDTO item, Connection connection ) throws Exception {

//		Connection connection = null;

		PreparedStatement pstmt = null;

		ResultSet rsGenKeys = null;

		try {


//			connection = DBConnectionFactory.getConnection( DBConnectionFactory.CROSSLINKS );

			pstmt = connection.prepareStatement( insertSQL );

			int counter = 0;

			counter++;
			pstmt.setInt( counter, item.getSequenceId() );
			counter++;
			pstmt.setInt( counter, item.getAnnotationTypeId() );
			counter++;
			pstmt.setInt( counter, item.getNcbiTaxonomyId() );
			counter++;
			pstmt.setInt( counter, item.getJobcenterRequestId());
			counter++;
			pstmt.setString( counter, item.getRunStatus() );

			int rowsUpdated = pstmt.executeUpdate();

			if ( rowsUpdated == 0 ) {

			}
//
//			rsGenKeys = pstmt.getGeneratedKeys();
//
//			if ( rsGenKeys.next() ) {
//
//				item.setId( rsGenKeys.getInt( 1 ) );
//			}



		} catch (Exception sqlEx) {
			log.error("save:Exception '" + sqlEx.toString() + ".\nSQL = " + insertSQL , sqlEx);
			throw sqlEx;

		} finally {

			if (rsGenKeys != null) {
				try {
					rsGenKeys.close();
				} catch (Exception ex) {
					// ignore
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException ex) {
					// ignore
				}
			}

//			if (connection != null) {
//				try {
//					connection.close();
//				} catch (Exception ex) {
//					// ignore
//				}
//			}
		}


	}
	
	

	/**
	 * @param item
	 * @return
	 * @throws Throwable
	 */
	public void updateRunStatus( AnnotationProcessingTrackingDTO item ) throws Exception {

		Connection connection = null;


		try {


			connection = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			updateRunStatus( item, connection );
			

//		} catch (Exception sqlEx) {
//			log.error("save:Exception '" + sqlEx.toString() + ".\nSQL = " + insertSQL , sqlEx);
//			throw sqlEx;

		} finally {

			
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ex) {
					// ignore
				}
			}
		}


	}
			

	private static String updateRunStatusSQL = "UPDATE annotation_processing_tracking " 
			+ " SET run_status = ?, last_update_date = NOW()"
			+ " WHERE sequence_id = ? AND annotation_type_id = ? AND ncbi_taxonomy_id = ? and jobcenter_request_id = ? ";


	//
	//CREATE TABLE annotation_processing_tracking (
	//  sequence_id INT UNSIGNED NOT NULL,
	//  annotation_type_id INT UNSIGNED NOT NULL,
	//  ncbi_taxonomy_id INT UNSIGNED NOT NULL,
	//  jobcenter_request_id INT UNSIGNED NOT NULL,
	//  run_status ENUM('submitted','complete','fail') NOT NULL,
	//  last_update_date DATETIME NOT NULL,



	/**
	 * @param item
	 * @return
	 * @throws Throwable
	 */
	public void updateRunStatus( AnnotationProcessingTrackingDTO item, Connection connection ) throws Exception {

//		Connection connection = null;

		PreparedStatement pstmt = null;

		ResultSet rsGenKeys = null;

		try {


//			connection = DBConnectionFactory.getConnection( DBConnectionFactory.CROSSLINKS );

			pstmt = connection.prepareStatement( updateRunStatusSQL );

			int counter = 0;

			counter++;
			pstmt.setString( counter, item.getRunStatus() );

			counter++;
			pstmt.setInt( counter, item.getSequenceId() );
			counter++;
			pstmt.setInt( counter, item.getAnnotationTypeId() );
			counter++;
			pstmt.setInt( counter, item.getNcbiTaxonomyId() );
			counter++;
			pstmt.setInt( counter, item.getJobcenterRequestId() );

			int rowsUpdated = pstmt.executeUpdate();

			if ( rowsUpdated == 0 ) {

			}
//
//			rsGenKeys = pstmt.getGeneratedKeys();
//
//			if ( rsGenKeys.next() ) {
//
//				item.setId( rsGenKeys.getInt( 1 ) );
//			}



		} catch (Exception sqlEx) {
			log.error("updateRunStatus: Exception '" + sqlEx.toString() + ".\nSQL = " + updateRunStatusSQL , sqlEx);
			throw sqlEx;

		} finally {

			if (rsGenKeys != null) {
				try {
					rsGenKeys.close();
				} catch (Exception ex) {
					// ignore
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException ex) {
					// ignore
				}
			}

//			if (connection != null) {
//				try {
//					connection.close();
//				} catch (Exception ex) {
//					// ignore
//				}
//			}
		}


	}
}
