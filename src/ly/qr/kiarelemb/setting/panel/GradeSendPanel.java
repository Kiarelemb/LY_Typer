package ly.qr.kiarelemb.setting.panel;

import ly.qr.kiarelemb.component.CheckBox;
import ly.qr.kiarelemb.component.ComboBox;
import ly.qr.kiarelemb.component.TextField;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.setting.SettingWindow;
import swing.qr.kiarelemb.basic.QRButton;
import swing.qr.kiarelemb.basic.QRComboBox;
import swing.qr.kiarelemb.basic.QRLabel;
import swing.qr.kiarelemb.basic.QRTextField;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.utils.QRComponentUtils;

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

		selectAllBtn.addClickAction(e -> {
			for (CheckBox box : boxes) {
				box.setSelected(true);
			}
		});

		notSelectAllBtn.addClickAction(e -> {
			for (CheckBox box : boxes) {
				box.setSelected(false);
			}
		});

		reverseSelectionBtn.addClickAction(e -> {
			for (CheckBox box : boxes) {
				box.setSelected(!box.checked());
			}
		});

		inputMethodTipLabel.setTextRight();
		signTipLabel.setTextRight();
		keyBoardTipLabel.setTextRight();
		typeMethodTipLabel.setTextRight();

		QRComponentUtils.setBoundsAndAddToComponent(this, gradeTipLabel, 25, 25, 125, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, selectAllBtn, 155, 25, 60, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, notSelectAllBtn, 220, 25, 70, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, reverseSelectionBtn, 295, 25, 60, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, wordNumCheckBox, 45, 80, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, backChangeCheckBox, 155, 80, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, backspaceCheckBox, 265, 80, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, enterCheckBox, 375, 80, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, wordWrongCheckBox, 45, 125, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, keyNumCheckBox, 155, 125, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, keyAccuracyCheckBox, 265, 125, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, timeCheckBox, 375, 125, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, keyBalanceCheckBox, 45, 170, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, keyBoardCheckBox, 375, 170, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, inputMethodCheckBox, 155, 170, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, signCheckBox, 265, 170, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, retypeCheckBox, 45, 215, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, pauseCheckBox, 155, 215, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, systemCheckBox, 265, 215, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, typeMethodCheckBox, 375, 215, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, simplifyModelCheckBox, 375, 25, 110, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, inputMethodTipLabel, 45, 265, 75, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, inputMethodTextField, 120, 260, 320, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, signTipLabel, 45, 310, 75, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, signTextField, 120, 305, 320, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, keyBoardTextField, 120, 350, 320, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, keyBoardTipLabel, 45, 355, 75, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, typeMethodTipLabel, 45, 400, 75, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, spaceCombo, 120, 400, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, spaceTipLabel, 215, 400, 45, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, bCombo, 300, 400, 90, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, bTipLabel, 400, 400, 45, 30);

		setPreferredSize(500, 465);
	}
}