@REM ./gradlew assembleDebug
@REM usage: in D:\Users\zhengpeng2\AndroidStudioProjects\MaxeSetting   type:
@REM gradlew assembleDebug & install_app.bat

adb root
adb remount
@REM adb push D:\Users\zhengpeng2\AndroidStudioProjects\MaxeSetting\app\build\outputs\apk\debug\app-debug.apk  /system_ext/priv-app/MaxeApp/
adb shell "rm /system_ext/priv-app/ScriptRunner/*.apk"
adb push app/build/outputs/apk/debug/app-debug.apk  /system_ext/priv-app/ScriptRunner/
adb reboot











@REM adb root && adb pull /data/vendor/bug2go/