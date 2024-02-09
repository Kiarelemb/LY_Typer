/*
 * Created by JFormDesigner on Fri Feb 09 16:52:01 CST 2024
 */

package ly.qr.kiarelemb.test;

import java.awt.*;
import javax.swing.*;
import swing.qr.kiarelemb.component.basic.*;

/**
 * @author Kiarelemb QR
 */
public class StandardWindow extends JFrame {
	public StandardWindow() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		panel1 = new JPanel();
		qRTextPane1 = new QRTextPane();
		qRPanel2 = new QRPanel();

		//======== this ========
		var contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(20, 20));

		//======== panel1 ========
		{
			panel1.setLayout(new BorderLayout());
		}
		contentPane.add(panel1, BorderLayout.NORTH);
		contentPane.add(qRTextPane1, BorderLayout.CENTER);
		contentPane.add(qRPanel2, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	private JPanel panel1;
	private QRTextPane qRTextPane1;
	private QRPanel qRPanel2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}