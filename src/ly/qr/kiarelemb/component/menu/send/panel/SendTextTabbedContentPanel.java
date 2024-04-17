package ly.qr.kiarelemb.component.menu.send.panel;

import ly.qr.kiarelemb.text.send.SendWindow;
import swing.qr.kiarelemb.component.combination.QRTabbedContentPanel;
import swing.qr.kiarelemb.inter.QRActionRegister;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className SendTextTabbedContentPanel
 * @description TODO
 * @create 2024/3/31 22:19
 */
public class SendTextTabbedContentPanel extends QRTabbedContentPanel {
	protected final SendWindow window;
	//TODO 在此处设置发文时，跟打结束后的操作
	private static final QRActionRegister E = e -> {

	};

	public SendTextTabbedContentPanel(SendWindow window) {
		this.window = window;
	}

}