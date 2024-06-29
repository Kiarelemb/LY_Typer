package ly.qr.kiarelemb.text.send;

import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.menu.send.*;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.send.data.TypedData;
import method.qr.kiarelemb.utils.QRLoggerUtils;
import method.qr.kiarelemb.utils.QRSerializeUtils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TextSendManager
 * @description TODO
 * @create 2024/4/16 19:32
 */
public class TextSendManager {
    private static final Logger logger = QRLoggerUtils.getLogger(TextSendManager.class);
    private static TypedData data;

    public static TypedData data() {
        return data;
    }

    public static void setData(TypedData data) {
        if (data != null && TextSendManager.data != data) {
            TextSendManager.data = data;
        }
    }

    public static void save() {
        TextSendManager.serializeData(TextSendManager.data);
    }

    /**
     * @return 判断当前是否在发文状态
     */
    public static boolean sendingText() {
        return TextSendManager.data != null;
    }

    private static void serializeData(TypedData data) {
        String filePath = Info.TYPE_DIRECTORY + data.fileName() + ".bin";
        try {
            QRSerializeUtils.writeObject(filePath, data);
            QRLoggerUtils.log(logger, Level.INFO, "跟打文件 %s 保存成功！", data.filePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName 带拓展名的文件名
     * @return 是否加载成功
     */
    public static boolean loadData(String fileName) {
        TypedData typedData = loadSerializedData(fileName);
        if (typedData == null) {
            logger.warning("加载失败：" + fileName);
            return false;
        }
        data = typedData;
        logger.config("加载成功：" + fileName);
        return true;
    }

    /**
     * @param fileName 带拓展名的文件名
     * @return 结果
     */
    public static TypedData loadSerializedData(String fileName) {
        String binPath = Info.TYPE_DIRECTORY + fileName + ".bin";
        try {
            return (TypedData) QRSerializeUtils.readObject(binPath);
        } catch (Exception e) {
            return null;
        }
    }


    public static void setTypedData(TypedData data) {
        setData(data);
        TextPane.TEXT_PANE.setTypeText(data.nextParaText());
        TextSendManager.control(true);
        logger.info("开始发文：" + data);
        //TODO 在此处添加跟打结束事件
    }

    /**
     * 结束发文
     */
    public static void endSendText() {
        setData(null);
        control(false);
        logger.info("发文结束！");
    }

    private static void control(boolean enable) {
        boolean disable = !enable;
        NewSendTextItem.NEW_SEND_TEXT_ITEM.setEnabled(disable);
        ContinueSendTextItem.CONTINUE_SEND_TEXT_ITEM.setEnabled(disable);
        EndSendTextItem.END_SEND_TEXT_ITEM.setEnabled(enable);
        ForeParaTextItem.FORE_PARA_TEXT_ITEM.setEnabled(enable);
        NextParaTextItem.NEXT_PARA_TEXT_ITEM.setEnabled(enable);
    }
}