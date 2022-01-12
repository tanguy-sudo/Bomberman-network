package controller.states;
import view.*;
/**
 * 
 * @author tanguy
 * Classe indiquant l'état restart
 */
public class EtatRestart implements Etat{
	
	private ViewCommand pViewCommand;
	
	/**
	 * Rends les boutons nexts et start intéragissable et les boutons restart et wait enable
	 * @param viewCommand : Vue command
	 */
	public EtatRestart(ViewCommand viewCommand) {
		this.pViewCommand = viewCommand;
		
		this.pViewCommand.restartButton.setEnabled(false);
		this.pViewCommand.startButton.setEnabled(true);
		this.pViewCommand.nextButton.setEnabled(true);
		this.pViewCommand.waitButton.setEnabled(false);
	}
	
	@Override
	public void restart() {
	}

	@Override
	public void play() {
		this.pViewCommand.setEtat(new EtatStart(this.pViewCommand));
	}

	@Override
	public void pause() {	
	}

	@Override
	public void step() {	
		this.pViewCommand.setEtat(new EtatStep(this.pViewCommand));
	}
	
}
