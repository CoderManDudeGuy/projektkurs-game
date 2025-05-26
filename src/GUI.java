import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI implements ActionListener {
    public JFrame mainFrame = new JFrame();
    public GamePanel gamePanel = new GamePanel();
    private final JPanel south = new JPanel(new FlowLayout());
    private final JPanel north = new JPanel(new FlowLayout());
    private final JPanel east = new JPanel(new FlowLayout());
    private final JPanel west = new JPanel(new FlowLayout());
    private final JButton[] buttons = new JButton[4];
    private final JTextArea taAnzeige = new JTextArea();
    private final JLabel lbTitel = new JLabel();
    private final Control control;
    public final int FRAME_WIDTH = 850;
    public final int FRAME_HEIGHT = 850;
    String version = "0.9";

    public GUI(Control pControl) {
        control = pControl;
        createGUI();
    }

    private void createGUI() {
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - mainFrame.getSize().width) / 2;
        int y = (d.height - mainFrame.getSize().height) / 2;
        mainFrame.setLocation(x, y);
        mainFrame.setTitle("Spiele-Entwicklung Version " + version);
        mainFrame.setResizable(false);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(gamePanel, BorderLayout.CENTER);
        mainFrame.add(south, BorderLayout.SOUTH);
        mainFrame.add(east, BorderLayout.EAST);
        mainFrame.add(west, BorderLayout.WEST);
        mainFrame.add(north, BorderLayout.NORTH);
        east.add(taAnzeige);
        taAnzeige.setBorder(BorderFactory.createLineBorder(Color.black));
        taAnzeige.append("Score: ");
        taAnzeige.setEditable(false);
        lbTitel.setFont(new Font("Dialog", 10, 30));
        north.add(lbTitel);

        String[] btNames = {"Exit", "Items Reset", "Map Reset", "Button4"};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(btNames[i]);
            buttons[i].addActionListener(this);
            south.add(buttons[i]);
        }
        mainFrame.addKeyListener(Control.keyManager);
        mainFrame.setVisible(true);
        mainFrame.requestFocus();
    }

    public Point getLocation() {
        return new Point(mainFrame.getLocation());
    }

    public void actionPerformed(ActionEvent evt) {
        JButton temp = (JButton) evt.getSource();
        switch (temp.getText()) {
            case "Exit":
                System.exit(0);
                mainFrame.requestFocus();
                break;
            case "Items Reset":
                break;
            case "Map Reset":
                break;
            case "Button4":
                //
                break;
        }
    }

    class GamePanel extends JPanel {

        public GamePanel() {
            super();
            this.setBorder(BorderFactory.createLineBorder(Color.black, 4));
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            if (Control.map != null) {
                Control.map.renderMap(g2d);
            }

            if (Control.player != null) {
                Control.player.paintMe(g2d);
            }

            if (Control.testEnemy != null){
                if (Control.testEnemy.isVisible) {
                    Control.testEnemy.paintMe(g2d);
                }

            }
        }
    }
}

