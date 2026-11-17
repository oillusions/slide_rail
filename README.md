# Aeronautics: Pulley

一个基于 Create 应力系统与 Create: Simulated 绳索物理的滑轮方块。

## 行为

- **绳索被拉动时**：滑轮检测到绳索绷紧并被拉长，按被拉出的速度放绳，同时把卷筒的转动作为应力来源接入 Create 动力网络（产生应力）。
- **接入转动输入时**：滑轮作为应力消耗设备工作，正向转动会把绳索收回（缩短绳索），反向转动则放绳。
- 没有拉力、也没有转动输入时，滑轮保持绳索长度不变。

## 使用

1. 放置滑轮，用 Create: Simulated 的 **Rope Coupling（绳索连接器）** 把滑轮与另一个绳索固定点连接起来。连接时先点击滑轮，这样滑轮会成为绳索的所有者并负责收放。
2. 用 Create 的传动杆/齿轮把转动输入接到滑轮背面（模型自带传动轴）。
3. 绳索上挂载的物理结构或载荷拉动绳索时，滑轮放绳并产生应力；给滑轮输入转动时，绳索收回。

### 两个滑轮连同一根绳索

现在允许两个滑轮作为同一根绳索的两个端点，并且**两侧都可以通电收绳**：

1. 放置两个滑轮。
2. 手持 **Rope Coupling**，先右键第一个滑轮，再右键第二个滑轮。
3. 第一个被点击的滑轮是绳索的“收放所有者”，负责实际改变绳索长度；第二个滑轮会把自身的转动请求转发给所有者。
4. 因此给任意一侧接转动输入都能收绳/放绳。两侧同时接动力时，两个请求会叠加；方向相反则互相抵消。
5. 用剪刀剪断绳索后，可以按相反顺序重新连接，让另一个滑轮成为所有者。

限制：Simulated 的一根绳索只有两个附着点（START / END），所以不能让第三个滑轮挂在绳索中间，也不能让两个滑轮同时各自向相反方向主动收放——它们共享同一根绳索长度，最终表现为叠加/抵消。真正的动滑轮/滑轮组需要额外的绳索分段或自定义绳段物理，目前未实现。

## 平衡滑轮

新方块：`aeronautics_pulley:balance_pulley`（平衡滑轮）。它把一根绳索逻辑上分成 A、B 两段：

- A、B 是两条独立的 Simulated 绳索，同一方块上各有一个 holder。
- 平衡滑轮本身不是绞盘：方块实体直接继承 `KineticBlockEntity`，A/B 两段由同一个 BE 对称移动，不再有“第一段 owner”或绞盘卷筒逻辑。
- 正常情况下 `A + B = 总长度` 保持不变：A 放绳时 B 等量收绳，反之亦然。
- 外部转动输入会主动改变分配：正向输入让 A 伸长、B 缩短，反向输入相反；此时消耗应力。
- 被动情况：A 或 B 被外部载荷拉动时，被拉的一侧放绳、另一侧收绳，并按收放速度产生应力。
- 最大长度由绑定时的 A+B 长度决定；手持 **Rope Coupling** 右键已绑定的平衡滑轮可以增加 8 格最大长度并消耗一个连接器。

### 操作

1. **建立 A**：手持 Rope Coupling，先右键外部挂载点，再右键平衡滑轮。外部挂载点不限于绳索连接器：普通滑轮、绞盘、另一个平衡滑轮等任意 `RopeStrandHolderBehavior` 都可以作为 A/B 的另一端。
2. **建立 B**：再右键另一个外部挂载点，然后右键平衡滑轮。
3. **从平衡滑轮开始连接**：也可以先右键平衡滑轮，再右键任意外部挂载点；平衡滑轮会作为该段绳索的所有者。A 未绑定时优先建立 A，A 已绑定后建立 B。
4. **绞盘模式**：在 B 尚未连接时，潜行 + 手持 Rope Coupling 右键平衡滑轮，把 B 切换为“连接自身”的内部虚拟绳长。此时只剩 A 一条外部绳索，行为类似带最大绳长的绞盘。
5. **增加最大长度**：A/B 都绑定完成（或处于绞盘模式）后，手持 Rope Coupling 右键平衡滑轮，每次增加 8 格上限并消耗一个连接器。
6. 给平衡滑轮接上传动输入即可主动收放；不接动力时，外部拉动会发电。

限制：A、B 仍然是两条独立的 Simulated 绳索，各自只有两个端点；不支持把第三条绳索挂到同一段中间。平衡滑轮不会消除 Simulated 本身的单段绳索约束。

### 子层 / 物理约束保护

如果日志出现：

```text
[BalancePulley] Skipping invalid rope constraint ...
```

说明某段绳索的一个挂载点被保存为静态世界端点，但它的坐标落在 Sable 子层的 plotgrid 内（Sable 无法为这种坐标建立约束）。现在会跳过这条无效约束并记录一次警告，而不是让服务器崩溃。请移动该挂载点或重新连接绳索；有效绳段仍可正常工作。

## 模型

- 平衡滑轮 `balance_pulley` 使用航空学/Simulated 绞盘的模型资源：
  - `assets/aeronautics_pulley/blockstates/balance_pulley.json` 指向 `simulated:block/rope_winch/block_vertical` / `block_horizontal`
  - `assets/aeronautics_pulley/models/item/balance_pulley.json` 指向 `simulated:block/rope_winch/item`
- 但代码是独立的：`PulleyBlockEntity` 直接继承 `KineticBlockEntity`，不继承 `RopeWinchBlockEntity`；A/B 两段由同一个 BE 对称移动。
- `PulleyRenderer` 分别绘制 A、B 两段绳索。
- 旧存档中的 `balance_pulley` 仍然使用原来的 `facing` / `axis_along_first` 属性，保持方块状态兼容。

## 构建

```bash
./gradlew build
```

产物位于 `build/libs/aeronautics_pulley-1.0.jar`。
