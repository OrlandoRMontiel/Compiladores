//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "lisp.y"
  import java.lang.Math;
  import java.io.*;
  import java.util.StringTokenizer;
//#line 21 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short ELSE=258;
public final static short WHILE=259;
public final static short FOR=260;
public final static short COMP=261;
public final static short DIFERENTES=262;
public final static short MAY=263;
public final static short MEN=264;
public final static short MAYI=265;
public final static short MENI=266;
public final static short FNCT=267;
public final static short NUMBER=268;
public final static short VAR=269;
public final static short AND=270;
public final static short OR=271;
public final static short FUNC=272;
public final static short RETURN=273;
public final static short PARAMETRO=274;
public final static short PROC=275;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    1,    1,    1,    1,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    7,
    7,    7,    8,    3,    3,    3,    3,    3,    3,   16,
   14,    6,   15,   10,    9,   11,   12,   13,   13,   13,
    4,    5,    5,
};
final static short yylen[] = {                            2,
    0,    2,    3,    2,    1,    3,    2,    1,    1,    8,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    2,    4,    2,    1,    4,    0,
    1,    3,    0,   14,   11,   10,   16,    8,    8,    1,
    1,    1,    0,    0,    1,    1,    1,    0,    1,    3,
    1,    1,    3,
};
final static short yydefred[] = {                         1,
    0,   45,   46,   47,    0,    0,    0,    0,    0,    0,
    0,    9,    0,    0,    0,   41,    0,   28,   40,    0,
    0,    0,    0,    0,    2,    0,    0,    0,    5,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   27,    0,    0,    0,
    0,   25,    0,    3,    0,    7,    4,    0,    0,    0,
    0,   42,    0,    0,   17,   18,   21,   19,   22,   20,
    0,   11,   51,    0,   23,   24,   14,   15,   12,   13,
   16,    6,   31,    0,   44,   44,   49,    0,    0,    0,
   26,    0,   29,    0,    0,    0,    0,    0,    0,    0,
    0,   32,    0,    0,   50,    0,    0,    0,    0,    0,
    0,   44,    0,    0,   52,    0,    0,    0,    0,    0,
    0,   10,    0,    0,    0,    0,   38,   39,   53,    0,
   44,    0,    0,   44,   36,    0,    0,   35,    0,    0,
    0,    0,    0,   34,    0,   44,   37,
};
final static short yydgoto[] = {                          1,
   27,   28,   29,   74,  116,   30,   84,  134,   31,   95,
   32,   33,   88,   34,  120,   35,
};
final static short yysindex[] = {                         0,
    9,    0,    0,    0,   58,   58,   58,   58,   58,   58,
  -88,    0,  -57,   58,   58,    0,   58,    0,    0,   58,
   58,   58,   58,   58,    0,   58,   28,  -54,    0,  -33,
  -24,  -20,  -16, -241, -241,   58,   58,   58,   58,   58,
   58,   58,   58, -239,   58,   58,    0,   58,   58,   58,
   58,    0,   -4,    0,  -28,    0,    0,   58,   58,   58,
   58,    0,   -1,    1,    0,    0,    0,    0,    0,    0,
  -53,    0,    0,  -50,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -29,    0,    0,    0,    2,    3,    4,
    0,  -14,    0,   58,    7,   12,   58,   -9,  -67,  -66,
  -65,    0,  -64,  -63,    0,   58,  -32,  -32, -206,  -32,
  -32,    0,  -32,  -32,    0,  -35,  -32,  -32,    5,  -62,
  -60,    0, -202,  -58,  -56,   58,    0,    0,    0, -186,
    0,    2,  -48,    0,    0,   36,  -32,    0,  -45,  -32,
  -32,  -46,  -32,    0,  -44,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   43,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -23,    0,    0,
  -42,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   35,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -30,  -30,    0,    0,  -26,  -26,    0,    0,
    0,    0,    0,    0,    0,  -19,    0,    0,    0,  -10,
    0,   64,    0,    0,    0,    0,    0,    0,    0,  -26,
    0,    0,  -26,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    6,  328,  -21,    0,    0,   -8,    0,    0,    0,   -6,
    0,    0,  -18,    0,   -7,    0,
};
final static int YYTABLESIZE=471;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         33,
   24,   48,   42,   43,   57,   56,   58,   26,  123,   22,
   20,   93,   21,   23,   94,   59,   48,   30,   25,   60,
   30,   48,   33,   61,   48,   63,   64,   62,   73,   33,
   82,   33,   33,   44,   33,   33,   81,   54,   89,   91,
   90,   24,   92,   99,  100,   97,  101,  103,   26,  106,
   22,   20,  104,   21,   23,  107,  108,  109,  110,  111,
   24,  115,  127,  126,  128,  129,  130,   26,  131,   22,
   20,  133,   21,   23,  137,    8,  139,  141,  144,   96,
  146,   98,    8,    8,    8,    8,    8,    8,    8,  122,
   24,   56,   56,   44,   43,   56,   56,   26,   44,   22,
   20,    8,   21,   23,   44,  119,  121,  132,    0,    0,
  124,  125,  113,  114,   33,  117,  118,    0,   56,    0,
    0,   56,    0,    0,  135,  136,    0,  138,    0,    0,
    0,    0,    0,  142,    0,    8,  145,    0,    0,  147,
    0,    0,  140,    0,    0,    0,  143,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    2,    0,    3,    4,    5,    6,
    7,    8,    9,   10,   11,   12,   13,   14,   15,   16,
   17,   18,   19,    0,    0,    0,   33,    0,   33,   33,
   33,   33,   33,   33,   33,   33,   33,   33,   33,   33,
   33,   33,   33,   33,   33,    2,    0,    3,    4,    5,
    6,    7,    8,    9,   10,   11,   12,   13,   14,   15,
   16,   17,   18,   19,    2,    0,    3,    4,    5,    6,
    7,    8,    9,   10,   11,   12,   13,   14,   15,   16,
   17,   18,   19,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    0,    8,    8,    0,    5,    6,
    7,    8,    9,   10,   11,   12,   13,   14,   15,    0,
   17,   18,   36,   37,   38,   39,   40,   41,    0,    0,
    0,   45,   46,    0,   47,    0,    0,   48,   49,   50,
   51,   52,    0,   53,   55,    0,    0,    0,    0,    0,
    0,    0,    0,   65,   66,   67,   68,   69,   70,   71,
   72,    0,   75,   76,    0,   77,   78,   79,   80,    0,
    0,    0,    0,    0,    0,   83,   85,   86,   87,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  102,    0,    0,  105,    0,    0,    0,    0,    0,
    0,    0,    0,  112,    0,    0,    0,    0,    0,    0,
   55,   55,    0,    0,   55,   55,    0,    0,    0,    0,
    0,    0,    0,   87,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   55,    0,    0,
   55,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         10,
   33,   44,   91,   61,   59,   27,   40,   40,   44,   42,
   43,   41,   45,   46,   44,   40,   59,   41,   10,   40,
   44,   41,   33,   40,   44,   34,   35,  269,  268,   40,
   59,   42,   43,   91,   45,   46,   41,   10,   40,   93,
   40,   33,   93,   41,   41,   44,   61,   41,   40,   59,
   42,   43,   41,   45,   46,  123,  123,  123,  123,  123,
   33,  268,  125,   59,  125,  268,  125,   40,  125,   42,
   43,  258,   45,   46,  123,   33,   41,  123,  125,   86,
  125,   88,   40,   41,   42,   43,   44,   45,   46,  125,
   33,  113,  114,   59,  125,  117,  118,   40,  125,   42,
   43,   59,   45,   46,   41,  112,  114,  126,   -1,   -1,
  117,  118,  107,  108,  125,  110,  111,   -1,  140,   -1,
   -1,  143,   -1,   -1,  131,  132,   -1,  134,   -1,   -1,
   -1,   -1,   -1,  140,   -1,   93,  143,   -1,   -1,  146,
   -1,   -1,  137,   -1,   -1,   -1,  141,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,   -1,  259,  260,  261,  262,
  263,  264,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,   -1,   -1,   -1,  257,   -1,  259,  260,
  261,  262,  263,  264,  265,  266,  267,  268,  269,  270,
  271,  272,  273,  274,  275,  257,   -1,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  270,  271,
  272,  273,  274,  275,  257,   -1,  259,  260,  261,  262,
  263,  264,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,   -1,  273,  274,   -1,  261,  262,
  263,  264,  265,  266,  267,  268,  269,  270,  271,   -1,
  273,  274,    5,    6,    7,    8,    9,   10,   -1,   -1,
   -1,   14,   15,   -1,   17,   -1,   -1,   20,   21,   22,
   23,   24,   -1,   26,   27,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   36,   37,   38,   39,   40,   41,   42,
   43,   -1,   45,   46,   -1,   48,   49,   50,   51,   -1,
   -1,   -1,   -1,   -1,   -1,   58,   59,   60,   61,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   94,   -1,   -1,   97,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  106,   -1,   -1,   -1,   -1,   -1,   -1,
  113,  114,   -1,   -1,  117,  118,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  126,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  140,   -1,   -1,
  143,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=275;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,"'\\n'",null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,"'!'",null,null,null,null,null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'",null,null,null,null,null,null,null,null,null,null,null,null,
