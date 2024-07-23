package ly.qr.kiarelemb.dl;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import method.qr.kiarelemb.utils.QRStringUtils;
import swing.qr.kiarelemb.basic.QRScrollPane;
import swing.qr.kiarelemb.basic.QRTextArea;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.utils.QRComponentUtils;
import swing.qr.kiarelemb.window.basic.QRDialog;
import swing.qr.kiarelemb.window.basic.QRFrame;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className DangLangWindow
 * @description 当量显示窗体
 * @create 2024/3/21 21:05
 */
public class DangLangWindow extends QRDialog {
    public final LogTextPane logTextPane = new LogTextPane();
    private static DangLangWindow dangLangWindow;

    private DangLangWindow(Window parent) {
        super(parent, false);
        setTitle("当量统计器");
        setTitlePlace(QRDialog.CENTER);
        mainPanel.setLayout(new BorderLayout());
        QRScrollPane scroll = logTextPane.addScrollPane();
        mainPanel.add(scroll, BorderLayout.CENTER);
        scroll.addLineNumberModelForTextPane();
        setResizable(true);
        setSize(320, 500);
        addWindowListener();
        setParentWindowNotFollowMove();
        ((QRFrame) parent).addWindowMoveAction(p -> setLocation(p.x - getWidth() - 10, p.y));
    }

    public static DangLangWindow dangLangWindow() {
        if (dangLangWindow == null) {
            dangLangWindow = new DangLangWindow(MainWindow.INSTANCE);
        }
        return dangLangWindow;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        setLocation(parent.getX() - getWidth() - 10, parent.getY());
    }

    public static class LogTextPane extends QRTextArea {
        final SimpleAttributeSet CHINESE_SET;
        final SimpleAttributeSet ENGLISH_SET;

        public LogTextPane() {
            CHINESE_SET = QRComponentUtils.getSimpleAttributeSet(QRColorsAndFonts.STANDARD_FONT_TEXT,
                    QRColorsAndFonts.TEXT_COLOR_FORE, QRColorsAndFonts.FRAME_COLOR_BACK);
            ENGLISH_SET = QRComponentUtils.getSimpleAttributeSet(TextStyleManager.PREFERRED_ENGLISH_FONT_NAME,
                    QRColorsAndFonts.STANDARD_FONT_TEXT.getSize(), QRColorsAndFonts.STANDARD_FONT_TEXT.getStyle(),
                    QRColorsAndFonts.TEXT_COLOR_FORE, QRColorsAndFonts.FRAME_COLOR_BACK);
            setEditable(false);
        }

        public void noneChinesePrint(String text) {
            print(text, ENGLISH_SET);
        }

        public void chinesePrint(String text) {
            print(text, CHINESE_SET);
        }

        public void noneChinesePrintln(String text) {
            print(QRStringUtils.AN_ENTER.concat(text), ENGLISH_SET);
            SwingUtilities.invokeLater(() -> {
                JScrollBar bar = scrollPane.getVerticalScrollBar();
                int max = bar.getMaximum();
                bar.setValue(max);
            });
        }

        public void chinesePrintln(String text) {
            print(QRStringUtils.AN_ENTER.concat(text), CHINESE_SET);
        }

        public void print(String text, SimpleAttributeSet attributeSet) {
            try {
                Document model = getDocument();
                model.insertString(model.getLength(), text, attributeSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}