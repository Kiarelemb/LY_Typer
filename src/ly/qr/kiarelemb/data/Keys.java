package ly.qr.kiarelemb.data;

import ly.qr.kiarelemb.res.Info;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRStringUtils;
import method.qr.kiarelemb.utils.QRTimeUtils;
import swing.qr.kiarelemb.resource.QRSwingInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @apiNote : 各键的存放位置
 * @create 2023-01-22 14:16
 **/
public class Keys {

    //region 读取配置的静态方法

    /**
     * 读取配置
     *
     * @param key 键
     * @return 值
     */
    public static int intValue(String key) {
        return QRSwingInfo.intValue(DEFAULT_MAP, key);
    }

    /**
     * 读取配置
     *
     * @param key 键
     * @return 值
     */
    public static boolean boolValue(String key) {
        return QRSwingInfo.boolValue(DEFAULT_MAP, key);
    }

    /**
     * 读取配置
     *
     * @param key 键
     * @return 值
     */
    public static String strValue(String key) {
        return QRSwingInfo.strValue(DEFAULT_MAP, key);
    }

    /**
     * 读取配置
     *
     * @param key 键
     * @return 值
     */
    public static float floatValue(String key) {
        return QRSwingInfo.floatValue(DEFAULT_MAP, key);
    }
    //endregion

    //region int类型
    /**
     * 记录主窗体分割面板的分割值
     */
    public static final String WINDOW_SPLIT_WEIGHT = "window.split.weight";
    /**
     * 繁简转换键，{@code 0} （默认）不转换，{@code 1} 繁转简，{@code 2} 简转繁
     */
    public static final String TEXT_SIMPLE_TRADITIONAL_CONVERT = "text.simple.traditional.convert";
    /**
     * 词提方案的码长，{@code 0} 为四码方案，{@code 1} （默认）为三码方案，{@code 2} 为42顶的三码方案
     */
    public static final String TEXT_TIP_CODE_LENGTH = "text.tip.code.length";
    /**
     * 看打区字体大小，默认为 {@code 28}
     */
    public static final String TEXT_FONT_SIZE_LOOK = "text.font.size.look";
    /**
     * 跟打区的字体大小，默认为 {@code 28}
     */
    public static final String TEXT_FONT_SIZE_TYPE = "text.font.size.type";
    /**
     * 跟打数据的更新，{@code 0} （默认）实时更新，{@code 1} 每秒更新，{@code 2} 每五秒更新
     */
    public static final String TYPE_STATISTICS_UPDATE = "type.statistics.update";
    /**
     * 发文回车键，{@code 0} （默认）Enter，{@code 1} Ctrl Enter
     */
    public static final String TYPE_SEND_KEY = "type.send.key";
    /**
     * 总打字个数
     */
    public static final String TYPE_WORD_TOTAL = "type.word.total";
    /**
     * 今日打字个数
     */
    public static final String TYPE_WORD_TODAY = "type.word.today";
    /**
     * 字数自动保存的时间间隔（单位：分钟），默认值为 {@code 5}
     */
    public static final String TYPE_WORD_AUTO_SAVE_MINUTE = "type.word.auto.save.minute";
    /**
     * 瞬时速度统计的个数，默认值为 {@code 3}
     */
    public static final String TYPE_WORD_SPEED_COUNT = "type.word.speed.count";

    /**
     * 键法之空格，{@code 0} （默认）左手空格，{@code 1} 右手空格，{@code 2} 不统计
     */
    public static final String TYPE_KEY_METHOD_SPACE = "type.method.key.space";
    /**
     * 键法之B，{@code 0} （默认）左手按B，{@code 1} 右手按B，{@code 2} 不统计
     */
    public static final String TYPE_KEY_METHOD_B = "type.method.key.b";
    /**
     * 编码提示面板的位置，{@code 0} 看打区上方，{@code 1} （默认）看打区下方，{@code 2} 跟打区上方，{@code 3} 跟打区下方
     */
    public static final String TEXT_TIP_PANEL_LOCATION = "text.tip.panel.location";
    /**
     * 编码提示窗口的位置，{@code 0} （默认）跟随光标，{@code 1} 固定于窗体上方居中
     */
    public static final String TEXT_TIP_WINDOW_LOCATION = "text.tip.window.location";
    /**
     * 发文时默认的开始段号选择，{@code 0} （默认）从 1 开始，{@code 1} 随机段号
     */
    public static final String TEXT_SEND_START_PARA = "text.send.start.para";
    /**
     * 发文时默认的每段字数，{@code 50} （默认）50字
     */
    public static final String TEXT_SEND_START_WORD_NUM = "text.send.start.word.num";
    //endregion int类型

