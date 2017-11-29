import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.WindowConstants;

public class Frame extends JFrame{

    public Frame(){
        super("Ultimate Solver");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));

        Frame f = this;

        Container c = this.getContentPane();
        JPanel container = new JPanel(new CardLayout());
        SolverPanel solverPanel = new SolverPanel(container);
        ETSPanel etsPanel = new ETSPanel(container);
        Color blu = new Color(144, 223, 244);

        JButton startButton = new JButton("START");
        startButton.setBackground(blu);
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setFont(new Font("Default", Font.BOLD, 25));

        JButton etsButton = new JButton("ETS");
        etsButton.setBackground(blu);
        etsButton.setForeground(Color.BLACK);
        etsButton.setFocusPainted(false);
        etsButton.setBorderPainted(false);
        etsButton.setFont(new Font("Default", Font.BOLD, 25));

        JPanel startPanel = new JPanel();
        JPanel etsPanelB = new JPanel();

        startPanel.add(startButton);
        etsPanelB.add(etsButton);

        startPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        etsPanelB.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 20));
        buttons.setBackground(Color.WHITE);
        buttons.add(startPanel);
        buttons.add(etsPanelB);

        JPanel iPan = new JPanel(new BorderLayout());
        URL url = this.getClass().getResource("images/ultimate_solver.gif");
        Icon myImgIcon = new ImageIcon(url);
        JLabel imageLbl = new JLabel(myImgIcon);
        iPan.add(imageLbl, BorderLayout.CENTER);
        iPan.add(buttons, BorderLayout.SOUTH);

        container.add(iPan, "Exit");
        container.add(solverPanel,"Solver");
        container.add(etsPanel,"ETS");
        c.add(container);
        
        startButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) container.getLayout();
                cardLayout.show(container,"Solver");
                solverPanel.requestFocusInWindow();
            }
        });

        startButton.addMouseListener(new MouseListener(){
            public void mouseEntered(MouseEvent e){
                startButton.setForeground(Color.GRAY);
            }

            public void mouseExited(MouseEvent e){
                startButton.setForeground(Color.BLACK);
            }
            public void mouseReleased(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseClicked(MouseEvent e){}
        });

        etsButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JPanel all = new JPanel(new GridLayout(3, 1));
                JPanel src = new JPanel();
                JLabel srcLabel = new JLabel("Number of sources: ");
                JTextField srcField = new JTextField(5);
                src.add(srcLabel);
                src.add(srcField);
                
                JPanel dest = new JPanel();
                JLabel destLabel = new JLabel("Number of destinations: ");
                JTextField destField = new JTextField(5);
                dest.add(destLabel);
                dest.add(destField);
                
                all.add(src);
                all.add(dest);
                
                JOptionPane pane = new JOptionPane(all);

                JDialog dialog = pane.createDialog(container, "Efficient Transport System");

                dialog.setVisible(true);

                if(((Integer)pane.getValue()).intValue() == JOptionPane.OK_OPTION) {
                    f.setTitle("Efficient Transport System");
                    etsPanel.setSrc(srcField.getText());
                    etsPanel.setDest(destField.getText());
                    etsPanel.setSelection(1);
                    etsPanel.initComponents();
                    CardLayout cardLayout = (CardLayout) container.getLayout();
                    cardLayout.show(container,"ETS");
                    solverPanel.requestFocusInWindow();
                }
            }
        });

        etsButton.addMouseListener(new MouseListener(){
            public void mouseEntered(MouseEvent e){
                etsButton.setForeground(Color.GRAY);
            }

            public void mouseExited(MouseEvent e){
                etsButton.setForeground(Color.BLACK);
            }
            public void mouseReleased(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseClicked(MouseEvent e){}
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}