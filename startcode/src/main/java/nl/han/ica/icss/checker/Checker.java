package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;

public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public Checker() {
        this.variableTypes = new HANLinkedList<>();
    }

    //Add stylesheet to AST root to start the AST tree
    public void check(AST ast) {
        this.checkStylesheet(ast.root);
    }

    //Stylesheet checks VariableAssignment or StyleRule
    public void checkStylesheet(ASTNode astNode) {
        Stylesheet stylesheet = (Stylesheet) astNode;

        variableTypes.addFirst(new HashMap<String, ExpressionType>());

        for (ASTNode child : stylesheet.getChildren()) {
            if (child instanceof VariableAssignment) {
                checkVariableAssignment(child);
            }
            if (child instanceof Stylerule) {
                checkStylerule(child);
            }
        }
        variableTypes.removeFirst();
    }

    //Checks the contents of a stylerule; this can be Declaration, IfClause or VariableAssignment
    public void checkStylerule(ASTNode astNode) {
        Stylerule stylerule = (Stylerule) astNode;

        variableTypes.addFirst(new HashMap<String, ExpressionType>());

        for (ASTNode node : stylerule.body) {
            if (node instanceof Declaration) {
                checkDeclaration(node);
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

    //Checks if the to be declared value is of the right expression
    public void checkDeclaration(ASTNode astNode) {
        Declaration declaration = (Declaration) astNode;
        ExpressionType expressionType = this.checkExpression(declaration.expression);
        switch(declaration.property.name) {
            case "color":
            case "background-color":
                if (expressionType != ExpressionType.COLOR) {
                    declaration.setError("STOP-KLEUR");
                }
                break;
            case "height":
            case "width":
                if (expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                    System.out.println(expressionType);
                    System.out.println("STOP-BREEDTE");
                    declaration.setError("STOP-BREEDTE");
                }
                break;
            default:
                declaration.setError("STOP-DEFAULT");
                break;
        }
    }

    //returns the ExpressionType of an expressionNode
    public ExpressionType checkExpression(ASTNode expressionNode) {
        if (expressionNode instanceof VariableReference) {
            return checkVariableReference((VariableReference) expressionNode);
        }else if(expressionNode instanceof Operation){
            return checkOperation((Operation) expressionNode);
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

    //Adds an assigned variable to the Hashmap
    public void checkVariableAssignment(ASTNode astNode){
        ExpressionType expressionType = checkExpression(((VariableAssignment) astNode).expression);
        variableTypes.getFirst().put((((VariableAssignment) astNode).name.name), expressionType);
    }

    //Checks the hashmap and returns the reference of a given variable equal to containsKey
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

    //Checks if the operation is a valid operation
    public ExpressionType checkOperation(Operation operation){
        ExpressionType left;
        ExpressionType right;

        if(operation.lhs instanceof Operation){
            left = checkOperation((Operation) operation.lhs);
        }   else if(operation.lhs instanceof VariableReference){
            left = checkVariableReference((VariableReference)operation.lhs);
        } else {
            left = checkExpression(operation.lhs);
        }
        if(operation.rhs instanceof Operation){
            right = checkOperation((Operation) operation.rhs);
        }   else if(operation.rhs instanceof VariableReference){
            right = checkVariableReference((VariableReference)operation.rhs);
        } else {
            right = checkExpression(operation.rhs);
        }
        if(left == ExpressionType.COLOR || right == ExpressionType.COLOR){
            System.out.println("colours are not allowed");
            operation.setError("colours are not allowed");
            return ExpressionType.UNDEFINED;
        }
        if(operation instanceof AddOperation || operation instanceof SubtractOperation){
            if(left != right){
                System.out.println("You can't mix and match");
                operation.setError("You can't mix and match");
                System.out.println(left);
                System.out.println(right);
                return ExpressionType.UNDEFINED;
            }
            return left;
        }else if(operation instanceof MultiplyOperation){
            if(left != ExpressionType.SCALAR && right != ExpressionType.SCALAR){
                operation.setError("missing scalar");
                return ExpressionType.UNDEFINED;
            }
            if(left != ExpressionType.SCALAR){
                return left;
            } else {
                return right;
            }
        }
        return ExpressionType.UNDEFINED;
    }

    //Checks the expression of an ifClause and the body of an ifClause. If there's an elseClause, run checkElseClause()
    public void checkIfClause(ASTNode astNode){
        IfClause ifClause = (IfClause) astNode;
        variableTypes.addFirst(new HashMap<String, ExpressionType>());
        if(astNode instanceof IfClause){
            ExpressionType expressionType = checkExpression(((IfClause) astNode).getConditionalExpression());
                if(expressionType != ExpressionType.BOOL){
                    astNode.setError("expression has to be a BOOOOOooooOoooOlian");
                }
        }
        for (ASTNode node : ifClause.body) {
            if (node instanceof Declaration) {
                checkDeclaration(node);
            }
            if (node instanceof IfClause) {
                checkIfClause(node);
            }
            if (node instanceof VariableAssignment) {
                checkVariableAssignment(node);
            }
        }
        variableTypes.removeFirst();
        if(ifClause.elseClause != null){
            checkElseClause(ifClause.elseClause);
        }
    }

    //Checks the body of an elseClause
    public void checkElseClause(ElseClause elseClause){
        variableTypes.addFirst(new HashMap<String, ExpressionType>());
        for (ASTNode node : elseClause.body) {
            if (node instanceof Declaration) {
                checkDeclaration(node);
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
}
