package view;

import controller.*;
import controller.states.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Vue des commandes de la partie
 * @author tanguy
 *
 */
public class ViewCommand implements PropertyChangeListener {
	
	private JLabel pNumberOfTurnJLabel ;
	private AbstractController pAbstractController;
	public JButton restartButton;
	public JButton startButton;
	public JButton nextButton;
	public JButton waitButton;
	private JSlider slider;
	private Etat etat;
	private JFrame window;
	

	public ViewCommand(AbstractController abstractController, String fileName, String niveau) {
		
		this.pAbstractController = abstractController;
		
		// création de l'interface graphique
		window = new JFrame("Command (map : " + fileName + ", level : " + niveau +")");
		
		JPanel globalpanel = new JPanel();
		JPanel highpanel = new JPanel();
		JPanel lowpanel = new JPanel();
		JPanel lowLeftJPanel = new JPanel();
		JPanel lowRightJPanel = new JPanel();
		
		
		GridLayout globalGridlayout = new GridLayout(2, 1);
		GridLayout buttonsGridLayout = new GridLayout(1, 4);
		GridLayout lowGridLayout = new GridLayout(1, 2);
		GridLayout lowLeftGridLayout = new GridLayout(2, 1);
		GridLayout lowRightGridLayout = new GridLayout(1, 1);
		
		// Ajout des icons
		Icon restartIcon= new ImageIcon("icons/icon_restart.png");
		Icon startIcon= new ImageIcon("icons/icon_play.png");
		Icon nextIcon= new ImageIcon("icons/icon_step.png");
		Icon waitIcon= new ImageIcon("icons/icon_pause.png");
		
		// Ajout des boutons avec leur icon
		this.restartButton = new JButton(restartIcon);
		this.restartButton.setEnabled(false);
		this.startButton = new JButton(startIcon);
		this.nextButton = new JButton(nextIcon);	
		this.waitButton = new JButton(waitIcon);
		this.waitButton.setEnabled(false);
		
		this.pNumberOfTurnJLabel = new JLabel("Turn : 0", JLabel.CENTER);
		JLabel sliderJLabel = new JLabel("Number of turns per second", JLabel.CENTER);
		
		this.slider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
		
		window.setSize(new Dimension(800, 400));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int sizeW = (int) screenSize.getWidth() - 800;
		int sizeH = (int) screenSize.getHeight() - 400;
		window.setLocation(sizeW, sizeH);
		
		globalpanel.setLayout(globalGridlayout);
		highpanel.setLayout(buttonsGridLayout);
		lowpanel.setLayout(lowGridLayout);
		globalpanel.add(highpanel);	
		globalpanel.add(lowpanel);
			
		highpanel.add(restartButton);
		highpanel.add(startButton);
		highpanel.add(nextButton);
		highpanel.add(waitButton);
		
		lowLeftJPanel.setLayout(lowLeftGridLayout);
		lowRightJPanel.setLayout(lowRightGridLayout);
		lowpanel.add(lowLeftJPanel);
		lowpanel.add(lowRightJPanel);
		
		lowRightJPanel.add(pNumberOfTurnJLabel);
		lowLeftJPanel.add(sliderJLabel);
		slider.setMajorTickSpacing(1);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		lowLeftJPanel.add(slider);
		
		window.add(globalpanel);
		// Affiche l'interface
		window.setVisible(true);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.etat = new EtatRestart(this);
		
		
		// Ajout des écouteurs d'actions sur les boutons
		this.restartButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				etat.restart();
				pAbstractController.restart();
				pNumberOfTurnJLabel.setText(0 + "");
			}
		});
		
		this.startButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				etat.play();
				pAbstractController.play();
			}
		});
		
		this.nextButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				etat.step();
				pAbstractController.step();
			}
		});
		
		this.waitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				etat.pause();
				pAbstractController.pause();
			}
		});
		
		this.slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				pAbstractController.setSpeed(slider.getValue());	
			}
		});
		
	}

	// Mets a jour la valeur du nombre de tours
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if(arg0.getPropertyName() == "pTurn") {
			int value = (int) arg0.getNewValue();
			this.pNumberOfTurnJLabel.setText(value + "");			
		}
	}
	
	public void setEtat(Etat etat) {
		this.etat = etat;
	}
}
