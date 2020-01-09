# Naive-Bayes-Classification
基于Hadoop的朴素贝叶斯分类(MapReduce实现)

## 环境搭建
搭建 `Hadoop` 环境，本项目在Mac系统上搭建的 `hadoop-2.8.5`环境下完成。

## 数据集说明
数据集在项目文件`\Data\NBCorpus\`中
包含二个子目录：country和industry

country和industry下每个子目录就是一个类别
但有的子目录下文件非常少，因此要选择文件比较多的目录（至少二个）进行训练和测试。
例如
NBCorpus\Country\ALB下包含81个文件
NBCorpus\Country\ARG下包含108个文件
NBCorpus\Country\AUSTR下包含305个文件
NBCorpus\Country\BELG下包含154个文件
NBCorpus\Country\BRAZ下包含200个文件
NBCorpus\Country\CANA下包含263个文件


NBCorpus\Industry\I01001下180个文件
NBCorpus\Industry\I13000下325个文件

可以选择country或industry下的几个文件比较多的目录进行训练和测试
例如选择NBCorpus\Country\AUSTR和BCorpus\Country\CANA二个目录，那么分类的class为 `AUSTER` 和 `CANA`

目录下的文件可以按一定比例随机挑选出来作为训练样本，剩下的文件作为测试样本。

每个文件已经分好词，一行一个单词。

## 项目使用的数据集

|     | CANA | CHINA | INDIA |
|:---:|:----:|:-----:|:-----:|
| 训练集 | 177  | 170   | 227   |
| 测试集 | 87   | 86    | 99    |
| 总结  | 264  | 256   | 326   |



## 上传训练集、测试集到HDFS
搭建完Hadoop后，在项目文件`\Data\NBCorpus\Country`中选择几个分类，分成训练集、测试集。
比如该目录下的`CANA`中共有264个文件，分成两个文件夹，随机取177个文件作为训练集、87个作为测试集。两个文件夹的名字都需要是文档类型名CANA，并将两个新的目录文件都上传到HDFS上。
```shell
# 进入到存放训练集的目录(目录名是CANA)
# 该文件夹中是随机取的177个文件作为训练集
cd \Data\NBCorpus\Country\CANA

# 上传训练集
hadoop dfs -put CANA \TRAIN_DATA_FILE

# 进入到存放测试集的目录(目录名还是CANA)
# 该文件夹中是随机取的87个文件作为训练集
cd test_data_path

# 上传测试集
hadoop dfs -put CANA \TEST_DATA_FILE

```
![-w1061](media/15785753667141/15785760907893.jpg)

![-w1111](media/15785753667141/15785761613450.jpg)

## 修改目录、全局变量配置文件
在项目文件`/src/main/java/utils/Const.class`中，根据需要修改文档目录名、HDFS的域名地址、停用词库。

> 必须修改Const.class中的DOC_TYPE_LIST变量

该变量是记录文档种类的，每个文档种类用字符`@`分隔，例如我的分类任务是分三类，上传了CANA、CHINA、INDIA三份数据，那么这个变量设置为：

```Java
 public static final String DOC_TYPE_LIST = "CANA@CHINA@INDIA";
```

## 在IDEA中运行
可以直接在IDEA里运行Main函数即可自动完成所有的MapReduceJob
![-w469](media/15785753667141/15785767016736.jpg)

## 在命令行中运行任务

需要先打成JAR包，Main Class指定成Main.class。

------------------
**Mac系统用户需要注意**
Mac由于系统原因，打出来的JAR包会无法在命令行中运行，会提示找不到主类，需要先把JAR包中的 `META-INF` 文件删掉后才能在命令行中运行。
可以自己解压JAR包，删掉该文件夹，再重新压缩成JAR格式。

推荐使用第二种方式完成：
到JAR包的目录下，加入打包的JAR文件是`Main.jar`执行以下命令：

```shell
zip -d Main.jar META-INF/LICENSE
```
------------------

打包完成后，进入到JAR包目录，运行命令 `hadoop jar jar_file_path main_class_name` 执行JAR包，如：

```shell
hadoop jar Main.jar Main
```




