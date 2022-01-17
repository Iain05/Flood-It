import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Write a description of class Main here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Main
{
    public static JPanel panel;
    public static JFrame frame = new JFrame("Flood-It");
    
    public static int gridWidth = 20;
    public static int gridHeight = 20;
    public static int boxSize = 20;
    
    public static Color[] tileTextColor = {
            new Color(255,0,255),
            new Color(255, 0, 0),
            new Color(0, 255, 255),
            new Color(255, 255, 0),
            new Color(0, 187, 0),
            new Color(255,204,102),
            new Color(102,102,255),
            new Color(128,0,128),
    };
    
    public static void main(String[] args) {
        
        initGridGUI();
        startGUI();
    }
    
    public static void initGridGUI() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        frame.setContentPane(panel);
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(1,1,1,1);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                JLabel tempLabel = new JLabel("", SwingConstants.CENTER);
                tempLabel.setOpaque(true);
                tempLabel.setBackground(Color.RED);
                tempLabel.setPreferredSize(new Dimension(boxSize, boxSize));
                
                c.gridy = i;
                c.gridx = j;
                panel.add(tempLabel, c);
            }
        }
        
    }
    public static void startGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
