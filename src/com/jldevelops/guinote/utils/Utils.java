/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jldevelops.guinote.utils;

import com.google.gson.Gson;

/**
 *
 * @author jorge
 */
public class Utils {

    public static final boolean GUARDAR_PARTIDAS = false;

    public static Gson GSON = new Gson();

    public static final String[] PALOS = new String[]{"oros", "copas", "espadas", "bastos"};

    public static final String PALOSC = "oceb";

    public static String paloStr(char palo) {
        return PALOSC.indexOf(palo) > -1?PALOS[PALOSC.indexOf(palo)]:"";
    }
    
    
}
