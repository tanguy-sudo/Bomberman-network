package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.ControllerBombermanGame;
/**
 * Vue de fin de partie
 * @author tanguy
 *
 */
public class ViewEnd {
	private JFrame window;
	private JLabel valueNbEnemiesDeadLabel;
	private JLabel valueNbEnemyLivingLabel;
	private JLabel valueNbAlliesDeadLabel;
	private JLabel valueNbAlliesLivingLabel;
	private JLabel winOrLoseLabel;
	public ControllerBombermanGame controller;
	
	public ViewEnd(int result, int NbEnemiesDead, int NbEnemyLiving, int NbAlliesDead, int NbAlliesLiving, ControllerBombermanGame controller) {

		this.window = new JFrame("Bomberman");
		JPanel infoPanel = new JPanel();
		JPanel globalPanel = new JPanel();
		this.controller = controller;
		
		GridLayout infoGridlayout = new GridLayout(4, 2);
		BoxLayout globalboxLayout = new BoxLayout(globalPanel, BoxLayout.Y_AXIS);
		
		switch(result) {
			case 0:
				this.winOrLoseLabel = new JLabel("Equality"); 
				break;
			case 1:
				this.winOrLoseLabel = new JLabel("You lose"); 
				break;
			case 2:
				this.winOrLoseLabel = new JLabel("You win"); 
				break;
		}		
		
		JLabel nbEnemiesDeadLabel = new JLabel("Number of enemies dead : ", SwingConstants.LEFT); 
		JLabel nbEnemyLivingLabel = new JLabel("Number of enemies living : ", SwingConstants.LEFT); 
		JLabel nbAlliesDeadLabel = new JLabel("Number of allies killed : ", SwingConstants.LEFT); 
		JLabel nbAlliesLivingLabel = new JLabel("Number of allies living : ", SwingConstants.LEFT); 
		this.valueNbEnemiesDeadLabel = new JLabel(String.valueOf(NbEnemiesDead), SwingConstants.CENTER); 
		this.valueNbEnemyLivingLabel = new JLabel(String.valueOf(NbEnemyLiving), SwingConstants.CENTER); 
		this.valueNbAlliesDeadLabel = new JLabel(String.valueOf(NbAlliesDead), SwingConstants.CENTER); 
		this.valueNbAlliesLivingLabel = new JLabel(String.valueOf(NbAlliesLiving), SwingConstants.CENTER); 
		JButton backButton = new JButton("Back to main menu");
		
		backButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.gc();
				for (Window w : Window.getWindows()) {
				    w.dispose();
				}
				controller.lunchViewStart();
			}
		});
			
		infoPanel.setLayout(infoGridlayout);
		globalPanel.setLayout(globalboxLayout);

		infoPanel.add(nbEnemiesDeadLabel, 0);
		infoPanel.add(this.valueNbEnemiesDeadLabel, 1);
		infoPanel.add(nbEnemyLivingLabel, 2);
		infoPanel.add(this.valueNbEnemyLivingLabel, 3);
		infoPanel.add(nbAlliesDeadLabel, 4);
		infoPanel.add(this.valueNbAlliesDeadLabel, 5);
		infoPanel.add(nbAlliesLivingLabel, 6);
		infoPanel.add(this.valueNbAlliesLivingLabel, 7);
		
		winOrLoseLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		backButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		globalPanel.add(winOrLoseLabel);
		globalPanel.add(infoPanel);
		globalPanel.add(backButton);
		
		this.window.add(globalPanel);
		this.window.setResizable(false);
		this.window.setSize(new Dimension(320, 200));
		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);
	}
	
	public void setInformation(int result, int NbEnemiesDead, int NbEnemyLiving, int NbAlliesDead, int NbAlliesLiving) {
		this.valueNbEnemiesDeadLabel.setText(String.valueOf(NbEnemiesDead));  
		this.valueNbEnemyLivingLabel.setText(String.valueOf(NbEnemyLiving));
		this.valueNbAlliesDeadLabel.setText(String.valueOf(NbAlliesDead));
		this.valueNbAlliesLivingLabel.setText(String.valueOf(NbAlliesLiving));
		this.window.setVisible(true);
	}
}
