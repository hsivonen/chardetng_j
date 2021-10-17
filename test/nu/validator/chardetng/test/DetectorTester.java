// Copyright Mozilla Foundation
//
// Licensed under the Apache License (Version 2.0), or the MIT license,
// (the "Licenses") at your option. You may not use this file except in
// compliance with one of the Licenses. You may obtain copies of the
// Licenses at:
//
//    http://www.apache.org/licenses/LICENSE-2.0
//    http://opensource.org/licenses/MIT
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the Licenses is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the Licenses for the specific language governing permissions and
// limitations under the Licenses.

package nu.validator.chardetng.test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import nu.validator.chardetng.EncodingDetector;

public class DetectorTester {

	private static final EncodingDetector detector = new EncodingDetector();

	private static void check(String name, String data, Charset encoding) {
		detector.reset();
		ByteBuffer binary = encoding.encode(data);
		detector.feed(binary, true);
		Charset guessed = detector.guess(null, false);
		if (encoding != guessed) {
			System.err.println("FAIL: " + name + "; got: " + EncodingDetector.nameFor(guessed));
		}
	}

	public static void main(String[] args) {
		check("fi", "Ääni", EncodingDetector.WINDOWS_1252);
		check("fi_bis", "Tämä", EncodingDetector.WINDOWS_1252);
		check("pt", "Este é um teste de codificação de caracteres.", EncodingDetector.WINDOWS_1252);
		check("is",
				"Þetta er kóðunarpróf á staf. Fyrir sum tungumál sem nota latneska stafi þurfum við meira inntak til að taka ákvörðunina.",
				EncodingDetector.WINDOWS_1252);
		check("ru_short", "Русский", EncodingDetector.WINDOWS_1251);
		check("ru", "Это тест кодировки символов.", EncodingDetector.WINDOWS_1251);
		check("ru_iso", "Это тест кодировки символов.", EncodingDetector.ISO_8859_5);
		check("ru_ibm", "Это тест кодировки символов.", EncodingDetector.IBM866);
		check("ru_koi", "Это тест кодировки символов.", EncodingDetector.KOI8_U);
		check("uk", "Це тест на кодування символів.", EncodingDetector.WINDOWS_1251);
		check("uk_koi", "Це тест на кодування символів.", EncodingDetector.KOI8_U);
		check("el_short", "Ελληνικά", EncodingDetector.WINDOWS_1253);
		check("el", "Πρόκειται για δοκιμή κωδικοποίησης χαρακτήρων: Άρης", EncodingDetector.WINDOWS_1253);
		check("el_iso", "Πρόκειται για δοκιμή κωδικοποίησης χαρακτήρων: Άρης", EncodingDetector.ISO_8859_7);
		check("de", "Straße", EncodingDetector.WINDOWS_1252);
		check("he", "\u05E2\u05D1\u05E8\u05D9\u05EA", EncodingDetector.WINDOWS_1255);
		check("2022", "日本語", EncodingDetector.ISO_2022_JP);
		check("th", "นี่คือการทดสอบการเข้ารหัสอักขระ", EncodingDetector.WINDOWS_874);
		check("vi", "Đây là một thử nghiệm mã hóa ký tự.", EncodingDetector.WINDOWS_1258);
		check("tr",
				"Bu bir karakter kodlama testidir. Latince karakterleri kullanan bazı dillerde karar vermek için daha fazla girdiye ihtiyacımız var.",
				EncodingDetector.WINDOWS_1254);
		check("simplified", "这是一个字符编码测试。", EncodingDetector.GBK);
		check("traditional", "這是一個字符編碼測試。", EncodingDetector.BIG5);
		check("ko", "이것은 문자 인코딩 테스트입니다.", EncodingDetector.EUC_KR);
		check("shift", "これは文字実験です。", EncodingDetector.SHIFT_JIS);
		check("euc", "これは文字実験です。", EncodingDetector.EUC_JP);
		check("ar", "هذا هو اختبار ترميز الأحرف.", EncodingDetector.WINDOWS_1256);
		check("ar_iso", "هذا هو اختبار ترميز الأحرف.", EncodingDetector.ISO_8859_6);
		check("fa", "این یک تست رمزگذاری کاراکتر است.", EncodingDetector.WINDOWS_1256);
		check("visual", ".םיוות דודיק ןחבמ והז", EncodingDetector.ISO_8859_8);
		check("yi", "דאָס איז אַ טעסט פֿאַר קאָדירונג פון כאַראַקטער.", EncodingDetector.WINDOWS_1255);
		check("it", "è", EncodingDetector.WINDOWS_1252);
		check("en", "isn’t", EncodingDetector.WINDOWS_1252);
		check("en_bis", "Rock ’n Roll", EncodingDetector.WINDOWS_1252);
		check("ca", "Codificació de caràcters", EncodingDetector.WINDOWS_1252);
		check("et", "või", EncodingDetector.WINDOWS_1252);
		check("pl_iso",
				"To jest test kodowania znaków. W przypadku niektórych języków, które używają znaków łacińskich, potrzebujemy więcej danych, aby podjąć decyzję.",
				EncodingDetector.ISO_8859_2);
		check("pl",
				"To jest test kodowania znaków. W przypadku niektórych języków, które używają znaków łacińskich, potrzebujemy więcej danych, aby podjąć decyzję.",
				EncodingDetector.WINDOWS_1250);
		check("lt",
				"Tai simbolių kodavimo testas. Kai kurioms kalboms, naudojančioms lotyniškus rašmenis, mums reikia daugiau informacijos, kad galėtume priimti sprendimą.",
				EncodingDetector.WINDOWS_1257);
		check("lv",
				"Šis ir rakstzīmju kodēšanas tests. Dažās valodās, kurās tiek izmantotas latīņu valodas burti, lēmuma pieņemšanai mums ir nepieciešams vairāk ieguldījuma.",
				EncodingDetector.WINDOWS_1257);
		check("lv_iso_8859_4",
				"Šis ir rakstzīmju kodēšanas tests. Dažās valodās, kurās tiek izmantotas latīņu valodas burti, lēmuma pieņemšanai mums ir nepieciešams vairāk ieguldījuma.",
				EncodingDetector.ISO_8859_4);
		check("a0", "\u00A0\u00A0 \u00A0", EncodingDetector.WINDOWS_1252);
		check("a0a0", "\u00A0\u00A0", EncodingDetector.WINDOWS_1252);
		check("space_masculine_space", " º ", EncodingDetector.WINDOWS_1252);
		check("space_feminine_space", " ª ", EncodingDetector.WINDOWS_1252);
		check("period_masculine_space", ".º ", EncodingDetector.WINDOWS_1252);
		check("period_feminine_space", ".ª ", EncodingDetector.WINDOWS_1252);
		check("maria", " Mª ", EncodingDetector.WINDOWS_1252);
		check("dona", " Dª ", EncodingDetector.WINDOWS_1252);
		check("nuestra", " Nª ", EncodingDetector.WINDOWS_1252);
		check("senora", " Sª ", EncodingDetector.WINDOWS_1252);
		check("digit_feminine", " 42ª ", EncodingDetector.WINDOWS_1252);
		check("digit_masculine", " 42º ", EncodingDetector.WINDOWS_1252);
		check("roman_feminine", " XIVª ", EncodingDetector.WINDOWS_1252);
		check("roman_masculine", " XIVº ", EncodingDetector.WINDOWS_1252);
		check("numero_uno", "Nº1", EncodingDetector.WINDOWS_1252);
		check("numero", "Nº", EncodingDetector.WINDOWS_1252);
		check("euro", " €9", EncodingDetector.WINDOWS_1252);
		check("shift_jis_half_width_katakana", "ﾊｰﾄﾞｳｪｱﾊｰﾄﾞｳｪｱﾊｰﾄﾞｳｪｱﾊｰﾄﾞｳｪｱﾊｰﾄﾞｳｪｱ", EncodingDetector.SHIFT_JIS);
	}
}
