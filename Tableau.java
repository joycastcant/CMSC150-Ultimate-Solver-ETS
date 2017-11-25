import java.util.ArrayList;

public class Tableau {
    private String[][] table;
    private int pEX;
    private float pe;
    private int nrow;
    private int ncol;
    private String basicSolution;

    public Tableau(String table[][]) {
        this.nrow = table.length;
        this.ncol = table[0].length + 1;
        this.basicSolution = " ";

        this.table = new String[this.nrow][this.ncol];
        
        for(int i = 0; i < this.nrow; i++) {
            for(int k = 0; k < this.ncol; k++) {
                if(k == (this.ncol - 1)) {
                    if(i == 0) this.table[0][k] = "TR";
                    else this.table[i][k] = "0";
                } else this.table[i][k] = table[i][k];
            }
        }
    }

    public String getBasicSolution() {
        return this.basicSolution;
    }

    public void setBasicSolution() {
        String add = "";
        int flag = 0;

        for(int j = 1; j < (this.getCol() - 2); j++) {
            for(int i = 1; i < this.getRow(); i++) {
                if(this.table[0][j].equals(this.checkColumn(i))) {
                    add = add + this.checkColumn(i) + " = " + this.table[i][this.getCol() - 2] + "; ";
                    flag = 1;
                }
            }
            if(flag == 0) {
                add = add + this.table[0][j] + " = 0.0; ";
            } else flag = 0;
        }

        this.basicSolution = "Basic Solution: " + add;
    }

    public String[][] getTable() {
        return this.table;
    }

    public void setTable(String [][] table) {
        this.table = table;
    }

    public int getPivotColumn() {
        String[][] t = this.getTable();
        float min = 0.0f; //min bc iz negative
        int index = 0;

        int lastRow = this.nrow - 1;
        for(int i = 0; i < (this.ncol - 2); i++) {
            if(t[lastRow][i].contains("-") && new Float(t[lastRow][i]) < min) {
                min = new Float(t[lastRow][i]);
                index = i;
            }
        }

        return index;
    }

    public void setPivotElementX(int x) {
        this.pEX = x;
    }

    public int getPivotElementX() {
        return this.pEX;
    }

    public float getPE() {
        return this.pe;
    }

    public int getRow() {
        return this.nrow;
    }

    public int getCol() {
        return this.ncol;
    }

    public void computeTestRatio(int pivotColumn) {
        String[][] t = this.getTable();

        int soln = this.ncol - 2;
        int tr = this.ncol - 1;

        for(int i = 1; i < this.nrow; i++) {
            float a = new Float(t[i][soln]);
            float b = new Float(t[i][pivotColumn]);

            if(i == this.nrow - 1)
                t[i][tr] = " ";
            else if(b != 0) {
                t[i][tr] = Float.toString(a/b);
                if(t[i][tr].equals("-0.0"))
                    t[i][tr] = "0";
            }
            else t[i][tr] = "X";
        }

        float min = 0.0f;
        int x = 0;

        for(int j = 1; j < this.nrow; j++) {
            if(!t[j][tr].equals(" ") && !t[j][tr].equals("X") && new Float(t[j][tr]) > 0 && (min == 0 || new Float(t[j][tr]) < min)) {
                min = new Float(t[j][tr]);
                x = j;
            }
        }

        this.pEX = x;
        this.pe = new Float(t[x][pivotColumn]);
    }

    public String checkColumn(int row) {
        String[][] t = this.getTable();
        String var = "";
        int found = 0;
        int flag = 0;

        for(int i = 1; i < (this.getCol() - 2); i++) {
            var = t[0][i];
            flag = 0;
            found = 0;
            for(int j = 1; j < this.getRow(); j++) {
                String n = t[j][i];
                if(!n.equals("0.0") && !n.equals("1.0"))
                    continue;
                else if(n.equals("1.0") && flag == 0) {
                    flag = 1;
                    if(j == row) {
                        found = 1;
                        var = t[0][i];
                    }
                } else if(n.equals("1.0") && flag == 1)
                    continue;

                if(found == 1 && j == this.getRow() - 1){
                    return var;
                }
            }
        }

        return "";
    }
}