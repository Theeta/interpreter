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
public class VariableReDeclarationException extends ParseException {
    public final String variableName;
    public VariableReDeclarationException(String variableName) {
        this.variableName = variableName;
    }
}
