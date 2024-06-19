package ly.qr.kiarelemb;

import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.component.setting.SettingWindow;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.tip.TextTip;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import method.qr.kiarelemb.utils.*;
import swing.qr.kiarelemb.QRSwing;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @apiNote: 揽月跟打器主方法类
 * @create 2023-01-12 22:51
 **/
public class Enter {

    private static Logger logger;

    public static void main(String[] args) {
        // 初始化 JDK 自带的 Logger
//        initLogger();
        QRLoggerUtils.prefix = "ly";
        QRLoggerUtils.initLogger(Level.INFO, Level.CONFIG);
        QRLoggerUtils.classMsgMaxLength = 120;
        logger = QRLoggerUtils.getLogger(Enter.class);
        logger.info("************************************** 揽月开始启动 **************************************");
        QRTimeCountUtil qcu = new QRTimeCountUtil();
        QRSwing.start("res/settings/setting.properties", "res/settings/window.properties");
        logger.info("QRSwing 框架加载完毕，" + qcu.endAndGet());

        QRSwing.windowIcon = Info.loadImage(Info.ICON_PNG_PATH);

        FlashLoadingWindow flw = new FlashLoadingWindow();
        flw.setVisible(true);

        variousLoad();

        logger.info("当前系统：" + QRSystemUtils.getSystemName());
        if (QRSystemUtils.IS_WINDOWS) {
            QRSwing.setGlobalKeyEventsListener(TyperTextPane.TYPER_TEXT_PANE.globalKeyListener, MainWindow.INSTANCE);
        }

//        if (Keys.boolValue(Keys.WINDOW_BACKGROUND_IMAGE_ENABLE)) {
//            MainWindow.INSTANCE.setBackgroundImage(Keys.strValue(QRSwing.WINDOW_IMAGE_PATH));
//            MainWindow.INSTANCE.setBackgroundBorderAlpha(QRSwing.windowBackgroundImageAlpha);
//        }

        flw.setVisible(false);

        logger.info("-------------------------------------- 配置加载完毕 --------------------------------------");
        QRSystemUtils.setWindowShowSlowly(MainWindow.INSTANCE, QRSwing.windowTransparency);
    }

    private static void variousLoad() {

        //region 全局界面字体
        Font font = null;
        boolean fontEnable = Keys.boolValue(Keys.TEXT_FONT_NAME_GLOBAL_ENABLE);
        if (fontEnable) {
            String fontNameOrPath = Keys.strValue(Keys.TEXT_FONT_NAME_GLOBAL);
            if (QRFileUtils.fileExists(fontNameOrPath)) {
                logger.config("加载字体：" + fontNameOrPath);
                font = QRFontUtils.loadFontFromFile(10, fontNameOrPath);
            } else {
                String[] names = QRFontUtils.getSystemFontNames();
                if (QRArrayUtils.objectIndexOf(names, fontNameOrPath) != -1) {
                    font = QRFontUtils.getFont(fontNameOrPath, 10);
                    logger.config("加载字体：" + fontNameOrPath);
                }
            }
        }
        if (font == null) {
            //全局默认字体即为阿里巴巴普惠体
            font = TextStyleManager.DEFAULT_FONT;
            logger.config("加载默认字体：" + font.getFontName());
        }
        QRSwing.customFontName(font);
        //endregion 全局界面字体

        //提前加载一遍试试
        SettingWindow.INSTANCE.setVisible(false);
        logger.config("设置窗口预加载完毕。");

        //加载一下词提
        TextTip.TEXT_TIP.load();

//        TextStyleManager.updateAll();
    }

    private static void initLogger() {


        LocalDate now = LocalDate.now();
        String separator = File.separator;
        String dir = "logs" + separator + now.format(DateTimeFormatter.ofPattern("yyyy.MM")) + separator;
        QRFileUtils.dirCreate(dir);
        String logFilePath = dir + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log";
        QRFileUtils.fileCreate(logFilePath);

        ConsoleHandler consoleHandler;
        FileHandler fileHandler;
        Level outputLevel = Level.INFO;
        Level writeLevel = Level.CONFIG;
        int classMsgMaxLength = 120;
        Formatter formatter = new Formatter() {


            @Override
            public String format(LogRecord record) {
                String dataFormat = QRTimeUtils.dateAndTimeMMFormat.format(Long.valueOf(record.getMillis()));
                StackTraceElement[] trace = Thread.currentThread().getStackTrace();
                StackTraceElement stackTrace = trace[0];
                for (StackTraceElement element : trace) {
                    if (element.getClassName().startsWith("ly")) {
                        stackTrace = element;
                        break;
                    }
                }
                String levelTmp = record.getLevel().toString();
                String level = levelTmp + "\t".repeat((8 - levelTmp.length()) / 4 + (levelTmp.length() % 4 == 0 ? 0 : 1));

                String classTmp = String.format("[%s:%s] %s:%d", stackTrace.getClassName(), stackTrace.getMethodName(),
                        stackTrace.getFileName(), stackTrace.getLineNumber());
                int restLen = classMsgMaxLength - classTmp.length();
                int times = restLen / 4 + (classTmp.length() % 4 == 0 ? 0 : 1);
                String classMsg = classTmp + "\t".repeat(Math.max(times, 0));
                String msg = String.format("%s\t%s\t%s\t%s\n", dataFormat, level, classMsg, record.getMessage());
                String throwable = "";
                if (record.getThrown() != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    pw.println();
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    throwable = sw.toString();
                }
                return msg + throwable;
            }
        };

        QRLoggerUtils.outputLevel = outputLevel;
        consoleHandler = new ConsoleHandler() {

            private String preRecord = "";

            @Override
            public synchronized void publish(LogRecord record) {
                String format = getFormatter().format(record);
                if (format.equals(preRecord)) {
                    return;
                }
                preRecord = format;
                super.publish(record);
            }
        };
        consoleHandler.setLevel(outputLevel);
        consoleHandler.setFormatter(formatter);
        try {
            fileHandler = new FileHandler(logFilePath, true) {

                private String preRecord = "";

                @Override
                public synchronized void publish(LogRecord record) {
                    String format = getFormatter().format(record);
                    if (format.equals(preRecord)) {
                        return;
                    }
                    preRecord = format;
                    super.publish(record);
                }
            };
            QRLoggerUtils.writeLevel = writeLevel;
            fileHandler.setLevel(writeLevel);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        QRLoggerUtils.logFilePath = logFilePath;
        QRLoggerUtils.outputLevel = outputLevel;
        QRLoggerUtils.writeLevel = writeLevel;
        QRLoggerUtils.classMsgMaxLength = classMsgMaxLength;
        QRLoggerUtils.consoleHandler = consoleHandler;
        QRLoggerUtils.fileHandler = fileHandler;
    }
}