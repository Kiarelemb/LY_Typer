package ly.qr.kiarelemb.menu.type;

import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.component.TextViewPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.menu.MenuItem;
import ly.qr.kiarelemb.qq.LoadText;
import ly.qr.kiarelemb.qq.QqOperation;
import ly.qr.kiarelemb.res.Info;
import swing.qr.kiarelemb.utils.QRComponentUtils;

import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-25 15:13
 **/
public class LoadTextItem extends MenuItem {
    public static final LoadTextItem LOAD_TEXT_ITEM = new LoadTextItem();

    private LoadTextItem() {
        super("载文", Keys.QUICK_KEY_MENU_TYPE_TEXT_LOAD, false);
        setEnabled(Info.IS_WINDOWS);
    }

    @Override
    protected void actionEvent(ActionEvent o) {
        if (ContractiblePanel.GROUP_BUTTON.groupLinked()) {
            QqOperation.start(QqOperation.GET_ARTICLE_MODEL, ContractiblePanel.GROUP_BUTTON.groupName());
            QRComponentUtils.runLater(500, e -> {
                String text = null;
//                if (ContractiblePanel.GROUP_BUTTON.isQQNT) {

//                } else {
                    text = LoadText.getLoadText();
//                }
                if (text != null && !text.isEmpty()) {
                    TextViewPane.TEXT_VIEW_PANE.setTypeText(text);
                }
            });
            return;
        }
        TyperTextPane.TYPER_TEXT_PANE.paste();
    }
}