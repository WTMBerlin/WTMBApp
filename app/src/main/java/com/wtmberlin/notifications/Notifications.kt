package com.wtmberlin.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.wtmberlin.R
import com.wtmberlin.data.WtmEvent

class Notifications(private val context: Context) {
    companion object {
        private const val UPCOMING_EVENTS_CHANNEL_ID = "UPCOMING_EVENTS"
    }

    fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                UPCOMING_EVENTS_CHANNEL_ID,
                context.getString(R.string.channel_name_upcoming_events),
                IMPORTANCE_DEFAULT
            )

            with(context.getSystemService<NotificationManager>()!!) {
                createNotificationChannel(channel)
            }
        }
    }

    fun showNewUpcomingEventNotification(event: WtmEvent) {
        val notification = NotificationCompat.Builder(context, UPCOMING_EVENTS_CHANNEL_ID)
            .setSmallIcon(R.drawable.wtm)
            .setLargeIcon(context.getDrawable(R.drawable.wtm)!!.toBitmap())
            .setContentTitle(context.getString(R.string.upcoming_event_title))
            .setContentText(event.name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createEventDetailsDeepLink(event.id))
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(event.id.hashCode(), notification)
        }
    }

    private fun createEventDetailsDeepLink(eventId: String): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.event_details_screen)
            .setArguments(bundleOf("eventId" to eventId))
            .createPendingIntent()
    }
}