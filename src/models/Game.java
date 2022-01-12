package models;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import utils.AgentAction;
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
	protected PropertyChangeSupport pSupport;
	
	/**
	 * Constructeur
	 * @param maxturn: Nombre maximum de tour
	 */
	public Game(int maxturn){
		this.pMaxturn = maxturn;
		this.pTime = 1000;
		this.pSupport = new PropertyChangeSupport(this);
	}
	
	/**
	 *  Initialise le jeu
	 */
	public void init() {
		this.pTurn = 0;
		int value = 0;
		pSupport.firePropertyChange("pTurn", this.pTurn, value);
		this.pIsRunning = true;		
		this.initializeGame();
	}
	
	/**
	 *  Effectue un seul tour du jeu
	 */
	public void step() {	
		if(this.gameContinue() && this.pTurn < this.pMaxturn) {
			int value = this.pTurn + 1;
			pSupport.firePropertyChange("pTurn", this.pTurn, value);
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
	 * Ajoute un observer
	 * @param pcl
	 */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
    	pSupport.addPropertyChangeListener(pcl);
    }

    /**
     * Ajoute un observer
     * @param pcl
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
    	pSupport.removePropertyChangeListener(pcl);
    }
   
    /**
     * 
     * @return Le nombre de tours effectué
     */
    public int getTurn() {
    	return this.pTurn;
    }
    
    /**
     *  Modifie la vitesse du jeu
     * @param speed
     */
    public void setTime(double speed) {
    	this.pTime = (long) (1000 / speed);
    }
	
	//Méthodes abstraite
	public abstract void gameOver();
	public abstract void takeTurn();
	public abstract void initializeGame();
	public abstract boolean gameContinue();
	public abstract void restart(String mapName);
	public abstract void updateActionUser(AgentAction action);
}
