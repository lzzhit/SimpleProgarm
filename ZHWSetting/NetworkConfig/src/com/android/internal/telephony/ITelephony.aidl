/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.android.internal.telephony;

import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;

import com.android.internal.telephony.ITelephonyListener;

import java.util.List;

import android.telephony.IccCardInfo;
import android.os.IBinder;

/**
 * Interface used to interact with the phone.  Mostly this is used by the
 * TelephonyManager class.  A few places are still using this directly.
 * Please clean them up if possible and use TelephonyManager insteadl.
 *
 * {@hide}
 */
interface ITelephony {

    /**
     * Dial a number. This doesn't place the call. It displays
     * the Dialer screen.
     * @param number the number to be dialed. If null, this
     * would display the Dialer screen with no number pre-filled.
     */
    void dial(String number);

    /**
     * Place a call to the specified number.
     * @param number the number to be called.
     */
    void call(String callingPackage, String number);

    /**
     * If there is currently a call in progress, show the call screen.
     * The DTMF dialpad may or may not be visible initially, depending on
     * whether it was up when the user last exited the InCallScreen.
     *
     * @return true if the call screen was shown.
     */
    boolean showCallScreen();

    /**
     * Variation of showCallScreen() that also specifies whether the
     * DTMF dialpad should be initially visible when the InCallScreen
     * comes up.
     *
     * @param showDialpad if true, make the dialpad visible initially,
     *                    otherwise hide the dialpad initially.
     * @return true if the call screen was shown.
     *
     * @see showCallScreen
     */
    boolean showCallScreenWithDialpad(boolean showDialpad);

    /**
     * End call if there is a call in progress, otherwise does nothing.
     *
     * @return whether it hung up
     */
    boolean endCall();

    /**
     * Answer the currently-ringing call.
     *
     * If there's already a current active call, that call will be
     * automatically put on hold.  If both lines are currently in use, the
     * current active call will be ended.
     *
     * TODO: provide a flag to let the caller specify what policy to use
     * if both lines are in use.  (The current behavior is hardwired to
     * "answer incoming, end ongoing", which is how the CALL button
     * is specced to behave.)
     *
     * TODO: this should be a oneway call (especially since it's called
     * directly from the key queue thread).
     */
    void answerRingingCall();

    /**
     * Silence the ringer if an incoming call is currently ringing.
     * (If vibrating, stop the vibrator also.)
     *
     * It's safe to call this if the ringer has already been silenced, or
     * even if there's no incoming call.  (If so, this method will do nothing.)
     *
     * TODO: this should be a oneway call too (see above).
     *       (Actually *all* the methods here that return void can
     *       probably be oneway.)
     */
    void silenceRinger();

    /**
     * Check if we are in either an active or holding call
     * @return true if the phone state is OFFHOOK.
     */
    boolean isOffhook();

    /**
     * Check if an incoming phone call is ringing or call waiting.
     * @return true if the phone state is RINGING.
     */
    boolean isRinging();

    /**
     * Check if the phone is idle.
     * @return true if the phone state is IDLE.
     */
    boolean isIdle();

    /**
     * Check if we are in either an active or holding call
     * @return true if the phone state is OFFHOOK.
     */
    boolean isOffhookMSMS(int linkId);

    /**
     * Check if an incoming phone call is ringing or call waiting.
     * @return true if the phone state is RINGING.
     */
    boolean isRingingMSMS(int linkId);

    /**
     * Check if the phone is idle.
     * @return true if the phone state is IDLE.
     */
    boolean isIdleMSMS(int linkId);

    /**
     * Check to see if the radio is on or not.
     * @return returns true if the radio is on.
     */
    boolean isRadioOn();

    /**
     * Check if the SIM pin lock is enabled.
     * @return true if the SIM pin lock is enabled.
     */
    boolean isSimPinEnabled();

    /**
     * Cancels the missed calls notification.
     */
    void cancelMissedCallsNotification();

    /**
     * Supply a pin to unlock the SIM.  Blocks until a result is determined.
     * @param pin The pin to check.
     * @return whether the operation was a success.
     */
    boolean supplyPin(String pin);

