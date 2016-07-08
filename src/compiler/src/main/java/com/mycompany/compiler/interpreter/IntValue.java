/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.interpreter;

/**
 *
 * @author noora
 */
public class IntValue implements Value {

    public final int value;

    public IntValue(int value) {
        this.value = value;
    }
}
