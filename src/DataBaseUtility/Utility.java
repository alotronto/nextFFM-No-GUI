package DataBaseUtility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utility {
	
	
	//------
	private static Statement myStatement;
	private static ResultSet myResultSet;
	private static Connection myConnection;
	//-------
	
	// ----Parametri di connessione db----------
	private static String host = "";
	private static String db = "";
	private static String user = "";
	private static String pass = "";
	//-------------------------------------------
	
	public static void setHost(String value){
		host = value;
	}
	public static void setDB(String value){
		db = value;
	}
	public static void setUser(String value){
		user = value;
	}
	public static void setPass(String value){
		pass = value;
	}
	/**
	 * Metodo per apertura di una connessione ad un DBMS
	 * 
	 * @param host
	 * @param db
	 * @param user
	 * @param pass
	 */
	public static void dbOpenConnection(String host, String db, String user,String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String connectionUrl = "jdbc:mysql://" + host + ":3306/" + db
					+ "?user=" + user + "&password=" + pass;
			myConnection = DriverManager.getConnection(connectionUrl);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Metodo per la chiusura della connessione al DBMS
	 */
	public static void dbCloseConnection() {
		try {
			myResultSet.close();
			myStatement.close();
			myConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Metdodo per il reperimento delle competenze necessarie per l'esuzione di un determinato intervento
	 * @param id ID dell'intervento
	 * @return ResultSet contenente il risultato della query e in particolare idIntevento,idInterventoTipo,tipoCompetenza,idCompetenza
	 */
	public static ResultSet getCopetenzeIntervento(String id){
		dbOpenConnection(host, db, user, pass);
		try {
			myStatement = myConnection.createStatement();
			/*System.out.print("SELECT intervento.idIntervento, " +
					"intervento.idInterventoTipo, competenzetecniche.tipoCompetenza, competenzetecniche.idCompetenza " +
					"FROM  `capacitarichieste` JOIN intervento ON " +
					"intervento.idInterventoTipo = capacitarichieste.idInterventoTipo "+
					"JOIN competenzetecniche ON capacitarichieste.idCompetenza = competenzetecniche.idCompetenza "+
					"WHERE intervento.idIntervento= "+id+" ;");*/
			return myResultSet = myStatement.executeQuery("SELECT intervento.idIntervento, " +
					"intervento.idInterventoTipo, competenzetecniche.tipoCompetenza, competenzetecniche.idCompetenza " +
					"FROM  `capacitarichieste` JOIN intervento ON " +
					"intervento.idInterventoTipo = capacitarichieste.idInterventoTipo "+
					"JOIN competenzetecniche ON capacitarichieste.idCompetenza = competenzetecniche.idCompetenza "+
					"WHERE intervento.idIntervento= "+id+" ;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			return myResultSet = null;
		}
	}

	/**
	 * Metodo per l'estrazione delle informazioni sulle squadre dal database
	 * 
	 * @return ResultSet della query "Select * from squadra"
	 * 
	 */
	public static ResultSet getSquadre() {
		dbOpenConnection(host, db, user, pass);
		try {
			myStatement = myConnection.createStatement();
			return myResultSet = myStatement
					.executeQuery("SELECT * FROM squadra");
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			return myResultSet = null;
		}
		
	}

	
	/**
	 * Metodo di supporto per il calcolo del costo monetario. Il metodo estrae dal DB i valori
	 * riguardanti il costo orario delle risorse componeti una squadra
	 * @param idSquadra id della squadra
	 * @return double con il costo orario totale della squadra
	 */
	public static float getCostoOrarioSquadra(String idSquadra){
		float costoOra=0;
		dbOpenConnection(host, db, user, pass);
		try{
			myStatement = myConnection.createStatement();
			myResultSet = myStatement.executeQuery("Select costoOra, idOperatore " +
					"From risorsaoperativa where idSquadra="+idSquadra+";");
			while(myResultSet.next()){
				costoOra += myResultSet.getFloat("costoOra");
			}
			return costoOra;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			return -1;
		}
		finally{
			dbCloseConnection();
		}
	}
	
	/**
	 * Metodo per determinare tutte le competenze di una squadra
	 * @param id idSquadra 
	 * @return Resultset contenete una tabella con gli id delle competenze della squadra
	 */
	public static ResultSet getCopetenzethatSquadra(String id){
		dbOpenConnection(host, db, user, pass);
		try {
			myStatement = myConnection.createStatement();
			return myResultSet = myStatement
					.executeQuery("SELECT DISTINCT capacitaoperative.IdCompetenza"
							+ " FROM squadra JOIN risorsaoperativa ON "
							+ "squadra.idSquadra = risorsaoperativa.idSquadra JOIN "
							+ "capacitaoperative ON risorsaoperativa.idOperatore = "
							+ "capacitaoperative.IdOperatore "
							+ "WHERE squadra.idSquadra = "+id+";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			return myResultSet = null;
		}
	}
	
	/**
	 * Metodo di supporto per l'estrazione del tipo di alimentazione delle vetture delle squadre
	 * ed il consumo per 100km 
	 * @return ResultSet della query contentente una tabella con idSqaudra idCaratteristica alimentazione e consumo
	 */
	public static ResultSet initAutomezzoSquadre(){
		dbOpenConnection(host, db, user, pass);
		try{
			myStatement = myConnection.createStatement();
			
			return myResultSet = myStatement.executeQuery("" +
					"SELECT squadra.idSquadra, risorsaautomezzo.caratteristicheTecniche, " +
					"caratteristichetecniche.alimentazione, caratteristichetecniche.consumo, " +
					"caratteristichetecniche.costiProporzionali " +
					"from squadra join risorsaautomezzo on squadra.idAutomezzo = " +
					"risorsaautomezzo.idAutomezzo join caratteristichetecniche on " +
					"risorsaautomezzo.caratteristichetecniche = caratteristichetecniche.idCaratteristica;");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			return myResultSet = null;
		}
	}
	
	/**
	 * Metodo di supporto per l'estrazione dal DB del costo della benzina dei costi del carburante
	 */
	public static float getCostoBenzinaCarburanti(){
		dbOpenConnection(host, db, user, pass);
		float result = -1;
		try{
			myStatement = myConnection.createStatement();
			myResultSet = myStatement.executeQuery("Select costoBenzina From datiglobali;");
			
			myResultSet.next();
			
			result = myResultSet.getFloat("costoBenzina");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		finally{
			dbCloseConnection();	
		}
		
		return result;
	}
	
	/**
	 * Metodo di supporto per l'estrazione dal DB del costo della benzina dei costi del carburante
	 */
	public static float getCostoDieselCarburanti(){
		dbOpenConnection(host, db, user, pass);
		float result = -1;
		try{
			myStatement = myConnection.createStatement();
			myResultSet = myStatement.executeQuery("Select costoDiesel From datiglobali;");
			
			myResultSet.next();
			
			result = myResultSet.getFloat("costoDiesel");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		finally{
			dbCloseConnection();	
		}
		
		return result;
	}
	
	
	/**
	 * Metdo di supporto per la creazione di una mappa contenete i valodi di distanze e tempi 
	 */
	public static ArrayList<Map<String, String>> getMapSpostamenti(){
		
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		dbOpenConnection(host, db, user, pass);
		try{
			myStatement = myConnection.createStatement();
			myResultSet = myStatement.executeQuery("Select * From distanzeimpianti; ");
			ResultSetMetaData myMeta = myResultSet.getMetaData();
			
			while(myResultSet.next()){
				HashMap<String, String> row = new HashMap<String, String>();
				row.put(myMeta.getColumnName(1), myResultSet.getString(1));
				row.put(myMeta.getColumnName(2), myResultSet.getString(2));
				row.put(myMeta.getColumnName(3), myResultSet.getString(3));
				row.put(myMeta.getColumnName(4), myResultSet.getString(4));
				
				result.add(row);
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		finally{
			dbCloseConnection();
		}
		return result;
	}

	/**
	 * Metodo per l'estrazione delle informazioni sugli interventi dal database
	 * 
	 * @return ResultSet della query "Select * from intervento"
	 * 
	 */
	public static ResultSet getInterventi() {
		dbOpenConnection(host, db, user, pass);
		try {
			myStatement = myConnection.createStatement();
			return myResultSet = myStatement
					.executeQuery("SELECT * FROM intervento");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			return myResultSet = null;
		}
	}
	
	/**
	 * Metodo di supporto per l'estrazione delle informazioni dei singoli interventi
	 * in particolare l'id dell'impinato in cui si trova l'intervento e la durata dell'intervento
	 * 
	 * @return ResultSet della query che genera una tabella con idIntervento, durata e idImpianto
	 */
	public static ResultSet getInfoInterventi(){
		dbOpenConnection(host, db, user, pass);
		try{
			myStatement = myConnection.createStatement();
			return myResultSet = myStatement.executeQuery("SELECT intervento.idIntervento, " +
					"intervento.durata, intervento.idImpianto " +
					"FROM intervento join impianto on intervento.idImpianto = impianto.idImpianto " +
					"order by idIntervento asc;");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			return myResultSet = null;
		}
	}
	
}
