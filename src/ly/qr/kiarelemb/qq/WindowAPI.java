package ly.qr.kiarelemb.qq;

import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WindowAPI {
    public static ArrayList<String> getQQWindows() {
        ArrayList<String> allWindows = getAllWindows();
//        return allWindows;
        ArrayList<String> QQWindows = new ArrayList<>();
        for (String window : allWindows) {
            if (window.equals("QQ")) {
                QQWindows.add(window);
                continue;
            }
            HWND hWnd = User32.INSTANCE.FindWindow("TXGuiFoundation", window);
            if (hWnd != null) {
                QQWindows.add(window);
            }
        }
        return QQWindows;
    }

    private static ArrayList<String> getAllWindows() {
        ArrayList<String> name = new ArrayList<>();
        try {
            final List<DesktopWindow> list = WindowUtils.getAllWindows(true);
            for (DesktopWindow dd : list) {
                HWND wnd = dd.getHWND();
                Rectangle rr = WindowUtils.getWindowLocationAndSize(wnd);
                if (rr.contains(-32000, -32000) || WindowUtils.getWindowTitle(wnd).isBlank()) {
                    continue;
                }
                name.add(WindowUtils.getWindowTitle(wnd));
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return name;
    }
}
