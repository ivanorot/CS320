package Project1b;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


class mainprogram{
    public static void main(String[] args) throws IOException {
        System.out.println("Ivan Orozco - 822171656");
        Lexemes checkLex = new Lexemes();
        Syntax hola=new Syntax();

        Scanner input = new Scanner(System.in);
        //Reading the file
        System.out.println("Please write the directory of the input file: ");
        String inputtoi=input.next();
        input.close();
        File toyprog = new File(inputtoi);
        
        Scanner lineScan = new Scanner(toyprog);

        while(lineScan.hasNextLine()){
            checkLex.readLine(lineScan.nextLine());

        }
      
        lineScan.close();
        hola.startLex(checkLex);
        hola.startCheck();
        

        
        checkLex.printFile();
        
    }

}
