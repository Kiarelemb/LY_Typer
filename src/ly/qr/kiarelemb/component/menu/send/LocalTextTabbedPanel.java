/*
 * Created by JFormDesigner on Sun Mar 31 22:28:32 CST 2024
 */

package ly.qr.kiarelemb.component.menu.send;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.ComboBox;
import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.send.data.TypedData;
import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRFileUtils;
import swing.qr.kiarelemb.component.basic.QRComboBox;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRRoundButton;
import swing.qr.kiarelemb.component.event.QRItemEvent;
import swing.qr.kiarelemb.component.event.QRTabSelectEvent;
import swing.qr.kiarelemb.component.utils.QRClearableTextField;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @author Kiarelemb QR
 */
public class LocalTextTabbedPanel extends TabbedContentPanel {
	private final QRClearableTextField sendFileTextField;
	private final QRComboBox startParaCbx;
	private final QRComboBox paraWordCbx;

	private TypedData sendData = null;

	public LocalTextTabbedPanel() {
		QRRoundButton fileSelectBtn = new QRRoundButton("选择");
		QRRoundButton clipboardBtn = new QRRoundButton("剪贴板");
		QRLabel startParaLabel = new QRLabel("起始段号：");
		QRLabel paraWordLabel = new QRLabel("每段字数：");
		startParaCbx = new ComboBox(0, Keys.TEXT_SEND_START_PARA, "1", "随机段号");
		QRRoundButton startBtn = new QRRoundButton("开始");
		startBtn.setEnabled(false);
		sendFileTextField = new QRClearableTextField(false, true, null, startBtn);
		startBtn.addClickAction(this::startAction);
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

		sendFileTextField.setBounds(25, 15, 260, 35);
		fileSelectBtn.setBounds(300, 15, 75, 35);
		clipboardBtn.setBounds(25, 70, 90, 35);
		startParaLabel.setBounds(25, 120, 95, 30);
		startParaCbx.setBounds(130, 120, 130, 30);
		paraWordLabel.setBounds(25, 165, 95, 30);
		paraWordCbx.setBounds(130, 165, 130, 30);
		startBtn.setBounds(300, 165, 75, 35);

		setLayout(null);
		add(sendFileTextField);
		add(fileSelectBtn);
		add(clipboardBtn);
		add(startParaLabel);
		add(startParaCbx);
		add(paraWordLabel);
		add(paraWordCbx);
		add(startBtn);


	}

	@Override
	protected void thisTabSelectChangeAction(QRTabSelectEvent event) {
		SwingUtilities.invokeLater(() -> {
			Window window = SwingUtilities.getWindowAncestor(this);
			window.setSize(390, 290);
			window.setLocationRelativeTo(MainWindow.INSTANCE);
		});
	}

	private void startAction(Object o) {
		String filePath = sendFileTextField.textField.getText();
		try {
			// TODO 文件格式化等操作
			String fileCrc = QRFileUtils.getCrc32(filePath);
			long words = QRFileUtils.getFileWordNumWithUTF8(filePath);
			int paraWords = Integer.parseInt(paraWordCbx.getText());
			this.sendData = new TypedData(QRFileUtils.getFileName(filePath), fileCrc, 0, 0, words, paraWords,
					startParaCbx.getSelectedIndex() == 1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TypedData getTypedData() {
		return sendData;
	}
}