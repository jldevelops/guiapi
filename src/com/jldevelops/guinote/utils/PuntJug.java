/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jldevelops.guinote.utils;

/**
 *
 * @author Jorge
 */
public class PuntJug {
    private final String n;//nombre
    private final int tg;//totales ganadas
    private final int tp;//totales perdidas

    public PuntJug(String n, int tg, int tp) {
        this.n = n;
        this.tg = tg;
        this.tp = tp;
    }

    public int getTotalesGanadas() {
        return tg;
    }

    public int getTotalesPerdidas() {
        return tp;
    }
    
    public String getNombre(){
        return n;
    }
}
