package ly.qr.kiarelemb.component;

import com.sun.jna.platform.win32.User32;
import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.qq.WindowAPI;
import ly.qr.kiarelemb.res.Info;
import method.qr.kiarelemb.utils.QRLoggerUtils;
import method.qr.kiarelemb.utils.QRSystemUtils;
import method.qr.kiarelemb.utils.QRThreadBuilder;
import mmarquee.automation.AutomationException;
import mmarquee.demo.NTQQAutomation;
import swing.qr.kiarelemb.basic.QRRoundButton;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className GroupButton
 * @description TODO
 * @create 2024/7/30 上午7:58
 */
public class GroupButton extends QRRoundButton {
    private static final ThreadPoolExecutor GROUP_CHANGE = QRThreadBuilder.singleThread("groupChange");

    private static final Logger LOGGER = QRLoggerUtils.getLogger(GroupButton.class);
private final String tip;
    GroupButton() {
        String key = Keys.strValue(Keys.QUICK_KEY_GROUP);
        String[] keys = key.split(",");
        tip = key + " / 点击换群";
        setText(keys[0] + " / 点击换群");
        setToolTipText(tip);
    }

    private final ArrayList<String> windows = new ArrayList<>();
    /**
     * 是否已找到群聊
     */
    private boolean groupLinked = false;
    /**
     * 找到的群聊名
     */
    private String groupName;
    /**
     * 是否为QQ NT
     */
    public boolean isQQNT = false;
    private int groupIndex = -1;
    public static String linuxQQWindowId;
    public static String linuxLYWindowId;
    private static NTQQAutomation ntQQAutomation;

    static {
        if (Info.IS_WINDOWS) {
            try {
                ntQQAutomation = new NTQQAutomation();
            } catch (Exception e) {
                ntQQAutomation = null;
            }
        }
    }

    @Override
    protected void actionEvent(ActionEvent o) {
        //放线程就不会卡
        if (Info.IS_WINDOWS) {
            GROUP_CHANGE.submit(() -> {
                List<String> windows = WindowAPI.getQQWindows();
                List<String> nameList = new ArrayList<>();
                if (User32.INSTANCE.FindWindow("TXGuiFoundation", "QQ") != null || !windows.contains("QQ")) {
                    // 如果是 NTQQ ，则 windows 列表里只会有 "QQ" 这个一个元素
                    isQQNT = false;
                    nameList.addAll(windows);
                } else {
                    isQQNT = true;
                    // Windows 8 及以上支持
                    if (ntQQAutomation != null) {
                        try {
                            nameList.addAll(ntQQAutomation.getSessionNameList());
                        } catch (AutomationException e) {
                            findFailed();
                            return;
                        }
                    }
                }

                LOGGER.info("群聊列表：" + nameList);
                if (this.groupName() != null) {
                    this.groupIndex = nameList.indexOf(this.groupName());
                }
                this.windows.clear();
                this.windows.addAll(nameList);

                int size = this.windows.size();
                if (size == 0) {
                    findFailed();
                    return;
                }
                this.groupIndex++;
                if (this.groupIndex == size) {
                    this.groupIndex = 0;
                }
                // 根据索引设置群聊名
                this.setGroupName(this.windows.get(this.groupIndex));
                Matcher matcher = Pattern.compile("等[0-9]+个会话").matcher(this.groupName());
                if (matcher.find()) {
                    var trimName = this.groupName().substring(0, matcher.start());
                    setText(trimName);
                    setToolTipText(String.format("%s (%s)", trimName, tip));
                } else {
                    setText(this.groupName());
                    setToolTipText(String.format("%s (%s)", this.groupName(), tip));
                }
                this.setGroupLinked(true);
            });
        } else if (Info.IS_LINUX) {
            OptionalInt first;
            try {
                first = QRSystemUtils.getSystemProcessInfo().stream()
                        .filter(info -> info.getName().equals("qq"))
                        .mapToInt(info -> Integer.parseInt(info.getPid()))
                        .sorted()
                        .findFirst();
            } catch (Exception e) {
                findFailed();
                return;
            }
            if (first.isEmpty()) {
                findFailed();
                return;
            }

            int pid = first.getAsInt();
            Runtime runtime = Runtime.getRuntime();
            try {
                Process exec = runtime.exec("xdotool search --onlyvisible --pid " + pid);
                InputStream stream = exec.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                linuxQQWindowId = reader.readLine();
                reader.close();
                if (linuxQQWindowId != null) {
                    this.setGroupLinked(true);
                    this.isQQNT = true;
                    setText("QQ");
                    InputStream inputStream = runtime.exec("xdotool search --name 揽月").getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    linuxLYWindowId = reader.readLine();
                    reader.close();
                }
            } catch (IOException e) {
                findFailed();
                throw new RuntimeException(e);
            }
        }
    }

    private void findFailed() {
        this.setGroupLinked(false);
        QRSmallTipShow.display(MainWindow.INSTANCE, "没有找到群聊");
    }

    /**
     * 找到的群聊名
     */
    public String groupName() {
        return this.groupName;
    }

    public boolean isQQNT() {
        return this.isQQNT;
    }

    private void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 是否已找到群聊
     */
    public boolean groupLinked() {
        return this.groupLinked;
    }

    private void setGroupLinked(boolean groupLinked) {
        this.groupLinked = groupLinked;
    }
}