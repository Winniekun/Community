package com.wkk.community.quartz;

import com.wkk.community.entity.DiscussPost;
import com.wkk.community.service.DiscussPostService;
import com.wkk.community.service.LikeService;
import com.wkk.community.util.CommunityConstant;
import com.wkk.community.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Time: 2020/5/8下午12:55
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */

public class PostScoreReferenceJob implements Job, CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(PostScoreReferenceJob.class);
    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-05-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化创站时间失败 " + e);
        }
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private LikeService likeService;

    // 定时任务
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String postScoreKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(postScoreKey);
        if(operations.size() == 0){
            logger.info("任务取消， 没有需要刷新的帖子");
            return;
        }
        logger.info("任务开始， 正在刷新帖子分数" + operations.size());
        while (operations.size() > 0){
            this.refresh((Integer)(operations.pop()));
        }

        logger.info("任务结束， 帖子分数刷新完毕" + operations.size());


    }

    /**
     * 计算分数
     * @param postId 帖子id
     *  log(精华分  + 评论*10 + 点赞*4 ) + （发布时间 - 创站时间）
     */
    private void refresh(Integer postId) {

        DiscussPost post = discussPostService.findDiscussPostById(postId);
        if(post == null){
            logger.error("该帖子不存在: id= " + postId);
            return;
        }
        // 是否加精
        boolean wonderful = post.getStatus()== 1;
        // 评论数量
        int commentCount = post.getCommentCount();
        // 点赞数量
        long likeCount = likeService.findEntityCount(postId, ENTITY_TYPE_POST);
        double w = (wonderful?75:0) + commentCount*10 + likeCount*4;
        // 分数 = 帖子权重+距离天数
        double score = Math.log10(Math.max(w, 1)) +
                ((post.getCreateTime().getTime() - epoch.getTime())/(1000*3600*24));
        // 更新帖子分数
        discussPostService.updateScore(postId, score);


    }
}
