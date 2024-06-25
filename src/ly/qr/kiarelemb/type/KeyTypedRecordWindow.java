package ly.qr.kiarelemb.type;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.KeyBoardPanel;
import ly.qr.kiarelemb.data.KeyTypedRecordData;
import swing.qr.kiarelemb.component.combination.QRTabbedContentPanel;
import swing.qr.kiarelemb.component.combination.QRTabbedPane;
import swing.qr.kiarelemb.window.basic.QRDialog;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className KeyTypedRecordWindow
 * @description TODO
 * @create 2024/4/13 14:40
 */
public class KeyTypedRecordWindow extends QRDialog {
    public KeyTypedRecordWindow() {
        super(MainWindow.INSTANCE);

        setTitle("键盘热力图");
        setTitlePlace(SwingConstants.CENTER);
        mainPanel.setLayout(new BorderLayout());


        QRTabbedPane tabbedPane = new QRTabbedPane();

        QRTabbedContentPanel today = new QRTabbedContentPanel();
        today.setLayout(new BorderLayout());
        today.add(new KeyBoardPanel(KeyTypedRecordData.KEY_TODAY_RECORD_DATA.keyMap));
        QRTabbedContentPanel total = new QRTabbedContentPanel();
        total.setLayout(new BorderLayout());
        total.add(new KeyBoardPanel(KeyTypedRecordData.KEY_TOTAL_RECORD_DATA.keyMap));

        tabbedPane.addTab("今日跟打记录", today);
        tabbedPane.addTab("总计跟打记录", total);
        tabbedPane.setSelectedTab(KeyTypedRecordData.KEY_TODAY_RECORD_DATA.keyMap.size() > 0 ? 0 : 1);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        setSize(1360, 600);
    }
}