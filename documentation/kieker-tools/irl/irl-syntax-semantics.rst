.. _kieker-tools-irl-syntax-semantics:

IRL - Language Syntax and Semantics
===================================

::

	Model:
		'package' name = QualifiedName
		(imports += Import)*
		(types += ComplexType |
		modelTypes += ModelType )*
	;

	Import:
		'import' importedNamespace = QualifiedNameWithWildcard
	;


The following rules are used to create metamodel classes for the base type ::

	Type:
		ComplexType | BaseType
	;

	BaseType:
		name=ID
	;

Complex types ::

	ComplexType:
		EventType | TemplateType | ModelSubType | EnumerationType
	;


::

	ModelType:
		('@author' author=STRING)?
		('@since' since=STRING)?
		'model' name=ID types+=[ComplexType|QualifiedName] (',' types+=[ComplexType|QualifiedName])* ;
		
	ModelSubType:
		('@author' author=STRING)?
		('@since' since=STRING)?
		'sub' name=ID modelType=[ModelType|QualifiedName] (
			('{' (properties+=Property | constants+=Constant)* '}') | 
			(':' extensions+=[TemplateType|QualifiedName] (',' extensions+=[TemplateType|QualifiedName])*)
		)
	;


::

	EnumerationType:
		('@author' author=STRING)?
		('@since' since=STRING)? 
		'enum' name=ID (':' inherits+=[EnumerationType|QualifiedName] (',' inherits+=[EnumerationType|QualifiedName])*)? 
			'{' literals+=EnumerationLiteral (',' literals+=EnumerationLiteral)* '}'
	;

	EnumerationLiteral:
		name=ID ('=' value=IntLiteral)?
	;


::

	TemplateType:
		('@author' author=STRING)?
		('@since' since=STRING)?
		'template' name=ID (':' inherits+=[TemplateType|QualifiedName] (',' inherits+=[TemplateType|QualifiedName])*)? 
		(
			('{' (properties+=Property | constants+=Constant)* '}') |
			(properties+=Property)
		)?
	;

	EventType:
		('@author' author=STRING)?
		('@since' since=STRING)?
		(abstract?='abstract')? ('event'|'entity') name=ID 
		('extends' parent=[EventType|QualifiedName])?
		(':' inherits+=[TemplateType|QualifiedName] (',' inherits+=[TemplateType|QualifiedName])*)?  
		('{'
			(properties+=Property | constants+=Constant)*
		'}')?
	;


::

	Constant:
		'const' type=Classifier name=ID '=' value=Literal
	;

	Property:
		(modifiers+=PropertyModifier)* (
			foreignKey=ForeignKey | 
			type=Classifier | 
			'alias' referTo=[Property|ID] 'as'
		) name=ID (':' semantics=[semantics::Annotation|ID])?
		('=' value=Literal)?
	;

	ForeignKey:
		'grouped' 'by' eventType=[EventType|ID] '.' propertyRef=[Property|ID]
	;

	enum PropertyModifier:
		TRANSIENT = 'transient' |
		INCREMENT = 'auto-increment' |
		CHANGEABLE = 'changeable'
	;

	Classifier:
		type=[Type|ID] (sizes+=ArraySize)*
	;

	ArraySize: {ArraySize}
		'[' (size=INT)? ']' 
	;


::

	Literal:
		StringLiteral | IntLiteral | FloatLiteral | BooleanLiteral | ConstantLiteral | ArrayLiteral | BuiltInValueLiteral
	;

	ArrayLiteral:
		'{' literals+=Literal (',' literals+=Literal)* '}'
	;

	StringLiteral:
		value=STRING
	;

	IntLiteral:
		value=INT
	;

	FloatLiteral:
		value=FLOAT
	;

	BooleanLiteral: 
		value=BOOLEAN
	;

	ConstantLiteral:
		value=[Constant|ID]
	;

	BuiltInValueLiteral: {BuiltInValueLiteral}
		value='KIEKER_VERSION'
	;

::

	QualifiedName:
	  ID (=>'.' ID)*;

	QualifiedNameWithWildcard:
		QualifiedName ('.' '*')?
	;

	// terminals
	terminal fragment NUMBER :
	    '0'..'9';
	       
	// redefine INT terminal to allow negative numbers
	@Override 
	terminal INT returns ecore::EInt:
	    '-'? NUMBER+;

	// make sure the Float rule does not shadow the INT rule
	terminal FLOAT returns ecore::EFloatObject :
	    '-'? NUMBER+ ('.' NUMBER*) (("e"|"E") ("+"|"-")? NUMBER+)? 'f'? |
	    '-'? NUMBER+ 'f';
	    
	// introduce boolean values
	terminal BOOLEAN returns ecore::EBooleanObject :
	    'true' | 'false';

