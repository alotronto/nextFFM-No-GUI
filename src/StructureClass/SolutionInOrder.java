package StructureClass;
import java.util.ArrayList;
import java.util.Collections;




public class SolutionInOrder {
	private int numberS;
	private ArrayList<String> listaInterventiString;
	private ArrayList<ArrayList<Intervento>> listaInterventi;
	private ArrayList<Float> listaCosto;
	private ArrayList<Float> listaTempo;
	private ArrayList<Float> listaDistanza;
	
	private float costoTotale;
	
	
	/**
	 * Costruttore
	 */
	public SolutionInOrder(int nSquadre) {
		this.numberS = nSquadre;
		listaInterventiString = new ArrayList<String>();
		listaInterventi  = new ArrayList<ArrayList<Intervento>>();
		listaCosto = new ArrayList<Float>();
		listaTempo = new ArrayList<Float>();
		listaDistanza = new ArrayList<Float>();
		
		costoTotale = 0;
		// TODO Auto-generated constructor stub
		for(int i=0; i< nSquadre; i++){
			listaInterventiString.add(new String(""));
			listaInterventi.add(new ArrayList<Intervento>());
			listaCosto.add(0F);
			listaTempo.add(0F);
			listaDistanza.add(0F);
		}
		
	}
	
	/**
	 * 
	 * @param solution
	 */
	public SolutionInOrder(SolutionInOrder solution){
		listaInterventiString = new ArrayList<String>();
		listaInterventi  = new ArrayList<ArrayList<Intervento>>();
		listaCosto = new ArrayList<Float>();
		listaTempo = new ArrayList<Float>();
		listaDistanza = new ArrayList<Float>();
		
		// TODO Auto-generated constructor stub
		setListaInterventi(solution.getListaInterventi());
	}
	/**
	 * Metodo per il set della lista di interventi
	 * viene memorizzata la lista in ordine degli id degli interventi
	 * in particolare il primo elemento dell'ArrayList corispondo alla lista della prima squadra
	 * e cosi' via
	 * @param listaInterventi
	 */
	public void setListaInterventi(ArrayList<String> lista){
		
		//Creo una ArrayList<String> degli interventi
		listaInterventiString.clear();
		listaInterventi.clear();
		listaTempo.clear();
		listaCosto.clear();
		listaDistanza.clear();
		for(int i =0; i<lista.size();i++)
			listaInterventiString.add(new String(lista.get(i)));
		
		//Creo un ArrayList<ArrayList<Intervento>> degli interventi
		for(int i=0;i<listaInterventiString.size();i++){
			String listaIntcomodo = listaInterventiString.get(i);
			listaInterventi.add(new ArrayList<Intervento>());
			int dim = listaIntcomodo.length();
			if(!listaIntcomodo.equals(" ")){
			for(int j=0;j<dim; j++){
				int indiceInt = Integer.valueOf(listaIntcomodo.substring(j, j+1));
				if(indiceInt!=0){
					if((listaIntcomodo.length()-j)>1){
						if(indiceInt==1 && Integer.valueOf(listaIntcomodo.substring(j+1, j+2))==0){
							listaInterventi.get(i).add(DataStructure.Utility.interventi.get(9));
						}
						else{
							listaInterventi.get(i).add(DataStructure.Utility.interventi.get(indiceInt-1));
						}
					}
					else{
						listaInterventi.get(i).add(DataStructure.Utility.interventi.get(indiceInt-1));
					}
				}
			}
			}
		}
		//Calcolo la lista dei costi,tempi e durata
		for(int i=0;i<listaInterventi.size();i++){
			int listaInt[]=new int[listaInterventi.get(i).size()];
			for(int j=0;j<listaInterventi.get(i).size();j++){
				int idInt = Integer.valueOf(listaInterventi.get(i).get(j).getId());
				listaInt[j]=idInt;
			}
			listaCosto.add(DataStructure.Utility.costoIntervetiPerSquadra(listaInt, String.valueOf(i)));
			listaTempo.add(DataStructure.Utility.tempoInterventiPerSquadra(listaInt, String.valueOf(i)));
			listaDistanza.add(DataStructure.Utility.distanzaInterventiPerSquadra(listaInt, String.valueOf(i)));
		}
		//Ricalcolo il costo totale della soluzione
		costoTotale=0;
		for(int i=0;i<listaCosto.size();i++){
			costoTotale+=listaCosto.get(i);
		}
		
	}
	
