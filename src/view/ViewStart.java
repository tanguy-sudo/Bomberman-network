package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter; 
import controller.ControllerBombermanGame;
/**
 * Vue de d�but de partie
 * @author tanguy
 *
 */
public class ViewStart {
	public JFrame window;
	public JLabel listMapLabel;
	public String filePath;
	public JComboBox<String> listLevel;
	public String fileName;
	public JCheckBox manualCheckBox;
	
	public ViewStart(ControllerBombermanGame controller) {
	
		JFrame window = new JFrame("Bomberman");
		JPanel globalpanel = new JPanel();
		
		GridLayout globalGridlayout = new GridLayout(6, 1);
		
		String s1[] = { "level 1", "level 2" };	
		JButton buttonfileChooser = new JButton("Click to choose a map");
		listMapLabel = new JLabel("File : any", SwingConstants.CENTER); 
		listLevel = new JComboBox<String>(s1);
		JLabel listLevelLabel = new JLabel("Choose a level", SwingConstants.CENTER); 
	    this.manualCheckBox = new JCheckBox("the manual mode"); 
		JButton button = new JButton("validate");
			
		globalpanel.setLayout(globalGridlayout);

		globalpanel.add(listLevelLabel);
		globalpanel.add(listLevel);
		globalpanel.add(listMapLabel);
		globalpanel.add(buttonfileChooser);
		globalpanel.add(manualCheckBox);
		globalpanel.add(button);
		window.add(globalpanel);
		window.setResizable(false);
		button.setEnabled(false);
		((JLabel)listLevel.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		manualCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
		
		buttonfileChooser.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser("./layouts/");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.lay", "lay");
				fileChooser.setFileFilter(filter);
				int returnValue = fileChooser.showSaveDialog(null);
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String tabFile[] = selectedFile.getAbsolutePath().split("\\\\");
					fileName = tabFile[tabFile.length-1];
					String extension = fileName.split("\\.")[1];
					if(extension.contains("lay")) {
						filePath = selectedFile.getAbsolutePath();
						listMapLabel.setText("File : " + fileName);
						button.setEnabled(true);
					}else {
						listMapLabel.setText("File : any");
						button.setEnabled(false);
					}
				}
			}
		});
			
		button.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.lunchGame(filePath, listLevel.getSelectedItem().toString().split(" ")[1], fileName, manualCheckBox.isSelected());
				window.setVisible(false);
			}
		});
		
		window.setSize(new Dimension(250, 220));
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
