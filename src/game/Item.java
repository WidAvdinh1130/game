package game;

import java.io.Serializable;

public class Item implements Serializable{
    private String name;
    private boolean carry;
	//constructor
	public Item(String n, boolean c) {
		name = n;
		carry = c;
	}
    	public String getName(){
		return name;
	}
    	public void setName(String n){
		name = n;
	}
    	public void use() {
    		Game.print("You cannot use this item stupid");
    	}
    	public boolean canCarry(){
    		return carry;
    	}
    	public String toString() {
		String s = "";
		s += name;
		return s;
	}
}