/*
 * Created by JFormDesigner on Fri Feb 09 14:12:04 CST 2024
 */

package ly.qr.kiarelemb.test;

import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRRoundButton;
import swing.qr.kiarelemb.component.utils.QRLineSeparatorLabel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kiarelemb QR
 */
public class CodeStatistics extends JPanel {
	public CodeStatistics() {
		initComponents();
	}


	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		stanSingleLabelTip = new QRLabel();
		stanSingleLabel = new QRLabel();
		firstMultLabelTip = new QRLabel();
		firstMultLabel = new QRLabel();
		separator1 = new QRLineSeparatorLabel();
		oneLabelTip = new QRLabel();
		oneLabel = new QRLabel();
		twoLabelTip = new QRLabel();
		twoLabel = new QRLabel();
		threeLabelTip = new QRLabel();
		threeLabel = new QRLabel();
		fourLabelTip = new QRLabel();
		fourLabel = new QRLabel();
		separator2 = new QRLineSeparatorLabel();
		allSpaceLabelTip = new QRLabel();
		allSpaceLabel = new QRLabel();
		leftRightLabelTip = new QRLabel();
		leftRightLabel = new QRLabel();
		viewTypeImageBtn = new QRRoundButton();
		singlePhraseLabelTip = new QRLabel();
		singlePhraseLabel = new QRLabel();

		//======== this ========
		setLayout(null);

		//---- stanSingleLabelTip ----
		stanSingleLabelTip.setText("\u6807\u9876 : \u5355\u5b57");
		add(stanSingleLabelTip);
		stanSingleLabelTip.setBounds(15, 20, 85, 30);

		//---- stanSingleLabel ----
		stanSingleLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		stanSingleLabel.setBackground(new Color(0xccff99));
		add(stanSingleLabel);
		stanSingleLabel.setBounds(110, 20, 115, 30);

		//---- firstMultLabelTip ----
		firstMultLabelTip.setText("\u9996\u9009 : \u9009\u91cd");
		add(firstMultLabelTip);
		firstMultLabelTip.setBounds(15, 60, 85, 30);

		//---- firstMultLabel ----
		firstMultLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		add(firstMultLabel);
		firstMultLabel.setBounds(110, 60, 115, 30);

		//---- separator1 ----
		separator1.setText("text");
		add(separator1);
		separator1.setBounds(15, 135, 220, 20);

		//---- oneLabelTip ----
		oneLabelTip.setText("\u4e00\u9996 : \u4e00\u91cd");
		add(oneLabelTip);
		oneLabelTip.setBounds(15, 160, 85, 30);

		//---- oneLabel ----
		oneLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		oneLabel.setBackground(new Color(0xccff99));
		add(oneLabel);
		oneLabel.setBounds(110, 160, 115, 30);

		//---- twoLabelTip ----
		twoLabelTip.setText("\u4e8c\u9996 : \u4e8c\u91cd");
		add(twoLabelTip);
		twoLabelTip.setBounds(15, 200, 85, 30);

		//---- twoLabel ----
		twoLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		add(twoLabel);
		twoLabel.setBounds(110, 200, 115, 30);

		//---- threeLabelTip ----
		threeLabelTip.setText("\u4e09\u9996 : \u4e09\u91cd");
		add(threeLabelTip);
		threeLabelTip.setBounds(15, 240, 85, 30);

		//---- threeLabel ----
		threeLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		threeLabel.setBackground(new Color(0xccff99));
		add(threeLabel);
		threeLabel.setBounds(110, 240, 115, 30);

		//---- fourLabelTip ----
		fourLabelTip.setText("\u56db\u9996 : \u56db\u91cd");
		add(fourLabelTip);
		fourLabelTip.setBounds(15, 280, 85, 30);

		//---- fourLabel ----
		fourLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		add(fourLabel);
		fourLabel.setBounds(110, 280, 115, 30);

		//---- separator2 ----
		separator2.setText("text");
		add(separator2);
		separator2.setBounds(15, 315, 220, 20);

		//---- allSpaceLabelTip ----
		allSpaceLabelTip.setText("\u603b\u952e : \u7a7a\u683c");
		add(allSpaceLabelTip);
		allSpaceLabelTip.setBounds(15, 340, 85, 30);

		//---- allSpaceLabel ----
		allSpaceLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		allSpaceLabel.setBackground(new Color(0xccff99));
		add(allSpaceLabel);
		allSpaceLabel.setBounds(110, 340, 115, 30);

		//---- leftRightLabelTip ----
		leftRightLabelTip.setText("\u5de6\u624b : \u53f3\u624b");
		add(leftRightLabelTip);
		leftRightLabelTip.setBounds(15, 380, 85, 30);

		//---- leftRightLabel ----
		leftRightLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		add(leftRightLabel);
		leftRightLabel.setBounds(110, 380, 115, 30);

		//---- viewTypeImageBtn ----
		viewTypeImageBtn.setText("\u67e5\u770b\u6807\u9876\u6253\u6cd5");
		add(viewTypeImageBtn);
		viewTypeImageBtn.setBounds(15, 425, 220, 30);

		//---- singlePhraseLabelTip ----
		singlePhraseLabelTip.setText("\u5355\u91cf : \u8bcd\u91cf");
		add(singlePhraseLabelTip);
		singlePhraseLabelTip.setBounds(15, 100, 85, 30);

		//---- singlePhraseLabel ----
		singlePhraseLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		add(singlePhraseLabel);
		singlePhraseLabel.setBounds(110, 100, 115, 30);

		setPreferredSize(new Dimension(250, 475));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	private QRLabel stanSingleLabelTip;
	private QRLabel stanSingleLabel;
	private QRLabel firstMultLabelTip;
	private QRLabel firstMultLabel;
	private QRLineSeparatorLabel separator1;
	private QRLabel oneLabelTip;
	private QRLabel oneLabel;
	private QRLabel twoLabelTip;
	private QRLabel twoLabel;
	private QRLabel threeLabelTip;
	private QRLabel threeLabel;
	private QRLabel fourLabelTip;
	private QRLabel fourLabel;
	private QRLineSeparatorLabel separator2;
	private QRLabel allSpaceLabelTip;
	private QRLabel allSpaceLabel;
	private QRLabel leftRightLabelTip;
	private QRLabel leftRightLabel;
	private QRRoundButton viewTypeImageBtn;
	private QRLabel singlePhraseLabelTip;
	private QRLabel singlePhraseLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}