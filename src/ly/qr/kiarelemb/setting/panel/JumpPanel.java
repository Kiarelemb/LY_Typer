package ly.qr.kiarelemb.setting.panel;

import method.qr.kiarelemb.utils.QRFontUtils;
import swing.qr.kiarelemb.basic.QRLabel;
import swing.qr.kiarelemb.basic.QRPanel;
import swing.qr.kiarelemb.combination.QRTreeTabbedPane;
import swing.qr.kiarelemb.utils.QRComponentUtils;
import swing.qr.kiarelemb.utils.QRHandLabel;

import java.awt.event.MouseEvent;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-31 15:10
 **/
public class JumpPanel extends QRPanel {
    public JumpPanel(QRTreeTabbedPane treeTabbedPane, SettingPanel... panels) {
        setLayout(null);
        for (int i = 0; i < panels.length; i++) {
            QRPanel panel = panels[i];
            QRLabel label = new QRHandLabel(panel.getName()) {
                @Override
                public void clickAction(MouseEvent e) {
                    treeTabbedPane.jumpTo(panel);
                }
            };
            int width = QRFontUtils.getTextInWidth(label, panel.getName());
            QRComponentUtils.setBoundsAndAddToComponent(this, label, 50, 30 + i * 50, width, 30);
        }
    }
}