package com.jldevelops.guinote.core;

import java.io.Serializable;




public class Carta implements Serializable{

	private static final long serialVersionUID = -3072057561982949886L;
	private char p;
	private int n;
	Carta(char palo, int numero){
		this.p = palo;
		this.n = numero;
	}

	public char getPalo() {
		return p;
	}
	
	
	/* devuelve las cartas del 0 al 9 siendo 0 la de menor valor (2)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return (this.n-1)+""+this.p;
	}
	
	public boolean isGuinote(){
		if(n == 9 || n == 10)
			return true;
		return false;
	}


	public int getPuntos(){
		switch(n){
		case 10:
			return 11;
		case 9:
			return 10;
		case 8:
			return 4;
		case 7:
			return 3;
		case 6:
			return 2;
		default:
			return 0;
		}
	}

	public int getNumero() {
		return n;
	}
	//cambia los valores de esta carta con los de la carta pasada
	//usado solo para permitir cambiar el 7 en la ultima robada
	void intercambiarValor(Carta c){
		char p = this.p;
		int n = this.n;
		this.p = c.p;
		this.n = c.n;
		c.p = p;
		c.n = n;
	}

}
