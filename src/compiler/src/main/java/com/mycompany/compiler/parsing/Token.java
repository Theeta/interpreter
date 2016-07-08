/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.parsing;

/**
 *
 * @author noora
 */
public class Token {

    public enum Type {

        plus, // +
        minus, // -
        times, // *
        div, // /
        lessthan, // <
        equal, // =
        and, // &
        not, // !
        lparen, // (
        rparen, // )
        assign, // :=
        range, // ..

        var,
        for_, // for
        end,
        in,
        do_, // do
        read,
        print,
        assert_, // assert
        
        colon, // :
        semicolon, // ;
        
        int_keyword, // int
        string_keyword, // string
        bool_keyword, //bool

        int_literal,
        string_literal,
        bool_literal,
        
        id,
        eof //end of file
    }
    
    public Token(Type type, String content, int line, int column) {
        this.type = type;
        this.content = content;
        this.line = line;
        this.column = column;
    }
    
    public Token(Type type, int line, int column) {
        this.type = type;
        this.line = line;
        this.column = column;
        switch(type) {
            case plus:
                this.content = "+";
                return;
            case minus:
                this.content = "-";
                return;
            case times:
                this.content = "*";
                return;
            case div:
                this.content = "/";
                return;
            case lessthan:
                this.content = "<";
                return;
            case equal:
                this.content = "=";
                return;
            case and:
                this.content = "&";
                return;
            case not:
                this.content = "!";
                return;
            case lparen:
                this.content = "(";
                return;
            case rparen:
                this.content = ")";
                return;
            case assign:
                this.content = ":=";
                return;
            case range:
                this.content = "..";
                return;
            case var:
                this.content = "var";
                return;
            case for_:
                this.content = "for";
                return;
            case end:
                this.content = "end";
                return;
            case in:
                this.content = "in";
                return;
            case do_:
                this.content = "do";
                return;
            case read:
                this.content = "read";
                return;
            case print:
                this.content = "print";
                return;
            case assert_:
                this.content = "assert";
                return;
            case colon:
                this.content = ":";
                return;
            case semicolon:
                this.content = ";";
                return;
            case int_keyword:
                this.content = "int";
                return;
            case string_keyword:
                this.content = "string";
                return;
            case bool_keyword:
                this.content = "bool";
                return;
            case eof:
                this.content = "&&";
                return;
            default:
                throw new IllegalArgumentException("Content must be provided for tokens of type " + type);
                
        }
    }
    
    public boolean isBinaryOperator() {
        switch (this.type) {
            case plus:
            case minus:
            case times:
            case div:
            case lessthan:
            case equal:
            case and:
                return true;
            default:
                return false;
        }
    }
    
    @Override
    public String toString() {
        return this.type.toString() + ": " + this.content;
    }

    public final Type type;
    public final String content;

    public final int line;
    public final int column;

}
