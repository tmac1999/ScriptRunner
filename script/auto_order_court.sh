#--------开启录屏--------
#下滑出 quick setting
input swipe 700 100 700 500
sleep 1
#点击quick setting里录屏按钮
input tap 665 2296
sleep 1
#点击开始
input tap 880 1573
sleep 8
#--------开启录屏----end----

#选择西区court
#input text "zhengpeng"
#点击逸诚体育公园
input tap 306 1060
sleep 1
#点击立即预约
input tap 550 1700
sleep 1
##点击第二个日期
#input tap 564 370
##再点击第一个日期 进行刷新
#sleep 0.5
#input tap 216 360
#sleep 1
#--------场地 选择其中之一--------
#点击8点 2号场
input tap 430 630
#点击8点 1号场
#input tap 250 630
#点击11~12点 1号场
#input tap 250 900
#点击13~14点 1号场
#input tap 227 1072
#点击13~14点 2号场
#input tap 430 1072
#--------场地 选择其中之一--------

#点击提交订单
sleep 0.2
input tap 630 2170
sleep 1
#预约人加号
input tap 926 1007
sleep 0.5
input text "zhengp"
#小键盘回车 输入名字
input tap 990 2420
#下一行 输入电话
input tap 400 1320
sleep 0.5
input text "18910864698"
input tap 400 1440
input text "shuangqinglu"
#小键盘回车 输入地址
input tap 990 2420
#下一行 输入人数
input tap 400 1560
sleep 1
input text 2
# 点击弹窗外区域 退出输入法键盘
input tap 466 750
sleep 1
# 点击确定
input tap 760 1726
sleep 1
# 点击确认订单
input tap 577 2500



#--------结束录屏--------
sleep 3
input tap 1008 320
#--------结束录屏--------