
public class Case {
	
	private TypeTerrain typeTerrain;
	private int nbCouronnes;
	private int X;
	private int Y;
	private boolean isZoned = false;
	
	public Case(int x, int y) {
		typeTerrain = TypeTerrain.NONE;
		nbCouronnes = 0;
		this.X = x;
		this.Y = y;
	}
	
	public Case(TypeTerrain type, int nombrebCouronnes) {
		typeTerrain = type;
		nbCouronnes = nombrebCouronnes;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public TypeTerrain getTypeTerrain() {
		return typeTerrain;
	}
	
	public void setTypeTerrain(TypeTerrain type) {
		typeTerrain = type;
	}

	public int getNbCouronnes() {
		return nbCouronnes;
	}
	
	public boolean isEmpty() {
		if(this.typeTerrain.equals(TypeTerrain.NONE))
			return true;
		else
			return false;
	}
	
	public boolean isCastle() {
		if(this.typeTerrain.equals(TypeTerrain.CHATEAU))
			return true;
		else
			return false;
	}
	
	public String printCouronnes() {
		if (this.isEmpty()) {
			return "     ";
		}
		return "  " + nbCouronnes + "  ";
	}
	
	public void setZoned() {
		isZoned = true;
	}
	
	public void setUnzoned() {
		isZoned = false;
	}
	
	public boolean getIsZoned() {
		return isZoned;
	}

}