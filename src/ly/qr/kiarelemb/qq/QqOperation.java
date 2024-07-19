package ly.qr.kiarelemb.qq;

import com.sun.jna.platform.WindowUtils;
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
        WinDef.HWND hWnd = User32.INSTANCE.FindWindow(null, Name);
//        QRComponentUtils.runLater(10, e -> {
            genda = User32.INSTANCE.FindWindow(null, MainWindow.INSTANCE.getTitle());
            double SCALE = (double) Toolkit.getDefaultToolkit().getScreenResolution() / 96;
            QRSleepUtils.sleep(50);
            if (hWnd != null) {
                User32.INSTANCE.SetForegroundWindow(hWnd);
                WinDef.RECT rect = new WinDef.RECT();
                User32.INSTANCE.GetWindowRect(hWnd, rect);
                String title = WindowUtils.getWindowTitle(hWnd);
                boolean isQQNT = title != null && title.equals("QQ");
                if (model == GET_ARTICLE_MODEL) {
                    if (!isQQNT) {
                        robot.mouseMove((int) ((rect.left + rect.right) / 2 / SCALE), (int) ((rect.top + rect.bottom) / 2 / SCALE));
                    }
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    QRSleepUtils.sleep(30);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    QRSleepUtils.sleep(80);
                    robot.keyPress(VK_CONTROL);
                    QRSleepUtils.sleep(80);
//                    if (!isQQNT) {
                        robot.keyPress(VK_A);
                        QRSleepUtils.sleep(80);
                        robot.keyRelease(VK_A);
                        QRSleepUtils.sleep(80);
//                    }
                    robot.keyPress(VK_C);
                    QRSleepUtils.sleep(80);
                    robot.keyRelease(VK_C);
                    robot.keyRelease(VK_CONTROL);
//                    Point location = TyperTextPane.TYPER_TEXT_PANE.getLocationOnScreen();
//                    robot.mouseMove((int) ((location.x + TyperTextPane.TYPER_TEXT_PANE.getWidth() / 2) / SCALE), (int) ((location.getY() + TyperTextPane.TYPER_TEXT_PANE.getHeight() / 2) / SCALE));
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
                    robot.mouseMove((int) ((location.x + TyperTextPane.TYPER_TEXT_PANE.getWidth() / 2) / SCALE), (int) ((location.getY() + TyperTextPane.TYPER_TEXT_PANE.getHeight() / 2) / SCALE));
                }
                //将跟打器置顶
                User32.INSTANCE.SetForegroundWindow(genda);
            }
//        });
    }

    public static boolean textCanSend() {
        if (!Info.IS_WINDOWS) {
            QRSmallTipShow.display(MainWindow.INSTANCE, "载文仅支持 Windows！");
            return false;
        }
        if (!ContractiblePanel.GROUP_BUTTON.groupLinked()) {
            ArrayList<String> group = WindowAPI.getQQWindows();
            if (group.size() == 1) {
                ContractiblePanel.GROUP_BUTTON.clickInvokeLater();
                return true;
            }
//			QRSmallTipShow.display(MainWindow.INSTANCE, "请按 F5 选择群聊！");
            return false;
        }
        return true;
    }
}