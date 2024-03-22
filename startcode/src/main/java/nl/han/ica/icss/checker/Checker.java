package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Stylesheet;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;

public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        this.checkStylesheet(ast.root);
    }

    public void checkStylesheet(ASTNode astNode){
        Stylesheet stylesheet = (Stylesheet) astNode;

    }

    public void checkStylerule(ASTNode astNode){

    }

    public void checkVariableAssignment(){

    }

    public void checkDeclaration(){

    }


}
