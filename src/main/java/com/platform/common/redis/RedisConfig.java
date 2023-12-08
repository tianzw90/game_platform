package com.platform.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class RedisConfig {
    private final static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("#{'${spring.data.redis.sentinel.nodes}'.split(',')}")
    private List<String> nodes;

    @Value("${spring.data.redis.sentinel.nodes}")
    private String redisNodes;

    @Value("${spring.data.redis.sentinel.master}")
    private String master;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.database}")
    private Integer database;
    @Value("${spring.data.redis.port}")
    private Integer port;

    @Value("${redis.model}")
    private Integer model;

    @Value("${redis.pool.maxIdle}")
    private Integer maxIdle;

    @Value("${redis.pool.maxTotal}")
    private Integer maxTotal;

    @Value("${redis.pool.maxWaitMillis}")
    private Integer maxWaitMillis;

    @Value("${redis.pool.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;

    @Value("${redis.pool.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;

    @Value("${redis.pool.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${redis.pool.testWhileIdle}")
    private boolean testWhileIdle;


    @Bean
    @ConfigurationProperties(prefix="spring.redis")
    public JedisPoolConfig getRedisConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(maxIdle);
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(maxTotal);
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        return jedisPoolConfig;
    }


    @Bean
    public RedisSentinelConfiguration sentinelConfiguration(){
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
        //配置matser的名称
        redisSentinelConfiguration.master(master);
        //配置redis的哨兵sentinel
        Set<RedisNode> redisNodeSet = new HashSet<>();
        nodes.forEach(x->{
            redisNodeSet.add(new RedisNode(x.split(":")[0],Integer.parseInt(x.split(":")[1])));
        });
        logger.info("redisNodeSet -->"+redisNodeSet);
        redisSentinelConfiguration.setSentinels(redisNodeSet);
        if (database!=null){
            redisSentinelConfiguration.setDatabase(database);
        }
        if (!StringUtils.isEmpty(password)){
            redisSentinelConfiguration.setPassword(RedisPassword.of(password));
        }

        return redisSentinelConfiguration;
    }
    @Bean
    public JedisConnectionFactory jedisConnectionFactory( RedisSentinelConfiguration redisSentinelConfiguration,
                                                          JedisPoolConfig jedisPoolConfig) {
        //单机模式
        if(model == 0){
            return JedisConnectionFactory(jedisPoolConfig);
        }
        logger.info("哨兵模式：master {}, node {}, database {}, 密码是否为空 {}",master,nodes,database,StringUtils.isEmpty(password));

        return new JedisConnectionFactory(redisSentinelConfiguration, jedisPoolConfig);
    }
    public  JedisConnectionFactory JedisConnectionFactory(JedisPoolConfig jedisPoolConfig){
        logger.info("单机模式：host {}, port {}, database {}, 密码是否为空 {}",host,port,database, StringUtils.isEmpty(password));
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        // 设置redis服务器的host或者ip地址
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        if(database != null){
            redisStandaloneConfiguration.setDatabase(database);
        }
        if (!StringUtils.isEmpty(password)){
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        }
        /**
         * 获得默认的连接池构造
         *  这里需要注意的是，redisConnectionFactoryJ对于
         *  Standalone模式的没有（RedisStandaloneConfiguration，JedisPoolConfig）的构造函数，对此
         *  我们用JedisClientConfiguration接口的builder方法实例化一个构造器，还得类型转换
         **/
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcf =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        // 修改我们的连接池配置
        jpcf.poolConfig(jedisPoolConfig);
        // 通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcf.build();

        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }
    /**
     *
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("jedisConnectionFactory") RedisConnectionFactory connectionFactory){

        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        // 关闭事务
        redisTemplate.setEnableTransactionSupport(false);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer =
                new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);

        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setEnableDefaultSerializer(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    /**
     * 注入封装RedisTemplate
     */
    @Bean(name = "redisUtil")
    public RedisUtils redisUtil(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils redisUtil = new RedisUtils();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }
}
