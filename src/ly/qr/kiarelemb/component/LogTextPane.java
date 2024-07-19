package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import method.qr.kiarelemb.utils.QRLoggerUtils;
import method.qr.kiarelemb.utils.QRTimeCountUtil;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.basic.QRTextPane;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.utils.QRComponentUtils;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className QRLogTextPane
 * @description TODO
 * @create 2024/7/12 下午10:30
 */
public class LogTextPane extends QRTextPane {
    public static final LogTextPane LOG_TEXT_PANE = new LogTextPane();

    private LogTextPane() {
        setEditableFalseButCursorEdit();
        setLineWrap(false);
        setLineSpacing(0.5f);

        SimpleAttributeSet s = QRComponentUtils.getSimpleAttributeSet(textFont, QRColorsAndFonts.TEXT_COLOR_FORE, QRColorsAndFonts.TEXT_COLOR_BACK);
        this.sas = new SimpleAttributeSet();
        StyleConstants.setFontSize(this.sas, textFont.getSize());
        StyleConstants.setFontFamily(this.sas, textFont.getFamily());
        StyleConstants.setBold(this.sas, false);
        if (!QRSwing.windowImageSet) StyleConstants.setBackground(sas, QRColorsAndFonts.TEXT_COLOR_BACK);
        TextStyleManager.attributeCopy(s, this.sas, StyleConstants.Foreground);
    }

    public void init() {
        QRTimeCountUtil qcu = new QRTimeCountUtil((short) 100);
        AtomicReference<String> preText = new AtomicReference<>("");
        QRLoggerUtils.addMessageOutputAction(str -> {
            String clearedStr = str.replace("\tINFO\t\t", "\t").replace("\tCONFIG\t\t", "\t").replaceAll("\t{2,}", "\n\t");
            if (qcu.isPassedMmTime() || !preText.get().equals(clearedStr)) {
                qcu.getAndUpdate();
                preText.set(clearedStr);
                print(clearedStr);
            }
        });
    }

    private final SimpleAttributeSet sas;

    @Override
    public void print(String str, Font f, Color colorFore, Color colorBack, int index) {
        print(str, sas, index);
    }
}