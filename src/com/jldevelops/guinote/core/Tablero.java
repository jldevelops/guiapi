package com.jldevelops.guinote.core;

import java.io.Serializable;

import com.jldevelops.guinote.utils.MiniTab;
import com.jldevelops.guinote.utils.Utils;
import static com.jldevelops.guinote.utils.Utils.PALOSC;

/**
 * Clase que representa una mesa de Guiñote en un momento determinado.
 *
 * @author jorge
 */
public class Tablero implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 5058406094244410834L;
    final private Jugador[] jugadores;
    final private Carta[][] bazas;
    final private int[] puntuaciones;
    final private boolean[][] cantes;
    /**
     * id del jugador que le toca hablar
     */
    private int idJugActual;
    private final int haempezado;
    final private Baraja b;
    final private char palotriunfo;
    /**
     * en la posicion 0 SIEMPRE estara la carta del jugador que ha sido el
     * primero en tirar.
     */
    final Carta[] cartasMesa;
    /**
     * id del jugador que ha hecho la ultima baza
     */
    private int ultBaza;
    final private boolean juegoDe4;

    /**
     * Construye un tablero nuevo con los puntos pasados.
     *
     * @param tipoJuego true=4 jugadores false=2 jugadores
     * @param idjug id del jugador que empieza
     * @param punt vector de int de 2
     */
    Tablero(boolean tipoJuego, int idjug, int[] punt) {
        b = new Baraja(true);
        bazas = new Carta[2][40];
        cantes = new boolean[2][4];
        if (punt != null) {
            puntuaciones = punt;
        } else {
            puntuaciones = new int[2];
        }
        juegoDe4 = tipoJuego;
        if (juegoDe4) {
            jugadores = new Jugador[4];
            cartasMesa = new Carta[4];
        } else {
            jugadores = new Jugador[2];
            cartasMesa = new Carta[2];

        }
        for (int i = 0; i < jugadores.length; i++) {
            jugadores[i] = new Jugador(i);
            for (int j = 0; j < jugadores[i].getMano().length; j++) {
                jugadores[i].getMano()[j] = b.sacarCarta();
            }
        }
        palotriunfo = getTriunfo().getPalo();
        idJugActual = idjug;
        haempezado = idjug;
        ultBaza = -1;//nadie tiene la ultima baza
    }

    //<editor-fold defaultstate="collapsed" desc="Getters">
    public boolean isFin() {
        for (Jugador j : jugadores) {
            if (!j.sinCartas()) {
                return false;
            }
        }
        for (Carta c : cartasMesa) {
            if (c != null) {
                return false;
            }
        }

        return true;
    }

    public char getPalotriunfo() {
        return palotriunfo;
    }

    /**
     * Devuelve la carta de la mesa del jugador idjug
     *
     * @param idjug
     * @return Carta
     */
    public Carta getCartaMesa(int idjug) {
        int ultBaza1 = ultBaza;
        if (ultBaza == -1) {
            ultBaza1 = haempezado;
        }
        if (ultBaza1 == idjug) {
            return cartasMesa[0];
        } else {
            if (s(ultBaza1) == idjug) {
                return cartasMesa[1];
            } else {
                if (s(s(ultBaza1)) == idjug) {
                    return cartasMesa[2];
                } else {
                    return cartasMesa[3];
                }
            }
        }
    }

    public boolean todasCartasTiradas() {
        for (Carta cartasMesa1 : cartasMesa) {
            if (cartasMesa1 == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * devuelve el id del jugador siguiente (el de la derecha) a idjug
     *
     * @param idjug ID del jugador
     * @return int
     */
    public int s(int idjug) {
        if (juegoDe4) {
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
     * @return null si no quedan cartas en la baraja
     */
    public Carta getTriunfo() {
        if (b.getCartaActual() < 40) {
            return b.getMazo()[39];
        } else {
            return null;
        }
    }

    public int getHaEmpezado() {
        return haempezado;
    }

    private int getCartasMesaIndex() {
        if (juegoDe4) {
            if (cartasMesa[0] == null) {
                return 0;
            } else {
                if (cartasMesa[1] == null) {
                    return 1;
                } else {
                    if (cartasMesa[2] == null) {
                        return 2;
                    } else {
                        return 3;
                    }
                }
            }
        } else {
            if (cartasMesa[0] == null) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public Jugador getJug(int idjug) {
        return jugadores[idjug];
    }

    private boolean isCantado(char palo, int idjug) {
        return cantes[aQueBaza(idjug)][PALOSC.indexOf(palo)];
    }

    public int getTurno() {
        return idJugActual;
    }

    public int getUltBaza() {
        return ultBaza;
    }

    public int getPuntuacion(int idjug) {
        return puntuaciones[aQueBaza(idjug)];
    }

    public int getCartasRestantes() {
        return (40 - b.getCartaActual());
    }

    public boolean isArrastre() {
        return getTriunfo() == null;
    }

    public MiniTab toMiniTab(int idjug) {
        return new MiniTab(getJug(idjug), puntuaciones, cantes, idJugActual, haempezado, palotriunfo, cartasMesa, ultBaza, juegoDe4, getCartasRestantes(), getTriunfo());
    }

    public boolean isJuegoDe4() {
        return juegoDe4;
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Package">
    
    /**
     * Cambia el 7 al jugador idjug si lo tiene
     *
     * @param idjug
     * @return String con el mensaje a imprimir en la pantalla de idjug
     */
    String cambiar7(int idjug) {
        if (ultBaza == idJugActual || ultBaza == s(s(idJugActual))) {
            if (jugadores[idjug].tieneCarta("4" + String.valueOf(palotriunfo))) {
                int idcarta = jugadores[idjug].numCarta("4" + palotriunfo);
                jugadores[idjug].getMano()[idcarta].intercambiarValor(b.getMazo()[39]);
                return "Has cambiado el 7.";//el punto del final es necesario
            } else {
                return "No tienes el 7";
            }
        } else {
            return "No tienes baza reciente";
        }

    }

    /**
     * @param idjug entre 0 y 1 o entre 0 y 3
     * @param idcarta entre 0 y 5
     * @return null si se ha tirado la carta, si no devuelve el error
     */
    String tirarCarta(int idjug, int idcarta) {//idjug
        if (idjug == idJugActual) {
            if (compruebaCarta(idjug, idcarta) == null) {
                Carta tmp = jugadores[idjug].tirarCarta(idcarta);
                if (tmp.getNumero() == 7 || tmp.getNumero() == 8) {
                    for (Jugador j : jugadores) {
                        j.getSotas()[PALOSC.indexOf(tmp.getPalo())] = true;
                        j.getReyes()[PALOSC.indexOf(tmp.getPalo())] = true;
                    }
                }
                cartasMesa[getCartasMesaIndex()] = tmp;
                if (!todasCartasTiradas()) {
                    siguienteJug();
                }
                return null;
            } else {
                return compruebaCarta(idjug, idcarta);
            }
        } else {
            return "";
        }
    }

    /**
     * @param idjug
     * @return "" si no tiene cantes; el/los palos si tiene cantes y no estan
     * cantados y los marca
     */
    String compruebaMarcaCantes(int idjug) {
        String ret = compruebaCantes(idjug);
        for(char c:ret.toCharArray()){
            marcarCante(c, idjug);
        }
        return ret;
    }

    /**
     * llamar cuando estén todas (2 o 4) las cartas tiradas; idJugActual debe
     * ser el anterior a ultBaza en este punto, excepto en la primera jugada;
     * decide el idJug del ganador y lo devuelve, actualiza ultBaza e
     * idJugActual, recoge las cartas a su respectiva baza (la del idjug 0 o 1),
     * y hace robar las cartas del mazo
     *
     * @return idJug del ganador de la baza
     */
    int ganadorDeLaJugada() {
        int ganadoridjug;
        if (ultBaza == -1) {
            ultBaza = haempezado; //debe hacerse para que evaluadorCartas() 
            //pueda decidir el ganador en la primera jugada
        }
        ganadoridjug = evaluadorCartas();
        ultBaza = ganadoridjug;
        idJugActual = ganadoridjug;
        recogerCartas(aQueBaza(ganadoridjug));
        robar();
        if (isFin()) {
            puntuaciones[aQueBaza(ganadoridjug)] += 10;
        }
        return ganadoridjug;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public">
    public boolean equals(Tablero t) {
        return Utils.GSON.toJson(t).equals(Utils.GSON.toJson(this));
    }

    @Override
    public Tablero clone() {
        try {
            return (Tablero) super.clone();
        } catch (Exception ex) {
            return null;
        }
    }

    public String compruebaCantes(int idjug) {
        //<editor-fold defaultstate="collapsed" desc="Code">
        boolean ro = false, so = false, rc = false, sc = false;
        boolean re = false, se = false, rb = false, sb = false;
        String palos = "";
        for (Carta c : jugadores[idjug].getMano()) {
            if (c != null) {
                if (c.getPalo() == 'o') {
                    if (c.getNumero() == 8)
                        ro = true;
                    if (c.getNumero() == 7)
                        so = true;
                }
                if (c.getPalo() == 'c') {
                    if (c.getNumero() == 8)
                        rc = true;
                    if (c.getNumero() == 7)
                        sc = true;
                }
                if (c.getPalo() == 'e') {
                    if (c.getNumero() == 8)
                        re = true;
                    if (c.getNumero() == 7)
                        se = true;
                }
                if (c.getPalo() == 'b') {
                    if (c.getNumero() == 8)
                        rb = true;
                    if (c.getNumero() == 7)
                        sb = true;
                }
            }
        }
        if (ro && so)
            if (!isCantado('o', idjug))
                palos += "o";
        if (rc && sc)
            if (!isCantado('c', idjug))
                palos += "c";
        if (re && se)
            if (!isCantado('e', idjug))
                palos += "e";
        if (rb && sb)
            if (!isCantado('b', idjug))
                palos += "b";
        return palos;
        //</editor-fold>
    }

    /**
     * @param idjug 0-1 si 2jug 0-3 si 4jug
     * @param idcarta 0-5
     * @return null si se puede tirar, si no devuelve un string con el error
     */
    public String compruebaCarta(int idjug, int idcarta) {
        //<editor-fold defaultstate="collapsed" desc="Code">
        if (jugadores[idjug].getMano()[idcarta] == null) {
            return "";
        }
        if (!isArrastre()) {
            return null;
        }

        if (idjug == ultBaza) {
            return null;
        }

        if (!juegoDe4) {
            return modo2Jug(idjug, idcarta);
        }

        if (cartasMesa[0] != null && cartasMesa[1] == null) {
            return modo2Jug(idjug, idcarta);
        }

        if (cartasMesa[2] != null) {
            switch (quienMata(cartasMesa[0], cartasMesa[1], cartasMesa[2])) {
                case 0:
                    return modo2Jug(idjug, idcarta);
                case 1://de compi
                    if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()) == null) {
                        return null;
                    } else {
                        if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()).charAt(idcarta) == '1') {
                            return null;
                        } else {
                            return "Tienes que echar mismo palo: " + Utils.paloStr(cartasMesa[0].getPalo());
                        }
                    }
                case 2:
                    if (cartasMesa[0].getPalo() == palotriunfo) {
                        if (jugadores[idjug].checkMismoPalo(palotriunfo) == null) {
                            return null;
                        } else {
                            if (jugadores[idjug].puedeMatar(cartasMesa[2]) == null) {
                                if (jugadores[idjug].checkMismoPalo(palotriunfo).charAt(idcarta) == '1') {
                                    return null;
                                } else {
                                    return "Tienes que echar triunfo: " + Utils.paloStr(palotriunfo);
                                }
                            } else {
                                if (jugadores[idjug].puedeMatar(cartasMesa[2]).charAt(idcarta) == '1') {
                                    return null;
                                } else {
                                    return "Tienes que matar";
                                }
                            }
                        }
                    } else {
                        //palo de salida no es triunfo
                        if (cartasMesa[2].getPalo() == palotriunfo) {
                            //el que ha matado lo ha hecho con triunfo
                            if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()) == null) {
                                //idjug no tiene palo de salida
                                if (jugadores[idjug].checkMismoPalo(palotriunfo) == null) {
                                    return null;
                                } else {
                                    //idjug tiene triunfo
                                    if (jugadores[idjug].puedeMatar(cartasMesa[2]) == null) {
                                        return null;
                                    } else {
                                        if (jugadores[idjug].puedeMatar(cartasMesa[2]).charAt(idcarta) == '1') {
                                            return null;
                                        } else {
                                            return "Tienes que matar";
                                        }
                                    }
                                }
                            } else {
                                //idjug tiene palo de salida
                                if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()).charAt(idcarta) == '1') {
                                    return null;
                                } else {
                                    return "Tienes que echar mismo palo: " + Utils.paloStr(cartasMesa[0].getPalo());
                                }
                            }
                        } else {
                            //el que ha matado no lo ha hecho con triunfo -mismo palo
                            if (jugadores[idjug].checkMismoPalo(cartasMesa[2].getPalo()) == null) {
                                //idjug no tiene el palo de salida
                                if (jugadores[idjug].checkMismoPalo(palotriunfo) == null) {
                                    return null;
                                } else {
                                    //idjug tiene triunfo
                                    if (jugadores[idjug].checkMismoPalo(palotriunfo).charAt(idcarta) == '1') {
                                        return null;
                                    } else {
                                        return "Tienes que echar triunfo: " + Utils.paloStr(palotriunfo);
                                    }
                                }
                            } else {
                                //idjug tiene palo de salida
                                if (jugadores[idjug].puedeMatar(cartasMesa[2]) == null) {
                                    if (jugadores[idjug].checkMismoPalo(cartasMesa[2].getPalo()).charAt(idcarta) == '1') {
                                        return null;
                                    } else {
                                        return "Tienes que echar mismo palo: " + Utils.paloStr(cartasMesa[0].getPalo());
                                    }
                                } else {
                                    if (jugadores[idjug].puedeMatar(cartasMesa[2]).charAt(idcarta) == '1') {
                                        return null;
                                    } else {
                                        return "Tienes que matar";
                                    }
                                }
                            }
                        }
                    }
                default:
                    System.out.println("fallo a la hora de escoger2");
                    break;
            }
        } else {
            switch (quienMata(cartasMesa[0], cartasMesa[1], cartasMesa[2])) {
                case 0://de compi
                    if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()) == null) {
                        return null;
                    } else {
                        if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()).charAt(idcarta) == '1') {
                            return null;
                        } else {
                            return "Tienes que echar mismo palo: " + Utils.paloStr(cartasMesa[0].getPalo());
                        }
                    }
                case 1:
                    if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()) == null) {
                        //idjug no tiene el palo de salida
                        if (cartasMesa[0].getPalo() == palotriunfo) {
                            return null;
                        } else {
                            //el palo de salida no es triunfo
                            if (cartasMesa[1].getPalo() == palotriunfo) {
                                //el palo del que ha matado es triunfo
                                if (jugadores[idjug].checkMismoPalo(palotriunfo) == null) {
                                    //idjug no tiene triunfo
                                    return null;
                                } else {
                                    //idjug tiene triunfo
                                    if (jugadores[idjug].puedeMatar(cartasMesa[1]) == null) {
                                        return null;
                                    } else {
                                        if (jugadores[idjug].puedeMatar(cartasMesa[1]).charAt(idcarta) == '1') {
                                            return null;
                                        } else {
                                            return "Tienes que matar";
                                        }
                                    }
                                }
                            } else {
                                //el palo del que ha matado no es triunfo
                                if (jugadores[idjug].checkMismoPalo(palotriunfo) == null) {
                                    return null;
                                } else {
                                    //idjug tiene triunfo
                                    if (jugadores[idjug].checkMismoPalo(palotriunfo).charAt(idcarta) == '1') {
                                        return null;
                                    } else {
                                        return "Tienes que echar triunfo: " + Utils.paloStr(palotriunfo);
                                    }
                                }
                            }
                        }
                    } else {
                        //idjug sí tiene el palo de salida
                        if (cartasMesa[0].getPalo() == palotriunfo) {
                            //el palo de salida es triunfo
                            if (jugadores[idjug].puedeMatar(cartasMesa[1]) == null) {
                                if (jugadores[idjug].getMano()[idcarta].getPalo() == palotriunfo) {
                                    return null;
                                } else {
                                    return "Tienes que echar triunfo: " + Utils.paloStr(palotriunfo);
                                }
                            } else {
                                if (jugadores[idjug].puedeMatar(cartasMesa[1]).charAt(idcarta) == '1') {
                                    return null;
                                } else {
                                    return "Tienes que matar";
                                }
                            }
                        } else {
                            //el palo de salida no es triunfo
                            if (cartasMesa[1].getPalo() == palotriunfo) {
                                //el que ha matado lo ha hecho con triunfo
                                if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()).charAt(idcarta) == '1') {
                                    return null;
                                } else {
                                    return "Tienes que echar mismo palo: " + Utils.paloStr(cartasMesa[0].getPalo());
                                }
                            } else {
                                //el que ha matado no lo ha hecho con triunfo
                                if (jugadores[idjug].puedeMatar(cartasMesa[1]) == null) {
                                    if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()).charAt(idcarta) == '1') {
                                        return null;
                                    } else {
                                        return "Tienes que echar mismo palo: " + Utils.paloStr(cartasMesa[0].getPalo());
                                    }
                                } else {
                                    if (jugadores[idjug].puedeMatar(cartasMesa[1]).charAt(idcarta) == '1') {
                                        return null;
                                    } else {
                                        return "Tienes que matar";
                                    }
                                }
                            }

                        }
                    }
                default:
                    System.out.println("fallo a la hora de escoger");
                    break;
            }
        }

        System.out.println("comorrrrrrrr!?!?!");
        return null;
        //</editor-fold>
    }

    /**
     * @param c1 palo de salida (0)
     * @param c2 el siguiente en tirar (1)
     * @param c3 el siguiente del siguiente en tirar (2)
     * @return la posicion relativa de la carta que gana: 0,1 o 2
     */
    public int quienMata(Carta c1, Carta c2, Carta c3) {
        //<editor-fold defaultstate="collapsed" desc="Code">
        if (c3 == null) {
            if (c1.getPalo() == palotriunfo) {
                if(c2 == null)
                    return 0;
                if (c2.getPalo() == palotriunfo) {
                    if (c1.getNumero() > c2.getNumero()) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return 0;
                }
            } else {
                if (c1.getPalo() == c2.getPalo()) {
                    if (c1.getNumero() > c2.getNumero()) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    if (c2.getPalo() == palotriunfo) {
                        return 1;
                    } else {
                        return 0;
                    }
                    
                }
            }
        } else {
            if (quienMata(c1, c2, null) == 0) {
                if (quienMata(c1, c3, null) == 0) {
                    return 0;
                } else {
                    return 2;
                }
            } else {
                if (quienMata(c2, c3, null) == 0) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }
    //</editor-fold>
    }

    public int aQueBaza(int idjug) {
        if (juegoDe4) {
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

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Private">
    //<editor-fold defaultstate="collapsed" desc="Pseudométodos">
    private String modo2Jug(int idjug, int idcarta) {
        if (cartasMesa[0].getPalo() == palotriunfo) {//palo de salida triunfo
            if (jugadores[idjug].checkMismoPalo(palotriunfo) == null) {//no tiene triunfo
                return null;
            } else {//tiene triunfo
                if (jugadores[idjug].puedeMatar(cartasMesa[0]) == null) {
                    //no puede matar
                    if (jugadores[idjug].checkMismoPalo(palotriunfo) == null) {
                        return null;
                    } else {
                        if (jugadores[idjug].checkMismoPalo(palotriunfo).charAt(idcarta) == '1') {
                            return null;
                        } else {
                            return "Tienes que echar triunfo: " + Utils.paloStr(palotriunfo);
                        }
                    }
                } else {//puede matar
                    if (jugadores[idjug].puedeMatar(cartasMesa[0]).charAt(idcarta) == '1') {
                        return null;
                    } else {
                        return "Tienes que matar";
                    }
                }
            }
        } else {//palo de salida no triunfo
            if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()) == null) {//no tiene el palo de salida
                if (jugadores[idjug].checkMismoPalo(palotriunfo) == null)//no tiene tr
                {
                    return null;
                } else {//tiene tr
                    if (jugadores[idjug].checkMismoPalo(palotriunfo).charAt(idcarta) == '1') {
                        return null;
                    } else {
                        return "Tienes que echar triunfo: " + Utils.paloStr(palotriunfo);
                    }
                }
            } else {//tiene el palo de salida
                if (jugadores[idjug].puedeMatar(cartasMesa[0]) == null) {//no puede matar
                    if (jugadores[idjug].checkMismoPalo(cartasMesa[0].getPalo()).charAt(idcarta) == '1') {
                        return null;
                    } else {
                        return "Tienes que tirar mismo palo: " + Utils.paloStr(cartasMesa[0].getPalo());
                    }
                } else {//puede matar
                    if (jugadores[idjug].puedeMatar(cartasMesa[0]).charAt(idcarta) == '1') {
                        return null;
                    } else {
                        return "Tienes que matar";
                    }
                }
            }

        }
    }

    /**
     * relacion cartasMesa -> idjug cartasMesa[0] -> ultBaza cartasMesa[1] ->
     * s(ultBaza) cartasMesa[2] -> s(s(ultBaza)) cartasMesa[3] ->
     * s(s(s(ultBaza)));
     *
     * @param c debe estar en cartasMesa
     * @return el idjug que ha tirado esa carta
     */
    private int cartaMesaEsDe(Carta c) {
        int i;
        for (i = 0; i < cartasMesa.length; i++) {
            if (c == cartasMesa[i]) {
                break;
            }
        }
        switch (i) {
            case 0:
                return ultBaza;
            case 1:
                return s(ultBaza);
            case 2:
                return s(s(ultBaza));
            case 3:
                return s(s(s(ultBaza)));
            default:
                return 0;
        }
    }

    /**
     * evalua cartasMesa y devuelve el idjug del jugador que ha tirado la mejor
     * carta
     *
     * @return
     */
    private int evaluadorCartas() {
        Carta ganadoraActual = cartasMesa[0];
        for (int i = 1; i < cartasMesa.length; i++) {
            if (ganadoraActual.getPalo() == getPalotriunfo()) {
                if (cartasMesa[i].getPalo() == getPalotriunfo()) {
                    if (cartasMesa[i].getNumero() > ganadoraActual.getNumero()) {
                        ganadoraActual = cartasMesa[i];
                    }
                }
            } else {
                if (cartasMesa[i].getPalo() == ganadoraActual.getPalo()) {
                    if (cartasMesa[i].getNumero() > ganadoraActual.getNumero()) {
                        ganadoraActual = cartasMesa[i];
                    }
                } else {
                    if (cartasMesa[i].getPalo() == getPalotriunfo()) {
                        ganadoraActual = cartasMesa[i];
                    }
                }
            }
        }
        return cartaMesaEsDe(ganadoraActual);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Métodos">
    private void robar() {
        if (!isArrastre()) {
            int tmp = ultBaza;
            jugadores[tmp].meterCarta(b.sacarCarta(), palotriunfo);
            tmp = s(tmp);
            while (tmp != ultBaza) {
                jugadores[tmp].meterCarta(b.sacarCarta(), palotriunfo);
                tmp = s(tmp);
            }
        }
    }

    private void marcarCante(char palo, int idjug) {
        cantes[aQueBaza(idjug)][PALOSC.indexOf(palo)] = true;
        for (Jugador j : jugadores) {
            j.getSotas()[PALOSC.indexOf(palo)] = true;
            j.getReyes()[PALOSC.indexOf(palo)] = true;
        }
        if (palotriunfo == palo) {
            puntuaciones[aQueBaza(idjug)] += 40;
        } else {
            puntuaciones[aQueBaza(idjug)] += 20;
        }
    }

    /**
     * pasa el turno al siguiente jugador
     */
    private void siguienteJug() {
        idJugActual = s(idJugActual);
    }

    /**
     * mueve las cartas de la mesa a la baza 0 o 1 y suma los puntos
     *
     * @param idbaza 0 o 1
     */
    private void recogerCartas(int idbaza) {
        int i;
        for (i = 0; i < bazas[idbaza].length; i++) {
            if (bazas[idbaza][i] == null) {
                break;
            }
        }
        for (int j = 0; j < cartasMesa.length; j++) {
            bazas[idbaza][i] = cartasMesa[j];
            puntuaciones[idbaza] += bazas[idbaza][i].getPuntos();
            cartasMesa[j] = null;
            i++;
        }
    }

    //</editor-fold>
    //</editor-fold>
}