"';'",null,"'='",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"IF","ELSE","WHILE","FOR","COMP",
"DIFERENTES","MAY","MEN","MAYI","MENI","FNCT","NUMBER","VAR","AND","OR","FUNC",
"RETURN","PARAMETRO","PROC",
};
final static String yyrule[] = {
"$accept : list",
"list :",
"list : list '\\n'",
"list : list linea '\\n'",
"linea : exp ';'",
"linea : stmt",
"linea : linea exp ';'",
"linea : linea stmt",
"exp : VAR",
"exp : NUMBER",
"exp : VAR '[' initNum ']' '=' '{' lista '}'",
"exp : VAR '=' exp",
"exp : '*' exp exp",
"exp : '.' exp exp",
"exp : '+' exp exp",
"exp : '-' exp exp",
"exp : '(' exp ')'",
"exp : COMP exp exp",
"exp : DIFERENTES exp exp",
"exp : MEN exp exp",
"exp : MENI exp exp",
"exp : MAY exp exp",
"exp : MAYI exp exp",
"exp : AND exp exp",
"exp : OR exp exp",
"exp : '!' exp",
"exp : FNCT '[' exp ']'",
"exp : RETURN exp",
"exp : PARAMETRO",
"exp : nombreProc '(' arglist ')'",
"arglist :",
"arglist : exp",
"arglist : arglist ',' exp",
"nop :",
"stmt : if_ '(' exp stop_ ')' '{' linea stop_ '}' ELSE '{' linea stop_ '}'",
"stmt : if_ '(' exp stop_ ')' '{' linea stop_ '}' nop stop_",
"stmt : while_ '(' exp stop_ ')' '{' linea stop_ '}' stop_",
"stmt : for_ '(' instrucciones stop_ ';' exp stop_ ';' instrucciones stop_ ')' '{' linea stop_ '}' stop_",
"stmt : funcion nombreProc '(' ')' '{' linea null_ '}'",
"stmt : procedimiento nombreProc '(' ')' '{' linea null_ '}'",
"procedimiento : PROC",
"funcion : FUNC",
"nombreProc : VAR",
"null_ :",
"stop_ :",
"if_ : IF",
"while_ : WHILE",
"for_ : FOR",
"instrucciones :",
"instrucciones : exp",
"instrucciones : instrucciones ',' exp",
"initNum : NUMBER",
"lista : NUMBER",
"lista : lista ',' NUMBER",
};

