package com.jldevelops.guinote.utils;

public class GuiJson {
	
	private String[] msgs;
	
	public GuiJson(){
		msgs = new String[4];
		borrarMsgs();
	}

	public GuiJson(boolean tipoJuego){
		if(tipoJuego)
			msgs = new String[4];
		else
			msgs = new String[2];
		borrarMsgs();
	}
	
	public String getMsg(int idjug) {
		return msgs[idjug];
	}
	public void setMsg(String msg,int idjug) {
		this.msgs[idjug] = msg;
	}
	public void borrarMsgs(){
		for(int i = 0;i<msgs.length;i++){
			msgs[i] = "";
		}
	}
	
	
}
