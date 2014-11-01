/*
 * Copyright 2014 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onlab.packet;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.onlab.junit.ImmutableClassChecker.assertThatClassIsImmutable;

/**
 * Tests for class {@link IpAddress}.
 */
public class IpAddressTest {
    /**
     * Tests the immutability of {@link IpAddress}.
     */
    @Test
    public void testImmutable() {
        assertThatClassIsImmutable(IpAddress.class);
    }

    /**
     * Tests the length of the address in bytes (octets).
     */
    @Test
    public void testAddrByteLength() {
        assertThat(IpAddress.INET_BYTE_LENGTH, is(4));
        assertThat(IpAddress.INET6_BYTE_LENGTH, is(16));
        assertThat(IpAddress.byteLength(IpAddress.Version.INET), is(4));
        assertThat(IpAddress.byteLength(IpAddress.Version.INET6), is(16));
    }

    /**
     * Tests the length of the address in bits.
     */
    @Test
    public void testAddrBitLength() {
        assertThat(IpAddress.INET_BIT_LENGTH, is(32));
        assertThat(IpAddress.INET6_BIT_LENGTH, is(128));
    }

    /**
     * Tests returning the IP address version.
     */
    @Test
    public void testVersion() {
        IpAddress ipAddress;

        // IPv4
        ipAddress = IpAddress.valueOf("0.0.0.0");
        assertThat(ipAddress.version(), is(IpAddress.Version.INET));

        // IPv6
        ipAddress = IpAddress.valueOf("::");
        assertThat(ipAddress.version(), is(IpAddress.Version.INET6));
    }

    /**
     * Tests returning an IPv4 address as a byte array.
     */
    @Test
    public void testAddressToOctetsIPv4() {
        IpAddress ipAddress;

        final byte[] value1 = new byte[] {1, 2, 3, 4};
        ipAddress = IpAddress.valueOf("1.2.3.4");
        assertThat(ipAddress.toOctets(), is(value1));

        final byte[] value2 = new byte[] {0, 0, 0, 0};
        ipAddress = IpAddress.valueOf("0.0.0.0");
        assertThat(ipAddress.toOctets(), is(value2));

        final byte[] value3 = new byte[] {(byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff};
        ipAddress = IpAddress.valueOf("255.255.255.255");
        assertThat(ipAddress.toOctets(), is(value3));
    }

    /**
     * Tests returning an IPv6 address as a byte array.
     */
    @Test
    public void testAddressToOctetsIPv6() {
        IpAddress ipAddress;

        final byte[] value1 = new byte[] {0x11, 0x11, 0x22, 0x22,
                                          0x33, 0x33, 0x44, 0x44,
                                          0x55, 0x55, 0x66, 0x66,
                                          0x77, 0x77,
                                          (byte) 0x88, (byte) 0x88};
        ipAddress =
            IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8888");
        assertThat(ipAddress.toOctets(), is(value1));

        final byte[] value2 = new byte[] {0x00, 0x00, 0x00, 0x00,
                                          0x00, 0x00, 0x00, 0x00,
                                          0x00, 0x00, 0x00, 0x00,
                                          0x00, 0x00, 0x00, 0x00};
        ipAddress = IpAddress.valueOf("::");
        assertThat(ipAddress.toOctets(), is(value2));

        final byte[] value3 = new byte[] {(byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff};
        ipAddress =
            IpAddress.valueOf("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        assertThat(ipAddress.toOctets(), is(value3));
    }

    /**
     * Tests returning an IPv4 address asn an integer.
     */
    @Test
    public void testToint() {
        IpAddress ipAddress;

        ipAddress = IpAddress.valueOf("1.2.3.4");
        assertThat(ipAddress.toInt(), is(0x01020304));

        ipAddress = IpAddress.valueOf("0.0.0.0");
        assertThat(ipAddress.toInt(), is(0));

        ipAddress = IpAddress.valueOf("255.255.255.255");
        assertThat(ipAddress.toInt(), is(-1));
    }

    /**
     * Tests valueOf() converter for an integer value.
     */
    @Test
    public void testValueOfForInteger() {
        IpAddress ipAddress;

        ipAddress = IpAddress.valueOf(0x01020304);
        assertThat(ipAddress.toString(), is("1.2.3.4"));

        ipAddress = IpAddress.valueOf(0);
        assertThat(ipAddress.toString(), is("0.0.0.0"));

        ipAddress = IpAddress.valueOf(0xffffffff);
        assertThat(ipAddress.toString(), is("255.255.255.255"));
    }

    /**
     * Tests valueOf() converter for IPv4 byte array.
     */
    @Test
    public void testValueOfByteArrayIPv4() {
        IpAddress ipAddress;

        final byte[] value1 = new byte[] {1, 2, 3, 4};
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET, value1);
        assertThat(ipAddress.toString(), is("1.2.3.4"));

        final byte[] value2 = new byte[] {0, 0, 0, 0};
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET, value2);
        assertThat(ipAddress.toString(), is("0.0.0.0"));

        final byte[] value3 = new byte[] {(byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff};
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET, value3);
        assertThat(ipAddress.toString(), is("255.255.255.255"));
    }

    /**
     * Tests valueOf() converter for IPv6 byte array.
     */
    @Test
    public void testValueOfByteArrayIPv6() {
        IpAddress ipAddress;

        final byte[] value1 = new byte[] {0x11, 0x11, 0x22, 0x22,
                                          0x33, 0x33, 0x44, 0x44,
                                          0x55, 0x55, 0x66, 0x66,
                                          0x77, 0x77,
                                          (byte) 0x88, (byte) 0x88};
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET6, value1);
        assertThat(ipAddress.toString(),
                   is("1111:2222:3333:4444:5555:6666:7777:8888"));

        final byte[] value2 = new byte[] {0x00, 0x00, 0x00, 0x00,
                                          0x00, 0x00, 0x00, 0x00,
                                          0x00, 0x00, 0x00, 0x00,
                                          0x00, 0x00, 0x00, 0x00};
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET6, value2);
        assertThat(ipAddress.toString(), is("::"));