    //region boolean 类型
    //region 成绩单发送
    public static final String SEND_ENTER_COUNT = "send.enter.count";
    public static final String SEND_METHOD_INPUT = "send.method.input";
    //	public static final String SEND_IS_SIMPLIFY = "send.isSimplify";
    public static final String SEND_KEY_ACCURACY = "send.key.accuracy";
    public static final String SEND_KEY_METHOD = "send.key.method";
    public static final String SEND_TIMES_PAUSE = "send.times.pause";
    public static final String SEND_TIMES_RETYPE = "send.times.retype";
    public static final String SEND_KEY_NUM = "send.key.num";
    public static final String SEND_KEYBOARD = "send.keyboard";
    public static final String SEND_METHOD_KEY = "send.method.key";
    public static final String SEND_SIGNATURE = "send.signature";
    public static final String SEND_SYSTEM_VERSION = "send.system.version";
    public static final String SEND_METHOD_TYPE = "send.method.type";
    public static final String SEND_TIME_COST = "send.time.cost";
    public static final String SEND_WORDS_NUM = "send.words.num";
    public static final String SEND_WORD_WRONG = "send.word.wrong";
    /**
     * 新建发文窗口默认选择的标签，{@code 0} （默认）跟随光标，{@code 1} 固定于窗体上方居中
     */
    public static final String SEND_TEXT_NEW_WINDOW_TAB_INDEX = "send.text.new.window.tab.index";
    /**
     * 极简模式
     */
    public static final String SEND_MINIMALISM = "send.minimalism";
    /**
     * 加密发文
     */
    public static final String SEND_CRYPTOGRAPHIC = "send.cryptographic";
    public static final String SEND_BACK_CHANGE = "send.back.change";
    public static final String SEND_BACKSPACE = "send.backspace";
    //endregion 成绩单发送
    /**
     * 智能载文，{@code false} 基本不格式化文本，{@code true} （默认）智能载文
     */
    public static final String TEXT_LOAD_INTELLI = "text.load.intelli";
    /**
     * 词提启用，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TEXT_TIP_ENABLE = "text.tip.enable";
    /**
     * 是否使用增强的词提模式，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TEXT_TIP_ENHANCE = "text.tip.enhance";
    /**
     * 是否显示编码提示面板，{@code false} （默认）不显示，{@code true} 显示
     */
    public static final String TEXT_TIP_PANEL_ENABLE = "text.tip.panel.enable";
    /**
     * 是否显示编码提示窗口，{@code false} （默认）不显示，{@code true} 显示
     */
    public static final String TEXT_TIP_WINDOW_ENABLE = "text.tip.window.enable";
    /**
     * 编码提示面板是否分行显示，{@code false} 单行显示，{@code true} （默认）分行显示
     */
    public static final String TEXT_TIP_DIVIDE = "text.tip.divide";
    /**
     * 是否着色，{@code false} 不启用，{@code true} （默认）启用
     */
    public static final String TEXT_TIP_PAINT_COLOR = "text.tip.paint.color";
    /**
     * 是否提示选重，{@code false} 不启用，{@code true} （默认）启用
     */
    public static final String TEXT_TIP_PAINT_SELECTION = "text.tip.paint.selection";
    /**
     * 是否提示编码，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TEXT_TIP_PAINT_CODE = "text.tip.paint.code";
    /**
     * 是否单字也启用词提‘，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TEXT_TIP_CHAR_ENABLE = "text.tip.char.enable";
    /**
     * 是否在跟打暂停时最小化窗口，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String WINDOW_PAUSE_MINIMIZE = "window.pause.minimize";
    /**
     * 全文无错，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TYPE_END_CONDITION_NO_WRONG = "type.end.condition.no.wrong";
    /**
     * 重打或乱序，{@code false} 不启用，{@code true} （默认）启用
     */
    public static final String TYPE_END_MIX_RESTART = "type.end.mix.restart";
    /**
     * 看打模式，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TYPE_MODEL_LOOK = "type.model.look";
    /**
     * 退格自动重打，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TEXT_TYPE_BACKSPACE_AUTO_RESTART = "text.type.backspace.auto.restart";
    /**
     * 丝滑模式，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TYPE_SILKY_MODEL = "type.silky.model";
    /**
     * 瞬时计算，{@code false} 不启用，{@code true} （默认）启用
     */
    public static final String TYPE_DATA_INSTANTANEOUS_VELOCITY = "type.data.instantaneous.velocity";
    /**
     * 潜水模式，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TYPE_DIVE_MODEL = "type.dive.model";
    /**
     * 屏蔽未知按键，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TYPE_DISCARD_UNKNOWN_KEY = "type.discard.unknown.key";
    /**
     * 跟打数据是否已折叠，{@code false} （默认）不折叠，{@code true} 折叠
     */
    public static final String WINDOW_COLUMN_FOLD_TYPING_STATISTICS = "window.column.fold.typing.statistics";
    /**
     * 当前状态是否已折叠，{@code false} （默认）不折叠，{@code true} 折叠
     */
    public static final String WINDOW_COLUMN_FOLD_STATE_INFO = "window.column.fold.state.info";
    /**
     * 本段信息是否已折叠，{@code false} （默认）不折叠，{@code true} 折叠
     */
    public static final String WINDOW_COLUMN_FOLD_PARA_INFO = "window.column.fold.para.info";
    /**
     * 标顶数据是否已折叠，{@code false} （默认）不折叠，{@code true} 折叠
     */
    public static final String WINDOW_COLUMN_FOLD_STANDARD_STATISTICS = "window.column.fold.standard.statistics";
    /**
     * 背景图的高强度刷新，{@code false} （默认）不启用，{@code true} 启用
     */
    @Deprecated
    public static final String WINDOW_BACKGROUND_FRESH_ENHANCEMENT = "window.background.fresh.enhancement";


