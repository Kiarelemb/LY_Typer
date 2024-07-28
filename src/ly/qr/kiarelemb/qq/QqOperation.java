package ly.qr.kiarelemb.qq;

import ly.qr.kiarelemb.qq.operation.LinuxOperation;
import ly.qr.kiarelemb.qq.operation.MacOperation;
import ly.qr.kiarelemb.qq.operation.Operation;
import ly.qr.kiarelemb.qq.operation.WindowsOperation;
import ly.qr.kiarelemb.res.Info;

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
        operation.start(model, Name);
    }
}