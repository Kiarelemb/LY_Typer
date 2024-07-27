package ly.qr.kiarelemb.type;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.KeyBoardPanel;
import ly.qr.kiarelemb.data.KeyTypedRecordData;
import swing.qr.kiarelemb.combination.QRTabbedPane;
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
        setParentWindowNotFollowMove();
        setTitle("键盘热力图");
        setTitlePlace(SwingConstants.CENTER);
        mainPanel.setLayout(new BorderLayout());

        var tabbedPane = init_tabbedPane();
        tabbedPane.setSelectedTab(!KeyTypedRecordData.KEY_TODAY_RECORD_DATA.keyMap.isEmpty() ? 0 : 1);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        setSize(1360, 610);
    }

    private static QRTabbedPane init_tabbedPane() {
        var todayKeyBoard = new KeyBoardPanel(KeyTypedRecordData.KEY_TODAY_RECORD_DATA.keyMap);
        var totalKeyBoard = new KeyBoardPanel(KeyTypedRecordData.KEY_TOTAL_RECORD_DATA.keyMap);

        var tabbedPane = new QRTabbedPane(BorderLayout.NORTH, FlowLayout.CENTER);

        var today = new KeyBoardPanel.TabContentPanel(todayKeyBoard, totalKeyBoard);
        var total = new KeyBoardPanel.TabContentPanel(totalKeyBoard, todayKeyBoard);

        tabbedPane.addTab("今日跟打记录", today);
        tabbedPane.addTab("总计跟打记录", total);
        return tabbedPane;
    }
}