# ShowDlnaMediaRenderer
- 基於开源的Platinum实现的安卓版本的投屏SDK,提供JNI接口实现以及Java层封装，并提供播放器接口，应用开发者需要自己实现的1)播放器以及2)调用本SDK提供的接口发送Gena事件
## 如何编译(Ubuntu 18.04, android ndk r10e)
- Step1: 安装scons
- Step2: 在platinum目录下执行./build_android.sh
- Step3: ./gradlew assembleRelease
- Step4: 在dlnadmr/build/output/aar/目录下即可得到dlnadmr-1.0.0.aar


