import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.BorderFactory.*;
import javax.swing.Timer;
import java.util.concurrent.*;

/**
* <h1>Flood-It!</h1>
* The goal of Flood-It is to flood the entire screen with
* a single colour by changing the flood colour to absorb other colours.
* <p>
* Click the colour blocks at the top of the screent to
* change the flood colour, also use the AI Solve function
* in the File menu to see the computer solve it itself!
*
* @author  Iain Griesdale
* @version 1.0
* @since   2021-01-26 
*/

public class Main {
    public static JPanel panel;
    public static JFrame frame = new JFrame("Flood-It");
    public static JLabel footerLabel;
    public static JLabel playAgainLabel;
    public static JLabel headerLabel;
    public static Timer timer;

    public static int gridWidth = 32;
    public static int gridHeight = 32;
    public static int boxSize = 20;
    public static String gameSize = "large";

    public static int currentMoves = 0;
    public static int maxMoves = 61;

    public static int numColours = 8;
    
    //all colours of the tiles
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

    public static Box[][] boxes = new Box[gridHeight + 2][gridWidth + 2];
    
    
    public static void main(String[] args) {
        new HowToPlay();
        doMachineSolve();
        resetGame();
    }

    /**
   * This method is used to initialize all the game elements
   * @return Nothing
   */
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
    
