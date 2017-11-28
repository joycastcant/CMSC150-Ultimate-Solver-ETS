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

public class SolverPanel extends JPanel {
    protected final static int MINIMIZE = 0;
    protected final static int MAXIMIZE = 1;
    protected final Color GRE = new Color(210, 215, 216);
    
    protected Container container;
    private JTextField objFxnField;
    private JTextArea conArea;
    private int selection;
    private String objFxn;
    private String constraints;
    private JPanel tables;

	public SolverPanel(Container container){
		super();
        this.container = container;
        this.selection = MAXIMIZE;
        this.objFxn = "";
        this.constraints = "";
		this.setBackground(new Color(234, 240, 242));
        this.requestFocusInWindow();
        this.initComponents();
    }

    private void initComponents() {
        Color blu = new Color(144, 223, 244);

        JPanel radioPanel = new JPanel();
        JRadioButton min = new JRadioButton("Minimize");
        JRadioButton max = new JRadioButton("Maximize");
        max.setSelected(true);
        min.setOpaque(false);
        max.setOpaque(false);
        radioPanel.setOpaque(false);

        ButtonGroup group = new ButtonGroup();
        group.add(min);
        group.add(max);

        min.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                minAction(e);
            }
        });

        max.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                maxAction(e);
            }
        });

        JPanel objFxnPanel = new JPanel();
        JPanel objFxnLabelPanel = new JPanel();
        JLabel objFxnLabel = new JLabel("Objective Function");
        JPanel objFxnFieldPanel = new JPanel();
        this.objFxnField = new JTextField();
        objFxnFieldPanel.setOpaque(false);
        objFxnLabelPanel.setBackground(blu);
        objFxnPanel.setBackground(Color.WHITE);
        objFxnPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel plotPanel = new JPanel();
        JPanel plotLabelPanel = new JPanel();
        JLabel plotLabel = new JLabel("Plot");
        plotLabelPanel.setBackground(blu);
        plotPanel.setBackground(Color.WHITE);
        plotPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel leftPanel = new JPanel();
        JPanel conPanel = new JPanel();
        JPanel conLabelPanel = new JPanel();
        JLabel conLabel = new JLabel("Constraints");
        this.conArea = new JTextArea();

        JPanel tabPanel = new JPanel();
        JPanel tabLabelPanel = new JPanel();
        JLabel tabLabel = new JLabel("Tableau");
        this.tables = new JPanel(new CardLayout());
        this.tables.add(new JPanel(), "Empty");
        
        JPanel solvePanel = new JPanel(new BorderLayout());
        JPanel solvePanel2 = new JPanel();
        JButton solveButton = new JButton("Solve!");
        
        leftPanel.setOpaque(false);
        conLabelPanel.setBackground(blu);
        conPanel.setBackground(Color.WHITE);
        conPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        tabLabelPanel.setBackground(blu);
        tabPanel.setBackground(Color.WHITE);
        tabPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        solvePanel.setOpaque(false);
        solvePanel2.setOpaque(false);;
        solvePanel.add(solvePanel2, BorderLayout.SOUTH);
        solvePanel2.add(solveButton);

        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                solveButtonAction(e);
            }
        });

        //layouting
        GroupLayout radioPanelLayout = new GroupLayout(radioPanel);
        radioPanel.setLayout(radioPanelLayout);

        radioPanelLayout.setHorizontalGroup(
            radioPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(radioPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(max, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(min, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        radioPanelLayout.setVerticalGroup(
            radioPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(radioPanelLayout.createSequentialGroup()
                .addGroup(radioPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(max)
                    .addComponent(min))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        GroupLayout objFxnFieldPanelLayout = new GroupLayout(objFxnFieldPanel);
        objFxnFieldPanel.setLayout(objFxnFieldPanelLayout);
        objFxnFieldPanelLayout.setHorizontalGroup(
            objFxnFieldPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(objFxnFieldPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(objFxnFieldPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(objFxnField, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        objFxnFieldPanelLayout.setVerticalGroup(
            objFxnFieldPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(objFxnFieldPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, objFxnFieldPanelLayout.createSequentialGroup()
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(objFxnField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        GroupLayout objFxnLabelPanelLayout = new GroupLayout(objFxnLabelPanel);
        objFxnLabelPanel.setLayout(objFxnLabelPanelLayout);
        objFxnLabelPanelLayout.setHorizontalGroup(
            objFxnLabelPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(objFxnLabelPanelLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(objFxnLabel, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE))
        );
        objFxnLabelPanelLayout.setVerticalGroup(
            objFxnLabelPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(objFxnLabel, GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
        );

        GroupLayout objFxnPanelLayout = new GroupLayout(objFxnPanel);
        objFxnPanel.setLayout(objFxnPanelLayout);
        objFxnPanelLayout.setHorizontalGroup(
            objFxnPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(objFxnPanelLayout.createSequentialGroup()
                // .addGap(22, 22, 22)
                .addComponent(objFxnLabelPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(objFxnFieldPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        objFxnPanelLayout.setVerticalGroup(
            objFxnPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(objFxnPanelLayout.createSequentialGroup()
                .addGroup(objFxnPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(objFxnFieldPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(objFxnLabelPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Dimension dim = new Dimension(500, 190);

        tabPanel.setPreferredSize(dim);
        tabPanel.setLayout(new BorderLayout());
        tabPanel.add(tabLabelPanel, BorderLayout.NORTH);
        tabLabelPanel.add(tabLabel);
        tabPanel.add(this.tables, BorderLayout.CENTER);

        plotPanel.setPreferredSize(dim);
        conPanel.setLayout(new BorderLayout());
        conPanel.add(conLabelPanel, BorderLayout.NORTH);
        conPanel.add(conArea, BorderLayout.CENTER);
        conLabelPanel.add(conLabel);

        solvePanel.setPreferredSize(new Dimension(325, 50));

        leftPanel.setPreferredSize(new Dimension(500, 445));
        GroupLayout leftPanelLayout = new GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(plotPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(solvePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addComponent(plotPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(solvePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        plotPanel.setLayout(new BorderLayout());
        plotPanel.add(plotLabelPanel, BorderLayout.NORTH);
        plotLabelPanel.add(plotLabel);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(objFxnPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(radioPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(leftPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(conPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(objFxnPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(conPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }

    private void minAction(ActionEvent e) {
        this.selection = MINIMIZE;
    }

    private void maxAction(ActionEvent e) {
        this.selection = MAXIMIZE;
    }

    private void solveButtonAction(ActionEvent e) {
        this.objFxn = this.objFxnField.getText();
        this.constraints = this.conArea.getText();
        UltimateSolver us = new UltimateSolver(this.selection, this.objFxn, this.constraints);
        ArrayList<Tableau> tabs = us.getTabList();
        
        // this.tabPanel.setLayout(new GridLayout((tabs.size() - 1), 1));
        JPanel tableauPanel = new JPanel(new GridLayout(tabs.size(), 1));
        // BoxLayout box = new BoxLayout(tableauPanel, BoxLayout.Y_AXIS);
        JScrollPane tabPane = new JScrollPane();
        tabPane.setViewportView(tableauPanel);
        this.tables.add(tabPane, "Tableau");
        
        for(int i = 0; i < tabs.size(); i++) {
            Tableau curr = tabs.get(i);
            String[][] t = curr.getTable();
            DefaultTableModel model = new DefaultTableModel(curr.getRow(), curr.getCol());
            // DefaultTableModel model = new DefaultTableModel(curr.getTable(), null);
            for(int j = 0; j < curr.getRow(); j++) {
                for(int k = 0; k < curr.getCol(); k++) {
                    model.setValueAt(t[j][k], j, k);
                }
            }

            JTable table = new JTable(model);
            table.setEnabled(false);
            JPanel newPanel = new JPanel(new BorderLayout());
            JPanel top = new JPanel();
            JLabel topLabel = new JLabel();
            JPanel basicSol = new JPanel();
            JLabel basic = new JLabel(curr.getBasicSolution());

            if(i == (tabs.size() - 1))
                topLabel.setText("Final Answer");
            else
                topLabel.setText("Iteration " + i);

            basicSol.setBackground(Color.WHITE);
            basicSol.add(basic);

            top.setBackground(GRE);
            top.add(topLabel);

            newPanel.add(top, BorderLayout.NORTH);
            newPanel.add(table, BorderLayout.CENTER);
            newPanel.add(basicSol, BorderLayout.SOUTH);
            newPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            tableauPanel.add(newPanel);
        }
        
        CardLayout cardLayout = (CardLayout) this.tables.getLayout();
        cardLayout.show(this.tables,"Tableau");
    }
}