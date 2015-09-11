package com.jldevelops.guinote.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Jugador implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2607177138340818103L;
    private Carta[] mano;
    private final int idJug;
    private boolean inactivo;
    private String idDisp;
    private String nombre;
    private final boolean[] reyes;//reyes que han salido en orden oceb
    private final boolean[] sotas;//sotas que han salido en orden oceb

    Jugador(int idjugador) {
        mano = new Carta[6];
        idJug = idjugador;
        reyes = new boolean[4];
        sotas = new boolean[4];
    }

    //<editor-fold defaultstate="collapsed" desc="Getters y setters">

    public boolean isInactivo() {
        return inactivo;
    }

    public void setInactivo(boolean inactivo) {
        this.inactivo = inactivo;
    }
    
    public String getIdDisp() {
        return idDisp;
    }

    public void setIdDisp(String idDisp) {
        this.idDisp = idDisp;
    }
    
    public int getIdJug() {
        return idJug;
    }
    
    Carta[] getMano() {
        return mano;
    }
    
    public Carta getCarta(int idcarta) {
        return mano[idcarta];
    }

    public boolean[] getReyes() {
        return reyes;
    }

    public boolean[] getSotas() {
        return sotas;
    }
    
    public boolean sinCartas() {
        for (Carta c : mano) {
            if (c != null) {
                return false;
            }
        }
        return true;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos de jugador">
    void meterCarta(Carta c, char t) {
        boolean b = false;
        for (int i = 0; i < mano.length; i++) {
            if (b) {
                mano[i - 1] = mano[i];
            }
            if (mano[i] == null) {
                b = true;
            }
        }
        mano[5] = c;
        ordenarMano(t);
    }
    
    private void ordenarMano(char triunfo) {
        int tri = 0;
        for (int tr = 0; tr < 6; tr++) {
            if (mano[tr] != null) {
                if (mano[tr].getPalo() == triunfo) {
                    if (tri < tr) {
                        intercambiar(tr, tri);
                        tri++;
                    }
                    if (tri == tr) {
                        tri++;
                    }
                }
            }
            
        }
    }
    
    
    
    void intercambiar(int carta1, int carta2) {
        if (carta1 >= 0 && carta1 <= 5 && carta2 >= 0 && carta2 <= 5) {
            Carta tmp = mano[carta1];
            mano[carta1] = mano[carta2];
            mano[carta2] = tmp;
        }
    }
    
    
    
    /**
     * ncarta debe estar entre 0 y 5 ambos inclusive este getter elimina la
     * carta del vector mano usar getMano()[i] para leer datos de la carta.
     *
     * @param ncarta
     * @return La carta tirada
     */
    Carta tirarCarta(int ncarta) {
        if (mano[ncarta] != null) {
            Carta tmp = mano[ncarta];
            mano[ncarta] = null;
            return tmp;
        } else {
            return null;
        }
    }
    
    /**
     * num (del 0 al 9) y palo
     *
     * @param carta
     * @return la posicion de la carta, si no -1
     */
    public int numCarta(String carta) {
        for (int i = 0; i < mano.length; i++) {
            if (mano[i] != null) {
                if (mano[i].getNumero() - 1 == Integer.parseInt(carta.charAt(0) + "")) {
                    if (mano[i].getPalo() == carta.charAt(1)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * num (del 0 al 9) y palo
     *
     * @param carta
     * @return true si el Jugador tiene la carta
     */
    public boolean tieneCarta(String carta) {
        return numCarta(carta) > -1;
    }
    
    //</editor-fold>
   
    //<editor-fold defaultstate="collapsed" desc="Métodos de evaluación">
    
    /**
     * no evalua los triunfos
     *
     * @param c no puede ser null
     * @return null si ninguna carta puede matar a c, si no devuelve un string
     * de 0 y 1 con las posiciones que sí pueden
     */
    String puedeMatar(Carta c) {
        String b = "";
        boolean flag = false;
        for (int i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (c.getPalo() == mano[i].getPalo()) {
                    if (c.getNumero() < mano[i].getNumero()) {
                        b += "1";
                        flag = true;
                    } else {
                        b += "0";
                    }
                } else {
                    b += "0";
                }
            } else {
                b += "0";
            }
        }
        if (flag) {
            return b;
        } else {
            return null;
        }
    }
    
    /**
     *
     * @param palo
     * @return null si no tiene el palo pasado, si no devuelve un string de 0 y
     * 1 con las posiciones que tienen mismo palo
     */
    String checkMismoPalo(char palo) {
        String b = tieneMismoPalo(palo);
        for (int i = 0; i < b.length(); i++) {
            if (b.charAt(i) == '1') {
                return b;
            }
        }
        return null;
    }
    
    
    
    private String tieneMismoPalo(char palo) {
        String b = "";
        for (int i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (palo == mano[i].getPalo()) {
                    b += "1";
                } else {
                    b += "0";
                }
            } else {
                b += "0";
            }
        }
        return b;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Métodos de ordenación (ia)">

    public List<Integer> paja(char pt) {//devuelve un vector con las posiciones
        List<Integer> ret = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (mano[i].getPalo() != pt && mano[i].getNumero() <= 5) {
                    ret.add(i);
                    
                }
            }
        }
        return ret;
    }
    
    public List<Integer> sotasyreyescantados(char pt) {//devuelve un vector con las posiciones
        List<Integer> ret = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (mano[i].getPalo() != pt && (mano[i].getNumero() == 7 || mano[i].getNumero() == 8)) {
                    switch (mano[i].getPalo()) {
                        case 'o':
                            if (mano[i].getNumero() == 7) {
                                if (reyes[0]) {
                                    ret.add(i);
                                    
                                }
                            } else {
                                if (sotas[0]) {
                                    ret.add(i);
                                    
                                }
                            }
                            break;
                        case 'c':
                            if (mano[i].getNumero() == 7) {
                                if (reyes[1]) {
                                    ret.add(i);
                                    
                                }
                            } else {
                                if (sotas[1]) {
                                    ret.add(i);
                                    
                                }
                            }
                            break;
                        case 'e':
                            if (mano[i].getNumero() == 7) {
                                if (reyes[2]) {
                                    ret.add(i);
                                    
                                }
                            } else {
                                if (sotas[2]) {
                                    ret.add(i);
                                    
                                }
                            }
                            break;
                        case 'b':
                            if (mano[i].getNumero() == 7) {
                                if (reyes[3]) {
                                    ret.add(i);
                                    
                                }
                            } else {
                                if (sotas[3]) {
                                    ret.add(i);
                                    
                                }
                            }
                            break;
                    }
                    
                }
            }
        }
        return ret;
    }
    
    public List<Integer> caballos(char pt) {//devuelve un vector con las posiciones
        List<Integer> ret = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (mano[i].getPalo() != pt && mano[i].getNumero() == 6) {
                    ret.add(i);
                    
                }
            }
        }
        return ret;
    }
    
    public List<Integer> guinotestriunfo(char pt) {//devuelve un vector con las posiciones
        List<Integer> ret = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (mano[i].getPalo() == pt && mano[i].getNumero() >= 9) {
                    ret.add(i);
                    
                }
            }
        }
        return ret;
    }
    
    public List<Integer> guinotes(char pt) {//devuelve un vector con las posiciones
        List<Integer> ret = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (mano[i].getPalo() != pt && (mano[i].getNumero() == 9 || mano[i].getNumero() == 10)) {
                    ret.add(i);
                    
                }
            }
        }
        return ret;
    }
    
    public int laMasBajo(List<Integer> l) {//escoge la carta mas baja de un grupo dado
        int min = 11;
        int pos = 0;
        for (int i = 0; i < l.size(); i++) {
            Integer c = l.get(i);
            if (c != null) {
                if (mano[c].getNumero() < min) {
                    min = mano[c].getNumero();
                    pos = c;
                }
            }
            
        }
        return pos;
    }
    
    public List<Integer> sotasyreyesporcantar(char pt) {//devuelve un vector con las posiciones
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (mano[i].getPalo() != pt && (mano[i].getNumero() == 7 || mano[i].getNumero() == 8)) {
                    switch (mano[i].getPalo()) {
                        case 'o':
                            if (mano[i].getNumero() == 7) {
                                if (!reyes[0]) {
                                    ret.add(i);
                                    
                                }
                            } else {
                                if (!sotas[0]) {
                                    ret.add(i);
                                    
                                }
                            }
                            break;
                        case 'c':
                            if (mano[i].getNumero() == 7) {
                                if (!reyes[1]) {
                                    ret.add(i);
                                    
                                }
                            } else {
                                if (!sotas[1]) {
                                    ret.add(i);
                                    
                                }
                            }
                            break;
                        case 'e':
                            if (mano[i].getNumero() == 7) {
                                if (!reyes[2]) {
                                    ret.add(i);
                                    
                                }
                            } else {
                                if (!sotas[2]) {
                                    ret.add(i);
                                    
                                }
                            }
                            break;
                        case 'b':
                            if (mano[i].getNumero() == 7) {
                                if (!reyes[3]) {
                                    ret.add(i);
                                    
                                }
                            } else {
                                if (!sotas[3]) {
                                    ret.add(i);
                                    
                                }
                            }
                            break;
                    }
                }
            }
        }
        return ret;
    }
    
    public List<Integer> triunfosbajos(char pt) {//devuelve un vector con las posiciones
        List<Integer> ret = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (mano[i].getPalo() == pt && mano[i].getNumero() < 5) {
                    ret.add(i);
                    
                }
            }
        }
        return ret;
    }
    
    public List<Integer> triunfosaltos(char pt) {//devuelve un vector con las posiciones
        List<Integer> ret = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (mano[i].getPalo() == pt && mano[i].getNumero() >= 5) {
                    ret.add(i);
                    
                }
            }
        }
        return ret;
    }
    
    public int tengoGuinote(Carta c) {//si tiene guinote para matar devuelve su posicion, si no -1
        int i;
        int r = -1;
        for (i = 0; i < 6; i++) {
            if (mano[i] != null) {
                if (mano[i].getPalo() == c.getPalo() && mano[i].getNumero() > 9 && c.getNumero() < mano[i].getNumero()) {
                    r = i;
                    break;
                }
            }
        }
        return r;
    }
    
    public void ordena(char palotriunfo) {
        List<Integer> lista = paja(palotriunfo);
        lista.addAll(sotasyreyescantados(palotriunfo));
        lista.addAll(caballos(palotriunfo));
        lista.addAll(sotasyreyesporcantar(palotriunfo));
        lista.addAll(triunfosaltos(palotriunfo).isEmpty()?new ArrayList<Integer>():triunfosbajos(palotriunfo));
        lista.addAll(guinotes(palotriunfo));
        lista.addAll(triunfosaltos(palotriunfo));
        lista.addAll(triunfosaltos(palotriunfo).isEmpty()?triunfosbajos(palotriunfo):new ArrayList<Integer>());
        Carta[] arr = new Carta[6];
        for(int i = 0 ;i<6;i++){
            if(i < lista.size())
                arr[i] = mano[lista.get(i)];
        }
        mano = arr;
    }
    //</editor-fold>
}
