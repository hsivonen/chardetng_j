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

package nu.validator.chardetng;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * A Web browser-oriented detector for guessing what character encoding a stream
 * of bytes is encoded in.
 *
 * The bytes are fed to the detector incrementally using the <code>feed</code>
 * method. The current guess of the detector can be queried using the
 * <code>guess</code> method. The guessing parameters are arguments to the
 * <code>guess</code> method rather than arguments to the constructor in order
 * to enable the application to check if the arguments affect the guessing
 * outcome. (The specific use case is to disable UI for re-running the detector
 * with UTF-8 allowed and the top-level domain name ignored if those arguments
 * don't change the guess.)
 */
public class EncodingDetector {

	/**
	 * Possible return value of <code>guess</code> signifying UTF-8.
	 */
	public static final Charset UTF_8 = Charset.forName("UTF-8");

	/**
	 * Possible return value of <code>guess</code> signifying windows-1250.
	 */
	public static final Charset WINDOWS_1250 = Charset.forName("windows-1250");

	/**
	 * Possible return value of <code>guess</code> signifying windows-1251.
	 */
	public static final Charset WINDOWS_1251 = Charset.forName("windows-1251");

	/**
	 * Possible return value of <code>guess</code> signifying windows-1252.
	 */
	public static final Charset WINDOWS_1252 = Charset.forName("windows-1252");

	/**
	 * Possible return value of <code>guess</code> signifying windows-1253.
	 */
	public static final Charset WINDOWS_1253 = Charset.forName("windows-1253");

	/**
	 * Possible return value of <code>guess</code> signifying windows-1254.
	 */
	public static final Charset WINDOWS_1254 = Charset.forName("windows-1254");

	/**
	 * Possible return value of <code>guess</code> signifying windows-1255.
	 */
	public static final Charset WINDOWS_1255 = Charset.forName("windows-1255");

	/**
	 * Possible return value of <code>guess</code> signifying windows-1256.
	 */
	public static final Charset WINDOWS_1256 = Charset.forName("windows-1256");

	/**
	 * Possible return value of <code>guess</code> signifying windows-1257.
	 */
	public static final Charset WINDOWS_1257 = Charset.forName("windows-1257");

	/**
	 * Possible return value of <code>guess</code> signifying windows-1258.
	 */
	public static final Charset WINDOWS_1258 = Charset.forName("windows-1258");

	/**
	 * Possible return value of <code>guess</code> signifying windows-874.
	 */
	public static final Charset WINDOWS_874 = Charset.forName("x-windows-874");

	/**
	 * Possible return value of <code>guess</code> signifying ISO-8859-2.
	 */
	public static final Charset ISO_8859_2 = Charset.forName("ISO-8859-2");

	/**
	 * Possible return value of <code>guess</code> signifying ISO-8859-4.
	 */
	public static final Charset ISO_8859_4 = Charset.forName("ISO-8859-4");

	/**
	 * Possible return value of <code>guess</code> signifying ISO-8859-5.
	 */
	public static final Charset ISO_8859_5 = Charset.forName("ISO-8859-5");

	/**
	 * Possible return value of <code>guess</code> signifying ISO-8859-.
	 */
	public static final Charset ISO_8859_6 = Charset.forName("ISO-8859-6");

	/**
	 * Possible return value of <code>guess</code> signifying ISO-8859-7.
	 */
	public static final Charset ISO_8859_7 = Charset.forName("ISO-8859-7");

	/**
	 * Possible return value of <code>guess</code> signifying ISO-8859-8.
	 */
	public static final Charset ISO_8859_8 = Charset.forName("ISO-8859-8");

	/**
	 * Possible return value of <code>guess</code> signifying ISO-8859-13.
	 */
	public static final Charset ISO_8859_13 = Charset.forName("ISO-8859-13");

	/**
	 * Possible return value of <code>guess</code> signifying KOI8-U.
	 */
	public static final Charset KOI8_U = Charset.forName("KOI8-U");

	/**
	 * Possible return value of <code>guess</code> signifying IBM866.
	 */
	public static final Charset IBM866 = Charset.forName("IBM866");

	/**
	 * Possible return value of <code>guess</code> signifying GBK.
	 */
	public static final Charset GBK = Charset.forName("GB18030");

	/**
	 * Possible return value of <code>guess</code> signifying Shift_JIS.
	 */
	public static final Charset SHIFT_JIS = Charset.forName("windows-31j");

	/**
	 * Possible return value of <code>guess</code> signifying EUC-KR.
	 */
	public static final Charset EUC_KR = Charset.forName("x-windows-949");

	/**
	 * Possible return value of <code>guess</code> signifying Big5.
	 */
	public static final Charset BIG5 = Charset.forName("Big5-HKSCS");

	/**
	 * Possible return value of <code>guess</code> signifying EUC-JP.
	 */
	public static final Charset EUC_JP = Charset.forName("EUC-JP");

	/**
	 * Possible return value of <code>guess</code> signifying ISO-2022-JP.
	 */
	public static final Charset ISO_2022_JP = Charset.forName("ISO-2022-JP");

	private static final Charset[] POSSIBLE_CHARSETS = { UTF_8, WINDOWS_1252, WINDOWS_1251, GBK, SHIFT_JIS, EUC_KR,
			WINDOWS_1250, WINDOWS_1256, WINDOWS_1254, BIG5, WINDOWS_874, WINDOWS_1255, WINDOWS_1253, WINDOWS_1257,
			WINDOWS_1258, EUC_JP, ISO_8859_2, ISO_8859_7, ISO_8859_5, ISO_8859_4, ISO_8859_6, ISO_2022_JP, KOI8_U,
			ISO_8859_13, IBM866, ISO_8859_8 };
	private static final String[] WHATWG_NAMES = { "UTF-8", "windows-1252", "windows-1251", "GBK", "Shift_JIS",
			"EUC-KR", "windows-1250", "windows-1256", "windows-1254", "Big5", "windows-874", "windows-1255",
			"windows-1253", "windows-1257", "windows-1258", "EUC-JP", "ISO-8859-2", "ISO-8859-7", "ISO-8859-5",
			"ISO-8859-4", "ISO-8859-6", "ISO-2022-JP", "KOI8-U", "ISO-8859-13", "IBM866", "ISO-8859-8" };

	/**
	 * Obtains the Encoding Standard-compliant name for a return value of
	 * <code>guess</code>.
	 * 
	 * @param encoding a possible return value of <code>guess</code>, i.e. a public
	 *                 constant of this class.
	 * @return the Encoding Standard-compliant name of the encoding
	 */
	public static String nameFor(Charset encoding) {
		for (int i = 0; i < POSSIBLE_CHARSETS.length; ++i) {
			if (POSSIBLE_CHARSETS[i] == encoding) {
				return WHATWG_NAMES[i];
			}
		}
		throw new IllegalArgumentException("Argument was not one of the Charset constants on this class.");
	}

	private final EncodingDetectorImpl impl;

	private final int detector;

	private final int buffer;

	private boolean finished;

	/**
	 * Constructs a new detector. This operation is relatively expensive, and the
	 * detector can be reused after calling <code>reset()</code>.
	 * 
	 * The object must not be used concurrently from multiple threads.
	 */
	public EncodingDetector() {
		this.impl = new EncodingDetectorImpl(65536 * 20);
		this.detector = this.impl.get__data_end();
		this.buffer = this.detector + this.impl.chardetng_j_size_of_encoding_detector();
		this.finished = false;
		this.impl.chardetng_j_encoding_detector_new_into(this.detector);
	}

	/**
	 * Resets this detector back to its initial state without the performance cost
	 * of running the full constructor.
	 */
	public void reset() {
		this.impl.chardetng_j_encoding_detector_new_into(this.detector);
		this.finished = false;
	}

	/**
	 * Inform the detector of a chunk of input.
	 *
	 * The byte stream is represented as a sequence of calls to this method such
	 * that the concatenation of the arguments to this method form the byte stream.
	 * It does not matter how the application chooses to chunk the stream. It is OK
	 * to call this method with a zero-remaining <code>ByteBuffer</code>.
	 *
	 * The end of the stream is indicated by calling this method with
	 * <code>last</code> set to <code>true</code>. In that case, the end of the
	 * stream is considered to occur after the last byte of the <code>buffer</code>
	 * (which may be zero-remaining) passed in the same call. Once this method has
	 * been called with <code>last</code> set to <code>true</code> this method must
	 * not be called again without calling <code>reset()</code> first.
	 *
	 * If you want to perform detection on just the prefix of a longer stream, do
	 * not pass <code>last=true</code> after the prefix if the stream actually still
	 * continues.
	 *
	 * @return <code>true</code> if after processing <code>buffer</code> the stream
	 *         has contained at least one non-ASCII byte and <code>false</code> if
	 *         only ASCII has been seen so far.
	 *
	 * @throws IllegalStateException If this method has previously been called with
	 *                               <code>last</code> set to <code>true</code>.
	 */
	public boolean feed(ByteBuffer buffer, boolean last) {
		if (this.finished) {
			throw new IllegalStateException("End of stream already fed to the detector.");
		}
		if (last) {
			this.finished = true;
		}
		boolean seenAscii = false;
		ByteBuffer memory = this.impl.getMemory();
		int originalMemoryPos = memory.position();
		memory.position(this.buffer);
		while (buffer.remaining() > memory.remaining()) {
			int originalBufferLimit = buffer.limit();
			int length = memory.remaining();
			buffer.limit(buffer.position() + length);
			memory.put(buffer);
			memory.position(originalMemoryPos);
			if (this.impl.chardetng_j_encoding_detector_feed(this.detector, this.buffer, length, 0) != 0) {
				seenAscii = true;
			}
			originalMemoryPos = memory.position();
			memory.position(this.buffer);
			buffer.limit(originalBufferLimit);
		}
		int length = buffer.remaining();
		memory.put(buffer);
		memory.position(originalMemoryPos);
		if (this.impl.chardetng_j_encoding_detector_feed(this.detector, this.buffer, length, last ? 1 : 0) != 0) {
			seenAscii = true;
		}
		assert buffer.position() == buffer.limit();
		return seenAscii;
	}

	/**
	 * Guess the encoding given the bytes pushed to the detector so far (via
	 * <code>feed()</code>), the top-level domain name from which the bytes were
	 * loaded, and an indication of whether to consider UTF-8 as a permissible
	 * guess.
	 *
	 * The <code>tld</code> argument takes the rightmost DNS label of the hostname
	 * of the host the stream was loaded from in lower-case ASCII form. That is, if
	 * the label is an internationalized top-level domain name, it must be provided
	 * in its Punycode form. If the TLD that the stream was loaded from is
	 * unavailable, <code>null</code> or <code>""</code> may be passed instead,
	 * which are equivalent to <code>"com"</code>.
	 *
	 * If the <code>allowUtf8</code> argument is set to <code>false</code>, the
	 * return value of this method won't be <code>UTF_8</code>. When performing
	 * detection on <code>text/html</code> on non-<code>file:</code> URLs, Web
	 * browsers must pass <code>false</code>, unless the user has taken a specific
	 * contextual action to request an override. This way, Web developers cannot
	 * start depending on UTF-8 detection. Such reliance would make the Web Platform
	 * more brittle.
	 *
	 * @return the guessed encoding, which is one of the constants on this class.
	 *
	 * @throws IllegalArgumentException if <code>tld</code> contains non-ASCII,
	 *                                  period, or upper-case letters. (The throwing
	 *                                  condition is intentionally limited to signs
	 *                                  of failing to extract the label correctly,
	 *                                  failing to provide it in its Punycode form,
	 *                                  and failure to lower-case it. Full DNS label
	 *                                  validation is intentionally not performed to
	 *                                  avoid exceptions when the reality doesn't
	 *                                  match the specs.)
	 */
	public Charset guess(String tld, boolean allowUtf8) {
		ByteBuffer memory = this.impl.getMemory();
		int originalMemoryPos = memory.position();
		if (tld == null) {
			tld = "";
		}
		int length = tld.length();
		memory.position(this.buffer);
		if (length > memory.remaining()) {
			memory.position(originalMemoryPos);
			throw new IllegalArgumentException("Unreasonably long TLD");
		}
		for (int i = 0; i < length; ++i) {
			char c = tld.charAt(i);
			if (c >= '\u0080' || c == '.' || (c >= 'A' && c <= 'Z')) {
				memory.position(originalMemoryPos);
				throw new IllegalArgumentException("TLD contains prohibited characters; must be in Punycode form.");
			}
			memory.put((byte) c);
		}
		memory.position(originalMemoryPos);
		return POSSIBLE_CHARSETS[this.impl.chardetng_j_encoding_detector_guess(this.detector, this.buffer, length,
				allowUtf8 ? 1 : 0)];
	}
}
