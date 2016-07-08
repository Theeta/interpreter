/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.ast;

import com.mycompany.compiler.StatementVisitor;

/**
 *
 * @author noora
 */
public abstract class Statement {
    
    public Statement(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
    
    public final int lineNumber;
    public final int columnNumber;

    public abstract void accept(StatementVisitor visitor);
}
