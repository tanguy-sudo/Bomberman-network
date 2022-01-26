package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.fasterxml.jackson.annotation.*;
import models.agent.BombermanAgent;
import models.agent.Agent;
import models.agent.FabriqueBomberman;
import models.agent.FabriqueEnemy;
import utils.AgentAction;
import utils.InfoAgent;
import utils.InfoBomb;
import utils.InfoItem;
import utils.ItemType;
import utils.StateBomb;
import controller.ControllerBombermanGame;

@JsonIgnoreProperties(value = { "pController" })

/**
 * 
 * @author tanguy
 * Classe qui stocke les informations sur une partie et fait jouer les différents agents
 */
public class BombermanGame extends Game {
	@JsonProperty("pInputMap")
	private InputMap pInputMap;
	@JsonProperty("pBreakable_walls")
	private boolean pBreakable_walls[][];
	@JsonProperty("pListBombermanAgent")
	private ArrayList<Agent> pListBombermanAgent;
	@JsonProperty("pListBombermanEnemy")
	private ArrayList<Agent> pListBombermanEnemy;
	@JsonProperty("pListBomb")
	private ArrayList<InfoBomb> pListBomb;
	@JsonProperty("pListItems")
	private ArrayList<InfoItem> pListItems;
	@JsonProperty("pController")
	private ControllerBombermanGame pController;
	@JsonProperty("pNiveau")
	private int pNiveau;
	@JsonProperty("pManual")
	private boolean pManual;

	/**
	 * Initialise les différents attributs
	 * @param inputMap : La map qui contient les données
	 * @param niveau : Le niveau de la partie
	 * @param controller : le contrôleur qui gère ce module
	 * @param manual : Bool�en indiquant si la partie est effectué en mode manuel par l'utilisateur
	 */
	public BombermanGame(int maxturn, InputMap inputMap, int niveau, ControllerBombermanGame controller, boolean manual) {
		super(maxturn);
		this.pInputMap = inputMap;
		this.pBreakable_walls = inputMap.getStart_breakable_walls();
		this.pListBombermanAgent = new ArrayList<Agent>();
		this.pListBombermanEnemy = new ArrayList<Agent>();
		this.pListBomb = new ArrayList<InfoBomb>();
		this.pListItems = new ArrayList<InfoItem>();
		this.pNiveau = niveau;
		this.pController = controller;
		this.pManual = manual;
	}

	@JsonCreator
	public BombermanGame(){}

	/**
	 * Renvoie les informations de fin de partie
	 */
	@Override
	public void gameOver() {
		int countAgent = 0;
		int countEnemy = 0;
		
		for(Agent agent : this.pListBombermanAgent) {
			if(agent.getpLiving()) countAgent = countAgent + 1;
		}
		for(Agent agent : this.pListBombermanEnemy) {
			if(agent.getpLiving()) countEnemy = countEnemy + 1;
		}
	}

	/**
	 * Redémarre une partie
	 */
	@Override
	public void restart(String mapName) {
		try {
			this.pInputMap = new InputMap(mapName);
			this.pBreakable_walls = this.pInputMap.getStart_breakable_walls();
			this.pListBombermanAgent.clear();
			this.pListBombermanEnemy.clear();
			this.pListBomb.clear();
			this.pListItems.clear();
			// Préviens l'observer des changements d'informations
			//pSupport.firePropertyChange("pGame", null, this);
		}catch(Exception e) {
			
		}
	}

