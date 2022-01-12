package controller.states;
/**
 * 
 * @author tanguy
 * Interface des différents états de la vue command
 */
public interface Etat {
	public void restart();
	public void play();
	public void pause();
	public void step();
}
