package ly.qr.kiarelemb.component.setting.panel;

import ly.qr.kiarelemb.component.*;
import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.component.setting.SettingWindow;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.input.InputManager;
import ly.qr.kiarelemb.text.tip.AbstractTextTip;
import ly.qr.kiarelemb.text.tip.TextTip;
import ly.qr.kiarelemb.text.tip.TextTipEnhance;
import ly.qr.kiarelemb.text.tip.TipWindow;
import swing.qr.kiarelemb.component.QRComponentUtils;
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
        QRComboBox codeLengthCheckBox = new ComboBox(Keys.TEXT_TIP_CODE_LENGTH, "四码方案", "三码方案", "三码42顶");
        QRLineSeparatorLabel lineB = new QRLineSeparatorLabel();
        QRLabel showModelLabel = new QRLabel("显示模式：");
        CheckBox paintColorCheckBox = new CheckBox("启用着色", Keys.TEXT_TIP_PAINT_COLOR);
        CheckBox paintSelectionCheckBox = new CheckBox("显示选重", Keys.TEXT_TIP_PAINT_SELECTION);
        CheckBox paintCodeCheckBox = new CheckBox("显示编码", Keys.TEXT_TIP_PAINT_CODE);
        CheckBox charModelEnableCheckBox = new CheckBox("单字启用", Keys.TEXT_TIP_CHAR_ENABLE);
        CheckBox singlePhraseDevideCheckBox = new CheckBox("字词分行", Keys.TEXT_TIP_DIVIDE);

        QRCheckBox tipPanelEnableCheckBox = new CheckBox("启用编码提示面板", Keys.TEXT_TIP_PANEL_ENABLE);
        QRLabel tipPanelLocationLabel = new QRLabel("位置：");
        QRComboBox tipPanelComboBox = new ComboBox(Keys.TEXT_TIP_PANEL_LOCATION, "看打区上方", "看打区下方", "跟打区上方", "跟打区下方");
        QRCheckBox tipWindowEnableCheckBox = new CheckBox("启用编码提示窗口", Keys.TEXT_TIP_WINDOW_ENABLE);
        QRLabel tipWindowLocationLabel = new QRLabel("位置：");
        QRComboBox tipWindowComboBox = new ComboBox(Keys.TEXT_TIP_WINDOW_LOCATION, "跟随光标", "固定于窗体上方居中");


        QRActionRegister tipLoadAction = es -> {
            TextTip.TEXT_TIP.release();
            TextTip.TEXT_TIP.load();
            if (InputManager.INPUT_MANAGER.isLoaded()) {
                InputManager.INPUT_MANAGER.tipUpdate();
            }
        };

        tipEnableCheckBox.addClickAction(e -> SettingsItem.SAVE_ACTIONS.putIfAbsent("tip.enable", tipLoadAction));

        tipEnhanceModelCheckBox.addClickAction(e -> {
            SettingsItem.SAVE_ACTIONS.putIfAbsent("tip.enable.enhance", es -> {
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
                SettingsItem.SAVE_ACTIONS.putIfAbsent("tip.load", tipLoadAction);
            }
        });

        QRActionRegister tipPanelAction = e -> SettingsItem.SAVE_ACTIONS.putIfAbsent("tip.panel",
                es -> SplitPane.SPLIT_PANE.updateTipPaneLocation());
        QRActionRegister tipWindowAction = e -> SettingsItem.SAVE_ACTIONS.putIfAbsent("tip.window",
                es -> TipWindow.TIP_WINDOW.updateTipWindowLocation());
        tipPanelEnableCheckBox.addClickAction(tipPanelAction);
        tipWindowEnableCheckBox.addClickAction(tipWindowAction);
        tipPanelComboBox.addItemChangeListener(tipPanelAction);
        tipWindowComboBox.addItemChangeListener(tipWindowAction);

        singlePhraseDevideCheckBox.addClickAction(e -> SettingsItem.SAVE_ACTIONS.putIfAbsent("tip.divide", es -> {
            SplitPane.SPLIT_PANE.tipPanel.layoutUpdate();
            TipWindow.TIP_WINDOW.tipPanel.layoutUpdate();
        }));

        QRComponentUtils.setBoundsAndAddToComponent(this, tipEnableCheckBox, 25, 30, 110, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, tipEnhanceModelCheckBox, 145, 30, 165, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, tipFileSelectBtn, 25, 110, 75, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, lineA, 30, 70, 455, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, tipFilePathLabel, 115, 110, 355, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, multiLabel, 25, 165, 75, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, selectionTextField, 115, 170, 130, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, codeLengthCheckBox, 25, 220, 150, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, lineB, 25, 265, 455, 22);
        QRComponentUtils.setBoundsAndAddToComponent(this, showModelLabel, 25, 300, 95, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, paintColorCheckBox, 55, 345, 110, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, paintSelectionCheckBox, 55, 385, 110, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, paintCodeCheckBox, 55, 425, 110, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, charModelEnableCheckBox, 55, 465, 110, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, tipPanelEnableCheckBox, 220, 345, 190, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, tipPanelLocationLabel, 245, 400, 65, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, tipPanelComboBox, 320, 400, 115, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, tipWindowEnableCheckBox, 220, 455, 190, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, tipWindowLocationLabel, 245, 505, 65, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, tipWindowComboBox, 320, 505, 115, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, singlePhraseDevideCheckBox, 55, 505, 110, 30);

        setPreferredSize(505, 580);
    }
}