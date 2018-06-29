package erp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Database connectivity class, handles all database read/write access for the
 * program
 * 
 */
public class LegoDB {

	/**
	 * connection method, called once on program initialization
	 */
	public Connection openConnection() {
		// MARIADB / MYSQL
		String url = "jdbc:mariadb://localhost:3306/legotrailer_db?user=root";
		String driver = "org.mariadb.jdbc.Driver";

		//TODO	
		return null;
	}

	/**
	 * reads all Auftrag elements from the database and returns them
	 * 
	 * @return a List of the model type Auftrag
	 */
	public List<Auftrag> readAuftraege() {
		//TODO
		return null;
	}

	/**
	 * method to find the sequentially next auftragsnummer for use with a new
	 * Auftrag
	 * 
	 * @return the next unused auftragsnummer
	 */
	public int readNextAuftragsnummer() {
		//TODO
		return -1;
	}

	/**
	 * method for saving an Auftrag and all its Auftragspositionen(String[] teileID, int[] farbe, int[] amount) to the database
	 * IMPORTANT: the java.util.Date parameter "kdAuftrDatum" must be converted to a java.sql.Date before using it in a preparedstatement
	 */
	public void createAuftrag(int kdNr, String kdAuftrNr, java.util.Date kdAuftrDatum, String[] teileID, int[] farbe, int[] amount) {
		int nextAuftragsnummer = readNextAuftragsnummer(); // get the AuftragsNummer for the new auftrag before it is inserted into the database
		//TODO
	}

	/**
	 * reads all Auftragsposition elements for a given auftragsnummer from the
	 * database and returns them
	 * 
	 * @param auftragsnummer
	 *            the auftragsnummer of the Auftrag you want the Auftragspositionen
	 *            of
	 * @return a List of the model type Auftragsposition
	 */
	public List<Auftragsposition> readAuftragspositionen(int auftragsnummer) {
		//TODO
		return null;
	}

	/**
	 * checks a given teileID/farbe combination for validity by checking the TEILE
	 * relation in the database, only combinations listed there are accepted
	 * 
	 * @param teileID
	 *            ID number of the part in question
	 * @param farbe
	 *            color code of the part
	 * @return true or false, depending if the part exists in this color
	 */
	public boolean checkValidTeil(String teileID, int farbe) {
		//TODO
		return false;
	}

	/**
	 * method for reading all kunden from the database
	 * 
	 * @return an ArrayList with all kunde objects in the database (sorted by their KdNr)
	 */
	public List<Kunde> readKunden() {
		//TODO
		return null;
	}
}
