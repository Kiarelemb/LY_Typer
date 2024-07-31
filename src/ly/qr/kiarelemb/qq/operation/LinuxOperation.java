package ly.qr.kiarelemb.qq.operation;

import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.component.GroupButton;
import method.qr.kiarelemb.utils.QRSleepUtils;

import java.awt.*;
import java.io.IOException;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className LinuxOperation
 * @description TODO
 * @create 2024/7/28 上午8:27
 */
public class LinuxOperation extends OperationAbs {
    @Override
    public void start(int model, String nameOrId, Robot robot) {
        Runtime runtime = Runtime.getRuntime();
        try {
            String command = "xdotool windowactivate " + GroupButton.linuxQQWindowId;
            runtime.exec(command);
            QRSleepUtils.sleep(100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (model == GET_ARTICLE_MODEL) {
            pressCopy(robot);
        } else if (model == SEND_ACHIEVEMENT_MODEL) {
            pressPaste(robot);

        }
        //将跟打器置顶
        if (GroupButton.linuxLYWindowId != null) {
            runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdotool windowactivate " + GroupButton.linuxLYWindowId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean textCanSend(int model) {
        if (!ContractiblePanel.GROUP_BUTTON.groupLinked()) {
            ContractiblePanel.GROUP_BUTTON.clickInvokeLater();
            return GroupButton.linuxQQWindowId != null;
        }
        return true;
    }
}