package org.boson.util;

import lombok.SneakyThrows;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class BeanUtils {

    public static <T> Supplier<? extends T> class2Supplier(Class<T> clazz) {
        return new Supplier<T>() {
            @SneakyThrows
            @Override
            public T get() {
                return clazz.newInstance();
            }
        };
    }

    /**
     * 拷贝模型对象
     *
     * @param from 源对象
     * @param to   目标对象
     * @return 目标对象
     */
    public static <T1, T2> T2 bean2Bean(T1 from, T2 to) {
        org.springframework.beans.BeanUtils.copyProperties(from, to);
        return to;
    }

    /**
     * 拷贝模型对象
     *
     * @param from     源对象
     * @param supplier 目标对象提供者
     * @return 目标对象
     */
    public static <T1, T2> T2 bean2Bean(T1 from, Supplier<? extends T2> supplier) {
        return bean2Bean(from, supplier.get());
    }

    /**
     * 拷贝模型对象
     *
     * @param from  源对象
     * @param clazz 目标对象类型
     * @return 目标对象
     */
    public static <T1, T2> T2 bean2Bean(T1 from, Class<T2> clazz) {
        return bean2Bean(from, class2Supplier(clazz));
    }

    /**
     * 拷贝模型对象序列
     *
     * @param from     源序列
     * @param supplier 目标对象提供者
     * @return 目标对象序列
     */
    public static <T1, T2> List<T2> bean2Bean(Collection<T1> from, Supplier<? extends T2> supplier) {
        return from.stream().map(t -> bean2Bean(t, supplier)).collect(Collectors.toList());
    }

    /**
     * 拷贝模型对象序列
     *
     * @param from  源序列
     * @param clazz 目标对象类型
     * @return 目标对象序列
     */
    public static <T1, T2> List<T2> bean2Bean(Collection<T1> from, Class<T2> clazz) {
        return bean2Bean(from, class2Supplier(clazz));
    }

    /**
     * 拷贝模型对象字典
     *
     * @param from     源字典
     * @param supplier 目标对象提供者
     * @return 目标字典
     */
    public static <T1, T2, T3> Map<T1, T3> bean2Bean(Map<T1, T2> from, Supplier<? extends T3> supplier) {
        return from.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, t -> bean2Bean(t.getValue(), supplier)));
    }

    /**
     * 拷贝模型对象字典
     *
     * @param from  源字典
     * @param clazz 目标对象类型
     * @return 目标字典
     */
    public static <T1, T2, T3> Map<T1, T3> bean2Bean(Map<T1, T2> from, Class<T3> clazz) {
        return bean2Bean(from, class2Supplier(clazz));
    }
}
