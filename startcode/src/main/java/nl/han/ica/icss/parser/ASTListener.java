package nl.han.ica.icss.parser;

import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private final AST ast;
    final public char hash = '#';
    final public char dot = '.';

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private IHANStack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new HANStack<>();
	}

    public AST getAST() {
        return ast;
    }

    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx){
        ASTNode stylesheet = new Stylesheet();
        currentContainer.push(stylesheet);
    }

    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        ast.setRoot((Stylesheet) currentContainer.pop());
    }

    @Override
    public void enterStyleRule(ICSSParser.StyleRuleContext ctx) {
        ASTNode styleRule = new Stylerule();
        ast.root.body.add(styleRule);
        currentContainer.push(styleRule);
    }

    @Override
    public void exitStyleRule(ICSSParser.StyleRuleContext ctx) {
        ASTNode stylerule = currentContainer.pop();
        currentContainer.peek().addChild(stylerule);
    }

    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        ASTNode variableAssignment = new VariableAssignment();
        currentContainer.push(variableAssignment);
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        ASTNode variableAssignment = currentContainer.pop();
        currentContainer.peek().addChild(variableAssignment);
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        ASTNode variableReference = new VariableReference(ctx.getText());
        currentContainer.peek().addChild(variableReference);
    }

    @Override
    public void exitVariableReference(ICSSParser.VariableReferenceContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterDecleration(ICSSParser.DeclerationContext ctx) {
        ASTNode declaration = new Declaration();
        currentContainer.push(declaration);
    }

    @Override
    public void exitDecleration(ICSSParser.DeclerationContext ctx) {
        ASTNode declaration = currentContainer.pop();
        currentContainer.peek().addChild(declaration);
    }

//    @Override
//    public void enterToDeclare(ICSSParser.ToDeclareContext ctx) {
//        ASTNode toDeclare = new ICSSParser.ToDeclareContext();
//        currentContainer.push(toDeclare);
//    }
//
//    @Override
//    public void exitToDeclare(ICSSParser.ToDeclareContext ctx) {
//        super.exitToDeclare(ctx);
//    }

    @Override
    public void enterSelector(ICSSParser.SelectorContext ctx) {
        //Retrieve first character of text representation corresponding to the node
        char selectorKind = ctx.getChild(0).getText().charAt(0);
        ASTNode selector;
        //Switchcase to find out which selector to use
        switch(selectorKind){
            case dot: selector = new IdSelector(Character.toString(selectorKind));
                break;
            case hash: selector = new ClassSelector(Character.toString(selectorKind));
                break;
            default: selector = new TagSelector(Character.toString(selectorKind));
        }
    }

    @Override
    public void exitSelector(ICSSParser.SelectorContext ctx) {
        currentContainer.pop();
    }

//    @Override
//    public void enterBoolLiteral(ICSSParser.LiteralContext ctx) {
//        super.enterBoolLiteral(ctx);
//    }
//
//    @Override
//    public void enterColorLiteral(ICSSParser.LiteralContext ctx) {
//        super.enterColorLiteral(ctx);
//    }
//
//    @Override
//    public void enterPercentageLiteral(ICSSParser.LiteralContext ctx) {
//        super.enterPercentageLiteral(ctx);
//    }
//
//    @Override
//    public void enterPixelLiteral(ICSSParser.LiteralContext ctx) {
//        super.enterPixelLiteral(ctx);
//    }
//
//
//    @Override
//    public void enterScalarLiteral(ICSSParser.LiteralContext ctx) {
//        super.enterScalarLiteral(ctx);
//    }
}