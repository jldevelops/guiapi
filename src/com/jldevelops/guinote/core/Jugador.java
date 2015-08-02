package com.jldevelops.guinote.core;

import java.io.Serializable;

public class Jugador implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2607177138340818103L;
	private final Carta[] mano;
	private final int idJug;
	private String nombre;
	private boolean[] reyes;//reyes que han salido en orden oceb
	private boolean[] sotas;//sotas que han salido en orden oceb


	Jugador(int idjugador){
		mano = new Carta[6];
		idJug = idjugador;
		//reyes = new boolean[4];
		//sotas = new boolean[4];
	}
	
	public int getIdJug() {
		return idJug;
	}
	Carta[] getMano() {
		return mano;
	}
	public Carta getCarta(int idcarta){
		return mano[idcarta];
	}
	void meterCarta(Carta c, char t){
        boolean b = false;
		for(int i = 0; i<mano.length; i++){
            if(b){
                mano[i-1] = mano[i];
            }
			if(mano[i] == null)
				b = true;
		}
        mano[5] = c;
		ordenarMano(t);
	}
	
	private void ordenarMano(char triunfo){
		int tri = 0;
		for(int tr = 0;tr<6;tr++){
			if(mano[tr] != null){
				if(mano[tr].getPalo() == triunfo){
					if(tri < tr){
						intercambiar(tr,tri);
						tri++;
					}
					if(tri == tr)
						tri++;
				}
			}
			
		}
	}
	
	void intercambiar(int carta1,int carta2){
		if(carta1 >= 0 && carta1<=5 && carta2 >= 0 && carta2<=5){
			//el metodo ordena peta si no se comprueban
			//nota para futuro: comprobar origen de petada
			Carta tmp = mano[carta1];
			mano[carta1] = mano[carta2];
			mano[carta2] = tmp;
		}
	}
	
	/**ncarta debe estar entre 0 y 5 ambos inclusive
	 * este getter elimina la carta del vector mano
	 * usar getMano()[i] para leer datos de la carta.
	 * @param ncarta
	 * @return La carta tirada
	 */
	Carta tirarCarta(int ncarta) {
		if(mano[ncarta] != null){
			Carta tmp = mano[ncarta];
			mano[ncarta] = null;
			return tmp;
		}
		else
			return null;
	}

	
	/** no evalua los triunfos
	 * @param c no puede ser null
	 * @return null si ninguna carta puede matar a c,
	 * si no devuelve un string de 0 y 1 con las posiciones que sí pueden
	 */
	String puedeMatar(Carta c){
		String b = "";
		boolean flag = false;
		for(int i = 0; i<6;i++){
			if(mano[i] != null){
				if(c.getPalo() == mano[i].getPalo()){
					if(c.getNumero()< mano[i].getNumero()){
						b += "1";
						flag = true;
					}
					else{
						b += "0";
					}
				}
				else
					b += "0";
			}
			else
				b +="0";
		}
		if(flag){
			return b;
		}
		else
			return null;
	}

    /**
     *
     * @param palo
     * @return null si no tiene el palo pasado, si no devuelve un string de 0 y 1 con las posiciones que tienen mismo palo
     */
    String checkMismoPalo(char palo){
        String b = tieneMismoPalo(palo);
        for(int i = 0; i<b.length();i++){
            if(b.charAt(i) == '1')
                return b;
        }
        return null;
    }
	
	/**num (del 0 al 9) y palo
	 * @param carta
	 * @return la posicion de la carta, si no -1
	 */
	public int numCarta(String carta){
		for(int i = 0; i < mano.length;i++){
			if(mano[i] != null){
				if(mano[i].getNumero()-1 == Integer.parseInt(carta.charAt(0)+"")){
					if(mano[i].getPalo() == carta.charAt(1))
						return i;
				}
			}
		}
		return -1;
	}
	
	/**num (del 0 al 9) y palo
	 * @param carta
	 * @return true si el Jugador tiene la carta
	 */
	public boolean tieneCarta(String carta){
		return numCarta(carta) > -1;
	}


    private String tieneMismoPalo(char palo){
        String b = "";
        for(int i = 0;i<6;i++){
            if(mano[i]!= null){
                if(palo == mano[i].getPalo())
                    b += "1";
                else
                    b += "0";
            }
            else
                b+="0";
        }
        return b;
    }
	
	public boolean sinCartas(){
            for(Carta c:mano){
                if(c != null)
                    return false;
            }
            return true;
        }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int[] paja(char pt){//devuelve un vector con las posiciones

		int k = 0;
		int v[] = new int[6];
		boolean flag = false;
		for(int i = 0; i<6;i++){
			if(mano[i]!= null){
				if(mano[i].getPalo() != pt && mano[i].getNumero()<=5){
					v[k] = i;
					k++;
					flag = true;
				}}
		}
		if(flag){
			int ret[] = new int[k];
			for(int i = 0;i<k;i++){
				ret[i] = v[i];
			}
			return ret;
		}
		else{
			return null;
		}
	}
	public int[] sotasyreyescantados(char pt){//devuelve un vector con las posiciones
		int k = 0;
		int v[] = new int[6];
		boolean flag = false;
		for(int i = 0; i<6;i++){
			if(mano[i]!= null){
				if(mano[i].getPalo() != pt && (mano[i].getNumero()==7 || mano[i].getNumero()==8)){
					switch(mano[i].getPalo()){
					case 'o':
						if(mano[i].getNumero()==7){
							if(reyes[0]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						else{
							if(sotas[0]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						break;
					case 'c':
						if(mano[i].getNumero()==7){
							if(reyes[1]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						else{
							if(sotas[1]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						break;
					case 'e':
						if(mano[i].getNumero()==7){
							if(reyes[2]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						else{
							if(sotas[2]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						break;
					case 'b':
						if(mano[i].getNumero()==7){
							if(reyes[3]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						else{
							if(sotas[3]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						break;
					}
					
				}
			}}
		if(flag){
			int ret[] = new int[k];
			for(int i = 0;i<k;i++){
				ret[i] = v[i];
			}
			return ret;
		}
		else{
			return null;
		}
	}
	public int[] caballos(char pt){//devuelve un vector con las posiciones
		int k = 0;
		int v[] = new int[6];
		boolean flag = false;
		for(int i = 0; i<6;i++){
			if(mano[i]!= null){
				if(mano[i].getPalo() != pt && mano[i].getNumero()==6){
					v[k] = i;
					k++;
					flag = true;
				}}
		}
		if(flag){
			int ret[] = new int[k];
			for(int i = 0;i<k;i++){
				ret[i] = v[i];
			}
			return ret;
		}
		else{
			return null;
		}
	}
	public int[] guinotestriunfo(char pt){//devuelve un vector con las posiciones
		int k = 0;
		int v[] = new int[6];
		boolean flag = false;
		for(int i = 0; i<6;i++){
			if(mano[i]!= null){
				if(mano[i].getPalo() == pt && mano[i].getNumero()>=9){
					v[k] = i;
					k++;
					flag = true;
				}}
		}
		if(flag){
			int ret[] = new int[k];
			for(int i = 0;i<k;i++){
				ret[i] = v[i];
			}
			return ret;
		}
		else{
			return null;
		}
	}
	public int[] guinotes(char pt){//devuelve un vector con las posiciones
		int k = 0;
		int v[] = new int[6];
		boolean flag = false;
		for(int i = 0; i<6;i++){
			if(mano[i]!= null){
				if(mano[i].getPalo() != pt && (mano[i].getNumero()==9 || mano[i].getNumero()==10)){
					v[k] = i;
					k++;
					flag = true;
				}}
		}
		if(flag){
			int ret[] = new int[k];
			for(int i = 0;i<k;i++){
				ret[i] = v[i];
			}
			return ret;
		}
		else{
			return null;
		}
	}
	public int laMasBajo(int[] v){//escoge la carta mas baja de un grupo dado
		int min = 11;
		int pos = 0;
		for(int i = 0;i<v.length;i++){
			if(mano[i]!= null){
				if(mano[v[i]].getNumero() < min){
					min = mano[v[i]].getNumero();
					pos = v[i];
				}
				}

		}
		return pos;
	}
	/*public int[] sotasyreyesporcantar(char pt){//devuelve un vector con las posiciones
		int k = 0;
		int v[] = new int[6];
		boolean flag = false;
		for(int i = 0; i<6;i++){
			if(mano[i]!= null){
				if(mano[i].getPalo() != pt && (mano[i].getNumero()==7 || mano[i].getNumero()==8)){
					switch(mano[i].getPalo()){
					case 'o':
						if(mano[i].getNumero()==7){
							if(!reyes[0]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						else{
							if(!sotas[0]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						break;
					case 'c':
						if(mano[i].getNumero()==7){
							if(!reyes[1]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						else{
							if(!sotas[1]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						break;
					case 'e':
						if(mano[i].getNumero()==7){
							if(!reyes[2]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						else{
							if(!sotas[2]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						break;
					case 'b':
						if(mano[i].getNumero()==7){
							if(!reyes[3]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						else{
							if(!sotas[3]){
								v[k] = i;
								k++;
								flag = true;
							}
						}
						break;
					}
				}
			}}
		if(flag){
			int ret[] = new int[k];
			for(int i = 0;i<k;i++){
				ret[i] = v[i];
			}
			return ret;
		}
		else{
			return null;
		}
	}*/
	public int[] triunfosbajos(char pt){//devuelve un vector con las posiciones
		int k = 0;
		int v[] = new int[6];
		boolean flag = false;
		for(int i = 0; i<6;i++){
			if(mano[i]!= null){
				if(mano[i].getPalo() == pt && mano[i].getNumero()<5){
					v[k] = i;
					k++;
					flag = true;
				}}
		}
		if(flag){
			int ret[] = new int[k];
			for(int i = 0;i<k;i++){
				ret[i] = v[i];
			}
			return ret;
		}
		else{
			return null;
		}
	}
	public int[] triunfosaltos(char pt){//devuelve un vector con las posiciones
		int k = 0;
		int v[] = new int[6];
		boolean flag = false;
		for(int i = 0; i<6;i++){
			if(mano[i]!= null){
				if(mano[i].getPalo() == pt && mano[i].getNumero()>=5){
					v[k] = i;
					k++;
					flag = true;
				}}
		}
		if(flag){
			int ret[] = new int[k];
			for(int i = 0;i<k;i++){
				ret[i] = v[i];
			}
			return ret;
		}
		else{
			return null;
		}
	}
	public int tengoGuinote(Carta c){//si tiene guinote para matar devuelve su posicion, si no -1
		int i;
		int r = -1;
		for(i = 0; i<6;i++){
			if(mano[i]!= null){
				if(mano[i].getPalo() == c.getPalo() && mano[i].getNumero()>=9){
					r = i;
					break;
				}}
		}
		return r;
	}
	public int pMatar(Carta c){//si puede matar devuelve su posicion, si no -1
		int i;
		int r = -1;
		for(i = 0; i<6;i++){
			if(mano[i]!= null){
				if(mano[i].getPalo() == c.getPalo() && mano[i].getNumero()>c.getNumero()){
					r = i;
					break;
				}}
		}
		return r;
	}
	public void ordena(char palotriunfo){
		int[] v = null;
		int in = 0;
		for(int i = 0; i<7;i++){
			switch(i){
			case 0:
				v = paja(palotriunfo);
				break;
			case 1:
				//v = sotasyreyescantados(palotriunfo);
				break;
			case 2:
				v = caballos(palotriunfo);
				break;
			case 3:
				//v = sotasyreyesporcantar(palotriunfo);
				break;
			case 4:
				v = triunfosbajos(palotriunfo);
				break;
			case 5:
				v = guinotes(palotriunfo);
				break;
			case 6:
				v = triunfosaltos(palotriunfo);
				if(v == null)
					v = triunfosbajos(palotriunfo);
				break;
			}
			if(v != null){
				for(int j = 0;j<v.length;j++){
					intercambiar(in,v[j]);
					in++;
				}
			}
		}
	}
	
}
