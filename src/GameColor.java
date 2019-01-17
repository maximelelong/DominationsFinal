import java.awt.Color;

public enum GameColor {
	ROUGE (Color.RED),
	VERT (new Color(96,255,7)),
	BLEU (new Color(52,152,219)),
	ROSE (new Color(227,150,243));

	private Color awtColor = null;
	
	GameColor(Color awtColor) {
		this.awtColor= awtColor;	
	}
	public Color getAwtColor() {
		return awtColor;
	}
}