	/**
	 * Metodo per la cancellazione di tutti i dati e portarlo allo stato subito
	 * dopo la dichiarazione
	 */
	public void clear(){
		listaInterventiString.clear();
		listaInterventi.clear();
		listaCosto.clear();
		listaTempo.clear();
		listaDistanza.clear();
		costoTotale = 0;
		
		for(int i=0; i< numberS; i++){
			listaInterventiString.add(new String(""));
			listaInterventi.add(new ArrayList<Intervento>());
			listaCosto.add(0F);
			listaTempo.add(0F);
			listaDistanza.add(0F);
		}
	}
	/**
	 * Metodo per la rimozione dell'ultimo intervento dalla lista di interventi
	 * di una squadra specificata
	 * @param indiceSquadra  indice della squalla alla quale si vuole rimuovere un 
	 * l'ultimo intervento
	 * @return id dell'intervento rimosso dalla lista della squadra
	 */
	public int removeLastIntervento(int indiceSquadra){
		listaTempo.clear();
		listaCosto.clear();
		listaDistanza.clear();
		listaInterventiString.clear();
		int numInterventi = listaInterventi.get(indiceSquadra).size();
		int idIntervento = Integer.valueOf(listaInterventi.get(indiceSquadra).get(numInterventi-1).getId());
		
		//Rimuovo l'intervento dalla lista di interventi
		listaInterventi.get(indiceSquadra).remove(numInterventi-1);
		
		//Ricalcolo le liste Costo Tempo e Distanza
		for(int i=0;i<listaInterventi.size();i++){
			int listaInt[]=new int[listaInterventi.get(i).size()];
			for(int j=0;j<listaInterventi.get(i).size();j++){
				int idInt = Integer.valueOf(listaInterventi.get(i).get(j).getId());
				listaInt[j]=idInt;
			}
			listaCosto.add(DataStructure.Utility.costoIntervetiPerSquadra(listaInt, String.valueOf(i)));
			listaTempo.add(DataStructure.Utility.tempoInterventiPerSquadra(listaInt, String.valueOf(i)));
			listaDistanza.add(DataStructure.Utility.distanzaInterventiPerSquadra(listaInt, String.valueOf(i)));
		}
		
		//Ricalcolo il costo totale della soluzione
		costoTotale=0;
		for(int i=0;i<listaCosto.size();i++){
			costoTotale+=listaCosto.get(i);
		}
		
		//Ridefinisco la listaInterventiString
		for(int i=0;i<listaInterventi.size();i++){
			String comodo= new String("");
			for(int j=0;j<listaInterventi.get(i).size();j++){
				comodo+=listaInterventi.get(i).get(j).getId();
			}
			listaInterventiString.add(comodo);
		}
		
		//restituisco l'id dell'intervento eliminato
		return idIntervento;
	}
	
	/**
	 * metodo per l'aggiunta di un intervento in coda alla lista di interventi di una squadra specificata
	 * @param idIntervento ID dell'intervento da aggiungere
	 * @param indiceSquadra indice della quadra alla quale aggiungere l'intervento
	 */
	
