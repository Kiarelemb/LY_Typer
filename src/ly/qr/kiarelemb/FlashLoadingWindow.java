package ly.qr.kiarelemb;

import ly.qr.kiarelemb.component.data.BallTempData;
import ly.qr.kiarelemb.res.Info;
import method.qr.kiarelemb.utils.QRFontUtils;
import method.qr.kiarelemb.utils.QRSystemUtils;
import method.qr.kiarelemb.utils.QRTimeCountUtil;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRPanel;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.window.basic.QREmptyDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-12 22:52
 **/
public class FlashLoadingWindow extends QREmptyDialog {
    private static class TextLabel extends QRLabel {
        public TextLabel(String text) {
            super(text);
            setFont(QRColorsAndFonts.MENU_ITEM_DEFAULT_FONT.deriveFont(25f));
            setForeground(new Color(235, 235, 235));
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private class BallRollPane extends QRLabel {

        public static final int RIGHT_MARGIN = 650;
        public static final int RADIUS = 8;
        private final int y;
        private final int bollNum = 7;
        private final double[] xes = new double[this.bollNum];
        private QRTimeCountUtil tcd = null;

        public BallRollPane(int y) {
            this.y = y;
            final int delay = 10;
            final int a = 735;
            final int v0 = 530;
            final float timePartA = 0.5f;
            final float timePartB = 1.3f;
            final BallTempData ballTempData = new BallTempData(a, v0, timePartA, timePartB);
            AtomicBoolean flag = new AtomicBoolean(false);
            AtomicReference<Timer> timer = new AtomicReference<>();
            ActionListener action = e -> {
                boolean visible = FlashLoadingWindow.this.isVisible();
                if (flag.get() && !visible) {
                    timer.get().stop();
                }
                if (visible) {
                    flag.set(true);
                    double t = this.tcd.get() / 1000D;
                    for (int j = 0; j < this.bollNum; j++) {
                        this.xes[j] = ballTempData.getX(t - 0.075 * j);
                    }
                    if (this.xes[this.bollNum - 1] + RADIUS > RIGHT_MARGIN) {
                        this.tcd.startTimeUpdate();
                    }
                    repaint();
                }
            };
            timer.set(new Timer(delay, action));
            this.tcd = new QRTimeCountUtil();
            timer.get().start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            final Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setColor(QRColorsAndFonts.LIGHT_GREEN);
            for (double x : this.xes) {
                graphics2D.fillOval((int) x, this.y - RADIUS, RADIUS, RADIUS);
            }
        }
    }

    private final QRLabel loadingInfoLabel;
    private final QRPanel mainPanel;

    public FlashLoadingWindow() {
        super(null, false);
        setIconImage(Info.loadImage(Info.ICON_PNG_PATH).getImage());
        setSize(550, 330);

        this.mainPanel = new QRPanel();
        //255
        this.mainPanel.setLayout(new BorderLayout());
        setContentPane(this.mainPanel);

        QRLabel imageLabel = new BallRollPane(305);
        this.mainPanel.add(imageLabel, BorderLayout.CENTER);
//        flashImage = ImageIO.read(Objects.requireNonNull());
//
        imageLabel.setIcon(Info.loadImage(Info.FLASH_PATH));
//        imageLabel.setIcon(new ImageIcon(FLASH_PATH));
        imageLabel.setLayout(null);

        TextLabel qiLabel = new TextLabel("揽月跟打器");
        qiLabel.setTextRight();

        this.loadingInfoLabel = new QRLabel();
        final Color foreColor = new Color(235, 235, 235);
        this.loadingInfoLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        QRLabel versionLabel = new TextLabel(Info.SOFTWARE_VERSION);
        versionLabel.setForeground(foreColor);
        versionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        versionLabel.setFont(QRFontUtils.getFont("Consolas", 18));


//		qiLabel.setBounds(10, 217, 150, 30);//37
//		versionLabel.setBounds(10, 190, 100, 30);
        qiLabel.setBounds(10, 20, 530, 30);//37
        versionLabel.setBounds(475, 10, 100, 30);
//        loadingInfoLabel.setBounds(10, 297, 250, 23);
        this.loadingInfoLabel.setBounds(10, 5, 450, 23);
//        loadingInfoLabel.setForeground(Color.BLACK);
        this.loadingInfoLabel.setForeground(new Color(235, 235, 235, 200));
//		loadingInfoLabel.setForeground(new Color(0, 0, 0, 90));
        this.loadingInfoLabel.setFont(QRColorsAndFonts.MENU_ITEM_DEFAULT_FONT);


//        imageLabel.add(IconLabel);
        imageLabel.add(qiLabel);
        imageLabel.add(this.loadingInfoLabel);
//		imageLabel.add(versionLabel);

        setAlwaysOnTop(true);
        setCursorWait();
        setLocationRelativeTo(null);
        QRSystemUtils.setWindowNotTrans(this);
        QRSystemUtils.setWindowNotRound(this);
    }

    public void setProgress(String text, int count) {
        this.loadingInfoLabel.setText(text + " (" + count + "/10)");
    }
}