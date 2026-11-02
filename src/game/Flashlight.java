package game;

public class Flashlight extends Item{
	private boolean foundKey;
	public Flashlight() {
		super("flashlight", false);
		foundKey=false;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void use() {
		if(Game.getLocation().getName().equals("Bedroom")) {
			if(foundKey) {
				Game.print("you can't find anything beause it is too dark");
			}
			else {
				Game.print("You are no longer blind");
			}
		}
	else {
		Game.print("You can't use it here");
		}
	}
}
