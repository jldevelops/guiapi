package com.jldevelops.guinote.utils;

import java.util.HashMap;

import com.jldevelops.guinote.core.Partida;


public class Almacen {
	private HashMap<Integer,Partida> alm;
	
	public Almacen(){
		alm = new HashMap<Integer,Partida>();
	}
	
	public Partida nuevaPartida(int idPart,boolean tipoj,int idjug,Object[] nom){
		alm.put(idPart, new Partida(idPart,tipoj,idjug,nom));
		return alm.get(idPart);
	}
	
	public Partida getPartida(int idPart){
		return alm.get(idPart);
	}
	
	public void deletePartida(int idPart){
		alm.put(idPart, null);
		alm.remove(idPart);
	}
}
