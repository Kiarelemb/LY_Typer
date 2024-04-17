/*
 * Created by JFormDesigner on Sun Mar 31 22:28:32 CST 2024
 */

package ly.qr.kiarelemb.component.menu.send.panel;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.ComboBox;
import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.TextWash;
import ly.qr.kiarelemb.text.send.SendWindow;
import ly.qr.kiarelemb.text.send.TextSendManager;
import ly.qr.kiarelemb.text.send.data.TypedData;
import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRFileUtils;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.basic.QRComboBox;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRRoundButton;
import swing.qr.kiarelemb.component.basic.QRTextField;
import swing.qr.kiarelemb.component.event.QRItemEvent;
import swing.qr.kiarelemb.component.event.QRTabSelectEvent;
import swing.qr.kiarelemb.component.utils.QRClearableTextField;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Kiarelemb QR
 */
public class LocalTextSendTextTabbedPanel extends SendTextTabbedContentPanel {
	private final QRTextField sendFileTextField;
	private final QRComboBox startParaCbx;
	private final QRComboBox paraWordCbx;

	public LocalTextSendTextTabbedPanel(SendWindow window) {
		super(window);
		QRRoundButton fileSelectBtn = new QRRoundButton("选择");
		QRRoundButton clipboardBtn = new QRRoundButton("剪贴板");
		QRLabel startParaLabel = new QRLabel("起始段号：");
		QRLabel paraWordLabel = new QRLabel("每段字数：");
		startParaCbx = new ComboBox(0, Keys.TEXT_SEND_START_PARA, "1", "随机段号");
		QRRoundButton startBtn = new QRRoundButton("开始");
		QRClearableTextField clearableTextField = new QRClearableTextField(true, true, null, startBtn);
		sendFileTextField = clearableTextField.textField;

		String[] wordArray = {"50", "100", "200", "500"};
		int wordNum = Keys.intValue(Keys.TEXT_SEND_START_WORD_NUM);

		String numString = String.valueOf(wordNum);
		int selectIndex = QRArrayUtils.objectIndexOf(wordArray, numString);
		if (selectIndex == -1) {
			wordArray = new String[]{"50", "100", "200", "500", numString};
			selectIndex = wordArray.length - 1;
		}
		paraWordCbx = new QRComboBox(wordArray) {
			@Override
			protected void itemChangedAction(QRItemEvent e) {
				SettingsItem.CHANGE_MAP.put(Keys.TEXT_SEND_START_WORD_NUM, e.after());
			}
		};
		paraWordCbx.setSelectedIndex(selectIndex);

		startBtn.setEnabled(false);
		startBtn.addClickAction(this::startAction);
		fileSelectBtn.addClickAction(this::fileSelect);
		clearableTextField.clearButton.addClickAction(event -> sendFileTextField.setToolTipText(null));

		paraWordCbx.setEditable(true);
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
		QRComponentUtils.setBoundsAndAddToComponent(this, startBtn, 300, 165, 75, 35);
		//endregion 位置与添加

	}

	@Override
	protected void thisTabSelectChangeAction(QRTabSelectEvent event) {
		SwingUtilities.invokeLater(() -> {
			Window window = SwingUtilities.getWindowAncestor(this);
			window.setSize(390, 290);
			window.setLocationRelativeTo(MainWindow.INSTANCE);
		});
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
		String filePath = sendFileTextField.getText();

		if (TextWash.fileCopyAndWash(filePath, Info.TYPE_DIRECTORY.concat(sendFileTextField.getToolTipText()))) {
			try {
				String fileCrc = QRFileUtils.getCrc32(filePath);
				int words = Math.toIntExact(QRFileUtils.getFileWordNumWithUTF8(filePath));
				int paraWords = Integer.parseInt(paraWordCbx.getText());
				TypedData sendData = new TypedData(QRFileUtils.getFileName(filePath), fileCrc, 0, 0, words, words,
						paraWords, startParaCbx.getSelectedIndex() == 1);
				TextSendManager.setTypedData(sendData);
				window.dispose();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}
}