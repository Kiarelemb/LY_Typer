package ly.qr.kiarelemb.component.menu.send.panel;

import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.component.menu.send.ForeParaTextItem;
import ly.qr.kiarelemb.component.menu.send.NextParaTextItem;
import ly.qr.kiarelemb.component.menu.send.SendTextItem;
import ly.qr.kiarelemb.text.send.SendWindow;
import ly.qr.kiarelemb.text.send.TextSendManager;
import ly.qr.kiarelemb.text.send.data.TypedData;
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

	public void setTypedData(TypedData data) {
		TextSendManager.setData(data);
		TextPane.TEXT_PANE.setTypeText(data.nextParaText());
		control(true);
		//TODO 在此处添加跟打结束事件
	}

	/**
	 * 结束发文
	 */
	public static void endSendText() {
		control(false);
	}

	private static void control(boolean enable) {
		SendTextItem.SEND_TEXT_ITEM.setEnabled(!enable);
		ForeParaTextItem.FORE_PARA_TEXT_ITEM.setEnabled(enable);
		NextParaTextItem.NEXT_PARA_TEXT_ITEM.setEnabled(enable);
	}
}