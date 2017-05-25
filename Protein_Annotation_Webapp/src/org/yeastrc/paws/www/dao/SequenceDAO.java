package org.yeastrc.paws.www.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.yeastrc.paws.www.db.DBConnectionFactory;

/**
 * Table sequence
 *
 */
public class SequenceDAO {

	private static final Logger log = Logger.getLogger(SequenceDAO.class);

	private SequenceDAO() { }
	public static SequenceDAO getInstance() { return new SequenceDAO(); }
	
	

	/**
	 * Null if no record found
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String getSequenceById( int id ) throws Exception {
		
		
		
		String result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		final String sql = "SELECT sequence FROM sequence WHERE id = ? ";

		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, id );
			counter++;
			
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {

				result = rs.getString( "sequence" );
			}
			
			
		} catch ( Exception e ) {
			
			log.error( "ERROR: database connection: '" + DBConnectionFactory.PAWS + "' sql: " + sql, e );
			
			throw e;
			
		} finally {
			
			// be sure database handles are closed
			if( rs != null ) {
				try { rs.close(); } catch( Exception t ) { ; }
				rs = null;
			}
			
			if( pstmt != null ) {
				try { pstmt.close(); } catch( Exception t ) { ; }
				pstmt = null;
			}
			
			if( conn != null ) {
				try { conn.close(); } catch( Exception t ) { ; }
				conn = null;
			}
			
		}
		
		
		return result;
	}
	

	/**
	 * return null if no record found
	 * 
	 * @param sequence
	 * @return
	 * @throws Exception
	 */
	public Integer getSequenceIdBySequence( String sequence ) throws Exception {
		
		Connection conn = null;

		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			return getSequenceIdBySequence( sequence, conn );
			
		} finally {
			
			// be sure database handles are closed
			
			if( conn != null ) {
				try { conn.close(); } catch( Exception t ) { ; }
				conn = null;
			}
		}
	}

	/**
	 * return null if no record found
	 * 
	 * @param sequence
	 * @return
	 * @throws Exception
	 */
	public Integer getSequenceIdBySequence( String sequence, Connection conn ) throws Exception {
		
		
		
		Integer result = null;
		
//		Connection conn = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		final String sql = "SELECT id FROM sequence WHERE sequence = ? ORDER BY id LIMIT 1 ";
		
		//  ORDER BY id to make it deterministic to return the smallest id for this sequence string

		try {
			
//			conn = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );

			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setString( counter, sequence );
			counter++;
			
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
				try { rs.close(); } catch( Exception t ) { ; }
				rs = null;
			}
			
			if( pstmt != null ) {
				try { pstmt.close(); } catch( Exception t ) { ; }
				pstmt = null;
			}
			
//			if( conn != null ) {
//				try { conn.close(); } catch( Exception t ) { ; }
//				conn = null;
//			}
			
		}
		
		
		return result;
	}
	


	private static String lockSequenceTableForWriteSQL = "LOCK TABLES sequence WRITE";



	/**
	 * Lock sequence table for write
	 *
	 * All tables read or written to have to be locked with a single "LOCK TABLES" statement
	 * 
	 * @throws Exception
	 */
	public void lockSequenceTableForWrite( Connection connection ) throws Exception {

		PreparedStatement pstmt = null;

		try {

			pstmt = connection.prepareStatement( lockSequenceTableForWriteSQL );

			pstmt.executeUpdate();

		} catch (Exception sqlEx) {

			log.error("lockSequenceTableForWrite: Exception '" + sqlEx.toString() + ".\nSQL = " + lockSequenceTableForWriteSQL , sqlEx);

			throw sqlEx;

		} finally {

			if (pstmt != null) {

				try {

					pstmt.close();

				} catch (SQLException ex) {

					// ignore

				}

			}

		}

	}


	private static String unlockAllTableSQL = "UNLOCK TABLES";

	/**

	 * Unlock All Tables

	 * @throws Exception

	 */

	public void unlockAllTable( Connection connection ) throws Exception {

		PreparedStatement pstmt = null;

		try {

			pstmt = connection.prepareStatement( unlockAllTableSQL );

			pstmt.executeUpdate();

		} catch (Exception sqlEx) {

			log.error("unlockAllTable: Exception '" + sqlEx.toString() + ".\nSQL = " + unlockAllTableSQL , sqlEx);

			throw sqlEx;

		} finally {

			if (pstmt != null) {

				try {

					pstmt.close();

				} catch (SQLException ex) {

					// ignore

				}

			}


		}

	}

	private static String insertSQL = "INSERT INTO sequence " 
			+ " ( sequence )"
			+ " VALUES ( ? )";

	
	/**
	 * @param sequence
	 * @return assigned id
	 * @throws Exception
	 */
	public int save( String sequence, Connection connection ) throws Exception {

		
		int assignedId = 0;
		
//		Connection connection = null;

		PreparedStatement pstmt = null;

		ResultSet rsGenKeys = null;

		try {


//			connection = DBConnectionFactory.getConnection( DBConnectionFactory.CROSSLINKS );

			pstmt = connection.prepareStatement( insertSQL, Statement.RETURN_GENERATED_KEYS );

			int counter = 0;

			counter++;
			pstmt.setString( counter, sequence );

			int rowsUpdated = pstmt.executeUpdate(  );
			

			if ( rowsUpdated == 0 ) {

			}
//
			rsGenKeys = pstmt.getGeneratedKeys();

			if ( rsGenKeys.next() ) {

				assignedId = rsGenKeys.getInt( 1 );
			} else {
				
				throw new Exception( "Unable to get assigned id" );
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

		return assignedId;

	}
	
	


	private static String deleteSQL = "DELETE FROM sequence WHERE id = ?"; 
	
	/**
	 * @param id
	 * @return assigned id
	 * @throws Exception
	 */
	public int delete( int id, Connection connection ) throws Exception {

		
		int assignedId = 0;
		
//		Connection connection = null;

		PreparedStatement pstmt = null;


		try {


//			connection = DBConnectionFactory.getConnection( DBConnectionFactory.CROSSLINKS );

			pstmt = connection.prepareStatement( deleteSQL );

			int counter = 0;

			counter++;
			pstmt.setInt( counter, id );

			int rowsUpdated = pstmt.executeUpdate();

			if ( rowsUpdated == 0 ) {

			}
//



		} catch (Exception sqlEx) {
			log.error("delete:Exception '" + sqlEx.toString() + ".\nSQL = " + deleteSQL , sqlEx);
			throw sqlEx;

		} finally {


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

		return assignedId;

	}
}
