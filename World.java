package game;

public class Tools extends Item {
	boolean usedTool = false;
	public Tools(String n, Boolean c) {
		super(n, c);
	}
	public void use() {
		if (Game.getLocation().getName().equals("Bedroom")) {
			if (usedTool) {
				Game.print("It's already opened");
			} else {
				Game.getLocation().getExit(1).setLocked(false);
				usedTool = true;
				Game.print("You used the Office Key already. Door is fully open leading to the office");
			}
		} else
			Game.print("You can't use it here");
	}
}
