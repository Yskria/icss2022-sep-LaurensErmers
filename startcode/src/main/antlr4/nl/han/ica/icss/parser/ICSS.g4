grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';

//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';




//--- PARSER: ---
stylesheet: (variableAssignment | styleRule)* | EOF;

//literals
boolLiteral: TRUE | FALSE;
colorLiteral: PIXELSIZE | COLOR;
scalarLiteral: SCALAR;
percentageLiteral: PERCENTAGE;
literal: boolLiteral | colorLiteral | percentageLiteral | scalarLiteral ;

//variables
variableAssignment: variableReference ASSIGNMENT_OPERATOR literal SEMICOLON;
variableReference: CAPITAL_IDENT;

//style rule
styleRule: selector OPEN_BRACE decleration+ CLOSE_BRACE;
decleration: selector COLON (literal | variableReference | expressions)+ SEMICOLON | ifClause;
//toDeclare: selector COLON (literal | variableReference | expressions)+ SEMICOLON;
operator: MUL | PLUS | MIN;
selector: LOWER_IDENT | ID_IDENT | CLASS_IDENT;
ifClause: IF BOX_BRACKET_OPEN (literal | variableReference) BOX_BRACKET_CLOSE OPEN_BRACE (decleration | ifClause)+ CLOSE_BRACE elseClause?;
elseClause: ELSE OPEN_BRACE (decleration | ifClause)+ CLOSE_BRACE;

expressions: expressions operator expressions | (literal | variableReference) | '('expressions')';