	public void addIntervento(int idIntervento, int indiceSquadra){
		listaTempo.clear();
		listaCosto.clear();
		listaDistanza.clear();
		listaInterventiString.clear();
		
		listaInterventi.get(indiceSquadra).add(DataStructure.Utility.interventi.get(idIntervento-1));
		
		//Ricalcolo le liste Costo Tempo e Distanza
		for(int i=0;i<listaInterventi.size();i++){
			int listaInt[]=new int[listaInterventi.get(i).size()];
			for(int j=0;j<listaInterventi.get(i).size();j++){
				int idInt = Integer.valueOf(listaInterventi.get(i).get(j).getId());
				listaInt[j]=idInt;
			}
			listaCosto.add(DataStructure.Utility.costoIntervetiPerSquadra(listaInt, String.valueOf(i)));
			listaTempo.add(DataStructure.Utility.tempoInterventiPerSquadra(listaInt, String.valueOf(i)));
			listaDistanza.add(DataStructure.Utility.distanzaInterventiPerSquadra(listaInt, String.valueOf(i)));
		}
				
		//Ricalcolo il costo totale della soluzione
		costoTotale=0;
		for(int i=0;i<listaCosto.size();i++){
			costoTotale+=listaCosto.get(i);
		}
		
		//Ridefinisco la listaInterventiString
		for(int i=0;i<listaInterventi.size();i++){
			String comodo= new String("");
			for(int j=0;j<listaInterventi.get(i).size();j++){
				comodo+=listaInterventi.get(i).get(j).getId();
			}
			listaInterventiString.add(comodo);
		}
				
	}
	
	
	/**
	 * Metodo get per ottenere la lista dei Costi
	 * @return
	 */
	public ArrayList<Float> getListaCosto(){
		ArrayList<Float> result  = new ArrayList<Float>();
		result = listaCosto;
		return result;
	}
	
	/**
	 * Metodo get per ottenere la lista dei tempi
	 * @return
	 */
	public ArrayList<Float> getListaTempo(){
		ArrayList<Float> result  = new ArrayList<Float>();
		result = listaTempo;
		return result;
	}
	/**
	 * Metodo get per ottenere la lista delle distanze
	 * @return
	 */
	public ArrayList<Float> getListaDistanza(){
		ArrayList<Float> result  = new ArrayList<Float>();
		result = listaDistanza;
		return result;
	}
	
	/**
	 * Metodo per il set del costo totale della soluzione
	 * @param costo
	 */
	public void setCostoTotale(float costo){
		this.costoTotale = costo;
	}
	
	/**
	 * Metodo per il get del costo TOTALE
	 * @return Costo TOTALE della soluzione
	 */
	public float getCosto(){
		return costoTotale;
	}
	
	/**
	 * Metodo get per ottenere la lista degli interventi nel formato ArrayList<Intervento>
	 * @param idSquadra
	 * @return
	 */
	public ArrayList<Intervento> getListaIntIntervento(int idSquadra){
		ArrayList<Intervento> result = new ArrayList<Intervento>();
		result = listaInterventi.get(idSquadra);
		return result;
	}
	
	/**
	 * Metdo per il get della lista di Interventi in formato String
	 * @return lista interventi ordinata della soluzione
	 */
	/*public ArrayList<String> getListaInterventiString(){
		return listaInterventiString;
	}
	*/
	public ArrayList<String> getListaInterventi(){
		ArrayList<String> listaInt = new ArrayList<String>();
		
		for(int i=0;i<listaInterventi.size();i++){
			String comodo = new String("");
			for(int j=0;j<listaInterventi.get(i).size();j++){
				comodo+=(listaInterventi.get(i).get(j).getId());
			}
			listaInt.add(comodo);
		}
		return listaInt;
	}
	
