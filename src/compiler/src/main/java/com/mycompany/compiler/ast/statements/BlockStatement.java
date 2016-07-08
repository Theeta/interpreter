/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.compiler.ast.statements;

import com.mycompany.compiler.ast.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author noora
 */
public class BlockStatement {
    public final List<Statement> statements = new ArrayList();
    
    public void interpret() {
//        for (Statement statement : statements) {
//            statement.execute();
//        }
    }
}
