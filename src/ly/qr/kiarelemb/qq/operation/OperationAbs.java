package ly.qr.kiarelemb.qq.operation;

import ly.qr.kiarelemb.data.Keys;
import method.qr.kiarelemb.utils.QRSleepUtils;

import java.awt.*;
import java.awt.event.InputEvent;

import static java.awt.event.KeyEvent.*;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className OperationAbs
 * @description TODO
 * @create 2024/7/28 上午8:21
 */
public abstract class OperationAbs implements Operation {
    public static final int GET_ARTICLE_MODEL = 1;
    public static final int SEND_ACHIEVEMENT_MODEL = 2;
    protected void pressCopy(Robot robot){
        System.out.println("copy");
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        QRSleepUtils.sleep(30);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        QRSleepUtils.sleep(80);
        robot.keyPress(VK_CONTROL);
        QRSleepUtils.sleep(80);
        robot.keyPress(VK_A);
        QRSleepUtils.sleep(80);
        robot.keyRelease(VK_A);
        QRSleepUtils.sleep(80);
        robot.keyPress(VK_C);
        QRSleepUtils.sleep(80);
        robot.keyRelease(VK_C);
        robot.keyRelease(VK_CONTROL);
    }

    protected void pressPaste(Robot robot){
        robot.keyPress(VK_CONTROL);
        robot.keyPress(VK_V);
        robot.keyRelease(VK_V);
        robot.keyRelease(VK_CONTROL);
        boolean ctrlEnter = Keys.intValue(Keys.TYPE_SEND_KEY) == 1;
        if (ctrlEnter) {
            robot.keyPress(VK_CONTROL);
        }
        QRSleepUtils.sleep(120);
        robot.keyPress(VK_ENTER);
        QRSleepUtils.sleep(30);
        robot.keyRelease(VK_ENTER);
        if (ctrlEnter) {
            robot.keyRelease(VK_CONTROL);
        }
    }
}