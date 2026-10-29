package game;

import java.util.HashMap;

public class World {
	public static HashMap<String,Room> houseData = new HashMap<>();
	public static Room buildWorld() {
		Room livingRoom = new Room("Living Room", false);
		Room office = new Room("Office", true);
		Room kitchen = new Room("Kitchen", false);
		Room bedroom = new Room("Bedroom", false);
		Room attic = new Room("Attic", true);
		Room garage = new Room("Garage", true);
		
		Item music_box = new Item("music box", true);
		Item flashlight = new Item("flashlight", true);
		Item knife = new Item("knife", true);
		Item mysterious_orb = new Item("mysterious orb", true);
		Item tools = new Tools("tools", true);
		Item office_key = new OfficeKey("Office Key", true);
		Item safe = new Item("safe", false);
		Item couch = new Item("couch", false);
		Item coffee_table = new Item("coffee_table", false);

		livingRoom.addItem(music_box);
		kitchen.addItem(flashlight);
		kitchen.addItem(knife);
		kitchen.addItem(mysterious_orb);
		bedroom.addItem(office_key);
		office.addItem(tools);
		attic.addItem(safe);
		livingRoom.addItem(couch);
		livingRoom.addItem(coffee_table);
		
		livingRoom.addExit(kitchen, 0);
		kitchen.addExit(livingRoom, 1);
		livingRoom.addExit(office, 1);
		office.addExit(livingRoom, 0);
		kitchen.addExit(garage, 3);
		garage.addExit(kitchen, 2);
		livingRoom.addExit(bedroom, 2);
		bedroom.addExit(livingRoom, 3);
		bedroom.addExit(attic, 4);
		attic.addExit(bedroom, 5);
		
		return livingRoom;
	}
}