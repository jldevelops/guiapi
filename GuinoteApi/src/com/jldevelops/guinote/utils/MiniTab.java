package com.jldevelops.guinote.utils;

import java.io.Serializable;

import com.jldevelops.guinote.core.Carta;
import com.jldevelops.guinote.core.Jugador;

public class MiniTab implements Serializable {
	
	private static final long serialVersionUID = 8L;
	final private Jugador j;
	final private int[] p;
	final private boolean[][] c;
	/**
	 * id del jugador que le toca hablar
	 */
	final private int i;
	final private int h;
	final private char pt;
	
	/**
	 * en la posicion 0 SIEMPRE estara la carta del jugador que ha sido el primero en tirar.
	 */
	final private Carta[] me;
	/**
	 * id del jugador que ha hecho la ultima baza
	 */
	final private int u;
	final private boolean jd;
	final private int cr;
	private String[] nj;

	public MiniTab(Jugador jugador, int[] puntuaciones, boolean[][] cantes,
			int idJugActual, int haempezado, char palotriunfo,
			Carta[] cartasMesa, int ultBaza, boolean juegoDe4,int cr) {
		super();
		this.j = jugador;
		this.p = puntuaciones;
		this.c = cantes;
		this.i = idJugActual;
		this.h = haempezado;
		this.pt = palotriunfo;
		this.me = cartasMesa;
		this.u = ultBaza;
		this.jd = juegoDe4;
		this.cr = cr;
	}
	
	
	public Jugador getJugador() {
		return j;
	}
	public boolean isCantado(char palo, int idjug) {
		return c[aQueBaza(idjug)][Utils.paloInt(palo)];
	}
	
	public String getNombreJugador(int idjug){
		return nj[idjug];
	}
	
	public void setNj(Object[] nom){
		nj = new String[jd?4:2];
		for(int i = 0;i<nom.length;i++){
			nj[i] = (String) nom[i];
		}
	}

	public int getTurno() {
		return i;
	}
	public int getHaempezado() {
		return h;
	}
	public char getPalotriunfo() {
		return pt;
	}
	public int getUltBaza() {
		return u;
	}
	public boolean isJuegoDe4() {
		return jd;
	}
	
	public int getCartasRestantes(){
		return cr;
	}
	
	private int aQueBaza(int idjug) {
		if (jd) {
			if (idjug == 0 || idjug == 1) {
				return idjug;
			} else {
				if (idjug == 2) {
					return 0;
				} else {
					return 1;
				}
			}
		} else {
			return idjug;
		}
	}
	
	public int getPuntuacion(int idjug) {
		return p[aQueBaza(idjug)];
	}
	
	/** devuelve el id del jugador siguiente (el de la derecha) a idjug
	 * @param idjug ID del jugador
	 * @return int
	 */
	public int s(int idjug) {


		if (jd) {
			if (idjug == 3) {
				return 0;
			} else {
				 idjug++;
				return idjug;
			}
		} else {
			if (idjug == 0) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	/** Devuelve la carta de la mesa del jugador idjug
	 * @param idjug
	 * @return Carta
	 */
	public Carta getCartaMesa(int idjug){
		int ultBaza1 = u;
		if(u == -1)
			ultBaza1 = h;
		if (ultBaza1 == idjug)
			return me[0];
		else {
			if (s(ultBaza1) == idjug)
				return me[1];
			else {
				if (s(s(ultBaza1)) == idjug)
					return me[2];
				else
					return me[3];
			}
		}
	}
}
