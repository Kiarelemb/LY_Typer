# 揽月跟打器 QQ群：[跟打｜键指启航・萃月浸享](https://qm.qq.com/cgi-bin/qm/qr?k=S12N8cwh3IxEu6dLCB7egmnUOTh476mm&jump_from=webapi&authKey=N6bS9Xu+PsBljT6DOTBnFqXO6E/4DRuKKVaJMHYbFlBVGKJ2oAlnV8DI83Lug3sh)

> 中文打字圈开源跟打器。本跟打器基于作者 **[QR](https://github.com/Kiarelemb)**
> 自研的开源界面框架 [QR_Swing](https://github.com/Kiarelemb/QR_Swing)
> 和开源工具包 [QR_Method](https://github.com/Kiarelemb/QR_Method)，使用纯 **`Java`** 语言开发而成，以支持 `Windows`
> 以外的其他系统。

## 小白快速开始

> 阅读指引：在这一部分，您将需要进行两个操作步骤：①环境准备 ②双击运行。  
> 每个步骤下，您将面临两个选择。  
> 
> 请注意，面对任何读不懂的内容，请转向我们的QQ群：[283674085](https://qm.qq.com/cgi-bin/qm/qr?k=S12N8cwh3IxEu6dLCB7egmnUOTh476mm&jump_from=webapi&authKey=N6bS9Xu+PsBljT6DOTBnFqXO6E/4DRuKKVaJMHYbFlBVGKJ2oAlnV8DI83Lug3sh)。

### `Step 1` ：运行环境准备

软件使用 `Java 17` 编译而成，但对于运行而言，只需要 `Java 11` 。不过，我们仍然推荐使用 `Java 17`，甚至更新的 JDK 版本。

#### - 下载 JDK 并自行配置运行环境

依据您的系统，选择点击下方的名称，以安装 [Java 17 GA](https://jdk.java.net/archive/) ：  
> [Windows x64](https://download.java.net/java/GA/jdk17/0d483333a00540d886896bac774ff48b/35/GPL/openjdk-17_windows-x64_bin.zip)  
> [Mac OS x64](https://download.java.net/java/GA/jdk17/0d483333a00540d886896bac774ff48b/35/GPL/openjdk-17_macos-x64_bin.tar.gz)  
> [Linux x64](https://download.java.net/java/GA/jdk17/0d483333a00540d886896bac774ff48b/35/GPL/openjdk-17_linux-x64_bin.tar.gz)

对于 `Windows 10+` 系统的环境配置，我们强烈推荐您参考 [这个网站](https://www.runoob.com/w3cnote/windows10-java-setup.html)。

#### - 群内下载 `exe` 安装包免去环境配置

对于 `Windows 7` 以上的系统，您可以在QQ群 [283674085](https://qm.qq.com/cgi-bin/qm/qr?k=S12N8cwh3IxEu6dLCB7egmnUOTh476mm&jump_from=webapi&authKey=N6bS9Xu+PsBljT6DOTBnFqXO6E/4DRuKKVaJMHYbFlBVGKJ2oAlnV8DI83Lug3sh) 的群文件 `运行环境` 文件夹中下载
```
jdk-17_windows-x64_bin.exe
```
文件，双击该安装包，并以默认设置完成安装即可运行揽月。

> Tip: 群内更有 `开箱即用` 而不需要考虑环境问题的打包版本。

### `Step 2` ：开始运行

- #### 运行 `Jar` 包

除了在本页面克隆下载之外，您还可以点击这里 [单独下载 Jar 包](https://github.com/Kiarelemb/LY_Typer/raw/master/LY_Tyepr.jar)。至今，我们的揽月跟打器**初次运行**并不需要任何额外的配置文件。当然，那些配置文件会在软件启用后自动生成。

- #### 运行打包好的 `exe` 文件

群文件 `小启＆揽月` 文件夹中，您还可以找到打包好的 `exe` 文件。其分为了 `老用户` 和 `新用户` 两个版本， `新用户` 亦即 `开箱即用` 版本，无需配置环境即可运行。
## 开发环境

### `Step 1` ：获取揽月代码

也许在群里下载的 `exe` 版本并非最新。  
不过，本页面所提供的 `Jar` 包和开发环境所运行的代码一定是当前揽月的最新版本。  
因为 QR 每次写完代码都会把代码上传到 GitHub，而 `Jar` 包和代码则是从本项目上下载的。
> 既然您选择**开发版本**，那么我们将默认您具备足够的 `Git` 使用技巧和 `IntelliJ IDEA` 等开发工具的使用能力。

- #### 使用开发工具 `IntelliJ IDEA`
在 `IntelliJ IDEA` 中新建项目，选择 `来自版本控制的项目...`，在弹出窗口的 `URL` 中输入：

```
https://github.com/Kiarelemb/LY_Typer.git
```
再单击 `克隆` 等待 `IntelliJ IDEA` 下载完毕即可。

- #### 使用 `Git Bash`

当然，您也完全可以在您想要存放揽月跟打器代码的**空**文件夹中，使用 `Git Bash` ，并输入如下代码，回车以获取揽月最新代码。
```
git clone https://github.com/Kiarelemb/LY_Typer.git
```

### `Step 2` ：项目配置

- #### 使用 `Visual Studio Code`
对于该开发工具，我们推荐您在扩展中安装 `Extension Pack for Java` 以便其读取软件配置，方便您后续的操作。  

使用 `Visual Studio Code` 打开从 `Git` 中克隆下来的项目文件夹。若没有看到报错，那就可以直接运行了。

- #### 使用 `IntelliJ IDEA`
若使用 `IntelliJ IDEA`，您需要将项目所使用的 `jar` 包改为 `lib` 文件夹中所依赖的 `jar` 包。
  - 对于英文版，其配置方法如下：
1. 删除现有的依赖：`File` -> `Project Structure` -> `Libraries` -> 选中所有的依赖 -> `-` -> `OK`。
2. 打开 `File` -> `Project Structure` -> `Libraries` -> `+` -> `Java` -> `LY_Typer` -> 选择 `lib` 文件夹 -> `OK`。
  - 对于中文版，其配置方法如下：
1. 删除现有的依赖：菜单 `文件` -> `项目结构` -> `库` -> 选中所有的依赖 -> `-` -> `确定`。
2. 打开菜单 `文件` -> `项目结构` -> `库` -> `+` -> `Java` -> `LY_Typer` -> 选择 `lib` 文件夹 -> `确定`。


### `Step 3` ：在开发软件中开始运行揽月
- 对于 `Visual Studio Code`，在 `src` 文件夹中，双击包 `ly.qr.kiarelemb`，找到 `Enter`，双击后，可在打开文件的右上角看到运行按扭，单击后运行。

- 对于 `IntelliJ IDEA`，在项目的依赖库配置好后，用同样的方法，在 `src` 文件夹中，找到 `Enter`，双击后，在文本编辑器中右键找到 `Run`，单击运行。