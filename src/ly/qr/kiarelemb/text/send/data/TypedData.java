package ly.qr.kiarelemb.text.send.data;

import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.qq.SendText;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.send.TextSendManager;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRMathUtils;
import method.qr.kiarelemb.utils.QRRandomUtils;
import method.qr.kiarelemb.utils.data.QRTextSendData;

import java.io.Serializable;

/**
 * 当前跟打信息记录类
 *
 * @author Kiarelemb QR
 */
public final class TypedData implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = -5849714470754967711L;
    private final String fileName;
    private final String fileCrc;
    private final int totalWordsNum;
    private final String filePath;
    private final boolean paraNumRandom;
    private long startIndex;
    private int typedTimes;
    private int remainingWordsCount;
    private int perLength;

    /**
     * @param fileName            跟打文件名，不带拓展名
     * @param fileCrc             文件校验CRC码
     * @param startIndex          本次跟打开始索引
     * @param typedTimes          已跟打次数
     * @param remainingWordsCount 剩余字数
     * @param perLength           每段字数
     * @param paraNumRandom       段号随机
     */
    public TypedData(String fileName, String fileCrc, long startIndex, int typedTimes, int totalWordsNum,
                     int remainingWordsCount, int perLength, boolean paraNumRandom) {
        this.fileName = fileName;
        filePath = Info.TYPE_DIRECTORY + fileName + ".txt";
        this.fileCrc = fileCrc;
        this.startIndex = startIndex;
        this.typedTimes = typedTimes;
        this.totalWordsNum = totalWordsNum;
        this.remainingWordsCount = remainingWordsCount;
        this.perLength = perLength;
        this.paraNumRandom = paraNumRandom;
        if (!this.paraNumRandom) {
            paraNum = 1;
        }
    }

    /**
     * 当前段号
     */
    private int paraNum;
    /**
     * 当前段文本内容
     */
    private String currentText;

    private QRTextSendData sendData = null;

    public TypedData gotoForePara() {
        if (sendData == null || startIndex <= 0) {
            return this;
        }

        // 段号
        if (paraNumRandom) {
            paraNum = QRRandomUtils.getRandomInt(100000, 999999);
        } else {
            paraNum--;
        }
        remainingWordsCount += currentText.length();
        return this;
    }

    public String foreParaText() {
        sendData = QRFileUtils.fileForeReaderByRandomAccessMarkPositionFind(filePath, startIndex, perLength,
                TypingData.textLoadIntelli);
        // 上一段的结束位置
        startIndex -= sendData.typeTextByteLen();
//		System.out.println("currentText = " + currentText);
        return getSendParaText();
    }

    /**
     * 为进入下一段前做数据计算准备。在最开始发文时，该方法不调用
     *
     * @return 当前跟打记录文件
     */
    public TypedData gotoNextPara() {
        // 段号
        if (paraNumRandom) {
            paraNum = QRRandomUtils.getRandomInt(100000, 999999);
        } else {
            paraNum++;
        }
        if (sendData == null) {
            return this;
        }
        // 下一段的开始位置
        startIndex += sendData.typeTextByteLen();
        return this;
    }

    public String nextParaText() {
        sendData = QRFileUtils.fileReaderByRandomAccessMarkPositionFind(filePath, startIndex, perLength,
                TypingData.textLoadIntelli);
        remainingWordsCount -= currentText.length();
        return getSendParaText();
    }

    private String getSendParaText() {
        String fore = fileName + "\n";
        currentText = sendData.text();
        String ends = "\n-----第" + paraNum + "段 " + currentText.length() + "字 进度"
                + QRMathUtils.doubleFormat((totalWordsNum - remainingWordsCount) * 100d / totalWordsNum, 2) + "%";
        TextSendManager.save();
        String text = fore + currentText + ends;
        SendText.sendText(text);
        return text;
    }

    public void addTypedTimes() {
        this.typedTimes++;
    }

    public void setPerLength(int perLength) {
        this.perLength = perLength;
    }

    public String fileName() {
        return fileName;
    }

    public String fileCrc() {
        return fileCrc;
    }

    public long startIndex() {
        return startIndex;
    }

    public int typedTimes() {
        return typedTimes;
    }

    public int remainingWordsCount() {
        return remainingWordsCount;
    }

    public int totalWordsNum() {
        return totalWordsNum;
    }

    public String filePath() {
        return filePath;
    }

    public boolean paraNumRandom() {
        return paraNumRandom;
    }

    public int perLength() {
        return perLength;
    }

    public int paraNum() {
        return paraNum;
    }

    public String currentText() {
        return currentText;
    }

    public QRTextSendData sendData() {
        return sendData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypedData data = (TypedData) o;

        if (totalWordsNum != data.totalWordsNum) return false;
        if (paraNumRandom != data.paraNumRandom) return false;
        if (startIndex != data.startIndex) return false;
        if (typedTimes != data.typedTimes) return false;
        if (remainingWordsCount != data.remainingWordsCount) return false;
        if (perLength != data.perLength) return false;
        if (paraNum != data.paraNum) return false;
        if (!fileName.equals(data.fileName)) return false;
        if (!fileCrc.equals(data.fileCrc)) return false;
        if (!filePath.equals(data.filePath)) return false;
        if (!currentText.equals(data.currentText)) return false;
        return sendData.equals(data.sendData);
    }

    @Override
    public int hashCode() {
        int result = fileName.hashCode();
        result = 31 * result + fileCrc.hashCode();
        result = 31 * result + totalWordsNum;
        result = 31 * result + filePath.hashCode();
        result = 31 * result + (paraNumRandom ? 1 : 0);
        result = 31 * result + Long.hashCode(startIndex);
        result = 31 * result + typedTimes;
        result = 31 * result + remainingWordsCount;
        result = 31 * result + perLength;
        result = 31 * result + paraNum;
        result = 31 * result + currentText.hashCode();
        result = 31 * result + sendData.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TypedData{" + "fileName='" + fileName + '\'' +
                ", fileCrc='" + fileCrc + '\'' +
                ", totalWordsNum=" + totalWordsNum +
                ", filePath='" + filePath + '\'' +
                ", paraNumRandom=" + paraNumRandom +
                ", startIndex=" + startIndex +
                ", typedTimes=" + typedTimes +
                ", remainingWordsCount=" + remainingWordsCount +
                ", perLength=" + perLength +
                ", paraNum=" + paraNum +
                ", sendData=" + sendData +
                '}';
    }
}