import job.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import utils.Const;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/1/2 17:11
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("INPUT_PATH", Const.TRAIN_DATA_INPUT_PATH);
        conf.set("OUTPUT_PATH", Const.TRAIN_DATA_SEQUENCE_FILE_PATH);

        // 将训练集多个文件生成一个sequence_file
        InitSequenceFileJob initSequenceFileJob = new InitSequenceFileJob();
        ToolRunner.run(conf, initSequenceFileJob, args);

        // 根据InitSequenceFileJob输出的sequence_file统计每个文档类别有多少个文档
        GetDocCountFromDocTypeJob getDocCountFromDocTypeJob = new GetDocCountFromDocTypeJob();
        ToolRunner.run(conf, getDocCountFromDocTypeJob, args);

        // 根据InitSequenceFileJob输出的sequence_file统计每个单词在每个文档类别中出现的次数
        GetSingleWordCountFromDocTypeJob getSingleWordCountFromDocTypeJob = new GetSingleWordCountFromDocTypeJob();
        ToolRunner.run(conf, getSingleWordCountFromDocTypeJob, args);

        // 根据GetSingleWordCountFromDocTypeJob输出的sequence_file统计每个文档类别的总单词数
        GetTotalWordCountFromDocTypeJob getTotalWordCountFromDocTypeJob = new GetTotalWordCountFromDocTypeJob();
        ToolRunner.run(conf, getTotalWordCountFromDocTypeJob, args);


        // 开始运行测试集的数据
        conf = new Configuration();
        conf.set("INPUT_PATH", Const.TEST_DATA_INPUT_PATH);
        conf.set("OUTPUT_PATH", Const.TEST_DATA_SEQUENCE_FILE_PATH);
        conf.set("DOC_TYPE_LIST", Const.DOC_TYPE_LIST);

        // 将测试集多个文件生成一个sequence_file
        initSequenceFileJob = new InitSequenceFileJob();
        ToolRunner.run(conf, initSequenceFileJob, args);

        // 读取之前所有任务输出的sequence_file到内存并计算训练集的先验概率、条件概率(setup中进行)
        // 读取InitSequenceFileJob生成的测试集的sequence_file计算测试集的每个文档分成每一类的概率
        GetNaiveBayesResultJob getNaiveBayesResultJob = new GetNaiveBayesResultJob();
        ToolRunner.run(conf, getNaiveBayesResultJob, args);

        // 对各文档的贝叶斯分类结果进行评估，计算各文档FP、TP、FN、TN、Precision、Recall、F1以及整体的宏平均、微平均。
        Evaluation evaluation = new Evaluation();
        ToolRunner.run(conf, evaluation, args);
    }
}
