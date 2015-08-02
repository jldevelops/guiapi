package com.jldevelops.guinote.core;

import java.io.Serializable;




public class Carta implements Serializable{

	private static final long serialVersionUID = -3072057561982949886L;
	private char palo;
	private int numero;
	Carta(char palo, int numero){
		this.palo = palo;
		this.numero = numero;
	}

	public char getPalo() {
		return palo;
	}
	
	
	/* devuelve las cartas del 0 al 9 siendo 0 la de menor valor (2)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return (this.numero-1)+""+this.palo;
	}
	
	public boolean isGuinote(){
		if(numero == 9 || numero == 10)
			return true;
		return false;
	}


	public int getPuntos(){
		switch(numero){
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
		return numero;
	}
	//cambia los valores de esta carta con los de la carta pasada
	//usado solo para permitir cambiar el 7 en la ultima robada
	void intercambiarValor(Carta c){
		char p = this.palo;
		int n = this.numero;
		this.palo = c.palo;
		this.numero = c.numero;
		c.palo = p;
		c.numero = n;
	}

}
