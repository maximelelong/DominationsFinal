import java.awt.Color;

public enum TypeTerrain {
	NONE ("     ", new Color(0,0,0,1)),
	FORET (" FOR ", new Color(40,122,45)),// dark green
	CHAMPS (" CHA ", new Color(226, 185, 36)), //dark yellow
	MER (" MER ", Color.BLUE),
	PRAIRIE (" PRA ", Color.GREEN),
	MINE (" MIN ", new Color(51,51,51)),
	MONTAGNE (" MON ", new Color(102,51,0)),
	CHATEAU (" ROI ", null);
	
	private String str = "  ";
	private Color color = null;
	
	TypeTerrain(String str, Color color) {
		this.str = str;
		this.color = color;
	}
	
	public String toString() {
		return str;
	}
	
	public Color getColor() {
		return color;
	}
}
