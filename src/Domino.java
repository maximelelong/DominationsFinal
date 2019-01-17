public class Domino {
	private Case caseRef;
	private Case caseRot;
	private int numero;
	private Roi roi = null;
	
	public Domino(Case caseRef, Case caseRot, int numero) {
		this.caseRef = caseRef;
		this.caseRot = caseRot;
		this.numero = numero;
	}

	public Case getCaseRef() {
		return caseRef;
	}

	public Case getCaseRot() {
		return caseRot;
	}
	
	public int getNumero() {
		return numero;
	}
	
	public void setRoi(Roi roi) {
		this.roi = roi;
	}
	
	public void removeRoi() {
		this.roi = null;
	}
	
	public Roi getRoi() {
		return roi;
	}
	
	public boolean isChoosed() {
		if (roi == null) {
			return false;
		} else
			return true;
	}
	
	public String toString() {
		String str = "";
		str = " ----- ----- \n";
		str += "|" + caseRef.getTypeTerrain() + "|" + caseRot.getTypeTerrain() + "| \n";
		str += "|" + caseRef.printCouronnes() + "|" + caseRot.printCouronnes() + "| \n";
		str += " ----- ----- ";
		return str;	
		
	}

}