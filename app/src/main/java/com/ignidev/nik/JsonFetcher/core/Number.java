package com.ignidev.nik.JsonFetcher.core;


import android.support.annotation.NonNull;

/**
 * POJO for storing parsed data.
 * Number represents object, parsed from simple number from range [0..255],
 * e.g. 1111_1111b, where:
 * 1111_11xx - define section number;
 * 1xxx_xx11 - define actual value;
 * x111_1111 - define presence of a check mark.
 */
public class Number implements Comparable<Number> {
    private final int section;
    private final int value;
    private final boolean checkMark;

    public Number(int rawValue) {
        this(rawValue & 0b11, (rawValue >> 2) & 0b11111, rawValue >> 7 == 1);
    }

    public Number(int section, int value, boolean checkMark) {
        this.section = section;
        this.value = value;
        this.checkMark = checkMark;
    }

    public int getSection() {
        return section;
    }

    public int getValue() {
        return value;
    }

    public boolean isCheckMark() {
        return checkMark;
    }

    @Override
    public int hashCode() {
        return value * 31 +
                section * 31 +
                (checkMark ? 1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Number &&
                value == ((Number) obj).value &&
                section == ((Number) obj).section &&
                checkMark == ((Number) obj).checkMark;
    }

    @Override
    public String toString() {
        return "Number: Value " + value + ", section " + section + ", checkMark " + checkMark;
    }

    @Override
    public int compareTo(@NonNull Number other) {
        return section != other.section ?
                section - other.section :
                value - other.value;
    }
}
