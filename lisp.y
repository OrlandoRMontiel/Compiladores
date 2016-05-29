%{
  import java.lang.Math;
  import java.io.*;
  import java.util.StringTokenizer;
%}

%token SETF
%token IF
%token ELSE
%token WHILE
%token FOR
%token COMP
%token DIFERENTES
%token MAY
%token MEN
%token MAYI
%token MENI
%token FNCT
%token NUMBER
%token VAR
%token AND
%token OR
%token FUNC
%token RETURN
%token PARAMETRO
%token PROC

%right SETF
%left '+' '-'
%right '*'
%left '.'
%left ';'
%left COMP
%left DIFERENTES
%left MAY
%left MAYI
%left MEN
%left MENI
%left '!'
%left AND
%left OR
%right RETURN

%%
  list:
    | list'\n'
    | list linea '\n'
    ;

  linea: '(' exp ')'{$$ = $2;}
    |stmt  {$$ = $1;}
    |linea '(' exp ')'{$$ = $1;}
    |linea stmt {$$ = $1;}
    ;

  exp:  VAR {
              $$ = new ParserVal(maquina.agregarOperacion("varPush_Eval"));
              maquina.agregar($1.sval);
            }
    | NUMBER {
              $$ = new ParserVal(maquina.agregarOperacion("constPush"));
              maquina.agregar($1.dval);
            }
    | SETF VAR '{' lista '}' {
              vAux = new Vector(auxiliar);
              $$ = new ParserVal(maquina.agregarOperacion("constPush"));
              maquina.agregar(vAux);
              maquina.agregarOperacion("varPush");
              maquina.agregar($2.sval);
              maquina.agregarOperacion("asignar");
              maquina.agregarOperacion("varPush_Eval");
              maquina.agregar($2.sval);
            }
    | SETF VAR exp {
              $$ = new ParserVal($3.ival);
              maquina.agregarOperacion("varPush");
              maquina.agregar($2.sval);
              maquina.agregarOperacion("asignar");
              maquina.agregarOperacion("varPush_Eval");
              maquina.agregar($2.sval);
            }
    | '*' exp exp {
              $$ = new ParserVal($2.ival);
              maquina.agregarOperacion("productoCruz");
            }
    | '.' exp exp {
              $$ = new ParserVal($2.ival);
              maquina.agregarOperacion("productoPunto");
            }
    | '+' exp exp {
              $$ = new ParserVal($2.ival);
              maquina.agregarOperacion("sumar");
            }
    | '-' exp exp {
              $$ = new ParserVal($2.ival);
              maquina.agregarOperacion("restar");
            }
    |'(' exp ')' {
              $$ = new ParserVal($2.ival);
            }
    | COMP exp exp {
              maquina.agregarOperacion("comparar");
              $$ = $2;
            }
    | DIFERENTES exp exp {
              maquina.agregarOperacion("compararNot");
              $$ = $2;
            }
    | MEN exp exp {
              maquina.agregarOperacion("menor");
              $$ = $2;
            }
    | MENI exp exp {
              maquina.agregarOperacion("menorIgual");
              $$ = $2;
            }
    | MAY exp exp {
              maquina.agregarOperacion("mayor");
              $$ = $2;
            }
    | MAYI exp exp {
              maquina.agregarOperacion("mayorIgual");
              $$ = $2;
            }
    | AND exp exp {
              maquina.agregarOperacion("and");
              $$ = $2;
            }
    | OR exp exp {
              maquina.agregarOperacion("or");
              $$ = $2;
            }
    | '!' exp {
              maquina.agregarOperacion("negar");
              $$ = $2;
            }
    | '|' exp '|' {
              maquina.agregarOperacion("norma");
              $$ = $2;
            }
    | FNCT '[' exp ']' {
              $$ = new ParserVal($3.ival);
              maquina.agregar((Funcion)($1.obj));
            }
    | RETURN exp { 
              $$ = $2; maquina.agregarOperacion("_return"); 
            }

    | PARAMETRO { 
              $$ = new ParserVal(maquina.agregarOperacion("push_parametro")); 
              maquina.agregar((int)$1.ival); 
            }

    |nombreProc '(' arglist ')' { 
              $$ = new ParserVal(maquina.agregarOperacionEn("invocar",($1.ival))); 
              maquina.agregar(null); 
            } //instrucciones tiene la estructura necesaria para la lista de argumentos
    ;

  arglist:
    |exp {
              $$ = $1; 
              maquina.agregar("Limite");
            }
    |arglist ',' exp {
              $$ = $1; 
              maquina.agregar("Limite");
            }
    ;

  nop: /*nada*/{
              $$ = new ParserVal(maquina.agregarOperacion("nop"));
            }
    ;

  stmt:if_ '(' exp stop_ ')' '{' linea stop_ '}' ELSE '{' linea stop_'}' {
              $$ = $1;
              maquina.agregar($7.ival, $1.ival + 1);
              maquina.agregar($12.ival, $1.ival + 2);
              maquina.agregar(maquina.numeroDeElementos() - 1, $1.ival + 3);
            }
    | if_ '(' exp stop_ ')' '{' linea stop_ '}' nop stop_{
              $$ = $1;
              maquina.agregar($7.ival, $1.ival + 1);
              maquina.agregar($10.ival, $1.ival + 2);
              maquina.agregar(maquina.numeroDeElementos() - 1, $1.ival + 3);
            }
    | while_ '(' exp stop_ ')' '{' linea stop_ '}' stop_{
              $$ = $1;
              maquina.agregar($7.ival, $1.ival + 1);
              maquina.agregar($10.ival, $1.ival + 2);
            }
    | for_ '(' instrucciones stop_ ';' exp stop_ ';' instrucciones stop_ ')' '{' linea stop_ '}' stop_{
              $$ = $1;
              maquina.agregar($6.ival, $1.ival + 1);
              maquina.agregar($9.ival, $1.ival + 2);
              maquina.agregar($13.ival, $1.ival + 3);
              maquina.agregar($16.ival, $1.ival + 4);
            }
    | funcion nombreProc '(' ')' '{' linea null_ '}'
    | procedimiento nombreProc '(' ')' '{' linea null_ '}'
    ;

  procedimiento: PROC { 
              maquina.agregarOperacion("declaracion"); 
            }
    ;

  funcion: FUNC { 
              maquina.agregarOperacion("declaracion"); 
            }
    ;

  nombreProc: VAR {
              $$ = new ParserVal(maquina.agregar($1.sval));
            }
    ;

  null_: {
              maquina.agregar(null);
            }
    ;

  stop_: {
              $$ = new ParserVal(maquina.agregarOperacion("stop"));
            } 
    ;

  if_: IF {
              $$ = new ParserVal(maquina.agregarOperacion("_if_then_else"));
              maquina.agregarOperacion("stop");//then
              maquina.agregarOperacion("stop");//else
              maquina.agregarOperacion("stop");//siguiente comando
            }
    ;

  while_: WHILE {
              $$ = new ParserVal(maquina.agregarOperacion("_while"));
              maquina.agregarOperacion("stop");//cuerpo
              maquina.agregarOperacion("stop");//final
            }
    ;

  for_ : FOR {
              $$ = new ParserVal(maquina.agregarOperacion("_for"));
              maquina.agregarOperacion("stop");//condicion
              maquina.agregarOperacion("stop");//instrucción final
              maquina.agregarOperacion("stop");//cuerpo
              maquina.agregarOperacion("stop");//final
            }
    ;

  instrucciones: { 
              $$ = new ParserVal(maquina.agregarOperacion("nop"));
            }
    |exp {
              $$ = $1;
            }
    |instrucciones ',' exp {
              $$ = $1;
            }
    ;

  lista: NUMBER {
              auxiliar = new double[0]; auxiliar = Operacion.insertar(auxiliar,$1.dval);
            }
    | lista ',' NUMBER {
              auxiliar = Operacion.insertar(auxiliar,$3.dval);
            }
    ;
%%

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
        if(s.equals("setf")){
          return SETF;
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