package com.mycompany.compiler;

import com.mycompany.compiler.ast.statements.AssertStatement;
import com.mycompany.compiler.ast.statements.AssignStatement;
import com.mycompany.compiler.ast.statements.BlockStatement;
import com.mycompany.compiler.ast.statements.ForStatement;
import com.mycompany.compiler.ast.statements.PrintStatement;
import com.mycompany.compiler.ast.statements.ReadStatement;
import com.mycompany.compiler.ast.statements.VariableDeclarationStatement;

public interface StatementVisitor {
    public void visit(AssertStatement stmt);
    public void visit(AssignStatement stmt);
    public void visit(BlockStatement stmt);
    public void visit(ForStatement stmt);
    public void visit(PrintStatement stmt);
    public void visit(ReadStatement stmt);
    public void visit(VariableDeclarationStatement stmt);
}
