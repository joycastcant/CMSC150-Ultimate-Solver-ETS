import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RList;
import org.rosuda.JRI.RVector;
import org.rosuda.JRI.RMainLoopCallbacks;

import java.util.ArrayList;
import java.util.HashMap;

public class UltimateSolver {
    protected final static Rengine ENGINE = new Rengine(new String[] { "--no-save" }, false, null);
    private int selection;
    private String[] objFxn;
    private ArrayList<String> constraints;
    private ArrayList<Tableau> tabList;
    // private ArrayList<Tableau> tableau;

    public UltimateSolver(int selection, String fxn, String con) {
        this.tabList = new ArrayList<Tableau>();
        this.selection = selection;
        String f = fxn.split("=")[1];   //objective function parsing
        this.objFxn = f.split("\\+");
        this.constraints = new ArrayList<String>();

        String[] conArray = con.split("\n");
        for(int i = 0; i < conArray.length; i++) {  //constraints parsing
            if(!conArray[i].contains("<=") && !conArray[i].contains(">=") && conArray[i].contains("=")) {
                String rep = conArray[i].replace('=', 'S');
                constraints.add(rep);
            }

            constraints.add(conArray[i]);
        }
        
        solve(this.objFxn.length, constraints.size(), f);   //actual solving
    }

    public ArrayList<Tableau> getTabList() {
        return this.tabList;
    }

    private void solve(int vars, int nrow, String f) {
        int ncol = vars + nrow + 2;
        String objSign = "";
        String[] variables = new String[vars];
        String[][] table = new String[nrow + 2][ncol + 1];

        if(this.selection == SolverPanel.MAXIMIZE) {    //if maximize was chosen
            //objective function parsing
            for(int k = 0; k < vars; k++) {
                String[] pair = (this.objFxn[k]).split("\\*");
                variables[k] = pair[1];
            }
    
            //table preparation
            for(int j = 0; j < (ncol + 1); j++) {
                if (j == 0) {
                    table[0][j] ="Var";
                } else {
                    if (j <= vars)
                        table[0][j] = variables[j - 1]; //actual vars
                    else if(j == (ncol - 1))
                        table[0][j] = objSign + "Z";
                    else if(j == ncol)
                        table[0][j] ="Sol'n";
                    else
                        table[0][j] ="S"+ (j - vars); //slack
                }
            }
            
            for(int i = 1; i < (nrow + 2); i++) {
                if(i == (nrow + 1)) {
                    table[i][0] = objSign + "Z";
                    this.addConstraint(table, i, f, ncol);
                } else {
                    table[i][0] ="S"+ i;
                    this.addConstraint(table, i, this.constraints.get(i - 1), ncol);
                }
            }
        } else {    //minimize is chosen
            objSign = "-";
            ncol = vars + 2;
            nrow = this.constraints.size() + 2;
            table = new String[nrow][ncol];

            //objective function parsing
            for(int k = 0; k < vars; k++) {
                String[] pair = (this.objFxn[k]).split("\\*");
                variables[k] = pair[1];
            }
            
            //table preparations
            for(int j = 0; j < (ncol + 1); j++) {
                if (j == 0) {
                    table[0][j] ="Var";
                } else {
                    if (j <= vars){
                        table[0][j] = variables[j - 1]; //actual vars
                    }
                    else if(j == (ncol - 1)){
                        table[0][j] ="Sol'n";
                    }
                }
            }
            
            for(int i = 1; i < nrow; i++) {
                if(i == (nrow - 1)) {
                    table[i][0] = "Z";
                    this.addVariables(table, i, f, ncol);
                } else {
                    this.addVariables(table, i, this.constraints.get(i - 1), ncol);
                }
            }

            //transpose table
            String[][] tab = transpose(table);
    
            int rrow = tab.length;
            int ccol = tab[0].length;

            for(int c = 1; c < ccol; c++) {
                tab[0][c] = "x" + c;
            }

            String[][] tablee = new String[rrow][ccol + vars];

            //solve like maximization
            for(int x = 0; x < rrow; x++) {
                for(int y = 0; y < (ccol - 1); y++) {
                    if(x == (rrow - 1)) {
                        if(!tab[x][y].contains("-"))
                            tablee[x][y] = "-" + tab[x][y];
                        else {
                            float b = new Float(tab[x][y]);
                            b = b * -1;
                            tablee[x][y] = String.valueOf(b);
                        }
                    }
                    else tablee[x][y] = tab[x][y];
                }
            }

            for(int i = 1; i < rrow; i++) {//<= rrow; i++) {
                if(i == (rrow - 1))
                    tablee[i][0] = "Z";
                else
                    tablee[i][0] = "S" + i;
            }

            int cons = this.constraints.size();
            
            for(int i = 1; i < rrow; i++) {
                for(int j = (i + cons); j <= (ccol + cons); j++) {
                    tablee[0][j] = "S" + (j - cons);
                    tablee[i][j] = "1";
                    break;
                }

                tablee[i][ccol + vars - 1] = tab[i][ccol - 1];
            }
            tablee[0][ccol + vars - 1] = "Sol'n";

            for(int i = 1; i < rrow; i++) {
                for(int j = 1; j < (ccol + vars); j++) {
                    if(tablee[i][j] == null)
                        tablee[i][j] = "0";
                }
            }

            table = tablee;
        }

        this.tabList.add(new Tableau(table));
        this.tabList.add(new Tableau(table));
        
        while(!isDone(this.tabList.get(this.tabList.size() - 1))) { //do gauss jordan while not done
            int curr = this.tabList.size() - 1;

            Tableau currTab = this.tabList.get(curr);
            int pivotColumn = currTab.getPivotColumn();

            currTab.computeTestRatio(pivotColumn);
            int pEX = currTab.getPivotElementX();
            int pEY = pivotColumn;
            float pe = currTab.getPE();

            this.tabList.add(gaussJordan(currTab, pEX, pEY, pe));
        }

        //adjust test ratio (move one iteration up)
        for(int p = 0; p < (this.tabList.size() - 1); p++) {
            Tableau currT = this.tabList.get(p);
            String[][] tb = currT.getTable();
            for(int x = 0; x < currT.getRow(); x++) {
                for(int y = 0; y < currT.getCol(); y++) {
                    if(y == currT.getCol() - 1) {
                        if(p == this.tabList.size() - 1) {
                            if(x == 0) tb[0][y] = "TR";
                            else tb[x][y] = " ";
                        } else {
                            String[][] next = this.tabList.get(p + 1).getTable();
                            if(x == 0) tb[0][y] = "TR";
                            else tb[x][y] = next[x][y];
                        }
                    }
                }
            }
            if (p > 0)
                currT.setBasicSolution();
        }
        
        printValue();
    }