    /**
   * This method initializes the grid GUI and applies
   * a random colour to each tile in the grid
   * @return Nothing
   */
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
        //create the boxes in the grid with a random colour
        for (int i = 1; i <= gridHeight; i++) {
            for (int j = 1; j <= gridWidth; j++) {
                Box tempBox = boxes[i][j];
                

                JLabel tempLabel = new JLabel("", SwingConstants.CENTER);
                tempLabel.setOpaque(true);
                tempLabel.setPreferredSize(new Dimension(boxSize, boxSize));

                boxes[i][j].label = tempLabel;
                boxes[i][j].setColor(random.nextInt(numColours));
                int originalColor = boxes[1][1].colorIndex;
                c.gridy = i+1;
                c.gridx = j;
                panel.add(tempLabel, c);
            }
        }

    }
    /**
   * This method creates the frame 
   */
    public static void startGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
    /**
   * This method creates the menu bar items
   */
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
        
        JMenuItem machineSolveItem = new JMenuItem("AI Solve");
        machineSolveItem.setMnemonic(KeyEvent.VK_S);
        machineSolveItem.addActionListener(e -> timer.start());
        fileMenu.add(machineSolveItem);

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

        //allows user to choose how many colours are in the game
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
    /**
   * This method fills the boxes[] array with the Box objects
   * @see class Box
   */
    public static void initBoxes() {
        for (int i = 1; i <= gridHeight; i++) {
            for (int j = 1; j <= gridWidth; j++) {
                //fills boxes[][] array with Box objects
                boxes[i][j] = new Box();
            }
        }
    }

    /**
   * This method creates the row of labels
   * at the top of the screen
   */
    public static void headerGUI() {
        for (int i = 0; i < numColours; i++) {
            JLabel tempLabel = new JLabel("", SwingConstants.CENTER);
            tempLabel.setOpaque(true);
            tempLabel.setBackground(boxColor[i]);
            tempLabel.setPreferredSize(new Dimension(boxSize, boxSize));
            int j = i;
            //get user input and flood the game with the selected colour
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
            
            //change size of labels based on window size
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
    }
    
    /**
   * This label is the label that shows the 
   * number of moves the player has made
   * @see movesRemaining
   */
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
    
    /**
   * This method updates the footer with how 
   * many moves the play has made for it to display
   * @see footerGUI
   */
    public static void movesRemaining() {
        if (gameOverCheck()) {
            footerLabel.setText("You win in " + currentMoves + " moves!");
            timer.stop();
        }
        else {
            footerLabel.setText("Moves " + currentMoves);
        }
        
    }

    /**
   * Creates the black border surrounding
   * the playing area
   * @param c This is the GridBagConstraints object
   */
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
    
    /**
   * This method is what floods the screen 
   * with the colour the player selects
   * @param color This is colour the player selects 
   */
    public static void floodColor(int color) {
        int[] SURROUNDED_TILES_X = {0, 1, 0, -1};
        int[] SURROUNDED_TILES_Y = {-1, 0, 1, 0};
        int y, x;
        int tempY, tempX;
        int[][] queue = new int[gridWidth * gridHeight][2]; //store all y/x values 
        boolean[][] visited = new boolean[gridHeight + 1][gridWidth + 1]; //store if each tile has been visited
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
        
        //queue up all tiles that will change colour
        while (queueIndex < queueLength) {
            y = queue[queueIndex][0];
            x = queue[queueIndex][1];
            boxes[y][x].setColor(color);
            
            //check surrounding tiles of queued tile
            for (int i = 0; i < 4; i++) {
                tempY = y + SURROUNDED_TILES_Y[i];
                tempX = x + SURROUNDED_TILES_X[i];
                if (tempY < 1 || tempY > gridHeight || tempX < 1 || tempX > gridWidth) continue;
                
                //if not queued tile is the same colour as [1][1] originally then add to queue and mark visited
                if (boxes[tempY][tempX].colorIndex == originalColor && !visited[tempY][tempX]) {
                    visited[tempY][tempX] = true;

                    queue[queueLength][0] = tempY;
                    queue[queueLength][1] = tempX;
                    queueLength++;
                }
            }
            //continue iterating through the queue
            queueIndex++;
        }
    }
    
    /**
   * This checks if the game is over by
   * checking if all the boxes are the 
   * same colour
   * @return boolean this is true if the game is over and false otherwise
   */
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
    /**
   * This method automatically solves the game
   * by checking all surrounding boxes and
   * changing the colour based off the surrounding colours
   */
    public static void machineSolve() {
        int[] colorCount = { 0, 0, 0, 0, 0, 0, 0, 0};
        int[] SURROUNDED_TILES_X = {0, 1, 0, -1};
        int[] SURROUNDED_TILES_Y = {-1, 0, 1, 0};
        int y, x;
        int tempY, tempX;
        int[][] queue = new int[gridWidth * gridHeight][2]; //store all y/x values 
        boolean[][] visited = new boolean[gridHeight + 1][gridWidth + 1]; //store if each tile has been visited
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
        
        //queue up all tiles that will change colour
        while (queueIndex < queueLength) {
            y = queue[queueIndex][0];
            x = queue[queueIndex][1];
            
            //check surrounding tiles of queued tile
            for (int i = 0; i < 4; i++) {
                tempY = y + SURROUNDED_TILES_Y[i];
                tempX = x + SURROUNDED_TILES_X[i];
                if (tempY < 1 || tempY > gridHeight || tempX < 1 || tempX > gridWidth) continue;
                if (!visited[tempY][tempX]) {
                    visited[tempY][tempX] = true;
                    
                    if (boxes[y][x].colorIndex == originalColor) {
                        queue[queueLength][0] = tempY;
                        queue[queueLength][1] = tempX;
                        queueLength++;
                        if (boxes[tempY][tempX].colorIndex != originalColor) {
                            colorCount[boxes[tempY][tempX].colorIndex]++;
                        }
                    } else {
                        if (boxes[tempY][tempX].colorIndex == boxes[y][x].colorIndex) {
                            queue[queueLength][0] = tempY;
                            queue[queueLength][1] = tempX;
                            queueLength++;
                            colorCount[boxes[tempY][tempX].colorIndex]++;
                        }
                    }
                }
                //if not queued tile is the same colour as [1][1] originally then add to queue and mark visited
                
                
                    
            }
            //continue iterating through the queue
            queueIndex++;
        }
        int maxCount = 0;
        int maxCountIndex = 0;
        for (int i = 0; i < colorCount.length; i++) {
            if (colorCount[i] > maxCount) {
                maxCount = colorCount[i];
                maxCountIndex = i;
            }
        }
        
        floodColor(maxCountIndex);
        frame.getContentPane().repaint();
        currentMoves++;
        movesRemaining();
    }
    /**
   * This method starts a periodic timer for
   * the machine solve function so that the player
   * can visually see the game being solved
   * @see machineSolve()
   */
    public static void doMachineSolve() {
        timer = new Timer(250, e -> {
            machineSolve();
        });
        timer.setInitialDelay(0);
    }
}

//class for each box to set colour and hold the label and index
class Box {
    public int colorIndex;
    public JLabel label;

    public Box() {
    }
    
    /**
   * This method sets the colour of the box
   * @param newColorIndex This is the colour in colorIndex to change the box to
   */
    public void setColor(int newColorIndex) {
        colorIndex = newColorIndex;
        label.setBackground(Main.boxColor[colorIndex]);
    }

}

class HowToPlay {
    JFrame f;
    public HowToPlay() {
        f = new JFrame();
        JOptionPane.showMessageDialog(f,"Fill the screen with one colour to win by clicking the colours at the top!");
    }
}
