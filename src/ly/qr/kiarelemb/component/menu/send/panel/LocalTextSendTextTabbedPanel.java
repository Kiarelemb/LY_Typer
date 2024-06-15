/*
 * Created by JFormDesigner on Sun Mar 31 22:28:32 CST 2024
 */

package ly.qr.kiarelemb.component.menu.send.panel;

import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.TextWash;
import ly.qr.kiarelemb.text.send.SendWindow;
import method.qr.kiarelemb.utils.QRFileUtils;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRRoundButton;
import swing.qr.kiarelemb.component.basic.QRTextField;
import swing.qr.kiarelemb.component.utils.QRClearableTextField;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author Kiarelemb QR
 */
public class LocalTextSendTextTabbedPanel extends SendTextTabbedContentPanel {
    private final QRTextField sendFileTextField;

    public LocalTextSendTextTabbedPanel(SendWindow window) {
        super(window, 400, 300);
        QRRoundButton fileSelectBtn = new QRRoundButton("选择");
        QRRoundButton clipboardBtn = new QRRoundButton("剪贴板");
        QRLabel startParaLabel = new QRLabel("起始段号：");
        QRLabel paraWordLabel = new QRLabel("每段字数：");
        startParaCbx = getStartParaComboBox();
        QRRoundButton startBtn = new QRRoundButton("开始");
        QRClearableTextField clearableTextField = new QRClearableTextField(true, true, null, startBtn);
        sendFileTextField = clearableTextField.textField;

        paraWordCbx = getParaWordComboBox();

        startBtn.setEnabled(false);
        startBtn.addClickAction(this::startAction);
        fileSelectBtn.addClickAction(this::fileSelect);
        clearableTextField.clearButton.addClickAction(event -> sendFileTextField.setToolTipText(null));

        sendFileTextField.setTextCenter();

        //region 位置与添加
        setLayout(null);
        QRComponentUtils.setBoundsAndAddToComponent(this, clearableTextField, 25, 15, 260, 35);
        QRComponentUtils.setBoundsAndAddToComponent(this, fileSelectBtn, 300, 15, 75, 35);
        QRComponentUtils.setBoundsAndAddToComponent(this, clipboardBtn, 25, 70, 90, 35);
        QRComponentUtils.setBoundsAndAddToComponent(this, startParaLabel, 25, 120, 95, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, startParaCbx, 130, 120, 130, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, paraWordLabel, 25, 165, 95, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, paraWordCbx, 130, 165, 130, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, startBtn, 300, 160, 75, 35);
        //endregion 位置与添加
    }

    private void fileSelect(Object o) {
        Window window = SwingUtilities.getWindowAncestor(this);
        File file = QRFileUtils.fileSelect(window, "文本文件", "txt", "mobi", "epub");
        if (file == null) {
            return;
        }
        sendFileTextField.setText(file.getAbsolutePath());
        sendFileTextField.setToolTipText(file.getName());
    }

    private void startAction(Object o) {
        filePath = sendFileTextField.getText();
        if (!TextWash.fileCopyAndWash(filePath, Info.TYPE_DIRECTORY.concat(sendFileTextField.getToolTipText()))) {
            return;
        }
        startSendAction(false);
    }
}