package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;

public class Generator {

    //General class for appending results of functions together
	public String generate(AST ast) {
        StringBuilder stringBuilder = new StringBuilder();
        Stylesheet stylesheet = ast.root;
        for(ASTNode node : stylesheet.getChildren()){
            stringBuilder.append(buildStringSelector((Stylerule) node));
            stringBuilder.append(buildStringDeclaration(node));
            stringBuilder.append("}\n");
        }
        return stringBuilder.toString();
	}

    //creates the string for selectors
    public String buildStringSelector(Stylerule stylerule){
        StringBuilder stringBuilder = new StringBuilder();
        for(Selector selector : stylerule.selectors){
            stringBuilder.append(selector.toString());
        }
        stringBuilder.append("{\n");
        return stringBuilder.toString();
    }

    //creates the string for all declarations in the scope(s)
    public String buildStringDeclaration(ASTNode astNode){
        StringBuilder stringBuilder = new StringBuilder();
        for(ASTNode node : astNode.getChildren()){
            if(node instanceof Declaration){
                stringBuilder.append("  ")
                        .append(((Declaration) node).property.name)
                        .append(": ")
                        .append(buildExpressionValue((((Declaration) node).expression)))
                        .append(";")
                        .append("\n");
            }
        }
        return stringBuilder.toString();
    }

    //returns String of all the expression of all literals, based on which literal it is
    public String buildExpressionValue(Expression expression){
        if(expression instanceof ColorLiteral){
            return ((ColorLiteral) expression).value;
        }if(expression instanceof PixelLiteral){
            return ((PixelLiteral) expression).value + "px";
        }if(expression instanceof PercentageLiteral){
            return ((PercentageLiteral) expression).value + "%";
        }
        return "";
    }
}