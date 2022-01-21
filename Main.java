import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.BorderFactory.*;
import java.util.concurrent.*;

public class Main {
    public static JPanel panel;
    public static JFrame frame = new JFrame("Flood-It");
    public static JLabel footerLabel;
    public static JLabel headerLabel;

    public static int gridWidth = 32;
    public static int gridHeight = 32;
    public static int boxSize = 20;
    public static String gameSize = "large";

    public static int currentMoves = 0;
    public static int maxMoves = 61;

    public static int numColours = 8;
    public static Color[] boxColor = {
            new Color(238,77,77),
            new Color(255,136,77),
            new Color(255,196,77),
            new Color(139,201,77),
            new Color(77,219,196),   
            new Color(160,113,255),
            new Color(255,77,165),
            new Color(119,132,146),
    };
    
    public static Color[] boxColorHighlight = {
            new Color(241,113,113),
            new Color(255,160,113),
            new Color(255,208,113),
            new Color(162,212,113),
            new Color(123,236,218),
            new Color(179,141,255),
            new Color(255,113,183),
            new Color(162,173,184),
    };

    public static Box[][] boxes = new Box[gridHeight + 2][gridWidth + 2];

    public static void main(String[] args) {
        resetGame();
    }

    public static void resetGame() {
        frame.setResizable(true);
        frame.getContentPane().revalidate();
        
        currentMoves = 0;

        initMenuBar();
        initBoxes();
        initGridGUI();
        headerGUI();
        footerGUI();
        
        startGUI();

    }

