import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {
    public static JPanel panel;
    public static JFrame frame = new JFrame("Flood-It");

    public static int gridWidth = 20;
    public static int gridHeight = 20;
    public static int boxSize = 20;

    public static Color[] boxColor = {
            new Color(255, 0, 255),
            new Color(255, 0, 0),
            new Color(0, 255, 255),
            new Color(255, 255, 0),
            new Color(0, 187, 0),
            new Color(255, 204, 102),
            new Color(102, 102, 255),
            new Color(128, 0, 128),
    };

    public static Box[][] boxes = new Box[gridHeight][gridWidth];

    public static void main(String[] args) {
        initBoxes();
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
        c.insets = new Insets(0, 0, 0, 0);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        Random random = new Random();
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                JLabel tempLabel = new JLabel("", SwingConstants.CENTER);
                tempLabel.setOpaque(true);
                tempLabel.setPreferredSize(new Dimension(boxSize, boxSize));

                boxes[i][j].label = tempLabel;
                boxes[i][j].setColor(random.nextInt(boxColor.length));

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

    public static void initBoxes() {
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                boxes[i][j] = new Box();
            }
        }
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
