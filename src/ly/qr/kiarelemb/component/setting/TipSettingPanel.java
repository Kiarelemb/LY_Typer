package ly.qr.kiarelemb.component.setting;

import ly.qr.kiarelemb.component.*;
import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.tip.AbstractTextTip;
import ly.qr.kiarelemb.text.tip.TextTip;
import ly.qr.kiarelemb.text.tip.TextTipEnhance;
import ly.qr.kiarelemb.text.tip.TipWindow;
import swing.qr.kiarelemb.component.basic.QRCheckBox;
import swing.qr.kiarelemb.component.basic.QRComboBox;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRTextField;
import swing.qr.kiarelemb.component.utils.QRFileSelectRoundButton;
import swing.qr.kiarelemb.component.utils.QRLineSeparatorLabel;
import swing.qr.kiarelemb.inter.QRActionRegister;

import java.io.File;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-07 21:13
 **/
public class TipSettingPanel extends SettingPanel {

	public TipSettingPanel(SettingWindow window) {
		super(window, "词提...");
		CheckBox tipEnableCheckBox = new CheckBox("启用词提", Keys.TEXT_TIP_ENABLE);
		CheckBox tipEnhanceModelCheckBox = new CheckBox("启用增强型词提", Keys.TEXT_TIP_ENHANCE);
		QRLineSeparatorLabel lineA = new QRLineSeparatorLabel();
		QRLabel tipFilePathLabel = new Label(Keys.TEXT_TIP_FILE_PATH);
		QRFileSelectRoundButton tipFileSelectBtn = new QRFileSelectRoundButton("选择", window, "词提文件", "txt");
		QRLabel multiLabel = new QRLabel("选重：");
		QRTextField selectionTextField = new TextField(Keys.TEXT_TIP_SELECTION);
		QRComboBox codeLengthCheckBox = new ComboBox(Keys.TEXT_TIP_CODE_LENGTH, "四码方案", "三码方案");
		QRLineSeparatorLabel lineB = new QRLineSeparatorLabel();
		QRLabel showModelLabel = new QRLabel("显示模式：");
		CheckBox paintColorCheckBox = new CheckBox("启用着色", Keys.TEXT_TIP_PAINT_COLOR);
		CheckBox paintSelectionCheckBox = new CheckBox("显示选重", Keys.TEXT_TIP_PAINT_SELECTION);
		CheckBox paintCodeCheckBox = new CheckBox("显示编码", Keys.TEXT_TIP_PAINT_CODE);
		CheckBox charModelEnableCheckBox = new CheckBox("单字启用", Keys.TEXT_TIP_CHAR_ENABLE);

		QRCheckBox tipPanelEnableCheckBox = new CheckBox("启用编码提示面板", Keys.TEXT_TIP_PANEL_ENABLE);
		QRLabel tipPanelLocationLabel = new QRLabel("位置：");
		QRComboBox tipPanelComboBox = new ComboBox(Keys.TEXT_TIP_PANEL_LOCATION, "看打区上方", "看打区下方", "跟打区上方", "跟打区下方");
		QRCheckBox tipWindowEnableCheckBox = new CheckBox("启用编码提示窗口", Keys.TEXT_TIP_WINDOW_ENABLE);
		QRLabel tipWindowLocationLabel = new QRLabel("位置：");
		QRComboBox tipWindowComboBox = new ComboBox(Keys.TEXT_TIP_WINDOW_LOCATION, "跟随光标", "固定于窗体上方居中");


		QRActionRegister tipLoadAction = es -> {
			TextTip.TEXT_TIP.release();
			TextTip.TEXT_TIP.load();
		};

		tipEnableCheckBox.addClickAction(e -> SettingsItem.saveActions.putIfAbsent("tip.enable", tipLoadAction));

		tipEnhanceModelCheckBox.addClickAction(e -> {
			SettingsItem.saveActions.putIfAbsent("tip.enable.enhance", es -> {
				if (Keys.boolValue(Keys.TEXT_TIP_ENHANCE)) {
					if (AbstractTextTip.TEXT_TIP instanceof TextTip) {
						AbstractTextTip.TEXT_TIP.release();
						AbstractTextTip.TEXT_TIP = new TextTipEnhance();
						AbstractTextTip.TEXT_TIP.load();
					}
				}
			});
		});

		selectionTextField.setTextCenter();
		tipFileSelectBtn.addSuccessAction(e -> {
			File file = (File) e;
			String path = file.getAbsolutePath();
			if (!path.equals(tipFilePathLabel.getText())) {
				tipFilePathLabel.setText(path);
				SettingsItem.saveActions.putIfAbsent("tip.load", tipLoadAction);
			}
		});

		QRActionRegister tipPanelAction = e -> SettingsItem.saveActions.putIfAbsent("tip.panel", es -> SplitPane.SPLIT_PANE.updateTipPaneLocation());
		QRActionRegister tipWindowAction = e -> SettingsItem.saveActions.putIfAbsent("tip.window", es -> TipWindow.TIP_WINDOW.updateTipWindowLocation());
		tipPanelEnableCheckBox.addClickAction(tipPanelAction);
		tipWindowEnableCheckBox.addClickAction(tipWindowAction);
		tipPanelComboBox.addItemChangeListener(tipPanelAction);
		tipWindowComboBox.addItemChangeListener(tipWindowAction);

		add(tipEnableCheckBox);
		tipEnableCheckBox.setBounds(25, 30, 110, 30);

		add(tipEnhanceModelCheckBox);
		tipEnhanceModelCheckBox.setBounds(145, 30, 165, 30);

		add(tipFileSelectBtn);
		tipFileSelectBtn.setBounds(25, 110, 75, 30);

		add(lineA);
		lineA.setBounds(30, 70, 455, 30);

		add(tipFilePathLabel);
		tipFilePathLabel.setBounds(115, 110, 355, 30);

		add(multiLabel);
		multiLabel.setBounds(25, 165, 75, 30);

		add(selectionTextField);
		selectionTextField.setBounds(115, 160, 130, 30);

		add(codeLengthCheckBox);
		codeLengthCheckBox.setBounds(25, 220, 150, 30);

		add(lineB);
		lineB.setBounds(25, 275, 455, 22);

		add(showModelLabel);
		showModelLabel.setBounds(25, 325, 95, 30);

		add(paintColorCheckBox);
		paintColorCheckBox.setBounds(55, 370, 110, 30);

		add(paintSelectionCheckBox);
		paintSelectionCheckBox.setBounds(55, 415, 110, 30);

		add(paintCodeCheckBox);
		paintCodeCheckBox.setBounds(55, 460, 110, 30);

		add(charModelEnableCheckBox);
		charModelEnableCheckBox.setBounds(55, 505, 110, 30);

		add(tipPanelEnableCheckBox);
		tipPanelEnableCheckBox.setBounds(220, 370, 190, 30);

		add(tipPanelLocationLabel);
		tipPanelLocationLabel.setBounds(245, 415, 65, 30);

		add(tipPanelComboBox);
		tipPanelComboBox.setBounds(320, 415, 135, 30);

		add(tipWindowEnableCheckBox);
		tipWindowEnableCheckBox.setBounds(220, 460, 190, 30);

		add(tipWindowLocationLabel);
		tipWindowLocationLabel.setBounds(245, 505, 65, 30);

		add(tipWindowComboBox);
		tipWindowComboBox.setBounds(320, 505, 135, 30);

		setPreferredSize(505, 565);
	}
}
