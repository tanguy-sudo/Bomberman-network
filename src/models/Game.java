package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import utils.AgentAction;
@JsonIgnoreProperties(value = { "pThread" })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
		@JsonSubTypes.Type(value = BombermanGame.class, name = "BombermanGame")
})
/**
 * 
 * @author tanguy
 * Classe Qui implémente les fonctions principales d'une partie
 */
public abstract class Game implements Runnable{
	//Attributs
	private int pTurn;
	protected int pMaxturn;
	protected boolean pIsRunning;
	private Thread pThread;
	private long pTime;

	@JsonCreator
	public Game(){}

	/**
	 * Constructeur
	 */
	public Game(int maxturn){
		this.pMaxturn = maxturn;
		this.pTime = 400;
	}
	
	/**
	 *  Initialise le jeu
	 */
	public void init() {
		this.pTurn = 0;
		int value = 0;
		this.pIsRunning = true;		
		this.initializeGame();
	}
	
	/**
	 *  Effectue un seul tour du jeu
	 */
	public void step() {	
		if(this.gameContinue() && this.pTurn < this.pMaxturn) {
			int value = this.pTurn + 1;
			this.pTurn = value;
			this.takeTurn();	
		}
		else {
			this.pIsRunning = false;
			this.gameOver();
		}
	}
	
	/**
	 *  Lance le jeu en pas à pas
	 */
	public void run() {
		while(this.pIsRunning) {
			this.step();
			try {
				Thread.sleep(pTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *  Mets en pause le jeu
	 */
	public void pause() {
		this.pIsRunning = false;
	}
	
	/**
	 * Lance une partie
	 */
	public void launch() {
		this.pIsRunning = true;
		this.pThread = new Thread(this);
		this.pThread.start();
	}
   
    /**
     * @return Le nombre de tours effectué
     */
    public int getpTurn() {
    	return this.pTurn;
    }
	public void setpTurn(int pTurn) {
		this.pTurn = pTurn;
	}

	public long getpTime() {
		return pTime;
	}
    public void setpTime(double speed) {
    	this.pTime = (long) (1000 / speed);
    }

	public int getpMaxturn() {
		return pMaxturn;
	}
	public void setpMaxturn(int pMaxturn) {
		this.pMaxturn = pMaxturn;
	}

	public boolean ispIsRunning() {
		return pIsRunning;
	}
	public void setpIsRunning(boolean pIsRunning) {
		this.pIsRunning = pIsRunning;
	}

	//M�thodes abstraite
	public abstract void gameOver();
	public abstract void takeTurn();
	public abstract void initializeGame();
	public abstract boolean gameContinue();
	public abstract void restart(String mapName);
	public abstract void updateActionUser(AgentAction action);
}
