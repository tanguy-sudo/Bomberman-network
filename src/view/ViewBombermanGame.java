package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;

import controller.ControllerBombermanGame;
import models.BombermanGame;
import utils.AgentAction;
/**
 * Vue de la partie
 * @author tanguy
 *
 */
public class ViewBombermanGame  implements PropertyChangeListener {
	private PanelBomberman pPanelBomberman;
	private JFrame window;
	ControllerBombermanGame controller;
	
	public ViewBombermanGame(PanelBomberman panelBomberman, ControllerBombermanGame controller) {
		this.pPanelBomberman = panelBomberman;
		this.controller = controller;
		window = new JFrame("Game");
		window.add(this.pPanelBomberman);
		window.setSize(this.pPanelBomberman.getSizeX() * 50, this.pPanelBomberman.getSizeY() * 50);
		window.setLocation(0, 0);
		// affiche l'interface
		window.setVisible(true);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setFocusable(true);
		initKeyListener();
	

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName() == "pGame") {
			BombermanGame game = (BombermanGame) evt.getNewValue();	
			this.pPanelBomberman.updateInfoGame(game.getBreakable_walls(), game.getListAgent(), game.getListItems(), game.getListBomb());
			this.pPanelBomberman.repaint();
		}
	}
	
	/**
	 * Événements clavier
	 */
    public void initKeyListener() {
        this.window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int key = keyEvent.getKeyCode();
                    switch (key) {
                		// gauche
                        case KeyEvent.VK_Q:
                        	controller.updateActionBomberman(AgentAction.MOVE_LEFT);
                        	break;
                        case KeyEvent.VK_LEFT:
                        	controller.updateActionBomberman(AgentAction.MOVE_LEFT);
                        	break;
                        // droite
                        case KeyEvent.VK_D:
                        	controller.updateActionBomberman(AgentAction.MOVE_RIGHT);
                        	break;
                        case KeyEvent.VK_RIGHT:
                        	controller.updateActionBomberman(AgentAction.MOVE_RIGHT);
                        	break;
                        // haut
                        case KeyEvent.VK_Z:
                        	controller.updateActionBomberman(AgentAction.MOVE_UP);
                        	break;
                        case KeyEvent.VK_UP:
                        	controller.updateActionBomberman(AgentAction.MOVE_UP);
                        	break;
                    	// bas
                        case KeyEvent.VK_S:
                        	controller.updateActionBomberman(AgentAction.MOVE_DOWN);
                        	break;
                        case KeyEvent.VK_DOWN:
                        	controller.updateActionBomberman(AgentAction.MOVE_DOWN);
                        	break;
                        // pose une bombe
                        case KeyEvent.VK_SPACE: 
                        	controller.updateActionBomberman(AgentAction.PUT_BOMB);
                        	break;
                        default:
                        	controller.updateActionBomberman(AgentAction.STOP);
                            break;
                    }
            }

            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                int key = keyEvent.getKeyCode();
                if (key != KeyEvent.VK_SPACE) {
                	controller.updateActionBomberman(AgentAction.STOP);
                }
            }

        });
    }
}