    public static void initGridGUI() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(205,205,205));
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
                Box tempBox = boxes[i][j];
                

                JLabel tempLabel = new JLabel("", SwingConstants.CENTER);
                tempLabel.setOpaque(true);
                tempLabel.setPreferredSize(new Dimension(boxSize, boxSize));
                /*tempLabel.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (tempBox.colorIndex != boxes[1][1].colorIndex) {
                            floodColor(tempBox.colorIndex);
                            frame.getContentPane().repaint();
                            currentMoves++;
                            movesRemaining();
                        }
                    }*/
                    
                    //public void mouseEntered(MouseEvent e) {
                    //    boxes[1][1].setColorHighlight(boxes[1][1].colorIndex);
                    //}
                    //public void mouseExited(MouseEvent e) {
                    //    boxes[1][1].setColor(boxes[1][1].colorIndex);
                    //}
               

                boxes[i][j].label = tempLabel;
                boxes[i][j].setColor(random.nextInt(numColours));
                int originalColor = boxes[1][1].colorIndex;
                c.gridy = i+1;
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
            gridHeight = 16;
            gridWidth = 16;
            gameSize = "small";
            resetGame();
        });
        JMenuItem mediumItem = new JMenuItem("Medium");
        mediumItem.setMnemonic(KeyEvent.VK_M);
        mediumItem.addActionListener(e -> {
            gridHeight = 24;
            gridWidth = 24;
            gameSize = "medium";
            resetGame();
        });
        JMenuItem hardItem = new JMenuItem("Large");
        hardItem.setMnemonic(KeyEvent.VK_L);
        hardItem.addActionListener(e -> {
            gridHeight = 32;
            gridWidth = 32;
            gameSize = "large";
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

    public static void headerGUI() {
        for (int i = 0; i < numColours; i++) {
            JLabel tempLabel = new JLabel("", SwingConstants.CENTER);
            tempLabel.setOpaque(true);
            tempLabel.setBackground(boxColor[i]);
            tempLabel.setPreferredSize(new Dimension(boxSize, boxSize));
            int j = i;
            tempLabel.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (j != boxes[1][1].colorIndex) {
                        floodColor(j);
                        frame.getContentPane().repaint();
                        currentMoves++;
                        movesRemaining();
                    }
                }
            });
            
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(5, 5, 5, 5);
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.fill = GridBagConstraints.BOTH;
            
            if (gameSize == "small") {
                c.gridx = 2*i+1;
                c.gridwidth = 2;
            } else if (gameSize == "medium") {
                c.gridx = 3*i+1;
                c.gridwidth = 3;
            } else if (gameSize == "large") {
                c.gridx = 4*i+1;
                c.gridwidth = 4;
            }
            
            c.gridy = 0;
            
            c.gridheight = 1;
            panel.add(tempLabel, c);
            
        } 
        /*           
        headerLabel = new JLabel("", SwingConstants.CENTER);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(175,175,175));
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 5, 0);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = gridWidth + 2;
        panel.add(headerLabel, c);
        */
    }
    
    public static void footerGUI() {
        footerLabel = new JLabel("", SwingConstants.CENTER);
        footerLabel.setOpaque(true);
        footerLabel.setBackground(new Color(205,205,205));
        
        if (gameSize == "small") {
            footerLabel.setFont(new Font("Lato Bold", Font.BOLD, 16));
        } else if (gameSize == "medium") {
            footerLabel.setFont(new Font("Lato Bold", Font.BOLD, 18));
        } else {
            footerLabel.setFont(new Font("Lato Bold", Font.BOLD, 22));
        }
        movesRemaining();
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 5, 0);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = gridHeight + 3;
        c.gridwidth = gridWidth + 2;
        panel.add(footerLabel, c);
    }

    public static void movesRemaining() {
        if (gameOverCheck()) {
            footerLabel.setText("You win in " + currentMoves + " moves!");
        }
        else {
            footerLabel.setText("Moves " + currentMoves);
        }
        
    }

    public static void createBorders(GridBagConstraints c) {
        JLabel borderLabelTop = new JLabel("", SwingConstants.CENTER);
        borderLabelTop.setOpaque(true);
        borderLabelTop.setPreferredSize(new Dimension(1, boxSize / 2));
        borderLabelTop.setBackground(Color.BLACK);
        
        c.insets = new Insets(0, 10, 0, 10);
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = gridWidth + 2;
        c.gridheight = 1;
        panel.add(borderLabelTop, c);

        JLabel borderLabelLeft = new JLabel("", SwingConstants.CENTER);
        borderLabelLeft.setOpaque(true);
        borderLabelLeft.setPreferredSize(new Dimension(boxSize / 2, 1));
        borderLabelLeft.setBackground(Color.BLACK);
        c.insets = new Insets(0, 10, 0, 0);
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridheight = gridHeight + 1;
        panel.add(borderLabelLeft, c);
        c.insets = new Insets(0, 0, 0, 0);
        JLabel borderLabelBottom = new JLabel("", SwingConstants.CENTER);
        borderLabelBottom.setOpaque(true);
        borderLabelBottom.setPreferredSize(new Dimension(1, boxSize / 2));
        borderLabelBottom.setBackground(Color.BLACK);
        c.insets = new Insets(0, 10, 0, 10);
        c.gridy = gridHeight + 2;
        c.gridx = 0;
        c.gridwidth = gridWidth + 2;
        c.gridheight = 1;
        panel.add(borderLabelBottom, c);

        JLabel borderLabelRight = new JLabel("", SwingConstants.CENTER);
        borderLabelRight.setOpaque(true);
        borderLabelRight.setPreferredSize(new Dimension(boxSize / 2, 1));
        borderLabelRight.setBackground(Color.BLACK);
        c.insets = new Insets(0, 0, 0, 10);
        c.gridy = 1;
        c.gridx = gridWidth + 1;
        c.gridwidth = 1;
        c.gridheight = gridHeight + 1;
        panel.add(borderLabelRight, c);
        c.insets = new Insets(0, 0, 0, 0);
    }

    public static void floodColor(int color) {
        int[] SURROUNDED_TILES_X = {0, 1, 0, -1};
        int[] SURROUNDED_TILES_Y = {-1, 0, 1, 0};
        int y, x;
        int tempY, tempX;
        int[][] queue = new int[gridWidth * gridHeight][2];
        boolean[][] visited = new boolean[gridHeight + 1][gridWidth + 1];
        int queueIndex = 0;
        int queueLength = 1;
        int originalColor = boxes[1][1].colorIndex;

        queue[0][0] = 1;
        queue[0][1] = 1;
        for (int i = 1; i <= gridHeight; i++) {
            for (int j = 1; j <= gridWidth; j++) {
                visited[i][j] = false;
            }
        }
        visited[1][1] = true;

        while (queueIndex < queueLength) {
            y = queue[queueIndex][0];
            x = queue[queueIndex][1];
            boxes[y][x].setColor(color);

            for (int i = 0; i < 4; i++) {
                tempY = y + SURROUNDED_TILES_Y[i];
                tempX = x + SURROUNDED_TILES_X[i];
                if (tempY < 1 || tempY > gridHeight || tempX < 1 || tempX > gridWidth) continue;

                if (boxes[tempY][tempX].colorIndex == originalColor && !visited[tempY][tempX]) {
                    visited[tempY][tempX] = true;

                    queue[queueLength][0] = tempY;
                    queue[queueLength][1] = tempX;
                    queueLength++;
                }
            }
            queueIndex++;
        }
    }
    
    public static boolean gameOverCheck() {
        int color = boxes[1][1].colorIndex;
        for (int i = 1; i <= gridHeight; i++) {
            for (int j = 1; j <= gridWidth; j++) {
                if (boxes[i][j].colorIndex != color) {
                    return false;
                }
            }
        }
        return true;
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
    
    public void setColorHighlight(int newColorIndex) {
        label.setBackground(Main.boxColorHighlight[colorIndex]);
    }
}
