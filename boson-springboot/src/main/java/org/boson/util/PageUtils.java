package org.boson.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.boson.domain.PageResult;

import java.util.List;
import java.util.Objects;


/**
 * 分页工具类
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
public class PageUtils {

    private static final ThreadLocal<Page<?>> PAGE_HOLDER = new ThreadLocal<>();

    public static void setCurrentPage(Page<?> page) {
        PAGE_HOLDER.set(page);
    }

    public static Page<?> getPage() {
        Page<?> page = PAGE_HOLDER.get();
        if (Objects.isNull(page)) {
            setCurrentPage(new Page<>());
        }
        return PAGE_HOLDER.get();
    }

    public static Long getCurrent() {
        return getPage().getCurrent();
    }

    public static Long getSize() {
        return getPage().getSize();
    }

    public static Long getLimitCurrent() {
        return (getCurrent() - 1) * getSize();
    }

    public static void remove() {
        PAGE_HOLDER.remove();
    }

    public static <T> PageResult<T> limit(List<T> records) {
        int fromIndex = getLimitCurrent().intValue();
        int size = getSize().intValue();
        int toIndex = records.size() - fromIndex > size ? fromIndex + size : records.size();

        List<T> subRecords = records.subList(fromIndex, toIndex);
        return PageResult.of(subRecords, records.size());
    }
}
