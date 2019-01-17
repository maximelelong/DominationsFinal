import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GuiBoard {
	public static Color boardBackgroundColor = Color.LIGHT_GRAY;
	public static int tileSize = 50;
	public static int royaumeSize = 9*tileSize;
	public static int spaceBetweenRoyaumes = 20;
	
	public static int sizeX = 1300;
	public static int sizeY = 3*spaceBetweenRoyaumes + 2*royaumeSize;
	
	public static int XDominosAJouer = 3*spaceBetweenRoyaumes + 2*royaumeSize + 2*spaceBetweenRoyaumes ;
	public static int XDominosAChoisir = XDominosAJouer + 2*tileSize + 2*spaceBetweenRoyaumes;
	public static int YStartListesDominos = 600;
	
	public static String  projectPath= Paths.get("").toAbsolutePath().toString() + File.separator;
	
	public static String tournerADroite = projectPath + "tournerADroite.png";
	public static String tournerAGauche = projectPath + "tournerAGauche.png";
	public static String couronne = projectPath + "crown.png";
	public static String couronneROUGE = projectPath+ "couronneRouge.png";
	public static String couronneBLEU = projectPath+ "couronneBleue.png";
	public static String couronneVERT = projectPath+ "couronneVerte.png";
	public static String couronneROSE = projectPath + "couronneRose.png";
	public static String chateauROUGE = projectPath + "chateauROUGE.png";
	public static String chateauBLEU = projectPath+ "chateauBLEU.png";
	public static String chateauVERT = projectPath+ "chateauVERT.png";
	public static String chateauROSE = projectPath + "chateauROSE.png";
	public static String wood = projectPath + "wood.png";
	
	
	static {
		
//		StdDraw.setCanvasSize(sizeX, sizeY);
//		StdDraw.setXscale(0, sizeX);
//		StdDraw.setYscale(0, sizeY);
//		StdDraw.enableDoubleBuffering();
	}
	public static void choosePlaceForDomino(Joueur joueur, Domino domino) {
		Royaume royaume = joueur.getRoyaume();
		Direction dir = Direction.DROITE;
		int XStartRoyaume = getRoyaumeXStart(joueur);
		int YStartRoyaume = getRoyaumeYStart(joueur);
		int xDominoToPlay = XDominosAJouer+ tileSize + (XDominosAChoisir - XDominosAJouer)/2;
		int yDominoToPlay = 800;
		Case caseRef = domino.getCaseRef();
		Case caseRot = domino.getCaseRot();
		
		loadBoard("Joueur " + joueur.getColorRoi(), "Veuillez placer votre domino");
		StdDraw.picture(xDominoToPlay -tileSize -spaceBetweenRoyaumes*3, yDominoToPlay, tournerADroite, 96/2, 96/2);
		StdDraw.picture(xDominoToPlay +tileSize +spaceBetweenRoyaumes*3, yDominoToPlay, tournerAGauche, 96/2, 96/2);
		printCase(caseRef, xDominoToPlay, yDominoToPlay);
		printCase(caseRot, xDominoToPlay+tileSize, yDominoToPlay);
		StdDraw.show();
		
		boolean isMousePressedBuffer = StdDraw.isMousePressed();;
		
		while (true) {
			
			if (StdDraw.isMousePressed() && !isMousePressedBuffer) {
				isMousePressedBuffer = true;
				int x = (int) StdDraw.mouseX();
				int y = (int) StdDraw.mouseY();
				if (x > XStartRoyaume && x < XStartRoyaume + royaumeSize &&
						y > YStartRoyaume && y < YStartRoyaume + royaumeSize) {
					int xCoord = (x - XStartRoyaume)/tileSize;//division enti�re
					int yCoord = (y - YStartRoyaume)/tileSize;
					
					if (royaume.placerDomino(new Move(domino, xCoord, yCoord, dir))) {
						break;
					}
				} else {
					if (GuiInitJeu.isMouseInSquare(xDominoToPlay + tileSize + spaceBetweenRoyaumes*3, yDominoToPlay, 92/2)) {
						switch (dir) {
						case BAS:
							dir= Direction.DROITE;
							break;
						case DROITE:
							dir= Direction.HAUT;
							break;
						case HAUT:
							dir= Direction.GAUCHE;
							break;
						case GAUCHE:
							dir= Direction.BAS;
							break;
							
						default:
							break;
						}
					
					} else if (GuiInitJeu.isMouseInSquare(xDominoToPlay - tileSize - spaceBetweenRoyaumes*3, yDominoToPlay, 92/2)) {
						switch (dir) {
						case BAS:
							dir= Direction.GAUCHE;
							break;
						case DROITE:
							dir= Direction.BAS;
							break;
						case HAUT:
							dir= Direction.DROITE;
							break;
						case GAUCHE:
							dir= Direction.HAUT;
							break;
							
						default:
							break;
						
						} 

					}
					
				}
				loadBoard("Joueur " + joueur.getColorRoi(), "Veuillez placer votre domino");
				StdDraw.picture(xDominoToPlay -tileSize -spaceBetweenRoyaumes*3, yDominoToPlay, tournerADroite, 96/2, 96/2);
				StdDraw.picture(xDominoToPlay +tileSize +spaceBetweenRoyaumes*3, yDominoToPlay, tournerAGauche, 96/2, 96/2);
				printCase(caseRef, xDominoToPlay, yDominoToPlay);
				switch (dir) {
				case DROITE:
					printCase(caseRot, xDominoToPlay+tileSize, yDominoToPlay);
					break;
				case BAS:
					printCase(caseRot, xDominoToPlay, yDominoToPlay-tileSize);
					break;
				case GAUCHE:
					printCase(caseRot, xDominoToPlay-tileSize, yDominoToPlay);
					break;
				case HAUT:
					printCase(caseRot, xDominoToPlay, yDominoToPlay+tileSize);
					break;
				default:
					break;
				}
				
				StdDraw.show();
				
			}
			
			if (!StdDraw.isMousePressed()) {
				isMousePressedBuffer = false;
			}
			
		}
	}
	
	public static int choisirDomino(Joueur joueur) {
		
		refreshBoard("Joueur " + joueur.getColorRoi(), "Veuillez choisir un domino");
		boolean isMousePressedBuffer = StdDraw.isMousePressed();
		while (true) {
			if (StdDraw.isMousePressed() && !isMousePressedBuffer) {
				
				int x = (int) StdDraw.mouseX();
				int y = (int) StdDraw.mouseY();
				
				int indiceDomino = -1;
				if (x > XDominosAChoisir && x < XDominosAChoisir + tileSize*2) {
					for (int i = 0; i < Partie.dominosAChoisir.size(); i++) {
						if (y < YStartListesDominos - i*(spaceBetweenRoyaumes + tileSize) &&
								y > YStartListesDominos - (i+1)*(spaceBetweenRoyaumes + tileSize)) {
							indiceDomino = i;
							break;
						}
					}
				}
				
				if (indiceDomino != -1) {
					if (!Partie.dominosAChoisir.get(indiceDomino).isChoosed()) {
						return indiceDomino;
					}
				}
			}
			
			if (!StdDraw.isMousePressed()) {
				isMousePressedBuffer = false;
			}
		}
	}
	
	public static void loadBoard(String...messages) {
		StdDraw.clear(boardBackgroundColor);

		//afficher les royaumes
		for (Joueur joueur : Partie.listeJoueurs) {

			int XStartRoyaume = getRoyaumeXStart(joueur);
			int YStartRoyaume = getRoyaumeYStart(joueur);
			
			StdDraw.setPenColor();
			StdDraw.setFont(GuiInitJeu.font.deriveFont(30f));
			StdDraw.text(1130, 200, messages[0]);
			
			StdDraw.setFont(GuiInitJeu.font.deriveFont(20f));
			for (int i = 1; i < messages.length; i++) {
				String message = messages[i];
				StdDraw.text(1130, 200 - i*50, message);
			}
			
			//affichage du fond en bois sur chaque royaume
			StdDraw.picture(XStartRoyaume + royaumeSize/2, YStartRoyaume +  royaumeSize/2, wood, royaumeSize, royaumeSize);
			
			Royaume royaume = joueur.getRoyaume();

			for (int x = 0; x < Royaume.largeurGrille; x++) {
				for (int y = 0; y < Royaume.hauteurGrille; y++) {
					int XBoard = XStartRoyaume + x*tileSize + tileSize/2;
					int YBoard = YStartRoyaume + y*tileSize + tileSize/2;
					Case caseCourante = royaume.getCase(x, y);
					if (caseCourante.isCastle()) {
						printChateau(joueur.getColorRoi(), XBoard, YBoard);
					} else {
						printCase(caseCourante, XBoard, YBoard);
					}
				}
			}

		}
		//Affiche la liste des dominos à jouer
		for (int i = 0; i < Partie.dominosAjouer.size(); i++) {
			Domino domino = Partie.dominosAjouer.get(i);
			Case caseRef = domino.getCaseRef();
			Case caseRot = domino.getCaseRot();
			int yDomino = YStartListesDominos - tileSize/2 - i*(spaceBetweenRoyaumes+tileSize);
			printCase(caseRef, XDominosAJouer + tileSize/2, yDomino);
			printCase(caseRot, XDominosAJouer + 3*tileSize/2, yDomino);
			//Affiche un pion de la couleur du joueur qui l'a choisi
			if (domino.isChoosed()) {
				GameColor couleurRoi = domino.getRoi().getColor();
				String fileToUse = "";
				switch (couleurRoi) {
				case ROUGE:
					fileToUse = couronneROUGE;
					break;
				case BLEU:
					fileToUse = couronneBLEU;
					break;
				case VERT:
					fileToUse = couronneVERT;
					break;
				case ROSE:
					fileToUse = couronneROSE;
					break;
				default:
					break;
				}
				StdDraw.picture(XDominosAJouer + tileSize, yDomino, fileToUse, 96/2, 96/2);
			}

		}

		//Affiche la liste des dominos à choisir
		for (int i = 0; i < Partie.dominosAChoisir.size(); i++) {
			Domino domino = Partie.dominosAChoisir.get(i);
			Case caseRef = domino.getCaseRef();
			Case caseRot = domino.getCaseRot();
			int yDomino = YStartListesDominos - tileSize/2 - i*(spaceBetweenRoyaumes+tileSize);
			printCase(caseRef, XDominosAChoisir + tileSize/2, yDomino);
			printCase(caseRot, XDominosAChoisir + 3*tileSize/2, yDomino);
			//Affiche un pion de la couleur du joueur qui l'a choisi
			if (domino.isChoosed()) {
				GameColor couleurRoi = domino.getRoi().getColor();
				String fileToUse = "";
				switch (couleurRoi) {
				case ROUGE:
					fileToUse = couronneROUGE;
					break;
				case BLEU:
					fileToUse = couronneBLEU;
					break;
				case VERT:
					fileToUse = couronneVERT;
					break;
				case ROSE:
					fileToUse = couronneROSE;
					break;
				default:
					break;
				}
				StdDraw.picture(XDominosAChoisir + tileSize, yDomino, fileToUse, 96/2, 96/2);
			}
		}
	}
	
	public static void refreshBoard(String...messages) {
		loadBoard(messages);
		StdDraw.show();
	}
	
	public static void printCase(Case caseToPrint, int x, int y) {
		TypeTerrain type = caseToPrint.getTypeTerrain();
		StdDraw.setPenColor(type.getColor());
		StdDraw.filledSquare(x, y, tileSize/2);
		StdDraw.setFont();
		StdDraw.setPenColor();
		//StdDraw.text(x, y+10, type.toString());
		
		switch (caseToPrint.getNbCouronnes()) {
		case 1:
			StdDraw.picture(x, y-10, couronne, 256/12, 256/12);
			break;
		case 2:
			StdDraw.picture(x-10, y-10, couronne, 256/12, 256/12);
			StdDraw.picture(x+10, y-10, couronne, 256/12, 256/12);
			break;
		case 3:
			StdDraw.picture(x-10, y-10, couronne, 256/12, 256/12);
			StdDraw.picture(x, y, couronne, 256/12, 256/12);
			StdDraw.picture(x+10, y-10, couronne, 256/12, 256/12);
		default:
			break;
		}
	} 
	
	
	public static void printChateau(GameColor color, int x, int y) {
		String fileToUse = "";
		switch (color) {
		case ROUGE:
			fileToUse = chateauROUGE;
			break;      
		case BLEU:      
			fileToUse = chateauBLEU;
			break;      
		case VERT:      
			fileToUse = chateauVERT;
			break;      
		case ROSE:      
			fileToUse = chateauROSE;
			break;
		default:
			break;
		}		
		StdDraw.picture(x, y, fileToUse, 45, 45);
	}
	
	public static void refreshBoard() {
		refreshBoard("");
	}
	
	public static void afficherScore(ArrayList<Joueur> listeJoueurs) {
		int xResultats = sizeX/2 + 470;
		int yStartListeJoueurs = 600;
		int spaceBetweenListElement = sizeY/8;
		
		
		StdDraw.setPenColor();
		StdDraw.filledRectangle(xResultats, sizeY/2, 150, 300);
		
		StdDraw.setPenColor(Color.WHITE);
		StdDraw.setFont(GuiInitJeu.font.deriveFont(50f).deriveFont(Font.BOLD));
		StdDraw.text(xResultats, yStartListeJoueurs+100, "Résultats");		
		
		StdDraw.setFont(GuiInitJeu.font.deriveFont(23f));
		for (int i = 0; i < listeJoueurs.size(); i++) {
			Joueur joueur = listeJoueurs.get(i);
			StdDraw.text(xResultats, yStartListeJoueurs - i*spaceBetweenListElement,
					(i+1) +"e : " + joueur.getColorRoi() + " avec " + joueur.getScore() +" points");
		}
		
		StdDraw.show();
	}
	
	public static int getRoyaumeXStart(Joueur joueur) {
		
		int XStartRoyaume;

		switch (joueur.getColorRoi()) {
		case ROUGE:
			XStartRoyaume = spaceBetweenRoyaumes;
			break;
		case VERT:
			XStartRoyaume = spaceBetweenRoyaumes*2 + royaumeSize;
			break;
		case BLEU:
			XStartRoyaume = spaceBetweenRoyaumes;
			break;
		case ROSE:
			XStartRoyaume = spaceBetweenRoyaumes*2 + royaumeSize;
			break;
		default:
			throw new NullPointerException("La couleur de ce joueur est inconnue");
		}
		
		return XStartRoyaume;
	}
	
	public static int getRoyaumeYStart(Joueur joueur) {
		int YStartRoyaume;

		switch (joueur.getColorRoi()) {
		case ROUGE:
			YStartRoyaume = spaceBetweenRoyaumes*2 + royaumeSize;
			break;
		case VERT:
			YStartRoyaume = spaceBetweenRoyaumes*2 + royaumeSize;
			break;
		case BLEU:
			YStartRoyaume = spaceBetweenRoyaumes;
			break;
		case ROSE:
			YStartRoyaume = spaceBetweenRoyaumes;
			break;
		default:
			throw new NullPointerException("La couleur de ce joueur est inconnue");
		}
		return YStartRoyaume;
	}
	
	

}
