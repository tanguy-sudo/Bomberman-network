package view;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Vue pour tester l'interface graphique
 * @author tanguy
 *
 */
public class ViewSimpleGame {
	public ViewSimpleGame() {
		
		// création de l'interface graphique
		JFrame window = new JFrame("Game");
		
		JPanel Globalpanel = new JPanel();
		
		GridLayout GlobalGridlayout = new GridLayout(1, 1);
		
		JLabel NumberOfTurnJLabel = new JLabel("Turn : 5", JLabel.CENTER);
		
		Globalpanel.setLayout(GlobalGridlayout);
		Globalpanel.add(NumberOfTurnJLabel);
		
		window.setSize(new Dimension(500, 500));
		window.add(Globalpanel);
		window.setLocationRelativeTo(null);
		// affiche l'interface
		window.setVisible(true);

	}
}
