package Project1b;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;


  
public class Syntax{
    Lexemes syntLex;
    
    public int scopemax = 0;
    public String scopeList[];
   // private String scope[]= {"GLOBAL", "LOCAL", "BLOCK"};
 //   private int sc_id;  //0 is global, 1 is local, 2 is block
      
    public int countcheck = 0;  //main count of lexeme list
    public boolean prgflag = false;
    private int blockcount = 0;
    
    //Lexemes class has two arrays and a count that will be of use for this class
   //String tokens[];
   //String values[];
   //int lexcount;
   //ids[]
   
   //tree tools
   private int scopecount=0;
   private LinkedList<String> SymbolTable[];
   private LinkedList<String> tree[];
   int treeLvl=0;   //points at the level
    int LvlSize=0;  //array max count
    int actualSize=0;
  
   int id_count;
    int toi;

    private boolean scopeflg=false;
    private String tempID;
    private String tempVal;
    private String tempType;


    public void printTree() throws IOException{
       
        FileWriter parseTree = new FileWriter("Project1b/ParseTree.out");
      
        int linesize;
        int a = 0;
        int branch=0;

        if(prgflag==true){
            for(int i = 0; i<actualSize; i++){
                //System.out.print(tree[i]);
                linesize=tree[i].size();
                for(int f = 0; f<linesize; f++){
                  //  System.out.print("("+a+") "+tree[i].removeFirst()+" ("+i+")      ");
                //  a++;
                
                    if(tree[i].peekFirst()!="..."){
                        parseTree.write("("+a+") "+tree[i].removeFirst()+" ["+branch+"]\t\t\t\t");
                        a++;
                    }
                    else{
                        tree[i].removeFirst();
                        branch++;
                    }


                }
                //System.out.print("\n");
           
                parseTree.write("\n");
            }
            parseTree.close();
        }


    }

    public void printSymTab() throws IOException{
      //  Scanner input = new Scanner(System.in);
     //   System.out.println("Please write the directory of where you want to save the Symbol Table: ");
      //  String symbol=input.next();
     //   input.close();
        
        FileWriter symTable = new FileWriter("Project1b/SymbolTable.out");
        
        int tableSize;
        
        if(prgflag==true){
            for(int i = 0; i<scopecount; i++){
                //System.out.print(tree[i]);
                tableSize=SymbolTable[i].size();
                for(int f = 0; f<tableSize; f++){
            
                    symTable.write(SymbolTable[i].removeFirst()+"\n");
                        
                }
                symTable.write("------------------------------\n");
            }
            symTable.close();
        }


    }
   
    private boolean terminal(String tok, String val){
        if(syntLex.tokens[countcheck].equals("COMMENT")){
            countcheck++;
        }
        String x = syntLex.tokens[countcheck];
        String y = syntLex.values[countcheck];
        if(x.equals(tok)){
            if(y.equals(val)){
                startSymbol(val);

                treeLvl++;
                startSymbol("...");
                treeLvl--;

                countcheck++;
                return true;
            }
            
        }
        return false;
    }

    private void startSymbol(String nterminal){
        tree = rstree(tree, treeLvl);
        LvlSize=tree.length;
        if(tree[treeLvl]==null){
        
            tree[treeLvl]= new LinkedList<String>();
            tree[treeLvl].add(nterminal);

        }
        else{
             tree[treeLvl].add(nterminal);
             if(treeLvl>=actualSize){
                 actualSize=treeLvl+1;
             }
        }
        
    }

    private boolean literal(String tok){
        if (syntLex.tokens[countcheck]=="COMMENT"){
          //  countcheck++;
            return true;
        }
        else if(syntLex.tokens[countcheck]==tok){
            startSymbol(syntLex.values[countcheck]);
            countcheck++;
            return true;
        }
        return false;
    }
   
    public void startLex(Lexemes newLex){
        syntLex = newLex;
    }

