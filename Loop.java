import java.util.ArrayList;

/**
 * Created by amin on 2/16/17.
 */
class Loop {
    int startAddr;//Address of the first instruction in while block
    int whileAddr;//Address for start of evaluation the while condition
    int endAddr;//Address of the first instruction after the while
    ArrayList<Integer> breakAddrs;
    ArrayList<Integer> continueAddrs;
    Variable conditionVar;
    int jzAddr;
    int jmpAddr;

    public Loop() {
        breakAddrs = new ArrayList<>();
        continueAddrs = new ArrayList<>();
    }
}