//#line 272 "lisp.y"

  TablaDeSimbolos tablaDeSimbolos = new TablaDeSimbolos();
  MaquinaDePila maquina = new MaquinaDePila(tablaDeSimbolos);
  int i = 0;
  int j = 0;
  double[] auxiliar;
  Vector vAux = new Vector();
  Funcion funcionAux;
  boolean huboError;
  String ins;
  StringTokenizer st;
  
  void yyerror(String s){
    huboError = true;
    System.out.println("error:"+s);
  }

  boolean newline;
  int yylex(){
    String s;
    int tok = 0;
    Double d;
    if (!st.hasMoreTokens()){
      if (!newline){
        newline=true;
        return '\n'; //So we look like classic YACC example
      }
      else
        return 0;
    }
    s = st.nextToken();
    try{
      d = Double.valueOf(s);/*this may fail*/
      yylval = new ParserVal(d.doubleValue()); //SEE BELOW
      return NUMBER;
    }
    catch (Exception e){}

      if(esVariable(s)){
        if(s.equals("proc")){
          return PROC;
        }
        if(s.charAt(0) == '$'){
          yylval = new ParserVal((int)Integer.parseInt(s.substring(1)));
          return PARAMETRO;
        }
        if(s.equals("return")){
          return RETURN;
        }
        if(s.equals("func")){
          return FUNC;
        }
        if(s.equals("==")){
          return COMP;
        }
        if(s.equals("!=")){
          return DIFERENTES;
        }
        if(s.equals("<")){
          return MEN;
        }
        if(s.equals("<=")){
          return MENI;
        }
        if(s.equals(">")){
          return MAY;
        }
        if(s.equals(">=")){
          return MAYI;
        }
        if(s.equals("&&")){
          return AND;
        }
        if(s.equals("||")){
          return OR;
        }
        if(s.equals("if")){
          return IF;
        }
        if(s.equals("else")){
          return ELSE;
        }
        if(s.equals("while")){
          return WHILE;
        }
        if(s.equals("for")){
          return FOR;
        }
        boolean esFuncion = false;
        Object objeto = tablaDeSimbolos.encontrar(s);
        if(objeto instanceof Funcion){
          funcionAux = (Funcion)objeto;
          yylval = new ParserVal(objeto);
          esFuncion = true;
          return FNCT;
        }
        if(!esFuncion){
          yylval = new ParserVal(s);
          return VAR;
        }
      }
      else{
        tok = s.charAt(0);
      }
      //System.out.println("Token: " + tok);
      return tok;
    }

    String reservados[] = {"=", "{", "}", ",", "*", "+", "-", "(", ")", "|", "[", "]", "!",".",";"};
    boolean esVariable(String s){
      boolean cumple = true;
      for(int i = 0; i < reservados.length; i++)
        if(s.equals(reservados[i]))
          cumple = false;
      return cumple;
    }

    void dotest() throws Exception{
      tablaDeSimbolos.insertar("Imprimir", new MaquinaDePila.Imprimir());
      tablaDeSimbolos.insertar("Sumar", new MaquinaDePila.Sumar());
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      while (true){
        huboError = false;
        try{
          ins = in.readLine();
        }
        catch (Exception e){}
        st = new StringTokenizer(ins);
        newline=false;
        //maquina = new MaquinaDePila(tablaDeSimbolos);
        yyparse();
        if(!huboError)
          maquina.ejecutar();
      }
    }

    public static void main(String args[]) throws Exception{
      System.out.println("Bienvenido");
      Parser par = new Parser(false);
      par.dotest();
    }

