/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.parsing;

import com.mycompany.compiler.StatementDummyVisitor;
import com.mycompany.compiler.StatementVisitor;
import com.mycompany.compiler.ast.AST;
import com.mycompany.compiler.ast.Statement;
import com.mycompany.compiler.ast.Type;
import com.mycompany.compiler.ast.operands.VariableIdentifierOperand;
import com.mycompany.compiler.ast.statements.AssertStatement;
import com.mycompany.compiler.ast.statements.AssignStatement;
import com.mycompany.compiler.ast.statements.BlockStatement;
import com.mycompany.compiler.ast.statements.ForStatement;
import com.mycompany.compiler.ast.statements.PrintStatement;
import com.mycompany.compiler.ast.statements.ReadStatement;
import com.mycompany.compiler.ast.statements.VariableDeclarationStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validator {

    public static List<CompilerError> validate(AST ast) {
        
        List<CompilerError> errors = new ArrayList<>();

        Map<String, VariableIdentifierOperand> variables = new HashMap<>();

        for (Statement statement : ast.statements) {
            statement.accept(new StatementVisitor() {
                @Override
                public void visit(AssertStatement stmt) {
                    if (stmt.expression.getType() != Type.BOOL) {
                        errors.add(new CompilerError(stmt.lineNumber, stmt.columnNumber, "Can't use assert with an argument of type " + stmt.expression.getType().toString()));
                    }
                }

                @Override
                public void visit(AssignStatement stmt) {
                    if (stmt.value.getType() != stmt.variable.variableType) {
                        errors.add(new CompilerError(stmt.lineNumber, stmt.columnNumber, "Can't assign a value of type " + stmt.value.getType() + " to variable of type " + stmt.variable.variableType));
                    }
                }

                @Override
                public void visit(BlockStatement stmt) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void visit(ForStatement stmt) {
                    stmt.statements.statements.forEach(statement -> statement.accept(new StatementDummyVisitor() {
                        @Override
                        public void visit(AssignStatement assignStmt) {
                            if (assignStmt.variable.variableName.equals(stmt.variable.variableName)) {
                                errors.add(new CompilerError(statement.lineNumber, statement.columnNumber, "Can't assign to control variable '" + stmt.variable.variableName + "' inside for-loop."));
                            }
                        }
                    }));
                }

                @Override
                public void visit(PrintStatement stmt) {
                }

                @Override
                public void visit(ReadStatement stmt) {
                    if (stmt.target.variableType == Type.BOOL) {
                        errors.add(new CompilerError(stmt.lineNumber, stmt.columnNumber, "Can't use read with a variable of type " + Type.BOOL));
                    }
                }

                @Override
                public void visit(VariableDeclarationStatement stmt) {
                    variables.put(stmt.variable.variableName, stmt.variable);
                }
            });
        }
        
        return errors;
    }
}
