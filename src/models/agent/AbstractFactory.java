package models.agent;
import utils.InfoAgent;
/**
 * Fabrique abstraite
 * @author tanguy
 *
 */
public interface AbstractFactory {
	public Agent createAgent(InfoAgent infoAgent, int niveau, boolean manual);
}
