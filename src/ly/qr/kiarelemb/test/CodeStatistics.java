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
		this.stanSingleLabelTip = new QRLabel();
		this.stanSingleLabel = new QRLabel();
		this.firstMultLabelTip = new QRLabel();
		this.firstMultLabel = new QRLabel();
		this.separator1 = new QRLineSeparatorLabel();
		this.oneLabelTip = new QRLabel();
		this.oneLabel = new QRLabel();
		this.twoLabelTip = new QRLabel();
		this.twoLabel = new QRLabel();
		this.threeLabelTip = new QRLabel();
		this.threeLabel = new QRLabel();
		this.fourLabelTip = new QRLabel();
		this.fourLabel = new QRLabel();
		this.separator2 = new QRLineSeparatorLabel();
		this.allSpaceLabelTip = new QRLabel();
		this.allSpaceLabel = new QRLabel();
		this.leftRightLabelTip = new QRLabel();
		this.leftRightLabel = new QRLabel();
		this.viewTypeImageBtn = new QRRoundButton();

		//======== this ========
		setLayout(null);

		//---- stanSingleLabelTip ----
		this.stanSingleLabelTip.setText("\u6807\u9876 : \u5355\u5b57");
		add(this.stanSingleLabelTip);
		this.stanSingleLabelTip.setBounds(15, 20, 85, 30);

		//---- stanSingleLabel ----
		this.stanSingleLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		this.stanSingleLabel.setBackground(new Color(0xccff99));
		add(this.stanSingleLabel);
		this.stanSingleLabel.setBounds(110, 20, 115, 30);

		//---- firstMultLabelTip ----
		this.firstMultLabelTip.setText("\u9996\u9009 : \u9009\u91cd");
		add(this.firstMultLabelTip);
		this.firstMultLabelTip.setBounds(15, 60, 85, 30);

		//---- firstMultLabel ----
		this.firstMultLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		add(this.firstMultLabel);
		this.firstMultLabel.setBounds(110, 60, 115, 30);

		//---- separator1 ----
		this.separator1.setText("text");
		add(this.separator1);
		this.separator1.setBounds(15, 95, 275, 20);

		//---- oneLabelTip ----
		this.oneLabelTip.setText("\u4e00\u9996 : \u4e00\u91cd");
		add(this.oneLabelTip);
		this.oneLabelTip.setBounds(15, 120, 85, 30);

		//---- oneLabel ----
		this.oneLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		this.oneLabel.setBackground(new Color(0xccff99));
		add(this.oneLabel);
		this.oneLabel.setBounds(110, 120, 115, 30);

		//---- twoLabelTip ----
		this.twoLabelTip.setText("\u4e8c\u9996 : \u4e8c\u91cd");
		add(this.twoLabelTip);
		this.twoLabelTip.setBounds(15, 160, 85, 30);

		//---- twoLabel ----
		this.twoLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		add(this.twoLabel);
		this.twoLabel.setBounds(110, 160, 115, 30);

		//---- threeLabelTip ----
		this.threeLabelTip.setText("\u4e09\u9996 : \u4e09\u91cd");
		add(this.threeLabelTip);
		this.threeLabelTip.setBounds(15, 200, 85, 30);

		//---- threeLabel ----
		this.threeLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		this.threeLabel.setBackground(new Color(0xccff99));
		add(this.threeLabel);
		this.threeLabel.setBounds(110, 200, 115, 30);

		//---- fourLabelTip ----
		this.fourLabelTip.setText("\u56db\u9996 : \u56db\u91cd");
		add(this.fourLabelTip);
		this.fourLabelTip.setBounds(15, 240, 85, 30);

		//---- fourLabel ----
		this.fourLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		add(this.fourLabel);
		this.fourLabel.setBounds(110, 240, 115, 30);

		//---- separator2 ----
		this.separator2.setText("text");
		add(this.separator2);
		this.separator2.setBounds(15, 275, 220, 20);

		//---- allSpaceLabelTip ----
		this.allSpaceLabelTip.setText("\u603b\u952e : \u7a7a\u683c");
		add(this.allSpaceLabelTip);
		this.allSpaceLabelTip.setBounds(15, 300, 85, 30);

		//---- allSpaceLabel ----
		this.allSpaceLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		this.allSpaceLabel.setBackground(new Color(0xccff99));
		add(this.allSpaceLabel);
		this.allSpaceLabel.setBounds(110, 300, 115, 30);

		//---- leftRightLabelTip ----
		this.leftRightLabelTip.setText("\u5de6\u624b : \u53f3\u624b");
		add(this.leftRightLabelTip);
		this.leftRightLabelTip.setBounds(15, 340, 85, 30);

		//---- leftRightLabel ----
		this.leftRightLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
		add(this.leftRightLabel);
		this.leftRightLabel.setBounds(110, 340, 115, 30);

		//---- viewTypeImageBtn ----
		this.viewTypeImageBtn.setText("\u67e5\u770b\u6807\u9876\u6253\u6cd5");
		add(this.viewTypeImageBtn);
		this.viewTypeImageBtn.setBounds(15, 385, 220, 30);

		setPreferredSize(new Dimension(250, 435));
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
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}