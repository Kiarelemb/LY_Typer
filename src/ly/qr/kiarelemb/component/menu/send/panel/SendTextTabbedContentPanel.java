package ly.qr.kiarelemb.component.menu.send.panel;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.ComboBox;
import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.send.SendWindow;
import method.qr.kiarelemb.utils.QRArrayUtils;
import swing.qr.kiarelemb.component.basic.QRComboBox;
import swing.qr.kiarelemb.component.combination.QRTabbedContentPanel;
import swing.qr.kiarelemb.component.event.QRItemEvent;
import swing.qr.kiarelemb.component.event.QRTabSelectEvent;
import swing.qr.kiarelemb.inter.QRActionRegister;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className SendTextTabbedContentPanel
 * @description TODO
 * @create 2024/3/31 22:19
 */
public class SendTextTabbedContentPanel extends QRTabbedContentPanel {
    protected final SendWindow window;
    private final int width;
    private final int height;
    //TODO 在此处设置发文时，跟打结束后的操作
    private static final QRActionRegister E = e -> {

    };

    public SendTextTabbedContentPanel(SendWindow window, int width, int height) {
        this.window = window;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void thisTabSelectChangeAction(QRTabSelectEvent event) {
        SwingUtilities.invokeLater(() -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            window.setSize(width, height);
            window.setLocationRelativeTo(MainWindow.INSTANCE);
        });
    }

    protected static QRComboBox getParaWordComboBox() {
        String[] wordArray = {"50", "100", "200", "500"};
        int wordNum = Keys.intValue(Keys.TEXT_SEND_START_WORD_NUM);
        String numString = String.valueOf(wordNum);
        int selectIndex = QRArrayUtils.objectIndexOf(wordArray, numString);
        if (selectIndex == -1) {
            wordArray = new String[]{"50", "100", "200", "500", numString};
            selectIndex = wordArray.length - 1;
        }
        QRComboBox paraWordCbx = new QRComboBox(wordArray) {
            @Override
            protected void itemChangedAction(QRItemEvent e) {
                SettingsItem.CHANGE_MAP.put(Keys.TEXT_SEND_START_WORD_NUM, e.after());
            }
        };
        paraWordCbx.setSelectedIndex(selectIndex);
        paraWordCbx.setEditable(true);
        return paraWordCbx;
    }

    protected static QRComboBox getStartParaComboBox() {
        return new ComboBox(0, Keys.TEXT_SEND_START_PARA, "1", "随机段号");
    }
}