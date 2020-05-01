## 社区首页开发
### 开发社区首页
* 开发流程
  - 1次请求的执行过程
*  分步实现
  * 开发社区首页，显示前10个帖子
  * **开发分页组件，分页显示所有的帖子**

#### 细节

```html
<nav class="mt-5" th:if="${page.rows>0}">
<!--分页基础视图-->
<ul class="pagination justify-content-center">
    <li class="page-item">
        <a class="page-link" href="#">首页</a>
    </li>
    <li class="page-item disabled">
        <a class="page-link" href="#">上一页</a>
    </li>
    <li class="page-item active">
        <a class="page-link" href="#">1</a>
    </li>
    <li class="page-item">
        <a class="page-link" href="#">下一页</a>
    </li>
    <li class="page-item">
        <a class="page-link" href="#">尾页</a>
    </li>
</ul>

<!--thymeleaf实现-->
<ul class="pagination justify-content-center">
    <!--首页-->
    <li class="page-item">
        <!--链接示例
        {${page.path}(current=1)}
        /index?current=1
        -->
        <a class="page-link" th:href="@{${page.path}(current=1)}">首页</a>
    </li>
    <!--上一页-->
    <li th:class="|page-item ${page.current==1?'disabled':''}|">
        <a class="page-link" th:href="@{${page.path}(current=${page.current-1})}">上一页</a>
    </li>
    <!--current为中心的五个 -->
    <li th:class="|page-item ${i==page.current?'active':''}|"
        th:each="i:${#numbers.sequence(page.from,page.to)}">
        <a class="page-link" th:text="${i}" th:href="@{${page.path}(current=${i})}"></a>
    </li>
    <!--下一页-->
    <li th:class="|page-item ${page.current==page.total?'disabled':''}|">
        <a class="page-link" th:href="@{${page.path}(current=${page.current+1})}">下一页</a>
    </li>
    <!--尾页-->
    <li class="page-item">
        <a class="page-link" th:href="@{${page.path}(current=${page.total})}">末页</a>
    </li>
</ul>
```

### 调试

* 响应状态码的含义
  * 重定向
  * 4xx
  * 5xx
* 服务端断点调试
* 客户端断点调试
* 日志输出

### 邮件配置

## 社区登录模块

### 开发注册功能

#### 步骤：

1. 访问注册页面
   1. 点击，打开注册界面
2. 提交注册数据
   1. 通过表单提交数据
   2. 服务端验证账号是否存在、邮箱是否注册
   3. 服务端发送激活邮件
3. 激活注册账号
   1. 通过邮箱的链接，访问服务端的激活服务



#### 细节

加密：MD5，密码后添加随机字符串（md5加密的结果是固定的，所以有隐患）

```java
// md5加密
public static String generateMD5(String key){
    if(StringUtils.isBlank(key)){
        return null;
    }
    return DigestUtils.md5DigestAsHex(key.getBytes());
}
```



表单处理：

```html
<!--method action 不能少-->
<form  method="post" th:action="@{/register}">
    <!--name要和自己编写的实体类的属性命名相同-->
    <input type="text"  id="username" name="username" placeholder="请输入您的账号!">
    ...
</form>
```

激活：

1. 成功
2. 重复
3. 错误链接



### 会话管理

#### HTTP的基本性质

- HTTP是简单的

  > 虽然下一代HTTP/2协议将HTTP消息封装到了帧（frames）中，HTTP大体上还是被设计得简单易读。HTTP报文能够被人读懂，还允许简单测试，降低了门槛，对新人很友好。

- HTTP是可拓展的

  > 在 HTTP/1.0 中出现的 [HTTP headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers) 让协议扩展变得非常容易。只要服务端和客户端就新 headers 达成语义一致，新功能就可以被轻松加入进来。

