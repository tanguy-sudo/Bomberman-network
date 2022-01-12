package models;

import utils.AgentAction;
/**
 * 
 * @author tanguy
 * Classe de test 
 */
public class SimpleGame extends Game  {
	
	/**
	 * Constructeur de la classe
	 * @param maxturn
	 */
	public SimpleGame(int maxturn){
		super(maxturn);
	}

	@Override
	public void gameOver() {
		System.out.println("Fin du jeu");
		
	}

	@Override
	public void takeTurn() {
		System.out.println("Tour " + getTurn() + " du jeu en cours");
	}

	@Override
	public void initializeGame() {
		
	}

	@Override
	public boolean gameContinue() {
		return true;
	}
	
	@Override
	public void restart(String mapName) {
		
	}

	@Override
	public void updateActionUser(AgentAction action) {
		// TODO Auto-generated method stub
		
	}

}
