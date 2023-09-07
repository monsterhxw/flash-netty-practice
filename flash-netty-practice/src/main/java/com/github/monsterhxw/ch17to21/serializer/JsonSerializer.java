package com.github.monsterhxw.ch17to21.serializer;

import com.alibaba.fastjson.JSON;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class JsonSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
