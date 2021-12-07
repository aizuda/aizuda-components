
# aizuda-components

![logo](https://portrait.gitee.com/uploads/avatars/namespace/2879/8637007_aizuda_1636162864.png!avatar100)

- 爱组搭 ~ 低代码组件化开发平台之组件库


- 愿景：每个人都是架构师

[爱组搭 ~ 组件源码示例演示](https://gitee.com/aizuda/aizuda-components-examples)



# 微信交流群

<img src="https://images.gitee.com/uploads/images/2021/1129/224455_6f75c7a9_12260.png" width="50%" height="50%">


# 公共模块

-  aizuda-common 主要内容 工具类 等。


# 限流（分布式锁）模块

-  aizuda-limiter 主要内容 api 限流，短信，邮件 发送限流、控制恶意利用验证码功能 等。

```xml
<dependency>
  <groupId>com.aizuda</groupId>
  <artifactId>aizuda-limiter</artifactId>
  <version>1.0.0</version>
</dependency>
```



# 机器人模块

- aizuda-robot 主要内容 bug 异常 推送到 企业微信 飞书 钉钉 等平台。

[企业微信机器人申请](https://work.weixin.qq.com/api/doc/90000/90136/91770)

[钉钉机器人申请](https://developers.dingtalk.com/document/robots/use-group-robots)

[飞书机器人申请](https://open.feishu.cn/document/ukTMukTMukTM/ucTM5YjL3ETO24yNxkjN)

```xml
<dependency>
  <groupId>com.aizuda</groupId>
  <artifactId>aizuda-robot</artifactId>
  <version>0.0.1</version>
</dependency>
```


# 安全模块

-  aizuda-security 主要内容 api 请求解密，响应加密，单点登录 等。

```xml
<dependency>
  <groupId>com.aizuda</groupId>
  <artifactId>aizuda-security</artifactId>
  <version>0.0.1</version>
</dependency>
```

> 签名规则规则

```text
md5( md5(传入内容) + timestamp ) = sign
```

> 请求约定规则

```text
时间戳 timestamp 签名 sign 参数（ MD5 算法 ）需要放在 header 或 url 明文传输。

开启加密签名内容为加密后的密文
```


> 响应返回规则

```text
{
   "code": 响应编码,
   "data": 文本json或其它，开启加密为加密后的内容,
   "message": 提示消息,
   "timestamp": 时间戳,
   "sign": 签名
}
```

- 单点登录功能支持，登录支持 cookie 或 token 两种模式，更多细节点击 [kisso](https://gitee.com/baomidou/kisso)

```text
// 生成 jwt 票据，访问请求头设置‘ accessToken=票据内容 ’ 适合前后分离模式单点登录
String jwtToken = SSOToken.create().setId(1).setIssuer("admin").setOrigin(TokenOrigin.HTML5).getToken();

// 解析票据
SSOToken ssoToken = SSOToken.parser(jwtToken);

// Cookie 模式设置
SSOHelper.setCookie(request, response,  new SSOToken().setId(String.valueOf(1)).setIssuer("admin"));

// 安全配置如下
kisso:
  config:
    # 开启 https 有效，传输更安全
    cookie-secure: true
    # 防止 XSS 防止脚本攻击
    cookie-http-only: true
    # 防止 CSRF 跨站攻击
    cookie-same-site: Lax
    # 加密算法 RSA
    sign-algorithm: RS512
    ...
```

