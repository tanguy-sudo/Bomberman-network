package models;

import java.util.ArrayList;
import models.agent.Agent;
/**
 * 
 * @author tanguy
 * Classe qui créé un itérator sur une liste d'agents
 */
public class AgentIterator implements MyIterator{
	private ArrayList<Agent> pListBomberman;
	private int position;
	
	/**
	 * Initialise une liste d'agents
	 * @param listBomberman : Liste d'agents
	 */
	public AgentIterator(ArrayList<Agent> listBomberman) {
		this.pListBomberman = listBomberman;
		this.position = 0;
	}

	/**
	 * Retourne vrai si le prochain agent est vivant
	 */
	@Override
	public boolean hasNext() {
		// on regarde si l'agent existe;
        if (position >= this.pListBomberman.size() || this.pListBomberman.get(position) == null) return false;
        // on regarde si l'agent est vivant
        else if (this.pListBomberman.get(position).getLiving()) return true;
        else {
        	// si l'agent n'est pas vivant on regarde l'élement suivant
        	while(position < this.pListBomberman.size()) {
        		position = position + 1;
        		if(position >= this.pListBomberman.size() || this.pListBomberman.get(position) == null) return false;
        		else if(this.pListBomberman.get(position).getLiving()) return true; 
        	}
        }
		return false;
	}

	/**
	 * Retourne le prochain agent
	 */
	@Override
	public Object next() {
		Agent agent = this.pListBomberman.get(position);
        position = position + 1;
		return agent;
	}

}
