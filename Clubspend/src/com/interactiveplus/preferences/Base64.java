package com.interactiveplus.preferences;

/*
 * pwhash - a password scrambling library
 * Copyright (c) $year Noa Resare <noa@voxbiblia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * The encode() method of this class encodes binary data into an US-ASCII
 * String according to the algorithm described in RFC2045 section 6.8.
 *
 * This implementation is optimized for readablility and could probably be made
 * somewhat more efficient, however there are lots of Base64 implementations
 * out there that does that tradeoff.
 *
 * @author Noa Resare (noa@voxbiblia.com)
 */
public class Base64
{
    private static char[] key =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                    .toCharArray();

    /**
     * Encodes the given byte array of binary data into a character String
     * according to the algorithm describedin RFC2045 section 6.8. Please
     * note that the resulting String doesn't include line breaks, which
     * will need to be added before it is suitable for inclusion in for example
     * RFC2822 email transmissions.
     *
     * @param data the binary data to encode
     * @return the resulting String
     */
    public static String encode(byte[] data)
    {
        StringBuffer sb = new StringBuffer((int)(data.length * 1.33) + 2);
        int i = 0;
        for (; i < (data.length - 2); i += 3) {
            encodeOutput((pos(data[i]) << 16) + (pos(data[i + 1]) << 8) +
                    pos(data[i + 2]), sb, 0);
        }
        if (i == data.length - 2) {
            encodeOutput((pos(data[i]) << 16) + (pos(data[i + 1]) << 8), sb, 1);
        } else if (i == data.length - 1) {
            encodeOutput(pos(data[i]) << 16, sb, 2);
        }

        return sb.toString();
    }

    /**
     * Accepts an input String encoded according to the algorithm described in
     * RFC2045 section 6.8, decodes it's content and returns the resulting byte
     * array.
     *
     * @param input a String containing Base64 encoded data.
     * @return a byte array containing the decoded data.
     */
    public static byte[] decode(String input)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (input.length() % 4 != 0) {
            throw new IllegalArgumentException("The length of the Base64 " +
                    "data needs to be an even multiple of 4");
        }
        for (int i = 0; i < input.length(); i += 4) {
            decodeOutput(input.charAt(i), input.charAt(i+1),
                    input.charAt(i+2), input.charAt(i+3),baos);
        }
        return baos.toByteArray();
    }

    private static void decodeOutput(char a, char b, char c, char d,
                                     OutputStream os)
    {
        try {
            os.write(at(a) << 2 | at(b) >>> 4);
            if (c != '=') {
                os.write((at(b) & 0xf) << 4 | at(c) >>> 2);
            }
            if (d != '=') {
                os.write((at(c) & 0x3) << 6 | at(d));
            }
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    private static int at(char c)
    {
        for (int i = 0 ; i < key.length; i++) {
            if (key[i] == c) {
                return i;
            }
        }
        throw new IllegalArgumentException("Could not decode Base64 data, " +
                "char "+ c +" is not in the Base64 alphabet");
    }

    /**
     * Writes base64 output to sb using data from the quantum, possibly
     * padding the written string with equals signs to indicate that the
     * quantum corresponds to less than 3 bytes of data. For the exact
     * semantics of this method, please see RFC2045 section 6.8.
     *
     * @param quantum the data to be written
     * @param sb the StringBuffer that the data should be appended to
     * @param pad can indicate that quantum only holds 1 or 2 bytes of data
     * in which case padding occurs according to spec.
     */
    private static void encodeOutput(int quantum, StringBuffer sb, int pad)
    {
        sb.append(key[(quantum >> 18) & 0x3f]);
        sb.append(key[(quantum >> 12) & 0x3f]);
        if (pad == 2) {
            sb.append("==");
        } else if (pad == 1) {
            sb.append(key[(quantum >> 6) & 0x3f]);
            sb.append('=');
        } else {
            sb.append(key[(quantum >> 6) & 0x3f]);
            sb.append(key[quantum & 0x3f]);
        }
    }


    /**
     * Returns the integer value of a byte if it had been unsigned.
     *
     * @param b a java byte (signed) value
     * @return an int that corresponds to the byte if it had been unsigned
     */
    static int pos(byte b)
    {
        return b < 0 ? b + 0x100 : b;
    }

}
