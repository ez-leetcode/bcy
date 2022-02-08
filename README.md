# 未次元
一个二次元ACG爱好者的社交APP&网站  
PC端产品原型链接: https://v5.modao.cc/app/49fb27876c7e1c7c818a437c2d1ebe8f8922c083  
Android端产品原型链接: https://v5.modao.cc/app/6ced71f67e1f6adfe4e82e547eb363bdb79bc3e6  
前端代码链接: https://github.com/401648921/acg  
Android端代码链接: https://github.com/Bngel/bcy  
后端架构图(图片在仓库目录下的framework.jpg):  ![image](https://github.com/ez-leetcode/bcy/blob/master/framework.jpg)  

# 后端技术相关
## 微服务解决方案：SpringCloud
     1.eureka(服务注册与发现)  
     2.gateway(服务网关)  
     3.openfeign(内置ribbon负载均衡)  
     4.hystrix(服务降级，服务熔断，服务限流)  
     5.oauth2(安全框架)  

## 主要技术选型：
     短信服务：腾讯云sms(阿里云去年开始就申请不了了，腾讯云可以注册微信公众号来申请)  
     搜索服务：elasticsearch + kibana(腾讯云不送logstash就只好用mq做数据同步了)  
     MySQL数据库读写分离中间件：mycat  
     持久层框架：mybatis-plus  
     即时通讯：websocket  
     定时任务：quartz  
     消息队列：rabbitmq  
     对象存储：阿里云oss  
     更安全的通信：ssl(https和wss，域名终于备案完了~)  
     第三方登录支持：微博登录(没做完QAQ)  
     发言敏感词API：阿里云第三方API  
     前后端交互：swagger  

## 服务器：
     1.阿里云主服务器(47.115.128.193) 
     2.阿里云(47.107.108.55) 
     3.腾讯云(1.117.75.49)  

## 服务部署分配：
     47.115.128.193(bcy-cloud-eureka,bcy-cloud-gateway,bcy-cloud-feign)  
     47.107.108.55(bcy-cloud-userpart,bcy-cloud-community-part,bcy-cloud-acgpart,bcy-cloud-quartz,bcy-cloud-oauth2,bcy-cloud-websocket)  
     1.117.75.49(bcy-cloud-userpart,bcy-cloud-community-part,bcy-cloud-acgpart,bcy-cloud-elasticsearch,bcy-cloud-websocket)  
     PS:主服务器上有mq和mycat等进程压力比较大就不部署别的provider服务了，两个副服务器内存不够，
     测试的时候三个部分(userpart,communitypart,acgpart)通常只部署在一个服务器上  

## 微服务请求相关流程：
     1.用统一域名(https://www.rat403.cn)请求过网关，通过过滤器校验id，token等  
     2.根据业务需求转发到openfeign/websocket/oauth2/elasticsearch等微服务  
     3.转发到openfeign的请求负载均衡地向服务提供者(hystrix)远程调用  
     4.服务提供者根据处理情况(服务高峰熔断，出错降级等)返回数据  

## MySQL数据库(1主2从)：
     1.基于binlog实现主从复制  
     2.mycat中间件实现读写分离(读策略简单轮询就好，服务器性能差不太多)  
     3.基于粗粒度rbac建用户表  
     4.分布式主键生成策略：snowflake算法  
     5.对于大张的业务表(例如用户信息表)根据模块需求垂直拆分(用户-系统设置表  用户-登录信息表  用户-个人信息表  用户-消息盒子表)  
     6.优化的读写策略：对热点数据，优先查redis缓存，redis无数据才会查mysql同时缓存数据到redis；
       有数据更新时，更新mysql的同时会更新redis(后面发现并发下会有缓存和数据库不一致的问题和缓存资源浪费，这里处理并不好，最好把缓存删掉--)；
     7.特别地对于计数类的数据(例如点赞数，收藏数)有更改时直接更新redis，用quartz定时调度（15分钟）持久化到mysql  

## Redis数据库(1主2从)：
     1.主从复制  
     2.读写分离  
     3.哨兵节点  
     4.缓存穿透(用谷歌开源的guava做一个布隆过滤器，在生成用户id时存储，在服务网关处对id用布隆过滤器校验，防止恶意id攻击)  
     5.缓存雪崩(对于可能同时大面积失效的key，如每日热榜在第二天12点会失效，redis缓存失效时间加上一个随机值)  

## Quartz：
     1.对redis缓存的计数统一调度持久化到mysql  
     2.每日每周根据点赞收藏加权排序更新日榜和周榜  

## Rabbitmq：
     1.对websocket的消息做fanout广播(websocket部署在多个服务器上)  
     2.对点赞 评论 关注 @ 热帖 私聊等消息推送  
     3.考虑消息可靠交付设置了手动ack(消费完之后再确认，而不是拿到消息直接确认)  
     4.websocket消息发送时自带uuid，接收后用redis缓存并校验丢弃重复数据，确保消息的幂等性，同时向发送方返回uuid作为ack  

## Elasticsearch：
     1.cos和问答的搜索采用了拼音分词器(搜拼音也可以哦~)和ik分词器并按相关度排序的策略  
     2.推荐cos和圈子收集了用户之前的搜索关键词(没有关键词就用固定的几个热词)，匹配现有的cos和圈子并随机抽取  
     3.由于腾讯云买es不送logstash，es和数据库的同步只好用rabbitmq，es的服务消费后从mysql重

## Oauth2：
     1.采用了简单的密码模式(有sms和第三方登录了这里就简单一点)  
     2.用户登录时在oauth2拿到token在网关校验即可  
     3.对客户端的验证，密码的加密，校验等支持


附swagger链接（现在有些服务器已经过期了，可能看不了）:  
用户部分:http://47.107.108.55:8001/swagger-ui.html  
社交部分:http://1.117.75.49:8002/swagger-ui.html  
ACG部分:http://1.117.75.49:8003/swagger-ui.html  
oauth2部分:http://47.107.108.55:8006/swagger-ui.html  
es部分:http://1.117.75.49:8008/swagger-ui.html

eureka链接:  
http://47.115.128.193:8000

#  产品架构图
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/0ADBB2060ABC0B0985F3C432D3B5B3CE.jpg)
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/18E30ADE7A965F45B1ED9EFA6D1FC18B.jpg)

# 下面是一部分项目展示图啦~
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/E02C6F797EF93F90A8F14AEB26FA4440.jpg)
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/D51068C6858F4122E85B0493E17D2F6F.jpg)
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/D3825AED092C3CD6FE0115533312385F.jpg)
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/661C6B2ED3784E0B5D6539CF98879CBD.jpg)
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/B672320F779AF3D5DF13261B4D0E4C9E.jpg)
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/C0F18EF1BA3DE8A6CEF5052A166DB751.jpg)
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/7E8C0D68E7441EDE940276478D96C89D.jpg)
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/CF87EACC2EDBB421632C4EB1749E705D.jpg)
![imag](https://rat-bcy.oss-cn-shenzhen.aliyuncs.com/yuque/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9%20%284%29/E1BB01AB6F81504547E3056C77AB6BA8.jpg)
