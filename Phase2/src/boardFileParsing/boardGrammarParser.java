// Generated from boardGrammar.g4 by ANTLR 4.4

package boardFileParsing;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class boardGrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__22=1, T__21=2, T__20=3, T__19=4, T__18=5, T__17=6, T__16=7, T__15=8, 
		T__14=9, T__13=10, T__12=11, T__11=12, T__10=13, T__9=14, T__8=15, T__7=16, 
		T__6=17, T__5=18, T__4=19, T__3=20, T__2=21, T__1=22, T__0=23, COMMENT=24, 
		WHITESPACE=25, NEWLINE=26, BALL=27, NAME=28, NAMEX=29, INTEGER=30, FLOAT=31, 
		EQUALS=32;
	public static final String[] tokenNames = {
		"<INVALID>", "'board'", "'orientation'", "'otherBoard'", "'xVelocity'", 
		"'height'", "'portal'", "'absorber'", "'rightFlipper'", "'x'", "'y'", 
		"'friction2'", "'trigger'", "'friction1'", "'width'", "'fire'", "'circleBumper'", 
		"'squareBumper'", "'yVelocity'", "'triangleBumper'", "'action'", "'leftFlipper'", 
		"'gravity'", "'otherPortal'", "COMMENT", "WHITESPACE", "NEWLINE", "'ball'", 
		"'name'", "NAMEX", "INTEGER", "FLOAT", "'='"
	};
	public static final int
		RULE_root = 0, RULE_numvalue = 1, RULE_xPos = 2, RULE_yPos = 3, RULE_position = 4, 
		RULE_type = 5, RULE_name = 6, RULE_width = 7, RULE_height = 8, RULE_size = 9, 
		RULE_speed = 10, RULE_orientation = 11, RULE_otherboard = 12, RULE_otherportal = 13, 
		RULE_gadget_define = 14, RULE_gravity = 15, RULE_friction1 = 16, RULE_friction2 = 17, 
		RULE_define_board = 18, RULE_fire_trigger = 19, RULE_action = 20, RULE_fire_trigger_action = 21;
	public static final String[] ruleNames = {
		"root", "numvalue", "xPos", "yPos", "position", "type", "name", "width", 
		"height", "size", "speed", "orientation", "otherboard", "otherportal", 
		"gadget_define", "gravity", "friction1", "friction2", "define_board", 
		"fire_trigger", "action", "fire_trigger_action"
	};

	@Override
	public String getGrammarFileName() { return "boardGrammar.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public boardGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class RootContext extends ParserRuleContext {
		public List<TerminalNode> COMMENT() { return getTokens(boardGrammarParser.COMMENT); }
		public List<Fire_trigger_actionContext> fire_trigger_action() {
			return getRuleContexts(Fire_trigger_actionContext.class);
		}
		public Define_boardContext define_board() {
			return getRuleContext(Define_boardContext.class,0);
		}
		public Gadget_defineContext gadget_define(int i) {
			return getRuleContext(Gadget_defineContext.class,i);
		}
		public TerminalNode EOF() { return getToken(boardGrammarParser.EOF, 0); }
		public TerminalNode COMMENT(int i) {
			return getToken(boardGrammarParser.COMMENT, i);
		}
		public Fire_trigger_actionContext fire_trigger_action(int i) {
			return getRuleContext(Fire_trigger_actionContext.class,i);
		}
		public List<Gadget_defineContext> gadget_define() {
			return getRuleContexts(Gadget_defineContext.class);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitRoot(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44); define_board();
			setState(50);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__16) | (1L << T__15) | (1L << T__8) | (1L << T__7) | (1L << T__6) | (1L << T__4) | (1L << T__2) | (1L << COMMENT) | (1L << BALL))) != 0)) {
				{
				setState(48);
				switch (_input.LA(1)) {
				case T__17:
				case T__16:
				case T__15:
				case T__7:
				case T__6:
				case T__4:
				case T__2:
				case BALL:
					{
					setState(45); gadget_define();
					}
					break;
				case COMMENT:
					{
					setState(46); match(COMMENT);
					}
					break;
				case T__8:
					{
					setState(47); fire_trigger_action();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(52);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(53); match(EOF);
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

	public static class NumvalueContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(boardGrammarParser.INTEGER, 0); }
		public TerminalNode FLOAT() { return getToken(boardGrammarParser.FLOAT, 0); }
		public NumvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterNumvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitNumvalue(this);
		}
	}

	public final NumvalueContext numvalue() throws RecognitionException {
		NumvalueContext _localctx = new NumvalueContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_numvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			_la = _input.LA(1);
			if ( !(_la==INTEGER || _la==FLOAT) ) {
			_errHandler.recoverInline(this);
			}
			consume();
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

	public static class XPosContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public NumvalueContext numvalue() {
			return getRuleContext(NumvalueContext.class,0);
		}
		public XPosContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xPos; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterXPos(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitXPos(this);
		}
	}

	public final XPosContext xPos() throws RecognitionException {
		XPosContext _localctx = new XPosContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_xPos);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57); match(T__14);
			setState(58); match(EQUALS);
			setState(59); numvalue();
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

	public static class YPosContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public NumvalueContext numvalue() {
			return getRuleContext(NumvalueContext.class,0);
		}
		public YPosContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_yPos; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterYPos(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitYPos(this);
		}
	}

	public final YPosContext yPos() throws RecognitionException {
		YPosContext _localctx = new YPosContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_yPos);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61); match(T__13);
			setState(62); match(EQUALS);
			setState(63); numvalue();
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

	public static class PositionContext extends ParserRuleContext {
		public XPosContext xPos() {
			return getRuleContext(XPosContext.class,0);
		}
		public YPosContext yPos() {
			return getRuleContext(YPosContext.class,0);
		}
		public PositionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_position; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterPosition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitPosition(this);
		}
	}

	public final PositionContext position() throws RecognitionException {
		PositionContext _localctx = new PositionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_position);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65); xPos();
			setState(66); yPos();
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

	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitType(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__16) | (1L << T__15) | (1L << T__7) | (1L << T__6) | (1L << T__4) | (1L << T__2) | (1L << BALL))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
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

	public static class NameContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public TerminalNode NAME() { return getToken(boardGrammarParser.NAME, 0); }
		public TerminalNode NAMEX() { return getToken(boardGrammarParser.NAMEX, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitName(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70); match(NAME);
			setState(71); match(EQUALS);
			setState(72); match(NAMEX);
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

	public static class WidthContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public NumvalueContext numvalue() {
			return getRuleContext(NumvalueContext.class,0);
		}
		public WidthContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_width; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterWidth(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitWidth(this);
		}
	}

	public final WidthContext width() throws RecognitionException {
		WidthContext _localctx = new WidthContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_width);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74); match(T__9);
			setState(75); match(EQUALS);
			setState(76); numvalue();
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

	public static class HeightContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public NumvalueContext numvalue() {
			return getRuleContext(NumvalueContext.class,0);
		}
		public HeightContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_height; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterHeight(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitHeight(this);
		}
	}

	public final HeightContext height() throws RecognitionException {
		HeightContext _localctx = new HeightContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_height);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78); match(T__18);
			setState(79); match(EQUALS);
			setState(80); numvalue();
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

	public static class SizeContext extends ParserRuleContext {
		public WidthContext width() {
			return getRuleContext(WidthContext.class,0);
		}
		public HeightContext height() {
			return getRuleContext(HeightContext.class,0);
		}
		public SizeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_size; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterSize(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitSize(this);
		}
	}

	public final SizeContext size() throws RecognitionException {
		SizeContext _localctx = new SizeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_size);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82); width();
			setState(83); height();
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

	public static class SpeedContext extends ParserRuleContext {
		public List<TerminalNode> EQUALS() { return getTokens(boardGrammarParser.EQUALS); }
		public List<NumvalueContext> numvalue() {
			return getRuleContexts(NumvalueContext.class);
		}
		public TerminalNode EQUALS(int i) {
			return getToken(boardGrammarParser.EQUALS, i);
		}
		public NumvalueContext numvalue(int i) {
			return getRuleContext(NumvalueContext.class,i);
		}
		public SpeedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_speed; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterSpeed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitSpeed(this);
		}
	}

	public final SpeedContext speed() throws RecognitionException {
		SpeedContext _localctx = new SpeedContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_speed);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85); match(T__19);
			setState(86); match(EQUALS);
			setState(87); numvalue();
			setState(88); match(T__5);
			setState(89); match(EQUALS);
			setState(90); numvalue();
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

	public static class OrientationContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public NumvalueContext numvalue() {
			return getRuleContext(NumvalueContext.class,0);
		}
		public OrientationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orientation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterOrientation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitOrientation(this);
		}
	}

	public final OrientationContext orientation() throws RecognitionException {
		OrientationContext _localctx = new OrientationContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_orientation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92); match(T__21);
			setState(93); match(EQUALS);
			setState(94); numvalue();
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

	public static class OtherboardContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public TerminalNode NAMEX() { return getToken(boardGrammarParser.NAMEX, 0); }
		public OtherboardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_otherboard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterOtherboard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitOtherboard(this);
		}
	}

	public final OtherboardContext otherboard() throws RecognitionException {
		OtherboardContext _localctx = new OtherboardContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_otherboard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96); match(T__20);
			setState(97); match(EQUALS);
			setState(98); match(NAMEX);
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

	public static class OtherportalContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public TerminalNode NAMEX() { return getToken(boardGrammarParser.NAMEX, 0); }
		public OtherportalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_otherportal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterOtherportal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitOtherportal(this);
		}
	}

	public final OtherportalContext otherportal() throws RecognitionException {
		OtherportalContext _localctx = new OtherportalContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_otherportal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100); match(T__0);
			setState(101); match(EQUALS);
			setState(102); match(NAMEX);
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

	public static class Gadget_defineContext extends ParserRuleContext {
		public SizeContext size() {
			return getRuleContext(SizeContext.class,0);
		}
		public SpeedContext speed() {
			return getRuleContext(SpeedContext.class,0);
		}
		public OrientationContext orientation() {
			return getRuleContext(OrientationContext.class,0);
		}
		public PositionContext position() {
			return getRuleContext(PositionContext.class,0);
		}
		public OtherportalContext otherportal() {
			return getRuleContext(OtherportalContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public OtherboardContext otherboard() {
			return getRuleContext(OtherboardContext.class,0);
		}
		public Gadget_defineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gadget_define; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterGadget_define(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitGadget_define(this);
		}
	}

	public final Gadget_defineContext gadget_define() throws RecognitionException {
		Gadget_defineContext _localctx = new Gadget_defineContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_gadget_define);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104); type();
			setState(106);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(105); name();
				}
			}

			setState(108); position();
			setState(110);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(109); size();
				}
			}

			setState(113);
			_la = _input.LA(1);
			if (_la==T__19) {
				{
				setState(112); speed();
				}
			}

			setState(116);
			_la = _input.LA(1);
			if (_la==T__21) {
				{
				setState(115); orientation();
				}
			}

			setState(119);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(118); otherboard();
				}
			}

			setState(122);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(121); otherportal();
				}
			}

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

	public static class GravityContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public NumvalueContext numvalue() {
			return getRuleContext(NumvalueContext.class,0);
		}
		public GravityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gravity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterGravity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitGravity(this);
		}
	}

	public final GravityContext gravity() throws RecognitionException {
		GravityContext _localctx = new GravityContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_gravity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124); match(T__1);
			setState(125); match(EQUALS);
			setState(126); numvalue();
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

	public static class Friction1Context extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public NumvalueContext numvalue() {
			return getRuleContext(NumvalueContext.class,0);
		}
		public Friction1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_friction1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterFriction1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitFriction1(this);
		}
	}

	public final Friction1Context friction1() throws RecognitionException {
		Friction1Context _localctx = new Friction1Context(_ctx, getState());
		enterRule(_localctx, 32, RULE_friction1);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128); match(T__10);
			setState(129); match(EQUALS);
			setState(130); numvalue();
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

	public static class Friction2Context extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public NumvalueContext numvalue() {
			return getRuleContext(NumvalueContext.class,0);
		}
		public Friction2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_friction2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterFriction2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitFriction2(this);
		}
	}

	public final Friction2Context friction2() throws RecognitionException {
		Friction2Context _localctx = new Friction2Context(_ctx, getState());
		enterRule(_localctx, 34, RULE_friction2);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132); match(T__12);
			setState(133); match(EQUALS);
			setState(134); numvalue();
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

	public static class Define_boardContext extends ParserRuleContext {
		public Friction1Context friction1() {
			return getRuleContext(Friction1Context.class,0);
		}
		public Friction2Context friction2() {
			return getRuleContext(Friction2Context.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public GravityContext gravity() {
			return getRuleContext(GravityContext.class,0);
		}
		public Define_boardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_define_board; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterDefine_board(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitDefine_board(this);
		}
	}

	public final Define_boardContext define_board() throws RecognitionException {
		Define_boardContext _localctx = new Define_boardContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_define_board);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136); match(T__22);
			setState(137); name();
			setState(139);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(138); gravity();
				}
			}

			setState(142);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(141); friction1();
				}
			}

			setState(145);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(144); friction2();
				}
			}

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

	public static class Fire_triggerContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public TerminalNode NAMEX() { return getToken(boardGrammarParser.NAMEX, 0); }
		public Fire_triggerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fire_trigger; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterFire_trigger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitFire_trigger(this);
		}
	}

	public final Fire_triggerContext fire_trigger() throws RecognitionException {
		Fire_triggerContext _localctx = new Fire_triggerContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_fire_trigger);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147); match(T__8);
			setState(148); match(T__11);
			setState(149); match(EQUALS);
			setState(150); match(NAMEX);
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

	public static class ActionContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(boardGrammarParser.EQUALS, 0); }
		public TerminalNode NAMEX() { return getToken(boardGrammarParser.NAMEX, 0); }
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitAction(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_action);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152); match(T__3);
			setState(153); match(EQUALS);
			setState(154); match(NAMEX);
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

	public static class Fire_trigger_actionContext extends ParserRuleContext {
		public Fire_triggerContext fire_trigger() {
			return getRuleContext(Fire_triggerContext.class,0);
		}
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public Fire_trigger_actionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fire_trigger_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).enterFire_trigger_action(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof boardGrammarListener ) ((boardGrammarListener)listener).exitFire_trigger_action(this);
		}
	}

	public final Fire_trigger_actionContext fire_trigger_action() throws RecognitionException {
		Fire_trigger_actionContext _localctx = new Fire_trigger_actionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_fire_trigger_action);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156); fire_trigger();
			setState(157); action();
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\"\u00a2\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\2\3\2\7\2"+
		"\63\n\2\f\2\16\2\66\13\2\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3"+
		"\5\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\5\20m\n\20\3\20\3\20\5\20q\n"+
		"\20\3\20\5\20t\n\20\3\20\5\20w\n\20\3\20\5\20z\n\20\3\20\5\20}\n\20\3"+
		"\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3"+
		"\24\5\24\u008e\n\24\3\24\5\24\u0091\n\24\3\24\5\24\u0094\n\24\3\25\3\25"+
		"\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\2\2\30\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,\2\4\3\2 !\7\2\b\n\22\23\25\25"+
		"\27\27\35\35\u0097\2.\3\2\2\2\49\3\2\2\2\6;\3\2\2\2\b?\3\2\2\2\nC\3\2"+
		"\2\2\fF\3\2\2\2\16H\3\2\2\2\20L\3\2\2\2\22P\3\2\2\2\24T\3\2\2\2\26W\3"+
		"\2\2\2\30^\3\2\2\2\32b\3\2\2\2\34f\3\2\2\2\36j\3\2\2\2 ~\3\2\2\2\"\u0082"+
		"\3\2\2\2$\u0086\3\2\2\2&\u008a\3\2\2\2(\u0095\3\2\2\2*\u009a\3\2\2\2,"+
		"\u009e\3\2\2\2.\64\5&\24\2/\63\5\36\20\2\60\63\7\32\2\2\61\63\5,\27\2"+
		"\62/\3\2\2\2\62\60\3\2\2\2\62\61\3\2\2\2\63\66\3\2\2\2\64\62\3\2\2\2\64"+
		"\65\3\2\2\2\65\67\3\2\2\2\66\64\3\2\2\2\678\7\2\2\38\3\3\2\2\29:\t\2\2"+
		"\2:\5\3\2\2\2;<\7\13\2\2<=\7\"\2\2=>\5\4\3\2>\7\3\2\2\2?@\7\f\2\2@A\7"+
		"\"\2\2AB\5\4\3\2B\t\3\2\2\2CD\5\6\4\2DE\5\b\5\2E\13\3\2\2\2FG\t\3\2\2"+
		"G\r\3\2\2\2HI\7\36\2\2IJ\7\"\2\2JK\7\37\2\2K\17\3\2\2\2LM\7\20\2\2MN\7"+
		"\"\2\2NO\5\4\3\2O\21\3\2\2\2PQ\7\7\2\2QR\7\"\2\2RS\5\4\3\2S\23\3\2\2\2"+
		"TU\5\20\t\2UV\5\22\n\2V\25\3\2\2\2WX\7\6\2\2XY\7\"\2\2YZ\5\4\3\2Z[\7\24"+
		"\2\2[\\\7\"\2\2\\]\5\4\3\2]\27\3\2\2\2^_\7\4\2\2_`\7\"\2\2`a\5\4\3\2a"+
		"\31\3\2\2\2bc\7\5\2\2cd\7\"\2\2de\7\37\2\2e\33\3\2\2\2fg\7\31\2\2gh\7"+
		"\"\2\2hi\7\37\2\2i\35\3\2\2\2jl\5\f\7\2km\5\16\b\2lk\3\2\2\2lm\3\2\2\2"+
		"mn\3\2\2\2np\5\n\6\2oq\5\24\13\2po\3\2\2\2pq\3\2\2\2qs\3\2\2\2rt\5\26"+
		"\f\2sr\3\2\2\2st\3\2\2\2tv\3\2\2\2uw\5\30\r\2vu\3\2\2\2vw\3\2\2\2wy\3"+
		"\2\2\2xz\5\32\16\2yx\3\2\2\2yz\3\2\2\2z|\3\2\2\2{}\5\34\17\2|{\3\2\2\2"+
		"|}\3\2\2\2}\37\3\2\2\2~\177\7\30\2\2\177\u0080\7\"\2\2\u0080\u0081\5\4"+
		"\3\2\u0081!\3\2\2\2\u0082\u0083\7\17\2\2\u0083\u0084\7\"\2\2\u0084\u0085"+
		"\5\4\3\2\u0085#\3\2\2\2\u0086\u0087\7\r\2\2\u0087\u0088\7\"\2\2\u0088"+
		"\u0089\5\4\3\2\u0089%\3\2\2\2\u008a\u008b\7\3\2\2\u008b\u008d\5\16\b\2"+
		"\u008c\u008e\5 \21\2\u008d\u008c\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u0090"+
		"\3\2\2\2\u008f\u0091\5\"\22\2\u0090\u008f\3\2\2\2\u0090\u0091\3\2\2\2"+
		"\u0091\u0093\3\2\2\2\u0092\u0094\5$\23\2\u0093\u0092\3\2\2\2\u0093\u0094"+
		"\3\2\2\2\u0094\'\3\2\2\2\u0095\u0096\7\21\2\2\u0096\u0097\7\16\2\2\u0097"+
		"\u0098\7\"\2\2\u0098\u0099\7\37\2\2\u0099)\3\2\2\2\u009a\u009b\7\26\2"+
		"\2\u009b\u009c\7\"\2\2\u009c\u009d\7\37\2\2\u009d+\3\2\2\2\u009e\u009f"+
		"\5(\25\2\u009f\u00a0\5*\26\2\u00a0-\3\2\2\2\r\62\64lpsvy|\u008d\u0090"+
		"\u0093";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}