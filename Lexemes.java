package Project1b;
import java.io.FileWriter;
import java.io.IOException;


public class Lexemes{
    public Lexemes(){};
    public int idcount=0;
    private int idMax=0;
    private int tokMax=0;
    public String tokens[];
    public String values[];
    public int lexcount=0;
    public String ids[];
    boolean commentFlag=false;
    private boolean errorcheck = true;
    private int idfloor=0;
   // private List<String> tokens 

   public String addSymTab(){
        String tempStr = ids[idfloor];
        idfloor++;
        return tempStr;

   }

    
    public String[] resize (String List[], int size){       //Function to resize arrays
        String newList[] = new String[size + 10];
        for (int i=0; i<size-10; i++){
            newList[i]=List[i];
        }
        return newList;  
    }


    //As the title suggest, it prints the token and its values
    public void printFile() throws IOException{

     
        FileWriter lexerout = new FileWriter("Project1b/lexer.out");
     

        for (int i=0; i<lexcount;i++){
        lexerout.write("Token Type: "+ tokens[i]+ "\n");
        lexerout.write("Value: "+ values[i] + "\n");
        }
        if(errorcheck == true){
            System.out.println("Success!");
        }
        lexerout.close();

    }
    
    //serves as the main method to get the information about the toy program
    //It works reading only a line per call
    public void readLine(String line){
      fullline:
        for (int i=0; i<line.length(); i++){
            if ((tokMax-lexcount)<=4){
                tokMax=tokMax+10;
                tokens=resize(tokens, tokMax);
                values=resize(values,tokMax);
           
            }
            String tempWord = new String();
            loop:
            while (line.charAt(i)!=' '&&line.charAt(i)!='\t'||commentFlag==true){
                tempWord+=line.charAt(i);
                i++;
                if (i==line.length()){
                    break loop;
                }

            }
         if(tempWord.length()!=0){
              
            if(commentFlag==true){
                tokens[lexcount]="COMMENT";
                values[lexcount]=tempWord;
                lexcount++;
                break fullline;
            }
            if(validLex(tempWord)==false){
                if(checkStringLit(tempWord)){
                   int x =i-tempWord.length(); 
                   int a = lenghtStringLit(line.substring(x,line.length()));
                   
                   tokens[lexcount]="STRINGLITERAL";
                   values[lexcount]=line.substring(x,x+a+1);
                   lexcount++;
                   
                   i=x+a; 
                }
                else if(nospacecheck(tempWord)==false){
                System.out.println("\""+ tempWord +"\""+ " is not a valid token");
                errorcheck = false;
                }
            }
        }
    }
        commentFlag = false;
        
    }


    //Intended for future use
   /* public boolean idType(String keyWd){
        switch(keyWd){
            case "program":
            case "int":
            case "void":
            case "string":
            case "float":
            return true;
        }
        return false;
    }*/

    //call different function to check what type of token is the string
    public boolean validLex(String word){      
        if (keywords(word)==true){
           return true;
       }
       else if(id(word)==true){
           return true;
       }
       else if(operands(word)==true){
           return true;
       }
       else if(checkInt(word)==true){
            return true;
       }
       else if(checkFloat(word)==true){
            return true;
       }
       else if(checkComment(word)==true){
            return true;
       }

       return false;
    }

    //function for last resort, when all the checks have failed, if it is still a valid
    //input, it probably stuck with other lexemes
    public boolean nospacecheck(String stuck2gether){
        
        for (int i=0; i<stuck2gether.length(); i++){
            String split= new String();
            String tempOperand = new String();
           
            while((i<stuck2gether.length()) &&(Character.isDigit(stuck2gether.charAt(i))==true||Character.isLetter(stuck2gether.charAt(i))==true)){
                split += stuck2gether.charAt(i);
                i++;
                
            }
            
            if(split.length()>0){
            validLex(split);
            }
            if (i<stuck2gether.length()){
                tempOperand += stuck2gether.charAt(i);                
                if (operands(tempOperand)==false){
                    return false;
                }
            }
        }
        return true;
    }

    //checks and adds if the token is a keyword
    public boolean keywords(String word){
        switch(word){
            case "program":
            case "begin":
            case "end":
            case "function":
            case "read":
            case "write":
            case "if":
            case "elseif":
            case "endif":
            case "do":
            case "while":
            case "return":
            case "int":
            case "void":
            case "string":
            case "float":
            case "true":
            case "false":
            {
            tokens[lexcount]="KEYWORD";
            values[lexcount]=word;
            lexcount++;
            return true;
            }
        }
        return false;
    }

    //checks and adds if the token is an operand
    public boolean operands(String word){
        switch(word){
            case ":=":
            case "+":
            case "-":
            case "*":
            case "/":
            case "==":
            case "!=":
            case "<<":
            case ">>":
            case "(":
            case ")":
            case ";":
            case ",":
            case "<<=":
            case "=>>":
            {
                tokens[lexcount]="OPERAND";
                values[lexcount]=word;
                lexcount++;
                return true;
            }
        }
        return false;
    }
    

    public boolean id(String ident){           // saves id names
        if (Character.isLetter(ident.charAt(0))){
            for (int i=1; i<ident.length();i++){
                if ((Character.isLetter(ident.charAt(i))==false)&&(Character.isDigit(ident.charAt(i))==false)){
                    return false;
                }
            }
            tokens[lexcount]="ID";
            values[lexcount]=ident;
            lexcount++;

            if ((idMax-idcount)<=4){
                idMax=idMax+10;
                ids=resize(ids, idMax);          
            }

            ids[idcount]=ident;
            idcount++;

            return true;
        }
        return false;
        
    }

    //checks for valid float value
    public boolean checkFloat(String tempFloat){
        int pointct=0;
        for (int i=0; i<tempFloat.length();i++){
            if (pointct>1){
                return false;
            }
            if (Character.isDigit(tempFloat.charAt(i))==false){
                if (tempFloat.charAt(i)== '.'){
                    pointct++;
                }
                return false;
            }

        }
        tokens[lexcount]="FLOATLITERAL";
        values[lexcount]=tempFloat;
        lexcount++;
        return true;
    }
    //checks for valid Int value
    public boolean checkInt(String tempInt){
        for (int i=0; i<tempInt.length();i++){
            if (Character.isDigit(tempInt.charAt(i))==false){
                return false;
            }
        }
        tokens[lexcount]="INTLITERAL";
        values[lexcount]=tempInt;
        lexcount++;
        return true;
    }

    //checks if it starts with a quotation marks
    public boolean checkStringLit(String tempText){
        if (tempText.charAt(0)== '"')
        return true;
        else {
            return false;
        }
    }
    

    public int lenghtStringLit(String text){             //Returns the lenght of the literal string
        if (text.charAt(0)== '"'){                      //if is not valid, returns 0
            for(int i=1; i<text.length();i++){
                if (text.charAt(i)=='"'){
                    return i;
                }
            }
        }
        return 0;
    }

    //checks for initial hints for a comment
    public boolean checkComment(String comm){
        if ((comm.charAt(0))=='#'&&(comm.charAt(1))=='#'){
            commentFlag=true;
            return true;
        }
        return false;
    }

      
      

}

