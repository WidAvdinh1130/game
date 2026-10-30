package game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game {
	private static Room location = World.buildWorld();
	private static ArrayList<Item> inventory = new ArrayList<Item>();
	public static HashMap<String,String> house = new HashMap<>();
	private static HashMap<String,Integer> dirTracker = new HashMap<>();

	public static void runGame() {
		PrintFileDemo();
		print("hello");
		print(location);
		Scanner input = new Scanner(System.in);
		String command = "";
		while(!command.equals("exit")) {
			
			System.out.println("Enter command");
			command = input.nextLine();
			String multcommand[]= command.split(" ");
			//ill  do at home
			if(!command.equals("exit")) {
				String[] dirs = {"east", "west", "north", "south", "up", "down"};
				if(command.split(" ")[0].equals("go")) {
					for(String cardin:dirs) {
						if(multcommand[1].equals(cardin)) {
							move(command.split(" ")[1]);
						}
					}
				}
				if(command.equals("i")) {
					print("your are carrying:");
					if(inventory.size() != 0)
					for(Item item:inventory) {
						print(item);
					}
					else {
						print("nothing");
					}
				}
				else if(command.equals("take")) {
					print("take what");
				}
				else if(multcommand[0].equals("take") && multcommand.length >= 2) {
					String itemName = "";
					for(int i = 1; i < multcommand.length; i++) {
						if(i < multcommand.length - 1)
							itemName += multcommand[i] + " ";
						else {
							itemName += multcommand[i];
						}
					}
					takeItem(itemName);
				}
				
				else if(multcommand[0].equals("use") && multcommand.length >= 2) {
					String itemName = "";
					for(int i = 1; i < multcommand.length; i++) {
						if(i < multcommand.length - 1)
							itemName += multcommand[i] + "_";
						else {
							itemName += multcommand[i];
						}
					}
					//inventory(itemName).use();
				}
				
				else if (command.equals("load")) {
					print("file loaded");
					loadGame();
				}
				else if (command.equals("save")) {
					print("file saved");
					saveGame();
				}
				
				print(location);
				
			}
			//System.out.println(command);
		}
		input.close();
		print("goodbye");
	}
	private static void saveGame() {
		try {
			File saveFile = new File("saveData0.sav");
			saveFile.createNewFile();
			ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(saveFile));
			outStream.writeObject(location);
			outStream.writeObject(inventory);
			outStream.writeObject(World.houseData);
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void loadGame() {
		try {
			File saveFile = new File("saveData0.sav");
			ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(saveFile));
			location = (Room) inStream.readObject();
			inventory = (ArrayList<Item>) inStream.readObject();
			World.houseData = (HashMap<String, Room>) inStream.readObject();
			inStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void move(String dir) {
		if(location.getExit(dirTracker.get(dir)).getLocked() == true) {
			print("it is locked, find something to unlock it");
		}
		else if(dir.equals("east") && location.getExit(0)!=null) {
			location = location.getExit(0);
		}
		else if(dir.equals("west") && location.getExit(1)!=null) {
			location = location.getExit(1);
		}
		else if(dir.equals("north") && location.getExit(2)!=null) {
			location = location.getExit(2);
		}
		else if(dir.equals("south") && location.getExit(3)!=null) {
			location = location.getExit(3);
		}
		else if(dir.equals("up") && location.getExit(4)!=null) {
			location = location.getExit(4);
		}
		else if(dir.equals("down") && location.getExit(5)!=null) {
			location = location.getExit(5);
		}
		else{
			print("you can't go there");
		}
	}
	public static void takeItem(String itemName) {
		if(location.getItem(itemName) != null) {
			if(itemName.equals(location.getItem(itemName).toString())){
				if(location.getItem(itemName).canCarry() == true) {
					inventory.add(location.getItem(itemName));
					print("you take the " + location.getItem(itemName).getName() + " with you");
					location.removeItem(itemName);	
				}
				else {
					print("you can't carry " + location.getItem(itemName).getName());
				}
			}
			else {
				print("you look for the " + itemName + " but didn't find it");
			}
			}
		else{
			print("there is nothing to take");
		}
	}
	public static void PrintFileDemo() {
		try {
			Scanner fileScan = new Scanner(new File("description.txt"));
			while(fileScan.hasNextLine()) {
				String Line = fileScan.nextLine();
				String theRoom = Line;
				String theDesc = "";
				Line = fileScan.nextLine();
				while(!Line.equals("#")) {
					theDesc += Line + " ";
					Line = fileScan.nextLine();
				}
				
			house.put(theRoom, theDesc);
			}
			fileScan.close();				
			}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void print(Object obj) {
		System.out.println(obj);
		}
	
	public static Room getLocation() {
		return location;
	}

	public static ArrayList<Item> getInventory() {
		return inventory;
	}

	public static void main(String[] args) {
		dirTracker.put("east",0);
		dirTracker.put("west",1);
		dirTracker.put("north",2);
		dirTracker.put("south",3);
		dirTracker.put("up",4);
		dirTracker.put("down",5);
		runGame();
	}
}