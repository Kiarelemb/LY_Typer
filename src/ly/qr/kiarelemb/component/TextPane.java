package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.data.TypingData;
import swing.qr.kiarelemb.basic.QRTextPane;

import java.awt.event.MouseEvent;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TextPane
 * @description TODO
 * @create 2024/7/11 下午10:09
 */
public class TextPane extends QRTextPane {
    public TextPane() {
        addMouseListener();
    }

    @Override
    protected void mouseClick(MouseEvent e) {
        TypingData.windowFresh();
    }

    @Override
    protected void mousePress(MouseEvent e) {
        TypingData.windowFresh();
    }
}