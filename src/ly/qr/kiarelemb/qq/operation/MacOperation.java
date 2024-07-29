package ly.qr.kiarelemb.qq.operation;

import ly.qr.kiarelemb.MainWindow;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;

import java.awt.*;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className MacOperation
 * @description TODO
 * @create 2024/7/28 上午8:29
 */
public class MacOperation extends OperationAbs {
    @Override
    public void start(int model, String nameOrId, Robot robot) {
        if (model == GET_ARTICLE_MODEL) {
            // mac F4 鼠标载文
            pressCopy(robot);
        } else if (model == SEND_ACHIEVEMENT_MODEL) {
            //pressPaste(robot);
        }
        //将跟打器置顶
    }

    @Override
    public boolean textCanSend(int model) {
        if (model == SEND_ACHIEVEMENT_MODEL) {
            QRSmallTipShow.display(MainWindow.INSTANCE, "与 QQ 联动暂不支持 MacOS");
            return false;
        }
        return true;
    }
}