- HTTP是无状态的，有会话的

  > HTTP是无状态的：在同一个连接中，两个执行成功的请求之间是没有关系的。这就带来了一个问题，用户没有办法在同一个网站中进行连续的交互，比如在一个电商网站里，用户把某个商品加入到购物车，切换一个页面后再次添加了商品，这两次添加商品的请求之间没有关联，浏览器无法知道用户最终选择了哪些商品。而使用HTTP的头部扩展，HTTP Cookies就可以解决这个问题。把Cookies添加到头部中，创建一个会话让每次请求都能共享相同的上下文信息，达成相同的状态。
  >
  > 注意，HTTP本质是无状态的，使用Cookies可以创建有状态的会话。

#### Cookie

* 服务端发送到浏览器，并保存在浏览器的一小块数据
* 浏览器下次访问该服务器时，会自动携带该块数据，将其发送给服务器

#### Session

* 是JavaEE的标准，用于服务端记录客户信息
* 数据储存放在服务端更安全，但是也会增加服务器压力

### 验证码

1. 使用kaptcha动态生成验证码

2. 因为使用的是第三方库，配置好之后，将bean装配到spring容器中

3. 在controller中编写对应的动态生成验证码的URL

   ```java
   @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
   public void getKaptcha(HttpServletResponse response, HttpSession session){
       // 生成验证码
       String text = kaptchaProducer.createText();
       // 生成验证码图片
       BufferedImage image = kaptchaProducer.createImage(text);
       // 将验证码存入session
       session.setAttribute("kaptcha", text);
       // 将图片输出给浏览器
       response.setContentType("image/png");
       try {
           OutputStream outputStream = response.getOutputStream();
           ImageIO.write(image, "png", outputStream);
       } catch (IOException e) {
           logger.error("响应验证码失败: " + e.getMessage());
       }
   }
   ```

   

为什么使用`session`存储验证码：

> 单台服务器下，session是一个不错的验证码存储的地方，我们不用担心各个会话间的验证码存在冲突。



### 登录、退出模块

#### 步骤：

1. 访问登录界面
2. 登录
   1. 验证账号、密码、验证码
   2. 成功: 生成登录凭证，发送给客户端（cookies）
   3. 失败: 跳转回登录页
3. 退出
   1. 将登录凭证改为失效状态
   2. 跳转至登录页面

### 显示登录信息（拦截器）

登录成功：

导航栏：首页、消息、用户头像

