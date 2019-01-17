import java.util.HashMap;
import java.util.Map;

public class Outils {
	public static Map<String, TypeTerrain> getDicoStringToType() {
		Map<String, TypeTerrain> map = new HashMap<>();
		
		map.put("Champs", TypeTerrain.CHAMPS);
		map.put("Foret", TypeTerrain.FORET);
		map.put("Mer", TypeTerrain.MER);
		map.put("Prairie", TypeTerrain.PRAIRIE);
		map.put("Mine", TypeTerrain.MINE);
		map.put("Montagne", TypeTerrain.MONTAGNE);	
		
		return map;
	}
}
