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
public class AssignStatement extends Statement {
    public VariableIdentifierOperand variable;
    public Expression value;
    
    public AssignStatement(VariableIdentifierOperand variable, Expression value, int lineNumber, int columnNumber) {
        super(lineNumber, columnNumber);
        this.variable = variable;
        this.value = value;
    }
    
    
    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

}