![社区-登录.png](https://i.loli.net/2020/04/30/X7sqpkOa9t5MRZf.png)



登录不成功：

导航栏：首页、注册、登录

![社区-未登录.png](https://i.loli.net/2020/04/30/eKwhQmu64AckdIS.png)

#### 实现

**拦截器**

1. 拦截器示例：
   1. 定义拦截器，实现`HandlerInterceptor`
   2. 配置拦截器，为期指定拦截、排除路径
2. 拦截器应用
   1. 请求开始时，查询登录的用户
   2. 本次请求中，持有用户数据
   3. 模板视图中显示用户数据
   4. 请求结束时，清理用户数据



### 账号设置（文件上传下载）

* 请求： 必须是post
* 表单： enctype = "multipart/form-data"
* Spring MVC： 通过MultipartFile处理文件上传

#### 步骤

1. 访问账号设置页面
2. 上传头像
3. 获取头像

### 检测登录状态：

**防止非登录状态获取登录状态的信息**

如：用户退出后，自己拼凑url，获取登录用户的功能

#### 解决：

**拦截器**

* 方法之前标注自定义注解
* 拦截所有请求，只处理该注解方法



## 社区核心功能

### 过滤敏感词

#### 前缀树

* 别名：Trie、字典树、查找树
* 特点：查找效率高、内存消耗大
* 应用：字符串检索、词频统计、字符串排序

#### 敏感词过滤实现：

* 定义前缀树
* 根据敏感词，初始化前缀树
* 编写过滤敏感词的方法



### 发布帖子

#### Ajax

* Asynchronous JavaScript and XML 
* 异步的JavaScript和XML
* 使用Ajax，网页能够将增量更新呈现在页面上，而不需要刷新整个页面
* 虽然X代表XML， 但是目前JSON的使用更加普遍

#### 实现：

* 采用ajax请求，实现发布帖子

### 帖子详情

#### 步骤：

* DiscussPostMapper
* DiscussPostService
* DiscussPostController
* index.html
  * 在帖子标题上添加增加访问详情页面链接
* discuss-detail.html
  * 处理静态资源访问路径
  * 复用index.html的header
  * 显示标题、作者、发布时间、帖子正文等内容

### 事物管理

#### 事物

事物是由N步数据库操作序列组成的逻辑执行单元。要么全执行，要么全放弃执行

#### ACID

* 原子性

  * 事物应用中不可再分的最小执行体

* 一致性

  * 事物的执行结果，须使数据从一个一致性执行状态，变为另一个一致性执行状态

* 隔离性

  * 各个事物之间互不干扰，任何事物的内部操作对其他的事物都是隔离的

  * 隔离级别

    * 读未提交
    * 读提交
    * 可重复读
    * 串行化

  * 常见的并发异常

    * 更新

      * 第一类丢失更新

        ![第一类丢失更新.png](https://i.loli.net/2020/05/01/AFpGSuDC9bqJzWR.png)

        

        * 某一个事物的回滚，导致另外一个事物已更新的数据丢失了

      * 第二类丢失更新

        ![第二类丢失更新.png](https://i.loli.net/2020/05/01/K9vXhCw2AGEmc3R.png)

        

        * 某一个事物的提交，到时另一个事物已更新的数据丢失了

        

    * 读

      * 脏读

        * 某一个事物读取了另外一个事物未提交数据

          ![脏读.png](https://i.loli.net/2020/05/01/75gFI24sYmRkc8j.png)

          

      * 幻读

        * 某一个事物，对同一个表前后查询到的航属于不一致

          ![幻读.png](https://i.loli.net/2020/05/01/SxVmWwGaQenPhXH.png)

          

      * 不可重复读

        * 某一个事物，对同一个数据的前后读取不一致

          ![不可重复读.png](https://i.loli.net/2020/05/01/L2sBIEDCQiFtuxf.png)

          

          

          

          

  异常出现和隔离级别

  | 隔离级别 | 第一类丢失更新 | 第二类丢失更新 | 脏读 | 幻读 | 不可重复读 |
  | -------- | :------------: | :------------: | :--: | :--: | :--------: |
  | 读未提交 |       Y        |       Y        |  Y   |  Y   |     Y      |
  | 读提交   |       N        |       Y        |  N   |  Y   |     Y      |
  | 可重复读 |       N        |       N        |  N   |  Y   |     N      |
  | 串行化   |       N        |       N        |  N   |  N   |     N      |

  

* 持久性

  * 事物一旦提交，对数据所做的改变都要记录到永久存储器中

#### 实现隔离级别的机制

* 悲观锁

  * 共享锁(s锁)

    > 事物A对某数据加了共享锁后，其他事物只能对该数据加共享锁，但是不能加排它锁

  * 排它锁(x锁)

    > 事物A对某数据加了排它锁后，其他事物对该数据既不能加共享锁，也不能加排它锁

* 乐观锁

  * 版本号、时间戳等

    > 在更新数据前，检测版本号是否发发生变化。若变化，则取消本次更新，否则就更新数据(版本号+1)

  

#### spring中的事物管理

* 声明式事务
  * 通过xml注入
  * 通过注解注入

* 编程式事物
  * 通过`TransactionTemplate`管理事物

### 显示评论

#### 数据层

* 根据实体查询一页的评论
* 根据实体查询评论数量

#### 业务层

* 处理查询评论的业务
* 处理查询评论数量的业务

#### 表现层

* 显示帖子详情数据时，同时显示所有的评论

### 添加评论（逻辑太繁琐）

#### 数据层

* 添加评论数据
* 修改帖子的数量

#### 业务层

* 处理添加评论的业务
* 先增加评论，再更新帖子的评论数量

#### 表现层

* 处理添加评论数据的请求
* 设置添加评论的表单



### 私信列表

#### 私信列表

* 查询当前用户的会话列表（每一条会话只显示一条最新的私信）
* 支持分页显示

#### 私信详情

* 查询某个会话所包含的信息
* 支持分页显示



