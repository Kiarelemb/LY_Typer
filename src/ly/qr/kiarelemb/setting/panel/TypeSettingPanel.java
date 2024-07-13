package ly.qr.kiarelemb.setting.panel;

import ly.qr.kiarelemb.component.CheckBox;
import ly.qr.kiarelemb.component.ComboBox;
import ly.qr.kiarelemb.component.LineSeparatorLabel;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.setting.SettingWindow;
import swing.qr.kiarelemb.basic.QRCheckBox;
import swing.qr.kiarelemb.basic.QRComboBox;
import swing.qr.kiarelemb.basic.QRLabel;
import swing.qr.kiarelemb.utils.QRComponentUtils;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TypeSettingPanel
 * @description TODO
 * @create 2024/6/30 上午10:24
 */
public class TypeSettingPanel extends SettingPanel {
    public TypeSettingPanel(SettingWindow window) {
        super(window, "跟打");

        QRLabel typeStartLabel = new QRLabel("跟打开始前：");
        QRCheckBox textFormatCheckBox = new CheckBox("格式化载文文本", Keys.TEXT_LOAD_INTELLI);

        LineSeparatorLabel separatorLabelA = new LineSeparatorLabel();

        QRLabel typingLabel = new QRLabel("跟打进行时：");
        QRCheckBox discardUnknownKeyCheckBox = new CheckBox("屏蔽未知按键的统计", Keys.TYPE_DISCARD_UNKNOWN_KEY);
        QRCheckBox instantaneousCheckBox = new CheckBox("跟打数据采用瞬时计算", Keys.TYPE_DATA_INSTANTANEOUS_VELOCITY);
        QRLabel updateFrequencyLabel = new QRLabel("跟打数据更新频率：");
        QRComboBox updateFrequencyComboBox = new ComboBox(Keys.TYPE_STATISTICS_UPDATE, "实时更新", "每秒更新", "每五秒更新");
        QRLabel sendKeyStrokeLabel = new QRLabel("QQ发送键：");
        QRComboBox sendKeyStrokeComboBox = new ComboBox(Keys.TYPE_SEND_KEY, "Enter", "Ctrl Enter");

        LineSeparatorLabel separatorLabelB = new LineSeparatorLabel();

        QRLabel typeEndLabel = new QRLabel("跟打结束后：");
        QRCheckBox noWrongCheckBox = new CheckBox("全文无错", Keys.TYPE_END_CONDITION_NO_WRONG);
        QRCheckBox mixOrRestartCheckBox = new CheckBox("单字乱序文章重打", Keys.TYPE_END_MIX_RESTART);

        instantaneousCheckBox.setToolTipText("如速度与击键采用瞬时速度与击键，但并不影响最终的成绩");
        noWrongCheckBox.setToolTipText("该项作为判断条件，影响下一项“单字乱序文章重打”是否执行。不勾选，则表示有错也会进入到下一项。");
        mixOrRestartCheckBox.setToolTipText("如勾选，则跟打结束后，单字将自动乱序，文章则自动重打。不勾选，则什么也不做。");

        QRComponentUtils.setBoundsAndAddToComponent(this, typeStartLabel, 25, 25, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, textFormatCheckBox, 50, 70, 205, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, separatorLabelA, 25, 115, 500, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, typingLabel, 25, 160, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, discardUnknownKeyCheckBox, 50, 205, 205, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, instantaneousCheckBox, 50, 250, 205, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, updateFrequencyLabel, 50, 295, 205, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, updateFrequencyComboBox, 265, 295, 145, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, separatorLabelB, 25, 340, 500, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, typeEndLabel, 25, 385, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, noWrongCheckBox, 50, 430, 205, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, mixOrRestartCheckBox, 50, 475, 205, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, sendKeyStrokeLabel, 50, 520, 205, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, sendKeyStrokeComboBox, 265, 520, 145, 30);

        setPreferredSize(505, 585);
    }
}