    /**
     * Supply puk to unlock the SIM and set SIM pin to new pin.
     *  Blocks until a result is determined.
     * @param puk The puk to check.
     *        pin The new pin to be set in SIM
     * @return whether the operation was a success.
     */
    boolean supplyPuk(String puk, String pin);

    /**
     * Supply a pin to unlock the SIM.  Blocks until a result is determined.
     * Returns a specific success/error code.
     * @param pin The pin to check.
     * @return retValue[0] = Phone.PIN_RESULT_SUCCESS on success. Otherwise error code
     *         retValue[1] = number of attempts remaining if known otherwise -1
     */
    int[] supplyPinReportResult(String pin);

    /**
     * Supply puk to unlock the SIM and set SIM pin to new pin.
     * Blocks until a result is determined.
     * Returns a specific success/error code
     * @param puk The puk to check
     *        pin The pin to check.
     * @return retValue[0] = Phone.PIN_RESULT_SUCCESS on success. Otherwise error code
     *         retValue[1] = number of attempts remaining if known otherwise -1
     */
    int[] supplyPukReportResult(String puk, String pin);

    /**
     * Handles PIN MMI commands (PIN/PIN2/PUK/PUK2), which are initiated
     * without SEND (so <code>dial</code> is not appropriate).
     *
     * @param dialString the MMI command to be executed.
     * @return true if MMI command is executed.
     */
    boolean handlePinMmi(String dialString);

    /**
     * Toggles the radio on or off.
     */
    void toggleRadioOnOff();

    void toggleRadioOnOffMSMS(int linkId);

    /**
     * Set the radio to on or off
     */
    boolean setRadio(boolean turnOn);

    boolean setRadioMSMS(boolean turnOn, int linkId);

    /**
     * Set the radio to on or off unconditionally
     */
    boolean setRadioPower(boolean turnOn);

    /**
     * Request to update location information in service state
     */
    void updateServiceLocation();

    /**
     * Enable location update notifications.
     */
    void enableLocationUpdates();

    /**
     * Disable location update notifications.
     */
    void disableLocationUpdates();

    /**
     * Enable a specific APN type.
     */
    int enableApnType(String type);

    /**
     * Enable a specific APN type.
     */
    int enableApnTypeMSMS(String type, int linkID);
    /**
     * Disable a specific APN type.
     */
    int disableApnType(String type);
    /**
     * Disable a specific APN type.
     */
    int disableApnTypeMSMS(String type, int linkID);

    /**
     * set fake IPv4 address fo BIH.
     */
    void setFakeIPv4AddrToInterfaceMSMS(String apnType, String fakeIPv4Addr, int linkID);

    /**
     * Allow mobile data connections.
     */
    boolean enableDataConnectivity();

    /**
     * Disallow mobile data connections.
     */
    boolean disableDataConnectivity();

    /**
     * Report whether data connectivity is possible.
     */
    boolean isDataConnectivityPossible();

    Bundle getCellLocation();

    //Do-not-delete-multisim-flag begin
    //add by yuanhaobo for L1813_Bug00000662 @ 2013-07-08 begin
    Bundle getCellLocationMSMS(int linkId);
    //add by yuanhaobo for L1813_Bug00000662 @ 2013-07-08 end
    //Do-not-delete-multisim-flag end

    /**
     * Returns the neighboring cell information of the device.
     */
    List<NeighboringCellInfo> getNeighboringCellInfo(String callingPkg);

     int getCallState();
     int getDataActivity();
     int getDataState();

     //Do-not-delete-multisim-flag begin
     int getCallStateMSMS(int linkId);
     int getDataStateMSMS(int linkId);
     int getDataActivityMSMS(int linkId);
     //Do-not-delete-multisim-flag end

    /**
     * Returns the current active phone type as integer.
     * Returns TelephonyManager.PHONE_TYPE_CDMA if RILConstants.CDMA_PHONE
     * and TelephonyManager.PHONE_TYPE_GSM if RILConstants.GSM_PHONE
     */
    int getActivePhoneType();

