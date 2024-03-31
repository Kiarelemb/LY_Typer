package ly.qr.kiarelemb.component.setting.panel;

import ly.qr.kiarelemb.component.CheckBox;
import ly.qr.kiarelemb.component.ComboBox;
import ly.qr.kiarelemb.component.TextField;
import ly.qr.kiarelemb.component.setting.SettingWindow;
import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.component.basic.QRButton;
import swing.qr.kiarelemb.component.basic.QRComboBox;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRTextField;
import swing.qr.kiarelemb.inter.QRActionRegister;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-01 14:21
 **/
public class GradeSendPanel extends SettingPanel {
	public GradeSendPanel(SettingWindow window) {
		super(window, "成绩单...");
		QRLabel gradeTipLabel = new QRLabel("成绩单发送项：");
		QRButton selectAllBtn = new QRButton("全选");
		QRButton notSelectAllBtn = new QRButton("全不选");
		QRButton reverseSelectionBtn = new QRButton("反选");
		CheckBox wordNumCheckBox = new CheckBox("字数", Keys.SEND_WORDS_NUM);
		CheckBox backChangeCheckBox = new CheckBox("回改", Keys.SEND_BACK_CHANGE);
		CheckBox backspaceCheckBox = new CheckBox("退格", Keys.SEND_BACKSPACE);
		CheckBox enterCheckBox = new CheckBox("回车", Keys.SEND_ENTER_COUNT);
		CheckBox wordWrongCheckBox = new CheckBox("错字", Keys.SEND_WORD_WRONG);
		CheckBox keyNumCheckBox = new CheckBox("键数", Keys.SEND_KEY_NUM);
		CheckBox keyAccuracyCheckBox = new CheckBox("键准", Keys.SEND_KEY_ACCURACY);
		CheckBox timeCheckBox = new CheckBox("用时", Keys.SEND_TIME_COST);
		CheckBox keyBalanceCheckBox = new CheckBox("键法", Keys.SEND_KEY_METHOD);
		CheckBox keyBoardCheckBox = new CheckBox("键盘", Keys.SEND_KEYBOARD);
		CheckBox inputMethodCheckBox = new CheckBox("输入法", Keys.SEND_METHOD_INPUT);
		CheckBox signCheckBox = new CheckBox("个签", Keys.SEND_SIGNATURE);
		CheckBox retypeCheckBox = new CheckBox("重打", Keys.SEND_TIMES_RETYPE);
		CheckBox pauseCheckBox = new CheckBox("暂停", Keys.SEND_TIMES_PAUSE);
		CheckBox systemCheckBox = new CheckBox("系统", Keys.SEND_SYSTEM_VERSION);
		CheckBox typeMethodCheckBox = new CheckBox("指法", Keys.SEND_METHOD_TYPE);
		CheckBox[] boxes = new CheckBox[]{wordNumCheckBox, backChangeCheckBox, backspaceCheckBox, enterCheckBox, wordWrongCheckBox, keyNumCheckBox, keyAccuracyCheckBox, timeCheckBox, keyBalanceCheckBox, keyBoardCheckBox, inputMethodCheckBox, signCheckBox, retypeCheckBox, pauseCheckBox, systemCheckBox, typeMethodCheckBox};
		CheckBox simplifyModelCheckBox = new CheckBox("极简模式", Keys.SEND_MINIMALISM);
		QRLabel inputMethodTipLabel = new QRLabel("输入法：");
		QRTextField inputMethodTextField = new TextField(Keys.TYPE_METHOD_INPUT);
		QRLabel signTipLabel = new QRLabel("个签：");
		QRTextField signTextField = new TextField(Keys.TYPE_SIGNATURE);
		QRLabel keyBoardTipLabel = new QRLabel("键盘：");
		QRTextField keyBoardTextField = new TextField(Keys.TYPE_METHOD_KEYBOARD);
		QRLabel typeMethodTipLabel = new QRLabel("指法：");
		QRComboBox spaceCombo = new ComboBox(Keys.TYPE_KEY_METHOD_SPACE, "左手", "右手", "不统计");
		QRLabel spaceTipLabel = new QRLabel("空格");
		QRComboBox bCombo = new ComboBox(Keys.TYPE_KEY_METHOD_B, "左手", "右手", "不统计");
		QRLabel bTipLabel = new QRLabel("B");

		simplifyModelCheckBox.setToolTipText("极简模式的成绩单只有一行，能尽最大可能减少刷屏程度。");
		QRActionRegister action = e -> {
			boolean b = !simplifyModelCheckBox.checked();
			for (CheckBox box : boxes) {
				box.setEnabled(b);
			}
		};
		//先执行一次
		action.action(null);
		simplifyModelCheckBox.addClickAction(action);

		selectAllBtn.addActionListener(e -> {
			for (CheckBox box : boxes) {
				box.setSelected(true);
			}
		});

		notSelectAllBtn.addActionListener(e -> {
			for (CheckBox box : boxes) {
				box.setSelected(false);
			}
		});

		reverseSelectionBtn.addActionListener(e -> {
			for (CheckBox box : boxes) {
				box.setSelected(!box.checked());
			}
		});

		inputMethodTipLabel.setTextRight();
		signTipLabel.setTextRight();
		keyBoardTipLabel.setTextRight();
		typeMethodTipLabel.setTextRight();

		gradeTipLabel.setBounds(25, 25, 125, 30);
		selectAllBtn.setBounds(155, 25, 60, 30);
		notSelectAllBtn.setBounds(220, 25, 70, 30);
		reverseSelectionBtn.setBounds(295, 25, 60, 30);
		wordNumCheckBox.setBounds(45, 80, 90, 30);
		backChangeCheckBox.setBounds(155, 80, 90, 30);
		backspaceCheckBox.setBounds(265, 80, 90, 30);
		enterCheckBox.setBounds(375, 80, 90, 30);
		wordWrongCheckBox.setBounds(45, 125, 90, 30);
		keyNumCheckBox.setBounds(155, 125, 90, 30);
		keyAccuracyCheckBox.setBounds(265, 125, 90, 30);
		timeCheckBox.setBounds(375, 125, 90, 30);
		keyBalanceCheckBox.setBounds(45, 170, 90, 30);
		keyBoardCheckBox.setBounds(375, 170, 90, 30);
		inputMethodCheckBox.setBounds(155, 170, 90, 30);
		signCheckBox.setBounds(265, 170, 90, 30);
		retypeCheckBox.setBounds(45, 215, 90, 30);
		pauseCheckBox.setBounds(155, 215, 90, 30);
		systemCheckBox.setBounds(265, 215, 90, 30);
		typeMethodCheckBox.setBounds(375, 215, 90, 30);
		simplifyModelCheckBox.setBounds(375, 25, 110, 30);
		inputMethodTipLabel.setBounds(45, 265, 75, 30);
		inputMethodTextField.setBounds(120, 260, 320, 30);
		signTipLabel.setBounds(45, 310, 75, 30);
		signTextField.setBounds(120, 305, 320, 30);
		keyBoardTextField.setBounds(120, 350, 320, 30);
		keyBoardTipLabel.setBounds(45, 355, 75, 30);
		typeMethodTipLabel.setBounds(45, 400, 75, 30);
		spaceCombo.setBounds(120, 400, 90, 30);
		spaceTipLabel.setBounds(215, 400, 45, 30);
		bCombo.setBounds(300, 400, 90, 30);
		bTipLabel.setBounds(400, 400, 45, 30);

		setPreferredSize(500, 465);

		add(gradeTipLabel);
		add(selectAllBtn);
		add(notSelectAllBtn);
		add(reverseSelectionBtn);
		add(simplifyModelCheckBox);
		add(inputMethodTipLabel);
		add(inputMethodTextField);
		add(signTipLabel);
		add(signTextField);
		add(keyBoardTipLabel);
		add(keyBoardTextField);
		add(typeMethodTipLabel);
		add(spaceCombo);
		add(spaceTipLabel);
		add(bCombo);
		add(bTipLabel);
		for (CheckBox box : boxes) {
			add(box);
		}
	}
}