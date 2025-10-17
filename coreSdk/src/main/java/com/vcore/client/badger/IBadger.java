package com.vcore.client.badger;

import android.content.Intent;

import com.vcore.remote.BadgerInfo;

/**
 * @author Lody
 */
public interface IBadger {

    String getAction();

    BadgerInfo handleBadger(Intent intent);

}
