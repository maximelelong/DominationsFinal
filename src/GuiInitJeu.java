import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GuiInitJeu {
	public static int sizeX = GuiBoard.sizeX;
	public static int sizeY = GuiBoard.sizeY;
	public static Color boardBackgroundColor = GuiBoard.boardBackgroundColor;
	public static Font font;
	public static Font titleFont;
	
	public static String  projectPath= Paths.get("").toAbsolutePath().toString() + File.separator;
	public static String ttfFile = projectPath + "RINGM___.TTF";
	public static String backGroundImage = projectPath + "backGround.png";
	public static String checkedBox = projectPath + "checked-white.png";
	public static String uncheckedBox = projectPath + "unchecked-white.png";
	
	static {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(ttfFile));
			titleFont = font.deriveFont(130f).deriveFont(Font.HANGING_BASELINE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StdDraw.setCanvasSize(sizeX, sizeY);
		StdDraw.setXscale(0, sizeX);
		StdDraw.setYscale(0, sizeY);
		StdDraw.enableDoubleBuffering();
	}
	
	public static int choisirNbreJoueurs() {
		
		int buttonHalfWidth = sizeX/5;
		int buttonHalfHeight = sizeY/15;
		int spaceBetweenButtons = sizeY/10;
		
		StdDraw.picture(sizeX/2 - 200, sizeY/2, backGroundImage, 1440*1.7, 600*1.7);
		
		StdDraw.setFont(titleFont);
		StdDraw.setPenColor();
		StdDraw.text(sizeX/2+8, sizeY - sizeY/10, "Domi'NationS");
		StdDraw.setPenColor(new Color(226, 185, 36));
		StdDraw.text(sizeX/2, sizeY - sizeY/10, "Domi'NationS");
		
		StdDraw.setPenColor();
		StdDraw.filledRectangle(sizeX/2, sizeY/2 + buttonHalfHeight + spaceBetweenButtons, buttonHalfWidth, buttonHalfHeight);
		StdDraw.filledRectangle(sizeX/2, sizeY/2, buttonHalfWidth, buttonHalfHeight);
		StdDraw.filledRectangle(sizeX/2, sizeY/2 - buttonHalfHeight - spaceBetweenButtons, buttonHalfWidth, buttonHalfHeight);
		StdDraw.setPenColor(Color.WHITE);
		StdDraw.setFont(font.deriveFont(50f));
		StdDraw.text(sizeX/2, sizeY/2 + buttonHalfHeight + spaceBetweenButtons, "2 Joueurs");
		StdDraw.text(sizeX/2, sizeY/2, "3 Joueurs");
		StdDraw.text(sizeX/2, sizeY/2 - buttonHalfHeight - spaceBetweenButtons, "4 Joueurs");
		StdDraw.show();
		
		while (true) {
			if (StdDraw.isMousePressed()) {
				if (isMouseInRectangle(sizeX/2, sizeY/2 + buttonHalfHeight + spaceBetweenButtons, buttonHalfWidth, buttonHalfHeight)) {
					return 2;
				}
				if (isMouseInRectangle(sizeX/2, sizeY/2, buttonHalfWidth, buttonHalfHeight)) {
					return 3;
				}
				if (isMouseInRectangle(sizeX/2, sizeY/2 - buttonHalfHeight - spaceBetweenButtons, buttonHalfWidth, buttonHalfHeight)) {
					return 4;
				}
			}
		}
	}
	
	public static Map<GameColor, Boolean> choisirIfIA(int nbreJoueur){
		
		ArrayList<GameColor> couleursRestantes = new ArrayList<>(Arrays.asList(GameColor.values()));
		Map<GameColor, Boolean> dicoCouleurIsAI = new HashMap<>();
		for(int i = 0; i < nbreJoueur; i++) {
			dicoCouleurIsAI.put(couleursRestantes.get(i), false);
		}
		
		int yStartListeJoueurs = 600;
		int spaceBetweenListElement = sizeY/8;
		int xText = sizeX/2 + 235;
		
		int xCheckBox = sizeX/2 + 200;
		int checkBoxWidth = 40;
		
		int yBackgroundRectangle = sizeY/2 - 100;
		int halfWidthRectangle = 260;
		int halfHeightRectangle = 350;
		
		StdDraw.picture(sizeX/2 - 200, sizeY/2, backGroundImage, 1440*1.7, 600*1.7);
		StdDraw.setFont(titleFont);
		StdDraw.setPenColor();
		StdDraw.text(sizeX/2+8, sizeY - sizeY/10, "Domi'NationS");
		StdDraw.setPenColor(new Color(226, 185, 36));
		StdDraw.text(sizeX/2, sizeY - sizeY/10, "Domi'NationS");
		
		StdDraw.setPenColor();
		StdDraw.filledRectangle(sizeX/2, yBackgroundRectangle, halfWidthRectangle, halfHeightRectangle);
		
		StdDraw.setFont(font.deriveFont(40f));
		StdDraw.setPenColor(Color.WHITE);
		for (int i = 0; i < nbreJoueur; i++) {
			StdDraw.textRight(xText, yStartListeJoueurs - i*spaceBetweenListElement,
					"Joueur " + couleursRestantes.get(i) + "  (IA      )");
			StdDraw.picture(xCheckBox, yStartListeJoueurs - i*spaceBetweenListElement - 5, uncheckedBox, checkBoxWidth, checkBoxWidth);
		}
		
		StdDraw.text(sizeX/2, 150, "Valider");
		
		StdDraw.show();
		
		boolean isMousePressedBuffer = StdDraw.isMousePressed();;
		
		while (true) {
			
			if (StdDraw.isMousePressed() && !isMousePressedBuffer) {
				isMousePressedBuffer = true;
				
				for (int i = 0; i < nbreJoueur; i++) {
					if (isMouseInSquare(xCheckBox, yStartListeJoueurs - i*spaceBetweenListElement, checkBoxWidth/2)) {
						GameColor couleur = couleursRestantes.get(i);
						boolean isIA = dicoCouleurIsAI.get(couleur);
						
						dicoCouleurIsAI.put(couleur, !isIA);
					}
				}
				
				StdDraw.picture(sizeX/2 - 200, sizeY/2, backGroundImage, 1440*1.7, 600*1.7);
				StdDraw.setFont(titleFont);
				StdDraw.setPenColor();
				StdDraw.text(sizeX/2+8, sizeY - sizeY/10, "Domi'NationS");
				StdDraw.setPenColor(new Color(226, 185, 36));
				StdDraw.text(sizeX/2, sizeY - sizeY/10, "Domi'NationS");
				
				StdDraw.setPenColor();
				StdDraw.filledRectangle(sizeX/2, yBackgroundRectangle, halfWidthRectangle, halfHeightRectangle);
				
				StdDraw.setFont(font.deriveFont(40f));
				StdDraw.setPenColor(Color.WHITE);
				StdDraw.text(sizeX/2, 150, "Valider");
				
				for (int i = 0; i < nbreJoueur; i++) {
					StdDraw.textRight(xText, yStartListeJoueurs - i*spaceBetweenListElement,
							"Joueur " + couleursRestantes.get(i) + "  (IA      )");
					GameColor couleur = couleursRestantes.get(i);
					boolean isIA = dicoCouleurIsAI.get(couleur);
					if (isIA) {
						StdDraw.picture(xCheckBox, yStartListeJoueurs - i*spaceBetweenListElement - 5, checkedBox, checkBoxWidth, checkBoxWidth);
						
					} else {
						StdDraw.picture(xCheckBox, yStartListeJoueurs - i*spaceBetweenListElement - 5, uncheckedBox, checkBoxWidth, checkBoxWidth);
					}
				}
				StdDraw.show();
				
				//si on appuie sur "Valider" on sort de la boucle
				if (isMouseInRectangle(sizeX/2, 150, 100, 40)) {
					break;
				}
			}
			if (!StdDraw.isMousePressed()) {
				isMousePressedBuffer = false;
			}
			
		}
		
		return dicoCouleurIsAI;
		
	}
	
	
	
	
	public static boolean isMouseInRectangle(int xCenter, int yCenter, int halfWidth, int halfHeight) {
		int x = (int) StdDraw.mouseX();
		int y = (int) StdDraw.mouseY();
		
		if (x > xCenter - halfWidth && x < xCenter + halfWidth && 
				y > yCenter - halfHeight && y < yCenter + halfHeight) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isMouseInSquare(int xCenter, int yCenter, int halfWidth) {
		return isMouseInRectangle(xCenter, yCenter, halfWidth, halfWidth);
	}
	
}
