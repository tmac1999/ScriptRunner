@REM ./gradlew assembleDebug
@REM usage: in D:\Users\zhengpeng2\AndroidStudioProjects\MaxeSetting   type:
@REM gradlew assembleDebug & install_maxe_app.bat

adb root
adb remount
@REM adb push D:\Users\zhengpeng2\AndroidStudioProjects\MaxeSetting\app\build\outputs\apk\debug\app-debug.apk  /system_ext/priv-app/MaxeApp/

adb push services.jar  /system/framework/

adb reboot










@REM adb root && adb pull /data/vendor/bug2go/