/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.ast.statements;

import com.mycompany.compiler.StatementVisitor;
import com.mycompany.compiler.ast.Expression;
import com.mycompany.compiler.ast.Statement;

/**
 *
 * @author noora
 */
public class AssertStatement extends Statement {
    public Expression expression;
    
    public AssertStatement(Expression expression, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.expression = expression;
    }
    
    
    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

}
