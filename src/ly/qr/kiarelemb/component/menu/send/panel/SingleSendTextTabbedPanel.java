package ly.qr.kiarelemb.component.menu.send.panel;

import ly.qr.kiarelemb.component.CheckBox;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.send.SendWindow;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRRandomUtils;
import method.qr.kiarelemb.utils.QRStringUtils;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.basic.*;
import swing.qr.kiarelemb.component.listener.QRDocumentListener;
import swing.qr.kiarelemb.component.listener.QRMouseListener;
import swing.qr.kiarelemb.inter.QRActionRegister;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className SingleSendTextTabbedPanel
 * @description TODO
 * @create 2024/3/31 22:23
 */
public class SingleSendTextTabbedPanel extends SendTextTabbedContentPanel {
    private final String[] values;
    private String content;
    private boolean contentLock = false;

    public SingleSendTextTabbedPanel(SendWindow window) {
        super(window, 630, 470);
        values = new String[]{"前500", "中500", "后500", "前1k", "前1.5k", "前1.5k-2k", "前2k-2.5k", "前2.5k-3k", "前3k-3.5k", "前3.5k", "皇500", "王500", "帝500"};
        content = "";
        QRList ContentList = new QRList(values);
        QRLabel previewTip = new QRLabel("内容预览：");
        QRCheckBox customContentCheckBox = new CheckBox("自定义单字", Keys.TEXT_SEND_SINGLE_CUSTOM_TEXT, false);
        QRCheckBox ContentMixCheckBox = new CheckBox("自动乱序", Keys.TEXT_SEND_SINGLE_RANDOM_TEXT, false);
        QRTextPane contentPreviewPane = new QRTextPane();
        QRLabel startParaLabel = new QRLabel("起始段号：");
        QRComboBox startParaCbx = getStartParaComboBox();
        QRLabel paraWordLabel = new QRLabel("每段字数：");
        QRComboBox paraWordCbx = getParaWordComboBox();
        QRCheckBox randomPickCbx = new CheckBox("随机抽取", Keys.TEXT_SEND_SINGLE_RANDOM_PICK, false);
        QRRoundButton startBtn = new QRRoundButton("开始");

        customContentCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
        ContentMixCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
        randomPickCbx.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPreviewPane.setEditableFalseButCursorEdit();
        startBtn.setEnabled(false);

        // 列表鼠标事件
        ContentList.addMouseListener();
        AtomicReference<TimerTask> task = new AtomicReference<>();
        Timer timer = new Timer();
        ContentList.addMouseListener(QRMouseListener.TYPE.CLICK, e -> {
            int index = ContentList.getSelectedIndex();
            if (index == -1) {
                return;
            }
            if (task.get() != null) {
                task.get().cancel();
            }
            task.set(new TimerTask() {
                @Override
                public void run() {
                    int clickCount = ((MouseEvent) e).getClickCount();
                    boolean isMix = ContentMixCheckBox.isSelected();
                    if (clickCount == 2 && customContentCheckBox.isSelected()) {
                        String text = contentPreviewPane.getText();
                        // 双击插入内容
                        content = text.isEmpty() ? getContent(index) : QRStringUtils.getNoRepeatString(new StringBuilder(text).insert(contentPreviewPane.getCaretPosition(), getContent(index)).toString());
                    } else {
                        // 单击设置内容
                        content = getContent(index);
                    }
                    contentLock = true;
                    contentPreviewPane.setText(isMix ? QRRandomUtils.getRandomString(content) : content);
                    contentLock = false;
                }
            });
            timer.schedule(task.get(), 300);
        });

        // 自定义文本框事件
        customContentCheckBox.addClickAction(e -> {
            contentPreviewPane.setEditable(customContentCheckBox.isSelected());
            contentPreviewPane.setCaretPosition(content.length());
        });

        // 乱序事件
        ContentMixCheckBox.addClickAction(e -> {
            if (content.isEmpty()) {
                return;
            }
            if (ContentMixCheckBox.isSelected()) {
                contentLock = true;
                contentPreviewPane.setText(QRRandomUtils.getRandomString(content));
                contentLock = false;
            } else {
                contentPreviewPane.setText(content);
            }
        });

        // 确定按钮的控制
        contentPreviewPane.addDocumentListener();
        QRActionRegister textChangedAction = event -> {
            String temp = contentPreviewPane.getText();
            startBtn.setEnabled(!temp.isEmpty());
            if (contentLock) {
                return;
            }
            content = temp;
        };
        contentPreviewPane.addDocumentListenerAction(QRDocumentListener.TYPE.INSERT, textChangedAction);
        contentPreviewPane.addDocumentListenerAction(QRDocumentListener.TYPE.REMOVE, textChangedAction);

        setLayout(null);
        QRComponentUtils.setBoundsAndAddToComponent(this, ContentList.addScrollPane(), 30, 25, 125, 330);
        QRComponentUtils.setBoundsAndAddToComponent(this, previewTip, 180, 25, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, customContentCheckBox, 330, 25, 130, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, ContentMixCheckBox, 465, 25, 120, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, contentPreviewPane.addScrollPane(), 180, 75, 405, 175);
        QRComponentUtils.setBoundsAndAddToComponent(this, startParaLabel, 180, 275, 95, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, startParaCbx, 285, 275, 130, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, paraWordLabel, 180, 325, 95, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, paraWordCbx, 285, 325, 130, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, randomPickCbx, 485, 275, 100, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, startBtn, 490, 320, 95, 35);
    }

    /**
     * 提取文
     *
     * @param index 选重的索引
     * @return 对应的文本
     */
    private String getContent(int index) {
        String text;
        File file = new File(Info.loadURI(Info.SINGLES_PATH));
        if (index > 9) {
            text = QRFileUtils.getFileLineText(file, index - 9);
        } else {
            String fore3500 = QRFileUtils.getFileLineText(file, 4);
            if (index < 3) {
                text = fore3500.substring(index++ * 500, index * 500);
            } else if (index == 9) {
                text = fore3500;
            } else if (index > 4) {
                text = fore3500.substring((--index - 1) * 500, index * 500);
            } else {
                // 3, 4
                text = fore3500.substring(0, --index * 500);
            }
        }
        return text;
    }
}