package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.basic.QRTextPane;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.utils.QRComponentUtils;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TextPane
 * @description TODO
 * @create 2024/7/19 下午7:04
 */
public class TextPane extends QRTextPane {
    public TextPane() {
        SimpleAttributeSet s = QRComponentUtils.getSimpleAttributeSet(textFont, QRColorsAndFonts.TEXT_COLOR_FORE, QRColorsAndFonts.TEXT_COLOR_BACK);
        this.sas = new SimpleAttributeSet();
        StyleConstants.setFontSize(this.sas, textFont.getSize());
        StyleConstants.setFontFamily(this.sas, textFont.getFamily());
        StyleConstants.setBold(this.sas, false);
        if (!QRSwing.windowImageSet) StyleConstants.setBackground(sas, QRColorsAndFonts.TEXT_COLOR_BACK);
        TextStyleManager.attributeCopy(s, this.sas, StyleConstants.Foreground);

        setEditableFalseButCursorEdit();
        setLineWrap(false);
        setOpaque(false);
        setLineSpacing(0.5f);
    }

    private final SimpleAttributeSet sas;

    @Override
    public void print(String str, Font f, Color colorFore, Color colorBack, int index) {
        print(str, sas, index);
    }
}