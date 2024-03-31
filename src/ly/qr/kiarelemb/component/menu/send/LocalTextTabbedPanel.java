/*
 * Created by JFormDesigner on Sun Mar 31 22:28:32 CST 2024
 */

package ly.qr.kiarelemb.component.menu.send;

import ly.qr.kiarelemb.text.send.data.TypedData;
import swing.qr.kiarelemb.component.combination.QRClearableTextField;

import java.awt.*;

/**
 * @author Kiarelemb QR
 */
public class LocalTextTabbedPanel extends TabbedContentPanel {
	public LocalTextTabbedPanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		qRClearableTextField1 = new QRClearableTextField(false);

		//======== this ========
		setLayout(null);
		add(qRClearableTextField1);
		qRClearableTextField1.setBounds(20, 35, 290, 35);

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
	private QRClearableTextField qRClearableTextField1;@Override
	public TypedData getTypedData() {
		return null;
	}
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}