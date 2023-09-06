package com.github.monsterhxw.ch08.seri;


/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public interface Serializer {

    byte JSON_SERIALIZER = 1;

    Serializer DEFAULT = new JsonSerializer();

    byte getSerializerAlgorithm();

    byte[] serialize(Object object);

    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
