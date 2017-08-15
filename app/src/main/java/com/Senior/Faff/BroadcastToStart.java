package com.Senior.Faff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by InFiNity on 05-May-17.
 */

public class BroadcastToStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent call_service = new Intent(context,ServerService.class); //เรียก service แจ้งเตือนเมื่อห้องอยู่ใกล้ผู้ใช้งาน
        context.startService(call_service);
    }
}
