import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RList;
import org.rosuda.JRI.RVector;
import org.rosuda.JRI.RMainLoopCallbacks;

public class UltimateSolver {
    private int selection;
    private String objFxn;
    private String constraints;

    public UltimateSolver(int selection, String fxn, String con) {
        this.selection = selection;
        this.objFxn = fxn;
        this.constraints = con;
        solve();
    }

    public void solve() {
        System.out.println(this.selection + "\nOBJECTIVE FUNCTION:\n" + this.objFxn + "\nCONSTRAINTS:\n" + this.constraints);        
    }
}