package ly.qr.kiarelemb.menu.send.panel;

import ly.qr.kiarelemb.component.CheckBox;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.TextWash;
import ly.qr.kiarelemb.text.send.SendWindow;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRRandomUtils;
import method.qr.kiarelemb.utils.QRStringUtils;
import method.qr.kiarelemb.utils.QRSystemUtils;
import swing.qr.kiarelemb.basic.*;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.listener.QRDocumentListener;
import swing.qr.kiarelemb.listener.QRMouseListener;
import swing.qr.kiarelemb.utils.QRComponentUtils;
import swing.qr.kiarelemb.window.enhance.QROpinionDialog;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;
import swing.qr.kiarelemb.window.utils.QRResizableTextShowDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
public class SingleSendTextTabbedPanel extends ly.qr.kiarelemb.menu.send.panel.SendTextTabbedContentPanel {
    private final String[] values;
    private final QRTextPane contentPreviewPane;
    private final QRTextField fileNameTextField;
    private final CheckBox randomPickCbx;
    private String content;
    private boolean contentLock = false;

    public SingleSendTextTabbedPanel(SendWindow window) {
        super(window, 630, 470);
        values = new String[]{"前500", "中500", "后500", "前1k", "前1.5k", "前1.5k-2k", "前2k-2.5k", "前2.5k-3k", "前3k-3.5k", "前3.5k", "皇500", "王500", "帝500"};
        content = "";
        QRList ContentList = new QRList(values);
        QRLabel previewTip = new QRLabel("内容预览：");
        CheckBox customContentCheckBox = new CheckBox("自定义单字", Keys.TEXT_SEND_SINGLE_CUSTOM_TEXT, false);
        CheckBox ContentMixCheckBox = new CheckBox("自动乱序", Keys.TEXT_SEND_SINGLE_RANDOM_TEXT, false);
        contentPreviewPane = new QRTextPane() {
            @Override
            protected void pasteAction() {
                String pasteText = QRSystemUtils.getSysClipboardText();
                if (pasteText == null || pasteText.isBlank()) {
                    return;
                }
                pasteText = TextWash.lightWashForChinese(pasteText);
                String text = contentPreviewPane.getText();
                content = QRStringUtils.getNoRepeatString(text.isEmpty() ? pasteText : new StringBuilder(text).insert(contentPreviewPane.getCaretPosition(), pasteText).toString());
                contentLock = true;
                contentPreviewPane.setText(ContentMixCheckBox.isSelected() ? QRRandomUtils.getRandomString(content) : content);
                contentLock = false;
                String fileName = fileNameTextField.getText();
                fileNameTextField.setText(fileName.isBlank() ? "自定义单字" : (fileName + "-" + "自定义单字"));
            }
        };
        QRLabel startParaLabel = new QRLabel("起始段号：");

        startParaCbx = getStartParaComboBox();
        QRLabel paraWordLabel = new QRLabel("每段字数：");
        paraWordCbx = getParaWordComboBox();

        randomPickCbx = new CheckBox("随机抽取", Keys.TEXT_SEND_SINGLE_RANDOM_PICK, false);

        fileNameTextField = new QRTextField();
        QRRoundButton introductionBtn = new QRRoundButton("使用说明");
        QRRoundButton startBtn = new QRRoundButton("开始");

        fileNameTextField.setTextCenter();
        fileNameTextField.setToolTipText("文件名称");
        customContentCheckBox.setToolTipText("双击左侧列表项可在内容预览的光标处插入单字。");
        customContentCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
        ContentMixCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
        randomPickCbx.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPreviewPane.setEditableFalseButCursorEdit();
        contentPreviewPane.setEditable(customContentCheckBox.checked());
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
                    String name = values[index];
                    int clickCount = e.getClickCount();
                    boolean isMix = ContentMixCheckBox.isSelected();
                    if (clickCount == 2 && customContentCheckBox.isSelected()) {
                        String text = contentPreviewPane.getText();
                        // 双击插入内容
                        content = QRStringUtils.getNoRepeatString(text.isEmpty() ? getContent(index) : new StringBuilder(text).insert(contentPreviewPane.getCaretPosition(), getContent(index)).toString());
                        String fileName = fileNameTextField.getText();
                        fileNameTextField.setText(fileName.isBlank() ? name : (fileName + "-" + name));
                    } else {
                        // 单击设置内容
                        content = getContent(index);
                        fileNameTextField.setText(name);
                    }
                    contentLock = true;
                    contentPreviewPane.setText(isMix ? QRRandomUtils.getRandomString(content) : content);
                    contentLock = false;
                }
            });
            timer.schedule(task.get(), 250);
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

        // 使用说明事件
        introductionBtn.addClickAction(e -> {
            String content = """
                    1. 自定义内容：
                        ① 单击列表项，文本面板的内容将设置为选择项的内容；
                        ② 双击列表项，在勾选“自定义内容”时，文本面板的光标位置处将插入选择项的内容，否则，功能同单击；
                        ③ 文本面板中插入或粘贴的内容将自动去重；

                    2. 自动乱序：
                        ① 勾选“自动乱序”后，文本面板的内容将随机打乱；
                        ② 取消勾选“自动乱序”后，文本面板的内容将恢复原样；
                        ③ 再次勾选“自动乱序”后，文本面板的内容将重新随机打乱，最终的发文内容为文本面板的内容；
                                        
                    3. 单字列表：
                        ① 为适应双击功能，文本面板内容的设置延迟 250 毫秒，该时间内若鼠标单击两次，则视为双击；
                        ② 单字发文的文件名可由列表选择后自动生成，也可手动输入。相同的文件名，在发文后，会替换之前的；
                                        
                    4. 文件名：
                        ① 文件名输入框中，输入的文件名将自动补全为 txt 格式；
                        ② 文件名输入框可自由编辑，但不能为空。""";
            QRResizableTextShowDialog textShowDialog = new QRResizableTextShowDialog(window, 590, 360, "单字发文使用说明", content, true);
            QRSystemUtils.setWindowNotTrans(textShowDialog);
            textShowDialog.setResizable(false);
            textShowDialog.setVisible(true);
        });

        // 确定按钮的控制
        contentPreviewPane.addDocumentListener();
        QRActionRegister<DocumentEvent> textChangedAction = event -> {
            String temp = contentPreviewPane.getText();
            startBtn.setEnabled(!temp.isEmpty());
            if (contentLock) {
                return;
            }
            content = temp;
        };
        contentPreviewPane.addDocumentListenerAction(QRDocumentListener.TYPE.INSERT, textChangedAction);
        contentPreviewPane.addDocumentListenerAction(QRDocumentListener.TYPE.REMOVE, textChangedAction);

        // 开始按钮事件
        startBtn.addClickAction(this::startAction);

        setLayout(null);
        QRComponentUtils.setBoundsAndAddToComponent(this, ContentList.addScrollPane(), 30, 25, 125, 225);
        QRComponentUtils.setBoundsAndAddToComponent(this, previewTip, 180, 25, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, customContentCheckBox, 330, 25, 130, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, ContentMixCheckBox, 465, 25, 120, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, contentPreviewPane.addScrollPane(), 180, 75, 405, 175);
        QRComponentUtils.setBoundsAndAddToComponent(this, startParaLabel, 180, 275, 95, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, startParaCbx, 285, 275, 130, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, paraWordLabel, 180, 325, 95, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, paraWordCbx, 285, 325, 130, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, randomPickCbx, 485, 275, 100, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, fileNameTextField, 30, 265, 125, 38);
        QRComponentUtils.setBoundsAndAddToComponent(this, introductionBtn, 45, 320, 95, 35);
        QRComponentUtils.setBoundsAndAddToComponent(this, startBtn, 490, 320, 95, 35);
    }

    private void startAction(Object o) {
        String text = TextWash.lightWashForChinese(contentPreviewPane.getText());
        if (text.isEmpty()) {
            return;
        }
        String fileName = fileNameTextField.getText();
        if (fileName.isEmpty()) {
            QRSmallTipShow.display(window, "请输入文件名！");
            return;
        }
        filePath = Info.TYPE_DIRECTORY + fileName + ".txt";
        if (!QRFileUtils.fileCreate(filePath)) {
            QROpinionDialog.messageErrShow(window, "文件名无效！");
            return;
        }

        QRFileUtils.fileWriterWithUTF8(filePath, text);
        startSendAction(randomPickCbx.checked());
    }

    /**
     * 提取文
     *
     * @param index 选重的索引
     * @return 对应的文本
     */
    private String getContent(int index) {
        try {
            URL url = Info.loadURI(Info.SINGLES_PATH).toURL();
            InputStream inputStream = url.openStream();
            if (index > 9) {
                return QRFileUtils.getFileLineText(inputStream, index - 9, StandardCharsets.UTF_8);
            }
            String fore3500 = QRFileUtils.getFileLineText(inputStream, 4, StandardCharsets.UTF_8);
            return switch (index) {
                case 0, 1, 2 -> fore3500.substring(index * 500, ++index * 500);
                case 3, 4 -> fore3500.substring(0, --index * 500);
                case 5, 6, 7, 8 -> fore3500.substring((--index - 1) * 500, index * 500);
                // 9
                default -> fore3500;
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}