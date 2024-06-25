package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.TipPanel;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.basic.QRPanel;
import swing.qr.kiarelemb.component.basic.QRSplitPane;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-27 22:33
 **/
public class SplitPane extends QRSplitPane {
    public final SplitTipPanel tipPanel;
    public static final SplitPane SPLIT_PANE = new SplitPane();

    private SplitPane() {
        super(JSplitPane.VERTICAL_SPLIT);

        setOpaque(false);
        //底部的面板需要放词提
        this.tipPanel = new SplitTipPanel();
        this.tipPanel.setPreferredSize(200, 100);

        setTopComponent(TextPane.TEXT_PANE.addScrollPane());
        setBottomComponent(TyperTextPane.TYPER_TEXT_PANE.addScrollPane());

        int value;
        try {
            value = Keys.intValue(Keys.WINDOW_SPLIT_WEIGHT);
        } catch (Exception e) {
            value = 300;
            QRSwing.setGlobalSetting(Keys.WINDOW_SPLIT_WEIGHT, value);
        }
        setUI(new SplitPaneUI());
        setDividerLocation(value);
        //更新编码提示的位置
        updateTipPaneLocation();
    }

    /**
     * 更新编码提示的位置
     */
    public void updateTipPaneLocation() {
        // 若未启用词提面板
        if (!Keys.boolValue(Keys.TEXT_TIP_PANEL_ENABLE)) {
            if (this.tipPanel.getParent() != null) {
                // 用循环移除 SPLIT_PANE
                QRActionRegister loopToRemove =
                        component -> QRComponentUtils.componentLoop((QRPanel) component, SplitTipPanel.class,
                                e -> ((QRPanel) component).remove((SplitTipPanel) e));
                QRComponentUtils.componentLoop(this, QRPanel.class, loopToRemove::action);
                this.revalidate();
                this.repaint();
                TypingData.windowFresh();
            }
            return;
        }
        this.tipPanel.pack();
        int value = Keys.intValue(Keys.TEXT_TIP_PANEL_LOCATION);
        this.tipPanel.setOnNorth(value % 2 == 0);
        // 获取分割线位置
        final int i = getDividerLocation();
        if (value < 2) {
            setTopComponent(null);
            QRPanel panel = new QRPanel(false);
            panel.setLayout(new BorderLayout());
            panel.add(TextPane.TEXT_PANE.addScrollPane(), BorderLayout.CENTER);
            panel.add(this.tipPanel, value == 0 ? BorderLayout.NORTH : BorderLayout.SOUTH);
            // 设置顶层大小能避免分割线位置变化
            TextPane.TEXT_PANE.addScrollPane().setPreferredSize(new Dimension(100, i - this.tipPanel.getHeight()));
            setTopComponent(panel);
        } else {
            setBottomComponent(null);
            QRPanel panel = new QRPanel(false);
            panel.setLayout(new BorderLayout());
            panel.add(TyperTextPane.TYPER_TEXT_PANE.addScrollPane(), BorderLayout.CENTER);
            panel.add(this.tipPanel, value == 3 ? BorderLayout.SOUTH : BorderLayout.NORTH);
            // 设置顶层大小能避免分割线位置变化
            TextPane.TEXT_PANE.addScrollPane().setPreferredSize(new Dimension(100, i));
            setBottomComponent(panel);
        }
    }

    @Override
    public void componentFresh() {
        super.componentFresh();
        if (this.tipPanel != null) {
            updateTipPaneLocation();
        }
    }

    public class SplitTipPanel extends TipPanel {
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

    static class SplitPaneUI extends BasicSplitPaneUI {

        public SplitPaneUI() {
            // 注册跟打进度条刷新
            QRActionRegister repaintAction = e -> divider.repaint();
            TyperTextPane.TYPER_TEXT_PANE.addTypeActions(repaintAction);
            TextPane.TEXT_PANE.addSetTextFinishedAction(repaintAction);
        }


        @Override
        public BasicSplitPaneDivider createDefaultDivider() {
            return new BasicSplitPaneDivider(this) {
                public void setBorder(Border b) {
                }

                @Override
                public void paint(Graphics g) {
                    g.setColor(QRColorsAndFonts.LINE_COLOR);
                    Dimension size = getSize();
                    g.fillRect(0, 0, size.width, size.height);
                    // 绘制当前跟打进度条
                    if (TextLoad.TEXT_LOAD != null && TypingData.currentTypedIndex > 0) {
                        g.setColor(QRColorsAndFonts.DEFAULT_COLOR_LABEL);
                        int width = size.width * TypingData.currentTypedIndex / TextLoad.TEXT_LOAD.wordsLength();
                        g.fillRect(0, size.height / 2, width, size.height - size.height / 2);
                    }
                    super.paint(g);
                }
            };
        }
    }
}