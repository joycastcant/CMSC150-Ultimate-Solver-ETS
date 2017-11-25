import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;

import javafx.scene.layout.Border;

import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;

import java.util.ArrayList;

public class ETSPanel extends JPanel {
    protected final int MINIMIZE = 0;
    protected final int MAXIMIZE = 1;
    protected final Color GRE = new Color(210, 215, 216);
    
    protected Container container;
    private String objFxn;
    private String constraints;
    private JPanel tables;
    private int src;
    private int dest;
    private int selection;

	public ETSPanel(Container container){
		super(new BorderLayout());
        this.container = container;
        this.selection = MAXIMIZE;
        this.objFxn = "";
        this.constraints = "";
		this.setBackground(new Color(234, 240, 242));
        this.requestFocusInWindow();
    }

    public void setSrc(String n) {
        this.src = Integer.parseInt(n);
    }

    public void setDest(String n) {
        this.dest = Integer.parseInt(n);
    }

    public void setSelection(int n) {
        this.selection = n;
    }

    public void initComponents() {
        Color blu = new Color(144, 223, 244);
        // JPanel everyThing = new JPanel(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("Values");
        titlePanel.add(title);
        titlePanel.setBackground(blu);
        this.add(titlePanel, BorderLayout.NORTH);

        JPanel tabPanel = new JPanel(new GridLayout(4, 1));

        JPanel capPanel = new JPanel(new BorderLayout());
        JLabel capLabel = new JLabel("Capacity per source");
        DefaultTableModel capModel = new DefaultTableModel(2, (this.src + 1));
        JTable capTable = new JTable(capModel);
        
        for(int i = 0; i <= this.src; i++) {
            if(i == 0) {
                capModel.setValueAt("Source", 0, i);
                capModel.setValueAt("Capacity", 1, i);
            } else {
                capModel.setValueAt(i, 0, i);
            }
        }

        capPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        capPanel.add(capLabel, BorderLayout.NORTH);
        capPanel.add(capTable, BorderLayout.CENTER);
        tabPanel.add(capPanel);

        JPanel demPanel = new JPanel(new BorderLayout());
        JLabel demLabel = new JLabel("Demand per destination");
        DefaultTableModel demModel = new DefaultTableModel(2, (this.dest + 1));
        JTable demTable = new JTable(demModel);
        
        for(int i = 0; i <= this.dest; i++) {
            if(i == 0) {
                demModel.setValueAt("Destination", 0, i);
                demModel.setValueAt("Demand", 1, i);
            } else {
                demModel.setValueAt(i, 0, i);
            }
        }

        demPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        demPanel.add(demLabel, BorderLayout.NORTH);
        demPanel.add(demTable, BorderLayout.CENTER);
        tabPanel.add(demPanel);

        JPanel coPanel = new JPanel(new BorderLayout());
        JLabel coLabel = new JLabel("Cost of transaction");
        DefaultTableModel coModel = new DefaultTableModel((this.src + 1), (this.dest + 1));
        JTable coTable = new JTable(coModel);
        
        for(int i = 0; i <= this.src; i++) {
            for(int j = 0; j <= this.dest; j++) {
                if(i == 0 && j == 0)
                    coModel.setValueAt("Src | Dest", i, j);
                else if(i == 0)
                    coModel.setValueAt(("D" + j), i, j);
                else if(j == 0)
                    coModel.setValueAt(("S" + i), i, j);
            }
        }

        coPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        coPanel.add(coLabel, BorderLayout.NORTH);
        coPanel.add(coTable, BorderLayout.CENTER);
        tabPanel.add(coPanel);

        JPanel unitPanel = new JPanel(new GridLayout(1, 2));
        JPanel units = new JPanel(new GridLayout(2, 1));

        JPanel uCD = new JPanel();
        JLabel uCDLabel = new JLabel("Unit of capacity and demand: ");
        JTextField uCDField = new JTextField(5);
        uCD.add(uCDLabel);
        uCD.add(uCDField);
        uCD.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JPanel uCo = new JPanel();
        JLabel uCoLabel = new JLabel("Unit of cost: ");
        JTextField uCoField = new JTextField(5);
        uCo.add(uCoLabel);
        uCo.add(uCoField);
        uCo.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        units.add(uCD);
        units.add(uCo);
        
        JPanel next = new JPanel(new BorderLayout());
        JPanel nextButtonP = new JPanel();
        JButton nextButton = new JButton("NEXT");
        nextButtonP.add(nextButton);
        
        next.add(nextButtonP, BorderLayout.SOUTH);

        unitPanel.add(units);
        unitPanel.add(next);
        tabPanel.add(unitPanel);

        JScrollPane pane = new JScrollPane();
        pane.setViewportView(tabPanel);
        this.add(pane, BorderLayout.CENTER);
    }
    
}