package com.usts.feeback.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.usts.feeback.utils.Constants.*;

/**
 * @author leenadz
 * @since 2022-12-20 14:20
 */
@Configuration
public class DelayedQueueConfig {

    /**
     * 延迟交换机
     *
     * @return 自定义交换机
     */
    @Bean
    public CustomExchange feeDelayedExchange() {

        Map<String, Object> arguments = new HashMap<>(8);
        arguments.put("x-delayed-type", "direct");
        /*
         * 1. 交换机名称
         * 2. 交换机类型
         * 3. 是否需要持久化
         * 4. 是否需要自动删除
         * 5. 其他参数
         */
        return new CustomExchange(EXCHANGE_FEE_DELAY, "x-delayed-message", true, false, arguments);
    }

    /**
     * 延迟队列
     *
     * @return 延迟队列
     */
    @Bean
    public Queue feeDelayedQueue() {
        return QueueBuilder.durable(QUEUE_FEE_DELAY).build();
    }

    /**
     * 绑定延迟队列和延迟交换机
     *
     * @param feeDelayedQueue    延迟队列
     * @param feeDelayedExchange 延迟交换机
     * @return 绑定
     */
    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("feeDelayedQueue") Queue feeDelayedQueue,
                                                      @Qualifier("feeDelayedExchange") CustomExchange feeDelayedExchange) {
        return BindingBuilder
                .bind(feeDelayedQueue)
                .to(feeDelayedExchange)
                .with(ROUTING_KEY_FEE_DELAY)
                .noargs();
    }
}
