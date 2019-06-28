#!/bin/sh

if [ -z "ANDROID_NDK" -o -z "$ANDROID_NDK" ]; then
    echo "You must define ANDROID_NDK_ROOT before starting build."
    exit 1
fi
# should use ndk r10e
NDK_BUILD_CMD=$ANDROID_NDK_ROOT/ndk-build
buildPlatinumCore() {
    echo "==>build platinum core..."
    scons target=arm-android-linux build_config=Release --file=$PWD/Build/Boot.scons
    echo "==>build platinum core end"
}

echo "start build"
buildPlatinumCore
echo "end build"
