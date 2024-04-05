package ly.qr.kiarelemb.text;

import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TipData;
import ly.qr.kiarelemb.text.tip.AbstractTextTip;
import ly.qr.kiarelemb.text.tip.data.TipPhraseStyleData;
import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRMathUtils;
import method.qr.kiarelemb.utils.QRRandomUtils;
import method.qr.kiarelemb.utils.QRStringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static method.qr.kiarelemb.utils.QRStringUtils.AN_ENTER;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-22 14:11
 **/
public class TextLoad {

	public static final char DI = '第';
	public static final char DUAN = '段';

	public static TextLoad TEXT_LOAD;
	public TipData tipData;
	private boolean isEnglish = false;
	private boolean isText = false;
	private boolean singleOnly = false;
	private boolean isEnglishPhrase = false;
	private boolean isChinesePhrase = false;
	private boolean isArticle = false;
	private boolean isExtra = false;
	private boolean isMixing = false;
	private double englishWordsAverageLength;
	private int wordsLength;
	private int numberNum;
	private int markNum;
	private int reTypeTimes;
	private int englishWordsNum;
	private int para;
	private String paragraph;
	/**
	 * 乱序后不影响
	 */
	private String originalText;
	/**
	 * 乱序后影响
	 */
	private String currentText;
	private String formattedActualText;
	private String foreText;
	private String endText;
	private String textMD5Short;
	private String textMD5Long;
	private String originalContent;
	private String[] phrases;
	private String[] wordParts;
	private char[] textChars;


