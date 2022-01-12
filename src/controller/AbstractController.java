package controller;
import models.*;
/**
 * 
 * @author tanguy
 * Classe qui d�finit les fonctions principales d'un contr�leur
 */
public abstract class AbstractController {
	
	protected Game pGame;
	protected String pMapName;
	
	/**
	 *  Arr�t et r�initialisation de la partie
	 */
	public void restart() {
		this.pGame.restart(pMapName);
		this.pGame.init();
		this.pause();
	}
	
	/**
	 *  Passage manuel d'une �tape
	 */
	public void step(){
		this.pGame.step();
	}
	
	/**
	 *  Passage automatique des �tapes
	 */
	public void play() {
		this.pGame.launch();
	}
	
	/*
	 *  Interruption du passage automatique des �tapes
	 */
	public void pause() {
		this.pGame.pause();
	}
	
	/**
	 *  R�glage de la vitesse du jeu
	 * @param speed : Vitesse du jeu
	 */
	public void setSpeed(double speed) {
		this.pGame.setTime(speed);
	}
}
