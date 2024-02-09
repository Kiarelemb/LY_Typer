/*
 * Created by JFormDesigner on Tue Feb 07 21:14:05 CST 2023
 */

package ly.qr.kiarelemb.test;

import swing.qr.kiarelemb.component.basic.*;
import swing.qr.kiarelemb.component.utils.QRLineSeparatorLabel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kiare
 */
public class TipPanelDesigner extends JPanel {
	private QRCheckBox tipEnableCheckBox;
	private QRRoundButton tipFileSelectBtn;
	private QRLineSeparatorLabel lineA;
	private QRLabel tipFilePathLabel;
	private QRLabel multyLabel;
	private QRTextField selectionTextField;
	private QRComboBox codeLengthComboBox;
	private QRLineSeparatorLabel lineB;
	private QRLabel showModelLabel;
	private QRCheckBox paintColorCheckBox;
	private QRCheckBox paintSelectionCheckBox;
	private QRCheckBox paintCodeCheckBox;
	private QRCheckBox charModelEnableCheckBox;
	private QRCheckBox tipEnhanceModelCheckBox;

	public TipPanelDesigner() {
		// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
		QRCheckBox tipEnableCheckBox;
		QRRoundButton tipFileSelectBtn;
		QRLineSeparatorLabel lineA;
		QRLabel tipFilePathLabel;
		QRLabel multyLabel;
		QRTextField selectionTextField;
		QRComboBox codeLengthComboBox;
		QRLineSeparatorLabel lineB;
		QRLabel showModelLabel;
		QRCheckBox paintColorCheckBox;
		QRCheckBox paintSelectionCheckBox;
		QRCheckBox paintCodeCheckBox;
		QRCheckBox charModelEnableCheckBox;
		QRCheckBox tipEnhanceModelCheckBox;
		QRCheckBox tipPanelEnableCheckBox;
		QRLabel tipPanelLocationLabel;
		QRComboBox tipPanelComboBox;
		QRCheckBox tipWindowEnableCheckBox;
		QRLabel tipWindowLocationLabel;
		QRComboBox tipWindowComboBox;
		// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
		tipEnableCheckBox = new QRCheckBox();
		tipFileSelectBtn = new QRRoundButton();
		lineA = new QRLineSeparatorLabel();
		tipFilePathLabel = new QRLabel();
		multyLabel = new QRLabel();
		selectionTextField = new QRTextField();
		codeLengthComboBox = new QRComboBox();
		lineB = new QRLineSeparatorLabel();
		showModelLabel = new QRLabel();
		paintColorCheckBox = new QRCheckBox();
		paintSelectionCheckBox = new QRCheckBox();
		paintCodeCheckBox = new QRCheckBox();
		charModelEnableCheckBox = new QRCheckBox();
		tipEnhanceModelCheckBox = new QRCheckBox();
		tipPanelEnableCheckBox = new QRCheckBox();
		tipPanelLocationLabel = new QRLabel();
		tipPanelComboBox = new QRComboBox();
		tipWindowEnableCheckBox = new QRCheckBox();
		tipWindowLocationLabel = new QRLabel();
		tipWindowComboBox = new QRComboBox();

		setLayout(null);

		tipEnableCheckBox.setText("\u542f\u7528\u8bcd\u63d0");
		add(tipEnableCheckBox);
		tipEnableCheckBox.setBounds(25, 30, 110, 30);

		tipFileSelectBtn.setText("\u9009\u62e9");
		add(tipFileSelectBtn);
		tipFileSelectBtn.setBounds(25, 110, 75, 30);

		lineA.setText("text");
		add(lineA);
		lineA.setBounds(30, 70, 455, lineA.getPreferredSize().height);

		tipFilePathLabel.setText("path");
		add(tipFilePathLabel);
		tipFilePathLabel.setBounds(115, 110, 355, 30);

		multyLabel.setText("\u9009\u91cd\uff1a");
		add(multyLabel);
		multyLabel.setBounds(25, 165, 75, 30);

		selectionTextField.setText("_234567890");
		add(selectionTextField);
		selectionTextField.setBounds(115, 170, 130, selectionTextField.getPreferredSize().height);
		add(codeLengthComboBox);
		codeLengthComboBox.setBounds(25, 220, 150, 30);

		lineB.setText("text");
		add(lineB);
		lineB.setBounds(25, 275, 455, 22);

		showModelLabel.setText("\u663e\u793a\u6a21\u5f0f\uff1a");
		add(showModelLabel);
		showModelLabel.setBounds(25, 325, 95, 30);

		paintColorCheckBox.setText("\u7740\u8272");
		add(paintColorCheckBox);
		paintColorCheckBox.setBounds(55, 370, 110, 30);

		paintSelectionCheckBox.setText("\u663e\u793a\u9009\u91cd");
		add(paintSelectionCheckBox);
		paintSelectionCheckBox.setBounds(55, 415, 110, 30);

		paintCodeCheckBox.setText("\u663e\u793a\u7f16\u7801");
		add(paintCodeCheckBox);
		paintCodeCheckBox.setBounds(55, 460, 110, 30);

		charModelEnableCheckBox.setText("\u5355\u5b57\u542f\u7528");
		add(charModelEnableCheckBox);
		charModelEnableCheckBox.setBounds(55, 505, 110, 30);

		tipEnhanceModelCheckBox.setText("\u542f\u7528\u589e\u5f3a\u578b\u8bcd\u63d0");
		add(tipEnhanceModelCheckBox);
		tipEnhanceModelCheckBox.setBounds(145, 30, 165, 30);

		tipPanelEnableCheckBox.setText("\u542f\u7528\u7f16\u7801\u63d0\u793a\u9762\u677f");
		add(tipPanelEnableCheckBox);
		tipPanelEnableCheckBox.setBounds(220, 370, 190, 30);

		tipPanelLocationLabel.setText("\u4f4d\u7f6e\uff1a");
		add(tipPanelLocationLabel);
		tipPanelLocationLabel.setBounds(245, 415, 65, 30);

		tipPanelComboBox.setText("text");
		add(tipPanelComboBox);
		tipPanelComboBox.setBounds(325, 415, 115, 30);

		tipWindowEnableCheckBox.setText("\u542f\u7528\u7f16\u7801\u63d0\u793a\u7a97\u53e3");
		add(tipWindowEnableCheckBox);
		tipWindowEnableCheckBox.setBounds(220, 460, 190, 30);

		tipWindowLocationLabel.setText("\u4f4d\u7f6e\uff1a");
		add(tipWindowLocationLabel);
		tipWindowLocationLabel.setBounds(245, 505, 65, 30);

		tipWindowComboBox.setText("text");
		add(tipWindowComboBox);
		tipWindowComboBox.setBounds(320, 505, 115, 30);

		{
			// compute preferred size
			Dimension preferredSize = new Dimension();
			for (int i = 0; i < getComponentCount(); i++) {
				Rectangle bounds = getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			setMinimumSize(preferredSize);
			setPreferredSize(preferredSize);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off

		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}
}