    private String[][] transpose(String[][] tab) {  //use R's transpose
        String matrix = "c(";
        
        int rrow = tab.length;
        int ccol = tab[0].length;
        String[] rownames = new String[ccol];
        String[] colnames = new String[rrow];

        for(int y = 0; y < ccol; y++) {
            for(int x = 0; x < rrow; x++) {
                if(x == 0)
                    rownames[y] = tab[x][y];
                if(y == 0)
                    colnames[x] = tab[x][y];

                if(x != 0 && y != 0) {
                    if(x == (rrow - 1) && y == (ccol - 1))
                        matrix = matrix + tab[x][y] + ")";
                    else
                        matrix = matrix + tab[x][y] + ",";
                }
            }
        }

        ENGINE.eval("mat <- matrix(" + matrix + ", nrow=" + (rrow - 1) + ", ncol=" + (ccol - 1) + ")");
        ENGINE.eval("nmat <- t(mat)");

        double[][] transposed = ENGINE.eval("nmat").asDoubleMatrix();
        
        rrow = transposed.length + 1;
        ccol = transposed[0].length + 1;

        String[][] fin = new String[rrow][ccol];
        for(int x = 0; x < rrow; x++) {
            for(int y = 0; y < ccol; y++) {
                if(x == 0)
                    fin[x][y] = colnames[y];
                if(y == 0)
                    fin[x][y] = rownames[x];

                if(x != 0 && y != 0) {
                    fin[x][y] = String.valueOf(transposed[x - 1][y - 1]);
                }
            }
        }

        return fin;
    }

