package ly.qr.kiarelemb.menu.send;

import ly.qr.kiarelemb.component.TextViewPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.menu.MenuItem;
import ly.qr.kiarelemb.text.send.TextSendManager;

import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className ForeParaTextItem
 * @description TODO
 * @create 2024/4/16 21:23
 */
public class ForeParaTextItem extends MenuItem {
	public static final ForeParaTextItem FORE_PARA_TEXT_ITEM = new ForeParaTextItem();

	private ForeParaTextItem() {
		super("上一段", Keys.QUICK_KEY_SEND_PARA_FORE);
		setEnabled(false);
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		if (TextSendManager.sendingText()) {
			String text = TextSendManager.data().gotoForePara().foreParaText();
			TextViewPane.TEXT_VIEW_PANE.setTypeText(text);
		}
	}
}