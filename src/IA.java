import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IA {
	
	public static Domino choisirBestDomino(Joueur joueur, ArrayList<Domino> dominosAChoisirRestant) {
		
		Move bestMove;
		Royaume royaume = joueur.getRoyaume();
		HashMap<Move, Integer> dicoMoveBestScore = new HashMap<>();
		
		
		//Ajoute virtuellement le(s) domino(s) qu'il a choisi au tour d'avant
		ArrayList<Move> nextMoves = joueur.getNextMoves();
		for (Move move : nextMoves) {
			if (move.haveToBeDeleted()) {
				//do nothing
			} else {
				royaume.placerDomino(move);
			}
		}
		

		for (Domino domino : dominosAChoisirRestant) {
			
			if (Move.getPossibleMoves(royaume, domino).isEmpty()) {
				//si on ne peut pas jouer ce domino => on lui donne un score négatif
				//et le move sera de le défausser
				dicoMoveBestScore.put(new Move(domino, true), -1);
			} else {
				Move bestMoveWithDomino = choisirBestMove(royaume, domino);
				int bestScoreWithDomino = royaume.getScoreAfterMove(bestMoveWithDomino);
				dicoMoveBestScore.put(bestMoveWithDomino, bestScoreWithDomino);	
			}
		}
		
		int bestScore = Collections.max(dicoMoveBestScore.values());
					
		ArrayList<Move> bestMoves = new ArrayList<>();
		
		for (Move move : dicoMoveBestScore.keySet()) {
			
			if (dicoMoveBestScore.get(move) == bestScore) {
				bestMoves.add(move);
			}
		}
		//pour l'instant on prend juste le premier des dominos qui ont le meilleur score
		bestMove = bestMoves.get(0);
		
		//revert tous les moves faits pour choisir le domino
		for (Move move : joueur.getNextMoves()) {
			if (move.haveToBeDeleted()) {
				//do nothing
			}else {
				royaume.revertMove(move);				
			}
		}
		
		joueur.addNextMove(bestMove);
		Domino bestDomino = bestMove.getDomino();
		
		
		return bestDomino;
		
	}
	
	
	public static Move choisirBestMove(Royaume royaume,Domino domino) {
		
		Map<Move, Integer> dicoMoveScore = new HashMap<Move, Integer>();
		ArrayList<Move> possibleMoves = Move.getPossibleMoves(royaume, domino);
		
		for (Move move : possibleMoves) {
			
			int scoreMove = royaume.getScoreAfterMove(move);
			dicoMoveScore.put(move, scoreMove);
		
			
		}
		int bestScore = 0;
		
		bestScore = Collections.max(dicoMoveScore.values());
		
		ArrayList<Move> bestMoves = new ArrayList<Move>();
		
		for (Move move : dicoMoveScore.keySet()) {
			
			if (dicoMoveScore.get(move) == bestScore) {
				bestMoves.add(move);
			}
		}
		
		//pour l'instant on prend juste le premier des moves qui ont le meilleur score
		return bestMoves.get(0);
	}
}
