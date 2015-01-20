package com.google.android.sample.qrcode;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button notificationButton = (Button) findViewById(R.id.qrbutton);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQRCode();
            }
        });
    }

    private void showQRCode(){

        int wearNotificationId = 1;
        // Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivity.class);
        viewIntent.putExtra("EXTRA_EVENT_ID", "eventId");
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        // Generate fancy title using only bold and gray colour
        // This is optional, you can use a normal string in it’s place
        SpannableStringBuilder title = new SpannableStringBuilder();

        title.append("GA0888\n");
        appendStyled(title, "LHR", new StyleSpan(Typeface.BOLD));
        appendStyled(title, " > ", new ForegroundColorSpan(Color.GRAY));
        appendStyled(title, "HKG", new StyleSpan(Typeface.BOLD));

        // Generate fancy text
        SpannableStringBuilder text = new SpannableStringBuilder();
        appendStyled(text, "Departs 12:50", "");
        text.append("\n");
        appendStyled(text, "Terminal 3 Gate 10",
                new ForegroundColorSpan(Color.GRAY));

        NotificationCompat.Builder wearablePageOneBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_airline_logo)
                        // You can brand here using setLargeIcon
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.ic_launcher))
                        .setColor(Color.parseColor("#E65100"))
                        .setContentTitle(title)
                        .setContentText(text)
                        .setContentIntent(viewPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Notification wearablePageTwo = new NotificationCompat.WearableExtender()
                // set QR code image
                .setBackground(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.lhr_to_hkg_qr))
                // show only the QR code and no white box on top of it
                .setHintShowBackgroundOnly(true)
                // disable clipping of background QR code
                .setHintAvoidBackgroundClipping(true)
                // set time out to 60,000ms or 60 seconds
                .setHintScreenTimeout(60000)
                .extend(new NotificationCompat.Builder(this)
                ).build();

        Notification wearableNotification = wearablePageOneBuilder
                .extend(new NotificationCompat.WearableExtender()
                                .addPage(wearablePageTwo)
                                .setHintHideIcon(false)
                                // Use a blurred QR code as a background to prompt the user
                                // to scroll to the QR code
                                .setBackground(
                                        BitmapFactory.decodeResource(
                                                this.getResources(), R.drawable.destbackground))
                ).build();

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(wearNotificationId, wearableNotification);
    }

    private void appendStyled(SpannableStringBuilder builder, String str, Object... spans) {
        builder.append(str);
        for (Object span : spans) {
            builder.setSpan(span, builder.length() - str.length(), builder.length(), 0);
        }
    }

}