	/**
	 * Effectue un tour pour chacun des bombermans et des monstres ennemis, et mets à jour l'état des bombes
	 */
	@Override
	public void takeTurn() {

		for (Iterator<InfoBomb> iterator = this.pListBomb.iterator(); iterator.hasNext();) {
			InfoBomb bomb = iterator.next();
			if (bomb.getStateBomb() == StateBomb.Boom) {
				iterator.remove();
				break;
			}
			bomb.setStateBomb(nextState(bomb.getStateBomb()));
			//pSupport.firePropertyChange("pGame", null, this);
			if (bomb.getStateBomb() == StateBomb.Boom) {
				destroyWall(bomb.getX(), bomb.getY(), bomb.getRange());
				for(MyIterator it = new AgentIterator(this.pListBombermanEnemy) ; it.hasNext() ;) {
					Agent agent = (Agent) it.next();
					int posXagent = agent.getpInfoAgent().getX();
					int posYagent = agent.getpInfoAgent().getY();
					for (int i = 0; i <= agent.getpRange(); i++) {
						if (posXagent == bomb.getX()) {
							if ((posYagent == (bomb.getY() + i)) || (posYagent == (bomb.getY() - i))) {
								agent.setpLiving(false);
								//pSupport.firePropertyChange("pGame", null, this);
							}
						} else if (posYagent == bomb.getY()) {
							if ((posXagent == (bomb.getX() + i)) || (posXagent == (bomb.getX() - i))) {
								agent.setpLiving(false);
								//pSupport.firePropertyChange("pGame", null, this);
							}
						}
					}
				}
			}
		}
		
		for(MyIterator iterator = new AgentIterator(this.pListBombermanAgent) ; iterator.hasNext() ;) {
			Agent agent = (Agent) iterator.next();
			updateEtatAgent(agent);
			if (agent.getpStrategy().isBlockOff(agent, this) && agent.getpSkullFor() <= 0 && !BombHere(agent.getpInfoAgent().getX(), agent.getpInfoAgent().getY()))
				putBomb(agent.getpInfoAgent().getX(), agent.getpInfoAgent().getY(), agent);
			else {
				AgentAction action = null;
				boolean next = true;
				while (next) {
					action = agent.getpStrategy().generateAction(agent, this);
					if (action == AgentAction.PUT_BOMB && !BombHere(agent.getpInfoAgent().getX(), agent.getpInfoAgent().getY())) {
						putBomb(agent.getpInfoAgent().getX(), agent.getpInfoAgent().getY(), agent);
						next = false;
					} else if (isLegalMove(agent, action)) {
						moveAgent(agent, action);
						AgentWalksOnItem(agent);
						next = false;
					}
				}
				//pSupport.firePropertyChange("pGame", null, this);
			}
		}
		
		for(MyIterator iterator = new AgentIterator(this.pListBombermanEnemy) ; iterator.hasNext() ;) {		
			Agent agent = (Agent) iterator.next();
			updateEtatAgent(agent);
			if (!agent.getpStrategy().isBlockOff(agent, this)) {
				AgentAction action = null;
				boolean next = true;
				while (next) {
					action = agent.getpStrategy().generateAction(agent, this);
					if (isLegalMove(agent, action)) {
						EnemyOrAgentIsHereThenKill(agent, action);
						moveAgent(agent, action);
						AgentWalksOnItem(agent);
						next = false;
					}		

				}
				//pSupport.firePropertyChange("pGame", null, this);
			}
		}

	}

	/**
	 * Initialise une partie en fabriquant les différents Bombermans et monstres
	 */
	@Override
	public void initializeGame() {
		FabriqueEnemy fabriqueEnemy = new FabriqueEnemy();
		FabriqueBomberman fabriqueBomberman = new FabriqueBomberman();
		boolean first = true;
		for (InfoAgent infoAgent : this.pInputMap.getStart_agents()) {
			if (infoAgent.getType() == 'B') {
				if(first) {
					this.pListBombermanAgent.add(fabriqueBomberman.createAgent(infoAgent, this.pNiveau, this.pManual));
					first = false;
				} else this.pListBombermanAgent.add(fabriqueBomberman.createAgent(infoAgent, this.pNiveau, false));
			} else {
				this.pListBombermanEnemy.add(fabriqueEnemy.createAgent(infoAgent, this.pNiveau, this.pManual));
			}
		}
		//pSupport.firePropertyChange("pGame", null, this);
	}

	/**
	 * Indique si la partie est finie ou non
	 */
	@Override
	public boolean gameContinue() {
		int countAgent = 0;
		int countEnemy = 0;
		
		for(Agent agent : this.pListBombermanAgent) {
			if(agent.getpLiving()) countAgent = countAgent + 1;
		}
		for(Agent agent : this.pListBombermanEnemy) {
			if(agent.getpLiving()) countEnemy = countEnemy + 1;
		}	

		if(countAgent == 0 || countEnemy == 0) return false;
		else return true;
	}
	
	/**
	 * Mets à jour l'action de l'utilisateur dans la classe bombermanAgent
	 */
	@Override
	public void updateActionUser(AgentAction action) {
		Agent agent = this.pListBombermanAgent.get(0);
		((BombermanAgent)agent).setpActionUtilisateur(action);
	}

	/**
	 * 
	 * @return La liste des agents alliées comme ennemies
	 */
	public ArrayList<InfoAgent> fusionListAgent() {
		ArrayList<InfoAgent> listAgent = new ArrayList<InfoAgent>();
		for (Agent agent : this.pListBombermanAgent) {
			if(agent.getpLiving()) listAgent.add(agent.getpInfoAgent());
		}
		for (Agent agent : this.pListBombermanEnemy) {
			if(agent.getpLiving()) listAgent.add(agent.getpInfoAgent());
		}
		return listAgent;
	}

