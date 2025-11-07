# FuckMI（炒糙米）
本模块针对小米系统的一些应用而制作。小米应用有些逻辑写得实在是太粗糙了，忍不住让人想给它加工一道。

---

### 当前功能

- [x] 阻止系统自带的下载管理在点击“安装”按钮时遇到平级或降级就直接启动应用
- [x] 移除自动连招数值大小限制和按键一秒CD
- [x] 阻止内鬼管家向其它应用汇报Root，可过Hunter的`SafetyDetectClient`
- [ ] 底栏切换输入法时显示全部启用的输入法，不要藏着掖着*（不知道为什么不生效）*
- [x] 屏蔽桌面图标一碰就预启动应用的逻辑
- [x] 禁止相册创建缩略图`/sdcard/Android/data/com.miui.gallery/files/gallery_disk_cache`占用大量空间（不影响即时显示）
- [x] 屏蔽`/sdcard/DCIM/.globalTrash`（感谢[GuhDoy/GlobalTrashKiller](https://github.com/GuhDoy/GlobalTrashKiller)）
- [ ] 通知面板左滑不消除卡片，而是切换到控制中心*（徒有想法但完全不会写）*

---

作者本人系Xposed初学者，目前写出来的东西也是一拓大边，有些Hook可能不生效，但是因为太菜而找不到原因；目前模块没有界面也没有开关功能，因为我还不会整，如果想完善欢迎[Fork](https://github.com/kxob/FuckMI/fork)并提交PR！