/*
javac *.java -nowarn
cls
yacc -J P2.y
javac Parser.java
java Parser
*/
//#line 524 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 4:
//#line 49 "lisp.y"
{yyval = val_peek(1);}
break;
case 5:
//#line 50 "lisp.y"
{yyval = val_peek(0);}
break;
case 6:
//#line 51 "lisp.y"
{yyval = val_peek(2);}
break;
case 7:
//#line 52 "lisp.y"
{yyval = val_peek(1);}
break;
case 8:
//#line 55 "lisp.y"
{
              yyval = new ParserVal(maquina.agregarOperacion("varPush_Eval"));
              maquina.agregar(val_peek(0).sval);
            }
break;
case 9:
//#line 59 "lisp.y"
{
              yyval = new ParserVal(maquina.agregarOperacion("constPush"));
              maquina.agregar(val_peek(0).dval);
            }
break;
case 10:
//#line 63 "lisp.y"
{
              vAux = new Vector(auxiliar);
              yyval = new ParserVal(maquina.agregarOperacion("constPush"));
              maquina.agregar(vAux);
              maquina.agregarOperacion("varPush");
              maquina.agregar(val_peek(7).sval);
              maquina.agregarOperacion("asignar");
              maquina.agregarOperacion("varPush_Eval");
              maquina.agregar(val_peek(7).sval);
            }
