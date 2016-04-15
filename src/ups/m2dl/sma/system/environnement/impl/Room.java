package ups.m2dl.sma.system.environnement.impl;

import java.util.HashMap;
import java.util.Map;

import ups.m2dl.sma.system.environnement.interfaces.IgetRoom;
import ups.m2dl.sma.system.environnement.interfaces.IsetRoom;

public class Room implements  IgetRoom,IsetRoom{

	private int id;
	private Map<Integer,String> creneauList;
	
	public Room(int id) {
		super();
		this.id = id;
		creneauList = new HashMap<Integer,String>();
		for(int i = 1; i<5; i ++){
			creneauList.put(i, "vide");
		}
	}

	@Override
	public void setRoom(int creneau, String idProf) {
		creneauList.replace(creneau, idProf);
	}

	@Override
	public Room getRoom() {
		// TODO Auto-generated method stub
		return this;
	}
	
	 

}
