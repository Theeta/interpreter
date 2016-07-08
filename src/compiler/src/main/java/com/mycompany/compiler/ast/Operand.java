/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.ast;

import com.mycompany.compiler.ASTVisitor;

/**
 *
 * @author noora
 */
public abstract class Operand {
    public abstract <R> R accept(ASTVisitor<R> visitor);
    public abstract Type getType();
}
