package SimulatedAnnealingUtility;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import DataStructure.Utility;

//import java.util.Random;



/**
 * 
 * @author Andrea Rocco Lotronto 21-06-2013
 * 
 * Classe che astrae l'algoritmo di Simulated Annealing
 *
 */
public class SimAnne {

	private float temp;
	private float tempLow;
	private int nIteration=0;
	private ArrayList<Float> vectorC;
	private int nValuate=0;
	//private double bestC;
	private float cooling=0;
	private float alfa = 0;
	
	//public  FileWriter myWriter;
	//public  BufferedWriter myBufferWriter;
	
	/**
	 * Costruttore che permette di impostare tutti i parametri della
	 * Simulated Annealing
	 * 
	 * @param nIteration numero di iterazioni per ogni valore di temperatura
	 * @param nValuate numero di soluzioni migliori da valutare
	 * @param initialSolution oggetto iniziale di soluzione
	 * @param coolingpercent percentuale di raffreddamento della temperatura
	 */
	public SimAnne(int nIteration, int nValuate, float alfa, float coolingpercent) {
		
		//Inizializzo i parametri del S.A.
		
		this.nIteration = nIteration;
		this.nValuate = nValuate;
		this.cooling = coolingpercent;
		this.alfa = alfa;
		
		//La temperatura iniziale sar� uguale al costo della soluzione iniziale(distanza totale)
		temp=DataStructure.Utility.initialSolution.getCosto();
		//La tempLow (Tlow) viene impostata inferiore del 50% della temperatura	
		//iniziale
		tempLow=setNewTemperature(cooling);
		vectorC = new ArrayList<Float>();
		//bestC=temp;
		
		
	}
	
	
	/**
	 * Costruttore che utilizza dei valori per:
	 * -nIteration : 20
	 * -nValuate: 30
	 * -coolingpercent:99%
	 * di default
	 * @param coolingpercent percentuale di raffreddamento della temperatura
	 */
	public SimAnne(){
		//Utilizzo i parametri di default del S.A.
		
		//La temperatura iniziale sar� uguale al costo della soluzione iniziale(distanza totale)
		temp=DataStructure.Utility.initialSolution.getCosto();
		//La tempLow (Tlow) viene impostata inferiore del 50% della temperatura
		//iniziale
		tempLow=setNewTemperature(cooling);
		vectorC = new ArrayList<Float>();
		//bestC=temp;
	}
	
	/**
	 * Costruttore che utilizza dei valori per:
	 * -nIteration : 20
	 * -nValuate: 30
	 * di default
	 * @param coolingpercent percentuale di raffreddamento della temperatura
	 */
	public SimAnne(float coolingpercent){
		
		//Utilizzo i parametri di default del S.A. ma modifica la percentuale
		// di raffreddamento
		
		this.cooling = coolingpercent;
		
		//La temperatura iniziale sar� uguale al costo della soluzione iniziale(distanza totale)
		temp=DataStructure.Utility.initialSolution.getCosto();
		//La tempLow (Tlow) viene impostata inferiore del 50% della temperatura
		//iniziale
		tempLow=setNewTemperature(cooling);
		vectorC = new ArrayList<Float>();
		//bestC=temp;
	}
	
	/**
	 * Metodo che astrare il comportamento del Simulated Annealing nella ricerca
	 * della soluzione ottimale
	 */

