package ly.qr.kiarelemb.component.menu.send.panel;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.ComboBox;
import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.send.SendWindow;
import ly.qr.kiarelemb.text.send.TextSendManager;
import ly.qr.kiarelemb.text.send.data.TypedData;
import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRLoggerUtils;
import swing.qr.kiarelemb.component.basic.QRComboBox;
import swing.qr.kiarelemb.component.combination.QRTabbedContentPanel;
import swing.qr.kiarelemb.component.event.QRItemEvent;
import swing.qr.kiarelemb.component.event.QRTabSelectEvent;
import swing.qr.kiarelemb.inter.QRActionRegister;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className SendTextTabbedContentPanel
 * @description TODO
 * @create 2024/3/31 22:19
 */
public class SendTextTabbedContentPanel extends QRTabbedContentPanel {
    private static final Logger logger = QRLoggerUtils.getLogger(SendTextTabbedContentPanel.class);
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
            window.setSize(width, height);
            window.setLocationRelativeTo(MainWindow.INSTANCE);
        });
    }

    protected String filePath;
    protected QRComboBox startParaCbx;
    protected QRComboBox paraWordCbx;

    protected void startSendAction(boolean randomPick) {
        try {
            String fileCrc = QRFileUtils.getCrc32(filePath);
            int words = Math.toIntExact(QRFileUtils.getFileWordNumWithUTF8(filePath));
            int paraWords = Integer.parseInt(paraWordCbx.getText());
            TypedData sendData = new TypedData(QRFileUtils.getFileName(filePath), fileCrc, 0, 0, words, words,
                    paraWords, startParaCbx.getSelectedIndex() == 1);
            sendData.setRandomPick(randomPick);
            TextSendManager.setTypedData(sendData);
            window.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                logger.log(Level.INFO, "设置每段发文字数：" + e.after());
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