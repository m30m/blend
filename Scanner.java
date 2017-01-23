/* The following code was generated by JFlex 1.6.1 */

/* JFlex example: partial Java language lexer specification */



/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.1
 * from the specification file <tt>scanner.flex</tt>
 */
public class Scanner {

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
    "\1\30\1\0\1\11\1\32\1\33\3\0\1\10\1\32\1\0\1\5"+
    "\1\0\1\7\1\13\11\14\2\0\1\4\1\31\1\6\2\0\32\11"+
    "\1\0\1\35\1\0\1\32\1\11\1\0\1\15\1\16\1\22\1\11"+
    "\1\25\5\11\1\27\1\24\1\11\1\26\1\23\2\11\1\21\1\17"+
    "\1\20\6\11\1\0\1\34\2\0\41\12\2\0\4\11\4\0\1\11"+
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
    "\2\0\1\1\2\2\1\1\3\3\1\4\2\5\2\4"+
    "\1\6\3\3\1\7\1\10\1\11\1\0\1\2\1\0"+
    "\3\4\1\12\1\13\1\14\1\15\2\0\3\4\2\0"+
    "\3\4\1\0\1\4\1\3\4\4";

  private static int [] zzUnpackAction() {
    int [] result = new int[48];
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
    "\0\0\0\36\0\74\0\132\0\74\0\170\0\226\0\264"+
    "\0\74\0\322\0\74\0\360\0\u010e\0\u012c\0\74\0\u014a"+
    "\0\u0168\0\u0186\0\u01a4\0\74\0\u01c2\0\u01e0\0\u01fe\0\u021c"+
    "\0\u023a\0\u0258\0\u0276\0\74\0\74\0\74\0\74\0\u0294"+
    "\0\u02b2\0\u02d0\0\u02ee\0\u030c\0\u032a\0\u0348\0\u0366\0\u0384"+
    "\0\u03a2\0\u03c0\0\u03de\0\322\0\u03fc\0\u041a\0\u0438\0\u0456";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[48];
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
    "\1\20\1\11\1\21\1\22\1\3\1\23\2\3\25\23"+
    "\1\24\4\23\1\25\40\0\1\5\40\0\1\26\35\0"+
    "\1\27\40\0\1\30\36\0\17\12\21\0\2\14\32\0"+
    "\5\12\1\31\11\12\17\0\10\12\1\32\1\12\1\33"+
    "\4\12\37\0\1\11\37\0\1\11\36\0\1\11\1\0"+
    "\1\23\2\0\25\23\1\0\4\23\21\0\1\34\1\35"+
    "\4\0\1\36\1\0\1\37\12\0\1\40\30\0\1\27"+
    "\1\4\1\5\33\27\10\0\1\41\36\0\6\12\1\42"+
    "\10\12\17\0\14\12\1\43\2\12\17\0\12\12\1\44"+
    "\4\12\6\0\5\40\1\45\30\40\10\41\1\46\25\41"+
    "\11\0\7\12\1\47\7\12\17\0\4\12\1\50\12\12"+
    "\17\0\13\12\1\51\3\12\6\0\5\40\1\52\30\40"+
    "\7\41\1\5\1\46\25\41\11\0\10\12\1\53\6\12"+
    "\17\0\16\12\1\54\17\0\14\12\1\55\2\12\6\0"+
    "\5\40\1\52\1\5\27\40\11\0\4\12\1\56\12\12"+
    "\17\0\4\12\1\57\12\12\17\0\11\12\1\60\5\12"+
    "\17\0\15\12\1\54\1\12\17\0\7\12\1\54\7\12"+
    "\6\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1140];
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
    "\3\1\1\11\4\1\1\11\1\1\1\0\1\1\1\0"+
    "\3\1\4\11\2\0\3\1\2\0\3\1\1\0\6\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[48];
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

    public int lineNumber() {
        return yyline;
    }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Scanner(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 2824) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public String NextToken() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
          {     return "$";
 }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { throw new Error("Illegal character <"+
                                                    yytext()+">");
            }
          case 14: break;
          case 2: 
            { /* ignore */
            }
          case 15: break;
          case 3: 
            { return yytext();
            }
          case 16: break;
          case 4: 
            { return "id";
            }
          case 17: break;
          case 5: 
            { return "integer";
            }
          case 18: break;
          case 6: 
            { string.setLength(0); yybegin(STRING);
            }
          case 19: break;
          case 7: 
            { string.append( yytext() );
            }
          case 20: break;
          case 8: 
            { yybegin(YYINITIAL);
                                   return
                                   string.toString();
            }
          case 21: break;
          case 9: 
            { string.append('\\');
            }
          case 22: break;
          case 10: 
            { string.append('\t');
            }
          case 23: break;
          case 11: 
            { string.append('\r');
            }
          case 24: break;
          case 12: 
            { string.append('\n');
            }
          case 25: break;
          case 13: 
            { string.append('\"');
            }
          case 26: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
