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
public class StringValue implements Value {

    public final String value;

    public StringValue(String value) {
        this.value = value;
    }

}
