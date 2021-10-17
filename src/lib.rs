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

#![no_std]

#[cfg(not(test))]
use core::panic::PanicInfo;

use chardetng::EncodingDetector;
use encoding_rs::Encoding;
use encoding_rs::BIG5_INIT;
use encoding_rs::EUC_JP_INIT;
use encoding_rs::EUC_KR_INIT;
use encoding_rs::GBK_INIT;
use encoding_rs::IBM866_INIT;
use encoding_rs::ISO_2022_JP_INIT;
use encoding_rs::ISO_8859_13_INIT;
use encoding_rs::ISO_8859_2_INIT;
use encoding_rs::ISO_8859_4_INIT;
use encoding_rs::ISO_8859_5_INIT;
use encoding_rs::ISO_8859_6_INIT;
use encoding_rs::ISO_8859_7_INIT;
use encoding_rs::ISO_8859_8_INIT;
use encoding_rs::KOI8_U_INIT;
use encoding_rs::SHIFT_JIS_INIT;
use encoding_rs::UTF_8_INIT;
use encoding_rs::WINDOWS_1250_INIT;
use encoding_rs::WINDOWS_1251_INIT;
use encoding_rs::WINDOWS_1252_INIT;
use encoding_rs::WINDOWS_1253_INIT;
use encoding_rs::WINDOWS_1254_INIT;
use encoding_rs::WINDOWS_1255_INIT;
use encoding_rs::WINDOWS_1256_INIT;
use encoding_rs::WINDOWS_1257_INIT;
use encoding_rs::WINDOWS_1258_INIT;
use encoding_rs::WINDOWS_874_INIT;

#[cfg(not(test))]
#[panic_handler]
#[inline(always)]
fn panic(_: &PanicInfo) -> ! {
    // Let's trust prior testing and UB all panics within the Wasm
    // sandbox to let LLVM optimize further while relying on the JVM
    // for safety.
    unsafe { core::hint::unreachable_unchecked() }
}

// Possible return values in approximate order of frequency
// of use on the Web.
static POSSIBLE_ENCODINGS: [&'static Encoding; 26] = [
    &UTF_8_INIT,
    &WINDOWS_1252_INIT,
    &WINDOWS_1251_INIT,
    &GBK_INIT,
    &SHIFT_JIS_INIT,
    &EUC_KR_INIT,
    &WINDOWS_1250_INIT,
    &WINDOWS_1256_INIT,
    &WINDOWS_1254_INIT,
    &BIG5_INIT,
    &WINDOWS_874_INIT,
    &WINDOWS_1255_INIT,
    &WINDOWS_1253_INIT,
    &WINDOWS_1257_INIT,
    &WINDOWS_1258_INIT,
    &EUC_JP_INIT,
    &ISO_8859_2_INIT,
    &ISO_8859_7_INIT,
    &ISO_8859_5_INIT,
    &ISO_8859_4_INIT,
    &ISO_8859_6_INIT,
    &ISO_2022_JP_INIT,
    &KOI8_U_INIT,
    &ISO_8859_13_INIT,
    &IBM866_INIT,
    &ISO_8859_8_INIT,
];

/// Writes a new `EncodingDetector` into `ptr` without reading from `ptr`.
#[no_mangle]
pub unsafe extern "C" fn chardetng_j_encoding_detector_new_into(ptr: *mut EncodingDetector) {
    core::ptr::write(ptr, EncodingDetector::new());
}

/// Returns the size of `EncodingDetector`.
#[no_mangle]
pub unsafe extern "C" fn chardetng_j_size_of_encoding_detector() -> usize {
    core::mem::size_of::<EncodingDetector>()
}

/// Inform the detector of a chunk of input.
///
/// The byte stream is represented as a sequence of calls to this
/// function such that the concatenation of the arguments to this
/// function form the byte stream. It does not matter how the application
/// chooses to chunk the stream. It is OK to call this function with
/// a zero-length byte slice.
///
/// The end of the stream is indicated by calling this function with
/// `last` set to `true`. In that case, the end of the stream is
/// considered to occur after the last byte of the `buffer` (which
/// may be zero-length) passed in the same call. Once this function
/// has been called with `last` set to `true` this function must not
/// be called again.
///
/// If you want to perform detection on just the prefix of a longer
/// stream, do not pass `last=true` after the prefix if the stream
/// actually still continues.
///
/// Returns `true` if after processing `buffer` the stream has
/// contained at least one non-ASCII byte and `false` if only
/// ASCII has been seen so far.
///
/// # Panics
///
/// If this function has previously been called with `last` set to `true`.
///
/// # Undefined Behavior
///
/// UB ensues if
///
/// * `detector` does not point to a detector obtained from
///   `chardetng_detector_new` but not yet freed with
///   `chardetng_detector_free`.
/// * `buffer` is `NULL`. (It can be a bogus pointer when `buffer_len` is 0.)
/// * ,buffer_len` is non-zero and `buffer` and `buffer_len` don't designate
///    a range of memory valid for reading.
#[no_mangle]
pub unsafe extern "C" fn chardetng_j_encoding_detector_feed(
    detector: *mut EncodingDetector,
    buffer: *const u8,
    buffer_len: usize,
    last: bool,
) -> bool {
    (*detector).feed(::core::slice::from_raw_parts(buffer, buffer_len), last)
}

/// Guess the encoding given the bytes pushed to the detector so far
/// (via `chardetng_encoding_detector_feed()`), the top-level domain name
/// from which the bytes were loaded, and an indication of whether to
/// consider UTF-8 as a permissible guess.
///
/// The `tld` argument takes the rightmost DNS label of the hostname of the
/// host the stream was loaded from in lower-case ASCII form. That is, if
/// the label is an internationalized top-level domain name, it must be
/// provided in its Punycode form. If the TLD that the stream was loaded
/// from is unavalable, `NULL` may be passed instead (and 0 as `tld_len`),
/// which is equivalent to passing pointer to "com" as `tld` and 3 as
/// `tld_len`.
///
/// If the `allow_utf8` argument is set to `false`, the return value of
/// this function won't be `UTF_8_ENCODING`. When performing detection
/// on `text/html` on non-`file:` URLs, Web browsers must pass `false`,
/// unless the user has taken a specific contextual action to request an
/// override. This way, Web developers cannot start depending on UTF-8
/// detection. Such reliance would make the Web Platform more brittle.
///
/// Returns the guessed encoding (never `NULL`).
///
/// # Panics
///
/// If `tld` contains non-ASCII, period, or upper-case letters. (The panic
/// condition is intentionally limited to signs of failing to extract the
/// label correctly, failing to provide it in its Punycode form, and failure
/// to lower-case it. Full DNS label validation is intentionally not performed
/// to avoid panics when the reality doesn't match the specs.)
///
/// # Undefined Behavior
///
/// UB ensues if
///
/// * `detector` does not point to a detector created with
///   `chardetng_j_encoding_detector_new_into`.
/// * `tld` and `tld_len` don't designate a range of memory valid for reading.
#[no_mangle]
pub unsafe extern "C" fn chardetng_j_encoding_detector_guess(
    detector: *const EncodingDetector,
    tld: *const u8,
    tld_len: usize,
    allow_utf8: bool,
) -> usize {
    let tld_opt = Some(::core::slice::from_raw_parts(tld, tld_len));
    let encoding = (*detector).guess(tld_opt, allow_utf8);
    POSSIBLE_ENCODINGS
        .iter()
        .position(|&x| x == encoding)
        .unwrap()
}
