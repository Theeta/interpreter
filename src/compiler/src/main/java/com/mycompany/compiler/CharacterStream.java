/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler;

/**
 *
 * @author noora
 */
public class CharacterStream {
    private final String str;
    private int index;
    private int line;
    private int column;
    private Character lastChar;
    
    public CharacterStream(String str) {
        this.str = str;
        index = 0;
        line = 1;
        column = 1;
    }
    
    public Character lastChar() {
        return lastChar;
    }
    
    public void nextChar() {
        if (endOfStream()) {
            lastChar = null;
            return;
        }
        lastChar = str.charAt(index++);
        if (lastChar == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
    }
    
    public Character peekChar() {
        if (endOfStream()) {
            return null;
        }
        return str.charAt(index);
    }
    
    public boolean endOfStream() {
        return index >= str.length();
    }
    
    public int lineNumber() {
        return line;
    }
    
    public int columnNumber() {
        return column;
    }
}