	/**
	 * Fonction qui détermine si une action est possible
	 * @param agent : Agent sur qui on souhaite effectuer une action 
	 * @param action : L'action que l'on souhaite faire
	 * @return Retourne vrai si cette action est possible
	 */
	public boolean isLegalMove(Agent agent, AgentAction action) {
		int coordX = agent.getpInfoAgent().getX();
		int coordY = agent.getpInfoAgent().getY();
		switch (action) {
		case MOVE_DOWN:
			if ((coordY + 1) < (this.pInputMap.getSize_y() - 1)) {
				if (agent.getpInfoAgent().getType() == 'V')
					return true;
				else if(this.pInputMap.getWalls()[coordX][coordY + 1])
					return false;
				else if(agent.getpInfoAgent().getType() == 'R' && !this.pBreakable_walls[coordX][coordY + 1])
					return true;
				else if(EnemyHere(coordX, coordY + 1))
					return false; 
				else if(agent.getpInfoAgent().getType() == 'B' && AllyHere(coordX, coordY + 1))
					return false;
				else if(!this.pBreakable_walls[coordX][coordY + 1])
					return true;
				else
					return false;
			} else
				return false;
		case MOVE_LEFT:
			if ((coordX - 1) > 0) {
				if (agent.getpInfoAgent().getType() == 'V')
					return true;
				else if(this.pInputMap.getWalls()[coordX - 1][coordY])
					return false;
				else if(agent.getpInfoAgent().getType() == 'R' && !this.pBreakable_walls[coordX - 1][coordY])
					return true;
				else if(EnemyHere(coordX - 1, coordY))
					return false; 
				else if(agent.getpInfoAgent().getType() == 'B' && AllyHere(coordX - 1, coordY))
					return false;
				else if (!this.pBreakable_walls[coordX - 1][coordY])
					return true;
				else
					return false;
			} else
				return false;
		case MOVE_RIGHT:
			if ((coordX + 1) < (this.pInputMap.getSize_x() - 1)) {
				if (agent.getpInfoAgent().getType() == 'V')
					return true;
				else if(this.pInputMap.getWalls()[coordX + 1][coordY])
					return false;
				else if(agent.getpInfoAgent().getType() == 'R' && !this.pBreakable_walls[coordX + 1][coordY])
					return true;
				else if(EnemyHere(coordX + 1, coordY))
					return false; 
				else if(agent.getpInfoAgent().getType() == 'B' && AllyHere(coordX + 1, coordY))
					return false;
				else if (!this.pBreakable_walls[coordX + 1][coordY])
					return true;
				else
					return false;
			} else
				return false;
		case MOVE_UP:
			if ((coordY - 1) > 0) {
				if (agent.getpInfoAgent().getType() == 'V')
					return true;
				else if(this.pInputMap.getWalls()[coordX][coordY - 1])
					return false;
				else if(!this.pBreakable_walls[coordX][coordY - 1] && agent.getpInfoAgent().getType() == 'R')
					return true;
				else if(EnemyHere(coordX, coordY - 1))
					return false; 
				else if(agent.getpInfoAgent().getType() == 'B' && AllyHere(coordX, coordY - 1))
					return false;
				else if (!this.pBreakable_walls[coordX][coordY - 1])
					return true;
				else
					return false;
			} else
				return false;
		case STOP:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Modifie les coordonnées d'un agent
	 * @param agent : Agent sur qui on souhaite effectuer une action 
	 * @param action : L'action que l'on souhaite faire
	 */
	public void moveAgent(Agent agent, AgentAction action) {
		if (action != AgentAction.PUT_BOMB && action != AgentAction.STOP) {
			agent.setMove(action);
		}
	}

	/**
	 * Pose une bombe si c'est possible
	 * @param coordX : Coordonnées X de la bombe
	 * @param coordY : Coordonnées Y de la bombe
	 * @param agent : Agent qui souhaite poser une bombe
	 */
	public void putBomb(int coordX, int coordY, Agent agent) {
		if(agent.getpSkullFor() <= 0) this.pListBomb.add(new InfoBomb(coordX, coordY, agent.getpRange(), StateBomb.Step0));
	}

	/**
	 * Regarde si un agent ou un ennemi est là et élimine le
	 * @param AgentEnemy : Agent qui fait l'action
	 * @param action : Action que l'on fait
	 */
	public void EnemyOrAgentIsHereThenKill(Agent AgentEnemy, AgentAction action) {
		int coordX = AgentEnemy.getpInfoAgent().getX();
		int coordY = AgentEnemy.getpInfoAgent().getY();

		switch (action) {
		case MOVE_DOWN:
			coordY = AgentEnemy.getpInfoAgent().getY() + 1;
			break;
		case MOVE_LEFT:
			coordX = AgentEnemy.getpInfoAgent().getX() - 1;
			break;
		case MOVE_RIGHT:
			coordX = AgentEnemy.getpInfoAgent().getX() + 1;
			break;
		case MOVE_UP:
			coordY = AgentEnemy.getpInfoAgent().getY() - 1;
			break;
		default:
			break;
		}

		for(MyIterator iterator = new AgentIterator(this.pListBombermanAgent) ; iterator.hasNext() ;) {
			Agent agent = (Agent) iterator.next();
			if (agent.getpInfoAgent().getX() == coordX && agent.getpInfoAgent().getY() == coordY && agent.getpInvincibleFor() <= 0) {
				agent.setpLiving(false);
				break;
			}
		}
		if(AgentEnemy.getpInfoAgent().getType() == 'R') {
			for(MyIterator iterator = new AgentIterator(this.pListBombermanEnemy) ; iterator.hasNext() ;) {
					Agent agent = (Agent) iterator.next();
					if(agent.getpInfoAgent().getX() == coordX && agent.getpInfoAgent().getY() == coordY && agent.getpInvincibleFor() <= 0 && agent != AgentEnemy) {
						agent.setpLiving(false);
						break;
					}
				}		
		}
	}

	/**
	 * Retourne le prochain état d'une bombe
	 * @param state : Etat actuel d'une bombe
	 * @return Le prochain état d'une bombe
	 */
	public StateBomb nextState(StateBomb state) {
		switch (state) {
		case Step0:
			return StateBomb.Step1;
		case Step1:
			return StateBomb.Step2;
		case Step2:
			return StateBomb.Step3;
		case Step3:
			return StateBomb.Boom;
		case Boom:
			return StateBomb.Boom;
		}
		return state;
	}

	/**
	 * Détruit les murs cassables
	 * @param coordX : Coordonnées X de la bombe
	 * @param coordY: Coordonnées Y de la bombe
	 * @param range : Porter de la bombe
	 */
	public void destroyWall(int coordX, int coordY, int range) {
		for (int i = 0; i <= range; i++) {
			if (coordY + i < this.pBreakable_walls[coordX].length && this.pBreakable_walls[coordX][coordY + i]) {
				this.pBreakable_walls[coordX][coordY + i] = false;
				generateItem(coordX, coordY + i);
			}
			if (coordY - i > 0 && this.pBreakable_walls[coordX][coordY - i]) {
				this.pBreakable_walls[coordX][coordY - i] = false;
				generateItem(coordX, coordY - i);
			}
		}
		for (int i = 0; i <= range; i++) {
			if (coordX + i < this.pBreakable_walls.length && this.pBreakable_walls[coordX + i][coordY]) {
				this.pBreakable_walls[coordX + i][coordY] = false;
				generateItem(coordX + i, coordY);
			}
			if (coordX - i > 0 && this.pBreakable_walls[coordX - i][coordY]) {
				this.pBreakable_walls[coordX - i][coordY] = false;
				generateItem(coordX - i, coordY);
			}
		}
	}

	/**
	 * Génère un item aux coordonnées d'un mur cassé
	 * @param coordX : Coordonnées X de l'item
	 * @param coordY: Coordonnées Y de l'item
	 */
	public void generateItem(int coordX, int coordY) {
		Random r = new Random();
		switch (r.nextInt(2)) {
		case 0:
			this.pListItems.add(new InfoItem(coordX, coordY, ramdomItem()));
			break;
		}

	}

	/**
	 * 
	 * @return Aléatoirement un item
	 */
	public ItemType ramdomItem() {
		Random r = new Random();
		switch (r.nextInt(4)) {
		case 0:
			return ItemType.FIRE_DOWN;
		case 1:
			return ItemType.FIRE_SUIT;
		case 2:
			return ItemType.FIRE_UP;
		case 3:
			return ItemType.SKULL;
		}
		return null;
	}

	/**
	 * Ajout a un agent l'effet d'un item sur lequel il marche
	 * @param agent : Agent qui marche sur un item
	 */
	public void AgentWalksOnItem(Agent agent) {
		for (Iterator<InfoItem> iterator = this.pListItems.iterator(); iterator.hasNext();) {
			InfoItem item = iterator.next();
			if (item.getX() == agent.getpInfoAgent().getX() && item.getY() == agent.getpInfoAgent().getY()) {
				switch (item.getType()) {
				case FIRE_UP:
					if (agent.getpRange() < 5) agent.setpRange(agent.getpRange() + 1);
					break;
				case FIRE_DOWN:
					if (agent.getpRange() > 1) agent.setpRange(agent.getpRange() - 1);
					break;
				case FIRE_SUIT:
					agent.getpEtat().invincible();
					break;
				case SKULL:
					agent.getpEtat().skull();
					break;
				}
				iterator.remove();
				break;
			}
		}
	}

	/**
	 * Mets à jour l'état d'un agent
	 * @param agent : Agent qu'il faut mettre à jour
	 */
	public void updateEtatAgent(Agent agent) {
		agent.setpInvincibleFor(agent.getpInvincibleFor() - 1);
		agent.setpSkullFor(agent.getpSkullFor() - 1);
		if (agent.getpInvincibleFor() <= 0 && agent.getpSkullFor() <= 0)
			agent.getpEtat().withoutEffects();
		//pSupport.firePropertyChange("pGame", null, this);
	}
	
	/**
	 * 
	 * @param coordX : Coordonnées X d'un agent
	 * @param coordY : Coordonnées Y d'un agent
	 * @return Vrai si un agent est est pr�sent aux coordonnées
	 */
	public boolean EnemyHere(int coordX, int coordY) {
		for(MyIterator iterator = new AgentIterator(this.pListBombermanEnemy) ; iterator.hasNext() ;) {
			Agent agent = (Agent) iterator.next();
			if(agent.getpInfoAgent().getX() == coordX && agent.getpInfoAgent().getY() == coordY) {
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * 
	 * @param coordX : Coordonnées X d'un agent
	 * @param coordY : Coordonnées Y d'un agent
	 * @return Vrai si un agent est est présent aux coordonnées
	 */
	public boolean AllyHere(int coordX, int coordY) {
		for(MyIterator iterator = new AgentIterator(this.pListBombermanAgent) ; iterator.hasNext() ;) {
			Agent agent = (Agent) iterator.next();
			if(agent.getpInfoAgent().getX() == coordX && agent.getpInfoAgent().getY() == coordY) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param coordX : Coordonnées X d'une bombe
	 * @param coordY : Coordonnées Y d'une bombe
	 * @return Vrai si une bombe est est présent aux coordonnées
	 */
	public boolean BombHere(int coordX, int coordY) {
		for(InfoBomb bomb : this.pListBomb) {
			if(bomb.getX() == coordX && bomb.getY() == coordY) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return La inputmap utilisée
	 */
	public InputMap getpInputMap() {
		return this.pInputMap;
	}

	/**
	 * @return Les murs cassables
	 */
	public boolean[][] getpBreakable_walls() {
		return this.pBreakable_walls;
	}

	/**
	 * @return Une liste d'agents ennemie
	 */
	public ArrayList<Agent> getpListBombermanEnemy(){
		return this.pListBombermanEnemy;
	}

	/**
	 * @return Une liste d'agents alliée
	 */
	public ArrayList<Agent> getpListBombermanAgent(){
		return this.pListBombermanAgent;
	}

	/**
	 * @return La liste des bombes
	 */
	public ArrayList<InfoBomb> getpListBomb() {
		return this.pListBomb;
	}

	/**
	 * @return la liste des items sur la map
	 */
	public ArrayList<InfoItem> getpListItems() {
		return this.pListItems;
	}

	public ControllerBombermanGame getpController() {return pController;}

	public int getpNiveau() {return pNiveau;}

	public boolean ispManual() {return pManual;}

	public void setpInputMap(InputMap pInputMap) {this.pInputMap = pInputMap;}

	public void setpBreakable_walls(boolean[][] pBreakable_walls) {
		this.pBreakable_walls = pBreakable_walls;
	}

	public void setpListBombermanAgent(ArrayList<Agent> pListBombermanAgent) {
		this.pListBombermanAgent = pListBombermanAgent;
	}

	public void setpListBombermanEnemy(ArrayList<Agent> pListBombermanEnemy) {
		this.pListBombermanEnemy = pListBombermanEnemy;
	}

	public void setpListBomb(ArrayList<InfoBomb> pListBomb) {
		this.pListBomb = pListBomb;
	}

	public void setpListItems(ArrayList<InfoItem> pListItems) {
		this.pListItems = pListItems;
	}

	public void setpController(ControllerBombermanGame pController) {
		this.pController = pController;
	}

	public void setpNiveau(int pNiveau) {
		this.pNiveau = pNiveau;
	}

	public void setpManual(boolean pManual) {
		this.pManual = pManual;
	}
}
