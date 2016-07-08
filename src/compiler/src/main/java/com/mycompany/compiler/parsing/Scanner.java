package com.mycompany.compiler.parsing;

import com.mycompany.compiler.CharacterStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Scanner {

    private final CharacterStream stream;
    private Token lastToken;

    private static final Map<String, Token.Type> reservedKeywords;

    static {
        reservedKeywords = new HashMap<>();
        reservedKeywords.put("var", Token.Type.var);
        reservedKeywords.put("for", Token.Type.for_);
        reservedKeywords.put("end", Token.Type.end);
        reservedKeywords.put("in", Token.Type.in);
        reservedKeywords.put("do", Token.Type.do_);
        reservedKeywords.put("read", Token.Type.read);
        reservedKeywords.put("print", Token.Type.print);
        reservedKeywords.put("assert", Token.Type.assert_);
        reservedKeywords.put("bool", Token.Type.bool_keyword);
        reservedKeywords.put("int", Token.Type.int_keyword);
        reservedKeywords.put("string", Token.Type.string_keyword);
    }

    public Scanner(String program) {
        this.stream = new CharacterStream(program);
    }

    public Token lastToken() {
        return lastToken;
    }

    public void nextToken() throws CompilerError {
        try {
            lastToken = nextToken(this.stream);
        } catch (NullPointerException e) {
            lastToken = new Token(Token.Type.eof, stream.lineNumber(), stream.columnNumber());
        }
    }

    //The methods below are marked "private static" for clarity.
    private static Token nextToken(CharacterStream stream) throws CompilerError {
        stream.nextChar();

        //skip the whitespace
        skipWhile(stream, c -> isWhitespace(c));

        switch (stream.lastChar()) {
            //SINGLE CHAR TOKENS:
            case '+':
                return new Token(Token.Type.plus, stream.lineNumber(), stream.columnNumber());
            case '-':
                return new Token(Token.Type.minus, stream.lineNumber(), stream.columnNumber());
            case '*':
                return new Token(Token.Type.times, stream.lineNumber(), stream.columnNumber());
            case '&':
                return new Token(Token.Type.and, stream.lineNumber(), stream.columnNumber());
            case '<':
                return new Token(Token.Type.lessthan, stream.lineNumber(), stream.columnNumber());
            case '=':
                return new Token(Token.Type.equal, stream.lineNumber(), stream.columnNumber());
            case '(':
                return new Token(Token.Type.lparen, stream.lineNumber(), stream.columnNumber());
            case ')':
                return new Token(Token.Type.rparen, stream.lineNumber(), stream.columnNumber());
            case '!':
                return new Token(Token.Type.not, stream.lineNumber(), stream.columnNumber());
            case ';':
                return new Token(Token.Type.semicolon, stream.lineNumber(), stream.columnNumber());
            //STRING LITERAL
            case '"':
                return getStringLiteralToken(stream);
            //COLON OR ASSIGNMENT
            case ':':
                return getColonOrAssignmentToken(stream);
            //DIVISION OR COMMENT
            case '/':
                return getDivisionTokenOrSkipComment(stream);
            //RANGE
            case '.':
                return getRangeToken(stream);
            //INTEGER OR BOOL LITERAL, IDENTIFIER, KEYWORD
            default:
                return getLiteralOrIdentifierOrKeywordToken(stream);
        }
    }

    private static Token getStringLiteralToken(CharacterStream stream) {
        int lineNumber = stream.lineNumber();
        int columnNumber = stream.columnNumber();
        StringBuilder builder = new StringBuilder();
        stream.nextChar();
        while (stream.lastChar() != '"') {
            if (stream.lastChar() == '\\') {
                if (stream.peekChar() == 't') {
                    builder.append('\t');
                    stream.nextChar();
                    stream.nextChar();
                } else if (stream.peekChar() == 'n') {
                    builder.append('\n');
                    stream.nextChar();
                    stream.nextChar();
                } else {
                    builder.append(stream.lastChar());
                    stream.nextChar();
                    builder.append(stream.lastChar());
                }
            } else {
                builder.append(stream.lastChar());
                stream.nextChar();
            }
        }
        return new Token(Token.Type.string_literal, builder.toString(), lineNumber, columnNumber);
    }

    private static Token getColonOrAssignmentToken(CharacterStream stream) {
        int lineNumber = stream.lineNumber();
        int columnNumber = stream.columnNumber();
        if (stream.peekChar() == '=') {
            stream.nextChar();
            return new Token(Token.Type.assign, lineNumber, columnNumber);
        } else {
            return new Token(Token.Type.colon, lineNumber, columnNumber);
        }
    }

    private static Token getDivisionTokenOrSkipComment(CharacterStream stream) throws CompilerError {
        int lineNumber = stream.lineNumber();
        int columnNumber = stream.columnNumber();
        if (stream.peekChar() == '/') {
            //comment, skip this line
            skipWhile(stream, (c) -> c != '\n');
            return nextToken(stream);
        } else if (stream.peekChar() == '*') {
            throw new CompilerError(lineNumber, columnNumber, "Multi-line comments are not implemented yet. Sorry! :(");
            //TODO: multiline comment
            //return nextToken(stream);
        } else {
            return new Token(Token.Type.div, lineNumber, columnNumber);
        }
    }

    private static Token getRangeToken(CharacterStream stream) throws CompilerError {
        int lineNumber = stream.lineNumber();
        int columnNumber = stream.columnNumber();
        if (stream.peekChar() == '.') {
            stream.nextChar();
            return new Token(Token.Type.range, lineNumber, columnNumber);
        }
        throw new CompilerError(lineNumber, columnNumber, "Invalid token");
    }

    private static Token getLiteralOrIdentifierOrKeywordToken(CharacterStream stream) throws CompilerError {
        int lineNumber = stream.lineNumber();
        int columnNumber = stream.columnNumber();
        StringBuilder builder = new StringBuilder();
        //reserved keyword or boolean literal or identifier
        if (Character.isLetter(stream.lastChar())) {
            builder.append(stream.lastChar());
            while (Character.isLetterOrDigit(stream.peekChar()) || stream.peekChar() == '_') {
                stream.nextChar();
                builder.append(stream.lastChar());
            }

            if (reservedKeywords.containsKey(builder.toString())) {
                return new Token(reservedKeywords.get(builder.toString()), lineNumber, columnNumber);
            }

            if ("true".equals(builder.toString())) {
                return new Token(Token.Type.bool_literal, "true", lineNumber, columnNumber);
            }

            if ("false".equals(builder.toString())) {
                return new Token(Token.Type.bool_literal, "false", lineNumber, columnNumber);
            }

            return new Token(Token.Type.id, builder.toString(), lineNumber, columnNumber);
        }

        //integer literal
        if (Character.isDigit(stream.lastChar())) {
            builder.append(stream.lastChar());
            while (Character.isDigit(stream.peekChar())) {
                builder.append(stream.peekChar());
                stream.nextChar();
            }
            return new Token(Token.Type.int_literal, builder.toString(), lineNumber, columnNumber);
        } else {
            throw new CompilerError(lineNumber, columnNumber, "Unknown character");
        }
    }

    private static void skipWhile(CharacterStream stream, Predicate<Character> condition) {
        while (condition.test(stream.lastChar())) {
            stream.nextChar();
        }
    }

    private static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n';
    }
}
