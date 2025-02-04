/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.timedetector;

import android.annotation.IntDef;
import android.annotation.NonNull;
import android.annotation.Nullable;
import android.app.timedetector.ManualTimeSuggestion;
import android.app.timedetector.NetworkTimeSuggestion;
import android.app.timedetector.TelephonyTimeSuggestion;
import android.os.TimestampedValue;

import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The interface for the class that implements the time detection algorithm used by the
 * {@link TimeDetectorService}.
 *
 * <p>Most calls will be handled by a single thread but that is not true for all calls. For example
 * {@link #dump(PrintWriter, String[])}) may be called on a different thread so implementations must
 * handle thread safety.
 *
 * @hide
 */
public interface TimeDetectorStrategy {

    @IntDef({ ORIGIN_TELEPHONY, ORIGIN_MANUAL, ORIGIN_NETWORK })
    @Retention(RetentionPolicy.SOURCE)
    @interface Origin {}

    /** Used when a time value originated from a telephony signal. */
    @Origin
    int ORIGIN_TELEPHONY = 1;

    /** Used when a time value originated from a user / manual settings. */
    @Origin
    int ORIGIN_MANUAL = 2;

    /** Used when a time value originated from a network signal. */
    @Origin
    int ORIGIN_NETWORK = 3;

    /** Processes the suggested time from telephony sources. */
    void suggestTelephonyTime(@NonNull TelephonyTimeSuggestion timeSuggestion);

    /**
     * Processes the suggested manually entered time. Returns {@code false} if the suggestion was
     * invalid, or the device configuration prevented the suggestion being used, {@code true} if the
     * suggestion was accepted. A suggestion that is valid but does not change the time because it
     * matches the current device time is considered accepted.
     */
    boolean suggestManualTime(@NonNull ManualTimeSuggestion timeSuggestion);

    /** Processes the suggested time from network sources. */
    void suggestNetworkTime(@NonNull NetworkTimeSuggestion timeSuggestion);

    /**
     * Handles the auto-time configuration changing For example, when the auto-time setting is
     * toggled on or off.
     */
    void handleAutoTimeConfigChanged();

    /** Dump debug information. */
    void dump(@NonNull PrintWriter pw, @Nullable String[] args);

    // Utility methods below are to be moved to a better home when one becomes more obvious.

    /**
     * Adjusts the supplied time value by applying the difference between the reference time
     * supplied and the reference time associated with the time.
     */
    static long getTimeAt(@NonNull TimestampedValue<Long> timeValue, long referenceClockMillisNow) {
        return (referenceClockMillisNow - timeValue.getReferenceTimeMillis())
                + timeValue.getValue();
    }

    /**
     * Converts one of the {@code ORIGIN_} constants to a human readable string suitable for config
     * and debug usage. Throws an {@link IllegalArgumentException} if the value is unrecognized.
     */
    static String originToString(@Origin int origin) {
        switch (origin) {
            case ORIGIN_MANUAL:
                return "manual";
            case ORIGIN_NETWORK:
                return "network";
            case ORIGIN_TELEPHONY:
                return "telephony";
            default:
                throw new IllegalArgumentException("origin=" + origin);
        }
    }

    /**
     * Converts a human readable config string to one of the {@code ORIGIN_} constants.
     * Throws an {@link IllegalArgumentException} if the value is unrecognized.
     */
    static @Origin int stringToOrigin(String originString) {
        switch (originString) {
            case "manual":
                return ORIGIN_MANUAL;
            case "network":
                return ORIGIN_NETWORK;
            case "telephony":
                return ORIGIN_TELEPHONY;
            default:
                throw new IllegalArgumentException("originString=" + originString);
        }
    }
}
