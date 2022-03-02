package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import network.client.ControllerClient;
import utils.AgentAction;
import utils.InfoAgent;
import utils.InfoBomb;
import utils.InfoItem;

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
		window.setVisible(false);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setFocusable(true);
		initKeyListener();
	

	}

	/**
	 * Mets à jour la panel
	 */
	public void updatePanel(boolean[][] breakable_walls, ArrayList<InfoAgent> listInfoAgents , ArrayList<InfoItem> listInfoItems, ArrayList<InfoBomb> listInfoBombs) {
			this.pPanelBomberman.updateInfoGame(breakable_walls, listInfoAgents, listInfoItems, listInfoBombs);
			this.pPanelBomberman.repaint();
	}

	public void setVisible(boolean visible){
		this.window.setVisible(visible);
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
			}
            @Override
            public void keyReleased(KeyEvent keyEvent) {}

        });
    }
}
