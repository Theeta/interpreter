/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler;

import com.mycompany.compiler.ast.statements.AssertStatement;
import com.mycompany.compiler.ast.statements.AssignStatement;
import com.mycompany.compiler.ast.statements.BlockStatement;
import com.mycompany.compiler.ast.statements.ForStatement;
import com.mycompany.compiler.ast.statements.PrintStatement;
import com.mycompany.compiler.ast.statements.ReadStatement;
import com.mycompany.compiler.ast.statements.VariableDeclarationStatement;

public class StatementDummyVisitor implements StatementVisitor {

    @Override
    public void visit(AssertStatement stmt) {
    }

    @Override
    public void visit(AssignStatement stmt) {
    }

    @Override
    public void visit(BlockStatement stmt) {
    }

    @Override
    public void visit(ForStatement stmt) {
    }

    @Override
    public void visit(PrintStatement stmt) {
    }

    @Override
    public void visit(ReadStatement stmt) {
    }

    @Override
    public void visit(VariableDeclarationStatement stmt) {
    }
    
}
