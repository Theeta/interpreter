/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.ast.operands;

import com.mycompany.compiler.ASTVisitor;
import com.mycompany.compiler.ast.Operand;
import com.mycompany.compiler.ast.Type;

/**
 *
 * @author noora
 */
public class VariableIdentifierOperand extends Operand {
    
    public VariableIdentifierOperand(String variableName, Type variableType) {
        this.variableName = variableName;
        this.variableType = variableType;
    }
    
    public String variableName;
    public Type variableType;
    
    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public Type getType() {
        return variableType;
    }

}