    public String[] copyList(){
        int scopemax = syntLex.idcount;
        String tempList[] = new String[scopemax];

        return tempList;
    }

   
    public void startCheck() throws IOException{
        scopeList=copyList();
        

        while(countcheck<syntLex.lexcount) {
            if (program()==true){
                prgflag=true;
                System.out.println("Woo Success!");
            }
            else if(literal("COMMENT")){
               countcheck++;
            }
          
        }
        if (prgflag==false){
            System.out.println("Program Failed");

        }
           
        else{
            printTree();
            printSymTab();
        }

    }

    public LinkedList<String>[] rstree (LinkedList<String> list[], int counter){       //Function to resize arrays
            
        if (list==null){
           @SuppressWarnings("unchecked")           
            LinkedList<String>[] newList = new LinkedList[10];
            return newList; 

        }
        
        else if(list.length-counter<5){
            int x = list.length;
          @SuppressWarnings("unchecked")           
            LinkedList<String>[] newList = new LinkedList[x+10];
            for (int i=0; i<x; i++){
                newList[i]=list[i];
            }
           
            return newList; 
        } 
        return list;
    }

  
   
    public boolean program() throws IOException{
        startSymbol("<program>");
        treeLvl++; //fix the the treeLvl in terminals
        int toyscope = scopecount;

        SymbolTable=rstree(SymbolTable, scopecount);
       // @SuppressWarnings("unchecked")
        SymbolTable[toyscope]= new LinkedList<String>();
        SymbolTable[toyscope].add("SymbolTable <GLOBAL>\n");

        if(!(terminal("KEYWORD","program"))){
            //System.out.println("Missing KEYWORD: \"program\"");
            countcheck++;
            treeLvl--;
            return false;
        }
       // FileWriter SymbolTable = new FileWriter("/Users/Ivan/Documents/SDSU/CS 320/Project 1/project1/SymbolTable.out");


        if(id(toyscope)){
            startSymbol("<id>");
          
           // sc_id=0;
          //  scopeList[scopecount]=;
          //  printScp_SymTab(SymbolTable);

            if((terminal("KEYWORD","begin"))){
                
                if(pgmBody(toyscope)==true){
                    startSymbol("<pgmBody>");
                    if(((terminal("KEYWORD","end")))){
                 //   SymbolTable.close();
                   
                    startSymbol("...");
                    treeLvl--;
                    startSymbol("...");
                    return true;
                    }

                }
            }

        }

     //   SymbolTable.close();
        treeLvl--;
        return false;

    }

    public boolean id(int scope){
        treeLvl++;
        if (syntLex.tokens[countcheck].equals("ID")){
            startSymbol(syntLex.values[countcheck]);

            if(scopeflg==true){
                tempID=syntLex.values[countcheck];
                scopeflg=false;
            }

            treeLvl++;
            startSymbol("...");
            treeLvl--;

            countcheck++;
            startSymbol("...");
            treeLvl--;
            
            return true;

        }
        treeLvl--;
        return false;
    }

    private boolean pgmBody(int scope){
        treeLvl++;
        if(decl(scope)==true){
            startSymbol("<decl>");
             if(func_declarations(scope)==true){
                startSymbol("<func_declarations>");
                startSymbol("..."); 
                treeLvl--;
                
                return true;
             }
             System.out.println("Function declarations failed");
        }
        System.out.println("Program Body contain errors");
        treeLvl--;
        return false;

    }

    private boolean decl(int scope){
        treeLvl++;
        toi=countcheck;
        if (string_decl(scope)==true){
            startSymbol("<string_decl>");
            if (decl(scope)==true){
                startSymbol("<decl>");
                
                startSymbol("...");
                treeLvl--;
               
                return true;
            }
            System.out.println("decl failed");
            treeLvl--;
            return false;
        }
        else{
            countcheck=toi;
             if (var_decl(scope)==true){
                startSymbol("<var_decl>");

                if (decl(scope)==true){
                    startSymbol("<decl>");
                    startSymbol("...");
                    treeLvl--;
                   
                    return true;
                }
                System.out.println("decl failed");
                treeLvl--;
                return false;
            }
            
            countcheck=toi;

        }
        startSymbol("...");
        treeLvl--;
       
        return true;
    }

