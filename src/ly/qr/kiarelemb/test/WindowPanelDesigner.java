/*
 * Created by JFormDesigner on Sat Jan 21 22:49:43 CST 2023
 */

package ly.qr.kiarelemb.test;

import swing.qr.kiarelemb.component.basic.QRCheckBox;
import swing.qr.kiarelemb.component.basic.QRComboBox;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRTextField;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kiare
 */
public class WindowPanelDesigner extends JPanel {
	public WindowPanelDesigner() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        var gradeTipLabel = new QRLabel();
        var wordNumCheckBox = new QRCheckBox();
        var backChangeCheckBox = new QRCheckBox();
        var backspaceCheckBox = new QRCheckBox();
        var enterCheckBox = new QRCheckBox();
        var wordWrongCheckBox = new QRCheckBox();
        var keyNumCheckBox = new QRCheckBox();
        var keyAccuracyCheckBox = new QRCheckBox();
        var timeCheckBox = new QRCheckBox();
        var keyBalanceCheckBox = new QRCheckBox();
        var keyBoardCheckBox = new QRCheckBox();
        var inputMethodCheckBox = new QRCheckBox();
        var signCheckBox = new QRCheckBox();
        var retypeCheckBox = new QRCheckBox();
        var pauseCheckBox = new QRCheckBox();
        var systemCheckBox = new QRCheckBox();
        var typeMethodCheckBox = new QRCheckBox();
        var phraseCountCombo = new QRCheckBox();
        var simplifyModelCheckBox = new QRCheckBox();
        var inputMethodTipLabel = new QRLabel();
        var inputMethodTextField = new QRTextField();
        var signTipLabel = new QRLabel();
        var signTextField = new QRTextField();
        var keyBoardTipLabel = new QRLabel();
        var keyBoardTextField = new QRTextField();
        var typeMethodTipLabel = new QRLabel();
        var spaceCombo = new QRComboBox();
        var spaceTipLabel = new QRLabel();
        var bCombo = new QRComboBox();
        var bTipLabel = new QRLabel();

        //======== this ========
        setLayout(null);

        //---- gradeTipLabel ----
        gradeTipLabel.setText("\u6210\u7ee9\u5355\u53d1\u9001\u9879\uff1a");
        add(gradeTipLabel);
        gradeTipLabel.setBounds(25, 25, 125, 30);

        //---- wordNumCheckBox ----
        wordNumCheckBox.setText("\u5b57\u6570");
        add(wordNumCheckBox);
        wordNumCheckBox.setBounds(45, 80, 90, 30);

        //---- backChangeCheckBox ----
        backChangeCheckBox.setText("\u56de\u6539");
        add(backChangeCheckBox);
        backChangeCheckBox.setBounds(155, 80, 90, 30);

        //---- backspaceCheckBox ----
        backspaceCheckBox.setText("\u9000\u683c");
        add(backspaceCheckBox);
        backspaceCheckBox.setBounds(265, 80, 90, 30);

        //---- enterCheckBox ----
        enterCheckBox.setText("\u56de\u8f66");
        add(enterCheckBox);
        enterCheckBox.setBounds(375, 80, 90, 30);

        //---- wordWrongCheckBox ----
        wordWrongCheckBox.setText("\u9519\u5b57");
        add(wordWrongCheckBox);
        wordWrongCheckBox.setBounds(45, 125, 90, 30);

        //---- keyNumCheckBox ----
        keyNumCheckBox.setText("\u952e\u6570");
        add(keyNumCheckBox);
        keyNumCheckBox.setBounds(155, 125, 90, 30);

        //---- keyAccuracyCheckBox ----
        keyAccuracyCheckBox.setText("\u952e\u51c6");
        add(keyAccuracyCheckBox);
        keyAccuracyCheckBox.setBounds(265, 125, 90, 30);

        //---- timeCheckBox ----
        timeCheckBox.setText("\u7528\u65f6");
        add(timeCheckBox);
        timeCheckBox.setBounds(375, 125, 90, 30);

        //---- keyBalanceCheckBox ----
        keyBalanceCheckBox.setText("\u952e\u6cd5");
        add(keyBalanceCheckBox);
        keyBalanceCheckBox.setBounds(45, 170, 90, 30);

        //---- keyBoardCheckBox ----
        keyBoardCheckBox.setText("\u952e\u76d8");
        add(keyBoardCheckBox);
        keyBoardCheckBox.setBounds(375, 170, 90, 30);

