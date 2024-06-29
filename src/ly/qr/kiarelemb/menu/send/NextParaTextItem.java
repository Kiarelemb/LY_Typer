package ly.qr.kiarelemb.menu.send;

import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.menu.MenuItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.send.TextSendManager;

import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className NextParaTextItem
 * @description TODO
 * @create 2024/4/16 21:23
 */
public class NextParaTextItem extends MenuItem {
	public static final NextParaTextItem NEXT_PARA_TEXT_ITEM = new NextParaTextItem();

	private NextParaTextItem() {
		super("下一段", Keys.QUICK_KEY_SEND_NEXT_PARA);
		setEnabled(false);
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		if (TextSendManager.sendingText()) {
			String text = TextSendManager.data().gotoNextPara().nextParaText();
			TextPane.TEXT_PANE.setTypeText(text);
		}
	}
}