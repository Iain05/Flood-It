import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.BorderFactory.*;

public class Main {
    public static JPanel panel;
    public static JFrame frame = new JFrame("Flood-It");
    public static JLabel footerLabel;

    public static int gridWidth = 22;
    public static int gridHeight = 22;
    public static int boxSize = 20;

    public static int numColours = 8;
    public static Color[] boxColor = {
            new Color(255, 255, 0),
            new Color(255, 204, 102),
            new Color(255, 0, 0),
            new Color(0, 187, 0),
            new Color(128, 0, 128),
            new Color(0, 255, 255),
            new Color(102, 102, 255),
            new Color(255, 0, 255),
    };

    public static Box[][] boxes = new Box[gridHeight+2][gridWidth+2];

    public static void main(String[] args) {
        resetGame();
    }

        public static void resetGame() {

        frame.setResizable(true);
        frame.getContentPane().revalidate();
        
        initMenuBar();
        initBoxes();
        initGridGUI();
        footerGUI();
        startGUI();
        
    }
    
    public static void initGridGUI() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        frame.setContentPane(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        
        createBorders(c);
        
        c.gridwidth = 1;
        c.gridheight = 1;
        Random random = new Random();
        for (int i = 1; i <= gridHeight; i++) {
            for (int j = 1; j <= gridWidth; j++) {
                JLabel tempLabel = new JLabel("", SwingConstants.CENTER);
                tempLabel.setOpaque(true);
                tempLabel.setPreferredSize(new Dimension(boxSize, boxSize));

                boxes[i][j].label = tempLabel;
                boxes[i][j].setColor(random.nextInt(numColours));
                   
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
    public static void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        
        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.setMnemonic(KeyEvent.VK_R);
        restartItem.addActionListener(e -> resetGame());
        fileMenu.add(restartItem);
        
        menuBar.add(fileMenu);

        JMenu difficultyMenu = new JMenu("Size");
        difficultyMenu.setMnemonic(KeyEvent.VK_S);

        JMenuItem easyItem = new JMenuItem("Small");
        easyItem.setMnemonic(KeyEvent.VK_S);
        easyItem.addActionListener(e -> {
            gridHeight = 10;
            gridWidth = 10;
            resetGame();
        });
        JMenuItem mediumItem = new JMenuItem("Medium");
        mediumItem.setMnemonic(KeyEvent.VK_M);
        mediumItem.addActionListener(e -> {
            gridHeight = 16;
            gridWidth = 16;
            resetGame();
        });
        JMenuItem hardItem = new JMenuItem("Large");
        hardItem.setMnemonic(KeyEvent.VK_L);
        hardItem.addActionListener(e -> {
            gridHeight = 22;
            gridWidth = 22;
            resetGame();
        });
        
        difficultyMenu.add(easyItem);
        difficultyMenu.add(mediumItem);
        difficultyMenu.add(hardItem);
        menuBar.add(difficultyMenu);
        
        JMenu numColoursMenu = new JMenu("Colors");
        numColoursMenu.setMnemonic(KeyEvent.VK_C);

        JMenuItem fourItem = new JMenuItem("4");
        fourItem.setMnemonic(KeyEvent.VK_4);
        fourItem.addActionListener(e -> {
            numColours = 4;
            resetGame();
        });
        JMenuItem sixItem = new JMenuItem("6");
        sixItem.setMnemonic(KeyEvent.VK_6);
        sixItem.addActionListener(e -> {
            numColours = 6;
            resetGame();
        });
        JMenuItem eightItem = new JMenuItem("8");
        eightItem.setMnemonic(KeyEvent.VK_8);
        eightItem.addActionListener(e -> {
            numColours = 8;
            resetGame();
        });

        numColoursMenu.add(fourItem);
        numColoursMenu.add(sixItem);
        numColoursMenu.add(eightItem);
        menuBar.add(numColoursMenu);

        frame.setJMenuBar(menuBar);
    }
    public static void initBoxes() {
        for (int i = 1; i <= gridHeight; i++) {
            for (int j = 1; j <= gridWidth; j++) {
                boxes[i][j] = new Box();
            }
        }
    }
    
    public static void footerGUI() {
        footerLabel = new JLabel("", SwingConstants.CENTER);
        footerLabel.setOpaque(true);
        footerLabel.setBackground(Color.WHITE);
        footerLabel.setFont(new Font("Lato Bold", Font.BOLD, 22));
        footerLabel.setText("boop");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 5, 0);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = gridHeight+2;
        c.gridwidth = gridWidth+2;
        panel.add(footerLabel, c);
    }
    
    public static void movesRemaining() {
        footerLabel.setText("Moves Remaining: ");
    }
    
    public static void createBorders(GridBagConstraints c) {
        JLabel borderLabelTop = new JLabel("", SwingConstants.CENTER);
        borderLabelTop.setOpaque(true);
        borderLabelTop.setPreferredSize(new Dimension(1, boxSize/2));
        borderLabelTop.setBackground(Color.BLACK);
        
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = gridWidth + 2;
        c.gridheight = 1;
        panel.add(borderLabelTop, c);
        
        JLabel borderLabelLeft = new JLabel("", SwingConstants.CENTER);
        borderLabelLeft.setOpaque(true);
        borderLabelLeft.setPreferredSize(new Dimension(boxSize/2, 1));
        borderLabelLeft.setBackground(Color.BLACK);
        
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridheight = gridHeight + 2;
        panel.add(borderLabelLeft, c);
        
        JLabel borderLabelBottom = new JLabel("", SwingConstants.CENTER);
        borderLabelBottom.setOpaque(true);
        borderLabelBottom.setPreferredSize(new Dimension(1, boxSize/2));
        borderLabelBottom.setBackground(Color.BLACK);
        
        c.gridy = gridHeight + 1;
        c.gridx = 0;
        c.gridwidth = gridWidth + 2;
        c.gridheight = 1;
        panel.add(borderLabelBottom, c);
        
        JLabel borderLabelRight = new JLabel("", SwingConstants.CENTER);
        borderLabelRight.setOpaque(true);
        borderLabelRight.setPreferredSize(new Dimension(boxSize/2, 1));
        borderLabelRight.setBackground(Color.BLACK);
        
        c.gridy = 0;
        c.gridx = gridWidth + 1;
        c.gridwidth = 1;
        c.gridheight = gridHeight + 2;
        panel.add(borderLabelRight, c);
    }
}
    
class Box {
    public int colorIndex;
    public JLabel label;

    public Box() {
    }

    public void setColor(int newColorIndex) {
        colorIndex = newColorIndex;
        label.setBackground(Main.boxColor[colorIndex]);
    }
}
