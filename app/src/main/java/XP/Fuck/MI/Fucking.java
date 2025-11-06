package XP.Fuck.MI;

import android.content.Context;
import android.view.View;

import org.luckypray.dexkit.DexKitBridge;
import org.luckypray.dexkit.query.FindMethod;
import org.luckypray.dexkit.query.enums.MatchType;
import org.luckypray.dexkit.query.matchers.MethodMatcher;
import org.luckypray.dexkit.query.matchers.MethodsMatcher;
import org.luckypray.dexkit.result.MethodData;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Fucking implements IXposedHookLoadPackage {
    static {
        System.loadLibrary("dexkit");
    }
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        //＠下载管理：安装就安装，不要瞎启动！
        if (lpparam.packageName.equals("com.android.providers.downloads.ui")) {
            DexKitBridge bridge = DexKitBridge.create(lpparam.appInfo.sourceDir);
            MethodData methodData = bridge.findMethod(FindMethod.create().matcher(MethodMatcher.create().modifiers(Modifier.PUBLIC | Modifier.STATIC).paramTypes("android.content.Context", "java.lang.String").returnType("int").invokeMethods(MethodsMatcher.create().add(MethodMatcher.create().name("getPackageInfo")).matchType(MatchType.Contains)))).single();
            Method method = methodData.getMethodInstance(lpparam.classLoader);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(-1);
                }
            });
        }
        //＠自动连招：解除数值大小限制
        if (lpparam.packageName.equals("com.xiaomi.macro")) {
            XposedHelpers.findAndHookMethod("com.xiaomi.macro.main.view.MainMacro", lpparam.classLoader, "handlePlayParameterConfirm", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(null);
                    Object etPlayParameter = XposedHelpers.getObjectField(param.thisObject, "mEtPlayParameter");
                    String inputValue = XposedHelpers.callMethod(etPlayParameter, "getText").toString();
                    int selectedPosition = XposedHelpers.getIntField(param.thisObject, "mSelectedPosition");
                    List<?> macroList = (List<?>) XposedHelpers.getObjectField(param.thisObject, "mMacroList");
                    Object macro = macroList.get(selectedPosition);
                    int playParameterFlag = XposedHelpers.getIntField(param.thisObject, "mPlayParameterFlag");
                    switch (playParameterFlag) {
                        case 1: // 播放速度
                            XposedHelpers.callMethod(macro, "setPlaySpeed", Double.parseDouble(inputValue));
                            break;
                        case 2: // 播放次数
                            XposedHelpers.callMethod(macro, "setPlayTimes", Integer.parseInt(inputValue));
                            break;
                        case 3: // 播放延迟
                            XposedHelpers.callMethod(macro, "setPlayDelay", Integer.parseInt(inputValue));
                            break;
                    }
                    Object mainMacroPresenter = XposedHelpers.getObjectField(param.thisObject, "mMainMacroPresenter");
                    XposedHelpers.callMethod(mainMacroPresenter, "updateMacro", macro);
                    Object macroWindowManager = XposedHelpers.getObjectField(param.thisObject, "mMacroWindowManager");
                    Object playParameterView = XposedHelpers.getObjectField(macroWindowManager, "mPlayParameterView");
                    XposedHelpers.callMethod(macroWindowManager, "removeView", playParameterView);
                }
            });
            XposedHelpers.findAndHookMethod("com.xiaomi.macro.widget.ClearEditText", lpparam.classLoader, "onTextChanged", CharSequence.class, int.class, int.class, int.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return null;
                }
            });
            //移除按键一秒CD
            XposedHelpers.findAndHookMethod("com.xiaomi.macro.using.view.UsingMacro", lpparam.classLoader, "lambda$initListener$0$UsingMacro", XposedHelpers.findClass("com.xiaomi.macro.widget.DrawMacro", lpparam.classLoader), View.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedHelpers.setLongField(param.thisObject, "mStopTime", 0L);
                }
            });
        }
        //＠手机／平板管家：屏蔽导致 Hunter 出现 SafetyDetectClient 异常项的内鬼服务
        if (lpparam.packageName.equals("com.miui.securitycenter")) {
            DexKitBridge bridge = DexKitBridge.create(lpparam.appInfo.sourceDir);
            MethodData methodData = bridge.findMethod(FindMethod.create().matcher(MethodMatcher.create().modifiers(Modifier.PUBLIC).name("<init>").paramTypes("int", null, "com.xiaomi.security.xsof.IMiSafetyDetectCallback"))).single();
            Constructor<?> method = methodData.getConstructorInstance(lpparam.classLoader);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.args[0] = 11;
                }
            });
        }
        //＠剪贴板与常用语：拒绝区别对待输入法
        if (lpparam.packageName.equals("com.miui.phrase")) {
            XposedHelpers.findAndHookMethod("com.miui.inputmethod.InputMethodBottomManager", lpparam.classLoader, "getSupportIme", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    Object sBottomViewHelper = XposedHelpers.getStaticObjectField(param.thisObject.getClass(), "sBottomViewHelper");
                    Object mImm = XposedHelpers.getObjectField(sBottomViewHelper, "mImm");
                    return XposedHelpers.callMethod(mImm, "getEnabledInputMethodList");
                }
            });
        }
        //＠系统桌面：谁教你一碰到图标就预启动应用的？
        if (lpparam.packageName.equals("com.miui.home")) {
            XposedHelpers.findAndHookMethod("com.miui.home.launcher.util.PreLaunchAppUtil", lpparam.classLoader, "preLaunchProcess", XposedHelpers.findClass("com.miui.home.launcher.ShortcutInfo", lpparam.classLoader), new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedHelpers.setStaticBooleanField(param.thisObject.getClass(), "isEnableTouchDown", false);
                }
            });
        }
        //＠相册：禁止存储缩略图（/sdcard/Android/data/com.miui.gallery/files/gallery_disk_cache）占用大量空间
        if (lpparam.packageName.equals("com.miui.gallery")) {
            XposedHelpers.findAndHookMethod("com.miui.gallery.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory$1", lpparam.classLoader, "getCacheDirectory", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(null);
                }
            });
        }
        //＠媒体选择工具：禁用.globalTrash
        if (lpparam.packageName.equals("com.android.providers.media.module")) {
            XposedHelpers.findAndHookMethod("com.android.providers.media.util.GalleryMediaStore", lpparam.classLoader, "initGlobalTrash", Context.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return null;
                }
            });
            XposedHelpers.findAndHookMethod("com.android.providers.media.MediaReceiverInjector", lpparam.classLoader, "initDragImgsDir", File.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return null;
                }
            });
        }
        //＠系统界面：通知面板左滑不消除卡片，而是切换到控制中心
        if (lpparam.packageName.equals("com.android.systemui")) {
            //有这个想法但不会写qAq
        }
    }
}
