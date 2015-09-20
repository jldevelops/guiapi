package com.jldevelops.guinote.utils;

import java.io.Serializable;

import com.jldevelops.guinote.core.Carta;
import com.jldevelops.guinote.core.Jugador;

public class MiniTab implements Serializable {

    private static final long serialVersionUID = 8L;
    final private String[] c = new String[6];
    final private int[] p;
    final private String t;
    /**
     * id del jugador que le toca hablar
     */
    final private int i;
    final private int h;
    /**
     * en la posicion 0 SIEMPRE estara la carta del jugador que ha sido el
     * primero en tirar.
     */
    final private String[] m;
    /**
     * id del jugador que ha hecho la ultima baza
     */
    final private int u;
    final private Integer r;

    public MiniTab(int idjug, Jugador[] jugadores, int[] puntuaciones, boolean[][] cantes,
            int idJugActual, int haempezado, char palotriunfo,
            Carta[] cartasMesa, int ultBaza, boolean juegoDe4, int cr, Carta triunfo) {
        super();
        for (int i = 0; i < 6; i++) {
            if (jugadores[idjug].getCarta(i) != null) {
                c[i] = jugadores[idjug].getCarta(i).toString();
            }
        }
        this.p = puntuaciones;
        this.i = idJugActual;
        this.h = haempezado;
        this.m = new String[juegoDe4 ? 4 : 2];
        for (int i = 0; i < (juegoDe4 ? 4 : 2); i++) {
            if (cartasMesa[i] != null) {
                m[i] = cartasMesa[i].toString();
            }
        }
        this.u = ultBaza;
        if(cr == 0)
            this.r = null;
        else
            this.r = cr;
        if (triunfo != null) {
            this.t = triunfo.toString();
        } else {
            this.t = null;
        }
    }
    
    public boolean todasCartasTiradas(boolean jd){
        for(int i = 0;i<(jd?4:2);i++){
            if(getCartaMesa(i,jd) == null){
                return false;
            }
        }
        return true;
    }
    
    
    public int getGanador(int idjug,boolean jd){
        if(p[aQueBaza(idjug,jd)]>101 && p[aQueBaza(s(idjug,jd),jd)]>101)
            return aQueBaza(u,jd);
        if(p[aQueBaza(idjug,jd)]>101)
            return aQueBaza(idjug,jd);
        return aQueBaza(s(idjug,jd),jd);
    }


    public String getTriunfo() {
        return t;
    }

    public String[] getCartas() {
        return c;
    }


    public int getTurno() {
        return i;
    }

    public int getHaempezado() {
        return h;
    }
    
    public int getUltBaza() {
        return u;
    }

    public Integer getCartasRestantes() {
        return r;
    }

    public int aQueBaza(int idjug,boolean jd) {
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

    public int getPuntuacion(int idjug,boolean jd) {
        return p[aQueBaza(idjug,jd)];
    }

    /**
     * devuelve el id del jugador siguiente (el de la derecha) a idjug
     *
     * @param idjug ID del jugador
     * @return int
     */
    public int s(int idjug,boolean jd) {

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

    /**
     * Devuelve la carta de la mesa del jugador idjug
     *
     * @param idjug
     * @return Carta
     */
    public String getCartaMesa(int idjug,boolean jd) {
        int ultBaza1 = u;
        if (u == -1) {
            ultBaza1 = h;
        }
        if (ultBaza1 == idjug) {
            return m[0];
        } else {
            if (s(ultBaza1,jd) == idjug) {
                return m[1];
            } else {
                if (s(s(ultBaza1,jd),jd) == idjug) {
                    return m[2];
                } else {
                    return m[3];
                }
            }
        }
    }
}