    /**
     * 全局的界面字体，{@code false} （默认）不启用，{@code true} 启用
     */
    public static final String TEXT_FONT_NAME_GLOBAL_ENABLE = "text.font.name.global.enable";
    /**
     * 单字发文时，是否随机抽取，{@code false} （默认）不启用
     */
    public static final String TEXT_SEND_SINGLE_RANDOM_PICK = "text.send.single.random.pick";
    /**
     * 单字发文时，是否乱序文本，{@code false} （默认）不启用
     */
    public static final String TEXT_SEND_SINGLE_RANDOM_TEXT = "text.send.single.random.text";
    /**
     * 单字发文时，是否自定义文本，{@code false} （默认）不启用
     */
    public static final String TEXT_SEND_SINGLE_CUSTOM_TEXT = "text.send.single.custom.text";
    //endregion boolean 类型

    //region String 类型
    /**
     * 看打区字体名称，默认为 {@code 黑体}
     */
    public static final String TEXT_FONT_NAME_LOOK = "text.font.name.look";
    /**
     * 跟打区字体名称，默认为 {@code 黑体}
     */
    public static final String TEXT_FONT_NAME_TYPE = "text.font.name.type";
    /**
     * 全局的界面字体，默认为 阿里巴巴普惠体 R
     */
    public static final String TEXT_FONT_NAME_GLOBAL = "text.font.name.global";
    /**
     * 词提文件路径
     */
    public static final String TEXT_TIP_FILE_PATH = "text.tip.file.path";
    /**
     * 词提的选重
     */
    public static final String TEXT_TIP_SELECTION = "text.tip.selection";
    /**
     * 一简字颜色
     */
    public static final String TEXT_TIP_COLOR_SIMPLIFIED_CODE_ONE = "text.tip.color.simplified.code.one";
    /**
     * 二简字颜色
     */
    public static final String TEXT_TIP_COLOR_SIMPLIFIED_CODE_TWO = "text.tip.color.simplified.code.two";
    /**
     * 三简字颜色
     */
    public static final String TEXT_TIP_COLOR_SIMPLIFIED_CODE_THREE = "text.tip.color.simplified.code.three";
    /**
     * 四简字颜色
     */
    public static final String TEXT_TIP_COLOR_CODE_ALL = "text.tip.color.code.all";
    /**
     * 今日跟打字数的最后更新日期
     */
    public static final String TYPE_WORD_TOTAL_LAST_UPDATE = "type.word.total.last.update";
    /**
     * 输入法，默认为 {@code null}
     */
    public static final String TYPE_METHOD_INPUT = "type.method.input";
    /**
     * 键盘，默认为 {@code null}
     */
    public static final String TYPE_METHOD_KEYBOARD = "type.method.keyboard";
    /**
     * 个签，默认为 {@code null}
     */
    public static final String TYPE_SIGNATURE = "type.signature";
    /**
     * 输入法码表路径，默认为 {@code null}
     */

