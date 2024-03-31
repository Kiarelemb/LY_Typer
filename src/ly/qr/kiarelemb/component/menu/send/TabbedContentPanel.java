package ly.qr.kiarelemb.component.menu.send;

import ly.qr.kiarelemb.text.send.data.TypedData;
import swing.qr.kiarelemb.component.combination.QRTabbedContentPanel;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TabbedContentPanel
 * @description TODO
 * @create 2024/3/31 22:19
 */
public abstract class TabbedContentPanel extends QRTabbedContentPanel {
	public abstract TypedData getTypedData();
}