/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.ast.operands;

import com.mycompany.compiler.ASTVisitor;
import com.mycompany.compiler.ast.Expression;
import com.mycompany.compiler.ast.Operand;
import com.mycompany.compiler.ast.Type;

/**
 *
 * @author noora
 */
public class ExpressionOperand extends Operand {
    public Expression expression;
    
    public ExpressionOperand(Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public Type getType() {
        return expression.getType();
    }

}
