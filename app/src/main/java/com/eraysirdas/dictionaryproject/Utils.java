package com.eraysirdas.dictionaryproject;

import com.google.firebase.Timestamp;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static String getTimeAgo(Timestamp timestamp) {
        long time = timestamp.getSeconds() * 1000; // Zamanı milisaniyeye çeviriyoruz
        long now = System.currentTimeMillis();

        if (time > now || time <= 0) {
            return "şimdi";
        }

        final long diff = now - time;
        if (diff < TimeUnit.MINUTES.toMillis(1)) {
            return "Şimdi";
        } else if (diff < TimeUnit.HOURS.toMillis(1)) {
            return diff / TimeUnit.MINUTES.toMillis(1) + " dakika önce";
        } else if (diff < TimeUnit.DAYS.toMillis(1)) {
            return diff / TimeUnit.HOURS.toMillis(1) + " saat önce";
        } else if (diff < TimeUnit.DAYS.toMillis(7)) {
            return diff / TimeUnit.DAYS.toMillis(1) + " gün önce";
        } else if (diff < TimeUnit.DAYS.toMillis(30)) {
            return diff / TimeUnit.DAYS.toMillis(7) + " hafta önce";
        } else if (diff < TimeUnit.DAYS.toMillis(365)) {
            return diff / TimeUnit.DAYS.toMillis(30) + " ay önce";
        } else {
            return diff / TimeUnit.DAYS.toMillis(365) + " yıl önce";
        }
    }
}
