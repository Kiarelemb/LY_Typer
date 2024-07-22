package ly.qr.kiarelemb.menu.type;

import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.menu.MenuItem;

import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className GroupMenuItem
 * @description TODO
 * @create 2024/7/21 下午7:30
 */
public class GroupMenuItem extends MenuItem {
    public static final GroupMenuItem GROUP_MENU_ITEM = new GroupMenuItem();
    private GroupMenuItem() {
        super("换群", Keys.QUICK_KEY_GROUP);
    }

    protected void actionEvent(ActionEvent o) {
        ContractiblePanel.GROUP_BUTTON.clickInvokeLater();
    }
}