break;
case 11:
//#line 73 "lisp.y"
{
              yyval = new ParserVal(val_peek(0).ival);
              maquina.agregarOperacion("varPush");
              maquina.agregar(val_peek(2).sval);
              maquina.agregarOperacion("asignar");
              maquina.agregarOperacion("varPush_Eval");
              maquina.agregar(val_peek(2).sval);
            }
break;
case 12:
//#line 81 "lisp.y"
{
              yyval = new ParserVal(val_peek(1).ival);
              maquina.agregarOperacion("productoCruz");
            }
break;
case 13:
//#line 85 "lisp.y"
{
              yyval = new ParserVal(val_peek(1).ival);
              maquina.agregarOperacion("productoPunto");
            }
break;
case 14:
//#line 89 "lisp.y"
{
              yyval = new ParserVal(val_peek(1).ival);
              maquina.agregarOperacion("sumar");
            }
break;
case 15:
//#line 93 "lisp.y"
{
              yyval = new ParserVal(val_peek(1).ival);
              maquina.agregarOperacion("restar");
            }
break;
case 16:
//#line 97 "lisp.y"
{
              yyval = new ParserVal(val_peek(1).ival);
            }
break;
case 17:
//#line 100 "lisp.y"
{
              maquina.agregarOperacion("comparar");
              yyval = val_peek(1);
            }
break;
case 18:
//#line 104 "lisp.y"
{
              maquina.agregarOperacion("compararNot");
              yyval = val_peek(1);
            }
break;
case 19:
//#line 108 "lisp.y"
{
              maquina.agregarOperacion("menor");
              yyval = val_peek(1);
            }
break;
case 20:
//#line 112 "lisp.y"
{
              maquina.agregarOperacion("menorIgual");
              yyval = val_peek(1);
            }
break;
case 21:
//#line 116 "lisp.y"
{
              maquina.agregarOperacion("mayor");
              yyval = val_peek(1);
            }
break;
case 22:
//#line 120 "lisp.y"
{
              maquina.agregarOperacion("mayorIgual");
              yyval = val_peek(1);
            }
break;
case 23:
//#line 124 "lisp.y"
{
              maquina.agregarOperacion("and");
              yyval = val_peek(1);
            }
break;
case 24:
//#line 128 "lisp.y"
{
              maquina.agregarOperacion("or");
              yyval = val_peek(1);
            }
break;
case 25:
//#line 132 "lisp.y"
{
              maquina.agregarOperacion("negar");
              yyval = val_peek(0);
            }
break;
case 26:
//#line 136 "lisp.y"
{
              yyval = new ParserVal(val_peek(1).ival);
              maquina.agregar((Funcion)(val_peek(3).obj));
            }
break;
case 27:
//#line 140 "lisp.y"
{ 
              yyval = val_peek(0); maquina.agregarOperacion("_return"); 
            }
break;
case 28:
//#line 144 "lisp.y"
{ 
              yyval = new ParserVal(maquina.agregarOperacion("push_parametro")); 
              maquina.agregar((int)val_peek(0).ival); 
            }
break;
case 29:
//#line 149 "lisp.y"
{ 
              yyval = new ParserVal(maquina.agregarOperacionEn("invocar",(val_peek(3).ival))); 
              maquina.agregar(null); 
            }
break;
case 31:
//#line 156 "lisp.y"
{
              yyval = val_peek(0); 
              maquina.agregar("Limite");
            }
