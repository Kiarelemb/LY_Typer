package ly.qr.kiarelemb.key;

import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.component.TextViewPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.text.TextLoad;
import method.qr.kiarelemb.utils.QRFontUtils;
import swing.qr.kiarelemb.inter.QRActionRegister;

import java.awt.event.KeyEvent;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className ActionLibrary
 * @description 用于存放诸多操作的库
 * @create 2024/7/31 下午11:17
 */
public class ActionLibrary {
    private ActionLibrary() {
        throw new UnsupportedOperationException();
    }

    /**
     * for class {@link TextViewPane}
     */
    public static final QRActionRegister<Object> TEXT_VIEW_PANE_UPDATE_ACTION = e -> {
        //更新数据
        TypingData.dataUpdate();
        //重置数据
        TypingData.clear();
        boolean visible = ContractiblePanel.SILKY_MODEL_CHECK_BOX.checked() && !ContractiblePanel.LOOK_MODEL_CHECK_BOX.checked();
        TextViewPane.TEXT_VIEW_PANE.caret.setVisible(visible);
    };

    /**
     * for class {@link TyperTextPane}
     */
    public static final QRActionRegister<Object> TYPER_TEXT_PANE_UPDATE_ACTION = e -> {
        TextViewPane.TEXT_VIEW_PANE.clear();
        TextViewPane.TEXT_VIEW_PANE.caret.setVisible(true);
        TextViewPane.TEXT_VIEW_PANE.setFont(QRFontUtils.getFont(TypingData.fontName, TypingData.typefontSize));
    };

    /**
     * 该操作存放的位置可以决定能否在 Mac 和 Linux 上实现单框跟打
     */
    public static final QRActionRegister<KeyEvent> KEY_TYPE_ACTION = (e) -> {
        if (TextLoad.TEXT_LOAD == null || TyperTextPane.keyCheck(e) || !TypingData.typing) {
            e.consume();
            return;
        }
        char keyChar = e.getKeyChar();
        if (keyChar == KeyEvent.VK_BACK_SPACE) {
            TextViewPane.TEXT_VIEW_PANE.deleteUpdates(e);
            return;
        }
        TextViewPane.TEXT_VIEW_PANE.insertUpdates(keyChar);
    };
}