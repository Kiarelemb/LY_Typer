package ly.qr.kiarelemb.qq.operation;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.qq.WindowAPI;
import method.qr.kiarelemb.utils.QRSleepUtils;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className WindowsOperation
 * @description TODO
 * @create 2024/7/28 上午8:22
 */
public class WindowsOperation extends OperationAbs {

    public WindowsOperation() {
    }

    @Override
    public void start(int model, String nameOrId, Robot robot) {
//        Main.main(null);
        boolean isQQNT = ContractiblePanel.GROUP_BUTTON.isQQNT;
        WinDef.HWND hWnd = User32.INSTANCE.FindWindow(null, nameOrId);
        if (hWnd == null) {
            return;
        }
        WinDef.HWND genda = User32.INSTANCE.FindWindow(null, MainWindow.INSTANCE.getTitle());
        double SCALE = Toolkit.getDefaultToolkit().getScreenResolution() / 96d;
        QRSleepUtils.sleep(50);
        User32.INSTANCE.SetForegroundWindow(hWnd);
        WinDef.RECT rect = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect(hWnd, rect);
        if (model == GET_ARTICLE_MODEL) {
            if (!isQQNT) {
                robot.mouseMove((int) ((rect.left + rect.right) / 2d / SCALE), (int) ((rect.top + rect.bottom) / 2d / SCALE));
            }
            pressCopy(robot);
        } else if (model == SEND_ACHIEVEMENT_MODEL) {
            pressPaste(robot);
            Point location = TyperTextPane.TYPER_TEXT_PANE.getLocationOnScreen();
            robot.mouseMove((int) ((location.x + TyperTextPane.TYPER_TEXT_PANE.getWidth() / 2d) / SCALE), (int) ((location.getY() + TyperTextPane.TYPER_TEXT_PANE.getHeight() / 2d) / SCALE));
        }
        //将跟打器置顶
        User32.INSTANCE.SetForegroundWindow(genda);
    }

    @Override
    protected void pressCopy(Robot robot) {

        super.pressCopy(robot);
    }

    @Override
    public boolean textCanSend(int model) {
        if (!ContractiblePanel.GROUP_BUTTON.groupLinked()) {
            ArrayList<String> group = WindowAPI.getQQWindows();
            if (group.size() == 1) {
                ContractiblePanel.GROUP_BUTTON.clickInvokeLater();
                return true;
            }
            QRSmallTipShow.display(MainWindow.INSTANCE, "请先换群！");
            return false;
        }
        return true;
    }
}