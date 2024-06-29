package ly.qr.kiarelemb.menu.send;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.menu.MenuItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.send.ContinueSendWindow;
import ly.qr.kiarelemb.text.send.TextSendManager;
import method.qr.kiarelemb.utils.QRFileUtils;
import swing.qr.kiarelemb.window.enhance.QROpinionDialog;

import java.awt.event.ActionEvent;
import java.util.LinkedList;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className ContinueSendTextItem
 * @description TODO
 * @create 2024/4/17 21:04
 */
public class ContinueSendTextItem extends MenuItem {
    public static final ContinueSendTextItem CONTINUE_SEND_TEXT_ITEM = new ContinueSendTextItem();

    private ContinueSendTextItem() {
        super("继续发文", Keys.QUICK_KEY_SEND_CONTINUE);
    }

    @Override
    protected void actionEvent(ActionEvent o) {
        LinkedList<String> list = new LinkedList<>();
        QRFileUtils.dirLoop(Info.TYPE_DIRECTORY, file -> {
            if (".bin".equals(QRFileUtils.getFileExtension(file))
                && QRFileUtils.fileExists(Info.TYPE_DIRECTORY + QRFileUtils.getFileName(file) + ".txt")) {
                list.add(QRFileUtils.getFileName(file));
            }
        });

        if (list.isEmpty()) {
            QROpinionDialog.messageTellShow(MainWindow.INSTANCE, "无继续发文内容！");
            return;
        }
        int size = list.size();
        String fileName;
        if (size == 1) {
            fileName = list.getFirst();
        } else {
            ContinueSendWindow continueSendWindow = new ContinueSendWindow(list);
            continueSendWindow.setVisible(true);
            if (continueSendWindow.selectedIndex() == -1 || !continueSendWindow.isContinue()) {
                return;
            }
            fileName = list.get(continueSendWindow.selectedIndex());
        }
        if (TextSendManager.loadData(fileName)) {
            TextSendManager.setTypedData(TextSendManager.data());
        }
    }
}