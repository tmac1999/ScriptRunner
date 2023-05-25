i=0;
while [ $i -le 362 ]
do
input swipe 500 1500 500 800
sleep 2
input tap 100 1500
sleep 2
input swipe 0 1500 500 1500
sleep 1
let i++

done