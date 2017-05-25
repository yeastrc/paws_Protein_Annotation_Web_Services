package org.yeastrc.paws.www.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.yeastrc.paws.www.constants.AnnotationDataRunStatusConstants;
import org.yeastrc.paws.www.constants.Database_OneTrueZeroFalse_Constants;
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
	 * @param id
	 * @param annotationTypeId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception
	 */
	public AnnotationProcessingTrackingDTO getById( int id ) throws Exception {
		
		
		
		AnnotationProcessingTrackingDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		final String sql = "SELECT * FROM annotation_processing_tracking WHERE id = ?";

		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, id );
			counter++;
			
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
	

	private static final String getAnnotationProcessingTrackingDTOBySequenceIdAnnotationTypeNCBITaxonomyIdSQL =
			"SELECT * FROM annotation_processing_tracking "
			+ "WHERE sequence_id = ? AND annotation_type_id = ? AND ncbi_taxonomy_id = ? ";

	/**
	 * 
	 * @param sequenceId
	 * @param annotationTypeId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception
	 */
	public List<AnnotationProcessingTrackingDTO> getAnnotationProcessingTrackingDTOBySequenceIdAnnotationTypeNCBITaxonomyId(
			int sequenceId, int annotationTypeId, int ncbiTaxonomyId ) throws Exception {
		
		List<AnnotationProcessingTrackingDTO> resultList = new ArrayList<AnnotationProcessingTrackingDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		final String sql = getAnnotationProcessingTrackingDTOBySequenceIdAnnotationTypeNCBITaxonomyIdSQL;

		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, sequenceId );
			counter++;
			pstmt.setInt( counter, annotationTypeId );
			counter++;
			pstmt.setInt( counter, ncbiTaxonomyId );
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {

				AnnotationProcessingTrackingDTO result = populateFromResultSet(rs);
				resultList.add( result );
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
		
		
		return resultList;
	}
	
	

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private AnnotationProcessingTrackingDTO populateFromResultSet(ResultSet rs) throws SQLException {
		
		AnnotationProcessingTrackingDTO result = new AnnotationProcessingTrackingDTO();
	
		result.setId( rs.getInt( "id" ) );
		result.setSequenceId( rs.getInt( "sequence_id" ) );
		result.setAnnotationTypeId( rs.getInt( "annotation_type_id" ) );
		result.setNcbiTaxonomyId( rs.getInt( "ncbi_taxonomy_id" ) );
		result.setJobcenterRequestId( rs.getInt( "jobcenter_request_id" ) );
		
		int batch_requestInt = rs.getInt( "batch_request" );
		if ( batch_requestInt == Database_OneTrueZeroFalse_Constants.DATABASE_FIELD_TRUE ) {
			result.setBatchRequest(true);
		} else {
			result.setBatchRequest(false);
		}

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
			+ " ( sequence_id, annotation_type_id, ncbi_taxonomy_id, "
			+   " requesting_ip, batch_request, batch_request_id, run_status )"
			+ " VALUES ( ?, ?, ?, ?, ?, ?, ? )";

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

//			connection = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

//			pstmt = connection.prepareStatement( insertSQL );
			pstmt = connection.prepareStatement( insertSQL, Statement.RETURN_GENERATED_KEYS );

			int counter = 0;

			counter++;
			pstmt.setInt( counter, item.getSequenceId() );
			counter++;
			pstmt.setInt( counter, item.getAnnotationTypeId() );
			counter++;
			pstmt.setInt( counter, item.getNcbiTaxonomyId() );
			
			counter++;
			pstmt.setString( counter, item.getRequestingIP() );

			int batch_requestInt = Database_OneTrueZeroFalse_Constants.DATABASE_FIELD_FALSE;
			if ( item.isBatchRequest() ) {
				batch_requestInt = Database_OneTrueZeroFalse_Constants.DATABASE_FIELD_TRUE;
			}
			counter++;
			pstmt.setInt( counter, batch_requestInt );

			counter++;
			pstmt.setString( counter, item.getBatchRequestId() );

			counter++;
			pstmt.setString( counter, item.getRunStatus() );

			int rowsUpdated = pstmt.executeUpdate();

			if ( rowsUpdated == 0 ) {

			}

			rsGenKeys = pstmt.getGeneratedKeys();

			if ( rsGenKeys.next() ) {

				item.setId( rsGenKeys.getInt( 1 ) );
			}



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
	public void updateJobcenterRequestId( AnnotationProcessingTrackingDTO item ) throws Exception {

		Connection connection = null;


		try {
			connection = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );
			updateJobcenterRequestId( item, connection );

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

	private static String updateJobcenterRequestIdSQL = "UPDATE annotation_processing_tracking " 
			+ " SET jobcenter_request_id = ?"
			+ " WHERE id = ? ";

	/**
	 * @param item
	 * @return
	 * @throws Throwable
	 */
	public void updateJobcenterRequestId( AnnotationProcessingTrackingDTO item, Connection connection ) throws Exception {

//		Connection connection = null;

		PreparedStatement pstmt = null;

		ResultSet rsGenKeys = null;

		try {
//			connection = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			pstmt = connection.prepareStatement( updateJobcenterRequestIdSQL );

			int counter = 0;

			counter++;
			pstmt.setInt( counter, item.getJobcenterRequestId() );

			counter++;
			pstmt.setInt( counter, item.getId() );

			int rowsUpdated = pstmt.executeUpdate();

			if ( rowsUpdated == 0 ) {}

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
			

	private static String updateRunStatusSQL = 
			"UPDATE annotation_processing_tracking " 
			+ " SET run_status = ?"
			+ " WHERE id = ? ";

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
//			connection = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			pstmt = connection.prepareStatement( updateRunStatusSQL );

			int counter = 0;

			counter++;
			pstmt.setString( counter, item.getRunStatus() );

			counter++;
			pstmt.setInt( counter, item.getId() );

			int rowsUpdated = pstmt.executeUpdate();

			if ( rowsUpdated == 0 ) {

			}


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


	private static final String updateRunStatusBySequenceIdAnnotationTypeNCBITaxonomyIdSQL =
			"UPDATE annotation_processing_tracking " 
					+ " SET run_status = ? "
			+ " WHERE run_status = '" + AnnotationDataRunStatusConstants.STATUS_SUBMITTED + "' "
			+   " AND sequence_id = ? AND annotation_type_id = ? AND ncbi_taxonomy_id = ? ";

	/**
	 * 
	 * @param sequenceId
	 * @param annotationTypeId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception
	 */
	public void updateRunStatusBySequenceIdAnnotationTypeNCBITaxonomyId(
			String newRunStatus,
			int sequenceId, int annotationTypeId, int ncbiTaxonomyId ) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		final String sql = updateRunStatusBySequenceIdAnnotationTypeNCBITaxonomyIdSQL;

		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setString( counter, newRunStatus );
			counter++;
			pstmt.setInt( counter, sequenceId );
			counter++;
			pstmt.setInt( counter, annotationTypeId );
			counter++;
			pstmt.setInt( counter, ncbiTaxonomyId );

			int rowsUpdated = pstmt.executeUpdate();
			
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
		
	}
	

}
