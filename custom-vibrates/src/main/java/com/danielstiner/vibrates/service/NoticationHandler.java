package com.danielstiner.vibrates.service;

import java.util.LinkedList;
import java.util.ListIterator;

import roboguice.inject.ContextScopedProvider;
import roboguice.inject.ContextSingleton;
import roboguice.util.Ln;
import android.app.Application;
import android.content.Context;
import android.os.PowerManager.WakeLock;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.model.IDataModel;
import com.danielstiner.vibrates.notify.INotificationConstraints;
import com.danielstiner.vibrates.notify.VibrateNotify;
import com.danielstiner.vibrates.notify.VibrateNotify.Kind;
import com.danielstiner.vibrates.settings.IUserSettings;
import com.google.inject.Inject;
import com.google.inject.Provider;

@ContextSingleton
public class NoticationHandler implements INotificationHandler {

	LinkedList<VibrateNotify> mQueue;

	@Inject
	Provider<IUserSettings> mUser_settings_provider;

	@Inject
	INotificationConstraints mNotify_constraints;

	IDataModel mManager;

	// @Inject
	// Provider<IPatternPlayer> mPattern_player_provider;

	@Inject
	private Provider<Application> mContextProvider;

	@Inject
	private IPatternPlayer mPatternPlayer;

	private VibrateNotify mCurrent;

	private Runnable mOnIdle;

	private WakeLock mWakeLock;

	protected boolean mVibrating;

	private boolean mQueueWasEmpty;

	@Inject
	public NoticationHandler(ContextScopedProvider<IDataModel> modelProvider,
			Provider<Application> contextProvider) {
		// Get a reference to the shared data manager
		mManager = modelProvider.get(contextProvider.get());

		mQueue = new LinkedList<VibrateNotify>();

		mQueueWasEmpty = true;
	}

	@Override
	public void handle(VibrateNotify notification) {

		// Put in queue to handler later
		queue(notification);

		// Start processing queue
		processQueue();

	}

	private void queue(VibrateNotify notification) {

		// Check if this is a cancellation
		if (notification.kind() == VibrateNotify.Kind.Cancel) {
			// Cancel both exact copies in the queue and the current if it is a
			// copy

			if (mCurrent != null && mCurrent.sameIdentifier(notification)) {
				mPatternPlayer.cancel();
				processQueue();
			}

			ListIterator<VibrateNotify> it = mQueue.listIterator();

			while (it.hasNext()) {
				if (notification.sameIdentifier(it.next())) {
					// Remove similar identifier notify
					it.remove();
				}
			}

		} else {

			// Check if an exact copy already exists in the queue
			for (VibrateNotify notify : mQueue) {
				if (notify.equals(notification)) {
					// Don't bother adding
					return;
				}
			}

			mQueue.add(notification);
		}
	}

	private void processQueue() {
		// Start something from the queue if possible
		if (mCurrent == null) {
			VibrateNotify n = mQueue.poll();

			if (n == null) {
				// Done processing

				// Queue was just emptied?
				if (mQueueWasEmpty == false) {
					mOnIdle.run();
					mWakeLock.release();
				}
				mQueueWasEmpty = true;

			} else {
				// Do some processing

				// Get a wake lock until queue is empty
				if (mQueueWasEmpty == true)
					mWakeLock.acquire();

				mCurrent = n;

				mQueueWasEmpty = false;

				start();

			}
		}

	}

	private void start() {
		VibrateNotify notification = mCurrent;
		Context context = mContextProvider.get();

		// Grab the notification's identifier and type
		String identifier = notification.identifier_id();
		String type = notification.identifier_type();
		Kind kind = notification.kind();

		// Try and find the associated group
		Entity group = mManager.getEntity(identifier, Entity.KIND_GROUP);

		// Try and find the associated entity
		Entity e = mManager.getEntity(identifier, Entity.KIND_CONTACT);

		// Do vibrations
		if (e == null && group == null) {
			Ln.d("VibratrService Notify: Got a null notification, playing default");

			IUserSettings test = mUser_settings_provider.get();

			// Play a default
			if (mNotify_constraints.vibrate_default(type)) {
				vibrate(test.defaultPattern(), kind);
			}
		} else if (mNotify_constraints.vibrate(e, type)) {
			vibrate(mManager.getPattern(e), kind);
		} else if (mNotify_constraints.vibrate_group(group, type)) {
			vibrate(mManager.getPattern(group), kind);
		}

		// Play call sounds
		if (e == null && group == null) {
			Ln.d("VibratrService Notify: Got a null notification, playing default");

			// Play a default
			if (mNotify_constraints.sound_default(type)) {
				// vibrator.vibrate(user_settings_provider.get().defaultPattern(),
				// -1);
				sound(mUser_settings_provider.get().defaultPattern(), type);
			}
		} else if (mNotify_constraints.sound(e, type)) {
			sound(mManager.getPattern(e), type);
		} else if (mNotify_constraints.sound_group(group, type)) {
			sound(mManager.getPattern(group), type);
		}

		// Show toast notification
		if (e == null && group == null) {

			// Play a default
			if (mNotify_constraints.toast_default(type)) {
				toast(mUser_settings_provider.get().defaultPattern(), type,
						null, context);
			}
		} else if (mNotify_constraints.toast(e, type)) {
			toast(mManager.getPattern(e), type, e, context);
		} else if (mNotify_constraints.toast_group(group, type)) {
			toast(mManager.getPattern(group), type, group, context);
		}

	}

	private void sound(Pattern pattern, String type) {
		// TODO Auto-generated method stub

	}

	private void vibrate(Pattern p, VibrateNotify.Kind kind) {
		mPatternPlayer.play(p, kind, new Runnable() {

			@Override
			public void run() {

				NoticationHandler.this.mVibrating = false;
				processQueue();
			}
		});
	}

	private void toast(Pattern pattern, String type, Entity e, Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOnIdle(Runnable onIdle) {
		mOnIdle = onIdle;
	}

	@Override
	public void setWakeLock(WakeLock wakeLock) {
		mWakeLock = wakeLock;
	}

	@Override
	public void cancel() {
		mQueue.clear();
		mPatternPlayer.cancel();
	}
}
