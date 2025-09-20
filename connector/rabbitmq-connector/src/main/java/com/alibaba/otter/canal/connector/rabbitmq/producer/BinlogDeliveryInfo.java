package com.alibaba.otter.canal.connector.rabbitmq.producer;

import org.springframework.util.Assert;

/**
 * binlog投递信息
 * @Author 许亮
 * @Mailto xht555@163.com, xuliang@poweroak.net
 * @Create 2025-09-18 17:21:35
 */
class BinlogDeliveryInfo {
    /**
     * binlog在主节点分发时的事务ID
     */
    private String dispatchId;
    /**
     * binlog在主节点分发的时间
     */
    private String dispatchTime;

    /**
     * binlog信息入MQ的投递事务ID
     */
    private String deliveryId;
    /**
     * binlog行日志记录的数据库名
     */
    private String database;
    /**
     * binlog行日志记录的表名
     */
    private String table;
    /**
     * 数据库表行记录的PK主键字段值
     */
    private String pkIdValue;

    /**
     * 是否为DDL数据库结构变更binlog
     */
    private boolean ddl;

    /**
     * 获取binlog分发事务的缓存key
     * @param prefix 前缀字符
     * @return
     */
    public String getDispatchCacheKey(String prefix) {
        Assert.hasText(this.database, "database不能为空！");
        Assert.hasText(this.table, "table不能为空！");
        Assert.hasText(this.pkIdValue, "pkIdValue不能为空！");
        Assert.hasText(this.dispatchId, "dispatchId不能为空！");
        return String.format("%s:dispatch:%s:%s:%s:%s", prefix, this.database, this.table, this.pkIdValue, this.dispatchId);
    }

    /**
     * 获取binlog在主节点分发时的事务ID
     * @return binlog在主节点分发时的事务ID
     */
    public String getDispatchId() {
        return dispatchId;
    }

    /**
     * 设置binlog在主节点分发时的事务ID
     * @param dispatchId binlog在主节点分发时的事务ID
     */
    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    /**
     * 获取binlog在主节点分发的时间
     * @return binlog在主节点分发的时间
     */
    public String getDispatchTime() {
        return dispatchTime;
    }

    /**
     * 设置binlog在主节点分发的时间
     * @param dispatchTime binlog在主节点分发的时间
     */
    public void setDispatchTime(String dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    /**
     * 获取binlog信息入MQ的投递事务ID
     * @return binlog信息入MQ的投递事务ID
     */
    public String getDeliveryId() {
        return deliveryId;
    }

    /**
     * 设置binlog信息入MQ的投递事务ID
     * @param deliveryId binlog信息入MQ的投递事务ID
     */
    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    /**
     * 获取binlog行日志记录的数据库名
     * @return binlog行日志记录的数据库名
     */
    public String getDatabase() {
        return database;
    }

    /**
     * 设置binlog行日志记录的数据库名
     * @param database binlog行日志记录的数据库名
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * 获取binlog行日志记录的表名
     * @return binlog行日志记录的表名
     */
    public String getTable() {
        return table;
    }

    /**
     * 设置binlog行日志记录的表名
     * @param table binlog行日志记录的表名
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * 获取数据库表行记录的PK主键字段值
     * @return 数据库表行记录的PK主键字段值
     */
    public String getPkIdValue() {
        return pkIdValue;
    }

    /**
     * 设置数据库表行记录的PK主键字段值
     * @param pkIdValue 数据库表行记录的PK主键字段值
     */
    public void setPkIdValue(String pkIdValue) {
        this.pkIdValue = pkIdValue;
    }

    /**
     * 获取是否为DDL数据库结构变更binlog
     * @return 是否为DDL数据库结构变更binlog
     */
    public boolean isDdl() {
        return ddl;
    }

    /**
     * 设置是否为DDL数据库结构变更binlog
     * @param ddl 是否为DDL数据库结构变更binlog
     */
    public void setDdl(boolean ddl) {
        this.ddl = ddl;
    }
}
