/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.interpreter;

import com.mycompany.compiler.ASTVisitor;
import com.mycompany.compiler.StatementVisitor;
import com.mycompany.compiler.ast.AST;
import com.mycompany.compiler.ast.Expression;
import com.mycompany.compiler.ast.Operand;
import com.mycompany.compiler.ast.Statement;
import com.mycompany.compiler.ast.Type;
import com.mycompany.compiler.ast.expressions.*;
import com.mycompany.compiler.ast.operands.*;
import com.mycompany.compiler.ast.statements.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author noora
 */
public class Interpreter implements ASTVisitor<Value>, StatementVisitor, Runnable {

    private AST ast;

    private Map<String, Value> variables;
    
    private boolean running;
    
    private Scanner sc;

    public Interpreter(AST ast) {
        this.ast = ast;
        this.sc = new Scanner(System.in);
        variables = new HashMap<>();
    }

    @Override
    public void run() {
        this.running = true;
        for (Statement statement : ast.statements) {
            if (!running) {
                return;
            }
            statement.accept(this);
        }
    }

    private Value evaluate(Expression expr) {
        return expr.accept(this);
    }

    private Value evaluate(Operand oper) {
        return oper.accept(this);
    }

    @Override
    public Value visit(BinaryExpression expr) {
        Value leftValue = evaluate(expr.left);
        Value rightValue = evaluate(expr.right);
        switch (expr.operator) {
            case and:
                return evaluateAndExpression((BoolValue) leftValue, (BoolValue) rightValue);
            case divide:
                return evaluateDivideExpression((IntValue) leftValue, (IntValue) rightValue);
            case equals:
                return evaluateEqualsExpression(leftValue, rightValue);
            case less:
                return evaluateLessExpression((IntValue) leftValue, (IntValue) rightValue);
            case minus:
                return evaluateMinusExpression((IntValue) leftValue, (IntValue) rightValue);
            case plus:
                return evaluatePlusExpression((IntValue) leftValue, (IntValue) rightValue);
            case times:
                return evaluateTimesExpression((IntValue) leftValue, (IntValue) rightValue);
            default:
                return null;
        }
    }

    private Value evaluateAndExpression(BoolValue left, BoolValue right) {
        return new BoolValue(left.value && right.value);
    }

    private Value evaluateDivideExpression(IntValue left, IntValue right) {
        return new IntValue(left.value / right.value);
    }

    private Value evaluateEqualsExpression(Value left, Value right) {
        if (left instanceof IntValue) {
            return new BoolValue(((IntValue) left).value == ((IntValue) right).value);
        } else {
            return new BoolValue(((StringValue) left).value.equals(((StringValue) right).value));
        }
    }

    private Value evaluateLessExpression(IntValue left, IntValue right) {
        return new BoolValue(left.value < right.value);
    }

    private Value evaluateMinusExpression(IntValue left, IntValue right) {
        return new IntValue(left.value - right.value);
    }

    private Value evaluatePlusExpression(Value left, Value right) {
        if (left instanceof IntValue) {
            return new IntValue(((IntValue) left).value + ((IntValue) right).value);
        } else {
            return new StringValue(((StringValue) left).value + ((StringValue) right).value);
        }
    }

    private Value evaluateTimesExpression(IntValue left, IntValue right) {
        return new IntValue(left.value * right.value);
    }

    @Override
    public Value visit(UnaryExpression expr) {
        if (expr.inverted) {
            BoolValue value = (BoolValue) evaluate(expr);
            return new BoolValue(!value.value);
        }
        return evaluate(expr.operand);
    }

    @Override
    public Value visit(ExpressionOperand oper) {
        return evaluate(oper.expression);
    }

    @Override
    public Value visit(IntegerLiteralOperand oper) {
        return new IntValue(oper.value);
    }

    @Override
    public Value visit(StringLiteralOperand oper) {
        return new StringValue(oper.value);
    }

    @Override
    public Value visit(VariableIdentifierOperand oper) {
        return variables.get(oper.variableName);
    }

    @Override
    public void visit(AssertStatement stmt) {
        BoolValue value = (BoolValue)evaluate(stmt.expression);
        if (!value.value) {
            System.err.println("Assert failed on line " + stmt.lineNumber + ".");
            running = false;
        }
    }

    @Override
    public void visit(AssignStatement stmt) {
        variables.put(stmt.variable.variableName, evaluate(stmt.value));
    }

    @Override
    public void visit(BlockStatement stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(ForStatement stmt) {
        IntValue beginValue = (IntValue)evaluate(stmt.rangeStart);
        IntValue endValue = (IntValue)evaluate(stmt.rangeEnd);
        
        for (int i = beginValue.value; i<= endValue.value; i++) {
            variables.put(stmt.variable.variableName, new IntValue(i));
            for (Statement s : stmt.statements.statements) {
                s.accept(this);
            }
        }
    }

    @Override
    public void visit(PrintStatement stmt) {
        Value value = evaluate(stmt.expression);
        if (value instanceof StringValue) {
            System.out.print(((StringValue)value).value);
        } else if (value instanceof IntValue) {
            System.out.print(((IntValue)value).value);
        } else if (value instanceof BoolValue) {
            System.out.print(((BoolValue)value).value);
        }
    }

    @Override
    public void visit(ReadStatement stmt) {
        if (stmt.target.variableType == Type.INT) {
            int value = Integer.parseInt(sc.nextLine());
            variables.put(stmt.target.variableName, new IntValue(value));
        } else {
            //string
            String value = sc.nextLine().split(" ")[0];
            variables.put(stmt.target.variableName, new StringValue(value));
        }
    }

    @Override
    public void visit(VariableDeclarationStatement stmt) {
        Value initialValue;
        if (stmt.initialValue == null) {
            if (stmt.variable.variableType == Type.BOOL) {
                initialValue = new BoolValue(false);
            } else if (stmt.variable.variableType == Type.INT) {
                initialValue = new IntValue(0);
            } else {
                //string
                initialValue = new StringValue("");
            }
        } else {
            initialValue = evaluate(stmt.initialValue);
        }
        variables.put(stmt.variable.variableName, initialValue);
    }

}
