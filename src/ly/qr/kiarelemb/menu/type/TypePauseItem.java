package ly.qr.kiarelemb.menu.type;

import ly.qr.kiarelemb.menu.MenuItem;
import ly.qr.kiarelemb.data.Keys;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TypePauseItem
 * @description TODO
 * @create 2024/6/27 下午9:50
 */
public class TypePauseItem extends MenuItem {
    public TypePauseItem(String text) {
        super(text, Keys.SEND_TIMES_PAUSE);
    }
}