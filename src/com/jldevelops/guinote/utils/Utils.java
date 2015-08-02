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
    
        public static int paloInt(char palo) {
        switch (palo) {
            case 'o':
                return 0;
            case 'c':
                return 1;
            case 'e':
                return 2;
            case 'b':
                return 3;
        }
        return -1;
    }
        
    public static String paloStr(char palo){
    	switch (palo) {
        case 'o':
            return "oros";
        case 'c':
            return "copas";
        case 'e':
            return "espadas";
        case 'b':
            return "bastos";
    }
    return "";
    }
    
    
}
