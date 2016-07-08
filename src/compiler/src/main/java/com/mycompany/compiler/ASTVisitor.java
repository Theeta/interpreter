/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler;

import com.mycompany.compiler.ast.expressions.*;
import com.mycompany.compiler.ast.operands.*;
import com.mycompany.compiler.ast.statements.*;

/**
 *
 * @author noora
 * @param <R>
 */
public interface ASTVisitor<R> {
    //expressions
    public R visit(BinaryExpression expr);
    public R visit(UnaryExpression expr);
    
    //operands
    public R visit(ExpressionOperand oper);
    public R visit(IntegerLiteralOperand oper);
    public R visit(StringLiteralOperand oper);
    public R visit(VariableIdentifierOperand oper);
    
}
