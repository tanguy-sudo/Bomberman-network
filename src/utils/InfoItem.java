package utils;

import org.json.JSONObject;

public class InfoItem {
	private int x;
	private int y;
	private ItemType type;


	public InfoItem(JSONObject j) {
		this.x=j.getInt("x");
		this.y=j.getInt("y");
		this.type = ItemType.valueOf(j.getString("type"));
	}

	public InfoItem(int x, int y, ItemType type) {
		this.x=x;
		this.y=y;
		this.type=type;
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

	public ItemType getType() {
		return type;
	}
	public void setType(ItemType type) {
		this.type = type;
	}

	public JSONObject toJSON() {
		JSONObject j = new JSONObject();
		j.put("x", this.x);
		j.put("y", this.y);
		j.put("type", this.type);
		return j;
	}

}
	