#!/usr/bin/env bash
# گرفتن اسکرین‌شات از اپ روی شبیه‌ساز (داخل GitHub Actions اجرا می‌شود)
#   ۱. لیست مخاطبین از سرور
#   ۲. دیالوگ افزودن مخاطب
#   ۳. حالت آفلاین: سرور خاموش، لیست از Room
set -euo pipefail

APK="$1"
OUT=screenshots
PKG=com.example.contacts
mkdir -p "$OUT"

shot() {
  adb exec-out screencap -p > "$OUT/$1.png"
  echo "📸 $1.png"
}

# مرکز یک المان از روی contentDescription آن (با uiautomator)
tap_by_description() {
  adb shell uiautomator dump /sdcard/ui.xml > /dev/null
  adb pull /sdcard/ui.xml /tmp/ui.xml > /dev/null
  local bounds
  bounds=$(grep -o "content-desc=\"$1\"[^>]*bounds=\"\[[0-9]*,[0-9]*\]\[[0-9]*,[0-9]*\]\"" /tmp/ui.xml \
    | grep -o '\[[0-9]*,[0-9]*\]\[[0-9]*,[0-9]*\]' | head -1)
  if [ -z "$bounds" ]; then
    echo "element '$1' not found"; return 1
  fi
  # "[x1,y1][x2,y2]" ← "x1 y1 x2 y2"
  read -r x1 y1 x2 y2 <<< "$(echo "$bounds" | sed 's/\]\[/ /; s/[][]//g; s/,/ /g')"
  adb shell input tap $(( (x1 + x2) / 2 )) $(( (y1 + y2) / 2 ))
}

adb install -r "$APK"

echo "== 1) list (online)"
adb shell am start -W -n "$PKG/.MainActivity"
sleep 10
shot 1-list

echo "== 2) add dialog"
tap_by_description "افزودن مخاطب"
sleep 3
shot 2-add-dialog
adb shell input keyevent KEYCODE_BACK
sleep 1

echo "== 3) offline"
# خاموش کردن سرور PHP («[p]» تا pkill خودش را نکشد)
sudo fuser -k 80/tcp || sudo pkill -f "[p]hp -S" || true
sleep 2
if curl -s -m 3 http://127.0.0.1/contacts-api/list.php > /dev/null; then
  echo "server is still up!"; exit 1
fi
echo "server is down ✔"
adb shell am force-stop "$PKG"
adb shell am start -W -n "$PKG/.MainActivity"
# پیام خطا (Snackbar) حدود ۴ ثانیه می‌ماند؛ زود عکس بگیر
sleep 2
shot 3-offline

ls -la "$OUT"
