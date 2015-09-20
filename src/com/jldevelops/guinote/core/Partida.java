package com.jldevelops.guinote.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jldevelops.guinote.utils.GuiJson;
import com.jldevelops.guinote.utils.Jug;
import com.jldevelops.guinote.utils.MiniTab;
import com.jldevelops.guinote.utils.Utils;

/**
 * Clase que graba toda la partida; Una partida es un listado de tableros hasta
 * que su propiedad terminada se pone a true cuando alguno de los jugadores
 * alcanza más de 100 puntos. Cuando se hayan tirado todas las cartas se debe
 * invocar a ganadorDeLaJugada() para continuar.
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
    private int ganadorDesemp = -1;//ganador de la jugada si ambos superan 51 buenas
    //si no -1
    private Tablero tab;
    private GuiJson js;
    private final boolean tipoPartida;
    private String salaEspera;
    /**
     * indica que la partida ya está acabada
     */
    private boolean terminada;
    private boolean dialogoMostrado;
    private boolean deVueltas;

    /**
     * @param id
     * @param tipoDeJuego true=4jug false=2jug
     * @param idjug id del jugador que empieza a tirar
     * @param nom array de string con los nombres de los jugadores
     */
    public Partida(int id, boolean tipoDeJuego, int idjug, String[] nom) {
        this.id = id;
        js = new GuiJson(tipoDeJuego);
        tipoPartida = tipoDeJuego;
        tabs = new ArrayList<>();
        tab = new Tablero(tipoDeJuego, idjug, null);
        if (nom != null) {
            for (int i = 0; i < (tipoPartida ? 4 : 2); i++) {
                tab.getJug(i).setNombre(nom[i]);
            }
        }

    }

    /**
     * @param id
     * @param tipoDeJuego true=4jug false=2jug
     * @param idjug id del jugador que empieza a tirar
     * @param jugs objetos con metodos para obtener id y nombre
     */
    public Partida(int id, boolean tipoDeJuego, int idjug, List<Jug> jugs,String salaEspera) {
        this.id = id;
        this.salaEspera = salaEspera;
        js = new GuiJson(tipoDeJuego);
        tipoPartida = tipoDeJuego;
        tabs = new ArrayList<>();
        tab = new Tablero(tipoDeJuego, idjug, null);
        if (jugs != null) {
            for (int i = 0; i < (tipoPartida ? 4 : 2); i++) {
                tab.getJug(i).setNombre(jugs.get(i).getNombre());
                tab.getJug(i).setIdDisp(jugs.get(i).getId());
            }
        }

    }

    //<editor-fold defaultstate="collapsed" desc="Getters">

    public String getSalaEspera() {
        return salaEspera;
    }

    public void setSalaEspera(String salaEspera) {
        this.salaEspera = salaEspera;
    }
    
    public boolean is4Jug() {
        return tipoPartida;
    }

    public ArrayList<Tablero> getTabs() {
        return tabs;
    }

    public int getId() {
        return id;
    }

    public Tablero getTab() {
        return tab;
    }

    public String getTabActualJSON() {
        return Utils.GSON.toJson(tab);
    }

    public String[] getMiniTabsJSON() {
        String[] tm = new String[tipoPartida ? 4 : 2];
        for (int i = 0; i < tm.length; i++) {
            MiniTab m = tab.toMiniTab(i);
            tm[i] = Utils.GSON.toJson(m);
        }
        return tm;
    }

    public String getPartidaJSON() {
        return Utils.GSON.toJson(tabs);
    }

    public boolean isTerminada() {
        return terminada;
    }

    public boolean isDeVueltas() {
        return deVueltas;
    }

    public boolean mostrarDialogoVueltas() {
        if (deVueltas && !dialogoMostrado) {
            dialogoMostrado = true;
            return true;
        }
        return false;
    }

    public String msgToJson(GuiJson msg) {
        return Utils.GSON.toJson(msg);
    }

    public int getTurno() {
        return tab.getTurno();
    }

    public Jugador getJug(int idjug) {
        return tab.getJug(idjug);
    }

    public boolean todasCartasTiradas() {
        return tab.todasCartasTiradas();
    }

    //pre: terminada=true
    //pos: true si gana el jug/equipo 0   false si gana el jug/equipo 1
    public boolean getGanador() {
        if (ganadorDesemp == -1) {
            return tab.getPuntuacion(0) > 101;
        } else {
            return ganadorDesemp == 0 || ganadorDesemp == 2;
        }
    }

    /**
     * Decide que carta tirar del jugador actual
     *
     * @return id de la carta a tirar
     */
    public int getCartaCPU() {
        //<editor-fold defaultstate="collapsed" desc="Code">
        Jugador j = tab.getJug(tab.getTurno());
        j.ordena(tab.getPalotriunfo());
        if (tab.s(tab.getTurno()) == tab.getUltBaza()) {
            if (tab.isJuegoDe4()) {
                switch (tab.quienMata(tab.cartasMesa[0], tab.cartasMesa[1], tab.cartasMesa[2])) {
                    case 1:
                        List<Integer> g = j.guinotes(tab.getPalotriunfo());
                        if (!g.isEmpty()) {
                            for (Integer i : g) {
                                if (tab.compruebaCarta(tab.getTurno(), i) == null) {
                                    return i;
                                }
                            }
                        }
                }
            }
            if (!tab.isArrastre()) {
                if (tab.getCartaMesa(tab.getUltBaza()).getPalo() != tab.getPalotriunfo()) {
                    int tengog = j.tengoGuinote(tab.getCartaMesa(tab.getUltBaza()));
                    if (tengog > -1
                            && tab.compruebaCarta(tab.getTurno(), tengog) == null) {
                        return tengog;
                    }
                }
            }
            if (tab.getCartaMesa(tab.getUltBaza()).isGuinote() && tab.getCartaMesa(tab.getUltBaza()).getPalo() != tab.getPalotriunfo()) {
                List<Integer> l = j.triunfosbajos(tab.getPalotriunfo());
                if (!l.isEmpty()) {
                    for (Integer i : l) {
                        if (tab.compruebaCarta(tab.getTurno(), i) == null) {
                            return i;
                        }
                    }
                }
            }
            String s = tab.compruebaCantes(tab.getTurno());
            if (s.length() > 0) {
                if (tab.getCartaMesa(tab.getUltBaza()).getPalo() != tab.getPalotriunfo()) {
                    String ret = j.puedeMatar(tab.getCartaMesa(tab.getUltBaza()));
                    if (ret != null) {
                        for (int i = 0; i < ret.length(); i++) {
                            if (ret.charAt(i) == '1') {
                                if (j.getMano()[i].getNumero() == 7
                                        || j.getMano()[i].getNumero() == 8) {
                                    if (!s.contains(j.getMano()[i].getPalo() + "")) {
                                        if (tab.compruebaCarta(tab.getTurno(), i) == null) {
                                            return i;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                List<Integer> l = j.triunfosbajos(tab.getPalotriunfo());
                if (!l.isEmpty()) {
                    for (Integer i : l) {
                        if (tab.compruebaCarta(tab.getTurno(), i) == null) {
                            return i;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 6; i++) {
            if (tab.compruebaCarta(j.getIdJug(), i) == null) {
                return i;
            }
        }
        return -1;
        //</editor-fold>
    }
    

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Métodos">
    /**
     * Realiza una acción de un jugador sobre el tablero devuelve objeto json
     * con mensajes asociados a los jugadores correspondientes
     *
     * @param idAcc id de la acción: 0=tirar carta(acompañada del string que
     * representa a la carta) 1=cantar 2=cambiar7 3=cambiar carta de sitio
     * @param params parametros de la acción
     * @param idjug id del jugador
     * @return GuiJson con los mensajes correspondientes a cada jugador
     */
    public GuiJson accionJug(int idAcc, String params, int idjug) {
        //<editor-fold defaultstate="collapsed" desc="Code">
        synchronized (tab) {
            String retorno = null;
            js.borrarMsgs();
            if (idAcc >= 0 && idAcc <= 3 && idjug >= 0 && idjug <= (tipoPartida ? 3 : 1)) {
                switch (idAcc) {
                    case 0:
                        if (params != null) {
                            int idcarta = Integer.parseInt(params.charAt(0) + "");
                            if (idcarta <= 5 && idcarta >= 0) {
                                retorno = tab.tirarCarta(idjug, idcarta);
                                if (retorno != null) {
                                    js.setMsg(retorno, idjug);
                                }
                            }
                        }
                        break;
                    case 1:
                        boolean b = false;
                        if (idjug == tab.getUltBaza() || (tipoPartida && tab.getUltBaza() == tab.s(tab.s(idjug)))) {
                            retorno = tab.compruebaMarcaCantes(idjug);
                            if (retorno.equals("")) {
                                js.setMsg("No tienes cantes", idjug);
                            } else {
                                for (int i = 0; i < retorno.length(); i++) {
                                    if (retorno.charAt(i) != 'n') {
                                        if (b) {
                                            if (retorno.charAt(i) == tab.getPalotriunfo()) {
                                                js.setMsg(js.getMsg(idjug) + " y las 40", idjug);
                                                js.setMsg(js.getMsg(tab.s(idjug)) + " y las 40", tab.s(idjug));

                                                if (tipoPartida) {
                                                    js.setMsg(js.getMsg(tab.s(tab.s(idjug))) + " y las 40", tab.s(tab.s(idjug)));
                                                    js.setMsg(js.getMsg(tab.s(tab.s(tab.s(idjug)))) + " y las 40", tab.s(tab.s(tab.s(idjug))));
                                                }
                                            } else {
                                                js.setMsg(js.getMsg(idjug) + " y las 20 en " + Utils.paloStr(retorno.charAt(i)), idjug);
                                                js.setMsg(js.getMsg(tab.s(idjug)) + " y las 20 en " + Utils.paloStr(retorno.charAt(i)), tab.s(idjug));

                                                if (tipoPartida) {
                                                    js.setMsg(js.getMsg(tab.s(tab.s(idjug))) + " y las 20 en " + Utils.paloStr(retorno.charAt(i)), tab.s(tab.s(idjug)));
                                                    js.setMsg(js.getMsg(tab.s(tab.s(tab.s(idjug)))) + " y las 20 en " + Utils.paloStr(retorno.charAt(i)), tab.s(tab.s(tab.s(idjug))));
                                                }
                                            }
                                        } else {
                                            if (retorno.charAt(i) == tab.getPalotriunfo()) {
                                                js.setMsg("Has cantado las 40", idjug);
                                                js.setMsg(tab.getJug(idjug).getNombre() + " ha cantado las 40", tab.s(idjug));
                                                if (tipoPartida) {
                                                    js.setMsg(tab.getJug(idjug).getNombre() + " ha cantado las 40", tab.s(tab.s(idjug)));
                                                    js.setMsg(tab.getJug(idjug).getNombre() + " ha cantado las 40", tab.s(tab.s(tab.s(idjug))));
                                                }

                                            } else {
                                                js.setMsg("Has cantado las 20 en " + Utils.paloStr(retorno.charAt(i)), idjug);
                                                js.setMsg(tab.getJug(idjug).getNombre() + " ha cantado las 20 en " + Utils.paloStr(retorno.charAt(i)), tab.s(idjug));

                                                if (tipoPartida) {
                                                    js.setMsg(tab.getJug(idjug).getNombre() + " ha cantado las 20 en " + Utils.paloStr(retorno.charAt(i)), tab.s(tab.s(idjug)));
                                                    js.setMsg(tab.getJug(idjug).getNombre() + " ha cantado las 20 en " + Utils.paloStr(retorno.charAt(i)), tab.s(tab.s(tab.s(idjug))));
                                                }
                                            }
                                            b = true;
                                        }
                                    }
                                }
                            }
                        } else {
                            js.setMsg("Necesitas baza reciente", idjug);
                        }
                        break;
                    case 2:
                        retorno = tab.cambiar7(idjug);
                        js.setMsg(retorno, idjug);
                        if (retorno.charAt(retorno.length() - 1) == '.') {
                            js.setMsg(tab.getJug(idjug).getNombre() + " ha cambiado el 7", tab.s(idjug));
                            if (tipoPartida) {
                                js.setMsg(tab.getJug(idjug).getNombre() + " ha cambiado el 7", tab.s(tab.s(idjug)));
                                js.setMsg(tab.getJug(idjug).getNombre() + " ha cambiado el 7", tab.s(tab.s(tab.s(idjug))));
                            }
                        }
                        break;
                    case 3:
                        if (params.contains(",")) {
                            tab.getJug(idjug).intercambiar(Integer.parseInt(params.charAt(0) + ""), Integer.parseInt(params.charAt(2) + ""));
                        }
                        break;

                }
                if (Utils.GUARDAR_PARTIDAS) {
                    if (!tabs.isEmpty()) {
                        if (!tabs.get(tabs.size() - 1).equals(getTab())) {
                            if (idAcc != 3) {
                                tabs.add(getTab().clone());
                            }
                        }
                    } else {
                        tabs.add(getTab().clone());
                    }
                }
                if (deVueltas) {
                    if (tab.getPuntuacion(0) > 100 || tab.getPuntuacion(1) > 100) {
                        terminada = true;
                    }
                }
                return js;
            }
            return js;
        }
        //</editor-fold>
    }

    public String accionJugJSON(int idAcc, String params, int idjug) {
        return Utils.GSON.toJson(accionJug(idAcc, params, idjug));
    }

    /**
     * si el tablero actual cambia lo guarda
     */
    private void guardar() {
        if (Utils.GUARDAR_PARTIDAS) {
            if (!tabs.isEmpty()) {
                if (tabs.get(tabs.size() - 1).equals(getTab())) {
                    return;
                }
                tabs.add(getTab().clone());
            } else {
                tabs.add(getTab().clone());
            }
        }
    }

    /**
     * llamar cuando estén todas (2 o 4) las cartas tiradas; decide el idJug del
     * ganador y lo devuelve, recoge las cartas a su respectiva baza, y hace
     * robar las cartas del mazo. si la partida ha terminado la marca como
     * terminada o crea un nuevo tablero si va de vueltas
     *
     * @return idJug del ganador de la jugada
     */
    public int ganadorDeLaJugada() {
        //<editor-fold defaultstate="collapsed" desc="Code">
        synchronized (tab) {
            int t = tab.ganadorDeLaJugada();
            guardar();
            if (tab.isFin()) {
                if (tab.getPuntuacion(0) < 101 && tab.getPuntuacion(1) < 101) {
                    //de vueltas
                    Tablero tabAnt = tab;
                    tab = new Tablero(tipoPartida, tab.s(t), new int[]{tabAnt.getPuntuacion(0), tabAnt.getPuntuacion(1)});
                    for (int i = 0; i < (tipoPartida ? 4 : 2); i++) {
                        tab.getJug(i).setNombre(tabAnt.getJug(i).getNombre());
                        tab.getJug(i).setIdDisp(tabAnt.getJug(i).getIdDisp());
                        tab.getJug(i).setInactivo(tabAnt.getJug(i).isInactivo());
                    }
                    deVueltas = true;
                    guardar();
                } else {
                    if (tab.getPuntuacion(0) > 100 && tab.getPuntuacion(1) > 100) {
                        ganadorDesemp = t;
                    }
                    terminada = true;
                }

            }
            if (deVueltas) {
                if (tab.getPuntuacion(0) > 100 || tab.getPuntuacion(1) > 100) {
                    terminada = true;
                }
            }
            return t;
        }
        //</editor-fold>
    }

    public String compruebaCarta(int idjug, int carta) {
        return tab.compruebaCarta(idjug, carta);
    }

    //</editor-fold>
}