    private boolean string_decl(int scope){
        treeLvl++;
        toi=countcheck;
        exit:
        if (((terminal("KEYWORD","string")))){

            scopeflg=true;
            
            
            if(id(scope)){
                startSymbol("<id>");
                if (terminal("OPERAND",":=")){
                    
                    scopeflg=true;
                    if(str(scope)==true){
                        startSymbol("<str>");
                        if(terminal("OPERAND",";")){
                        
                        SymbolTable[scope].add("name: "+tempID+" - kind: variable - type: string - value: "+tempVal);

                        startSymbol("...");
                        treeLvl--;
                        return true;
                        }
                    }
                    System.out.println("Expected a STRINGLITERAL in the string declaration");
                    break exit;
                }
                System.out.println("Expected an OPERAND in the string declaration");
                break exit;


            }
            System.out.println("Invalid IDENTIFIER after the KEYWORD:\"string\"");
        }
        countcheck=toi;
        treeLvl--;
        return false;        
    }

    private boolean str(int scope){
        treeLvl++;
        if (syntLex.tokens[countcheck].equals("STRINGLITERAL")){
            startSymbol(syntLex.values[countcheck]);

            if(scopeflg==true){
                tempVal=syntLex.values[countcheck];
                scopeflg=false;
            }

            countcheck++;
          
            startSymbol("...");
            treeLvl--;
           
            return true;

        }
        treeLvl--;
        return false;

    }

    private boolean var_decl(int scope){
        treeLvl++;
        toi=countcheck;
        scopeflg=true;
        boolean tempflg=scopeflg;
        if (var_type(scope)==true){
            startSymbol("<var_type>");
            scopeflg=tempflg;
            if (id_list(scope)){
                startSymbol("<id_list>");
                if(terminal("OPERAND", ";")){
                    startSymbol("...");
                    treeLvl--;
                   
                    return true;
                }
            }
            System.out.println("var_decl failed");

        }
      
        countcheck=toi;
        treeLvl--;
        return false;
    }

    private boolean var_type(int scope){
        treeLvl++;

        toi=countcheck;
        if (terminal("KEYWORD","float")){
            startSymbol("...");
            treeLvl--;
           
          //  if(scopeflg==true){
            tempType="float";
            //scopeflg=false;
            //}
        
           
            return true;
        }

        else {
            countcheck=toi;
            if (terminal("KEYWORD","int")){
                startSymbol("...");
                treeLvl--;
                
              //  if(scopeflg==true){
                    tempType="int";
              //      scopeflg=false;
              //  }
                
               
            return true;
            }
        }

        countcheck=toi;
        treeLvl--;
        return false;
    }

    private boolean any_type(int scope){
        treeLvl++;
        toi=countcheck;
        if ((terminal("KEYWORD","void"))){
            startSymbol("...");
            treeLvl--;           
            
            return true;
        }
        else {
            countcheck=toi;
            if(var_type(scope)){
                startSymbol("<var_type>");

                startSymbol("...");                
                treeLvl--;
                
            return true;
            }
        }
        countcheck=toi;
        System.out.println("any_type failed");
        treeLvl--;
        return false;
    }

    private boolean id_list(int scope){
        boolean tempflg=scopeflg;
        treeLvl++;
        toi=countcheck;

        if(id(scope)){
            startSymbol("<id>");
            toi=countcheck;
            scopeflg=tempflg;


            if(id_tail(scope)){
                startSymbol("<id_tail>");
                startSymbol("...");
                treeLvl--;
               
                return true;
            }
            
        }

        countcheck=toi;
        System.out.println("id_list failed");
        treeLvl--;
        return false;
    }
    
