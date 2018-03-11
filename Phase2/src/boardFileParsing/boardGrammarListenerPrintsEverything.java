package boardFileParsing;

import boardFileParsing.boardGrammarParser.ActionContext;
import boardFileParsing.boardGrammarParser.NameContext;

class boardGrammarListenerPrintsEverything  extends boardGrammarBaseListener {
        public void enterRoot(ActionContext ctx) {
            System.err.println("entering root");
        }
        public void exitRoot(ActionContext ctx) {
            System.err.println("exiting root");
        }

        public void enterNormal(NameContext ctx) {
            System.err.println("entering normal");
        }
        public void exitNormal(NameContext ctx) {
            System.err.println("exiting normal");
        }


    }


