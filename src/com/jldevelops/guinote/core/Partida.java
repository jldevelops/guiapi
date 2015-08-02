
package com.jldevelops.guinote.core;

import java.io.Serializable;
import java.util.ArrayList;

import com.jldevelops.guinote.utils.GuiJson;
import com.jldevelops.guinote.utils.MiniTab;
import com.jldevelops.guinote.utils.Utils;


/**
 * Clase que graba toda la partida;
 * Una partida es un listado de tableros hasta que su propiedad terminada se pone a true
 * cuando alguno de los jugadores alcanza más de 100 puntos. 
 * Cuando se hayan tirado todas las cartas se debe invocar a ganadorDeLaJugada()
 * para continuar.
 * 
 * 
 * @author jorge
 */
public class Partida implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8183297397472435252L;
	final private ArrayList<Tablero> tabs;
	final private int id;
	private int ganadorDesemp=-1;//ganador de la jugada si ambos superan 51 buenas
	//si no -1
	private Tablero tabActual;
	private String[] noms;
	private GuiJson js;
	private final boolean tipoPartida;
	/**
	 * indica que la partida ya está acabada
	 */
	private boolean terminada;
	private boolean deVueltas;
	/**
	 * @param id 
	 * @param tipoDeJuego true=4jug false=2jug
	 * @param idjug id del jugador que empieza a tirar
	 * @param nom array de string con los nombres de los jugadores
	 */
	public Partida(int id,boolean tipoDeJuego,int idjug,Object[] nom){
		this.id = id;
		js = new GuiJson(tipoDeJuego);
		tipoPartida = tipoDeJuego;
		tabs = new ArrayList<Tablero>();
		tabActual = new Tablero(tipoDeJuego,idjug,null);
		noms = new String[tipoPartida?4:2];
		if(nom != null){
			for(int i = 0; i<(tipoPartida?4:2);i++){
				tabActual.getJug(i).setNombre((String)nom[i]);
				noms[i] = (String)nom[i];
			}
		}
		
	}
	
	public boolean is4Jug(){
		return tipoPartida;
	}

	public ArrayList<Tablero> getTabs() {
		return tabs;
	}

	public int getId() {
		return id;
	}

	public Tablero getTabActual() {
        return tabActual;
	}

	public String getTabActualJSON(){
		return Utils.GSON.toJson(tabActual);
	}
	
	public String[] getMiniTabsJSON(){
		String[] tm = new String[tipoPartida?4:2];
		for(int i = 0;i<tm.length;i++){
			MiniTab m = tabActual.toMiniTab(i);
			m.setNj(noms);
			tm[i] = Utils.GSON.toJson(m);
		}
		return tm;
	}
	
	public String getPartidaJSON(){
		return Utils.GSON.toJson(tabs);
	}

	public boolean isTerminada(){
		return terminada;
	}

	public boolean isDeVueltas(){
		return deVueltas;
	}

	/**
	 * Realiza una acción de un jugador sobre el tablero
	 * devuelve objeto json con mensajes asociados a los jugadores correspondientes
	 * @param idAcc id de la acción:
	 * 0=tirar carta(acompañada del string que representa a la carta)
	 * 1=cantar
	 * 2=cambiar7
	 * 3=cambiar carta de sitio
	 * @param params parametros de la acción
	 * @param idjug id del jugador
	 * @return GuiJson con los mensajes correspondientes a cada jugador
	 */
	public GuiJson accionJug(int idAcc,String params,int idjug){
		synchronized(tabActual){
			String retorno = null;
			js.borrarMsgs();
			if(idAcc >= 0 && idAcc <= 3 && idjug >= 0 && idjug <= (tipoPartida?3:1)){
				switch(idAcc){
				case 0:
					if(params != null){
						int idcarta = Integer.parseInt(params.charAt(0)+"");
						if(idcarta <= 5 && idcarta >= 0){
							retorno = tabActual.tirarCarta(idjug, idcarta);
							if(retorno != null)
								js.setMsg(retorno, idjug);
						}
					}
					break;
				case 1:
					boolean b = false;
					if(idjug == tabActual.getUltBaza() || (tipoPartida && tabActual.getUltBaza() == tabActual.s(tabActual.s(idjug)))){
						retorno = tabActual.compruebaMarcaCantes(idjug);
						if(retorno.equals(""))
							js.setMsg("No tienes cantes", idjug);
						else{
							for(int i = 0; i<retorno.length();i++){
								if(retorno.charAt(i) != 'n'){
									if(b){
										if(retorno.charAt(i) == tabActual.getPalotriunfo()){
											js.setMsg(js.getMsg(idjug)+" y las 40", idjug);
											js.setMsg(js.getMsg(tabActual.s(idjug))+" y las 40", tabActual.s(idjug));

                                            if(tipoPartida){
                                                js.setMsg(js.getMsg(tabActual.s(tabActual.s(idjug)))+" y las 40", tabActual.s(tabActual.s(idjug)));
                                                js.setMsg(js.getMsg(tabActual.s(tabActual.s(tabActual.s(idjug))))+" y las 40", tabActual.s(tabActual.s(tabActual.s(idjug))));
                                            }
										}
										else{
											js.setMsg(js.getMsg(idjug)+" y las 20 en "+Utils.paloStr(retorno.charAt(i)), idjug);
											js.setMsg(js.getMsg(tabActual.s(idjug))+" y las 20 en "+Utils.paloStr(retorno.charAt(i)), tabActual.s(idjug));

                                            if(tipoPartida){
                                                js.setMsg(js.getMsg(tabActual.s(tabActual.s(idjug)))+" y las 20 en "+Utils.paloStr(retorno.charAt(i)), tabActual.s(tabActual.s(idjug)));
                                                js.setMsg(js.getMsg(tabActual.s(tabActual.s(tabActual.s(idjug))))+" y las 20 en "+Utils.paloStr(retorno.charAt(i)), tabActual.s(tabActual.s(tabActual.s(idjug))));
                                            }
										}
									}
									else{
										if(retorno.charAt(i) == tabActual.getPalotriunfo()){
											js.setMsg("Has cantado las 40", idjug);
											js.setMsg(tabActual.getJug(idjug).getNombre()+" ha cantado las 40", tabActual.s(idjug));
                                            if(tipoPartida){
                                                js.setMsg(tabActual.getJug(idjug).getNombre()+" ha cantado las 40", tabActual.s(tabActual.s(idjug)));
                                                js.setMsg(tabActual.getJug(idjug).getNombre()+" ha cantado las 40", tabActual.s(tabActual.s(tabActual.s(idjug))));
                                            }

										}
										else{
											js.setMsg("Has cantado las 20 en "+Utils.paloStr(retorno.charAt(i)), idjug);
											js.setMsg(tabActual.getJug(idjug).getNombre()+" ha cantado las 20 en "+Utils.paloStr(retorno.charAt(i)), tabActual.s(idjug));

										    if(tipoPartida){
                                                js.setMsg(tabActual.getJug(idjug).getNombre()+" ha cantado las 20 en "+Utils.paloStr(retorno.charAt(i)), tabActual.s(tabActual.s(idjug)));
                                                js.setMsg(tabActual.getJug(idjug).getNombre()+" ha cantado las 20 en "+Utils.paloStr(retorno.charAt(i)), tabActual.s(tabActual.s(tabActual.s(idjug))));
                                            }
                                        }
										b = true;
									}
								}
							}
						}
					}
					else{
						js.setMsg("Necesitas baza reciente", idjug);
					}
					break;
				case 2:
					retorno = tabActual.cambiar7(idjug);
					js.setMsg(retorno, idjug);
					if(retorno.charAt(retorno.length()-1) == '.'){
						js.setMsg(tabActual.getJug(idjug).getNombre()+" ha cambiado el 7", tabActual.s(idjug));
						if(tipoPartida){
							js.setMsg(tabActual.getJug(idjug).getNombre()+" ha cambiado el 7", tabActual.s(tabActual.s(idjug)));
							js.setMsg(tabActual.getJug(idjug).getNombre()+" ha cambiado el 7", tabActual.s(tabActual.s(tabActual.s(idjug))));
						}
					}
					break;
				case 3:
					if(params.contains(","))
						tabActual.getJug(idjug).intercambiar(Integer.parseInt(params.charAt(0)+""), Integer.parseInt(params.charAt(2)+""));
					break;

				}
				if(Utils.GUARDAR_PARTIDAS){
					if(!tabs.isEmpty()){
						if(!tabs.get(tabs.size()-1).equals(getTabActual())){
							if(idAcc != 3)
								tabs.add(getTabActual().clone());
						}
					}
					else{
						tabs.add(getTabActual().clone());
					}
				}
				if(deVueltas){
					if(tabActual.getPuntuacion(0) > 100 || tabActual.getPuntuacion(1) > 100)
						terminada = true;
				}
				return js;
			}
			return js;
		}
	}
	
	public String accionJugJSON(int idAcc,String params,int idjug){
		return Utils.GSON.toJson(accionJug(idAcc,params,idjug));
	}
	
	/**
	 * si el tablero actual cambia lo guarda
	 */
    private void guardar(){
    	if(Utils.GUARDAR_PARTIDAS){
			if(!tabs.isEmpty()){
				if(tabs.get(tabs.size()-1).equals(getTabActual())){
					return;
				}
				tabs.add(getTabActual().clone());
			}
			else{
				tabs.add(getTabActual().clone());
			}
    	}
	}

	/**llamar cuando estén todas (2 o 4) las cartas tiradas;
	 * decide el idJug del ganador y lo devuelve, 
	 * recoge las cartas a su respectiva baza,
	 * y hace robar las cartas del mazo.
	 * si la partida ha terminado la marca como terminada o crea un nuevo tablero
	 * si va de vueltas
	 * 
	 * @return idJug del ganador de la jugada
	 */
	public int ganadorDeLaJugada(){
        synchronized (tabActual) {
            int t = tabActual.ganadorDeLaJugada();
            guardar();
            if (tabActual.isFin()) {
                if (tabActual.getPuntuacion(0) < 101 && tabActual.getPuntuacion(1) < 101) {
                    //de vueltas
                    int[] punt = {tabActual.getPuntuacion(0), tabActual.getPuntuacion(1)};
                    tabActual = new Tablero(tipoPartida, tabActual.s(t), punt);
                    for(int i = 0; i<(tipoPartida?4:2);i++){
        				tabActual.getJug(i).setNombre(noms[i]);
        			}
                    deVueltas = true;
                    guardar();
                } else{
                	if(tabActual.getPuntuacion(0) > 100 && tabActual.getPuntuacion(1) > 100)
                		ganadorDesemp = t;
                	terminada = true;
                }
                    
            }
            if (deVueltas) {
                if (tabActual.getPuntuacion(0) > 100 || tabActual.getPuntuacion(1) > 100)
                    terminada = true;
            }
            return t;
        }
	}

	public String msgToJson(GuiJson msg){
		return Utils.GSON.toJson(msg);
	}
	
	public int getTurno(){
		return tabActual.getTurno();
	}
	
	public Jugador getJug(int idjug){
		return tabActual.getJug(idjug);
	}
	
	public boolean todasCartasTiradas(){
		return tabActual.todasCartasTiradas();
	}
	
	public String compruebaCarta(int idjug,int carta){
		return tabActual.compruebaCarta(idjug, carta);
	}
	
	//pre: terminada=true
	//pos: true si gana el jug/equipo 0   false si gana el jug/equipo 1
	public boolean getGanador(){
		if(ganadorDesemp == -1)
			return tabActual.getPuntuacion(0) > 101;
		else{
			if(ganadorDesemp == 0 || ganadorDesemp == 2)
				return true;
			else return false;
		}
	}
	
	
	/**
	 * Decide que carta tirar del jugador actual
	 */
	public int getCartaCPU(){
		Jugador j = tabActual.getJug(tabActual.getTurno());
		for(int i = 5;i<=0;i--){
			if(tabActual.compruebaCarta(j.getIdJug(), i) == null)
				return i;
		}
		return -1;
	}

}
