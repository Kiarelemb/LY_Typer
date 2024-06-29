package ly.qr.kiarelemb.menu.type;

import ly.qr.kiarelemb.menu.MenuItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.TextLoad;

import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TextMixItem
 * @description TODO
 * @create 2024/4/15 19:01
 */
public class TextMixItem extends MenuItem {
	public static final TextMixItem TEXT_MIX_ITEM = new TextMixItem();

	private TextMixItem() {
		super("乱序", Keys.QUICK_KEY_TEXT_MIX);
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		if (TextLoad.TEXT_LOAD != null) {
			TextLoad.TEXT_LOAD.actualContentMix();
		}
	}
}