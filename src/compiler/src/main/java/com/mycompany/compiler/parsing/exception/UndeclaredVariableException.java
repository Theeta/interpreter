/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.parsing.exception;

/**
 *
 * @author noora
 */
public class UndeclaredVariableException extends ParseException {
    public final String variableName;
    public UndeclaredVariableException(String variableName) {
        this.variableName = variableName;
    }
}
