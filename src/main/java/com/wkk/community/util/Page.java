package com.wkk.community.util;

/**
 * @Time: 2020/4/29下午2:55
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
// 分页
public class Page {
    // 当前页
    private int current = 1;
    // 显示上限
    private int limit = 10;
    // 数据总量， 方便计算分页个数
    private int rows;
    // 链接
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        // 防止当前页出现问题： current>=1
        if (current >= 1) {
            this.current = current;
        }

    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // 获取当前页的起始行
    public int getOffset() {
        return (current - 1) * limit;
    }

    // 获取种页数
    public int getTotal() {
        if (rows % limit != 0) {
            return rows / limit + 1;
        } else {
            return rows / limit;
        }
    }

    // 起始页数
    public int getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }

    // 结束页数
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return Math.min(to, total);
    }
}
