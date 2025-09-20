package com.alibaba.otter.canal.connector.rabbitmq.producer;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.otter.canal.common.utils.JsonUtils;
import com.rabbitmq.client.AMQP;
import net.poweroak.framework.api.data.UniqueCharId;
import net.poweroak.security.EncryptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

import static com.alibaba.otter.canal.connector.redis.RedisClient.jedis;

/**
 * RabbitMQ Producer的工具类，BLUETTI数据同步分发特色的
 * @Author 许亮
 * @Mailto xht555@163.com, xuliang@poweroak.net
 * @Create 2025-09-18 10:31:15
 */
class CanalRabbitMQUtils {
    private static final Logger logger = LoggerFactory.getLogger(CanalRabbitMQUtils.class);

    public static AMQP.BasicProperties getMqProperties(Map<String, Object> headers) {
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.headers(headers);
        return builder.build();
    }

    /**
     * 获取binlog入消息队列前的消息投递详情
     * @param exchange MQ交换机名
     * @param message  byte[]格式的binlog日志
     * @return 消息投递详情
     */
    public static BinlogDeliveryInfo binlogDeliveryInfo(String exchange, byte[] message) {
        // message为即将要推送到MQ队列中的binlog
        // 在此转换为Map，进行一些入队列前的预处理操作
        Map<String, Object> canalLogMap = JsonUtils.unmarshalFromByte(message, new TypeReference<Map<String, Object>>() {});
        if (logger.isDebugEnabled()) {
            logger.debug("binlog监听解析：{}", canalLogMap);
        }

        BinlogDeliveryInfo binlogDeliveryInfo = new BinlogDeliveryInfo();
        boolean ddl = (boolean) canalLogMap.get("isDdl");
        if (ddl) {
            binlogDeliveryInfo.setDdl(true);
            return binlogDeliveryInfo;
        }

        // binlog行记录的主键字段名
        String pkColumn = ((JSONArray) canalLogMap.get("pkNames")).getString(0);
        // binlog行记录的变化数据
        TreeMap<String, String> binlogData = getBinlogData(((JSONArray)canalLogMap.get("data")).getJSONObject(0));

        // binlog投递信息

        binlogDeliveryInfo.setDeliveryId(UniqueCharId.newId()); // binlog投递ID，可理解为消息入MQ队列时的唯一消息ID，每次的投递ID都不相同
        binlogDeliveryInfo.setDatabase(canalLogMap.get("database").toString());
        binlogDeliveryInfo.setTable(canalLogMap.get("table").toString());
        binlogDeliveryInfo.setPkIdValue(binlogData.get(pkColumn));
        binlogDeliveryInfo.setDispatchId(EncryptUtils.md5Signature(binlogData)); // 计算签名值

        // 尝试获取binlog在主节点分发时的事务ID
        String app = exchange.split("-")[0];
        String cacheKey = binlogDeliveryInfo.getDispatchCacheKey(app);
        if (logger.isDebugEnabled()) {
            logger.debug("binlog dispatch cache key：==========> data-signature={}, cache-key={}", binlogDeliveryInfo.getDispatchId(), cacheKey);
        }

        binlogDeliveryInfo.setDispatchTime(jedis().get(cacheKey));
        return binlogDeliveryInfo;
    }

    private static TreeMap<String, String> getBinlogData(JSONObject jsonObject) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        for (String column : jsonObject.keySet()) {
            if (column.equals("srvRegion") || column.equals("actRegion")) {
                continue;
            }

            treeMap.put(column, jsonObject.getString(column));
        }

        return treeMap;
    }
}
