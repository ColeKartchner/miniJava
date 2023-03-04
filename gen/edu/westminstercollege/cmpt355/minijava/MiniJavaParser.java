// Generated from java-escape by ANTLR 4.11.1
package edu.westminstercollege.cmpt355.minijava;

            import edu.westminstercollege.cmpt355.minijava.node.*;
            import java.util.Optional;
        
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class MiniJavaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, RESERVED_WORD=19, NAME=20, WHITESPACE=21, INT=22, DOUBLE=23, 
		BOOLEAN=24, STRING=25, LINE_COMMENT=26, BLOCK_COMMENT=27;
	public static final int
		RULE_goal = 0, RULE_methodBody = 1, RULE_statement = 2, RULE_variableDeclaration = 3, 
		RULE_variableDeclarationItem = 4, RULE_expression = 5, RULE_type = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"goal", "methodBody", "statement", "variableDeclaration", "variableDeclarationItem", 
			"expression", "type"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'{'", "'}'", "','", "'='", "'print'", "'('", "')'", "'++'", 
			"'--'", "'-'", "'+'", "'*'", "'/'", "'int'", "'double'", "'boolean'", 
			"'String'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "RESERVED_WORD", "NAME", "WHITESPACE", 
			"INT", "DOUBLE", "BOOLEAN", "STRING", "LINE_COMMENT", "BLOCK_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "java-escape"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MiniJavaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GoalContext extends ParserRuleContext {
		public Block n;
		public MethodBodyContext mbod;
		public TerminalNode EOF() { return getToken(MiniJavaParser.EOF, 0); }
		public MethodBodyContext methodBody() {
			return getRuleContext(MethodBodyContext.class,0);
		}
		public GoalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_goal; }
	}

	public final GoalContext goal() throws RecognitionException {
		GoalContext _localctx = new GoalContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_goal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			((GoalContext)_localctx).mbod = methodBody();
			setState(15);
			match(EOF);

			            ((GoalContext)_localctx).n =  ((GoalContext)_localctx).mbod.n;
			       
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MethodBodyContext extends ParserRuleContext {
		public Block n;
		public StatementContext statement;
		public List<StatementContext> mbod = new ArrayList<StatementContext>();
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public MethodBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodBody; }
	}

	public final MethodBodyContext methodBody() throws RecognitionException {
		MethodBodyContext _localctx = new MethodBodyContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_methodBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((_la) & ~0x3f) == 0 && ((1L << _la) & 47685318L) != 0) {
				{
				{
				setState(18);
				((MethodBodyContext)_localctx).statement = statement();
				((MethodBodyContext)_localctx).mbod.add(((MethodBodyContext)_localctx).statement);
				}
				}
				setState(23);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}

			           var statements = new ArrayList<Statement>();
			           for (var mbods : ((MethodBodyContext)_localctx).mbod)
			                       statements.add(mbods.n);
			           ((MethodBodyContext)_localctx).n =  new Block(statements);
			       
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public Statement n;
		public StatementContext statement;
		public List<StatementContext> mbod = new ArrayList<StatementContext>();
		public VariableDeclarationContext variableDeclaration;
		public ExpressionContext expression;
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_statement);
		int _la;
		try {
			setState(44);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(26);
				match(T__0);

				            ((StatementContext)_localctx).n =  new EmptyStatement();
				       
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(28);
				match(T__1);
				setState(32);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((_la) & ~0x3f) == 0 && ((1L << _la) & 47685318L) != 0) {
					{
					{
					setState(29);
					((StatementContext)_localctx).statement = statement();
					((StatementContext)_localctx).mbod.add(((StatementContext)_localctx).statement);
					}
					}
					setState(34);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(35);
				match(T__2);

				            var statements = new ArrayList<Statement>();
				            for (var mbods : ((StatementContext)_localctx).mbod)
				                statements.add(mbods.n);
				            ((StatementContext)_localctx).n =  new Block(statements);
				       
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(37);
				((StatementContext)_localctx).variableDeclaration = variableDeclaration();

				           ((StatementContext)_localctx).n =  ((StatementContext)_localctx).variableDeclaration.n; //?
				       
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(40);
				((StatementContext)_localctx).expression = expression(0);
				setState(41);
				match(T__0);

				           ((StatementContext)_localctx).n =  new ExpressionStatement(((StatementContext)_localctx).expression.n);
				       
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclarationContext extends ParserRuleContext {
		public VarDeclarations n;
		public TypeContext type;
		public VariableDeclarationItemContext variableDeclarationItem;
		public List<VariableDeclarationItemContext> dec = new ArrayList<VariableDeclarationItemContext>();
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<VariableDeclarationItemContext> variableDeclarationItem() {
			return getRuleContexts(VariableDeclarationItemContext.class);
		}
		public VariableDeclarationItemContext variableDeclarationItem(int i) {
			return getRuleContext(VariableDeclarationItemContext.class,i);
		}
		public VariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaration; }
	}

	public final VariableDeclarationContext variableDeclaration() throws RecognitionException {
		VariableDeclarationContext _localctx = new VariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_variableDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			((VariableDeclarationContext)_localctx).type = type();
			setState(47);
			((VariableDeclarationContext)_localctx).variableDeclarationItem = variableDeclarationItem();
			((VariableDeclarationContext)_localctx).dec.add(((VariableDeclarationContext)_localctx).variableDeclarationItem);
			setState(52);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(48);
				match(T__3);
				setState(49);
				((VariableDeclarationContext)_localctx).variableDeclarationItem = variableDeclarationItem();
				((VariableDeclarationContext)_localctx).dec.add(((VariableDeclarationContext)_localctx).variableDeclarationItem);
				}
				}
				setState(54);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(55);
			match(T__0);

			           var declarations = new ArrayList<VarDeclaration>();
			                   for (var dec : ((VariableDeclarationContext)_localctx).dec)
			                       declarations.add(dec.n);
			                   TypeNode x = new TypeNode(((VariableDeclarationContext)_localctx).type.n);
			                   ((VariableDeclarationContext)_localctx).n =  new VarDeclarations(x, declarations);
			       
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclarationItemContext extends ParserRuleContext {
		public VarDeclaration n;
		public Token NAME;
		public ExpressionContext expression;
		public TerminalNode NAME() { return getToken(MiniJavaParser.NAME, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableDeclarationItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarationItem; }
	}

	public final VariableDeclarationItemContext variableDeclarationItem() throws RecognitionException {
		VariableDeclarationItemContext _localctx = new VariableDeclarationItemContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_variableDeclarationItem);
		try {
			setState(65);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(58);
				((VariableDeclarationItemContext)_localctx).NAME = match(NAME);

				           Optional<Expression> staticOptional = Optional.empty();
				           ((VariableDeclarationItemContext)_localctx).n =  new VarDeclaration(staticOptional, (((VariableDeclarationItemContext)_localctx).NAME!=null?((VariableDeclarationItemContext)_localctx).NAME.getText():null));
				       
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(60);
				((VariableDeclarationItemContext)_localctx).NAME = match(NAME);
				setState(61);
				match(T__4);
				setState(62);
				((VariableDeclarationItemContext)_localctx).expression = expression(0);

				            Optional<Expression> staticOptional = Optional.of(((VariableDeclarationItemContext)_localctx).expression.n);
				           ((VariableDeclarationItemContext)_localctx).n =  new VarDeclaration(staticOptional, (((VariableDeclarationItemContext)_localctx).NAME!=null?((VariableDeclarationItemContext)_localctx).NAME.getText():null));
				       
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public Expression n;
		public ExpressionContext expression;
		public Token INT;
		public Token DOUBLE;
		public Token STRING;
		public Token NAME;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode INT() { return getToken(MiniJavaParser.INT, 0); }
		public TerminalNode DOUBLE() { return getToken(MiniJavaParser.DOUBLE, 0); }
		public TerminalNode STRING() { return getToken(MiniJavaParser.STRING, 0); }
		public TerminalNode NAME() { return getToken(MiniJavaParser.NAME, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(68);
				match(T__5);
				setState(69);
				match(T__6);
				setState(78);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 47193792L) != 0) {
					{
					setState(70);
					((ExpressionContext)_localctx).expression = expression(0);
					setState(75);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__3) {
						{
						{
						setState(71);
						match(T__3);
						setState(72);
						((ExpressionContext)_localctx).expression = expression(0);
						}
						}
						setState(77);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(80);
				match(T__7);

				            ((ExpressionContext)_localctx).n =  new Print(((ExpressionContext)_localctx).expression.n);
				       
				}
				break;
			case 2:
				{
				setState(82);
				((ExpressionContext)_localctx).INT = match(INT);

				           ((ExpressionContext)_localctx).n =  new IntLiteral((((ExpressionContext)_localctx).INT!=null?((ExpressionContext)_localctx).INT.getText():null));
				       
				}
				break;
			case 3:
				{
				setState(84);
				((ExpressionContext)_localctx).DOUBLE = match(DOUBLE);

				           ((ExpressionContext)_localctx).n =  new DoubleLiteral((((ExpressionContext)_localctx).DOUBLE!=null?((ExpressionContext)_localctx).DOUBLE.getText():null));
				       
				}
				break;
			case 4:
				{
				setState(86);
				((ExpressionContext)_localctx).STRING = match(STRING);

				           ((ExpressionContext)_localctx).n =  new StringLiteral((((ExpressionContext)_localctx).STRING!=null?((ExpressionContext)_localctx).STRING.getText():null));
				       
				}
				break;
			case 5:
				{
				setState(88);
				((ExpressionContext)_localctx).NAME = match(NAME);

				           ((ExpressionContext)_localctx).n =  new VariableAccess((((ExpressionContext)_localctx).NAME!=null?((ExpressionContext)_localctx).NAME.getText():null));
				       
				}
				break;
			case 6:
				{
				setState(90);
				match(T__6);
				setState(91);
				((ExpressionContext)_localctx).expression = expression(0);
				setState(92);
				match(T__7);

				           ((ExpressionContext)_localctx).n =  ((ExpressionContext)_localctx).expression.n;
				       
				}
				break;
			case 7:
				{
				setState(95);
				_la = _input.LA(1);
				if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 7680L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(96);
				((ExpressionContext)_localctx).expression = expression(5);

				           ((ExpressionContext)_localctx).n =  ((ExpressionContext)_localctx).expression.n;
				       
				}
				break;
			case 8:
				{
				setState(99);
				match(T__6);
				setState(100);
				type();
				setState(101);
				match(T__7);
				setState(102);
				((ExpressionContext)_localctx).expression = expression(4);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(127);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(125);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(106);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(107);
						_la = _input.LA(1);
						if ( !(_la==T__12 || _la==T__13) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(108);
						((ExpressionContext)_localctx).expression = expression(4);

						                     ((ExpressionContext)_localctx).n =  ((ExpressionContext)_localctx).expression.n;
						                 
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(111);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(112);
						_la = _input.LA(1);
						if ( !(_la==T__10 || _la==T__11) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(113);
						((ExpressionContext)_localctx).expression = expression(3);

						                     ((ExpressionContext)_localctx).n =  ((ExpressionContext)_localctx).expression.n;
						                 
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(116);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(117);
						match(T__4);
						setState(118);
						((ExpressionContext)_localctx).expression = expression(1);

						                     ((ExpressionContext)_localctx).n =  ((ExpressionContext)_localctx).expression.n;
						                 
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(121);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(122);
						_la = _input.LA(1);
						if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 7680L) != 0) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(123);
						match(T__0);

						                    ((ExpressionContext)_localctx).n =  ((ExpressionContext)_localctx).expression.n;
						                 
						}
						break;
					}
					} 
				}
				setState(129);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public String n;
		public Token NAME;
		public TerminalNode NAME() { return getToken(MiniJavaParser.NAME, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_type);
		try {
			setState(140);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__14:
				enterOuterAlt(_localctx, 1);
				{
				setState(130);
				match(T__14);

				           ((TypeContext)_localctx).n =  "int";
				       
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 2);
				{
				setState(132);
				match(T__15);

				           ((TypeContext)_localctx).n =  "double";
				       
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 3);
				{
				setState(134);
				match(T__16);

				           ((TypeContext)_localctx).n =  "boolean";
				       
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 4);
				{
				setState(136);
				match(T__17);

				           ((TypeContext)_localctx).n =  "String";
				       
				}
				break;
			case NAME:
				enterOuterAlt(_localctx, 5);
				{
				setState(138);
				((TypeContext)_localctx).NAME = match(NAME);

				           ((TypeContext)_localctx).n =  new String((((TypeContext)_localctx).NAME!=null?((TypeContext)_localctx).NAME.getText():null));
				       
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 5:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 3);
		case 1:
			return precpred(_ctx, 2);
		case 2:
			return precpred(_ctx, 1);
		case 3:
			return precpred(_ctx, 6);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u001b\u008f\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0005\u0001\u0014\b\u0001\n\u0001"+
		"\f\u0001\u0017\t\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0005\u0002\u001f\b\u0002\n\u0002\f\u0002\"\t"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002-\b\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u00033\b\u0003\n\u0003"+
		"\f\u00036\t\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003"+
		"\u0004B\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0005\u0005J\b\u0005\n\u0005\f\u0005M\t\u0005\u0003"+
		"\u0005O\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0003\u0005i\b\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005~\b"+
		"\u0005\n\u0005\f\u0005\u0081\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0003\u0006\u008d\b\u0006\u0001\u0006\u0000\u0001\n\u0007"+
		"\u0000\u0002\u0004\u0006\b\n\f\u0000\u0003\u0001\u0000\t\f\u0001\u0000"+
		"\r\u000e\u0001\u0000\u000b\f\u009f\u0000\u000e\u0001\u0000\u0000\u0000"+
		"\u0002\u0015\u0001\u0000\u0000\u0000\u0004,\u0001\u0000\u0000\u0000\u0006"+
		".\u0001\u0000\u0000\u0000\bA\u0001\u0000\u0000\u0000\nh\u0001\u0000\u0000"+
		"\u0000\f\u008c\u0001\u0000\u0000\u0000\u000e\u000f\u0003\u0002\u0001\u0000"+
		"\u000f\u0010\u0005\u0000\u0000\u0001\u0010\u0011\u0006\u0000\uffff\uffff"+
		"\u0000\u0011\u0001\u0001\u0000\u0000\u0000\u0012\u0014\u0003\u0004\u0002"+
		"\u0000\u0013\u0012\u0001\u0000\u0000\u0000\u0014\u0017\u0001\u0000\u0000"+
		"\u0000\u0015\u0013\u0001\u0000\u0000\u0000\u0015\u0016\u0001\u0000\u0000"+
		"\u0000\u0016\u0018\u0001\u0000\u0000\u0000\u0017\u0015\u0001\u0000\u0000"+
		"\u0000\u0018\u0019\u0006\u0001\uffff\uffff\u0000\u0019\u0003\u0001\u0000"+
		"\u0000\u0000\u001a\u001b\u0005\u0001\u0000\u0000\u001b-\u0006\u0002\uffff"+
		"\uffff\u0000\u001c \u0005\u0002\u0000\u0000\u001d\u001f\u0003\u0004\u0002"+
		"\u0000\u001e\u001d\u0001\u0000\u0000\u0000\u001f\"\u0001\u0000\u0000\u0000"+
		" \u001e\u0001\u0000\u0000\u0000 !\u0001\u0000\u0000\u0000!#\u0001\u0000"+
		"\u0000\u0000\" \u0001\u0000\u0000\u0000#$\u0005\u0003\u0000\u0000$-\u0006"+
		"\u0002\uffff\uffff\u0000%&\u0003\u0006\u0003\u0000&\'\u0006\u0002\uffff"+
		"\uffff\u0000\'-\u0001\u0000\u0000\u0000()\u0003\n\u0005\u0000)*\u0005"+
		"\u0001\u0000\u0000*+\u0006\u0002\uffff\uffff\u0000+-\u0001\u0000\u0000"+
		"\u0000,\u001a\u0001\u0000\u0000\u0000,\u001c\u0001\u0000\u0000\u0000,"+
		"%\u0001\u0000\u0000\u0000,(\u0001\u0000\u0000\u0000-\u0005\u0001\u0000"+
		"\u0000\u0000./\u0003\f\u0006\u0000/4\u0003\b\u0004\u000001\u0005\u0004"+
		"\u0000\u000013\u0003\b\u0004\u000020\u0001\u0000\u0000\u000036\u0001\u0000"+
		"\u0000\u000042\u0001\u0000\u0000\u000045\u0001\u0000\u0000\u000057\u0001"+
		"\u0000\u0000\u000064\u0001\u0000\u0000\u000078\u0005\u0001\u0000\u0000"+
		"89\u0006\u0003\uffff\uffff\u00009\u0007\u0001\u0000\u0000\u0000:;\u0005"+
		"\u0014\u0000\u0000;B\u0006\u0004\uffff\uffff\u0000<=\u0005\u0014\u0000"+
		"\u0000=>\u0005\u0005\u0000\u0000>?\u0003\n\u0005\u0000?@\u0006\u0004\uffff"+
		"\uffff\u0000@B\u0001\u0000\u0000\u0000A:\u0001\u0000\u0000\u0000A<\u0001"+
		"\u0000\u0000\u0000B\t\u0001\u0000\u0000\u0000CD\u0006\u0005\uffff\uffff"+
		"\u0000DE\u0005\u0006\u0000\u0000EN\u0005\u0007\u0000\u0000FK\u0003\n\u0005"+
		"\u0000GH\u0005\u0004\u0000\u0000HJ\u0003\n\u0005\u0000IG\u0001\u0000\u0000"+
		"\u0000JM\u0001\u0000\u0000\u0000KI\u0001\u0000\u0000\u0000KL\u0001\u0000"+
		"\u0000\u0000LO\u0001\u0000\u0000\u0000MK\u0001\u0000\u0000\u0000NF\u0001"+
		"\u0000\u0000\u0000NO\u0001\u0000\u0000\u0000OP\u0001\u0000\u0000\u0000"+
		"PQ\u0005\b\u0000\u0000Qi\u0006\u0005\uffff\uffff\u0000RS\u0005\u0016\u0000"+
		"\u0000Si\u0006\u0005\uffff\uffff\u0000TU\u0005\u0017\u0000\u0000Ui\u0006"+
		"\u0005\uffff\uffff\u0000VW\u0005\u0019\u0000\u0000Wi\u0006\u0005\uffff"+
		"\uffff\u0000XY\u0005\u0014\u0000\u0000Yi\u0006\u0005\uffff\uffff\u0000"+
		"Z[\u0005\u0007\u0000\u0000[\\\u0003\n\u0005\u0000\\]\u0005\b\u0000\u0000"+
		"]^\u0006\u0005\uffff\uffff\u0000^i\u0001\u0000\u0000\u0000_`\u0007\u0000"+
		"\u0000\u0000`a\u0003\n\u0005\u0005ab\u0006\u0005\uffff\uffff\u0000bi\u0001"+
		"\u0000\u0000\u0000cd\u0005\u0007\u0000\u0000de\u0003\f\u0006\u0000ef\u0005"+
		"\b\u0000\u0000fg\u0003\n\u0005\u0004gi\u0001\u0000\u0000\u0000hC\u0001"+
		"\u0000\u0000\u0000hR\u0001\u0000\u0000\u0000hT\u0001\u0000\u0000\u0000"+
		"hV\u0001\u0000\u0000\u0000hX\u0001\u0000\u0000\u0000hZ\u0001\u0000\u0000"+
		"\u0000h_\u0001\u0000\u0000\u0000hc\u0001\u0000\u0000\u0000i\u007f\u0001"+
		"\u0000\u0000\u0000jk\n\u0003\u0000\u0000kl\u0007\u0001\u0000\u0000lm\u0003"+
		"\n\u0005\u0004mn\u0006\u0005\uffff\uffff\u0000n~\u0001\u0000\u0000\u0000"+
		"op\n\u0002\u0000\u0000pq\u0007\u0002\u0000\u0000qr\u0003\n\u0005\u0003"+
		"rs\u0006\u0005\uffff\uffff\u0000s~\u0001\u0000\u0000\u0000tu\n\u0001\u0000"+
		"\u0000uv\u0005\u0005\u0000\u0000vw\u0003\n\u0005\u0001wx\u0006\u0005\uffff"+
		"\uffff\u0000x~\u0001\u0000\u0000\u0000yz\n\u0006\u0000\u0000z{\u0007\u0000"+
		"\u0000\u0000{|\u0005\u0001\u0000\u0000|~\u0006\u0005\uffff\uffff\u0000"+
		"}j\u0001\u0000\u0000\u0000}o\u0001\u0000\u0000\u0000}t\u0001\u0000\u0000"+
		"\u0000}y\u0001\u0000\u0000\u0000~\u0081\u0001\u0000\u0000\u0000\u007f"+
		"}\u0001\u0000\u0000\u0000\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u000b"+
		"\u0001\u0000\u0000\u0000\u0081\u007f\u0001\u0000\u0000\u0000\u0082\u0083"+
		"\u0005\u000f\u0000\u0000\u0083\u008d\u0006\u0006\uffff\uffff\u0000\u0084"+
		"\u0085\u0005\u0010\u0000\u0000\u0085\u008d\u0006\u0006\uffff\uffff\u0000"+
		"\u0086\u0087\u0005\u0011\u0000\u0000\u0087\u008d\u0006\u0006\uffff\uffff"+
		"\u0000\u0088\u0089\u0005\u0012\u0000\u0000\u0089\u008d\u0006\u0006\uffff"+
		"\uffff\u0000\u008a\u008b\u0005\u0014\u0000\u0000\u008b\u008d\u0006\u0006"+
		"\uffff\uffff\u0000\u008c\u0082\u0001\u0000\u0000\u0000\u008c\u0084\u0001"+
		"\u0000\u0000\u0000\u008c\u0086\u0001\u0000\u0000\u0000\u008c\u0088\u0001"+
		"\u0000\u0000\u0000\u008c\u008a\u0001\u0000\u0000\u0000\u008d\r\u0001\u0000"+
		"\u0000\u0000\u000b\u0015 ,4AKNh}\u007f\u008c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}