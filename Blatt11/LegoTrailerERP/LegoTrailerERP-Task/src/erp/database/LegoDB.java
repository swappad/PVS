package erp.database;



import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
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

		try{
			Class.forName(driver);

		}catch(ClassNotFoundException e1){
			e1.printStackTrace();
		}
		try{
			Connection connection = DriverManager.getConnection(url);
			return connection;
		}catch(SQLException e2){
			e2.printStackTrace();
		}

		return null;
	}

	/**
	 * reads all Auftrag elements from the database and returns them
	 * 
	 * @return a List of the model type Auftrag
	 */
	public List<Auftrag> readAuftraege() {
		Connection connection = null;
		List<Auftrag> auftragList = new ArrayList<Auftrag>();
		try {
			connection = openConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM auftragsds");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				int auftrNr = resultSet.getInt("AuftrNr");
				int kdNr = resultSet.getInt("KdNr");
				String kdAuftrNr = resultSet.getString("KdAuftrNr");
				Date kdAuftrDatum = resultSet.getDate("KdAuftrDatum");
				Date erfassungsDatum = resultSet.getDate("ErfassungsDatum");
				Auftrag auftrag= new Auftrag(auftrNr,kdNr,kdAuftrNr,kdAuftrDatum,erfassungsDatum);
				auftrag.setAuftrStatus(resultSet.getInt("AuftrStatus"));
				auftragList.add(auftrag);
			}
			try{
				connection.close();
			}catch(SQLException e3){}
			return auftragList;

		}catch(SQLException e1){
			e1.printStackTrace();
		}finally {
			try{
				connection.close();
			}catch(SQLException e3){}
		}
		return null;
	}

	/**
	 * method to find the sequentially next auftragsnummer for use with a new
	 * Auftrag
	 * 
	 * @return the next unused auftragsnummer
	 */
	public int readNextAuftragsnummer() {
		Connection connection = null;
		try{
			connection = openConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(AuftrNr) as Last FROM auftragsds");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			try{
				connection.close();
			}catch(SQLException e3){}
			return resultSet.getInt("Last");

		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			try{
				connection.close();
			}catch(SQLException e3){}
		}

		return -1;
	}

	/**
	 * method for saving an Auftrag and all its Auftragspositionen(String[] teileID, int[] farbe, int[] amount) to the database
	 * IMPORTANT: the java.util.Date parameter "kdAuftrDatum" must be converted to a java.sql.Date before using it in a preparedstatement
	 */
	public void createAuftrag(int kdNr, String kdAuftrNr, java.util.Date kdAuftrDatum, String[] teileID, int[] farbe, int[] amount) {
		int nextAuftragsnummer = readNextAuftragsnummer(); // get the AuftragsNummer for the new auftrag before it is inserted into the database
		Connection connection= null;
		try{
			connection = openConnection();
			//PreparedStatement preparedStatement= connection.prepareStatement("INSERT INTO auftragsds VALUES(?,?,?,)");



			try{
				connection.close();
			}catch(SQLException e3){}
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			try{
				connection.close();
			}catch(SQLException e3){}
		}
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
		Connection connection= null;
		List<Auftragsposition> auftragspositionList = new ArrayList<Auftragsposition>();
		try{
			connection = openConnection();
			PreparedStatement preparedStatement =connection.prepareStatement("SELECT * FROM auftragsposds WHERE AuftrNR =?");
			preparedStatement.setInt(1,auftragsnummer);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){

				int auftragspositionsnummer = resultSet.getInt("AuftrPos");
				String teileID = resultSet.getString("TeileID");
				int farbe= resultSet.getInt("Farbe");
				float verkaufspreis=resultSet.getFloat("VkPreis");
				int anzahlBestellt=resultSet.getInt("AnzVonKundeBestellt");
				int anzahlReserviert=resultSet.getInt("AnzFuerKundeReserviert");
				int anzahlNochZuFertigen=resultSet.getInt("AnzNochZuFertigen");
				Date fertigungBeendet=resultSet.getDate("FertigungBeendet");
				int fertigungsstatus= resultSet.getInt("FertigungsStatus");
				//auftragspositionList.add(new Auftragsposition(auftragsnummer,auftragspositionsnummer,teileID,farbe,verkaufspreis,anzahlBestellt,anzahlReserviert,anzahlNochZuFertigen,A))
			}
			try{
				connection.close();
			}catch(SQLException e){}
			return
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			try{
				connection.close();
			}catch(SQLException e){}
		}

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
		Connection connection = null;
		try{
			connection = openConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT FIRST(TeileID) FROM teile WHERE TeileID=? AND Farbe=?" );
			preparedStatement.setString(1,teileID);
			preparedStatement.setInt(2,farbe);
			ResultSet resultSet = preparedStatement.executeQuery();
			try{
				connection.close();
			}catch(SQLException e3){}
			return resultSet.next();

		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			try{
				connection.close();
			}catch(SQLException e3){}
		}
		return false;
	}

	/**
	 * method for reading all kunden from the database
	 * 
	 * @return an ArrayList with all kunde objects in the database (sorted by their KdNr)
	 */
	public List<Kunde> readKunden() {
		Connection connection=null;
		List<Kunde> kundeList = new ArrayList<Kunde>();
		try{
			connection=openConnection();
			PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM kunden ORDER BY kdNr");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				int kdNr = resultSet.getInt("KdNr");
				String kdName = resultSet.getString("KdName");
				String kdStadt= resultSet.getString("KdStadt");
				int bonitaet= resultSet.getInt("bonitaet");
				kundeList.add(new Kunde(kdNr,kdName,kdStadt,bonitaet));
			}
			try{
				connection.close();
			}catch(SQLException e3){}

			return kundeList;

		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			try{
				connection.close();
			}catch(SQLException e3){}
		}

		return null;
	}
}
