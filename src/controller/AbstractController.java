package controller;
import models.*;
/**
 * 
 * @author tanguy
 * Classe qui définit les fonctions principales d'un contrôleur
 */
public abstract class AbstractController {
	
	protected Game pGame;
	protected String pMapName;
	
	/**
	 *  Arrêt et réinitialisation de la partie
	 */
	public void restart() {
		this.pGame.restart(pMapName);
		this.pGame.init();
		this.pause();
	}
	
	/**
	 *  Passage manuel d'une étape
	 */
	public void step(){
		this.pGame.step();
	}
	
	/**
	 *  Passage automatique des étapes
	 */
	public void play() {
		this.pGame.launch();
	}
	
	/*
	 *  Interruption du passage automatique des étapes
	 */
	public void pause() {
		this.pGame.pause();
	}
	
	/**
	 *  Réglage de la vitesse du jeu
	 * @param speed : Vitesse du jeu
	 */
	public void setSpeed(double speed) {
		this.pGame.setTime(speed);
	}
}
