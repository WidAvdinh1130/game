package game;

public class OfficeKey extends Item {
	boolean usedTool = false;
	public OfficeKey(String n, Boolean c) {
		super(n, c);
	}
	public void use() {
		if (Game.getLocation().getName().equals("Living Room")) {
			if (usedTool) {
				Game.print("Office is already opened");
			} else {
				Game.getLocation().getExit(4).setLocked(false);
				usedTool = true;
				Game.print("You used the tool. The trapdoor turned into stairs to climb up to.");
			}
		} else
			Game.print("You can't use it here");
	}
}