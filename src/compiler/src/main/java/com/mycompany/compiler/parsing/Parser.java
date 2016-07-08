/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.parsing;

import com.mycompany.compiler.parsing.exception.ParseException;
import com.mycompany.compiler.ast.*;
import com.mycompany.compiler.ast.expressions.*;
import com.mycompany.compiler.ast.operands.*;
import com.mycompany.compiler.ast.statements.*;
import com.mycompany.compiler.parsing.exception.UndeclaredVariableException;
import com.mycompany.compiler.parsing.exception.VariableReDeclarationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author noora
 */
public class Parser {

    private Scanner scanner;
    private Map<String, VariableIdentifierOperand> variables;
    private List<Error> errors;

    public Parser() {

    }

    public AST parse(String program) throws CompilerError {
        errors = new ArrayList<>();
        variables = new HashMap<>();
        scanner = new Scanner(program);
        scanner.nextToken();
        AST ast = new AST();

        Statement stmt;
        while ((stmt = nextStatement()) != null) {
            ast.statements.add(stmt);
        }

        return ast;
    }

    private Statement nextStatement() throws CompilerError {
        Statement stmt;
        switch (scanner.lastToken().type) {
            case var:
                stmt = getVariableDeclarationStatement();
                break;
            case for_:
                stmt = getForStatement();
                break;
            case print:
                stmt = getPrintStatement();
                break;
            case read:
                stmt = getReadStatement();
                break;
            case assert_:
                stmt = getAssertStatement();
                break;
            case id:
                stmt = getAssignmentStatement();
                break;
            case eof:
                return null;
            default:
                throw new CompilerError(scanner.lastToken().line, scanner.lastToken().column, "Not a statement");
        }
        expect(Token.Type.semicolon);
        return stmt;
    }

    private Token expect(Token.Type type) throws CompilerError {
        Token token = scanner.lastToken();
        if (token.type != type) {
            throw new CompilerError(token.line, token.column, "Unexpected token. Expected: " + type + ", was: " + token.type);
        }
        scanner.nextToken();
        return token;
    }

    private Token expect(Token.Type... types) {
        Token token = scanner.lastToken();
        for (Token.Type type : types) {
            if (type == token.type) {
                scanner.nextToken();
                return token;
            }
        }
        throw new IllegalStateException("Unexpected token.");
    }

    private Statement getVariableDeclarationStatement() throws CompilerError {
        Token begin = expect(Token.Type.var);
        Token id = expect(Token.Type.id);
        expect(Token.Type.colon);
        Token type = expect(Token.Type.int_keyword, Token.Type.string_keyword, Token.Type.bool_keyword);

        Expression initialValue = null;
        try {
            if (scanner.lastToken().type == Token.Type.assign) {
                scanner.nextToken();
                initialValue = getExpression();
            }

        } catch (UndeclaredVariableException e) {
            handle(e, begin.line, begin.column);
        }

        try {
            switch (type.type) {
                case int_keyword:
                    declareVariable(id.content, Type.INT);
                    break;
                case bool_keyword:
                    declareVariable(id.content, Type.BOOL);
                    break;
                case string_keyword:
                    declareVariable(id.content, Type.STRING);
                    break;
            }
        } catch (VariableReDeclarationException e) {
            handle(e, begin.line, begin.column);
        }

        try {
            return new VariableDeclarationStatement(getVariable(id.content), initialValue, begin.line, begin.column);
        } catch (UndeclaredVariableException e) {
            handle(e, begin.line, begin.column);
            return null;
        }
    }

    private Statement getForStatement() throws CompilerError {
        Token begin = expect(Token.Type.for_);
        try {
            Token id = expect(Token.Type.id);
            expect(Token.Type.in);
            Expression rangeBegin = getExpression();
            expect(Token.Type.range);
            Expression rangeEnd = getExpression();
            expect(Token.Type.do_);
            BlockStatement statements = new BlockStatement();
            while (scanner.lastToken().type != Token.Type.end) {
                statements.statements.add(nextStatement());
            }
            expect(Token.Type.end);
            expect(Token.Type.for_);

            return new ForStatement(getVariable(id.content), rangeBegin, rangeEnd, statements, begin.line, begin.column);
        } catch (UndeclaredVariableException e) {
            handle(e, begin.line, begin.column);
            return null;
        }
    }

    private Statement getPrintStatement() throws CompilerError {
        Token begin = expect(Token.Type.print);
        Expression expr;
        try {
            expr = getExpression();
        } catch (UndeclaredVariableException e) {
            handle(e, begin.line, begin.column);
            return null;
        }

        return new PrintStatement(expr, begin.line, begin.column);
    }

