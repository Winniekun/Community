package com.wkk.community.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.wkk.community.dao.DiscussPostMapper;
import com.wkk.community.entity.DiscussPost;
import com.wkk.community.util.SensitiveFilter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import javax.swing.text.html.HTML;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Time: 2020/4/29上午10:42
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class DiscussPostService {
    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);
    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private int maxSize;

    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;

    // caffeine核心接口： Caffeine LoadingCache, AsyncLoadingCache

    // 帖子列表缓存
    private LoadingCache<String, List<DiscussPost>> postListCache;

    // 帖子总数的缓存
    private LoadingCache<Integer, Integer> postRowsCache;

    @PostConstruct
    public void init() {
        // 初始化帖子列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Nullable
                    @Override
                    public List<DiscussPost> load(@NonNull String key) throws Exception {
                        if(key == null || key.length() == 0){
                            throw new IllegalArgumentException("参数错误");
                        }
                        String[] split = key.split(":");
                        if(split == null || split.length != 2){
                            throw new IllegalArgumentException("参数错误");
                        }
                        int offset = Integer.valueOf(split[0]);
                        int limit = Integer.valueOf(split[1]);
                        // 可添加二级缓存
                        logger.debug("load post list from DB. ");
                        return discussPostMapper.selectDiscussPosts(0, offset, limit, 1);
                    }
                });
        // 初始化帖子总数的缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(@NonNull Integer userId) throws Exception {

                        // 可添加二级缓存
                        logger.debug("load post rows from DB. ");
                        return discussPostMapper.selectDiscussPostrows(userId);
                    }
                });
    }

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode) {
        // 首页热帖时才进行缓存
//        if (userId == 0 && orderMode == 1) {
//            return postListCache.get(offset + ":" + limit);
//        }
        logger.debug("load post list from DB. ");
        return discussPostMapper.selectDiscussPosts(userId, offset, limit, orderMode);
    }

    public int findDiscussPostRows(int userId) {
        // 首页查询缓存
//        if(userId == 0){
//            return postRowsCache.get(userId);
//        }
        logger.debug("load post rows from DB. ");
        return discussPostMapper.selectDiscussPostrows(userId);
    }

    // 添加post
    public int addDiscussPost(DiscussPost discussPost) {
        if (discussPost == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        // 转义html标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        // 过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        // 插入数据
        return discussPostMapper.insertDiscussPost(discussPost);

    }

    // post详情
    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    // 置顶
    public int updateType(int id, int type) {
        return discussPostMapper.updateType(id, type);
    }

    // 加精
    public int updateStatus(int id, int status) {
        return discussPostMapper.updateStatus(id, status);
    }

    // 更新分数
    public int updateScore(int id, double score) {
        return discussPostMapper.updateScore(id, score);
    }
}
