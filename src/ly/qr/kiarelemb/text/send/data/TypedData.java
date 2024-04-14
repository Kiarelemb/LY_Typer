package ly.qr.kiarelemb.text.send.data;

import java.util.Objects;

/**
 * 当前跟打信息记录类
 *
 * @author Kiarelemb QR
 */
public final class TypedData {
	private final String fileName;
	private final String fileCrc;
	private int startIndex;
	private int typedTimes;
	private long remainingWordsCount;
	private int perLength;
	private final boolean paraNumRandom;

	/**
	 * @param fileName            跟打文件名
	 * @param fileCrc             文件校验CRC码
	 * @param startIndex          本次跟打开始索引
	 * @param typedTimes          已跟打次数
	 * @param remainingWordsCount 剩余字数
	 * @param perLength           每段字数
	 * @param paraNumRandom       段号随机
	 */
	public TypedData(String fileName, String fileCrc, int startIndex, int typedTimes, long remainingWordsCount,
	                 int perLength, boolean paraNumRandom) {
		this.fileName = fileName;
		this.fileCrc = fileCrc;
		this.startIndex = startIndex;
		this.typedTimes = typedTimes;
		this.remainingWordsCount = remainingWordsCount;
		this.perLength = perLength;
		this.paraNumRandom = paraNumRandom;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public void setTypedTimes(int typedTimes) {
		this.typedTimes = typedTimes;
	}

	public void setRemainingWordsCount(int remainingWordsCount) {
		this.remainingWordsCount = remainingWordsCount;
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

	public int startIndex() {
		return startIndex;
	}

	public int typedTimes() {
		return typedTimes;
	}

	public long remainingWordsCount() {
		return remainingWordsCount;
	}

	public int perLength() {
		return perLength;
	}

	public boolean paraNumRandom() {
		return paraNumRandom;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (TypedData) obj;
		return Objects.equals(this.fileName, that.fileName) &&
				Objects.equals(this.fileCrc, that.fileCrc) &&
				this.startIndex == that.startIndex &&
				this.typedTimes == that.typedTimes &&
				this.remainingWordsCount == that.remainingWordsCount &&
				this.perLength == that.perLength &&
				this.paraNumRandom == that.paraNumRandom;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileName, fileCrc, startIndex, typedTimes, remainingWordsCount, perLength, paraNumRandom);
	}

	@Override
	public String toString() {
		return "TypedData[" +
				"fileName=" + fileName + ", " +
				"fileCrc=" + fileCrc + ", " +
				"startIndex=" + startIndex + ", " +
				"typedTimes=" + typedTimes + ", " +
				"remainingWordsCount=" + remainingWordsCount + ", " +
				"perLength=" + perLength + ", " +
				"paraNumRandom=" + paraNumRandom + ']';
	}
}