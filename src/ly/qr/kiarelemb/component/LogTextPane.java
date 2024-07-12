package ly.qr.kiarelemb.component;

import method.qr.kiarelemb.utils.QRLoggerUtils;
import method.qr.kiarelemb.utils.QRTimeCountUtil;
import swing.qr.kiarelemb.basic.QRTextArea;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className QRLogTextPane
 * @description TODO
 * @create 2024/7/12 下午10:30
 */
public class LogTextPane extends QRTextArea {
    public static final LogTextPane LOG_TEXT_PANE = new LogTextPane();

    private LogTextPane() {
//        setEditableFalseButCursorEdit();
        setLineWrap(false);
        PlainDocument doc = new PlainDocument();
        setDocument(doc);
        doc.putProperty(PlainDocument.tabSizeAttribute, 4);
    }

    public void init() {
        QRTimeCountUtil qcu = new QRTimeCountUtil((short) 100);
        AtomicReference<String> preText = new AtomicReference<>("");
        QRLoggerUtils.addMessageOutputAction(str -> {
            String clearedStr = str.replace("\tINFO\t\t", "\t").replace("\tCONFIG\t\t", "\t").replaceAll("\t{2,}","\n\t");
            if (qcu.isPassedMmTime() || !preText.get().equals(clearedStr)) {
                qcu.getAndUpdate();
                preText.set(clearedStr);
                Document d = getDocument();
                try {
                    d.insertString(d.getLength(), clearedStr, null);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}