	public void findBestSolution() {
		long startTime = System.nanoTime();
		temp=DataStructure.Utility.initialSolution.getCosto();
		tempLow = setNewTemperature(cooling);
		vectorC.clear();
		//bestC = temp;
		int count = 0;// Variabile per testare il numero di cicli wile fatti
		while ( (!checkStopCriteria()) && (temp > tempLow)) {
		//while ( (!checkStopCriteriaIntorno()) && (temp > tempLow)) {
			int i = 0;
			count++;
			// System.out.println("Distanza Attuale::"+currentSolution.getDistance());
			while (i < nIteration) {

				// Creazione di una nuova Soluzione Vicina al problema di
				// partenza
				// effettuo in modo casulare o una modifica sulla stessa squadra
				// oppure una modifica su squadre differenti
				int r = DataStructure.Utility.sRandom.nextInt(2);
				if (r == 0) {
					DataStructure.Utility.createNearSolution(1, 0);
				}
				else {
					DataStructure.Utility.createNearSolution(0, 1);
				}
				// Calcolo dei costi (distanza in questo caso)
				// double currentEngery = currentSolution.getDistance();
				float currentEngery = DataStructure.Utility.initialSolution.getCosto();
				float neighbourEngery = DataStructure.Utility.valuateSolution.getCosto();

				// Comparazione del costo della nuova soluzione con quello della
				// vecchia
				// ed accettazione della nuova soluzione sulla base della
				// funziore di accettazione
				if (accettazione(neighbourEngery, currentEngery)) {
					DataStructure.Utility.acceptNewSolution();
				} else {
					DataStructure.Utility.restoreToInitialSolution();
				}
				i++;
			}
			// Controllo sugli ultimi risultati
			// bestC <- ultimo valore trovato;
			vectorC.add(DataStructure.Utility.initialSolution.getCosto());
			// Modifico la temperatura (sistema di cooling) sulla base dell'alfa
			temp = temp - (alfa * temp);
		}

		
		//Memorizzo il tempo di esecuzione del SA
		long estimatedTime = System.nanoTime() - startTime;
		int iteration = count * nIteration;
		
		Utility.openFile("iteration_time" + getnIteration() + "_"
				+ getnValuate() + "_" + getCooling() + "_" + getAlfa() + ".txt");
		Utility.printToFile(iteration+ "\t"+
				TimeUnit.MILLISECONDS.convert(estimatedTime,TimeUnit.NANOSECONDS) + "\n");
		Utility.closefile();
		
		

		/*Utility.openFile("count_check_intorno" + getnIteration() + "_"
						+ getnValuate() + "_" + getCooling() + "_" + getAlfa()
						+ ".txt");
		if (checkStopCriteriaIntorno()) 
			Utility.printToFile("1\n");
		
		Utility.closefile();
		*/
	}
	/**
	 * Metodo per l'accettazione della nuova soluzione sulla base della comparazione del costo
	 * Date due soluzioni e i rispettivi costi si determina se accettare o meno la soluzione
	 * generata o meno sulla base della seguente formula:
	 * Siano newCost e actualCost i rispettivi costi della nuova soluzione e della soluzione attuale;
	 * sia delta=newCost-actualCost la differnza fra i costi,
	 * se delta <=0 la nuova soluzione viene accetta come migliore
	 * se delta > 0 la nuova soluzione sar� accettata se e^(-delta/temp) < r dove: 
	 * - r � un numero random tra 0 e 1  0 <= r <= 1;
	 * - -delta � uguale a actualCost-newCost;
	 * - temp � la temperatura attuale;
	 * 
	 * @param newCost costo della nuova soluzione
	 * @param actulaCost costo della soluzione attuale
	 * @return true se si accetta la nuova soluzione, false se si deve scartare la nuova soluzione
	 */
	private boolean accettazione(float newCost, float actulaCost){
		float delta = newCost - actulaCost; 
		//double r= new Random().nextDouble();
		double r = DataStructure.Utility.sRandom.nextDouble();
		double prob = Math.exp((actulaCost-newCost)/temp);
		
		
		if(delta<=0){
			//printToFile("(C'- C)<=0::"+delta+"Accetto la soluzione C' perch� migliore\n");
			return true;
		}
		else{
			if(prob > r){
				//printToFile("e^(C - C')/T::e^("+actulaCost+"-"+newCost+")/"+temp+"::"+prob+"\n");
				//printToFile("ACCETTO LA SOLUZIONE PEGGIORATIVA::"+prob+">"+r+"\n");
				return true;
			}
			else{
				//printToFile("e^(C - C')/T::e^("+actulaCost+"-"+newCost+")/"+temp+"::"+prob+"\n");
				//printToFile("NON ACCETTO NE LA NUOVA SOLUZIONE NE LA PEGGIORATIVA"+prob+">"+r+"\n");
				return false;
			}
		}
	}
	
	/**
	 * Metodo per l'analisi degli n precedenti valori di soluzione trovati,
	 * contralla che vi siano state variazioni nelle n soluzioni trovate
	 *  
	 * @return true se si deve sospendere
	 */
	private boolean checkStopCriteria(){
		cleanVectorC();//elimino elementi storici troppo vecchi dal vettore delle soluzioni passate
		//printToFile("\n Vettore dei d valori di C::"+vectorC.toString()+"\n");
		if(vectorC.size()==nValuate){
			float firstSolutionStored=vectorC.get(0);
			for(int i=1; i<vectorC.size();i++){
				if(firstSolutionStored!=vectorC.get(i))
					return false;
			}
			//DataStructure.Utility.printToFile("\nUSCITO PER IL CHECK_STOP!!!\n");
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Metodo per l'analisi degli n precedenti valori di soluzione trovati,
	 * contralla che vi siano state variazioni nelle n soluzioni trovate
	 *  
	 * @return true se si deve sospendere
	 */
	/*
	private boolean checkStopCriteriaIntorno(){
		double teta = 10;
		cleanVectorC();//elimino elementi storici troppo vecchi dal vettore delle soluzioni passate
		//printToFile("\n Vettore dei d valori di C::"+vectorC.toString()+"\n");
		if(vectorC.size()==nValuate){
			double firstSolutionStored=vectorC.get(0);
			for(int i=1; i<vectorC.size();i++){
				//Cotrntollo che i valori dell'array siano al di fuori dell'intorno
				//cotruito sul primo valore del vettore 
				if( (vectorC.get(i)>firstSolutionStored+teta) || vectorC.get(i)< firstSolutionStored-teta){
					return false;
				}
					
			}
			//DataStructure.Utility.printToFile("\nUSCITO PER IL CHECK_STOP!!!\n");
			return true;
		}
		else
			return false;
	}
	*/
	/**
	 * Il metodo permette di mantenere nell'array delle soluzioni solo le ultime n soluzioni, dove:
	 * n � stato impostato o tramite costruttore o tramite valore di default;
	 * numero di valori storici di costo da valutare
	 */
	private void cleanVectorC(){
		if(vectorC.size()>nValuate){
			int n = vectorC.size() - nValuate;
			for(int i=0; i<n; i++){
				vectorC.remove(0);
			}
		}
	}
	
	//------------------Metodi di supporto per il debugging------------------
	private float setNewTemperature(float percetuale){
		return (temp - ((temp/100) * percetuale));
	}
	
	public float getTemp(){
		return this.temp;
	}
	public float getTempLow(){
		return this.tempLow;
	}
	public int getnIteration(){
		return this.nIteration;
	}
	public int getnValuate(){
		return this.nValuate;
	}
	public float getCooling(){
		return this.cooling;
	}
	public float getAlfa(){
		return this.alfa;
	}

}
