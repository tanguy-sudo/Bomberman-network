package utils;

import org.json.*;

public class InfoAgent{
	private int x;
	private int y;
	private AgentAction agentAction;
	private ColorAgent color;
	private char type;
	private boolean isInvincible;
	private boolean isSick;

	public InfoAgent(JSONObject j) {
		this.x=j.getInt("x");
		this.y=j.getInt("y");
		this.agentAction = AgentAction.valueOf(j.getString("agentAction"));
		this.color = ColorAgent.valueOf(j.getString("color"));;
		this.type = j.getString("type").charAt(0);
		this.isInvincible = j.getBoolean("invincible");
		this.isSick = j.getBoolean("sick");;

	}
	public InfoAgent(int x, int y, AgentAction agentAction, char type, ColorAgent color, boolean isInvincible, boolean isSick) {
		this.x=x;
		this.y=y;
		this.agentAction = agentAction;
		this.color = color;
		this.type = type;
		this.isInvincible = isInvincible;
		this.isSick = isSick;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public AgentAction getAgentAction() {
		return agentAction;
	}
	public void setAgentAction(AgentAction agentAction) {
		this.agentAction = agentAction;
	}
	
	public ColorAgent getColor() {
		return color;
	}
	public void setColor(ColorAgent color) {
		this.color = color;
	}

	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}

	public boolean isInvincible() {
		return isInvincible;
	}
	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}

	public boolean isSick() {
		return isSick;
	}
	public void setSick(boolean isSick) {
		this.isSick = isSick;
	}

	public JSONObject toJSON() {
		JSONObject j = new JSONObject();
		j.put("x", this.x);
		j.put("y", this.y);
		j.put("agentAction", this.agentAction);
		j.put("color", this.color);
		j.put("type", this.type + "");
		j.put("isInvincible", this.isInvincible);
		j.put("isSick", this.isSick);
		return j;
	}
}
	