package ly.qr.kiarelemb.dl;

/**
 * 打字过程中的当量数据
 *
 * @param c    按下的键
 * @param time 按键时间
 */
public record DangLangData(char c, long time) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DangLangData that = (DangLangData) o;

		if (c != that.c) return false;
		return time == that.time;
	}

	@Override
	public int hashCode() {
		int result = c;
		result = 31 * result + (int) (time ^ (time >>> 32));
		return result;
	}
}