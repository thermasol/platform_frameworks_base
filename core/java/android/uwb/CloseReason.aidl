/*
 * Copyright 2020 The Android Open Source Project
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

package android.uwb;

/**
 * @hide
 */
@Backing(type="int")
enum CloseReason {
  /**
   * Unknown reason
   */
  UNKNOWN,

  /**
   * A local API call triggered the close, such as a call to
   * IUwbAdapter.stopRanging.
   */
  LOCAL_API,

  /**
   * The maximum number of sessions has been reached. This error may be generated
   * for an active session if a higher priority session begins.
   */
  MAX_SESSIONS_REACHED,

  /**
   * The system state has changed resulting in the session ending (e.g. the user
   * disables UWB, or the user's locale changes and an active channel is no longer
   * permitted to be used).
   */
  SYSTEM_POLICY,

  /**
   * The remote device has requested to terminate the session
   */
  REMOTE_REQUEST,

  /**
   * The session was closed for a protocol specific reason
   */
  PROTOCOL_SPECIFIC,
}

