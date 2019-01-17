import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;


public class Partie {
	public static Scanner scan = new Scanner(System.in); 
	public static ArrayList<Domino> dominosPioche =new ArrayList<>();
	public static ArrayList<Joueur> listeJoueurs=new ArrayList<>();
	public static ArrayList<Domino> dominosAChoisir= new ArrayList<>();
	public static ArrayList<Domino> dominosAjouer = new ArrayList<Domino>();
	public static ArrayList<Roi> listeRois = new ArrayList<Roi>();
	
	Random rand = new Random();

	public Partie() throws UnsupportedEncodingException {
		
		//import des dominos depuis le fichier csv
		importDominos();
		
		//Nbre de joueur choisi via l'IHM
		int nbJoueur = GuiInitJeu.choisirNbreJoueurs();
		
		//Nbre d'IA choisi via l'IHM
		Map<GameColor, Boolean> dicoCouleurIsIA = GuiInitJeu.choisirIfIA(nbJoueur);
		
		for (GameColor couleur : dicoCouleurIsIA.keySet()) {
			boolean isIA = dicoCouleurIsIA.get(couleur);
			listeJoueurs.add(new Joueur(couleur, isIA));
		}
		
		
		//Attribution des rois
		
		//s'il y a 2 joueurs => 2 rois par joueur
		if(nbJoueur == 2) {
			removeDominosFromPioche(24);
			for(int i = 0; i<nbJoueur ; i++) {
				listeJoueurs.get(i).addRoi();
				listeJoueurs.get(i).addRoi();
			}
		//sinon => 1 roi par joueur
		} else {
			if (nbJoueur == 3)
				removeDominosFromPioche(12);
			for(int i = 0; i<nbJoueur ; i++) {
				listeJoueurs.get(i).addRoi();
			}
		}
		
		for (Joueur joueur : listeJoueurs) {
			listeRois.addAll(joueur.getListeRois());
		}
		
		
		
		premierTour();
		
		
		boolean royaumesAllFull = false;
		while (!royaumesAllFull) {
			
			royaumesAllFull = true;
			//vérifie si au moins un des royaume n'est pas plein => il peut encore jouer => nouveau tour
			for (Roi roi : listeRois) {
				if (!getJoueurOfRoi(roi).getRoyaume().isFull()) {
					royaumesAllFull = false;
					break;
				}
			}
			
			nouveauTour();
			
			if (dominosAChoisir.isEmpty()) {
				break;
			}
			
		}
		GuiBoard.refreshBoard();
		
		//Calcul du score de chaque joueur
		for (Joueur joueur : listeJoueurs) {
			int score = joueur.getRoyaume().calculerScore();
			joueur.setScore(score);
		}
		
		//On ordonne les joueurs selon leur score (dans l'ordre décroissant)
		listeJoueurs.sort(Comparator.comparing(Joueur::getScore).reversed());
		
		GuiBoard.afficherScore(listeJoueurs);
		
	}
	
	private void nouveauTour() {

		dominosAjouer.addAll(dominosAChoisir);
		dominosAChoisir.clear();
		
		if (dominosPioche.size() >= listeRois.size()) {
			tirerNouveauxDominos();	
		}
		
		int i = dominosAjouer.size() - 1;
		while(i >= 0) {
			Domino dominoAJouer = dominosAjouer.get(0);
			
			Roi roi = dominoAJouer.getRoi();
			
			//le joueur choisi un domino pour le prochain tour
			if (!dominosAChoisir.isEmpty()) {
				choisirDomino(roi);
			}
			//on retire le roi du domino à joueur car il a été placé sur le domino choisi
			dominoAJouer.removeRoi();	
			
			GuiBoard.refreshBoard();
			
			//le joueur choisit ce qu'il fait du domino choisi au tour précédent;
			if (getJoueurOfRoi(roi).getRoyaume().isFull()) {
				//ne fait rien => le domino ne sera pas placé
			} else {
				demandeOuPlacerDomino(dominoAJouer, roi);
			}
			dominosAjouer.remove(dominoAJouer);
			i -= 1;
		}
	}
	
	
	
	private void tirerNouveauxDominos() {
		for (int i = 0; i < listeRois.size(); i++) {
			int randomIndex = rand.nextInt(dominosPioche.size());
			Domino domino = dominosPioche.get(randomIndex);
			dominosPioche.remove(domino);
			dominosAChoisir.add(domino);
		}
		
		//Permet d'ordonner les dominos selon leur nombre
		dominosAChoisir.sort(Comparator.comparing(Domino::getNumero));
	}
	
