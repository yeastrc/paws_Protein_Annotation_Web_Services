package org.yeastrc.paws.www.service;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.yeastrc.paws.www.dao.SequenceDAO;
import org.yeastrc.paws.www.db.DBConnectionFactory;


/**
 * 
 *
 */
public class AddSequenceOrGetExistingSequenceId {

	private static final Logger log = Logger.getLogger(AddSequenceOrGetExistingSequenceId.class);
	
	private AddSequenceOrGetExistingSequenceId() { }
	public static AddSequenceOrGetExistingSequenceId getInstance() { return new AddSequenceOrGetExistingSequenceId(); }
	
	
	/**
	 * @param sequence
	 * @return
	 * @throws Exception 
	 */
	public int addSequenceOrGetExistingSequenceId( String sequence ) throws Exception {

		int addedSequenceIdOrExistingSequenceId = 0;
		
		Connection dbConnection = null;
		
		SequenceDAO sequenceDAO = SequenceDAO.getInstance();
		
		try {

			dbConnection = getConnectionWithAutocommitTurnedOff();
			
			Integer sequenceIdFromDB = sequenceDAO.getSequenceIdBySequence( sequence, dbConnection );
			
			if ( sequenceIdFromDB == null ) {

				addedSequenceIdOrExistingSequenceId = addSequence( sequence, dbConnection );

			} else {

				addedSequenceIdOrExistingSequenceId = sequenceIdFromDB;
			}

			

			dbConnection.commit();
			
		} catch ( Exception e ) {
			
			String msg = "Failed addSequenceOrGetExistingSequenceId(...)";
			
			log.error( msg, e );
			
			if ( dbConnection != null ) {
				
				dbConnection.rollback();
			}
			
			throw e;
			
		} finally {
			
			
			if( dbConnection != null ) {
				
				
				try {
					dbConnection.setAutoCommit(true);  /// reset for next user of connection
				} catch (Exception ex) {
					String msg = "Failed dbConnection.setAutoCommit(true) in addSequenceOrGetExistingSequenceId(...)";

					log.error( msg, ex );
					
					throw new Exception(msg, ex);
				}
				
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
		}
		
		return addedSequenceIdOrExistingSequenceId;
	}
	
	
	/**
	 * @param sequence
	 * @param dbConnection
	 * @return
	 * @throws Exception 
	 */
	private int addSequence( String sequence, Connection dbConnection ) throws Exception {
		
		int addedSequenceId = 0;
		
		SequenceDAO sequenceDAO = SequenceDAO.getInstance();
		
		//  First lock sequence table to ensure no other process updates it between the query and the insert
		
		//  This is done to ensure that no other process also inserts a record with the same sequence
		
		
		try {
		
			sequenceDAO.lockSequenceTableForWrite( dbConnection );

			Integer sequenceIdFromDB = sequenceDAO.getSequenceIdBySequence( sequence, dbConnection );

			if ( sequenceIdFromDB == null ) {

				addedSequenceId = sequenceDAO.save( sequence, dbConnection );
			}
		
		} catch ( Exception e ) {
			
			String msg = "Failed addSequence(...)";

			log.error( msg, e );
			
			throw new Exception(msg, e);
			
		} finally {
			
			sequenceDAO.unlockAllTable( dbConnection );
			
		}
		
		return addedSequenceId;
		
	}
	
	
	/**
	 * @return
	 * @throws Exception
	 */
	private Connection getConnectionWithAutocommitTurnedOff(  ) throws Exception {
		
		Connection dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.PAWS );
		
		dbConnection.setAutoCommit(false);
		
		return dbConnection;
	}
	
}
