package com.topjohnwu.magisk.asyncs;

import android.content.Context;

import com.topjohnwu.magisk.MagiskManager;
import com.topjohnwu.magisk.container.Module;
import com.topjohnwu.magisk.container.ValueSortedMap;
import com.topjohnwu.magisk.utils.Logger;
import com.topjohnwu.magisk.utils.Utils;

public class LoadModules extends ParallelTask<Void, Void, Void> {

    public LoadModules(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MagiskManager mm = getMagiskManager();
        if (mm == null) return null;
        Logger.dev("LoadModules: Loading modules");

        mm.moduleMap = new ValueSortedMap<>();

        for (String path : Utils.getModList(getShell(), MagiskManager.MAGISK_PATH)) {
            Logger.dev("LoadModules: Adding modules from " + path);
            Module module = new Module(getShell(), path);
            mm.moduleMap.put(module.getId(), module);
        }

        Logger.dev("LoadModules: Data load done");
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        MagiskManager mm = getMagiskManager();
        if (mm == null) return;
        mm.moduleLoadDone.publish();
        super.onPostExecute(v);
    }
}
