package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.HashMap;

public class Evaluator implements Transform {

    private IHANLinkedList<HashMap<String, Literal>> variableValues;

    public Evaluator() {
        // this.variableValues = new HANLinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        variableValues = new HANLinkedList<>();
        this.transformStylesheet(ast.root);
    }

    public void transformStylesheet(ASTNode astNode) {
        ArrayList<ASTNode> toRemove = new ArrayList<>();

        variableValues.addFirst(new HashMap<>());

        for (ASTNode node : astNode.getChildren()) {
            if (node instanceof VariableAssignment) {
                transformVariableAssignment(node);
                toRemove.add(node);
            }

            if (node instanceof Stylerule) {
                transformStylerule(node);
            }
        }
        variableValues.removeFirst();
        toRemove.forEach(astNode::removeChild);
    }

    public void transformStylerule(ASTNode astNode) {
        Stylerule stylerule = (Stylerule) astNode;

        ArrayList<ASTNode> toAdd = new ArrayList<>();

        variableValues.addFirst(new HashMap<>());

        for (ASTNode node : stylerule.body) {
            if (node instanceof Declaration) {
                transformDeclaration(node);
                toAdd.add(node);
            }
            if (node instanceof VariableAssignment) {
                transformVariableAssignment(node);
            }
            if (node instanceof IfClause) {
                toAdd.addAll(transformIfClause(node));
            }
        }
        stylerule.body = toAdd;

        variableValues.removeFirst();
    }

    public ArrayList<ASTNode> transformIfClause(ASTNode astNode) {
        ArrayList<ASTNode> ifClauseArray = new ArrayList<>();
        variableValues.addFirst(new HashMap<>());
        IfClause ifClause = (IfClause) astNode;

        //Checks if the ifClause Condition is TRUE or FALSE
        if (ifClause.conditionalExpression instanceof VariableReference) {
            ifClause.conditionalExpression = getVariableReferenceLiteral(((VariableReference) ifClause.conditionalExpression).name);
        }

        if (ifClause.conditionalExpression instanceof BoolLiteral) {
            //If ifClause = true, do:
            if (((BoolLiteral) ifClause.conditionalExpression).value) {
                for (ASTNode ifClauseContent : ifClause.body) {
                    if (ifClauseContent instanceof IfClause) {
                        ifClauseArray.addAll(transformIfClause(ifClauseContent));
                    }
                    if (ifClauseContent instanceof Declaration) {
                        if (((Declaration) ifClauseContent).expression instanceof VariableReference) {
                            ((Declaration) ifClauseContent).expression = getVariableReferenceLiteral(((VariableReference) ((Declaration) ifClauseContent).expression).name);
                        }
                        ifClauseArray.add(ifClauseContent);
                    }
                }
                //replace if-body ifClause = false
            } else if (ifClause.elseClause != null){
                ifClauseArray.addAll(ifClause.elseClause.body);
            }
        }
        System.out.println(ifClauseArray);
        variableValues.removeFirst();
        return ifClauseArray;
    }

    //Transforms the declaration to it's actual value
    public void transformDeclaration(ASTNode astNode) {
        Declaration declaration = (Declaration) astNode;
        if (declaration.expression instanceof VariableReference) {
            declaration.expression = getVariableReferenceLiteral(((VariableReference) declaration.expression).name);
        }
        if (declaration.expression instanceof Operation) {
            declaration.expression = transformOperation((Operation) declaration.expression);
        }
    }

    //Checks if the assignment is Literal or Variable Reference
    public void transformVariableAssignment(ASTNode astNode) {
        VariableAssignment variableAssignment = (VariableAssignment) astNode;
        Literal literal;
        if (variableAssignment.expression instanceof Literal) {
            literal = (Literal) variableAssignment.expression;
            variableValues.getFirst().put(((variableAssignment).name.name), literal);
        }
        if (variableAssignment.expression instanceof VariableReference) {
            literal = getVariableReferenceLiteral(((VariableReference) variableAssignment.expression).name);
            variableValues.getFirst().put(((variableAssignment).name.name), literal);
        }
    }

    //Checks the ArrayList, compares VariableName to the list through containsKey and returns it's actual value
    public Literal getVariableReferenceLiteral(String variableName) {
        Literal literal;
        for (int i = 0; i < variableValues.getSize(); i++) {
            if (variableValues.get(i).containsKey(variableName)) {
                literal = variableValues.get(i).get(variableName);
                return literal;
            }
        }
        return null;
    }

    //Calculates the operation based on it's operator.
    public Literal transformOperation(Operation operation) {
        Literal literal = null;
        Literal left;
        Literal right;
        int leftValue;
        int rightValue;

        if (operation.lhs instanceof Operation) {
            left = transformOperation((Operation) operation.lhs);
        } else if (operation.lhs instanceof VariableReference) {
            left = getVariableReferenceLiteral(((VariableReference) operation.lhs).name);
        } else {
            left = (Literal) operation.lhs;
        }
        if (operation.rhs instanceof Operation) {
            right = transformOperation((Operation) operation.rhs);
        } else if (operation.rhs instanceof VariableReference) {
            right = getVariableReferenceLiteral(((VariableReference) operation.rhs).name);
        } else {
            right = (Literal) operation.rhs;
        }

        if (left instanceof PercentageLiteral) {
            leftValue = ((PercentageLiteral) left).value;
            literal = new PercentageLiteral(leftValue);
        }
        if (left instanceof PixelLiteral) {
            leftValue = ((PixelLiteral) left).value;
            literal = new PixelLiteral(leftValue);
        } else {
            leftValue = ((ScalarLiteral) left).value;
            literal = new ScalarLiteral(leftValue);
        }

        if (right instanceof PercentageLiteral) {
            rightValue = ((PercentageLiteral) right).value;
            literal = new PercentageLiteral(rightValue);
        }
        if (right instanceof PixelLiteral) {
            rightValue = ((PixelLiteral) right).value;
            literal = new PixelLiteral(rightValue);
        } else {
            rightValue = ((ScalarLiteral) right).value;
            literal = new ScalarLiteral(rightValue);
        }

        if (operation instanceof AddOperation) {
            if (left instanceof PixelLiteral || right instanceof PixelLiteral) {
                literal = new PixelLiteral(leftValue + rightValue);
            } else {
                literal = new PercentageLiteral(leftValue + rightValue);
            }
        }
        if (operation instanceof SubtractOperation) {
            if (left instanceof PixelLiteral || right instanceof PixelLiteral) {
                literal = new PixelLiteral(leftValue - rightValue);
            } else {
                literal = new PercentageLiteral(leftValue - rightValue);
            }
        }
        if (operation instanceof MultiplyOperation) {
            if (left instanceof PixelLiteral || right instanceof PixelLiteral) {
                literal = new PixelLiteral(leftValue * rightValue);
            } else if (left instanceof ScalarLiteral && right instanceof ScalarLiteral) {
                literal = new ScalarLiteral(leftValue * rightValue);
            } else {
                literal = new PercentageLiteral(leftValue * rightValue);
            }
        }
        return literal;
    }
}
