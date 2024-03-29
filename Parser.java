import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

public class Parser {
    Scanner scanner;
    static CodeGenerator cg;
    PTBlock[][] parseTable;
    Stack<Integer> parseStack = new Stack<Integer>();
    String[] symbols;
    Token currentToken;

    public Parser(String inputFile, String[] symbols, PTBlock[][] parseTable) throws Exception {
        try {
            this.parseTable = parseTable;
            this.symbols = symbols;

            File f = new File(inputFile);
            if (!f.exists())
                throw new Exception("File does not exist: " + f);
            if (!f.isFile())
                throw new Exception("Should not be a directory: " + f);
            if (!f.canRead())
                throw new Exception("Can not read input file: " + f);
            FileReader reader = new FileReader(f);
            scanner = new Scanner(reader);
            cg = new CodeGenerator(scanner);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int LineNumber() {
        return scanner.lineNumber(); // Or any other name you used in your Scanner
    }

    public void Parse() throws Exception {
        try {
            int tokenId = nextTokenID();
            int curNode = 0;
            boolean notAccepted = true;
            while (notAccepted) {
                String token = symbols[tokenId];
                PTBlock ptb = parseTable[curNode][tokenId];
                switch (ptb.getAct()) {
                    case PTBlock.ActionType.Error: {
                        throw new Exception(String.format("Compile Error (" + token + ") at line " + scanner.lineNumber() + " @ " + curNode));
                    }
                    case PTBlock.ActionType.Shift: {
                        cg.Generate(ptb.getSem(), currentToken);
                        tokenId = nextTokenID();
                        curNode = ptb.getIndex();
                    }
                    break;

                    case PTBlock.ActionType.PushGoto: {
                        parseStack.push(curNode);
                        curNode = ptb.getIndex();
                    }
                    break;

                    case PTBlock.ActionType.Reduce: {
                        if (parseStack.size() == 0) {
                            throw new Exception(String.format("Compile Error (" + token + ") at line " + scanner.lineNumber() + " @ " + curNode));
                        }

                        curNode = parseStack.pop();
                        ptb = parseTable[curNode][ptb.getIndex()];
                        cg.Generate(ptb.getSem(), currentToken);
                        curNode = ptb.getIndex();
                    }
                    break;

                    case PTBlock.ActionType.Accept: {
                        notAccepted = false;
                    }
                    break;

                }
            }
            cg.FinishCode();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    int nextTokenID() throws Exception {
        try {
            currentToken = scanner.NextToken();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        int i;
        for (i = 0; i < symbols.length; i++) {
            if (symbols[i].equals(currentToken.parser_token))
                return i;
        }
        throw new Exception("Undefined token: " + currentToken.parser_token);
    }

    public void WriteOutput(String outputFile) // You can change this function, if you think it is not comfortable
    {
        cg.WriteOutput(outputFile);
    }
}


