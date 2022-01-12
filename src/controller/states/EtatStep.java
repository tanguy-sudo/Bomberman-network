package controller.states;
import view.*;
/**
 * 
 * @author tanguy
 * Classe indiquant l'état step
 */
public class EtatStep implements Etat{
	
	private ViewCommand pViewCommand;
	
	/**
	 * Rends les boutons restart et start et next intéragissable et le boutons wait enable
	 * @param viewCommand : Vue command
	 */
	public EtatStep(ViewCommand viewCommand) {
		this.pViewCommand = viewCommand;
		
		this.pViewCommand.restartButton.setEnabled(true);
		this.pViewCommand.startButton.setEnabled(true);
		this.pViewCommand.nextButton.setEnabled(true);
		this.pViewCommand.waitButton.setEnabled(false);
	}

	@Override
	public void restart() {
		this.pViewCommand.setEtat(new EtatRestart(this.pViewCommand));			
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
