import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Container;
import java.awt.Image;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.WindowConstants;

public class Frame extends JFrame{

    public Frame(){
        super("Ultimate Solver");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));

        final Container c = this.getContentPane();
        final JPanel container = new JPanel(new CardLayout());
        final SolverPanel solverPanel = new SolverPanel(container);

        // Thread gp = new Thread(solverPanel);

        final JButton startButton = new JButton("START");
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setFont(new Font("Default", Font.BOLD, 25));

        /* Image image = this.getToolkit().createImage("photos/battleCityProto.gif");
        ImagePanel imagePanel = new ImagePanel(image,startButton);

        container.add(imagePanel,"Exit"); */
        container.add(startButton);
        container.add(solverPanel,"Solver");
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
                startButton.setForeground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent e){
                startButton.setForeground(Color.WHITE);
            }
            public void mouseReleased(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseClicked(MouseEvent e){}
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        /* gp.start();
        try{gp.join();}catch(Exception e){} */
        
    }
}