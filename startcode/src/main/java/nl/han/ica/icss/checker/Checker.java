package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.beans.Expression;
import java.util.HashMap;

public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        this.checkStylesheet(ast.root);
    }

    public void checkStylesheet(ASTNode astNode){
        Stylesheet stylesheet = (Stylesheet) astNode;

        for(ASTNode child : stylesheet.getChildren()){
            if(child instanceof VariableAssignment){
               // System.out.println("Variable");
                checkVariableAssignment(child);
            }
            if(child instanceof Stylerule){
                //System.out.println("Style");
                checkStylerule(child);
            }
        }
    }

    public void checkStylerule(ASTNode astNode){
        Stylerule stylerule = (Stylerule) astNode;
        for(ASTNode node : stylerule.body){
            if(node instanceof Declaration){
                //System.out.println("declaration");
                checkDeclaration(node);
            }
            if(node instanceof IfClause){
                //System.out.println("ifClause");
                checkIfClause(node);
            }
        }
    }

    //?
    public void checkDeclaration(ASTNode astNode){
        Declaration declaration = (Declaration) astNode;
        ExpressionType expressionType = this.checkExpression(declaration.expression);
        switch (declaration.property.name){
            case "color":
                if(expressionType != expressionType.COLOR) {
                    astNode.setError("STOP-KLEUR");
                }
                break;
            case "background-color":
                if(expressionType != expressionType.COLOR) {
                    astNode.setError("STOP-KLEUR");
                }
                break;
            case "height":
                if(expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                    astNode.setError("STOP-HOOGTE");
                }
                break;
            case "width":
                if(expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                    astNode.setError("STOP-BREEDTE");
                }
                break;
            default:
                astNode.setError("STOP-DEFAULT");
                break;

        }
        //check Operation
        System.out.println(checkExpression(astNode));
    }

    // .java
    public ExpressionType checkExpression(ASTNode expressionNode){
        //Expression expression = (Expression) astNode;
        if (expressionNode instanceof PercentageLiteral) {
            System.out.println("contains %");
            return ExpressionType.PERCENTAGE;
        } else if (expressionNode instanceof PixelLiteral) {
            System.out.println("contains px");
            return ExpressionType.PIXEL;
        } else if (expressionNode instanceof ColorLiteral) {
            System.out.println("contains #");
            return ExpressionType.COLOR;
        } else if (expressionNode instanceof ScalarLiteral) {
            System.out.println("contains scalar");
            return ExpressionType.SCALAR;
        } else if (expressionNode instanceof BoolLiteral) {
            System.out.println("contains bool");
            return ExpressionType.BOOL;
        }
        System.out.println(expressionNode);
        return ExpressionType.UNDEFINED;
    }

    //separate .java
    public void checkVariableAssignment(ASTNode astNode){
    VariableAssignment variableAssignment = (VariableAssignment) astNode;
        //check VariableRef
        //check expression (literals)
    }

    public Expression checkVariableReference(ASTNode astNode){
    VariableReference variableReference = (VariableReference) astNode;
        //check of variable bestaat
    return null;
    }


    //separate .java
    public void checkIfClause(ASTNode astNode){
        System.out.println("ifClauseChecked");
        //check of de if-clause wel bool is
        //check ...
        //if elseclause bestaat, check else clause
    }

    public void checkElseClause(ASTNode astNode){
        //check stylerule
    }

    //separate .java
    public void checkOperations(ASTNode astNode){
        //check of de operation regels wel mogen
    }
}