	private void premierTour() {
		tirerNouveauxDominos();
		
		ArrayList<Roi> tempListeRois = new ArrayList<>();
		tempListeRois.addAll(listeRois);
		//L'ordre de jeu au premier tour est aléatoire
		for(int i =  0; i < listeRois.size(); i++) {
			int randomIndex = rand.nextInt(tempListeRois.size());
			Roi roi = tempListeRois.get(randomIndex);
			GuiBoard.refreshBoard();
			choisirDomino(roi);
			tempListeRois.remove(roi);
		}
	}

	private void choisirDomino(Roi roi) {
		
		/*
		 * Dans cette méthode, tous les indices affichés ont un "+1"
		 * pour compter à partir de 1 pour l'utilisateur et pas à partir de 0
		 * Inversement les indices entrés par l'utilisateur sont décrementés de 1	
		 */
		
		int rangDomino;
		Joueur joueur = getJoueurOfRoi(roi);
		
		if (joueur.isAI()) {
			
			
			ArrayList<Domino> dominosDispo = new ArrayList<>();
			for (Domino domino : dominosAChoisir) {
				if (!domino.isChoosed()) {
					dominosDispo.add(domino);
				}
			}
			
			Domino dominoChoisi = IA.choisirBestDomino(joueur, dominosDispo);
			rangDomino = dominosAChoisir.indexOf(dominoChoisi);
						
		} else {			
			GuiBoard.refreshBoard();
			rangDomino = GuiBoard.choisirDomino(joueur);
		}
		
		Domino dominoChoisi = dominosAChoisir.get(rangDomino);
		dominoChoisi.setRoi(roi);
	}

	private void demandeOuPlacerDomino(Domino domino, Roi roi) {
		Joueur joueur = getJoueurOfRoi(roi);
		Royaume royaume = joueur.getRoyaume();
		
		if (joueur.isAI()) {
			Move moveToDo = joueur.getMoveToDoForDomino(domino);
			
			if (moveToDo.haveToBeDeleted()) {
			} else {
				royaume.placerDomino(moveToDo);
			}
			joueur.getNextMoves().remove(moveToDo);
		} else {
			GuiBoard.choosePlaceForDomino(joueur, domino);
		}
	}

	private Joueur getJoueurOfRoi(Roi roi) {
		Joueur joueur = null;
		for (Joueur joueurCourant : listeJoueurs) {
			if (joueurCourant.getListeRois().contains(roi)) {
				joueur =  joueurCourant;
				break;
			}
		}
		
		return joueur;
	}

	private Domino getRandomFromPioche() {
		int randomIndex = rand.nextInt(dominosPioche.size());
		return dominosPioche.get(randomIndex);
	}

	private void removeDominosFromPioche(int nbrToRemove) {
		for(int i = 0; i < nbrToRemove; i++) {
			Domino dominoToRemove = getRandomFromPioche();
			dominosPioche.remove(dominoToRemove);
		}
	}

	/*
	 * Code de base copié depuis https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	 */
	private void importDominos() throws UnsupportedEncodingException {
		String csvFile = Paths.get("dominos.csv").toString();
	    String line = "";
	    String csvSplitBy = ",";
	    Map<String, TypeTerrain> map = Outils.getDicoStringToType();
	
	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
	    	
	    	//saute la première ligne qui décrit le contenu de chaque colonne
	    	br.readLine();
	
	        while ((line = br.readLine()) != null) {
	
	            // use comma as separator
	            String[] dominoString = line.split(csvSplitBy);
	            
	            int nbCouronne1 = Integer.valueOf(dominoString[0]);
	            TypeTerrain type1 = map.get(dominoString[1]);
	            int nbCouronne2 = Integer.valueOf(dominoString[2]);
	            TypeTerrain type2 = map.get(dominoString[3]);
	            int numeroDomino = Integer.valueOf(dominoString[4]);
	            
	            Case caseRef = new Case(type1, nbCouronne1);
	            Case caseRot = new Case(type2, nbCouronne2);
	            
	            Domino domino = new Domino(caseRef, caseRot, numeroDomino);
	            dominosPioche.add(domino);
	
	        }
	
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