    private boolean id_tail(int scope){
        boolean tempflg=scopeflg;
        treeLvl++;
        toi=countcheck;
        if ((terminal("OPERAND",","))){
            if(scopeflg==true){
                SymbolTable[scope].add("name: "+tempID+" - kind: variable - type: "+tempType);
            }
            toi=countcheck;

            if (id(scope)){
                startSymbol("<id>");
                
                toi=countcheck;

                scopeflg=tempflg;
                if (id_tail(scope)){
                    scopeflg=true;
                    startSymbol("<id_tail>");
                    scopeflg=tempflg;
                    
                    startSymbol("...");
                    treeLvl--;
                   // if(scopeflg==true){
                    //    SymbolTable[scope].add("name "+tempID+"- type: "+tempType);
                     //   scopeflg=false;
                   // }

                   
                    return true;
                }
                
            }
         
            

            countcheck=toi;
            System.out.println("id_tail failed");
            treeLvl--;
            return false;        
        }

        if(scopeflg==true){
            SymbolTable[scope].add("name: "+tempID+" - kind: variable - type: "+tempType);
            scopeflg=false;
        }
        countcheck=toi;
        startSymbol("...");
        treeLvl--;
       
        return true;
    }
    
    private boolean param_decl_list(int scope){
        treeLvl++;
        toi=countcheck;
        if(param_decl(scope)){
            startSymbol("<param_decl>");

            if(param_decl_tail(scope)){
                startSymbol("<param_decl_tail>");
                startSymbol("...");
                treeLvl--;
                   
                return true;
            }
            System.out.println("param_decl_list failed");
            treeLvl--;
            return false;
        }
        countcheck=toi;
        startSymbol("...");
        treeLvl--;
        
            return true;
    }

    private boolean param_decl(int scope){
        treeLvl++;
        if (var_type(scope)){
            startSymbol("<var_type>");
            if (id(scope)){
                startSymbol("<id>");

                startSymbol("...");

                treeLvl--;
               
               return true;
            }
            System.out.println("param_decl failed");
        }
        treeLvl--;
        return false;
    }
    
    private boolean param_decl_tail(int scope){
        treeLvl++;
       
        toi=countcheck;
        if((terminal("OPERAND",","))){
            toi=countcheck;
            if (param_decl(scope)){
                startSymbol("<param_decl>");
                toi=countcheck;
                if (param_decl_tail(scope)){
                    startSymbol("<param_decl_tail>");
                    startSymbol("...");
                    treeLvl--;
                   
            return true;
                }

            }
            countcheck=toi;
            System.out.println("param_decl_tail failed");
            treeLvl--;
            return false;
        }
        countcheck=toi;
        startSymbol("...");
        treeLvl--;        
        
            return true;

    }
   
    private boolean func_declarations(int scope){
        treeLvl++;
        toi=countcheck;
        if (func_decl(scope)){
            startSymbol("<func_decl>");
            if(func_declarations(scope)){
                startSymbol("<func_declarations>");
                startSymbol("...");
                treeLvl--;
               
                return true;
            }
            System.out.println("func_declarations failed");
            treeLvl--;
            return false;
        }
        countcheck=toi;
        startSymbol("...");
        treeLvl--;
       
            return true;
    }
    