	/**
	 * 
	 * @param element1
	 * @param element2
	 */
	public void swapIntSameSquad(int idSquadra,int element1,int element2){
		
		
		if(listaInterventi.get(idSquadra).size()>1){
			listaTempo.clear();
			listaCosto.clear();
			listaDistanza.clear();
			listaInterventiString.clear();
			
			Collections.swap(listaInterventi.get(idSquadra), element1, element2);
			
			//Ricalcolo le liste Costo Tempo e Distanza
			for(int i=0;i<listaInterventi.size();i++){
				int listaInt[]=new int[listaInterventi.get(i).size()];
				for(int j=0;j<listaInterventi.get(i).size();j++){
					int idInt = Integer.valueOf(listaInterventi.get(i).get(j).getId());
					listaInt[j]=idInt;
				}
				listaCosto.add(DataStructure.Utility.costoIntervetiPerSquadra(listaInt, String.valueOf(i)));
				listaTempo.add(DataStructure.Utility.tempoInterventiPerSquadra(listaInt, String.valueOf(i)));
				listaDistanza.add(DataStructure.Utility.distanzaInterventiPerSquadra(listaInt, String.valueOf(i)));
			}
							
			//Ricalcolo il costo totale della soluzione
			costoTotale=0;
			for(int i=0;i<listaCosto.size();i++){
				costoTotale+=listaCosto.get(i);
			}
					
			//Ridefinisco la listaInterventiString
			for(int i=0;i<listaInterventi.size();i++){
				String comodo= new String("");
				for(int j=0;j<listaInterventi.get(i).size();j++){
					comodo+=listaInterventi.get(i).get(j).getId();
				}
				listaInterventiString.add(comodo);
			}
		}
		else{
			System.err.println("Impossibile effettuare lo swap per la squadra selezionata");
		}
		
	}
	
	public void swapIntFromTwoSquad(int idSquqadra1, int idSquadra2, int idIntervento){
		//Estraggo l'intervento da rimuovere dalla squadra1
		Intervento intToSwap = DataStructure.Utility.interventi.get(idIntervento-1);
		//Rimuovo l'intervento dalla lista di intervento della squadra1
		listaInterventi.get(idSquqadra1).remove(intToSwap);
		//In modo random decido in che posizione inserire l'intervento della squadra2
		int positionToInsert;
		if(listaInterventi.get(idSquadra2).size()==0){
			positionToInsert=0;
		}
		else{
			positionToInsert = DataStructure.Utility.sRandom.nextInt(
					listaInterventi.get(idSquadra2).size()
					);
		}
		//Intersico l'intervenot all'interno della lista di interveti della squadra2
		listaInterventi.get(idSquadra2).add(positionToInsert, intToSwap);
		
		//Aggiorno le strutture dell'oggetto
		listaTempo.clear();
		listaCosto.clear();
		listaDistanza.clear();
		listaInterventiString.clear();
		
		//Ricalcolo le liste Costo Tempo e Distanza
		for(int i=0;i<listaInterventi.size();i++){
			int listaInt[]=new int[listaInterventi.get(i).size()];
			for(int j=0;j<listaInterventi.get(i).size();j++){
				int idInt = Integer.valueOf(listaInterventi.get(i).get(j).getId());
				listaInt[j]=idInt;
			}
			listaCosto.add(DataStructure.Utility.costoIntervetiPerSquadra(listaInt, String.valueOf(i)));
			listaTempo.add(DataStructure.Utility.tempoInterventiPerSquadra(listaInt, String.valueOf(i)));
			listaDistanza.add(DataStructure.Utility.distanzaInterventiPerSquadra(listaInt, String.valueOf(i)));
		}
						
		//Ricalcolo il costo totale della soluzione
		costoTotale=0;
		for(int i=0;i<listaCosto.size();i++){
			costoTotale+=listaCosto.get(i);
		}
				
		//Ridefinisco la listaInterventiString
		for(int i=0;i<listaInterventi.size();i++){
			String comodo= new String("");
			for(int j=0;j<listaInterventi.get(i).size();j++){
				comodo+=listaInterventi.get(i).get(j).getId();
			}
			listaInterventiString.add(comodo);
		}
		
		
		
	}
	/**
	 * Ridefinizione del metodo toString()
	 */
	public String toString(){
		String result = new String();
		result = "Lista interventi:";
		for(int i=0;i<listaInterventiString.size();i++){
			result+="squadra "+(i+1)+":"+listaInterventiString.get(i)+":";
		}
		result+="Costo TOTALE:"+costoTotale;
		return result;
	}
	
}
