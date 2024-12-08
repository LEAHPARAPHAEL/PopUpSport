package launcher;

import backend.DatabaseManagerH2;
import ui.GUI;

public class Main 
{
	
	public static void main(String[] args)
	{
		System.out.println("jm");
		DatabaseManagerH2 databaseManagerH2 = new DatabaseManagerH2();
		GUI clientUI = new GUI(databaseManagerH2);
	}
}
