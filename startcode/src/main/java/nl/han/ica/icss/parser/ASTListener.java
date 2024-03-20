package nl.han.ica.icss.parser;

import java.lang.runtime.SwitchBootstraps;
import java.util.Stack;


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
	private AST ast;
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
        ast.setRoot((Stylesheet) currentContainer.pop());
    }

    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterStyleRule(ICSSParser.StyleRuleContext ctx) {
        Stylerule styleRule = new Stylerule();
        ast.root.body.add(styleRule);
        currentContainer.push(styleRule);
    }

    @Override
    public void exitStyleRule(ICSSParser.StyleRuleContext ctx) {
        ASTNode stylerule = currentContainer.pop();
        currentContainer.peek().addChild(stylerule);
    }

    @Override
    public void enterLiteral(ICSSParser.LiteralContext ctx) {

    }

    @Override
    public void exitLiteral(ICSSParser.LiteralContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        super.enterVariableAssignment(ctx);
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        super.enterVariableReference(ctx);
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

    @Override
    public void enterSelector(ICSSParser.SelectorContext ctx) {
        //Retrieve first character of text representation corresponding to the node
        char selectorKind = ctx.getChild(0).getText().charAt(0);
        Selector selector;
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
}