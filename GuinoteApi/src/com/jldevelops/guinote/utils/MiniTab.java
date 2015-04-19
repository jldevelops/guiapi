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

	public MiniTab(Jugador jugador, int[] puntuaciones, boolean[][] cantes,
			int idJugActual, int haempezado, char palotriunfo,
			Carta[] cartasMesa, int ultBaza, boolean juegoDe4) {
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
	}
	
	
	public Jugador getJugador() {
		return j;
	}
	public int[] getPuntuaciones() {
		return p;
	}
	public boolean[][] getCantes() {
		return c;
	}
	public int getIdJugActual() {
		return i;
	}
	public int getHaempezado() {
		return h;
	}
	public char getPalotriunfo() {
		return pt;
	}
	public Carta[] getCartasMesa() {
		return me;
	}
	public int getUltBaza() {
		return u;
	}
	public boolean isJuegoDe4() {
		return jd;
	}
	
	

}
