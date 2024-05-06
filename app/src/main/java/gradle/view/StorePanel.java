package gradle.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.google.common.base.Strings;

import gradle.controller.Constants;
import gradle.controller.GameSettings;
import gradle.controller.StoreController;
import gradle.model.EpsilonModel;
import gradle.view.charecretsView.EpsilonView;

public class StorePanel extends JPanel {
    private static StorePanel INSTANCE;
    private static final int BOX_WIDTH = (int) Constants.STORE_PANEL_DIMENSION.getWidth() / 4;
    private static final int IMAGE_SIZE = (int) (BOX_WIDTH - BOX_WIDTH / 4);
    private static final int BOX_HEIGHT = (int) (Constants.STORE_PANEL_DIMENSION.getHeight()
            - Constants.STORE_PANEL_DIMENSION.getHeight() / 3);
    private static final Color BORDER_COLOR = Color.RED;

    double speed;

    String[] images = {
            "app/src/main/java/gradle/assets/icons/add-health.png",
            "app/src/main/java/gradle/assets/icons/3-bullets.png",
            "app/src/main/java/gradle/assets/icons/wave.png"
    };
    String[] labels = {
            "+10 HP",
            "3 shots",
            "wave"
    };

    public static int[] xp = { 5, 75, 100 };

    public static boolean[] enabled = { true, true, true };
    private int epsilonXp;

    private StorePanel() throws HeadlessException {
        setOpaque(true);
        setBackground(new Color(5, 5, 5, 210));
        setBorder(new LineBorder(Color.yellow));
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.GREEN),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        setSize(new Dimension((int) Constants.STORE_PANEL_DIMENSION.getWidth(), 0));
        setLocationToCenter(GamePanel.getINSTANCE());
        setFocusable(true);
        this.addKeyListener(GamePanel.getINSTANCE().getKeyListeners()[0]);
        // setVisible(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        for (int i = 0; i < 3; i++) {
            add(createBoxPanel(i));
        }

        GamePanel.getINSTANCE().add(this);
    }

    private JPanel createBoxPanel(int boxIndex) {
        JPanel boxContainer = new JPanel();
        boxContainer.setBackground(new Color(0, 0, 0, 0));
        boxContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        BoxPanel boxPanel = new BoxPanel(boxIndex);
        boxPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (enabled[boxIndex]) {
                    GameSettings.isStore = false;
                    GameSettings.isPause = false;
                    StoreController.handleStore(boxIndex);
                } else {
                    String error_text = "you can't buy this item you wanna " + String.valueOf(xp[boxIndex] - epsilonXp)
                            + " XP more";
                    JOptionPane.showMessageDialog(null, error_text);
                }
            }
        });
        boxContainer.add(boxPanel);

        Dimension containerSize = new Dimension(BOX_WIDTH + 20, BOX_HEIGHT + 20);
        boxContainer.setPreferredSize(containerSize);
        boxContainer.setMaximumSize(containerSize);
        boxContainer.setMinimumSize(containerSize);

        return boxContainer;
    }

    private class BoxPanel extends JPanel {

        private int boxIndex;
        private boolean hovered = false;
        private Border defaultBorder;

        public BoxPanel(int boxIndex) {
            this.boxIndex = boxIndex;
            setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));
            setBackground(new Color(50, 50, 50, 100));
            defaultBorder = getBorder();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (enabled[boxIndex]) {
                        hovered = true;
                        setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_COLOR),
                                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    setBorder(defaultBorder);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g.setColor(Color.white);
            FontMetrics fm = g.getFontMetrics();
            String textLabel = labels[boxIndex];
            g.setFont(new Font("Raleway ExtraBold", Font.BOLD, 14));
            g.drawString(textLabel, BOX_WIDTH / 2 - fm.stringWidth(textLabel) / 2, 20);
            Image healthIcon = new ImageIcon(images[boxIndex]).getImage();
            g.drawImage(healthIcon, BOX_WIDTH / 2 - IMAGE_SIZE / 2, 30, IMAGE_SIZE, IMAGE_SIZE,
                    null);

            g.setFont(new Font("Consolas", Font.PLAIN, 17));
            String XP_Label = String.valueOf(xp[boxIndex]) + " XP   ";
            g.setColor(Color.GREEN);
            g.drawString(XP_Label, BOX_WIDTH / 2 - fm.stringWidth(XP_Label) / 2, IMAGE_SIZE + 70);
            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(BOX_WIDTH, BOX_HEIGHT);
        }

    }

    public void showOrHidePanel() {
        if (GameSettings.isStore) {
            EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
            epsilonXp = epsilonModel.XP;
            if (getHeight() <= Constants.STORE_PANEL_DIMENSION.getHeight())
                setSize(new Dimension(getWidth(), getHeight() + 15));
        } else {
            if (getHeight() > 0)
                setSize(new Dimension(getWidth(), getHeight() - 15));
        }

        setLocationToCenter(GamePanel.getINSTANCE());

    }

    public void setLocationToCenter(GamePanel gamePanel) {
        setLocation(gamePanel.getWidth() / 2 - getWidth() / 2, gamePanel.getHeight() / 2 - getHeight() / 2);
    }

    public static StorePanel getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StorePanel();
        }
        return INSTANCE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);

        FontMetrics fm = g.getFontMetrics();
        String text = String.valueOf(epsilonXp) + " XP  ";
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.drawString(text, getWidth() / 2 - fm.stringWidth(text) / 2, BOX_HEIGHT + 80);
    }

}
