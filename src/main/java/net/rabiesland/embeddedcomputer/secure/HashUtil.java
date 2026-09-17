/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.secure;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
public class HashUtil {
    public static String hashStrSHA256(String str) {
        HashFunction hash = Hashing.sha256();
        return hash.hashString(str, StandardCharsets.ISO_8859_1).toString();
    }
    public static String hashStrSHA512(String str) {
        HashFunction hash = Hashing.sha512();
        return hash.hashString(str, StandardCharsets.ISO_8859_1).toString();
    }
    public static String hashStrMurmur3(String str) {
        HashFunction hash = Hashing.murmur3_128();
        return hash.hashString(str, StandardCharsets.ISO_8859_1).toString();
    }
    public static String hashStrAdler32(String str) {
        HashFunction hash = Hashing.adler32();
        return hash.hashString(str, StandardCharsets.ISO_8859_1).toString();
    }
    public static String hashStrSipHash24(String str) {
        HashFunction hash = Hashing.sipHash24();
        return hash.hashString(str, StandardCharsets.ISO_8859_1).toString();
    }
}
