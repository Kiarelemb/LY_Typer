package ly.qr.kiarelemb.qq;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.qq.operation.LinuxOperation;
import ly.qr.kiarelemb.qq.operation.MacOperation;
import ly.qr.kiarelemb.qq.operation.Operation;
import ly.qr.kiarelemb.qq.operation.WindowsOperation;
import ly.qr.kiarelemb.res.Info;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;

import java.awt.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2024-07-28 00:13
 **/
public class QqOperation {
    public static final Operation operation;

    static {
        operation = Info.IS_WINDOWS ? new WindowsOperation() : Info.IS_LINUX ? new LinuxOperation() : new MacOperation();
    }

    public static void start(int model, String Name) {
        Robot robot;
        try {
            robot = new Robot();
            operation.start(model, Name, robot);
        } catch (AWTException e) {
            QRSmallTipShow.display(MainWindow.INSTANCE,"操作失败，请重试！");
        }
    }
}