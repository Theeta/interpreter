/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.ast.expressions;

import com.mycompany.compiler.ASTVisitor;
import com.mycompany.compiler.ast.Expression;
import com.mycompany.compiler.ast.Operand;
import com.mycompany.compiler.ast.Type;

/**
 *
 * @author noora
 */
public class BinaryExpression extends Expression {

    public static enum Operator {
        equals, // =
        less, // <
        plus, // +
        minus, // -
        times, // *
        divide, // /
        and, // &
    }

    public Operand left;
    public Operand right;
    public Operator operator;

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public Type getType() {
        switch(operator) {
            case equals:
            case less:
            case and:
                return Type.BOOL;
            case minus:
            case times:
            case divide:
                return Type.INT;
            default: //plus
                return left.getType(); //assume left and right operand are of the same type
        }
    }

}
