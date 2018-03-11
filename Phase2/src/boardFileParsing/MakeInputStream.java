package boardFileParsing;
import pingball.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;


import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
/**
 * 
 * @author kellypet
 * This class takes a board file and uses the ANTLR lexer and parsers to create an ANTLR listener to walk the parse tree. 
 * The parse tree will then format the board file input into a board. The output of this class can be used by a client. 
 */
public class MakeInputStream {

    /** 
     * 
     * @param file
     *          a board input file specifying the gadgets and objects to put on a board. 
     * @return a Board object corresponding to the params specified in file. 
     * @throws IOException
     */
    public static Board parseFile(File file) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine(); 
        String boardFile = "";
        try {
                while (line !=null){
                    boardFile += line + "\n"; 
                    line = reader.readLine(); //go to next line
                }
                reader.close(); 

        }catch (IOException e) {
                e.printStackTrace();
                throw new FileNotFoundException(e.getMessage()); //throw error if no file found
            }
                ANTLRInputStream stream =new  ANTLRInputStream(boardFile); 

                boardGrammarLexer lexer = new boardGrammarLexer(stream);
                CommonTokenStream tokens = new CommonTokenStream(lexer); //stream of tokens we can feed to parser 
                boardGrammarParser parser = new boardGrammarParser(tokens); //feed tokens to parser 
                
                //produce a parse tree
                ParseTree tree = parser.root();
               
                ParseTreeWalker walker = new ParseTreeWalker();
                boardFileListenerDocumentCreator listener = new boardFileListenerDocumentCreator();
                walker.walk(listener, tree);

                //print out stream so we can debug
                //System.err.println(tree.toStringTree(parser)); uncomment this when you want to debug output
                return listener.makeBoard(); 

          
        }
  


}



