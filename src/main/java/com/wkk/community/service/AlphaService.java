package com.wkk.community.service;

import com.sun.xml.internal.ws.spi.db.DatabindingException;
import com.wkk.community.dao.AlphaDao;
import com.wkk.community.dao.DiscussPostMapper;
import com.wkk.community.dao.UserMapper;
import com.wkk.community.entity.DiscussPost;
import com.wkk.community.entity.User;
import com.wkk.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * @Time: 2020/4/28下午8:08
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private TransactionTemplate template;

    public AlphaService(){
        System.out.println("构造器 alpha");
    }

    @PostConstruct
    // 在构造器之后调用
    public void init(){
        System.out.println("初始化 init");
    }

    @PreDestroy
    public void destory(){
        System.out.println("销毁 destory");
    }


    public String find(){
        return alphaDao.select();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1(){
        // 新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getSalt() + "123"));
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://images.nowcoder.com/head/98t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

//        user = userMapper.selectByName("alpha");

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("hello world");
        post.setContent("这是新人报道数据");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);
        Integer.valueOf("abc");


        return "OK";
    }

    public Object save2(){
        template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return template.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                // 新增用户
                User user = new User();
                user.setUsername("alpha");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5(user.getSalt() + "123"));
                user.setEmail("alpha@qq.com");
                user.setHeaderUrl("http://images.nowcoder.com/head/98t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

//        user = userMapper.selectByName("alpha");

                // 新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("我是新人报道数据");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);
                Integer.valueOf("abc");
                return "OK";
            }
        });
    }




}
