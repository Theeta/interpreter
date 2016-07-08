/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.ast.statements;

import com.mycompany.compiler.StatementVisitor;
import com.mycompany.compiler.ast.Expression;
import com.mycompany.compiler.ast.Statement;
import com.mycompany.compiler.ast.operands.VariableIdentifierOperand;

/**
 *
 * @author noora
 */
public class ForStatement extends Statement {
    public VariableIdentifierOperand variable;
    public Expression rangeStart;
    public Expression rangeEnd;
    public BlockStatement statements;
    
    public ForStatement(VariableIdentifierOperand variable, Expression rangeStart, Expression rangeEnd, BlockStatement statements, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.variable = variable;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.statements = statements;
    }
    
    
    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

}
