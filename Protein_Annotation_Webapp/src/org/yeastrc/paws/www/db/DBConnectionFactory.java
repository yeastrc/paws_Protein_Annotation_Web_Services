package org.yeastrc.paws.www.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * Get connections to the database. Unless using JNDI naming (such as with the web site), be sure
 * to set the IDBConnectionFactory implementer to be used to get connections to the database via
 * setDbConnectionFactoryImpl( your_implementation_here ) at the start of your program.

 * @author Mike
 *
 */
public class DBConnectionFactory  {
	
	private static final Logger log = Logger.getLogger(DBConnectionFactory.class);
			

	public static final String PAWS = "paws";

	/**
	 * Instance variable holding the IDBConnectionFactory we want serving
	 * connections
	 */
	private static IDBConnectionFactory dbConnectionFactoryImpl = null;

	/**
	 * Get the IDBConnectionFactory we want serving connections.
	 * 
	 * @return
	 */
	public static IDBConnectionFactory getDbConnectionFactoryImpl() {
		return dbConnectionFactoryImpl;
	}

	/**
	 * Set the IDBConnectionFactory we want serving connections. If not set,
	 * JNDI will be used (I.e., the web site)
	 * 
	 * @param connectionFactoryImpl
	 */
	public static void setDbConnectionFactoryImpl(
			IDBConnectionFactory connectionFactoryImpl) {
		DBConnectionFactory.dbConnectionFactoryImpl = connectionFactoryImpl;
	}

	/**
	 * Get a connection to the specified database.
	 * 
	 * @param db
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String db) throws Exception {

		if (dbConnectionFactoryImpl != null) {
			return dbConnectionFactoryImpl.getConnection(db);
		}

		return getConnectionWeb(db);
	}

	/**
	 * Get DataSource from JNDI as setup in Application Server and get database
	 * connection from it
	 * 
	 * @param db
	 * @return
	 * @throws SQLException
	 */
	private static Connection getConnectionWeb(String db) throws Exception {

		try {
			Context ctx = new InitialContext();
			DataSource ds;
			Connection conn;

			if (db.equals(PAWS)) {
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/paws");
			}

			else {
				throw new SQLException(
						"Invalid database name passed into DBConnectionManager.");
			}

			if (ds != null) {
				conn = ds.getConnection();
				if (conn != null) {
					
					boolean connectionAutoCommit = conn.getAutoCommit();

					return conn;
				} else {
					throw new SQLException("Got a null connection...");
				}
			}
			

			throw new SQLException("Got a null DataSource...");
		} catch (NamingException ne) {
			
			log.error( "ERROR: getting database connection: db: " + db, ne );

			throw new SQLException("Naming exception: " + ne.getMessage(), ne);
		
			
		} catch ( Exception e ) {
			
			log.error( "ERROR: getting database connection: db: " + db, e );
			
			throw e;
		}
	}
	
	
	public static void closeAllConnections() throws Exception {
		if (dbConnectionFactoryImpl != null) {
			
			try {
				dbConnectionFactoryImpl.closeAllConnections();


			} catch ( Exception e ) {

				log.error( "ERROR: closeAllConnections() ", e );

				throw e;
			}

		}
	}
	
}
