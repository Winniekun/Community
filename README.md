## 社区首页开发
### 开发社区首页
* 开发流程
  - 1次请求的执行过程
*  分步实现
  * 开发社区首页，显示前10个帖子
  * 开发分页组件，分页显示所有的帖子



### 调试

* 响应状态码的含义
  * 重定向
  * 4xx
  * 5xx
* 服务端断点调试
* 客户端断点调试
* 日志输出

### 邮件配置

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



