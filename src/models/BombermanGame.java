package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
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
/**
 * 
 * @author tanguy
 * Classe qui stocke les informations sur une partie et fait jouer les différents agents
 */
public class BombermanGame extends Game {

	private InputMap pInputMap;
	private boolean pBreakable_walls[][];
	private ArrayList<Agent> pListBombermanAgent;
	private ArrayList<Agent> pListBombermanEnemy;
	private ArrayList<InfoBomb> pListBomb;
	private ArrayList<InfoItem> pListItems;
	private ControllerBombermanGame pController;
	private int pNiveau;
	private boolean pManual;

	/**
	 * Initialise les différents attributs
	 * @param maxturn : Nombre de tours maximum dans la partie
	 * @param inputMap : La map qui contient les données
	 * @param niveau : Le niveau de la partie
	 * @param controller : le contrôleur qui gère ce modèle
	 * @param manual : Booléen indiquant si la partie est effectué en mode manuel par l'utilisateur
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

	/**
	 * Renvoie les informations de fin de partie
	 */
	@Override
	public void gameOver() {
		int countAgent = 0;
		int countEnemy = 0;
		
		for(Agent agent : this.pListBombermanAgent) {
			if(agent.getLiving()) countAgent = countAgent + 1;
		}
		for(Agent agent : this.pListBombermanEnemy) {
			if(agent.getLiving()) countEnemy = countEnemy + 1;
		}	
		
		if(countAgent == 0 && countEnemy == 0) {
			// égalité
			this.pController.lunchViewEnd(0, this.pListBombermanEnemy, this.pListBombermanAgent);
		}
		else if(countAgent == 0) {
			// perdu
			this.pController.lunchViewEnd(1, this.pListBombermanEnemy, this.pListBombermanAgent);
		}else if(countEnemy == 0) {
			// gagné
			this.pController.lunchViewEnd(2, this.pListBombermanEnemy, this.pListBombermanAgent);
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
			pSupport.firePropertyChange("pGame", null, this);
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
			pSupport.firePropertyChange("pGame", null, this);
			if (bomb.getStateBomb() == StateBomb.Boom) {
				destroyWall(bomb.getX(), bomb.getY(), bomb.getRange());
				for(MyIterator it = new AgentIterator(this.pListBombermanEnemy) ; it.hasNext() ;) {
					Agent agent = (Agent) it.next();
					int posXagent = agent.getAgent().getX();
					int posYagent = agent.getAgent().getY();
					for (int i = 0; i <= agent.getRange(); i++) {
						if (posXagent == bomb.getX()) {
							if ((posYagent == (bomb.getY() + i)) || (posYagent == (bomb.getY() - i))) {
								agent.setLiving(false);
								pSupport.firePropertyChange("pGame", null, this);
							}
						} else if (posYagent == bomb.getY()) {
							if ((posXagent == (bomb.getX() + i)) || (posXagent == (bomb.getX() - i))) {
								agent.setLiving(false);
								pSupport.firePropertyChange("pGame", null, this);
							}
						}
					}
				}
			}
		}
		
		for(MyIterator iterator = new AgentIterator(this.pListBombermanAgent) ; iterator.hasNext() ;) {
			Agent agent = (Agent) iterator.next();
			updateEtatAgent(agent);
			if (agent.getStrategy().isBlockOff(agent, this) && agent.getSkullFor() <= 0 && !BombHere(agent.getAgent().getX(), agent.getAgent().getY()))
				putBomb(agent.getAgent().getX(), agent.getAgent().getY(), agent);
			else {
				AgentAction action = null;
				boolean next = true;
				while (next) {
					action = agent.getStrategy().generateAction(agent, this);
					if (action == AgentAction.PUT_BOMB && !BombHere(agent.getAgent().getX(), agent.getAgent().getY())) {
						putBomb(agent.getAgent().getX(), agent.getAgent().getY(), agent);
						next = false;
					} else if (isLegalMove(agent, action)) {
						moveAgent(agent, action);
						AgentWalksOnItem(agent);
						next = false;
					}
				}
				pSupport.firePropertyChange("pGame", null, this);
			}
		}
		
		for(MyIterator iterator = new AgentIterator(this.pListBombermanEnemy) ; iterator.hasNext() ;) {		
			Agent agent = (Agent) iterator.next();
			updateEtatAgent(agent);
			if (!agent.getStrategy().isBlockOff(agent, this)) {
				AgentAction action = null;
				boolean next = true;
				while (next) {
					action = agent.getStrategy().generateAction(agent, this);
					if (isLegalMove(agent, action)) {
						EnemyOrAgentIsHereThenKill(agent, action);
						moveAgent(agent, action);
						AgentWalksOnItem(agent);
						next = false;
					}		

				}
				pSupport.firePropertyChange("pGame", null, this);
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
		pSupport.firePropertyChange("pGame", null, this);
	}

	/**
	 * Indique si la partie est finie ou non
	 */
	@Override
	public boolean gameContinue() {
		int countAgent = 0;
		int countEnemy = 0;
		
		for(Agent agent : this.pListBombermanAgent) {
			if(agent.getLiving()) countAgent = countAgent + 1;
		}
		for(Agent agent : this.pListBombermanEnemy) {
			if(agent.getLiving()) countEnemy = countEnemy + 1;
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
		((BombermanAgent)agent).setAtion(action);
	}

	/**
	 * @return La inputmap utilisée
	 */
	public InputMap getInputMap() {
		return this.pInputMap;
	}

	/**
	 * 
	 * @return Les murs cassables
	 */
	public boolean[][] getBreakable_walls() {
		return this.pBreakable_walls;
	}

	/**
	 * 
	 * @return La liste des agents alliées comme ennemies
	 */
	public ArrayList<InfoAgent> getListAgent() {
		ArrayList<InfoAgent> listAgent = new ArrayList<InfoAgent>();
		for (Agent agent : this.pListBombermanAgent) {
			if(agent.getLiving()) listAgent.add(agent.getAgent());
		}
		for (Agent agent : this.pListBombermanEnemy) {
			if(agent.getLiving()) listAgent.add(agent.getAgent());
		}
		return listAgent;
	}

	/**
	 * 
	 * @return La liste des bombes
	 */
	public ArrayList<InfoBomb> getListBomb() {
		return this.pListBomb;
	}

	/**
	 * 
	 * @return la liste des items sur la map
	 */
	public ArrayList<InfoItem> getListItems() {
		return this.pListItems;
	}

	/**
	 * Fonction qui détermine si une action est possible
	 * @param agent : Agent sur qui on souhaite effectuer une action 
	 * @param action : L'action que l'on souhaite faire
	 * @return Retourne vrai si cette action est possible
	 */
	public boolean isLegalMove(Agent agent, AgentAction action) {
		int coordX = agent.getAgent().getX();
		int coordY = agent.getAgent().getY();
		switch (action) {
		case MOVE_DOWN:
			if ((coordY + 1) < (this.pInputMap.getSizeY() - 1)) {
				if (agent.getAgent().getType() == 'V')
					return true;
				else if(this.pInputMap.get_walls()[coordX][coordY + 1])
					return false;
				else if(agent.getAgent().getType() == 'R' && !this.pBreakable_walls[coordX][coordY + 1])
					return true;
				else if(EnemyHere(coordX, coordY + 1))
					return false; 
				else if(agent.getAgent().getType() == 'B' && AllyHere(coordX, coordY + 1))
					return false;
				else if(!this.pBreakable_walls[coordX][coordY + 1])
					return true;
				else
					return false;
			} else
				return false;
		case MOVE_LEFT:
			if ((coordX - 1) > 0) {
				if (agent.getAgent().getType() == 'V')
					return true;
				else if(this.pInputMap.get_walls()[coordX - 1][coordY])
					return false;
				else if(agent.getAgent().getType() == 'R' && !this.pBreakable_walls[coordX - 1][coordY])
					return true;
				else if(EnemyHere(coordX - 1, coordY))
					return false; 
				else if(agent.getAgent().getType() == 'B' && AllyHere(coordX - 1, coordY))
					return false;
				else if (!this.pBreakable_walls[coordX - 1][coordY])
					return true;
				else
					return false;
			} else
				return false;
		case MOVE_RIGHT:
			if ((coordX + 1) < (this.pInputMap.getSizeX() - 1)) {
				if (agent.getAgent().getType() == 'V')
					return true;
				else if(this.pInputMap.get_walls()[coordX + 1][coordY])
					return false;
				else if(agent.getAgent().getType() == 'R' && !this.pBreakable_walls[coordX + 1][coordY])
					return true;
				else if(EnemyHere(coordX + 1, coordY))
					return false; 
				else if(agent.getAgent().getType() == 'B' && AllyHere(coordX + 1, coordY))
					return false;
				else if (!this.pBreakable_walls[coordX + 1][coordY])
					return true;
				else
					return false;
			} else
				return false;
		case MOVE_UP:
			if ((coordY - 1) > 0) {
				if (agent.getAgent().getType() == 'V')
					return true;
				else if(this.pInputMap.get_walls()[coordX][coordY - 1])
					return false;
				else if(!this.pBreakable_walls[coordX][coordY - 1] && agent.getAgent().getType() == 'R')
					return true;
				else if(EnemyHere(coordX, coordY - 1))
					return false; 
				else if(agent.getAgent().getType() == 'B' && AllyHere(coordX, coordY - 1))
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
		if(agent.getSkullFor() <= 0) this.pListBomb.add(new InfoBomb(coordX, coordY, agent.getRange(), StateBomb.Step0));
	}

	/**
	 * Regarde si un agent ou un ennemi est là et élimine le
	 * @param AgentEnemy : Agent qui fait l'action
	 * @param action : Action que l'on fait
	 */
	public void EnemyOrAgentIsHereThenKill(Agent AgentEnemy, AgentAction action) {
		int coordX = AgentEnemy.getAgent().getX();
		int coordY = AgentEnemy.getAgent().getY();

		switch (action) {
		case MOVE_DOWN:
			coordY = AgentEnemy.getAgent().getY() + 1;
			break;
		case MOVE_LEFT:
			coordX = AgentEnemy.getAgent().getX() - 1;
			break;
		case MOVE_RIGHT:
			coordX = AgentEnemy.getAgent().getX() + 1;
			break;
		case MOVE_UP:
			coordY = AgentEnemy.getAgent().getY() - 1;
			break;
		default:
			break;
		}

		for(MyIterator iterator = new AgentIterator(this.pListBombermanAgent) ; iterator.hasNext() ;) {
			Agent agent = (Agent) iterator.next();
			if (agent.getAgent().getX() == coordX && agent.getAgent().getY() == coordY && agent.getInvincibleFor() <= 0) {
				agent.setLiving(false);
				break;
			}
		}
		if(AgentEnemy.getAgent().getType() == 'R') {	
			for(MyIterator iterator = new AgentIterator(this.pListBombermanEnemy) ; iterator.hasNext() ;) {
					Agent agent = (Agent) iterator.next();
					if(agent.getAgent().getX() == coordX && agent.getAgent().getY() == coordY && agent.getInvincibleFor() <= 0 && agent != AgentEnemy) {
						agent.setLiving(false);
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
	 * @param range : Porté de la bombe
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
			if (item.getX() == agent.getAgent().getX() && item.getY() == agent.getAgent().getY()) {
				switch (item.getType()) {
				case FIRE_UP:
					if (agent.getRange() < 5) agent.setRange(agent.getRange() + 1);
					break;
				case FIRE_DOWN:
					if (agent.getRange() > 1) agent.setRange(agent.getRange() - 1);
					break;
				case FIRE_SUIT:
					agent.getEtat().invincible();
					break;
				case SKULL:
					agent.getEtat().skull();
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
		agent.setInvincibleFor(agent.getInvincibleFor() - 1);
		agent.setSkullFor(agent.getSkullFor() - 1);
		if (agent.getInvincibleFor() <= 0 && agent.getSkullFor() <= 0)
			agent.getEtat().withoutEffects();
		pSupport.firePropertyChange("pGame", null, this);
	}
	
	/**
	 * 
	 * @return Une liste d'agents ennemie
	 */
	public ArrayList<Agent> getListAgentEnemy(){
		return this.pListBombermanEnemy;
	}
	/**
	 * 
	 * @return Une liste d'agents alliée
	 */
	public ArrayList<Agent> getListAgentBomberman(){
		return this.pListBombermanAgent;
	}
	
	/**
	 * 
	 * @param coordX : Coordonnées X d'un agent
	 * @param coordY : Coordonnées Y d'un agent
	 * @return Vrai si un agent est est présent aux coordonnées
	 */
	public boolean EnemyHere(int coordX, int coordY) {
		for(MyIterator iterator = new AgentIterator(this.pListBombermanEnemy) ; iterator.hasNext() ;) {
			Agent agent = (Agent) iterator.next();
			if(agent.getAgent().getX() == coordX && agent.getAgent().getY() == coordY) {
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
			if(agent.getAgent().getX() == coordX && agent.getAgent().getY() == coordY) {
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
}