	public TextLoad(String originalText) {
		if (originalText.isEmpty() || originalText.isBlank()) {
			return;
		}
		String spaceFormattedText = QRArrayUtils.spaceFormat(originalText);
		int diIndex = spaceFormattedText.lastIndexOf(DI) + 1;
		int duanIndex = spaceFormattedText.indexOf(DUAN, diIndex);
		//没有段号信息就不行
		if (diIndex == 0 || duanIndex == -1) {
			return;
		}
		int firstln = spaceFormattedText.indexOf(AN_ENTER) + 1;
		int lastln = spaceFormattedText.lastIndexOf(AN_ENTER, diIndex);
		boolean threePara = (firstln != 0 && firstln - 1 < lastln);

		boolean twoPara = firstln != 0 && (firstln == lastln + 1);
		if (threePara || twoPara) {
			this.originalText = originalText.replaceAll("\r", "\n");

			//段号
			this.paragraph = spaceFormattedText.substring(diIndex, duanIndex);
			this.para = Integer.parseInt(this.paragraph);
			//正文稍微格式化后的内容
			String tmp = (threePara ? spaceFormattedText.substring(firstln, lastln) : spaceFormattedText.substring(0, lastln)).trim();
			String actualContent = switch (Keys.intValue(Keys.TEXT_SIMPLE_TRADITIONAL_CONVERT)) {
				case 1 -> QRStringUtils.isTraditionalChinese(tmp) ? QRStringUtils.convertToSimplifiedChinese(tmp) : tmp;
				case 2 -> QRStringUtils.isSimplifiedChinese(tmp) ? QRStringUtils.convertToTraditionalChinese(tmp) : tmp;
				default -> tmp;
			};
			//是否是英文
			this.isEnglish = QRStringUtils.isEnglish(actualContent);
			//打字文本格式化后的内容
			this.formattedActualText = TextWash.actualContentFormat(actualContent, this.isEnglish);
			this.wordsLength = this.formattedActualText.length();
			if (this.wordsLength == 0) {
				return;
			}
			if (this.isEnglish) {
				if (QRStringUtils.stringContainsNon(this.formattedActualText, new String[]{",", "\"", ":"})) {
					this.isEnglishPhrase = true;
				}
			}
			//正文字数
			if (twoPara) {
				this.originalContent = this.originalText.substring(0, this.originalText.lastIndexOf(AN_ENTER)).trim();
			} else {
				this.originalContent = this.originalText.substring(this.originalText.indexOf(AN_ENTER) + 1, this.originalText.lastIndexOf(AN_ENTER)).trim();
			}
			//正文前的制作者的信息
			this.foreText = threePara ? spaceFormattedText.substring(0, firstln - 1).trim() : "";
			//载文最后一行的段号及其它校验信息
			this.endText = spaceFormattedText.substring(lastln).trim();
			//计算单词平均长度
			this.englishWordsAverageLength = this.isEnglish ? QRMathUtils.doubleFormat((this.wordsLength + 0.0) / 5) : 0;
			//取得内部空格的位置，多数情况下，ali.size == 0
			ArrayList<Integer> ali = QRStringUtils.getAllSpaceIndexes(this.originalContent);
			//如果不是英文
			if (!this.isEnglish) {
				//先清除所有的空格和换行符
				String spaceClearedActualText = QRStringUtils.spaceAndLineSeparatorClear(this.formattedActualText);
				//除去所有常用单字后
				String normalRest = QRStringUtils.isCharInRange(spaceClearedActualText, QRStringUtils.CHINESE_NORMAL).toString();
				//只要包含任意一个中文标点
				boolean marksContains = Stream.of("，", "。", "“", "；").anyMatch(normalRest::contains);
				this.phrases = QRStringUtils.getChineseExtraPhrase(this.formattedActualText);
				//拿到词提
				updateTipsWithoutEnable();
				this.isExtra = QRStringUtils.getChineseExtraPhrase(spaceClearedActualText).length != spaceClearedActualText.length();
				//如果确实没有标点
				if (!marksContains) {
					//再判断是不是中文词语
					if (ali.size() > 2) {
						int account = 1;
						int size = ali.size();
						int fore = ali.get(size - 1);
						for (int i = size - 2; i > -1; i--) {
							int thisI = ali.get(i);
							//前一个位置的空格位置不与后一个相连，且至少相连两个字
							if (thisI < fore - 2) {
								//占比增加
								account++;
							}
							fore = thisI;
						}
						//符合条件且占比较大就判定为词
						this.isChinesePhrase = (account + 0.0) / size > 0.6;
						if (this.isChinesePhrase) {
							this.phrases = this.originalContent.split(QRStringUtils.A_WHITE_SPACE);
						}
					} else if (AbstractTextTip.TEXT_TIP.loaded()) {
						int phraseLen = this.tipData.tpsd.stream().filter(Objects::nonNull).filter(TipPhraseStyleData::shortPhrase).mapToInt(data -> data.phrase().length()).sum();
						//用这个方法也判断是词
						if (phraseLen >= this.wordsLength / 2) {
							//抽取所有的词
							this.isChinesePhrase = true;
							this.phrases = IntStream.range(0, this.tipData.tpsd.size()).filter(i -> this.tipData.tpsd.get(i) != null).mapToObj(i -> this.tipData.tpsd.get(i).phrase()).collect(Collectors.toCollection(ArrayList::new)).toArray(QRStringUtils.ARR_EMPTY);
						}
					}
					this.singleOnly = !this.isChinesePhrase;
					this.phrases = QRStringUtils.getChineseExtraPhrase(this.formattedActualText);
				} else {
					//有标点就判断不是单字
					this.singleOnly = false;
					//就是文章
					this.isArticle = true;
				}
			} else {
				this.englishWordsNum = QRStringUtils.englishWordsCount(this.formattedActualText);
				//先去除所有的字母
				String alpClear = QRStringUtils.isCharInRange(this.formattedActualText, QRStringUtils.ENGLISH_RANGE).toString();
				//只要包含任意一个英文标点
				this.isEnglishPhrase = Stream.of(".", ",", ":", ".").noneMatch(alpClear::contains);
				this.phrases = this.formattedActualText.split(QRStringUtils.A_WHITE_SPACE);
				this.wordParts = QRStringUtils.getChineseExtraPhrase(this.formattedActualText);
				this.wordsLength = this.wordParts.length;
			}
			try {
				final String md5 = QRStringUtils.getMd5(this.formattedActualText);
				this.textMD5Long = md5.substring(md5.length() / 2);
				//正文内容的验证，取md5的前6位
				this.textMD5Short = this.textMD5Long.substring(0, 4);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String temp = QRStringUtils.spaceAndLineSeparatorClear(this.formattedActualText);
			String numberClearRest = QRStringUtils.isCharInRange(temp, QRStringUtils.NUMBER_RANGE).toString();
			this.numberNum = temp.length() - numberClearRest.length();
			StringBuilder marks = QRStringUtils.isCharInRange(QRStringUtils.isCharInRange(numberClearRest, QRStringUtils.ENGLISH_RANGE).toString(), QRStringUtils.CHINESE_NORMAL);
			this.markNum = marks.length();
			//更新
			textUpdate();
			//这个是可以打的文
			this.isText = true;
		}
	}

	/**
	 * 文本内容的更新
	 */
	private void textUpdate() {
		//取得字符数组
		this.textChars = this.formattedActualText.toCharArray();
		//当前文本内容更新
		this.currentText = this.foreText + (this.foreText.isEmpty() ? "" : QRStringUtils.AN_ENTER) + (this.isChinesePhrase ? String.join(" ", this.phrases) : this.formattedActualText) + QRStringUtils.AN_ENTER + this.endText;
	}

	/**
	 * 这个用于判断是否是词语，仅当在没有找到标点的情况下
	 */
	public void updateTipsWithoutEnable() {
		//这个不能动
		this.wordParts = QRStringUtils.getChineseExtraPhrase(this.formattedActualText);
		this.wordsLength = this.wordParts.length;
		if (!this.isEnglish && AbstractTextTip.TEXT_TIP.loaded()) {
			//取得所有的编码
			this.tipData = AbstractTextTip.TEXT_TIP.GetShortestCode(this.formattedActualText);
			if (this.isChinesePhrase) {
				float avgLen = this.phrases.length / (this.wordsLength + 0.0f);
				LinkedList<String> phraseList = new LinkedList<>();
				Collections.addAll(phraseList, this.phrases);
				boolean needNew = false;
				ArrayList<TipPhraseStyleData> tpsd = this.tipData.tpsd;
				//词组去新词
				for (int i = 0, tpsdSize = tpsd.size(); i < tpsdSize; i++) {
					TipPhraseStyleData phrase = tpsd.get(i);
					if (phrase == null) {
						continue;
					}
					String p = phrase.phrase();
					if (!phraseList.contains(p)) {
						for (int j = 0, phraseListSize = phraseList.size(); j < phraseListSize; j++) {
							String s = phraseList.get(j);
							if (p.startsWith(s)) {
								needNew = true;
								i++;
								String tmp = this.phrases[j];
								int index;
								if (j > phraseListSize / 2) {
									index = QRRandomUtils.getRandomInt(0, phraseListSize / 2);
								} else {
									index = QRRandomUtils.getRandomInt(phraseListSize / 2 + 1, phraseListSize);
								}
								this.phrases[j] = this.phrases[index];
								this.phrases[index] = tmp;
								p = p.substring(s.length());
								if (p.length() > avgLen) {
									j = -1;
								}
							}
						}
					}
				}
				if (needNew) {
					this.formattedActualText = String.join("", this.phrases);
					textUpdate();
					updateTipsWithoutEnable();
				}
			}
			if (this.formattedActualText.contains(" ")) {
				this.formattedActualText = this.formattedActualText.replaceAll(" ", "");
				textUpdate();
				updateTipsWithoutEnable();
			}
			String codes = this.tipData.codes;
			if (codes == null) {
				return;
			}
			//计算最短码长
			ContractiblePanel.STANDARD_LEN_LABEL.setText(String.valueOf(QRMathUtils.doubleFormat((codes.length() + 0.0) / this.wordsLength)));
		}
	}

	/**
	 * 文本乱序
	 */
	public void actualContentMix() {
		//中英文文章不需要乱序
		if (this.isEnglish && !this.isEnglishPhrase) {
			return;
		}
		if (this.isChinesePhrase || this.isExtra) {
			this.phrases = QRArrayUtils.getRandomPhrase(this.phrases);
			this.formattedActualText = String.join("", this.phrases);
		} else if (this.isEnglishPhrase) {
			//英语单词还需要加空格
			this.phrases = QRArrayUtils.getRandomPhrase(this.phrases);
			this.formattedActualText = String.join(QRStringUtils.A_WHITE_SPACE, this.phrases);
		} else {
			this.formattedActualText = QRRandomUtils.getRandomString(this.formattedActualText);
			this.phrases = QRStringUtils.getChineseExtraPhrase(this.formattedActualText);
		}
		//更新
		textUpdate();
		updateTipsWithoutEnable();
		this.isMixing = true;
	}

	public boolean textNotIsChanged(String text) {
		if (this.formattedActualText.length() != text.length()) {
			return false;
		}
		try {
			if (this.textMD5Short.equals(QRStringUtils.getMd5(text).substring(0, 4))) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		char[] ch = this.formattedActualText.toCharArray();
		for (char c : ch) {
			if (!text.contains(String.valueOf(c))) {
				return false;
			}
		}
		return true;
	}

	public char getTextAtIndex(int index) {
		if (index >= this.textChars.length || index < 0) {
			return Character.MIN_VALUE;
		}
		return this.textChars[index];
	}

	public String getWordPartsAtIndex(int index) {
		if (index >= this.wordParts.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.wordParts[index];
	}

	//region getters

	public String wordPartsCopyRange(int startIndex, int len) {
		return QRArrayUtils.getArraySubstring(this.wordParts, startIndex, len);
	}

	public boolean isEnglish() {
		return this.isEnglish;
	}

	public boolean isText() {
		return this.isText;
	}

	public boolean singleOnly() {
		return this.singleOnly;
	}

	public boolean isEnglishPhrase() {
		return this.isEnglishPhrase;
	}

	public boolean isChinesePhrase() {
		return this.isChinesePhrase;
	}

	public boolean isArticle() {
		return this.isArticle;
	}

	public boolean isExtra() {
		return this.isExtra;
	}

	public boolean isMixing() {
		return this.isMixing;
	}


	public double englishWordsAverageLength() {
		return this.englishWordsAverageLength;
	}

	public int wordsLength() {
		return this.wordsLength;
	}

	public int reTypeTimes() {
		return this.reTypeTimes;
	}

	public int englishWordsNum() {
		return this.englishWordsNum;
	}

	public int para() {
		return this.para;
	}

	public String paragraph() {
		return this.paragraph;
	}

	public String originalText() {
		return this.originalText;
	}

	public String currentText() {
		return this.currentText;
	}

	public String formattedActualText() {
		return this.formattedActualText;
	}

	public String foreText() {
		return this.foreText;
	}

	public String foreTextShort() {
		return this.foreText.length() > 15 ? this.foreText.substring(0, 6) + "..." + this.foreText.substring(this.foreText.length() - 7) : this.foreText;
	}

	public String endText() {
		return this.endText;
	}

	public String endTextShort() {
		char[] chars = this.endText.toCharArray();
		int i = 0;
		for (int charsLength = chars.length; i < charsLength; i++) {
			char c = chars[i];
			if (c != '-') {
				break;
			}
		}
		String temp = new String(Arrays.copyOfRange(chars, i, chars.length));
		return temp.length() > 15 ? temp.substring(0, 6) + "..." + temp.substring(temp.length() - 7) : temp;
	}

	public String textMD5Short() {
		return this.textMD5Short;
	}

	public String textMD5Long() {
		return this.textMD5Long;
	}

	public String originalContent() {
		return this.originalContent;
	}

	public String[] phrases() {
		return this.phrases;
	}

	public String[] wordParts() {
		return this.wordParts;
	}

	public char[] textChars() {
		return this.textChars;
	}

	public int numberNum() {
		return this.numberNum;
	}

	public int markNum() {
		return this.markNum;
	}

	public void reTypeTimesAdd() {
		this.reTypeTimes++;
//		INSTANCE.retypedTimesLabel.setText(reTypeTimes);
	}

	//endregion
}