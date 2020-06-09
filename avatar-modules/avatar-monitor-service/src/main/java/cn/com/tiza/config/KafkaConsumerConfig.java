package cn.com.tiza.config;

import cn.com.tiza.tstar.common.entity.KeyDecoder;
import cn.com.tiza.tstar.common.entity.TStarDataDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: TZ0781
 **/
@Slf4j
@Configuration
@EnableKafka
@ConditionalOnProperty(name = "application.kafka.bootstrapServers")
public class KafkaConsumerConfig {

    @Autowired
    private ApplicationProperties properties;

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    public Map<String, Object> consumerConfigs() {
        log.info("---------------- init kafka consumer ---------------- ");
        Map<String, Object> propsMap = new HashMap<>();
        //"tstar-112:9092,tstar-113:9092,tstar-114:9092"
        ApplicationProperties.Kafka kafka = properties.getKafka();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KeyDecoder.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TStarDataDecoder.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, kafka.getGroupId());
        //"earliest"
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafka.getOffsetReset());
        if(kafka.isEnable()){
            log.info(" ---------------- enable k8s jaas ---------------- ");
            propsMap.put("security.protocol",kafka.getProtocol());
            propsMap.put("sasl.mechanism",kafka.getMechanism());
            propsMap.put("sasl.kerberos.service.name",kafka.getServiceName());
        }
        log.info(" ---------------- init kafka consumer end ---------------- ");
        return propsMap;
    }

    @Bean
    @ConditionalOnProperty(name = "application.kafka.keyTab")
    public KafkaJaasLoginModuleInitializer jaasConfig() throws IOException {
        log.info(" ---------------- k8s jaas config ---------------- ");
        ApplicationProperties.Kafka kafka = properties.getKafka();
        KafkaJaasLoginModuleInitializer jaasConfig = new KafkaJaasLoginModuleInitializer();
        jaasConfig.setControlFlag(KafkaJaasLoginModuleInitializer.ControlFlag.REQUIRED);
        Map<String, String> options = new HashMap<>();
        options.put("useKeyTab", "true");
        options.put("storeKey", "true");
        //"/etc/security/keytabs/kafka_client.keytab"
        options.put("keyTab", kafka.getKeyTab());
        //"kafka-client-1@EXAMPLE.COM"
        options.put("principal", kafka.getPrincipal());
        jaasConfig.setOptions(options);
        log.info(" ---------------- k8s jaas config end ---------------- ");
        return jaasConfig;
    }
}
