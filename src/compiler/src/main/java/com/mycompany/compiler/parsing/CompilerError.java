/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.parsing;

/**
 *
 * @author noora
 */
public class CompilerError extends Throwable {
    
    public final int line;
    public final int column;
    public final String message;
    
    public CompilerError(int line, int column, String message) {
        this.line = line;
        this.column = column;
        this.message = message;
    }
}
