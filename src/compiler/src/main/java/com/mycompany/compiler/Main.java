/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler;

import com.mycompany.compiler.ast.AST;
import com.mycompany.compiler.interpreter.Interpreter;
import com.mycompany.compiler.parsing.CompilerError;
import com.mycompany.compiler.parsing.Parser;
import com.mycompany.compiler.parsing.Validator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author noora
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: Give path to minipl source file as the only argument");
            return;
        }
        String program;
        try {
            String filename = args[0];
            program = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Error reading source file");
            return;
        }
        
        Parser parser = new Parser();
        AST ast;
        try {
            ast = parser.parse(program);
        } catch (CompilerError error) {
            System.err.println("Parse error: ");
            printError(error);
            return;
        }
        
        List<CompilerError> errors = Validator.validate(ast);
        if (!errors.isEmpty()) {
            System.err.println(errors.size()+ " error(s) were found:");
            for (CompilerError error : errors) {
                printError(error);
            }
            return;
        }

        Interpreter interpreter = new Interpreter(ast);
        interpreter.run();
        
    }
    
    private static void printError(CompilerError error) {
        System.err.println("(line " + error.line + ", column " +  error.column + "): " + error.message);
    }

}
