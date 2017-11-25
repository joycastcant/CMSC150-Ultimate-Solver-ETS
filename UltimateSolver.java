/* import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RList;
import org.rosuda.JRI.RVector;
import org.rosuda.JRI.RMainLoopCallbacks; */

import java.util.ArrayList;
import java.util.HashMap;

public class UltimateSolver {
    private int selection;
    private String[] objFxn;
    private ArrayList<String> constraints;
    private ArrayList<Tableau> tabList;
    // private ArrayList<Tableau> tableau;

    public UltimateSolver(int selection, String fxn, String con) {
        this.tabList = new ArrayList<Tableau>();
        this.selection = selection;
        String f = fxn.split("=")[1];
        this.objFxn = f.split("\\+");
        this.constraints = new ArrayList<String>();

        String[] conArray = con.split("\n");
        for(int i = 0; i < conArray.length; i++) {
            constraints.add(conArray[i]);
        }
        
        solve(this.objFxn.length, conArray.length, f);
    }

    public ArrayList<Tableau> getTabList() {
        return this.tabList;
    }

    private void solve(int vars, int nrow, String f) {
        int ncol = vars + nrow + 2;
        String[] variables = new String[vars];
        String[][] table = new String[nrow + 2][ncol + 1];

        for(int k = 0; k < vars; k++) {
            String[] pair = (this.objFxn[k]).split("\\*");
            variables[k] = pair[1];
        }

        for(int j = 0; j < (ncol + 1); j++) {
            if (j == 0) {
                table[0][j] ="Var";
            } else {
                if (j <= vars)
                    table[0][j] = variables[j - 1]; //actual vars
                else if(j == (ncol - 1))
                    table[0][j] ="Z";
                else if(j == ncol)
                    table[0][j] ="Sol'n";
                else
                    table[0][j] ="S"+ (j - vars); //slack
            }
        }
        
        for(int i = 1; i < (nrow + 2); i++) {
            if(i == (nrow + 1)) {
                table[i][0] ="Z";
                this.addConstraint(table, i, f, ncol);
            } else {
                table[i][0] ="S"+ i;
                this.addConstraint(table, i, this.constraints.get(i - 1), ncol);
            }
        }

        this.tabList.add(new Tableau(table));
        this.tabList.add(new Tableau(table));
        
        while(!isDone(this.tabList.get(this.tabList.size() - 1))) {
            int curr = this.tabList.size() - 1;

            Tableau currTab = this.tabList.get(curr);
            int pivotColumn = currTab.getPivotColumn();

            currTab.computeTestRatio(pivotColumn);
            int pEX = currTab.getPivotElementX();
            int pEY = pivotColumn;
            float pe = currTab.getPE();

            this.tabList.add(gaussJordan(currTab, pEX, pEY, pe));
        }

        //adjust TR
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

        /* for(int q = 0; q < (this.tabList.size() - 1); q++) {
            System.out.println("Iteration " + q);
            Tableau cur = this.tabList.get(q);
            for(int x = 0; x < cur.getRow(); x++) {
                for(int y = 0; y < cur.getCol(); y++) {
                    System.out.print(cur.getTable()[x][y] + "\t");
                }
                System.out.println(" ");
            }
        } */
        
        printValue();
    }

    private void printValue() {
        Tableau tab = this.tabList.get(this.tabList.size() - 1);
        String[][] t = tab.getTable();
        int j = tab.getCol() - 1;
        
        for(int i = 0; i < tab.getRow(); i++) {
            if(i == 0)
                t[i][j] = "Val";
            else t[i][j] = tab.checkColumn(i);
        }

        /* System.out.println("Final Answers: ");
        for(int x = 0; x < tab.getRow(); x++) {
            for(int y = 0; y < tab.getCol(); y++) {
                System.out.print(t[x][y] + "\t");
            }
            System.out.println(" ");
        } */
    }

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
        String[][] table = tab.getTable();
        
        int lastRow = table.length - 1;
        for(int i = 0; i < (table[0].length - 2); i++) {
            if(table[lastRow][i].contains("-"))
                return false;
        }

        return true;
    }

    private void addConstraint(String[][] tab, int row, String eq, int n) {
        String sol = "0";
        int flag = 0;
        String splitter = "";
        String sign = "";
        if (eq.contains("<=")) {
            splitter ="<=";
            sign = "+";
        } else if (eq.contains(">=")) {
            splitter = ">=";
            sign = "-";
        } else splitter = "=";

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
                        tab[row][col] = "-"+ arr[0];
                        tab[row][n-1] = "1";
                    }
                    else
                        tab[row][col] = arr[0];
                } else if(("S" + row).equals(tab[0][col])) {
                    tab[row][col] = "1";
                } else if(tab[row][col] == null) tab[row][col] = "0";
            }

            if(col == n)
                tab[row][col] = sol;
        }
    }
}