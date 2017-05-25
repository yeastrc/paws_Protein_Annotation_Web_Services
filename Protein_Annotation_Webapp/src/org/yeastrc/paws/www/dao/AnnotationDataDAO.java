package org.yeastrc.paws.www.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.yeastrc.paws.www.db.DBConnectionFactory;
import org.yeastrc.paws.www.dto.AnnotationDataDTO;

/**
 * Table annotation_data
 *
 */
public class AnnotationDataDAO {

	private static final Logger log = Logger.getLogger(AnnotationDataDAO.class);

	private AnnotationDataDAO() { }
	public static AnnotationDataDAO getInstance() { return new AnnotationDataDAO(); }
	
	

	/**
	 * Null if no record found
	 * 
	 * @param sequenceId
	 * @param annotationTypeId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception
	 */
	public AnnotationDataDTO getAnnotationDataDTOBySequenceIdAnnotationTypeNCBITaxonomyId( int sequenceId, int annotationTypeId, int ncbiTaxonomyId ) throws Exception {
		
		
		
		AnnotationDataDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		final String sql = "SELECT * FROM annotation_data WHERE sequence_id = ? AND annotation_type_id = ? AND ncbi_taxonomy_id = ?";

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
	private AnnotationDataDTO populateFromResultSet(ResultSet rs) throws SQLException {
		
		AnnotationDataDTO result = new AnnotationDataDTO();
	
		result.setSequenceId( rs.getInt( "sequence_id" ) );
		result.setAnnotationTypeId( rs.getInt( "annotation_type_id" ) );
		result.setNcbiTaxonomyId( rs.getInt( "ncbi_taxonomy_id" ) );
		result.setAnnotationData( rs.getString( "annotation_data" ) );
		result.setRunStatus( rs.getString( "run_status" ) );
		result.setLastRunDate( rs.getDate( "last_run_date" ) );
		
		return result;
	}
	
	
	/**
	 * @param item
	 * @return
	 * @throws Throwable
	 */
	public void save( AnnotationDataDTO item ) throws Exception {

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
			

	private static String insertSQL = "INSERT INTO annotation_data " 
			+ " ( sequence_id, annotation_type_id, ncbi_taxonomy_id, run_status, last_run_date, annotation_data )"
			+ " VALUES ( ?, ?, ?, ?,  NOW(), ? )";

	/**
	 * @param item
	 * @return
	 * @throws Throwable
	 */
	public void save( AnnotationDataDTO item, Connection connection ) throws Exception {

//		Connection connection = null;

		PreparedStatement pstmt = null;

		ResultSet rsGenKeys = null;

		try {


//			connection = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			pstmt = connection.prepareStatement( insertSQL );

			int counter = 0;

			counter++;
			pstmt.setInt( counter, item.getSequenceId() );
			counter++;
			pstmt.setInt( counter, item.getAnnotationTypeId() );
			counter++;
			pstmt.setInt( counter, item.getNcbiTaxonomyId() );
			counter++;
			pstmt.setString( counter, item.getRunStatus() );
			counter++;
			pstmt.setString( counter, item.getAnnotationData() );

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
	public void updateAnnotationDataAnnDataRunStatus( AnnotationDataDTO item ) throws Exception {

		Connection connection = null;
		try {
			connection = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			updateAnnotationDataAnnDataRunStatus( item, connection );
			
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
			

	private static String updateAnnotationDataRunStatusSQL = "UPDATE annotation_data " 
			+ " SET annotation_data = ?, run_status = ?, last_run_date = NOW()"
			+ " WHERE sequence_id = ? AND annotation_type_id = ? AND ncbi_taxonomy_id = ? ";

	/**
	 * @param item
	 * @return
	 * @throws Throwable
	 */
	public void updateAnnotationDataAnnDataRunStatus( AnnotationDataDTO item, Connection connection ) throws Exception {

//		Connection connection = null;

		PreparedStatement pstmt = null;

		ResultSet rsGenKeys = null;

		try {


//			connection = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			pstmt = connection.prepareStatement( updateAnnotationDataRunStatusSQL );

			int counter = 0;

			counter++;
			pstmt.setString( counter, item.getAnnotationData() );
			counter++;
			pstmt.setString( counter, item.getRunStatus() );

			counter++;
			pstmt.setInt( counter, item.getSequenceId() );
			counter++;
			pstmt.setInt( counter, item.getAnnotationTypeId() );
			counter++;
			pstmt.setInt( counter, item.getNcbiTaxonomyId() );

			int rowsUpdated = pstmt.executeUpdate();

			if ( rowsUpdated == 0 ) {

			}

		} catch (Exception sqlEx) {
			log.error("updateAnnotationDataAnnDataRunStatus: Exception '" + sqlEx.toString() + ".\nSQL = " + updateAnnotationDataRunStatusSQL , sqlEx);
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
