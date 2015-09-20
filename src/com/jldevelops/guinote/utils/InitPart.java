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
public class InitPart {
    
    private PuntJug[] p;
    private boolean t;

    public InitPart(PuntJug[] p, boolean t) {
        this.p = p;
        this.t = t;
    }

    public PuntJug[] getPuntJug() {
        return p;
    }

    public boolean isJuegoDe4() {
        return t;
    }
    
}