        final byte[] value3 = new byte[] {(byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff};
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET6, value3);
        assertThat(ipAddress.toString(),
                   is("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));
    }

    /**
     * Tests invalid valueOf() converter for a null array for IPv4.
     */
    @Test(expected = NullPointerException.class)
    public void testInvalidValueOfNullArrayIPv4() {
        IpAddress ipAddress;

        final byte[] fromArray = null;
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET, fromArray);
    }

    /**
     * Tests invalid valueOf() converter for a null array for IPv6.
     */
    @Test(expected = NullPointerException.class)
    public void testInvalidValueOfNullArrayIPv6() {
        IpAddress ipAddress;

        final byte[] fromArray = null;
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET6, fromArray);
    }

    /**
     * Tests invalid valueOf() converger for an array that is too short for
     * IPv4.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueOfShortArrayIPv4() {
        IpAddress ipAddress;

        final byte[] fromArray = new byte[] {1, 2, 3};
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET, fromArray);
    }

    /**
     * Tests invalid valueOf() converger for an array that is too short for
     * IPv6.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueOfShortArrayIPv6() {
        IpAddress ipAddress;

        final byte[] fromArray = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET6, fromArray);
    }

    /**
     * Tests valueOf() converter for IPv4 byte array and an offset.
     */
    @Test
    public void testValueOfByteArrayOffsetIPv4() {
        IpAddress ipAddress;

        final byte[] value1 = new byte[] {11, 22, 33,   // Preamble
                                          1, 2, 3, 4,
                                          44, 55};      // Extra bytes
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET, value1, 3);
        assertThat(ipAddress.toString(), is("1.2.3.4"));

        final byte[] value2 = new byte[] {11, 22,       // Preamble
                                          0, 0, 0, 0,
                                          33};          // Extra bytes
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET, value2, 2);
        assertThat(ipAddress.toString(), is("0.0.0.0"));

        final byte[] value3 = new byte[] {11, 22,       // Preamble
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          33};          // Extra bytes
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET, value3, 2);
        assertThat(ipAddress.toString(), is("255.255.255.255"));
    }

    /**
     * Tests valueOf() converter for IPv6 byte array and an offset.
     */
    @Test
    public void testValueOfByteArrayOffsetIPv6() {
        IpAddress ipAddress;

        final byte[] value1 = new byte[] {11, 22, 33,           // Preamble
                                          0x11, 0x11, 0x22, 0x22,
                                          0x33, 0x33, 0x44, 0x44,
                                          0x55, 0x55, 0x66, 0x66,
                                          0x77, 0x77,
                                          (byte) 0x88, (byte) 0x88,
                                          44, 55};              // Extra bytes
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET6, value1, 3);
        assertThat(ipAddress.toString(),
                   is("1111:2222:3333:4444:5555:6666:7777:8888"));

        final byte[] value2 = new byte[] {11, 22,               // Preamble
                                          0x00, 0x00, 0x00, 0x00,
                                          0x00, 0x00, 0x00, 0x00,
                                          0x00, 0x00, 0x00, 0x00,
                                          0x00, 0x00, 0x00, 0x00,
                                          33};                  // Extra bytes
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET6, value2, 2);
        assertThat(ipAddress.toString(), is("::"));

        final byte[] value3 = new byte[] {11, 22,               // Preamble
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          (byte) 0xff, (byte) 0xff,
                                          33};                  // Extra bytes
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET6, value3, 2);
        assertThat(ipAddress.toString(),
                   is("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));
    }

    /**
     * Tests invalid valueOf() converger for an array and an invalid offset
     * for IPv4.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueOfArrayInvalidOffsetIPv4() {
        IpAddress ipAddress;

        final byte[] value1 = new byte[] {11, 22, 33,   // Preamble
                                          1, 2, 3, 4,
                                          44, 55};      // Extra bytes
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET, value1, 6);
    }

    /**
     * Tests invalid valueOf() converger for an array and an invalid offset
     * for IPv6.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueOfArrayInvalidOffsetIPv6() {
        IpAddress ipAddress;

        final byte[] value1 = new byte[] {11, 22, 33,           // Preamble
                                          0x11, 0x11, 0x22, 0x22,
                                          0x33, 0x33, 0x44, 0x44,
                                          0x55, 0x55, 0x66, 0x66,
                                          0x77, 0x77,
                                          (byte) 0x88, (byte) 0x88,
                                          44, 55};              // Extra bytes
        ipAddress = IpAddress.valueOf(IpAddress.Version.INET6, value1, 6);
    }

    /**
     * Tests valueOf() converter for IPv4 string.
     */
    @Test
    public void testValueOfStringIPv4() {
        IpAddress ipAddress;

        ipAddress = IpAddress.valueOf("1.2.3.4");
        assertThat(ipAddress.toString(), is("1.2.3.4"));

        ipAddress = IpAddress.valueOf("0.0.0.0");
        assertThat(ipAddress.toString(), is("0.0.0.0"));

        ipAddress = IpAddress.valueOf("255.255.255.255");
        assertThat(ipAddress.toString(), is("255.255.255.255"));
    }

    /**
     * Tests valueOf() converter for IPv6 string.
     */
    @Test
    public void testValueOfStringIPv6() {
        IpAddress ipAddress;

        ipAddress =
            IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8888");
        assertThat(ipAddress.toString(),
                   is("1111:2222:3333:4444:5555:6666:7777:8888"));

        ipAddress = IpAddress.valueOf("::");
        assertThat(ipAddress.toString(), is("::"));

        ipAddress =
            IpAddress.valueOf("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        assertThat(ipAddress.toString(),
                   is("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));
    }

    /**
     * Tests invalid valueOf() converter for a null string.
     */
    @Test(expected = NullPointerException.class)
    public void testInvalidValueOfNullString() {
        IpAddress ipAddress;

        String fromString = null;
        ipAddress = IpAddress.valueOf(fromString);
    }

    /**
     * Tests invalid valueOf() converter for an empty string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueOfEmptyString() {
        IpAddress ipAddress;

        String fromString = "";
        ipAddress = IpAddress.valueOf(fromString);
    }

    /**
     * Tests invalid valueOf() converter for an incorrect string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueOfIncorrectString() {
        IpAddress ipAddress;

        String fromString = "NoSuchIpAddress";
        ipAddress = IpAddress.valueOf(fromString);
    }

    /**
     * Tests making a mask prefix for a given prefix length for IPv4.
     */
    @Test
    public void testMakeMaskPrefixIPv4() {
        IpAddress ipAddress;

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET, 25);
        assertThat(ipAddress.toString(), is("255.255.255.128"));

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET, 0);
        assertThat(ipAddress.toString(), is("0.0.0.0"));

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET, 32);
        assertThat(ipAddress.toString(), is("255.255.255.255"));
    }

    /**
     * Tests making a mask prefix for a given prefix length for IPv6.
     */
    @Test
    public void testMakeMaskPrefixIPv6() {
        IpAddress ipAddress;

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET6, 8);
        assertThat(ipAddress.toString(), is("ff00::"));

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET6, 120);
        assertThat(ipAddress.toString(),
                   is("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ff00"));

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET6, 0);
        assertThat(ipAddress.toString(), is("::"));

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET6, 128);
        assertThat(ipAddress.toString(),
                   is("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET6, 64);
        assertThat(ipAddress.toString(), is("ffff:ffff:ffff:ffff::"));
    }

    /**
     * Tests making a mask prefix for an invalid prefix length for IPv4:
     * negative prefix length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMakeNegativeMaskPrefixIPv4() {
        IpAddress ipAddress;

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET, -1);
    }

    /**
     * Tests making a mask prefix for an invalid prefix length for IPv6:
     * negative prefix length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMakeNegativeMaskPrefixIPv6() {
        IpAddress ipAddress;

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET6, -1);
    }

    /**
     * Tests making a mask prefix for an invalid prefix length for IPv4:
     * too long prefix length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMakeTooLongMaskPrefixIPv4() {
        IpAddress ipAddress;

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET, 33);
    }

    /**
     * Tests making a mask prefix for an invalid prefix length for IPv6:
     * too long prefix length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMakeTooLongMaskPrefixIPv6() {
        IpAddress ipAddress;

        ipAddress = IpAddress.makeMaskPrefix(IpAddress.Version.INET6, 129);
    }

    /**
     * Tests making of a masked address for IPv4.
     */
    @Test
    public void testMakeMaskedAddressIPv4() {
        IpAddress ipAddress = IpAddress.valueOf("1.2.3.5");
        IpAddress ipAddressMasked;

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, 24);
        assertThat(ipAddressMasked.toString(), is("1.2.3.0"));

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, 0);
        assertThat(ipAddressMasked.toString(), is("0.0.0.0"));

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, 32);
        assertThat(ipAddressMasked.toString(), is("1.2.3.5"));
    }

    /**
     * Tests making of a masked address for IPv6.
     */
    @Test
    public void testMakeMaskedAddressIPv6() {
        IpAddress ipAddress =
            IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8885");
        IpAddress ipAddressMasked;

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, 8);
        assertThat(ipAddressMasked.toString(), is("1100::"));

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, 120);
        assertThat(ipAddressMasked.toString(),
                   is("1111:2222:3333:4444:5555:6666:7777:8800"));

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, 0);
        assertThat(ipAddressMasked.toString(), is("::"));

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, 128);
        assertThat(ipAddressMasked.toString(),
                   is("1111:2222:3333:4444:5555:6666:7777:8885"));

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, 64);
        assertThat(ipAddressMasked.toString(), is("1111:2222:3333:4444::"));
    }

    /**
     * Tests making of a masked address for invalid prefix length for IPv4:
     * negative prefix length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMakeNegativeMaskedAddressIPv4() {
        IpAddress ipAddress = IpAddress.valueOf("1.2.3.5");
        IpAddress ipAddressMasked;

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, -1);
    }

    /**
     * Tests making of a masked address for invalid prefix length for IPv6:
     * negative prefix length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMakeNegativeMaskedAddressIPv6() {
        IpAddress ipAddress =
            IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8885");
        IpAddress ipAddressMasked;

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, -1);
    }

    /**
     * Tests making of a masked address for an invalid prefix length for IPv4:
     * too long prefix length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMakeTooLongMaskedAddressIPv4() {
        IpAddress ipAddress = IpAddress.valueOf("1.2.3.5");
        IpAddress ipAddressMasked;

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, 33);
    }

    /**
     * Tests making of a masked address for an invalid prefix length for IPv6:
     * too long prefix length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMakeTooLongMaskedAddressIPv6() {
        IpAddress ipAddress =
            IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8885");
        IpAddress ipAddressMasked;

        ipAddressMasked = IpAddress.makeMaskedAddress(ipAddress, 129);
    }

    /**
     * Tests comparison of {@link IpAddress} for IPv4.
     */
    @Test
    public void testComparisonIPv4() {
        IpAddress addr1, addr2, addr3, addr4;

        addr1 = IpAddress.valueOf("1.2.3.4");
        addr2 = IpAddress.valueOf("1.2.3.4");
        addr3 = IpAddress.valueOf("1.2.3.3");
        addr4 = IpAddress.valueOf("1.2.3.5");
        assertTrue(addr1.compareTo(addr2) == 0);
        assertTrue(addr1.compareTo(addr3) > 0);
        assertTrue(addr1.compareTo(addr4) < 0);

        addr1 = IpAddress.valueOf("255.2.3.4");
        addr2 = IpAddress.valueOf("255.2.3.4");
        addr3 = IpAddress.valueOf("255.2.3.3");
        addr4 = IpAddress.valueOf("255.2.3.5");
        assertTrue(addr1.compareTo(addr2) == 0);
        assertTrue(addr1.compareTo(addr3) > 0);
        assertTrue(addr1.compareTo(addr4) < 0);
    }

    /**
     * Tests comparison of {@link IpAddress} for IPv6.
     */
    @Test
    public void testComparisonIPv6() {
        IpAddress addr1, addr2, addr3, addr4;

        addr1 = IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8888");
        addr2 = IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8888");
        addr3 = IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8887");
        addr4 = IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8889");
        assertTrue(addr1.compareTo(addr2) == 0);
        assertTrue(addr1.compareTo(addr3) > 0);
        assertTrue(addr1.compareTo(addr4) < 0);

        addr1 = IpAddress.valueOf("ffff:2222:3333:4444:5555:6666:7777:8888");
        addr2 = IpAddress.valueOf("ffff:2222:3333:4444:5555:6666:7777:8888");
        addr3 = IpAddress.valueOf("ffff:2222:3333:4444:5555:6666:7777:8887");
        addr4 = IpAddress.valueOf("ffff:2222:3333:4444:5555:6666:7777:8889");
        assertTrue(addr1.compareTo(addr2) == 0);
        assertTrue(addr1.compareTo(addr3) > 0);
        assertTrue(addr1.compareTo(addr4) < 0);

        addr1 = IpAddress.valueOf("ffff:2222:3333:4444:5555:6666:7777:8888");
        addr2 = IpAddress.valueOf("ffff:2222:3333:4444:5555:6666:7777:8888");
        addr3 = IpAddress.valueOf("ffff:2222:3333:4443:5555:6666:7777:8888");
        addr4 = IpAddress.valueOf("ffff:2222:3333:4445:5555:6666:7777:8888");
        assertTrue(addr1.compareTo(addr2) == 0);
        assertTrue(addr1.compareTo(addr3) > 0);
        assertTrue(addr1.compareTo(addr4) < 0);
    }

    /**
     * Tests equality of {@link IpAddress} for IPv4.
     */
    @Test
    public void testEqualityIPv4() {
        IpAddress addr1, addr2;

        addr1 = IpAddress.valueOf("1.2.3.4");
        addr2 = IpAddress.valueOf("1.2.3.4");
        assertThat(addr1, is(addr2));

        addr1 = IpAddress.valueOf("0.0.0.0");
        addr2 = IpAddress.valueOf("0.0.0.0");
        assertThat(addr1, is(addr2));

        addr1 = IpAddress.valueOf("255.255.255.255");
        addr2 = IpAddress.valueOf("255.255.255.255");
        assertThat(addr1, is(addr2));
    }

    /**
     * Tests equality of {@link IpAddress} for IPv6.
     */
    @Test
    public void testEqualityIPv6() {
        IpAddress addr1, addr2;

        addr1 = IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8888");
        addr2 = IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8888");
        assertThat(addr1, is(addr2));

        addr1 = IpAddress.valueOf("::");
        addr2 = IpAddress.valueOf("::");
        assertThat(addr1, is(addr2));

        addr1 = IpAddress.valueOf("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        addr2 = IpAddress.valueOf("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        assertThat(addr1, is(addr2));
    }

    /**
     * Tests non-equality of {@link IpAddress} for IPv4.
     */
    @Test
    public void testNonEqualityIPv4() {
        IpAddress addr1, addr2, addr3, addr4;

        addr1 = IpAddress.valueOf("1.2.3.4");
        addr2 = IpAddress.valueOf("1.2.3.5");
        addr3 = IpAddress.valueOf("0.0.0.0");
        addr4 = IpAddress.valueOf("255.255.255.255");
        assertThat(addr1, is(not(addr2)));
        assertThat(addr3, is(not(addr2)));
        assertThat(addr4, is(not(addr2)));
    }

    /**
     * Tests non-equality of {@link IpAddress} for IPv6.
     */
    @Test
    public void testNonEqualityIPv6() {
        IpAddress addr1, addr2, addr3, addr4;

        addr1 = IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8888");
        addr2 = IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:888A");
        addr3 = IpAddress.valueOf("::");
        addr4 = IpAddress.valueOf("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        assertThat(addr1, is(not(addr2)));
        assertThat(addr3, is(not(addr2)));
        assertThat(addr4, is(not(addr2)));
    }

    /**
     * Tests object string representation for IPv4.
     */
    @Test
    public void testToStringIPv4() {
        IpAddress ipAddress;

        ipAddress = IpAddress.valueOf("1.2.3.4");
        assertThat(ipAddress.toString(), is("1.2.3.4"));

        ipAddress = IpAddress.valueOf("0.0.0.0");
        assertThat(ipAddress.toString(), is("0.0.0.0"));

        ipAddress = IpAddress.valueOf("255.255.255.255");
        assertThat(ipAddress.toString(), is("255.255.255.255"));
    }

    /**
     * Tests object string representation for IPv6.
     */
    @Test
    public void testToStringIPv6() {
        IpAddress ipAddress;

        ipAddress =
            IpAddress.valueOf("1111:2222:3333:4444:5555:6666:7777:8888");
        assertThat(ipAddress.toString(),
                   is("1111:2222:3333:4444:5555:6666:7777:8888"));

        ipAddress = IpAddress.valueOf("1111::8888");
        assertThat(ipAddress.toString(), is("1111::8888"));

        ipAddress = IpAddress.valueOf("1111::");
        assertThat(ipAddress.toString(), is("1111::"));

        ipAddress = IpAddress.valueOf("::8888");
        assertThat(ipAddress.toString(), is("::8888"));

        ipAddress = IpAddress.valueOf("::");
        assertThat(ipAddress.toString(), is("::"));

        ipAddress =
            IpAddress.valueOf("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        assertThat(ipAddress.toString(),
                   is("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));
    }
}
