package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.beans.Expression;
import java.util.HashMap;

public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public Checker() {
        this.variableTypes = new HANLinkedList<>();
    }

    public void check(AST ast) {
        this.checkStylesheet(ast.root);
    }

    public void checkStylesheet(ASTNode astNode) {
        Stylesheet stylesheet = (Stylesheet) astNode;

        variableTypes.addFirst(new HashMap<String, ExpressionType>());

        for (ASTNode child : stylesheet.getChildren()) {
            if (child instanceof VariableAssignment) {
                // System.out.println("Variable");
                checkVariableAssignment(child);
            }
            if (child instanceof Stylerule) {
                //System.out.println("Style");
                checkStylerule(child);
            }
        }
        variableTypes.removeFirst();
    }

    public void checkStylerule(ASTNode astNode) {
        Stylerule stylerule = (Stylerule) astNode;

        variableTypes.addFirst(new HashMap<String, ExpressionType>());

        for (ASTNode node : stylerule.body) {
            if (node instanceof Declaration) {
                checkDeclaration((Declaration) node);
            }
            if (node instanceof IfClause) {
                checkIfClause(node);
            }
            if (node instanceof VariableAssignment) {
                checkVariableAssignment(node);
            }
        }
        variableTypes.removeFirst();
    }

    public void checkDeclaration(Declaration declaration) {
        // Declaration declaration = (Declaration) declaration;
        ExpressionType expressionType = this.checkExpression(declaration.expression);
        switch(declaration.property.name)
    {
        case "color":
            if (expressionType != expressionType.COLOR) {
                declaration.setError("stop-kleur");
            }
            break;
        case "background-color":
            if (expressionType != expressionType.COLOR) {
                declaration.setError("STOP-KLEUR");
            }
            break;
        case "height":
            if (expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                declaration.setError("STOP-HOOGTE");
            }
            break;
        case "width":
            if (expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                declaration.setError("STOP-BREEDTE");
                System.out.println(declaration.expression);
            }
            break;
        default:
            declaration.setError("STOP-DEFAULT");
            break;
        }
    }


    public ExpressionType checkExpression(ASTNode expressionNode) {
        if (expressionNode instanceof VariableReference) {
            return checkVariableReference((VariableReference) expressionNode);
            }else if(expressionNode instanceof Operation){
            System.out.println(expressionNode);
                return checkOperation(expressionNode);
            }else if (expressionNode instanceof PercentageLiteral) {
                return ExpressionType.PERCENTAGE;
            } else if (expressionNode instanceof PixelLiteral) {
                return ExpressionType.PIXEL;
            } else if (expressionNode instanceof ColorLiteral) {
                return ExpressionType.COLOR;
            } else if (expressionNode instanceof ScalarLiteral) {
                return ExpressionType.SCALAR;
            } else if (expressionNode instanceof BoolLiteral) {
                return ExpressionType.BOOL;
            }
            return ExpressionType.UNDEFINED;
        }

    public void checkVariableAssignment(ASTNode astNode){
        ExpressionType expressionType = checkExpression(((VariableAssignment) astNode).expression);
        variableTypes.getFirst().put((((VariableAssignment) astNode).name.name), expressionType);
    }

    public ExpressionType checkVariableReference(VariableReference astNode) {
        ExpressionType expression = ExpressionType.UNDEFINED;
        for (int i = 0; i < variableTypes.getSize(); i++) {
            if(variableTypes.get(i).containsKey(astNode.name)) {
                expression = variableTypes.get(i).get(astNode.name);
                break;
            }
        }
        if(expression == ExpressionType.UNDEFINED){
            astNode.setError("niet gedefinieerd");
        }
        return expression;
    }

    public ExpressionType checkOperation(ASTNode astNode){
        if (astNode instanceof MultiplyOperation){
            ExpressionType left = getOperation(((MultiplyOperation)astNode).lhs);
            ExpressionType right = getOperation(((MultiplyOperation)astNode).rhs);
            if(left != ExpressionType.SCALAR && right != ExpressionType.SCALAR){
                astNode.setError("missing scalar");
            }
            return ExpressionType.UNDEFINED;
        }
        if (astNode instanceof AddOperation || astNode instanceof SubtractOperation) {
            if (getOperation(((AddOperation) astNode).lhs) != getOperation(((AddOperation) astNode).rhs)) {
                System.out.println("left:" + ((AddOperation) astNode).lhs);
                System.out.println("right:" + ((AddOperation) astNode).rhs);
                astNode.setError("You can't mix and match");
            }
            return ExpressionType.UNDEFINED;
        }
        if (astNode instanceof Operation) {
            for (ASTNode operationKind : astNode.getChildren()){
                if (operationKind instanceof ColorLiteral){
                    astNode.setError("Colour can not be used in an operation");
                }
            }
        }
        return getOperation(astNode);
    }

    public ExpressionType getOperation(ASTNode astNode){
        ExpressionType expressionType = ExpressionType.UNDEFINED;
        if(astNode instanceof Operation) {
            for (ASTNode operationKind : astNode.getChildren()) {
                if (operationKind instanceof Literal) {
                    expressionType = checkExpression(operationKind);
                    System.out.println(expressionType);
                } else if (operationKind instanceof VariableReference) {
                    expressionType = checkVariableReference((VariableReference) operationKind);
                    System.out.println(expressionType);
                }
            }
        }
        return expressionType;
    }

    public void checkIfClause(ASTNode astNode){
        System.out.println("ifClauseChecked");
        //check of de if-clause wel bool is
        //check ...
        //if elseclause bestaat, check else clause
    }

    public void checkElseClause(ASTNode astNode){
        //check stylerule
    }
}