break;
case 32:
//#line 160 "lisp.y"
{
              yyval = val_peek(2); 
              maquina.agregar("Limite");
            }
break;
case 33:
//#line 166 "lisp.y"
{
              yyval = new ParserVal(maquina.agregarOperacion("nop"));
            }
break;
case 34:
//#line 171 "lisp.y"
{
              yyval = val_peek(13);
              maquina.agregar(val_peek(7).ival, val_peek(13).ival + 1);
              maquina.agregar(val_peek(2).ival, val_peek(13).ival + 2);
              maquina.agregar(maquina.numeroDeElementos() - 1, val_peek(13).ival + 3);
            }
break;
case 35:
//#line 177 "lisp.y"
{
              yyval = val_peek(10);
              maquina.agregar(val_peek(4).ival, val_peek(10).ival + 1);
              maquina.agregar(val_peek(1).ival, val_peek(10).ival + 2);
              maquina.agregar(maquina.numeroDeElementos() - 1, val_peek(10).ival + 3);
            }
break;
case 36:
//#line 183 "lisp.y"
{
              yyval = val_peek(9);
              maquina.agregar(val_peek(3).ival, val_peek(9).ival + 1);
              maquina.agregar(val_peek(0).ival, val_peek(9).ival + 2);
            }
break;
case 37:
//#line 188 "lisp.y"
{
              yyval = val_peek(15);
              maquina.agregar(val_peek(10).ival, val_peek(15).ival + 1);
              maquina.agregar(val_peek(7).ival, val_peek(15).ival + 2);
              maquina.agregar(val_peek(3).ival, val_peek(15).ival + 3);
              maquina.agregar(val_peek(0).ival, val_peek(15).ival + 4);
            }
break;
case 40:
//#line 199 "lisp.y"
{ 
              maquina.agregarOperacion("declaracion"); 
            }
break;
case 41:
//#line 204 "lisp.y"
{ 
              maquina.agregarOperacion("declaracion"); 
            }
break;
case 42:
//#line 209 "lisp.y"
{
              yyval = new ParserVal(maquina.agregar(val_peek(0).sval));
            }
break;
case 43:
//#line 214 "lisp.y"
{
              maquina.agregar(null);
            }
break;
case 44:
//#line 219 "lisp.y"
{
              yyval = new ParserVal(maquina.agregarOperacion("stop"));
            }
break;
case 45:
//#line 224 "lisp.y"
{
              yyval = new ParserVal(maquina.agregarOperacion("_if_then_else"));
              maquina.agregarOperacion("stop");/*then*/
              maquina.agregarOperacion("stop");/*else*/
              maquina.agregarOperacion("stop");/*siguiente comando*/
            }
break;
case 46:
//#line 232 "lisp.y"
{
              yyval = new ParserVal(maquina.agregarOperacion("_while"));
              maquina.agregarOperacion("stop");/*cuerpo*/
              maquina.agregarOperacion("stop");/*final*/
            }
break;
case 47:
//#line 239 "lisp.y"
{
              yyval = new ParserVal(maquina.agregarOperacion("_for"));
              maquina.agregarOperacion("stop");/*condicion*/
              maquina.agregarOperacion("stop");/*instrucción final*/
              maquina.agregarOperacion("stop");/*cuerpo*/
              maquina.agregarOperacion("stop");/*final*/
            }
break;
case 48:
//#line 248 "lisp.y"
{ 
              yyval = new ParserVal(maquina.agregarOperacion("nop"));
            }
break;
case 49:
//#line 251 "lisp.y"
{
              yyval = val_peek(0);
            }
break;
case 50:
//#line 254 "lisp.y"
{
              yyval = val_peek(2);
            }
break;
case 51:
//#line 259 "lisp.y"
{
              i = 0; auxiliar = new double[(int)val_peek(0).dval];
            }
break;
case 52:
//#line 264 "lisp.y"
{
              auxiliar[i] = val_peek(0).dval; i++;
            }
break;
case 53:
//#line 267 "lisp.y"
{
              auxiliar[i] = val_peek(0).dval; i++;
            }
break;
//#line 1000 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