    private void printValue() { //add values to the final table for answers
        Tableau tab = this.tabList.get(this.tabList.size() - 1);
        String[][] t = tab.getTable();
        int j = tab.getCol() - 1;
        
        for(int i = 0; i < tab.getRow(); i++) {
            if(i == 0)
                t[i][j] = "Val";
            else t[i][j] = tab.checkColumn(i);
        }
    }

    //only one iteration in gauss jordan
    private Tableau gaussJordan(Tableau tab, int pEX, int pEY, float pe) {
        String[][] t = tab.getTable();
        int nrow = tab.getRow();
        int ncol = tab.getCol();

        for(int i = 1; i < (ncol - 1); i++) {     //normalize
            float norm = Float.valueOf(t[pEX][i]) / pe;
            t[pEX][i] = Float.toString(norm);
        }

        for (int j = 1; j < nrow; j++) {
            if (j == pEX) continue;

            float multiplier = new Float(t[j][pEY]);
            for(int k = 1; k < (ncol - 1); k++) {
                float pRElem = new Float(t[pEX][k]);
                float orig = new Float(t[j][k]);
                float el = orig - (multiplier * pRElem);
                t[j][k] = Float.toString(el);
            }
        }

        String[][] newTab = new String[nrow][ncol - 1];

        for(int m = 0; m < nrow; m++) {
            for(int n = 0; n < (ncol - 1); n++) {
                newTab[m][n] = t[m][n];
            }
        }

        return new Tableau(newTab);
    }

    private boolean isDone(Tableau tab) {
        if(this.tabList.size() >= 50) return true;

        String[][] table = tab.getTable();
        
        int lastRow = table.length - 1;
        for(int i = 0; i < (table[0].length - 2); i++) {
            if(table[lastRow][i].contains("-"))
                return false;
        }

        return true;
    }

    //parse constraints to add correct values to the table
    private void addVariables(String[][] tab, int row, String eq, int n) {
        String sol = "0";
        int flag = 0;
        String splitter = "";

        if(eq.contains("<="))
            splitter ="<=";
        else if(eq.contains(">=")) {
            splitter = ">=";
        } else if(eq.contains("S")) {
            splitter = "S";
        } else splitter = "=";

        String[] wsol = eq.split(splitter);
        if(wsol.length > 1) {
            sol = wsol[1];
            flag = 1;
        }

        String[] v = (wsol[0]).split("\\+");
        for(int col = 1; col < n; col++) {
            for(int i = 0; i < v.length; i++) {
                String[] arr = (v[i]).split("\\*");
                if(arr[1].equals(tab[0][col])) {
                    if(flag == 0){
                        tab[row][col] = arr[0];
                        tab[row][n-1] = "1";
                    } else
                        tab[row][col] = arr[0];
                } else if(tab[row][col] == null) tab[row][col] = "0";
                
            }
            
            if(col == (n - 1))
                tab[row][col] = sol;
        }
    }

    //add constraints to the table
    private void addConstraint(String[][] tab, int row, String eq, int n) {
        String sol = "0";
        int flag = 0;
        String splitter = "";
        String sign = "";
        String objSign = "";

        if(eq.contains("<="))
            splitter ="<=";
        else if(eq.contains(">=")) {
            splitter = ">=";
            sign = "-";
        } else if(eq.contains("S")) {
            splitter = "S";
            sign = "-";
        } else splitter = "=";

        if(this.selection == 1) objSign = "-";

        String[] wsol = eq.split(splitter);
        if(wsol.length > 1) {
            sol = wsol[1];
            flag = 1;
        }

        String[] v = (wsol[0]).split("\\+");
        for(int col = 1; col <= n; col++) {
            for(int i = 0; i < v.length; i++) {
                String[] arr = (v[i]).split("\\*");
                if(arr[1].equals(tab[0][col])) {
                    if(flag == 0){
                        if(!arr[0].contains("-"))
                            tab[row][col] = objSign + arr[0];
                            else {
                                float g = new Float(arr[0]);
                                g = g * -1;
                                tab[row][col] = String.valueOf(g);
                            }
                        tab[row][n-1] = "1";
                    }
                    else
                        tab[row][col] = arr[0];
                } else if(("S" + row).equals(tab[0][col])) {
                    tab[row][col] = sign + "1";
                } else if(tab[row][col] == null) tab[row][col] = "0";
                
            }
            
            if(col == n)
                tab[row][col] = sol;
        }
    }
}