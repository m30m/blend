/* The following code was generated by JFlex 1.6.1 */

/* JFlex example: partial Java language lexer specification */



/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.1
 * from the specification file <tt>lexer.flex</tt>
 */
class Lexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int STRING = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\12\1\3\1\2\1\0\1\3\1\1\16\12\4\0\1\3\1\0"+
    "\1\30\1\0\1\11\1\32\1\33\3\0\1\10\1\36\1\0\1\5"+
    "\1\0\1\7\1\13\11\14\2\0\1\4\1\31\1\6\2\0\32\11"+
    "\1\0\1\37\1\0\1\34\1\11\1\0\1\15\1\16\1\22\1\11"+
    "\1\25\5\11\1\27\1\24\1\11\1\26\1\23\2\11\1\21\1\17"+
    "\1\20\6\11\1\0\1\35\2\0\41\12\2\0\4\11\4\0\1\11"+
    "\2\0\1\12\7\0\1\11\4\0\1\11\5\0\27\11\1\0\37\11"+
    "\1\0\u01ca\11\4\0\14\11\16\0\5\11\7\0\1\11\1\0\1\11"+
    "\21\0\160\12\5\11\1\0\2\11\2\0\4\11\10\0\1\11\1\0"+
    "\3\11\1\0\1\11\1\0\24\11\1\0\123\11\1\0\213\11\1\0"+
    "\5\12\2\0\236\11\11\0\46\11\2\0\1\11\7\0\47\11\7\0"+
    "\1\11\1\0\55\12\1\0\1\12\1\0\2\12\1\0\2\12\1\0"+
    "\1\12\10\0\33\11\5\0\3\11\15\0\5\12\6\0\1\11\4\0"+
    "\13\12\5\0\53\11\37\12\4\0\2\11\1\12\143\11\1\0\1\11"+
    "\10\12\1\0\6\12\2\11\2\12\1\0\4\12\2\11\12\12\3\11"+
    "\2\0\1\11\17\0\1\12\1\11\1\12\36\11\33\12\2\0\131\11"+
    "\13\12\1\11\16\0\12\12\41\11\11\12\2\11\4\0\1\11\5\0"+
    "\26\11\4\12\1\11\11\12\1\11\3\12\1\11\5\12\22\0\31\11"+
    "\3\12\104\0\1\11\1\0\13\11\67\0\33\12\1\0\4\12\66\11"+
    "\3\12\1\11\22\12\1\11\7\12\12\11\2\12\2\0\12\12\1\0"+
    "\7\11\1\0\7\11\1\0\3\12\1\0\10\11\2\0\2\11\2\0"+
    "\26\11\1\0\7\11\1\0\1\11\3\0\4\11\2\0\1\12\1\11"+
    "\7\12\2\0\2\12\2\0\3\12\1\11\10\0\1\12\4\0\2\11"+
    "\1\0\3\11\2\12\2\0\12\12\4\11\7\0\1\11\5\0\3\12"+
    "\1\0\6\11\4\0\2\11\2\0\26\11\1\0\7\11\1\0\2\11"+
    "\1\0\2\11\1\0\2\11\2\0\1\12\1\0\5\12\4\0\2\12"+
    "\2\0\3\12\3\0\1\12\7\0\4\11\1\0\1\11\7\0\14\12"+
    "\3\11\1\12\13\0\3\12\1\0\11\11\1\0\3\11\1\0\26\11"+
    "\1\0\7\11\1\0\2\11\1\0\5\11\2\0\1\12\1\11\10\12"+
    "\1\0\3\12\1\0\3\12\2\0\1\11\17\0\2\11\2\12\2\0"+
    "\12\12\1\0\1\11\17\0\3\12\1\0\10\11\2\0\2\11\2\0"+
    "\26\11\1\0\7\11\1\0\2\11\1\0\5\11\2\0\1\12\1\11"+
    "\7\12\2\0\2\12\2\0\3\12\10\0\2\12\4\0\2\11\1\0"+
    "\3\11\2\12\2\0\12\12\1\0\1\11\20\0\1\12\1\11\1\0"+
    "\6\11\3\0\3\11\1\0\4\11\3\0\2\11\1\0\1\11\1\0"+
    "\2\11\3\0\2\11\3\0\3\11\3\0\14\11\4\0\5\12\3\0"+
    "\3\12\1\0\4\12\2\0\1\11\6\0\1\12\16\0\12\12\11\0"+
    "\1\11\7\0\3\12\1\0\10\11\1\0\3\11\1\0\27\11\1\0"+
    "\12\11\1\0\5\11\3\0\1\11\7\12\1\0\3\12\1\0\4\12"+
    "\7\0\2\12\1\0\2\11\6\0\2\11\2\12\2\0\12\12\22\0"+
    "\2\12\1\0\10\11\1\0\3\11\1\0\27\11\1\0\12\11\1\0"+
    "\5\11\2\0\1\12\1\11\7\12\1\0\3\12\1\0\4\12\7\0"+
    "\2\12\7\0\1\11\1\0\2\11\2\12\2\0\12\12\1\0\2\11"+
    "\17\0\2\12\1\0\10\11\1\0\3\11\1\0\51\11\2\0\1\11"+
    "\7\12\1\0\3\12\1\0\4\12\1\11\10\0\1\12\10\0\2\11"+
    "\2\12\2\0\12\12\12\0\6\11\2\0\2\12\1\0\22\11\3\0"+
    "\30\11\1\0\11\11\1\0\1\11\2\0\7\11\3\0\1\12\4\0"+
    "\6\12\1\0\1\12\1\0\10\12\22\0\2\12\15\0\60\11\1\12"+
    "\2\11\7\12\4\0\10\11\10\12\1\0\12\12\47\0\2\11\1\0"+
    "\1\11\2\0\2\11\1\0\1\11\2\0\1\11\6\0\4\11\1\0"+
    "\7\11\1\0\3\11\1\0\1\11\1\0\1\11\2\0\2\11\1\0"+
    "\4\11\1\12\2\11\6\12\1\0\2\12\1\11\2\0\5\11\1\0"+
    "\1\11\1\0\6\12\2\0\12\12\2\0\4\11\40\0\1\11\27\0"+
    "\2\12\6\0\12\12\13\0\1\12\1\0\1\12\1\0\1\12\4\0"+
    "\2\12\10\11\1\0\44\11\4\0\24\12\1\0\2\12\5\11\13\12"+
    "\1\0\44\12\11\0\1\12\71\0\53\11\24\12\1\11\12\12\6\0"+
    "\6\11\4\12\4\11\3\12\1\11\3\12\2\11\7\12\3\11\4\12"+
    "\15\11\14\12\1\11\17\12\2\0\46\11\1\0\1\11\5\0\1\11"+
    "\2\0\53\11\1\0\u014d\11\1\0\4\11\2\0\7\11\1\0\1\11"+
    "\1\0\4\11\2\0\51\11\1\0\4\11\2\0\41\11\1\0\4\11"+
    "\2\0\7\11\1\0\1\11\1\0\4\11\2\0\17\11\1\0\71\11"+
    "\1\0\4\11\2\0\103\11\2\0\3\12\40\0\20\11\20\0\125\11"+
    "\14\0\u026c\11\2\0\21\11\1\0\32\11\5\0\113\11\3\0\3\11"+
    "\17\0\15\11\1\0\4\11\3\12\13\0\22\11\3\12\13\0\22\11"+
    "\2\12\14\0\15\11\1\0\3\11\1\0\2\12\14\0\64\11\40\12"+
    "\3\0\1\11\3\0\2\11\1\12\2\0\12\12\41\0\3\12\2\0"+
    "\12\12\6\0\130\11\10\0\51\11\1\12\1\11\5\0\106\11\12\0"+
    "\35\11\3\0\14\12\4\0\14\12\12\0\12\12\36\11\2\0\5\11"+
    "\13\0\54\11\4\0\21\12\7\11\2\12\6\0\12\12\46\0\27\11"+
    "\5\12\4\0\65\11\12\12\1\0\35\12\2\0\13\12\6\0\12\12"+
    "\15\0\1\11\130\0\5\12\57\11\21\12\7\11\4\0\12\12\21\0"+
    "\11\12\14\0\3\12\36\11\15\12\2\11\12\12\54\11\16\12\14\0"+
    "\44\11\24\12\10\0\12\12\3\0\3\11\12\12\44\11\122\0\3\12"+
    "\1\0\25\12\4\11\1\12\4\11\3\12\2\11\11\0\300\11\47\12"+
    "\25\0\4\12\u0116\11\2\0\6\11\2\0\46\11\2\0\6\11\2\0"+
    "\10\11\1\0\1\11\1\0\1\11\1\0\1\11\1\0\37\11\2\0"+
    "\65\11\1\0\7\11\1\0\1\11\3\0\3\11\1\0\7\11\3\0"+
    "\4\11\2\0\6\11\4\0\15\11\5\0\3\11\1\0\7\11\16\0"+
    "\5\12\32\0\5\12\20\0\2\11\23\0\1\11\13\0\5\12\5\0"+
    "\6\12\1\0\1\11\15\0\1\11\20\0\15\11\3\0\33\11\25\0"+
    "\15\12\4\0\1\12\3\0\14\12\21\0\1\11\4\0\1\11\2\0"+
    "\12\11\1\0\1\11\3\0\5\11\6\0\1\11\1\0\1\11\1\0"+
    "\1\11\1\0\4\11\1\0\13\11\2\0\4\11\5\0\5\11\4\0"+
    "\1\11\21\0\51\11\u0a77\0\57\11\1\0\57\11\1\0\205\11\6\0"+
    "\4\11\3\12\2\11\14\0\46\11\1\0\1\11\5\0\1\11\2\0"+
    "\70\11\7\0\1\11\17\0\1\12\27\11\11\0\7\11\1\0\7\11"+
    "\1\0\7\11\1\0\7\11\1\0\7\11\1\0\7\11\1\0\7\11"+
    "\1\0\7\11\1\0\40\12\57\0\1\11\u01d5\0\3\11\31\0\11\11"+
    "\6\12\1\0\5\11\2\0\5\11\4\0\126\11\2\0\2\12\2\0"+
    "\3\11\1\0\132\11\1\0\4\11\5\0\51\11\3\0\136\11\21\0"+
    "\33\11\65\0\20\11\u0200\0\u19b6\11\112\0\u51cd\11\63\0\u048d\11\103\0"+
    "\56\11\2\0\u010d\11\3\0\20\11\12\12\2\11\24\0\57\11\1\12"+
    "\4\0\12\12\1\0\31\11\7\0\1\12\120\11\2\12\45\0\11\11"+
    "\2\0\147\11\2\0\4\11\1\0\4\11\14\0\13\11\115\0\12\11"+
    "\1\12\3\11\1\12\4\11\1\12\27\11\5\12\20\0\1\11\7\0"+
    "\64\11\14\0\2\12\62\11\21\12\13\0\12\12\6\0\22\12\6\11"+
    "\3\0\1\11\4\0\12\12\34\11\10\12\2\0\27\11\15\12\14\0"+
    "\35\11\3\0\4\12\57\11\16\12\16\0\1\11\12\12\46\0\51\11"+
    "\16\12\11\0\3\11\1\12\10\11\2\12\2\0\12\12\6\0\27\11"+
    "\3\0\1\11\1\12\4\0\60\11\1\12\1\11\3\12\2\11\2\12"+
    "\5\11\2\12\1\11\1\12\1\11\30\0\3\11\2\0\13\11\5\12"+
    "\2\0\3\11\2\12\12\0\6\11\2\0\6\11\2\0\6\11\11\0"+
    "\7\11\1\0\7\11\221\0\43\11\10\12\1\0\2\12\2\0\12\12"+
    "\6\0\u2ba4\11\14\0\27\11\4\0\61\11\u2104\0\u016e\11\2\0\152\11"+
    "\46\0\7\11\14\0\5\11\5\0\1\11\1\12\12\11\1\0\15\11"+
    "\1\0\5\11\1\0\1\11\1\0\2\11\1\0\2\11\1\0\154\11"+
    "\41\0\u016b\11\22\0\100\11\2\0\66\11\50\0\15\11\3\0\20\12"+
    "\20\0\7\12\14\0\2\11\30\0\3\11\31\0\1\11\6\0\5\11"+
    "\1\0\207\11\2\0\1\12\4\0\1\11\13\0\12\12\7\0\32\11"+
    "\4\0\1\11\1\0\32\11\13\0\131\11\3\0\6\11\2\0\6\11"+
    "\2\0\6\11\2\0\3\11\3\0\2\11\3\0\2\11\22\0\3\12"+
    "\4\0\14\11\1\0\32\11\1\0\23\11\1\0\2\11\1\0\17\11"+
    "\2\0\16\11\42\0\173\11\105\0\65\11\210\0\1\12\202\0\35\11"+
    "\3\0\61\11\57\0\37\11\21\0\33\11\65\0\36\11\2\0\44\11"+
    "\4\0\10\11\1\0\5\11\52\0\236\11\2\0\12\12\u0356\0\6\11"+
    "\2\0\1\11\1\0\54\11\1\0\2\11\3\0\1\11\2\0\27\11"+
    "\252\0\26\11\12\0\32\11\106\0\70\11\6\0\2\11\100\0\1\11"+
    "\3\12\1\0\2\12\5\0\4\12\4\11\1\0\3\11\1\0\33\11"+
    "\4\0\3\12\4\0\1\12\40\0\35\11\203\0\66\11\12\0\26\11"+
    "\12\0\23\11\215\0\111\11\u03b7\0\3\12\65\11\17\12\37\0\12\12"+
    "\20\0\3\12\55\11\13\12\2\0\1\12\22\0\31\11\7\0\12\12"+
    "\6\0\3\12\44\11\16\12\1\0\12\12\100\0\3\12\60\11\16\12"+
    "\4\11\13\0\12\12\u04a6\0\53\11\15\12\10\0\12\12\u0936\0\u036f\11"+
    "\221\0\143\11\u0b9d\0\u042f\11\u33d1\0\u0239\11\u04c7\0\105\11\13\0\1\11"+
    "\56\12\20\0\4\12\15\11\u4060\0\2\11\u2163\0\5\12\3\0\26\12"+
    "\2\0\7\12\36\0\4\12\224\0\3\12\u01bb\0\125\11\1\0\107\11"+
    "\1\0\2\11\2\0\1\11\2\0\2\11\2\0\4\11\1\0\14\11"+
    "\1\0\1\11\1\0\7\11\1\0\101\11\1\0\4\11\2\0\10\11"+
    "\1\0\7\11\1\0\34\11\1\0\4\11\1\0\5\11\1\0\1\11"+
    "\3\0\7\11\1\0\u0154\11\2\0\31\11\1\0\31\11\1\0\37\11"+
    "\1\0\31\11\1\0\37\11\1\0\31\11\1\0\37\11\1\0\31\11"+
    "\1\0\37\11\1\0\31\11\1\0\10\11\2\0\62\12\u1600\0\4\11"+
    "\1\0\33\11\1\0\2\11\1\0\1\11\2\0\1\11\1\0\12\11"+
    "\1\0\4\11\1\0\1\11\1\0\1\11\6\0\1\11\4\0\1\11"+
    "\1\0\1\11\1\0\1\11\1\0\3\11\1\0\2\11\1\0\1\11"+
    "\2\0\1\11\1\0\1\11\1\0\1\11\1\0\1\11\1\0\1\11"+
    "\1\0\2\11\1\0\1\11\2\0\4\11\1\0\7\11\1\0\4\11"+
    "\1\0\4\11\1\0\1\11\1\0\12\11\1\0\21\11\5\0\3\11"+
    "\1\0\5\11\1\0\21\11\u1144\0\ua6d7\11\51\0\u1035\11\13\0\336\11"+
    "\u3fe2\0\u021e\11\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\u05ee\0"+
    "\1\12\36\0\140\12\200\0\360\12\uffff\0\uffff\0\ufe12\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\2\2\1\1\1\3\1\4\1\5\1\6"+
    "\2\7\2\6\1\10\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\17\1\20\1\21\1\0\1\2\1\0\3\6"+
    "\1\22\1\23\1\24\1\25\1\26\1\27\1\30\2\0"+
    "\3\6\2\0\3\6\1\0\1\6\1\31\4\6";

  private static int [] zzUnpackAction() {
    int [] result = new int[54];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\40\0\100\0\140\0\100\0\200\0\240\0\300"+
    "\0\100\0\340\0\100\0\u0100\0\u0120\0\u0140\0\100\0\u0160"+
    "\0\100\0\u0180\0\100\0\u01a0\0\100\0\u01c0\0\100\0\u01e0"+
    "\0\u0200\0\u0220\0\u0240\0\u0260\0\u0280\0\u02a0\0\100\0\100"+
    "\0\100\0\100\0\100\0\100\0\100\0\u02c0\0\u02e0\0\u0300"+
    "\0\u0320\0\u0340\0\u0360\0\u0380\0\u03a0\0\u03c0\0\u03e0\0\u0400"+
    "\0\u0420\0\340\0\u0440\0\u0460\0\u0480\0\u04a0";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[54];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\2\5\1\6\1\7\1\3\1\10\1\11"+
    "\1\12\1\3\1\13\1\14\1\15\1\16\11\12\1\17"+
    "\1\20\1\21\1\22\1\23\1\24\1\25\1\3\1\26"+
    "\2\3\25\26\1\27\6\26\1\30\42\0\1\5\42\0"+
    "\1\31\37\0\1\32\42\0\1\33\40\0\17\12\23\0"+
    "\2\14\34\0\5\12\1\34\11\12\21\0\10\12\1\35"+
    "\1\12\1\36\4\12\41\0\1\37\41\0\1\40\41\0"+
    "\1\41\2\0\1\26\2\0\25\26\1\0\6\26\21\0"+
    "\1\42\1\43\4\0\1\44\1\0\1\45\14\0\1\46"+
    "\32\0\1\32\1\4\1\5\35\32\10\0\1\47\40\0"+
    "\6\12\1\50\10\12\21\0\14\12\1\51\2\12\21\0"+
    "\12\12\1\52\4\12\10\0\5\46\1\53\32\46\10\47"+
    "\1\54\27\47\11\0\7\12\1\55\7\12\21\0\4\12"+
    "\1\56\12\12\21\0\13\12\1\57\3\12\10\0\5\46"+
    "\1\60\32\46\7\47\1\5\1\54\27\47\11\0\10\12"+
    "\1\61\6\12\21\0\16\12\1\62\21\0\14\12\1\63"+
    "\2\12\10\0\5\46\1\60\1\5\31\46\11\0\4\12"+
    "\1\64\12\12\21\0\4\12\1\65\12\12\21\0\11\12"+
    "\1\66\5\12\21\0\15\12\1\62\1\12\21\0\7\12"+
    "\1\62\7\12\10\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1216];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\1\1\1\11\3\1\1\11\1\1\1\11"+
    "\3\1\1\11\1\1\1\11\1\1\1\11\1\1\1\11"+
    "\1\1\1\11\1\1\1\0\1\1\1\0\3\1\7\11"+
    "\2\0\3\1\2\0\3\1\1\0\6\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[54];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
  StringBuffer string = new StringBuffer();




  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  Lexer(java.io.Reader in)