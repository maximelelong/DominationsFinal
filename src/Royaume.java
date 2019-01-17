import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Royaume {
	static int largeurGrille = 9;
	static int hauteurGrille = 9;
	static int largeurMax = 5;
	static int hauteurMax = 5;
	
	Case[][] listeCases = new Case[largeurGrille][hauteurGrille];
	ArrayList<ArrayList<Case>> listeZones = new ArrayList<>();

	public Royaume() {
		for(int x = 0; x < largeurGrille; x++) {
			for(int y = 0; y < hauteurGrille; y++) {
				listeCases[x][y] = new Case(x,y);
			}
		}
		
		//On ajoute le chateau au mileu du royaume
		listeCases[4][4] = new Case(TypeTerrain.CHATEAU, 0);
		
	}
	
	public boolean placerDomino(Move move) {
		Domino domino = move.getDomino();
		
		if (canPlace(move)) {
			int Xref = move.getXref();
			int Yref = move.getYref();
			int Xrot = move.getXrot();
			int Yrot = move.getYrot();
			
			listeCases[Xref][Yref] = domino.getCaseRef();
			listeCases[Xref][Yref].setX(Xref);
			listeCases[Xref][Yref].setY(Yref);
			listeCases[Xrot][Yrot] = domino.getCaseRot();
			listeCases[Xrot][Yrot].setX(Xrot);
			listeCases[Xrot][Yrot].setY(Yrot);
			return true;
		} else
			return false;
	}
	
	/**Vérifie si un domino peut être placé
	 * 
	 * @param domino
	 * @param Xref
	 * @param Yref
	 * @param Xrot
	 * @param Yrot
	 * @return true si le domino peut être placé à cet emplacement 
	 */
	public boolean canPlace(Move move) {
		Domino domino = move.getDomino();
		int Xref = move.getXref();
		int Yref = move.getYref();
		int Xrot = move.getXrot();
		int Yrot = move.getYrot();
		
		
		//On vérifie que les coordonnées voulues sont dans la grille
		if(!isInGrid(Xref, Yref) || !isInGrid(Xrot, Yrot)){
			return false;
		}
		
		//On vérifie que l'emplacement ciblé n'est pas déja occupé
		if (!listeCases[Xref][Yref].isEmpty() || !listeCases[Xrot][Yrot].isEmpty())
			return false;
		
		/*Ajoute le domino pour tester si cela dépasse la taille max du royaume
		 * Teste si c'est le cas puis supprime le domino
		 */
		listeCases[Xref][Yref] = domino.getCaseRef();
		listeCases[Xrot][Yrot] = domino.getCaseRot();
		boolean isOversized = isOversized();
		listeCases[Xref][Yref] = new Case(Xref, Yref);
		listeCases[Xrot][Yrot] = new Case(Xrot, Yrot);
		
		if(isOversized) {
			return false;
		} else {
						
			Case caseRef = domino.getCaseRef();
			Case caseRot = domino.getCaseRot();
						
			ArrayList<Case> voisinsRef = getVoisins(Xref, Yref);
			ArrayList<Case> voisinsRot = getVoisins(Xrot, Yrot);
			
			//on teste les voisins de l'emplacement de la case Ref
			for (Case caseAdj : voisinsRef) {
				if (caseAdj.getTypeTerrain().equals(caseRef.getTypeTerrain())
						||caseAdj.isCastle()) {
					return true;
				}
			}
			
			//on teste les voisins de l'emplacement de la case Rot
			for (Case caseAdj : voisinsRot) {
				if (caseAdj.getTypeTerrain().equals(caseRot.getTypeTerrain())
						||caseAdj.isCastle()) {
					return true;
				}
			}
			
			//si aucune case voisine ne correspond, le domino ne peut pas être placé
			return false;
			
		}
	}
	private boolean isOversized() {
		Map<String, Integer> limites = getLimites();
		int xMax = limites.get("xMax");
		int xMin = limites.get("xMin");
		int yMin = limites.get("yMin");
		int yMax = limites.get("yMax");
		
		int largeur = xMax - xMin + 1;
		int hauteur = yMax - yMin + 1;
		//on vérifie si le royaume ne dépasse pas 5x5
		if (largeur > largeurMax || hauteur > hauteurMax) {
			return true;
		} else
			return false;
	}
	
	/**Vérifie si le royaume est plein
	 * Renvoie true si plus aucun domino ne peut être ajouté au royaume
	 * Renvoie false sinon
	 * 
	 * @return
	 */
	public boolean isFull() {
		Map<String, Integer> limites = getLimites();
		int xMax = limites.get("xMax");
		int xMin = limites.get("xMin");
		int yMin = limites.get("yMin");
		int yMax = limites.get("yMax");
		
		int largeur = xMax - xMin + 1;
		int hauteur = yMax - yMin + 1;
		
		//si le royaume a atteint la largeur max et hauteur max
		if (largeur == largeurMax && hauteur == hauteurMax) {
			//on parcours toutes les cases	
			for(int x = xMin; x <= xMax; x++) {
				for(int y = yMin; y <= yMax; y++) {
					
					if(listeCases[x][y].isEmpty()) {
						
						for (Case caseAdjacente : getVoisins(x, y)) {
							
							if(caseAdjacente.isEmpty()) {
								
								int xAdj = caseAdjacente.getX();
								int yAdj = caseAdjacente.getY();
								//s'il possède un voisin vide qui reste dans les dimensions max
								//alors la grille n'est pas pleine car cela signifie qu'il y a
								//2 cases côtes à cotes et donc que l'on peut y mettre un domino
								if(xAdj <= xMax && xAdj >= xMin && yAdj <= yMax && yAdj >= yMin) {
									return false;
								}
							}
						}
					}
				}
			}
			return true;
		} else { //si le royaume n'a pas atteint la taille max il ne peut pas être plein
			return false;
		}
	}
	
	private Map<String, Integer> getLimites() {
		Map<String, Integer> limites = new HashMap<>();
		
		int xMin = 9;
		int xMax = 0;
		int yMin = 9;
		int yMax = 0;
		for(int x = 0; x < largeurGrille; x++) {
			for(int y = 0; y < hauteurGrille; y++) {
				if (!listeCases[x][y].isEmpty()) {
					if (x>xMax)
						xMax = x;
					if(x<xMin)
						xMin = x;
					if(y>yMax)
						yMax = y;
					if (y<yMin)
						yMin = y;
				}
			}
		}
		
		limites.put("xMin", xMin);
		limites.put("xMax", xMax);
		limites.put("yMin", yMin);
		limites.put("yMax", yMax);
		return limites;
	}
	
	public static boolean isInGrid(int x, int y) {
		if(x < 0 || x >= largeurGrille || y < 0 || y >= hauteurGrille){
			return false;
		} else 
			return true;
	}
	
	public Case getCase(int x, int y) {
		if(isInGrid(x, y)) {
			return listeCases[x][y];
		} else 
			return null;
	}
	
	//permet de visualiser le royaume sous la forme d'un String
	//Utilisé pour la version console et le debugging
	public String toString() {
		String string = "";
		string += "\n  ";
		for(int x = 0; x < largeurGrille; x++) {
			string += "   "+ x + "  ";
		}
		string += "\n";
		for(int y = 0; y < hauteurGrille; y++) {
			string += "  ";
			for(int x = 0; x < largeurGrille; x++) {
				string += " -----";
			}
			string += "\n";
			//print le type des cases de la ligne ligne	
			for(int x = 0; x < largeurGrille; x++) {
				if(x == 0)
					string += y +" |";
				string += listeCases[x][y].getTypeTerrain() + "|";
			}
			string += "\n";
			//print le nombre de couronnes des cases de la ligne
			for(int x = 0; x < largeurGrille; x++) {
				if(x == 0)
					string += "  |";
				string += listeCases[x][y].printCouronnes() + "|";
			}
			string += "\n";
			
			if (y == hauteurGrille -1 ) {
				string += "  ";
				for(int x = 0; x < largeurGrille; x++) {
					string += " -----";
				}
			}
		}
		return string;
	}
	
	private void delimitZones() {
		//on rénitialise les zones
		listeZones = new ArrayList<>();
		
		for (int x = 0; x < largeurGrille; x++) {
			for(int y = 0; y < hauteurGrille; y++) {
				
				Case caseCourante = listeCases[x][y];
				
				/* On ne va ajouter à une zone que les cases qui sont pas vides
				 *  et on n'ajoute pas le château non plus 
				 *  On vérifie également que la case n'a pas déjà été zonée
				 */
				if(!caseCourante.isEmpty() && !caseCourante.isCastle() && !caseCourante.getIsZoned()) {
					ArrayList<Case> subZone = new ArrayList<>();
					subZone.add(caseCourante);
					
					for (Case caseAdjacente : getVoisins(x, y)) {
						//On regarde si la case adjacente est de même terrain que la case courante
						if(caseCourante.getTypeTerrain().equals(caseAdjacente.getTypeTerrain())) {
							if(caseAdjacente.getIsZoned()) {
								ArrayList<Case> zone = getZoneOfCase(caseAdjacente);
								//on ajoute la case à la zone
								caseCourante.setZoned();
								listeZones.get(listeZones.indexOf(zone)).add(caseCourante);
								break;
							} else {
								subZone.add(caseAdjacente);
							}
						}
					}
					
					/*Si après recherche, on ne peut ajouter la case à aucune zone
					 * on en crée une nouvelle avec la case
					 */
					if(!caseCourante.getIsZoned()) {
						for (Case case1 : subZone) {
							case1.setZoned();
						}
						ArrayList<Case> newZone = new ArrayList<Case>();
						newZone.addAll(subZone);
						listeZones.add(newZone);
					}
					
//					ArrayList<Case> zoneOfCurrentCase = getZoneOfCase(caseCourante);
//					
//					for (Case caseAdjacente : getVoisins(x, y)) {
//						if (caseAdjacente.getIsZoned()) {
//							if (caseAdjacente.getTypeTerrain().equals(caseAdjacente.getTypeTerrain())) {
//								if (!getZoneOfCase(caseAdjacente).equals(zoneOfCurrentCase)) {
//									unionZones(zoneOfCurrentCase,getZoneOfCase(caseAdjacente));
//								}
//							}
//						}
//						
//					}
					
				}
				
				
			}
		}
		
		for (int x = 0; x < largeurGrille; x++) {
			for(int y = 0; y < hauteurGrille; y++) {
				listeCases[x][y].setUnzoned();
			}
		}
	}
	
	public int calculerScore() {
		
		delimitZones();
		
		int score = 0;
		for(ArrayList<Case> zone : listeZones) {
			int couronnesZone = 0;
			for(Case caseCourante : zone) {
				couronnesZone += caseCourante.getNbCouronnes();
			}
			
			score += couronnesZone*zone.size();
		}
		
		
		/* 
		 * Implémentation de la règle "Empire du Milieu"
		 * (si le chateau est au milieu du royaume => +10 points)
		 */
		Map<String, Integer> limites = getLimites();
		int xMax = limites.get("xMax");
		int xMin = limites.get("xMin");
		int yMin = limites.get("yMin");
		int yMax = limites.get("yMax");
		if(xMin == 2 && xMax == 6 && yMin == 2 && yMax == 6) {
			score += 10;
		}
		
		/*
		 * Implémentation de la règle "Harmonie"
		 * (si royaume entièrement remplie => +5 points)
		 */
		if(getCaseInRoyaume() == 25) {
			score += 5;			
		}
		
		return score;
	}
	
	private ArrayList<Case> getZoneOfCase(Case caseParam){
		for(ArrayList<Case> zone : listeZones) {
			for(Case caseCourante : zone) {
				if(caseCourante.equals(caseParam))
					return zone;
			}
		}
		return null;
	}
	
	/** Renvoie une liste de tous les voisins d'une case dont les coordonnées sont passées
	 * en paramètres
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private ArrayList<Case> getVoisins(int x, int y) {
		
		ArrayList<Case> voisins = new ArrayList<Case>();
		
		for(int[] supp : new int[][] {{1,0},{-1,0},{0,1},{0,-1}}) {
			
			int j = supp[0];
			int k =supp[1];
			int xAdj = x + j;
			int yAdj = y + k;
			
			if(isInGrid(xAdj, yAdj)) {
				Case caseAdjacente = listeCases[xAdj][yAdj];
				voisins.add(caseAdjacente);
			}
		}
		return voisins;
	}
	
	public int getCaseInRoyaume() {
		int nbrCaseNonvide = 0;
		for (int x = 0; x < largeurGrille; x++) {
			for(int y = 0; y < hauteurGrille; y++) {
				if(!listeCases[x][y].isEmpty())
					nbrCaseNonvide += 1;
			}
		}
		return nbrCaseNonvide;
	}
	
	public int getScoreAfterMove(Move move) {
		
		Domino domino = move.getDomino();
		if (canPlace(move)) {
			int Xref = move.getXref();
			int Yref = move.getYref();
			int Xrot = move.getXrot();
			int Yrot = move.getYrot();
			
			listeCases[Xref][Yref] = domino.getCaseRef();
			listeCases[Xref][Yref].setX(Xref);
			listeCases[Xref][Yref].setY(Yref);
			listeCases[Xrot][Yrot] = domino.getCaseRot();
			listeCases[Xrot][Yrot].setX(Xrot);
			listeCases[Xrot][Yrot].setY(Yrot);
			
			int score = calculerScore();
			
			revertMove(move);			
			
			return score;
		} else {
			return -1;
		}
	}
	
	public void revertMove(Move move) {
		int Xref = move.getXref();
		int Yref = move.getYref();
		int Xrot = move.getXrot();
		int Yrot = move.getYrot();
		
		listeCases[Xref][Yref] = new Case(Xref, Yref);
		listeCases[Xrot][Yrot] = new Case(Xrot, Yrot);		
	}
	
	
	
	
	
}
