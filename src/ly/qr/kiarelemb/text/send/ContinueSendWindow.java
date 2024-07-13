package ly.qr.kiarelemb.text.send;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.text.send.data.TypedData;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRMathUtils;
import swing.qr.kiarelemb.basic.QRComboBox;
import swing.qr.kiarelemb.basic.QRLabel;
import swing.qr.kiarelemb.basic.QRRoundButton;
import swing.qr.kiarelemb.basic.QRTextArea;
import swing.qr.kiarelemb.combination.QRListTabbedPane;
import swing.qr.kiarelemb.combination.QRTabbedContentPanel;
import swing.qr.kiarelemb.event.QRTabSelectEvent;
import swing.qr.kiarelemb.utils.QRComponentUtils;
import swing.qr.kiarelemb.window.basic.QRDialog;

import java.awt.*;
import java.util.List;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className ContinueSendWindow
 * @description TODO
 * @create 2024/5/22 下午8:54
 */
public class ContinueSendWindow extends QRDialog {
    private final QRListTabbedPane listTabbedPane;
    private boolean isContinue = false;

    public ContinueSendWindow(List<String> list) {
        super(MainWindow.INSTANCE);
        setTitle("继续发文");
        setTitlePlace(QRDialog.CENTER);
        setParentWindowNotFollowMove();
        setSize(540, 420);

        //获取文件列表
        listTabbedPane = new QRListTabbedPane();
        list.forEach(s -> {
            TypedData data = TextSendManager.loadSerializedData(s);
            if (data != null) {
                listTabbedPane.addPanel(s, new ListContentPanel(data));
            }
        });

        listTabbedPane.getList().setPreferredSize(new Dimension(100, 0));
        QRRoundButton sureButton = new QRRoundButton("确定");
        sureButton.setEnabled(false);

        sureButton.addClickAction(e -> {
            if (listTabbedPane.selectedIndex() == -1) {
                return;
            }
            isContinue = true;
            dispose();
        });
        listTabbedPane.addTabSelectChangedAction(event -> {
            QRTabSelectEvent e = (QRTabSelectEvent) event;
            if (e.after() != -1) {
                sureButton.setEnabled(true);
            }
        });

        QRComponentUtils.setBoundsAndAddToComponent(mainPanel, listTabbedPane, 10, 10, 500, 350);
        QRComponentUtils.setBoundsAndAddToComponent(mainPanel, sureButton, 445, 310, 80, 40);
    }

    static class ListContentPanel extends QRTabbedContentPanel {

        public ListContentPanel(TypedData data) {
            QRLabel fileNameTip = new QRLabel("书名：");
            QRLabel fileNameShowLabel = new QRLabel(data.fileName());
            QRLabel wordsLabel = new QRLabel("剩余/总计：");
            QRLabel wordsShowLabel = new QRLabel(String.format("%s / %s (%s)", data.remainingWordsCount(), data.totalWordsNum(),
                    QRMathUtils.doubleFormat((double) (data.totalWordsNum() - data.remainingWordsCount()) / data.totalWordsNum() * 100) + "%"));
            QRLabel paraNumTip = new QRLabel("每段字数：");
            QRComboBox paraNumComboBox = new QRComboBox("50", "100", "200", "500", "1000");
            QRLabel typeParaTip = new QRLabel("已打段数：");
            QRLabel typeParaShowLabel = new QRLabel(String.valueOf(data.typedTimes()));
            QRLabel previewTip = new QRLabel(data.randomPick() ? "发文内容将从下面的单字中随机抽取：" : "当前段预览：");
            QRTextArea previewTextArea = new QRTextArea(true);


            setLayout(null);
            if (data.randomPick()) {
                previewTextArea.setText(QRFileUtils.fileReaderWithUtf8All(data.filePath()));
            } else {
                previewTextArea.setText(data.currentText());
            }
            previewTextArea.setEditable(true);
            paraNumComboBox.setText(String.valueOf(data.perLength()));

            QRComponentUtils.setBoundsAndAddToComponent(this, fileNameTip, 10, 5, 105, 30);
            QRComponentUtils.setBoundsAndAddToComponent(this, fileNameShowLabel, 120, 5, 300, 30);
            QRComponentUtils.setBoundsAndAddToComponent(this, wordsLabel, 10, 125, 105, 30);
            QRComponentUtils.setBoundsAndAddToComponent(this, wordsShowLabel, 120, 125, 300, 30);
            QRComponentUtils.setBoundsAndAddToComponent(this, paraNumTip, 10, 85, 105, 30);
            QRComponentUtils.setBoundsAndAddToComponent(this, paraNumComboBox, 120, 85, 115, 30);
            QRComponentUtils.setBoundsAndAddToComponent(this, typeParaTip, 10, 45, 105, 30);
            QRComponentUtils.setBoundsAndAddToComponent(this, typeParaShowLabel, 120, 45, 115, 30);
            QRComponentUtils.setBoundsAndAddToComponent(this, previewTip, 10, 165, 300, 30);
            QRComponentUtils.setBoundsAndAddToComponent(this, previewTextArea.addScrollPane(), 10, 205, 295, 135);
        }
    }

    public int selectedIndex() {
        return listTabbedPane.selectedIndex();
    }

    public boolean isContinue() {
        return isContinue;
    }
}