    private boolean func_decl(int scope){
        treeLvl++;

        toi=countcheck;

        if((terminal("KEYWORD","function"))){
            
            toi=countcheck;
           
         

            if (any_type(scope)){

                startSymbol("<any_type>");
                toi=countcheck;
                scopecount++;
                scope=scopecount;

                
                SymbolTable=rstree(SymbolTable, scopecount);
               
                SymbolTable[scope]= new LinkedList<String>();
                
                SymbolTable[scope].addFirst("Symbol Table "+syntLex.values[countcheck]+"\n");
                
                if(id(scope)){
                    startSymbol("<id>"); 
                    SymbolTable[scope-1].add("name "+tempID+" - kind: function - type: "+tempType+" ");   
                    toi=countcheck;

                    if((terminal("OPERAND","("))){
                        toi=countcheck;
                        
                        if(param_decl_list(scope)){
                            startSymbol("<param_decl_list>");
                            toi=countcheck;
                            if((terminal("OPERAND",")"))){
                                toi=countcheck;
                                if((terminal("KEYWORD","begin"))){
                                    if(func_body(scope)){
                                        startSymbol("<func_body>");
                                        toi=countcheck;

                                        if((terminal("KEYWORD","end"))){
                                            
                                           
                                            startSymbol("...");
                                            treeLvl--;
                                           
                                            return true;
                                        }   
                                    }
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("func_decl failed");
        }
        countcheck=toi;
        treeLvl--;     
        return false;

    }
   
    private boolean func_body(int scope){
        treeLvl++;
        toi=countcheck;

        if (decl(scope)){
            startSymbol("<decl>");
            toi=countcheck;
            if (stmt_list(scope)){
                startSymbol("<stmt_list>");

                startSymbol("...");
                treeLvl--;
                
                return true;
            }
            System.out.println("func_body failed");
        }
        countcheck=toi;
        
        treeLvl--;
        return false;
    }
   
    private boolean stmt_list(int scope){
        treeLvl++;
        toi=countcheck;
        if(stmt(scope)){
            startSymbol("<stmt>");

            if(stmt_list(scope)){
                startSymbol("<stmt_list>");
                startSymbol("...");
                treeLvl--;
               
            return true;
            }
            System.out.println("stmt_list failed");
            treeLvl--;
            return false;
        }
        countcheck=toi;
        startSymbol("...");
        treeLvl--;
       
            return true;
    }
    
    private boolean stmt(int scope){
        treeLvl++;
        toi=countcheck;
        if(base_stmt(scope)){
            startSymbol("<base_stmt>");
            startSymbol("...");
            treeLvl--;
           
            return true;
        }
        else {
            countcheck=toi;
            if(if_stmt(scope)){
                startSymbol("<if_stmt>");

                startSymbol("...");
                treeLvl--;
                 
            return true;
            }
            else {
                countcheck=toi;
                if(do_while_stmt(scope)){
                    startSymbol("<do_while_stmt>");
                    startSymbol("...");
                    treeLvl--;
                
                    return true;
                }
            }
            
        }
        countcheck=toi;
        treeLvl--;
        return false;
    }
    
    private boolean base_stmt(int scope){
        treeLvl++;
        toi=countcheck;
        if(assign_stmt(scope)){
            startSymbol("<assign_stmt>");

            startSymbol("...");
            treeLvl--;
            
            return true;
        }

        else{
            countcheck=toi;
            if(read_stmt(scope)){
                startSymbol("<read_stmt>");
                startSymbol("...");
                treeLvl--;
               
            return true;
            }
                else{
                    countcheck=toi;
                    if(write_stmt(scope)){
                        startSymbol("<write_stmt>");
                        startSymbol("...");
                        treeLvl--;
                        
                        return true;
                    }
                    else{
                        countcheck=toi;
                        if(return_stmt(scope)){
                            startSymbol("<return_stmt>");
                            startSymbol("...");
                            treeLvl--;

                            return true;
                        }
                    }



                }

        }
        countcheck=toi;
  //      System.out.println("base_stmt failed");
        treeLvl--;
        return false;
    }
    
    private boolean assign_stmt(int scope){
        treeLvl++;
        toi=countcheck;
        if(assign_expr(scope)){
            startSymbol("<assign_expr>");

            toi=countcheck;
            if(terminal("OPERAND",";")){
                startSymbol("...");
                treeLvl--;
                
            return true;
            }
            System.out.println("assign_stmt failed");
        }
        countcheck=toi;
        treeLvl--;      
        return false;
    }
    
    private boolean assign_expr(int scope){
        treeLvl++;
        toi=countcheck;
        if (id(scope)){
            startSymbol("<id>");
            if(terminal("OPERAND",":=")){
                
                if(expr(scope)){
                    startSymbol("<expr>");
                    startSymbol("...");
                    treeLvl--;
                   
                    return true;
                }
            }
            System.out.println("assign_expr failed");
        }
        countcheck=toi;

        treeLvl--;
       
        return false;
    }

    private boolean read_stmt(int scope){
        treeLvl++;
        
        if((terminal("KEYWORD","read"))){
            
            if(terminal("OPERAND","(")){
                scopeflg=false;
                if (id_list(scope)){
                    startSymbol("<id_list>");

                    if((terminal("OPERAND",")"))){
                        if((terminal("OPERAND",";"))){
                            startSymbol("...");
                            treeLvl--;
                       
                            return true;
                        }
                    }
                }
            }
            System.out.println("read_stmt failed");
        }

        treeLvl--;
       
        return false;
    }

    private boolean write_stmt(int scope){
        treeLvl++;
        if((terminal("KEYWORD","write"))){
            
            if(terminal("OPERAND","(")){
                
                scopeflg=false;
                if (id_list(scope)){
                    startSymbol("<id_list>");

                    if((terminal("OPERAND",")"))){
                        
                        if((terminal("OPERAND",";"))){
                            startSymbol("...");
                            treeLvl--;
                           
                            return true;
                        }
                    }
                }
            }
            System.out.println("write_stmt failed");
        }
        treeLvl--;
        return false;
    }
   
    private boolean return_stmt(int scope){
        treeLvl++;
        toi=countcheck;
        if(terminal("KEYWORD","return")){
            if(expr(scope)){
                startSymbol("<expr");

                if((terminal("OPERAND",";"))){
                    startSymbol("...");
                    treeLvl--;
                   
                    return true;
                }
        
            }
            System.out.println("return_stmt failed");
        }
        toi=countcheck;
        treeLvl--;
        return false;
    }

    private boolean expr(int scope){
        treeLvl++;
        if(factor(scope)){
            startSymbol("<factor>");
            if(expr_tail(scope)){
                startSymbol("<expr_tail>");
                startSymbol("...");
                treeLvl--;
               
                return true;
            }
            System.out.println("expr failed");
        }
        System.out.println("expr failed");

       treeLvl--;
        return false;
    }
   
    private boolean expr_tail(int scope){
        treeLvl++;
        toi=countcheck;
        if(addop(scope)){
            startSymbol("<addop>");

            if (factor(scope)){
                startSymbol("<factor>");

                if (expr_tail(scope)){
                    startSymbol("<expr_tail>");
                    startSymbol("...");
                    treeLvl--;
                  
                    return true;
                }

                System.out.println("expr_tail failed");
            }

            treeLvl--;
            return false;
        }
        countcheck=toi;
        startSymbol("...");
        treeLvl--;
       
        return true;
    }
   
    private boolean factor(int scope){
        treeLvl++;
        if(postfix_expr(scope)){
            startSymbol("<postfix_expr>");
            if(factor_tail(scope)){
                startSymbol("<factor_tail>");
                startSymbol("...");
                treeLvl--;
                
            return true;
            }
           //System.out.println("factor failed");
        }
       treeLvl--;
        return false;
    }
   
    private boolean factor_tail(int scope){
        treeLvl++;
       toi=countcheck;
        if (mulop(scope)){
            startSymbol("<mulop>");
            if (postfix_expr(scope)){

                startSymbol("<postfix_expr>");



                if(factor_tail(scope)){
                    startSymbol("<factor_tail>");
                    startSymbol("...");
                    treeLvl--;
                   
                    return true;
                }
            }
            System.out.println("factor_tail failed");
            treeLvl--;
            return false;
        }
        countcheck=toi;
        startSymbol("...");
        treeLvl--;
        
        return true;
    }
   
    private boolean postfix_expr(int scope){
        treeLvl++;
       toi=countcheck;
        if(call_expr(scope)){
            startSymbol("<call_expr>");
            startSymbol("...");
            treeLvl--;
           
            return true;
        }
        else{ 
            countcheck=toi;
            if(primary(scope)){
                startSymbol("<primary>");
                startSymbol("...");
                treeLvl--;
          
                return true;
            }
           
        }
        treeLvl--;
        return false;

    }
    
    private boolean idandsom(String tok1, String comp1, String tok2, String comp2, String tok3, String comp3){
        if(tok1.equals(comp1)){
            if(tok2.equals(comp2)){
                if(tok3.equals(comp3)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean call_expr(int scope){
        treeLvl++;
        if(idandsom("ID",syntLex.tokens[countcheck],"OPERAND", syntLex.tokens[countcheck+1], "(", syntLex.values[countcheck+1] )){
            if(id(scope)){
                startSymbol("<id>");

                    if(terminal("OPERAND", "(")){

                        if (expr_list(scope)){
                            startSymbol("<expr_list>");

                            if(terminal("OPERAND", ")")){
                        
                                startSymbol("...");
                                treeLvl--;
                        
                                return true;
                            }
                            System.out.println("call_expr failed");
                        }   
                
                    }
                
            }
        }
        treeLvl--;
        return false;
    }

    private boolean expr_list(int scope){
        treeLvl++;
        if(expr(scope)){
            startSymbol("<expr>");
            if (expr_list_tail(scope)){
                
                startSymbol("<expr_list_tail>");
                
                startSymbol("...");
                treeLvl--;
               
                return true;
            }
            System.out.println("expr_list failed");
        }
        treeLvl--;
        return false;
    }

    private boolean expr_list_tail(int scope){
        treeLvl++;
       toi=countcheck;
        if(terminal("OPERAND", ",")){
            if(expr(scope)){
                startSymbol("<expr>");
                if (expr_list_tail(scope)){
                    startSymbol("<expr_list_tail>");
                   
                    startSymbol("...");
                    treeLvl--;
                   
                    return true;
                }

            }
            System.out.println("expr_list_tail failed");
            treeLvl--;
            return false;

        }
        countcheck=toi;
        startSymbol("...");
        treeLvl--;
       
            return true;

    }

    private boolean primary(int scope){
        treeLvl++;
        toi=countcheck;
        if (terminal("OPERAND", "(")){
            if (expr(scope)){
                startSymbol("<expr>");
                if (terminal("OPERAND", ")")){
                    startSymbol("...");
                    treeLvl--;
                    
                    return true;
                }
                System.out.println("primary failed");
            }
            
            treeLvl--;
            return false;
        }
        else {
            countcheck=toi;
            if(id(scope)){
                startSymbol("<id>");
               
                startSymbol("...");
                treeLvl--;
               
                return true;
            }
            else {
                countcheck=toi;
                if(literal("INTLITERAL")){
                    
                   // startSymbol(syntLex.values[countcheck]);
                    startSymbol("...");
                    treeLvl--;
                   
                    return true;
                }
                else{
                    countcheck=toi;
                    if(literal("FLOATLITERAL")){
                      //  startSymbol(syntLex.values[countcheck]);
                        startSymbol("...");
                        treeLvl--;
                       
                        return true;
                    }
                }
            }
           // System.out.println("primary failed");
        }
        countcheck=toi;
        treeLvl--;
        return false;
    }
    
    private boolean addop(int scope){
        treeLvl++;
       toi=countcheck;
        if (terminal("OPERAND","+")){
            startSymbol("...");
            treeLvl--;
           
            return true;
        }
        else{
            countcheck=toi;
             if (terminal("OPERAND","-")){
                startSymbol("...");
                treeLvl--;
           
                return true;
             }
            
        } 
        treeLvl--;    
        return false;
    }

    private boolean mulop(int scope){
        treeLvl++;
        toi=countcheck;
        if (terminal("OPERAND","*")){
           
            startSymbol("...");
            treeLvl--;
            return true;
        }
        else {
            countcheck=toi;
            if (terminal("OPERAND","/")){
                startSymbol("...");
                treeLvl--;
                return true;
            }
        }
        treeLvl--;
        return false;
    }

    private boolean if_stmt(int scope){
        treeLvl++;
        if(terminal("KEYWORD","if")){
            if(terminal("OPERAND","(")){
                if (cond(scope)){

                    startSymbol("<cond>");

                    if(terminal("OPERAND",")")){

                        if(decl(scope)){
                            startSymbol("decl");

                            if (stmt_list(scope)){
                                startSymbol("stmt_list");

                                if(else_part(scope)){
                                    startSymbol("else_part");
                                    
                                    if(terminal("KEYWORD","endif")){
                                        startSymbol("...");
                                        treeLvl--;
                                       
                                        return true;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        treeLvl--;
        return false;
    }

    
    private boolean else_part(int scope){
        treeLvl++;
      toi=countcheck;
        if(terminal("KEYWORD","elseif")){
            if(terminal("OPERAND","(")){
                toi=countcheck;
                if (cond(scope)){
                    startSymbol("cond");
                    toi=countcheck;
                    if(terminal("OPERAND",")")){
                        toi=countcheck;
                        if(decl(scope)){
                            startSymbol("decl");
                            toi=countcheck;
                            if (stmt_list(scope)){
                                startSymbol("stmt_list");
                                toi=countcheck;
                                if(else_part(scope)){
                                    startSymbol("else_part");
                                    toi=countcheck;
                                    startSymbol("...");
                                    treeLvl--;
                                 
                                    return true;
                                    
                                }
                            }

                        }
                    }
                }
            }
            treeLvl--;
            return false;
        }
        countcheck=toi;
        startSymbol("...");
        treeLvl--;
     
        return true;
    }

    private boolean cond(int scope){
        treeLvl++;
       toi=countcheck;
        if(expr(scope)){
            startSymbol("expr");
            if(op(scope)){
                startSymbol("op");
            
                if(expr(scope)){
                    startSymbol("expr");
                    startSymbol("...");
                    treeLvl--;
                   
                    return true;
                }
            }
            treeLvl--;
            return false;
        }
       
        else {
            countcheck=toi;
            if(terminal("KEYWORD", "true")){
                startSymbol("...");
                treeLvl--;
                
                return true;
            }
            else if(terminal("KEYWORD", "false")){
                startSymbol("...");
                treeLvl--;
               
                return true;
            }
        }
        treeLvl--;
        return false;
    }

    private boolean op(int scope){
        treeLvl++;
        if(terminal("OPERAND", "<<")){
            startSymbol("...");
            treeLvl--;
            
            return true;
        }
        else if(terminal("OPERAND", ">>")){
            startSymbol("...");
            treeLvl--;
           
            return true;
        }
        else if(terminal("OPERAND", "==")){
            startSymbol("...");
            treeLvl--;
            
            return true;
        }
        else if(terminal("OPERAND", "!=")){
            startSymbol("...");
            treeLvl--;
           
            return true;
        }
        else if(terminal("OPERAND", "<<=")){
            startSymbol("...");
            treeLvl--;
           
            return true;
        }
        else if(terminal("OPERAND", ">>=")){
            startSymbol("...");
            treeLvl--;
           
            return true;
        }
        System.out.println("op failed");
        treeLvl--;
        return false;
    }

    private boolean do_while_stmt(int scope){
        treeLvl++;
        if(terminal("KEYWORD", "do")){

            scopecount++;
            blockcount++;
            scope=scopecount;
            SymbolTable=rstree(SymbolTable, scopecount);
            SymbolTable[scope]= new LinkedList<String>();
            SymbolTable[scope].addFirst("Symbol Table Block"+blockcount+"\n");
            

            if(decl(scope)){
                startSymbol("<decl>");
                if (stmt_list(scope)){
                    startSymbol("<stmt_list>");
                    if(terminal("KEYWORD", "while")){
                        if(terminal("OPERAND", "(")){
                            if (cond(scope)){
                                startSymbol("<cond>");
                                if(terminal("OPERAND", ")")){
                                    if(terminal("OPERAND", ";")){
                                        startSymbol("...");
                                        treeLvl--;
                                       
                                        return true;
                                    }
                                }
                            }
                        }

                    }
                    
                }

            }
            System.out.println("do_while_stmt failed");
        }
        treeLvl--;
        return false;
    }  
    
    /*   private void printScp_SymTab(FileWriter ST) throws IOException{
        ST.write("Symbol Table");
        switch (sc_id){
            case 0:
            ST.write("GLOBAL\n");
            break;
            case 1:
            ST.write(syntLex.addSymTab()+"\n");
            break;
            case 2:
            ST.write("Block"+block+"\n");
            break;
            
        }

    }*/

   
    }