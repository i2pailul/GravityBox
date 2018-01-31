/*
 * Copyright (C) 2018 Peter Gregus for GravityBox Project (C3C076@xda)
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
package com.ceco.oreo.gravitybox.quicksettings;

import com.ceco.oreo.gravitybox.GravityBox;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;
import de.robv.android.xposed.XposedHelpers;

class OOSThemeColorUtils {
    private static final String TAG = "GB:OOSThemeColorUtils";
    private static final String CLASS_THEME_COLOR_UTILS = "com.android.systemui.util.ThemeColorUtils";

    public static final int QS_PRIMARY_TEXT = 0x1;
    public static final int QS_SECONDARY_TEXT = 0x2;
    public static final int QS_ACCENT = 0x64;

    private OOSThemeColorUtils() { /* static, non-instantiable */ }

    public static int getColor(Context ctx, int spec, int defaultColorStyleAttr) {
        try {
            final Class<?> tcuClass = XposedHelpers.findClass(
                    CLASS_THEME_COLOR_UTILS, ctx.getClassLoader());
            return (int) XposedHelpers.callStaticMethod(tcuClass, "getColor", spec);
        } catch (Throwable t) {
            GravityBox.log(TAG, "Error getting OOS theme specific color", t);
            return getColorFromStyleAttr(ctx, defaultColorStyleAttr);
        }
    }

    private static int getColorFromStyleAttr(Context ctx, int attrId) {
        if (attrId == 0)
            return 0;

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = ctx.getTheme();
        theme.resolveAttribute(attrId, typedValue, true);
        TypedArray arr = ctx.obtainStyledAttributes(
                typedValue.data, new int[] { attrId });
        int color = arr.getColor(0, -1);
        arr.recycle();
        return color;
    }
}