    //Do-not-delete-multisim-flag begin
    //add by yuanhaobo for L1813_Bug00000662 @ 2013-07-08 begin
    int getActivePhoneTypeMSMS(int linkId);
    //add by yuanhaobo for L1813_Bug00000662 @ 2013-07-08 end
    //Do-not-delete-multisim-flag end

    /**
     * Returns the CDMA ERI icon index to display
     */
    int getCdmaEriIconIndex();

    /**
     * Returns the CDMA ERI icon mode,
     * 0 - ON
     * 1 - FLASHING
     */
    int getCdmaEriIconMode();

    /**
     * Returns the CDMA ERI text,
     */
    String getCdmaEriText();

    /**
     * Returns true if OTA service provisioning needs to run.
     * Only relevant on some technologies, others will always
     * return false.
     */
    boolean needsOtaServiceProvisioning();

    /**
      * Returns the unread count of voicemails
      */
    int getVoiceMessageCount();

    /**
      * Returns the network type for data transmission
      */
    int getNetworkType();

    /**
      * Returns the network type relative to linkId
      */
    int getNetworkTypeMSMS(int linkId);

    /**
      * Returns the network type for data transmission
      */
    int getDataNetworkType();
    int getDataNetworkTypeMSMS(int linkId);     /*modify by yuanhaobo for L1860_Req00000163 @ 2014-02-16*/

    /**
      * Returns the network type for voice
      */
    int getVoiceNetworkType();
    int getVoiceNetworkTypeMSMS(int linkId);    /*modify by yuanhaobo for L1860_Req00000163 @ 2014-02-16*/

    /**
     * Return true if an ICC card is present
     */
    boolean hasIccCard();

    /**
     * Return true if an ICC card is present relative to linkId
     */
    boolean hasIccCardMSMS(int linkId);

    /**
     * Return true if it is sim card.
     */
    boolean isSimCardMSMS(int linkId);

    //Fix L1810_Bug00000678 by yangli 20120713 begin
    /**
     * Return true if an ICC card is ready
     */
    boolean isIccCardReady();
    //Fix L1810_Bug00000678 by yangli 20120713 end

    //Fix L1860_20140606-15648 by xianglijun
    boolean isIccCardReadyMSMS(int linkId);

    //<UH, 2011-02-21, lifangdong, Add ContactsAttribute./
    IccCardInfo getIccCardRecordInfo(int infoId);

    //Add for Multi_Mode by yangli 2012-05-26 begin
    /**
     * Return supposed IccCardRecordInfo relative to linkId.
     */
    IccCardInfo getIccCardRecordInfoMSMS(int infoId, int linkId);
    //Add for Multi_Mode by yangli 2012-05-26 end

    /**
     * Return if the current radio is LTE on CDMA. This
     * is a tri-state return value as for a period of time
     * the mode may be unknown.
     *
     * @return {@link Phone#LTE_ON_CDMA_UNKNOWN}, {@link Phone#LTE_ON_CDMA_FALSE}
     * or {@link PHone#LTE_ON_CDMA_TRUE}
     */
    int getLteOnCdmaMode();
    /* Add L1810_speed dial new functions 2012-03-06 gaofeng begin */
    String getSpeedDialNumber(int key);
    String getIpNumber();
    //Do-not-delete-multisim-flag begin
    //add ip dial for double cards{card1 and card2}
    String getIpNumberMSMS(int linkId);
    //Do-not-delete-multisim-flag end
    /* Add L1810_speed dial new functions 2012-03-06 gaofeng end */

    /**
     * Returns the all observed cell information of the device.
     */
    List<CellInfo> getAllCellInfo();

    /*Fix 1810 Bug00000709,liruifang 20120713*/
    // add [by chenshu 2011-07-15] for fix 1809[Bug00002779]
    void playTones(char c);
    // add [by chenshu 2011-07-15] end
    /*Fix end*/
    void playTonesMSMS(char c, int linkId);/*L1813_Bug00001201, wangsheng, 2013-07-29*/

