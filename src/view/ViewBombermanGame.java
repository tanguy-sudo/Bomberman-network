package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import models.BombermanGame;
import network.client.ControllerClient;
import utils.AgentAction;
/**
 * Vue de la partie
 * @author tanguy
 *
 */
public class ViewBombermanGame{
	private PanelBomberman pPanelBomberman;
	private JFrame window;
	ControllerClient controller;
	
	public ViewBombermanGame(PanelBomberman panelBomberman, ControllerClient controller) {
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

	public void updatePanel(BombermanGame bombermanGame) {
			this.pPanelBomberman.updateInfoGame(bombermanGame.getpBreakable_walls(), bombermanGame.fusionListAgent(), bombermanGame.getpListItems(), bombermanGame.getpListBomb());
			this.pPanelBomberman.repaint();
	}
	
	/**
	 * évênements clavier
	 */
    public void initKeyListener() {
        this.window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int key = keyEvent.getKeyCode();
                    switch (key) {
                		// gauche
                        case KeyEvent.VK_Q:
						case KeyEvent.VK_LEFT:
							controller.setAction(AgentAction.MOVE_LEFT);
                        	break;
						// droite
                        case KeyEvent.VK_D:
						case KeyEvent.VK_RIGHT:
							controller.setAction(AgentAction.MOVE_RIGHT);
                        	break;
						// haut
                        case KeyEvent.VK_Z:
						case KeyEvent.VK_UP:
							controller.setAction(AgentAction.MOVE_UP);
                        	break;
						// bas
                        case KeyEvent.VK_S:
						case KeyEvent.VK_DOWN:
							controller.setAction(AgentAction.MOVE_DOWN);
                        	break;
						// pose une bombe
                        case KeyEvent.VK_SPACE: 
                        	controller.setAction(AgentAction.PUT_BOMB);
                        	break;
                        default:
                        	controller.setAction(AgentAction.STOP);
                            break;
                    }
            }

            @Override
            public void keyTyped(KeyEvent keyEvent) {
				controller.setAction(AgentAction.STOP);

			}
            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }

        });
    }
}
