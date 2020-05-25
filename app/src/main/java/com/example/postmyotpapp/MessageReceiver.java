package com.example.postmyotpapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.IOException;

public class MessageReceiver extends BroadcastReceiver {

    private static MessageListener mListener;

    @Override
    public void onReceive(Context context, Intent intent){
        try {
            Bundle data = intent.getExtras();
            Object[] pdus = (Object[]) data.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                //String message = "Sender: "+smsMessage.getDisplayOriginatingAddress()
                //       +"SMS body: "+smsMessage.getMessageBody();
                String message = smsMessage.getMessageBody();
                mListener.messageReceived(message);
            }
        }catch (Exception ex){
                Log.e("TAG_Message_Receiver", ex.getMessage(), ex);
            }
    }

    public static void bindListener(MessageListener listener){
        mListener = listener;
    }
}