    //region 快捷键设置
    public static final String INPUT_CODE_DICT_PATH = "input.code.dict.path";
    /**
     * 新建发文快捷键，默认为 {@code F2}
     */
    public static final String QUICK_KEY_NEW_SEND = "quick.key.new.send";
    /**
     * 重打快捷键，默认为 {@code F3}
     */
    public static final String QUICK_KEY_RESTART = "quick.key.restart";
    /**
     * 暂停快捷键，默认为  {@code F9}
     */
    public static final String QUICK_KEY_PAUSE = "quick.key.pause";
    /**
     * 菜单快捷键之载文，默认为 {@code F4}
     */
    public static final String QUICK_KEY_MENU_TYPE_TEXT_LOAD = "quick.key.menu.type.text.load";
    /**
     * 换群快捷键，默认为 {@code F5}
     */
    public static final String QUICK_KEY_GROUP = "quick.key.group";
    /**
     * 乱序快捷键，默认为 {@code F8}
     */
    public static final String QUICK_KEY_TEXT_MIX = "quick.key.text.mix";
    /**
     * 继续发文快捷键，默认为 {@code F9}
     */
    public static final String QUICK_KEY_SEND_CONTINUE = "quick.key.send.continue";
    /**
     * 结束发文快捷键，默认为 {@code Ctrl E}
     */
    public static final String QUICK_KEY_SEND_END = "quick.key.send.end";
    /**
     * 下一段快捷键，默认为 {@code F10}
     */
    public static final String QUICK_KEY_SEND_NEXT_PARA = "quick.key.send.para.next";
    /**
     * 上一段快捷键，默认为 {@code Ctrl F}
     */
    public static final String QUICK_KEY_SEND_PARA_FORE = "quick.key.send.para.fore";
    /**
     * 菜单快捷键之设置，默认为 {@code Ctrl Z}
     */
    public static final String QUICK_KEY_SETTING_WINDOW = "quick.key.menu.type.setting";
    /**
     * 当量窗体显示快捷键，默认为 {@code Ctrl D}
     */
    public static final String QUICK_KEY_DANG_LIANG_WINDOW = "quick.key.dang.liang.window";
    /**
     * 内置输入框启用快捷键，默认为 {@code Ctrl I}
     */
    public static final String QUICK_KEY_INNER_INPUT_WINDOW = "quick.key.inner.input.window";
    //endregion 快捷键设置

    //endregion String 类型

    //region float 类型
    /**
     * 行距，默认为 {@code 0.8}
     */
    public static final String TEXT_LINE_SPACE = "text.line.space";
    //endregion float 类型

    public static final Map<String, String> DEFAULT_MAP = new TreeMap<>() {
        {
            try {
                URL defaultSettings = Info.loadURL("default_settings.properties");
                InputStream inputStream = defaultSettings.openStream();
                QRFileUtils.fileReaderWithUtf8(inputStream, (lineText -> {
                    String[] split = QRStringUtils.stringSplit(lineText, '=', true);
                    put(split[0], split[1]);
                }));
                put(TYPE_WORD_TOTAL_LAST_UPDATE, QRTimeUtils.getDateNow());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };
}