    /* FIX L1809OG_Bug00000405 2011-03-17 zoufeng begin */
    void shutDownRadio();
    /* FIX L1809OG_Bug00000405 2011-03-17 zoufeng end */

    int getSIMTypeMSMS(int linkID);

    int supplyPinMSMS(String pin, int linkID);
    int supplyPukMSMS(String puk, String pin, int linkID);
    int[] supplyPinReportResultMSMS(String pin, int linkId);
    int[] supplyPukReportResultMSMS(String puk, String pin, int linkId);

    void shutDownRadioMSMS(int linkID);
    boolean endCallMSMS(int linkId);
    boolean isRadioOnMSMS(int linkID);
    void registerForPSStateChange(IBinder binder);

    void unregisterForPSStateChange();
    void registerForNotReleasePsRes(IBinder binder);
    void unregisterForNotReleasePsRes();

    // LC extra START:[Enh00000024]
    /**
     * Return attempts remaining for linkId  {PIN1, PIN2, PUK1, PUK2}
     */
    int[] getPinRemainAttempt(int linkId);
    // LC extra END:[Enh00000024]

    boolean getIccFdnEnabled(int linkId);// LC extra:[Bug00001377]
    String[] getIpNumberList(); // LC extra:[Req00000297]

    /**
     * Sets minimum time in milli-seconds between onCellInfoChanged
     */
    void setCellInfoListRate(int rateInMillis);

    /**
     * Put a call on hold.
     */
    void toggleHold();

    /**
     * Merge foreground and background calls.
     */
    void merge();

    /**
     * Swap foreground and background calls.
     */
    void swap();

    /**
     * Mute the phone.
     */
    void mute(boolean mute);

    /**
     * Start playing DTMF tone for the specified digit.
     *
     * @param digit the digit that corresponds with the desired tone.
     * @param timedShortcode whether the specified digit should be played as a timed short code.
     */
    void playDtmfTone(char digit, boolean timedShortCode);

    /**
     * Stop playing DTMF tones.
     */
    void stopDtmfTone();

    /**
     * Register a callback.
     */
    void addListener(ITelephonyListener listener);

    /**
     * Unregister a callback.
     */
    void removeListener(ITelephonyListener listener);

    /*add by yuanhaobo for L1860_Bug00000089 @ 2014-03-04 begin*/
    void setDefaultCSPhone(int linkId);
    /*add by yuanhaobo for L1860_Bug00000089 @ 2014-03-04 end*/

    /*add by zhouguocheng for getting real-time IMS registry state begin*/
    boolean isImsInService();
    /*add by zhouguocheng for getting real-time IMS registry state end*/

/* FIX for support AHN 2016-05-10  begin */
    boolean setAhnConfigMSMS(int linkId, int levelId, int groupId, int nodeType, int syncType, in int[] serialId);
    boolean setAhnFrequencyMSMS(int linkId, int frequencyType, in float[] frequency);
    boolean setAhnPowerMSMS(int linkId, int onOff);
/* FIX for support AHN 2016-05-10  end */
/* FIX for support AHN 2016-06-06  begin */
    boolean mergeAhnNetworkMSMS(int linkId, int onOff);
/* FIX for support AHN 2016-06-06  end */
/* FIX for support AHN 2016-06-06  begin */
    boolean switchNetworkModeMSMS(int linkId, int mode);
/* FIX for support AHN 2016-06-06  end */
/* add start by MarkSun 2016-09-23 */
    boolean getAhnParameterMSMS(int linkId, int type, out int[] outValue);
    boolean setAhnParameterMSMS(int linkId, int type, in int[] inValue);
    int     getAhnRFPowerMSMS(int linkId);
    boolean setAhnRFPowerMSMS(int linkId, int RFPower);
    int getCallerStateMSMS(int index);
    int setCallerStateMSMS(int index, int state);
    boolean isRadioAvailableMSMS(int linkId);
    int getAhnMergedStateMSMS(int linkId);
/* add end by MarkSun 2016-09-23 */
}

