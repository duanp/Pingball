package pingball;



import java.util.regex.*;

// actual parser is in package boardFileParsing
public class Parser {


    //to replace trailing whitespaces in a board file: 
    public static String replaceWhitespace(String string){
        String newString =  string.replaceAll("\\s+", " ").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
        return string; 
    }


    public static String matchCommentLine(String stringComment){
        Matcher m = Pattern.compile("#([a-z]+\\.)+[a-z]+\\n").matcher(stringComment);
        if (m.matches()) {
            return stringComment; 
            // then string is a url
        }
        return stringComment; 
    }

    public static String matchGadgetDefinition(String stringGadgetDefinition){
        Matcher m = Pattern.compile("#([a-z]+\\.)+[a-z]+\\n").matcher(stringGadgetDefinition);
        if (m.matches()) {
            return stringGadgetDefinition; 
        }
        return stringGadgetDefinition;
    }
}






