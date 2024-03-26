package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.datastructures.ListNode;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Evaluator implements Transform {

    private IHANLinkedList<HashMap<String, Literal>> variableValues;

    public Evaluator() {
        this.variableValues = new HANLinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        variableValues = new HANLinkedList<>();
        this.transformStylesheet(ast.root);
    }

    public void transformStylesheet(ASTNode astNode){
        ArrayList<ASTNode> toRemove = new ArrayList<>();

        variableValues.addFirst(new HashMap<>());

        for(ASTNode node : astNode.getChildren()){
            if(node instanceof VariableAssignment){
                transformVariableAssignment(node);
                toRemove.add(node);
            }
            if(node instanceof Stylerule){
                transformStylerule(node);
            }
        }
        variableValues.removeFirst();
        toRemove.forEach(astNode::removeChild);
    }

    public void transformStylerule(ASTNode astNode){
        Stylerule stylerule = (Stylerule) astNode;
        ArrayList<ASTNode> toAdd = new ArrayList<>();

        variableValues.addFirst(new HashMap<>());

        for(ASTNode node : stylerule.body) {
            if (node instanceof Declaration) {
                this.transformDeclaration(node);
                toAdd.add(node);
            }
            if (node instanceof VariableAssignment) {
                transformVariableAssignment(node);
            }
        }
        stylerule.body = toAdd;
        variableValues.removeFirst();
    }

    public void transformDeclaration(ASTNode astNode) {
        Declaration declaration = (Declaration) astNode;
        if (declaration.expression instanceof VariableReference) {
            declaration.expression = getLiteral(((VariableReference)declaration.expression).name);
        }
    }

    public void addVariableToLastScope(ASTNode astNode){

    }

    public void transformVariableAssignment(ASTNode astNode){
        VariableAssignment variableAssignment = (VariableAssignment) astNode;
        Literal literal;
        if(variableAssignment.expression instanceof Literal){
            literal = (Literal) variableAssignment.expression;
            variableValues.getFirst().put(((variableAssignment).name.name), literal);
        } if (variableAssignment.expression instanceof VariableReference){
            literal = getLiteral(((VariableReference) variableAssignment.expression).name);
            variableValues.getFirst().put(((variableAssignment).name.name), literal);
        }
    }

    public Literal getLiteral(String key){
    Literal literal = null;
        for (int i = 0; i < variableValues.getSize(); i++) {
            if (variableValues.get(i).containsKey(key)) {
                literal = variableValues.get(i).get(key);
                break;
            }
        }
        return literal;
    }

    public int returnLiteralValue(Literal literal){
        if(literal instanceof PixelLiteral){
            return ((PixelLiteral) literal).value;
        }
        else if(literal instanceof ScalarLiteral){
            return ((ScalarLiteral) literal).value;
        }
        else{
            return ((PercentageLiteral) literal).value;
        }
    }

    public Literal transformExpression(Expression astNode){
        return (Literal) astNode;
    }

    public void transformOperation(ASTNode astNode){

    }

    public void transformIfClause(ASTNode astNode){

    }

}
