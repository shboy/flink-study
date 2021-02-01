package com.xiaomi;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Authors: shenhao <shenhao@xiaomi.com>
 * created on 21-2-1
 */
public class Sum {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        // 访问http://localhost:8082 可以看到Flink Web UI
        conf.setInteger(RestOptions.PORT, 8082);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(2, conf);
        DataStream<Word> wordStream = env.fromElements(
            Word.of("Hello", 1), Word.of("Flink", 1),
            Word.of("Hello", 2), Word.of("Flink", 2)
        );

        // 使用KeySelector
        DataStream<Word> keySelectorStream = wordStream.keyBy(new KeySelector<Word, String>() {
            @Override
            public String getKey(Word in) {
                return in.word;
            }
        }).sum("count");

        keySelectorStream.print().setParallelism(1);

        env.execute("sum");
    }
}