    private Statement getReadStatement() throws CompilerError {
        Token begin = expect(Token.Type.read);
        Token id = expect(Token.Type.id);
        try {
            return new ReadStatement(getVariable(id.content), begin.line, begin.column);
        } catch (UndeclaredVariableException e) {
            handle(e, begin.line, begin.column);
            return null;
        }
    }

    private Statement getAssertStatement() throws CompilerError {
        Token begin = expect(Token.Type.assert_);
        expect(Token.Type.lparen);
        Expression expr = null;
        try {
            expr = getExpression();
        } catch (UndeclaredVariableException e) {
            handle(e, begin.line, begin.column);
            return null;
        }
        expect(Token.Type.rparen);
        return new AssertStatement(expr, begin.line, begin.column);
    }

    private Statement getAssignmentStatement() throws CompilerError {
        Token targetId = expect(Token.Type.id);
        expect(Token.Type.assign);
        try {
            Expression expr = getExpression();
            return new AssignStatement(getVariable(targetId.content), expr, targetId.line, targetId.column);
        } catch (UndeclaredVariableException e) {
            handle(e, targetId.line, targetId.column);
            return null;
        }
    }

    private Operand getOperand() throws CompilerError, UndeclaredVariableException {
        //Note that we are using 'expect' to skip the tokens just to be explicit about them
        Operand oper;
        switch (scanner.lastToken().type) {
            case int_literal:
                oper = new IntegerLiteralOperand(Integer.parseInt(scanner.lastToken().content));
                expect(Token.Type.int_literal);
                return oper;
            case string_literal:
                oper = new StringLiteralOperand(scanner.lastToken().content);
                expect(Token.Type.string_literal);
                return oper;
            case id:
                oper = getVariable(scanner.lastToken().content);
                expect(Token.Type.id);
                return oper;
            case lparen:
                expect(Token.Type.lparen);
                oper = new ExpressionOperand(getExpression());
                expect(Token.Type.rparen);
                return oper;
            default:
                throw new CompilerError(scanner.lastToken().line, scanner.lastToken().column, "Invalid token for operand");
        }
    }

    private Expression getExpression() throws CompilerError, UndeclaredVariableException {
        if (scanner.lastToken().type == Token.Type.not) {
            UnaryExpression expression = new UnaryExpression();
            expression.inverted = true;
            expression.operand = getOperand();
            return expression;
        } else {
            Operand op = getOperand();
            if (scanner.lastToken().isBinaryOperator()) {
                BinaryExpression expression = new BinaryExpression();
                expression.left = op;
                switch (scanner.lastToken().type) {
                    case plus:
                        expression.operator = BinaryExpression.Operator.plus;
                        break;
                    case minus:
                        expression.operator = BinaryExpression.Operator.minus;
                        break;
                    case times:
                        expression.operator = BinaryExpression.Operator.times;
                        break;
                    case div:
                        expression.operator = BinaryExpression.Operator.divide;
                        break;
                    case equal:
                        expression.operator = BinaryExpression.Operator.equals;
                        break;
                    case lessthan:
                        expression.operator = BinaryExpression.Operator.less;
                        break;
                    case and:
                        expression.operator = BinaryExpression.Operator.and;
                        break;
                    default:
                        throw new IllegalStateException("Unknown operator");
                }
                scanner.nextToken();
                expression.right = getOperand();
                return expression;
            } else {
                UnaryExpression expression = new UnaryExpression();
                expression.inverted = false;
                expression.operand = op;
                return expression;
            }
        }
    }

    private void declareVariable(String id, Type type) throws VariableReDeclarationException {
        if (variables.containsKey(id)) {
            throw new VariableReDeclarationException(id);
        }
        variables.put(id, new VariableIdentifierOperand(id, type));
    }

    private VariableIdentifierOperand getVariable(String id) throws UndeclaredVariableException {
        if (!variables.containsKey(id)) {
            throw new UndeclaredVariableException(id);
        }
        return variables.get(id);
    }

    private void handle(UndeclaredVariableException e, int line, int column) throws CompilerError {
        throw new CompilerError(line, column, "Variable " + e.variableName + " has not been declared.");
    }

    private void handle(VariableReDeclarationException e, int line, int column) throws CompilerError {
        throw new CompilerError(line, column, "Variable " + e.variableName + " has already been declared.");
    }

}
