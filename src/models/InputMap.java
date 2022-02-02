package models;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import utils.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;


@JsonIgnoreProperties(value = { "buffer" })
/** 
 * Classe qui permet de charger une carte de Bomberman à partir d'un fichier de layout d'extension .lay
 *  @author tanguy
 * 
 */

public class InputMap implements Serializable {
	private static final long serialVersionUID = 1L;
	private String filename;
	private int size_x;
	private int size_y;
	private boolean walls[][];
	private boolean start_breakable_walls[][];
	private ArrayList<InfoAgent> start_agents ;
	private BufferedReader buffer;

	@JsonCreator
	public InputMap() {}

	public InputMap(String filename) throws Exception{
		this.filename = filename;
		try{
			InputStream flux =new FileInputStream(filename);
			InputStreamReader lecture =new InputStreamReader(flux);
			buffer = new BufferedReader(lecture);

			String ligne;

			int nbX=0;
			int nbY=0;

			while ((ligne = buffer.readLine())!=null)
			{
				ligne = ligne.trim();
				if (nbX==0) {nbX = ligne.length();}
				else if (nbX != ligne.length()) throw new Exception("Toutes les lignes doivent avoir la même longueur");
				nbY++;
			}
			buffer.close();

			size_x = nbX;
			size_y = nbY;

			walls = new boolean [size_x][size_y];
			start_breakable_walls  = new boolean [size_x][size_y];

			flux = new FileInputStream(filename);
			lecture = new InputStreamReader(flux);
			buffer = new BufferedReader(lecture);
			int y=0;

			ColorAgent[] color = ColorAgent.values();
			int cpt_col = 0;

			start_agents = new ArrayList<InfoAgent>();

			while ((ligne=buffer.readLine())!=null)
			{
				ligne=ligne.trim();

				for(int x=0;x<ligne.length();x++)
				{

					if (ligne.charAt(x)=='%')
						walls[x][y]=true;

					else walls[x][y]=false;

					if (ligne.charAt(x)=='$')
						start_breakable_walls[x][y]=true;
					else start_breakable_walls[x][y]=false;



					if (ligne.charAt(x)=='E' || ligne.charAt(x)=='V' || ligne.charAt(x)=='R') {
						start_agents.add(new InfoAgent(x,y,AgentAction.STOP,ligne.charAt(x),ColorAgent.DEFAULT,false,false));
					}

					if (ligne.charAt(x)=='B') {
						ColorAgent col;
						if (cpt_col < color.length) col = color[cpt_col];
						else col = ColorAgent.DEFAULT;
						start_agents.add(new InfoAgent(x,y,AgentAction.STOP, ligne.charAt(x),col,false,false));
						cpt_col++;
					}

				}
				y++;
			}
			buffer.close();

			//On verifie que le labyrinthe est clos
			for(int x=0;x<size_x;x++) if (!walls[x][0]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
			for(int x=0;x<size_x;x++) if (!walls[x][size_y-1]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
			for(y=0;y<size_y;y++) if (!walls[0][y]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
			for(y=0;y<size_y;y++) if (!walls[size_x-1][y]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
		
		}catch (Exception e){
			System.out.println("Erreur : "+e.getMessage());
		}
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getSize_x() {
		return size_x;
	}
	public void setSize_x(int size_x) {
		this.size_x = size_x;
	}

	public int getSize_y() {
		return size_y;
	}
	public void setSize_y(int size_y) {
		this.size_y = size_y;
	}

	public boolean[][] getWalls() {
		return walls;
	}
	public void setWalls(boolean[][] walls) {
		this.walls = walls;
	}

	public boolean[][] getStart_breakable_walls() {
		return start_breakable_walls;
	}
	public void setStart_breakable_walls(boolean[][] start_breakable_walls) {
		this.start_breakable_walls = start_breakable_walls;
	}

	public ArrayList<InfoAgent> getStart_agents() {
		return start_agents;
	}
	public void setStart_agents(ArrayList<InfoAgent> start_agents) {
		this.start_agents = start_agents;
	}

	public BufferedReader getBuffer() {
		return buffer;
	}
	public void setBuffer(BufferedReader buffer) {
		this.buffer = buffer;
	}
}