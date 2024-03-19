package nl.han.ica.icss.parser;

import java.util.Stack;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;

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
        Stylesheet stylesheet = new Stylesheet();
        currentContainer.push(stylesheet);
        ast.setRoot(stylesheet);
    }

    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterStyleRule(ICSSParser.StyleRuleContext ctx) {
        super.enterStyleRule(ctx);
    }

    @Override
    public void exitStyleRule(ICSSParser.StyleRuleContext ctx) {
        super.exitStyleRule(ctx);
    }

    @Override
    public void enterLiteral(ICSSParser.LiteralContext ctx) {
        super.enterLiteral(ctx);
    }

    @Override
    public void exitLiteral(ICSSParser.LiteralContext ctx) {
        super.exitLiteral(ctx);
    }

    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        super.enterVariableAssignment(ctx);
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        super.exitVariableAssignment(ctx);
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        super.enterVariableReference(ctx);
    }

    @Override
    public void exitVariableReference(ICSSParser.VariableReferenceContext ctx) {
        super.exitVariableReference(ctx);
    }

    @Override
    public void enterDecleration(ICSSParser.DeclerationContext ctx) {
        super.enterDecleration(ctx);
    }

    @Override
    public void exitDecleration(ICSSParser.DeclerationContext ctx) {
        super.exitDecleration(ctx);
    }

    @Override
    public void enterSelector(ICSSParser.SelectorContext ctx) {
        super.enterSelector(ctx);
    }

    @Override
    public void exitSelector(ICSSParser.SelectorContext ctx) {
        super.exitSelector(ctx);
    }
}