        //---- inputMethodCheckBox ----
        inputMethodCheckBox.setText("\u8f93\u5165\u6cd5");
        add(inputMethodCheckBox);
        inputMethodCheckBox.setBounds(155, 170, 90, 30);

        //---- signCheckBox ----
        signCheckBox.setText("\u4e2a\u7b7e");
        add(signCheckBox);
        signCheckBox.setBounds(265, 170, 90, 30);

        //---- retypeCheckBox ----
        retypeCheckBox.setText("\u91cd\u6253");
        add(retypeCheckBox);
        retypeCheckBox.setBounds(45, 215, 90, 30);

        //---- pauseCheckBox ----
        pauseCheckBox.setText("\u6682\u505c");
        add(pauseCheckBox);
        pauseCheckBox.setBounds(155, 215, 90, 30);

        //---- systemCheckBox ----
        systemCheckBox.setText("\u7cfb\u7edf");
        add(systemCheckBox);
        systemCheckBox.setBounds(265, 215, 90, 30);

        //---- typeMethodCheckBox ----
        typeMethodCheckBox.setText("\u6307\u6cd5");
        add(typeMethodCheckBox);
        typeMethodCheckBox.setBounds(375, 215, 90, 30);

        //---- phraseCountCombo ----
        phraseCountCombo.setText("\u8bcd\u7387");
        add(phraseCountCombo);
        phraseCountCombo.setBounds(45, 260, 90, 30);

        //---- simplifyModelCheckBox ----
        simplifyModelCheckBox.setText("\u6781\u7b80\u6a21\u5f0f");
        simplifyModelCheckBox.setToolTipText("\u6781\u7b80\u6a21\u5f0f\u7684\u6210\u7ee9\u5355\u53ea\u6709\u4e00\u884c\uff0c\u80fd\u5c3d\u6700\u5927\u53ef\u80fd\u51cf\u5c11\u5237\u5c4f\u7a0b\u5ea6\u3002");
        add(simplifyModelCheckBox);
        simplifyModelCheckBox.setBounds(375, 30, 110, 30);

        //---- inputMethodTipLabel ----
        inputMethodTipLabel.setText("\u8f93\u5165\u6cd5\uff1a");
        inputMethodTipLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(inputMethodTipLabel);
        inputMethodTipLabel.setBounds(200, 260, 75, 30);
        add(inputMethodTextField);
        inputMethodTextField.setBounds(290, 260, 150, 30);

        //---- signTipLabel ----
        signTipLabel.setText("\u4e2a\u7b7e\uff1a");
        signTipLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(signTipLabel);
        signTipLabel.setBounds(45, 350, 75, 30);
        add(signTextField);
        signTextField.setBounds(120, 350, 320, 30);

        //---- keyBoardTipLabel ----
        keyBoardTipLabel.setText("\u952e\u76d8\uff1a");
        keyBoardTipLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(keyBoardTipLabel);
        keyBoardTipLabel.setBounds(200, 305, 75, 30);
        add(keyBoardTextField);
        keyBoardTextField.setBounds(290, 305, 150, 30);

        //---- typeMethodTipLabel ----
        typeMethodTipLabel.setText("\u6307\u6cd5\uff1a");
        typeMethodTipLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(typeMethodTipLabel);
        typeMethodTipLabel.setBounds(45, 400, 75, 30);

        //---- spaceCombo ----
        spaceCombo.setText("\u5de6\u624b");
        spaceCombo.setModel(new DefaultComboBoxModel<>(new String[] {
            "\u5de6\u624b",
            "\u53f3\u624b",
            "\u4e0d\u7edf\u8ba1"
        }));
        add(spaceCombo);
        spaceCombo.setBounds(120, 400, 90, 30);

        //---- spaceTipLabel ----
        spaceTipLabel.setText("\u7a7a\u683c");
        add(spaceTipLabel);
        spaceTipLabel.setBounds(215, 400, 45, 30);

        //---- bCombo ----
        bCombo.setText("\u5de6\u624b");
        bCombo.setModel(new DefaultComboBoxModel<>(new String[] {
            "\u5de6\u624b",
            "\u53f3\u624b",
            "\u4e0d\u7edf\u8ba1"
        }));
        add(bCombo);
        bCombo.setBounds(300, 400, 90, 30);

        //---- bTipLabel ----
        bTipLabel.setText("B");
        add(bTipLabel);
        bTipLabel.setBounds(400, 400, 45, 30);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < getComponentCount(); i++) {
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
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}