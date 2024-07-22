package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.TipPanel;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.basic.QRLabel;
import swing.qr.kiarelemb.basic.QRPanel;
import swing.qr.kiarelemb.combination.QRTransparentSplitPane;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.utils.QRComponentUtils;

import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-27 22:33
 **/
public class SplitPane extends QRTransparentSplitPane {
    public final SplitTipPanel tipPanel;
    public static final SplitPane SPLIT_PANE = new SplitPane();
    public final QRPanel topPanel;
    public final QRPanel bottomPanel;

    private SplitPane() {
//        super(JSplitPane.VERTICAL_SPLIT);
        super();

        int value;
        try {
            value = Keys.intValue(Keys.WINDOW_SPLIT_WEIGHT);
        } catch (Exception e) {
            value = 300;
            QRSwing.setGlobalSetting(Keys.WINDOW_SPLIT_WEIGHT, value);
        }
//        setUI(new SplitPaneUI());
        setDividerLocation(value);

        //底部的面板需要放词提
        this.tipPanel = new SplitTipPanel();
        topPanel = new QRPanel(false, new BorderLayout());
        bottomPanel = new QRPanel(false, new BorderLayout());
        topPanel.setPreferredSize(new Dimension(100, value));


        QRLabel tip = new QRLabel() {
            final Font f = QRColorsAndFonts.DEFAULT_FONT_MENU.deriveFont((float) Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK));

            @Override
            public void paintBorder(Graphics g) {

                String[] split = "F2 | 跟打器发文\nF4 | 载文\nF5 | 换群\nCtrl V | 粘贴载文\nCtrl Z | 设置".split("\n");
                int height = getDividerLocation();
                int size = f.getSize();
                float rest = height - 5 * size;
                float div = rest / 6;
                float[] y = {(-size - div) * 2, -size - div, 0, size + div, (size + div) * 2};
                for (int i = 0; i < split.length; i++) {
                    String s = split[i];
                    QRComponentUtils.componentStringDraw(this, g, s, f, QRColorsAndFonts.TEXT_COLOR_FORE, height / 2f + y[i]);
                }
            }
        };

        topPanel.add(TextViewPane.TEXT_VIEW_PANE.addScrollPane(1), BorderLayout.CENTER);
        bottomPanel.add(TypeTabbedPane.TYPE_TABBED_PANE, BorderLayout.CENTER);

        setTopComponent(tip);
        setBottomComponent(bottomPanel);
//        add(topPanel, BorderLayout.CENTER);
//        add(bottomPanel, BorderLayout.SOUTH);

//        setTopComponent(tip);
//        setTopComponent(tip);
//        setBottomComponent(bottomPanel);
//        setBottomComponent(new QRTextPane().addScrollPane(1));
        //更新编码提示的位置
        updateTipPaneLocation();

        QRActionRegister repaintAction = e -> divider.repaint();
        TyperTextPane.TYPER_TEXT_PANE.addTypeActions(repaintAction);
        TextViewPane.TEXT_VIEW_PANE.addSetTextFinishedAction(repaintAction);

//        setOpaque(true);
    }

    private boolean notSwitched = true;

    public void switchTipPanel() {
        if (notSwitched) {
            notSwitched = false;
            setTopComponent(topPanel);
        }
    }

    /**
     * 更新编码提示的位置
     */
    public void updateTipPaneLocation() {
        // 若未启用词提面板
        if (!Keys.boolValue(Keys.TEXT_TIP_PANEL_ENABLE)) {
            if (this.tipPanel.getParent() != null) {
                topPanel.remove(this.tipPanel);
                bottomPanel.remove(this.tipPanel);
                this.revalidate();
                this.repaint();
            }
            return;
        }
        this.tipPanel.pack();
        // 编码提示面板的位置，0 看打区上方，1 （默认）看打区下方，2 跟打区上方，3 跟打区下方
        int value = Keys.intValue(Keys.TEXT_TIP_PANEL_LOCATION);
        this.tipPanel.setOnNorth(value % 2 == 0);
        // 获取分割线位置
//        final int i = getDividerLocation();
        if (value < 2) {
            topPanel.add(this.tipPanel, value == 0 ? BorderLayout.NORTH : BorderLayout.SOUTH);
            // 设置顶层大小能避免分割线位置变化
//            topPanel.setPreferredSize(new Dimension(100, i - this.tipPanel.getHeight()));
        } else {
            bottomPanel.add(this.tipPanel, value == 3 ? BorderLayout.SOUTH : BorderLayout.NORTH);
            // 设置顶层大小能避免分割线位置变化
//            bottomPanel.setPreferredSize(new Dimension(100, i));
        }
    }

    @Override
    protected void paintDivider(Graphics2D g) {
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        super.paintDivider(g);
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, QRSwing.windowImageSet ? 0.5f : 1f));
        g.setColor(QRColorsAndFonts.LINE_COLOR);
        Dimension size = divider.getSize();
        g.fillRect(0, 0, size.width, size.height);
        // 绘制当前跟打进度条
        if (TextLoad.TEXT_LOAD != null && TypingData.currentTypedIndex > 0) {
            g.setColor(QRColorsAndFonts.DEFAULT_COLOR_LABEL);
            int width = size.width * TypingData.currentTypedIndex / TextLoad.TEXT_LOAD.wordsLength();
            g.fillRect(0, size.height / 2, width, size.height - size.height / 2);
        }
    }

    @Override
    public void componentFresh() {
        super.componentFresh();
        if (this.tipPanel != null) {
            updateTipPaneLocation();
        }
    }

    public static class SplitTipPanel extends TipPanel {
        private boolean isOnNorth = false;

        @Override
        public void tipUpdate() {
            super.tipUpdate();
            borderUpdate();
        }

        public void setOnNorth(boolean onNorth) {
            this.isOnNorth = onNorth;
        }

        void borderUpdate() {
            if (this.isOnNorth) {
                setBorder(new MatteBorder(0, 0, 1, 0, QRColorsAndFonts.BORDER_COLOR));
            } else {
                setBorder(new MatteBorder(1, 0, 0, 0, QRColorsAndFonts.BORDER_COLOR));
            }
        }

        @Override
        public void componentFresh() {
            super.componentFresh();
            borderUpdate();
        }
    }
}