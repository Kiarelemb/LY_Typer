/*
 * Created by JFormDesigner on Fri Feb 09 16:52:01 CST 2024
 */

package ly.qr.kiarelemb.test;

import swing.qr.kiarelemb.component.utils.QRKeyBoardPanel;

import javax.swing.*;
import java.awt.*;

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
        qRKeyBoardPanel1 = new QRKeyBoardPanel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(20, 20));

        //======== panel1 ========
        {
            panel1.setLayout(null);
            panel1.add(qRKeyBoardPanel1);
            qRKeyBoardPanel1.setBounds(25, 280, 1285, 530);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel1.getComponentCount(); i++) {
                    Rectangle bounds = panel1.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel1.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel1.setMinimumSize(preferredSize);
                panel1.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panel1;
    private QRKeyBoardPanel qRKeyBoardPanel1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}