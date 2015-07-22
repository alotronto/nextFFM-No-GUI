import SimulatedAnnealingUtility.SimAnne;

public class StartAppl {

	public static void main(String[] args) {
		
		//Controllo il numero di argomenti
		if (args.length != 8) {
			System.out.println("numero o tipo parapetri passati non valido"); 
		}
		else{
			DataBaseUtility.Utility.setHost(String.valueOf(args[0]));
			DataBaseUtility.Utility.setDB(String.valueOf(args[1]));
			DataBaseUtility.Utility.setUser(String.valueOf(args[2]));
			DataBaseUtility.Utility.setPass(String.valueOf(args[3]));
		 
		  
			// DataStructure.Utility.initCostoCarguranti();
			// DataStructure.Utility.initMapSpostamenti();
			// DataStructure.Utility.initSquadre();
			// DataStructure.Utility.initInterventi(); //
			// DataStructure.Utility.createInitialSolution(); // Creazione di una
			// soluzione iniziale casuale ma con una distribuzione // uniforma tra i
			// teams DataStructure.Utility.createInitialSolutionUniform();
			//
			//DataBaseUtility.Utility.setHost("localhost");
			//DataBaseUtility.Utility.setDB("nextffm20130926");
			//DataBaseUtility.Utility.setUser("root");
			//DataBaseUtility.Utility.setPass("28bh0608");

			DataStructure.Utility.initCostoCarguranti();
			DataStructure.Utility.initMapSpostamenti();
			DataStructure.Utility.initSquadre();
			DataStructure.Utility.initInterventi();

			//DataStructure.Utility.createInitialSolutionUniform();
			DataStructure.Utility.createInitialSolution();
			
			//DEBUG
			//Controllo della soluzione iniziale
			
			SimAnne sa = new SimAnne(Integer.valueOf(args[4]), Integer.valueOf(args[5]),
						Float.valueOf(args[6]), Float.valueOf(args[7]));
				for (int z = 0; z < 50; z++) {
					//Debug Message
					System.out.println("Test: "+z);
					
					sa.findBestSolution();
					// ############# Salvataggio del risultato su file
					DataStructure.Utility.openFile("result_"
							+ sa.getnIteration() + "_" + sa.getnValuate() + "_"
							+ sa.getCooling() + "_" + sa.getAlfa() + ".txt");
					DataStructure.Utility
							.printToFile(DataStructure.Utility.initialSolution
									.getCosto() + "\n");
					DataStructure.Utility.closefile();
					
					DataStructure.Utility.openFile("result_full"
							+ sa.getnIteration() + "_" + sa.getnValuate() + "_"
							+ sa.getCooling() + "_" + sa.getAlfa() + ".txt");
					DataStructure.Utility
							.printToFile(DataStructure.Utility.initialSolution
									.toString() + "\n");
					DataStructure.Utility.closefile();
					
					//DataStructure.Utility.createInitialSolutionUniform();
					DataStructure.Utility.createInitialSolution();
				}
			
		

	}
	}
}
// ####################################################

// DataStructure.Utility.distribuzionePercentuale(sa.getnIteration()+ "_" +
// sa.getnValuate() + "_" + sa.getCooling() + "_"+ sa.getAlfa());

// DataStructure.Utility.createMediaIterTime(sa.getnIteration(),sa.getnValuate(),
// sa.getCooling(), sa.getAlfa());

