package ly.qr.kiarelemb.qq;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import method.qr.kiarelemb.utils.QRSleepUtils;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.event.KeyEvent.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-24 00:13
 **/
public class QqOperation {
    public static final int GET_ARTICLE_MODEL = 1;
    public static final int SEND_ACHIEVEMENT_MODEL = 2;
    public static WinDef.HWND genda;

    public static void start(int model, String Name) {
        if (!textCanSend()) {
            return;
        }
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            QRSmallTipShow.display(MainWindow.INSTANCE, "操作失败，请重试！");
            return;
        }
        boolean isQQNT = ContractiblePanel.GROUP_BUTTON.isQQNT;
        double SCALE = 0;
        if (Info.IS_WINDOWS) {
            WinDef.HWND hWnd = User32.INSTANCE.FindWindow(null, Name);
            if (hWnd == null) {
                return;
            }
            genda = User32.INSTANCE.FindWindow(null, MainWindow.INSTANCE.getTitle());

            SCALE = (double) Toolkit.getDefaultToolkit().getScreenResolution() / 96;
            QRSleepUtils.sleep(50);
            User32.INSTANCE.SetForegroundWindow(hWnd);
            WinDef.RECT rect = new WinDef.RECT();
            User32.INSTANCE.GetWindowRect(hWnd, rect);
            if (model == GET_ARTICLE_MODEL) {
                if (!isQQNT) {
                    robot.mouseMove((int) ((rect.left + rect.right) / 2f / SCALE), (int) ((rect.top + rect.bottom) / 2f / SCALE));
                }
            }
        } else if (Info.IS_LINUX) {
            Runtime runtime = Runtime.getRuntime();
            try {
                String command = "xdotool windowactivate " + ContractiblePanel.GroupButton.linuxQQWindowId;
                runtime.exec(command);
                QRSleepUtils.sleep(100);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (model == GET_ARTICLE_MODEL) {
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
            QRSleepUtils.sleep(500);
        } else if (model == SEND_ACHIEVEMENT_MODEL) {
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
            Point location = TyperTextPane.TYPER_TEXT_PANE.getLocationOnScreen();

            if (SCALE != 0) {
                robot.mouseMove((int) ((location.x + TyperTextPane.TYPER_TEXT_PANE.getWidth() / 2f) / SCALE), (int) ((location.getY() + TyperTextPane.TYPER_TEXT_PANE.getHeight() / 2f) / SCALE));
            }
        }
        //将跟打器置顶
        if (genda != null) {
            User32.INSTANCE.SetForegroundWindow(genda);
        } else if (ContractiblePanel.GroupButton.linuxLYWindowId != null) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdotool windowactivate " + ContractiblePanel.GroupButton.linuxLYWindowId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean textCanSend() {
        if (Info.IS_MACOS) {
            QRSmallTipShow.display(MainWindow.INSTANCE, "与 QQ 联动暂不支持 MacOS");
            return false;
        }
        if (!ContractiblePanel.GROUP_BUTTON.groupLinked()) {
            if (Info.IS_WINDOWS) {
                ArrayList<String> group = WindowAPI.getQQWindows();
                if (group.size() == 1) {
                    ContractiblePanel.GROUP_BUTTON.clickInvokeLater();
                    return true;
                }
            } else {
                ContractiblePanel.GROUP_BUTTON.clickInvokeLater();
                return ContractiblePanel.GroupButton.linuxQQWindowId != null;
            }
            return false;
        }
        return true;
    }
}