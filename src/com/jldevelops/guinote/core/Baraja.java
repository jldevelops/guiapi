package com.jldevelops.guinote.core;

import java.io.Serializable;
import java.util.Random;


public class Baraja implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4478857872186762496L;
	private Carta[] b;
	private int cartaActual = 0;
	
	/**
	 * @param d false= no se mezcla  true= se mezcla
	 */
	Baraja(boolean d){ 

		b = new Carta[40];
		char palo = 'o';
		for(int i = 0;i<40;i++){
			if(palo == 'o'){
				if(i+1>10){
					palo = 'c';
				}
				else{
					b[i] = new Carta(palo,i+1);
				}
				
			}
			if(palo == 'c'){
				if(i+1>20){
					palo = 'e';
				}
				else{
					b[i] = new Carta(palo,i+1-10);
				}
				
			}
			if(palo == 'e'){
				if(i+1>30){
					palo = 'b';
				}
				else{
					b[i] = new Carta(palo,i+1-20);
				}
				
			}
			if(palo == 'b'){
				if(i+1>40){
					System.out.println("oh oh");
				}
				else{
					b[i] = new Carta(palo,i+1-30);
				}
				
			}
		}
		//printBaraja();
		if(d)
			mezclar(this);
	}
	
	
	public String toString(){
		String s = "";
		for(int i = 0; i<40;i++){
			s += getMazo()[i].getPalo()+getMazo()[i].getNumero()+" ";
		}
		
		return s;
		
	}

	public void printBaraja(){
		for(int i = 0; i<40;i++){
			System.out.println(b[i].getNumero()+" "+b[i].getPalo()+" "+i);
		}
	}
	
	private void mezclar(Baraja b){
		Random r = new Random(System.currentTimeMillis());
		Carta tmp;
		int n1,n2;
		for(int i = 0; i<1000; i++){
			n1 = r.nextInt(40);
			n2 = r.nextInt(40);
			tmp = b.b[n1];
			b.b[n1] = b.b[n2];
			b.b[n2] = tmp;
		}
	}
	
	Carta sacarCarta(){
		if(cartaActual<40){
			cartaActual++;
			//System.out.println("Carta num "+cartaActual);
			return b[cartaActual-1];
		}
		else
			return null;
	}
	
	Carta[] getMazo() {
		return b;
	}
	
	
	public int getCartaActual() {
		return cartaActual;
	}
	
}
