# bcy
一个ACG爱好者的APP~

# 微服务解决方案：springcloud  
     1.eureka(服务注册与发现)  
     2.gateway(服务网关)  
     3.feign(负载均衡)  
     4.hystrix(服务降级，服务熔断，服务限流)  
     5.oauth2(安全框架)  
		 
# 主要技术选型：  
     短信服务：腾讯云sms(阿里云去年开始就申请不了了，腾讯云可以注册微信公众号来申请)  
     搜索服务：elasticsearch + kibana(腾讯云不送logstash就只好用mq做数据同步了)  
     mysql数据库中间件：mycat  
     持久层框架：mybatis-plus  
     即时通讯：websocket  
     定时任务：quartz  
     消息队列：rabbitmq  
     对象存储：阿里云oss  
     更安全的通信：ssl(https和wss，域名终于备案完了~)  
     第三方登录支持：微博登录  
     发言敏感词API：阿里云第三方API  
     前后端交互：swagger  
 
# 服务器：  
     1.阿里云主服务器(47.115.128.193) 
     2.阿里云(47.107.108.55) 
     3.腾讯云(1.117.75.49)  
		 
# 服务部署分配：  
     47.115.128.193(bcy-cloud-eureka,bcy-cloud-gateway,bcy-cloud-feign)  
     47.107.108.55(bcy-cloud-userpart,bcy-cloud-community-part,bcy-cloud-acgpart,bcy-cloud-quartz,bcy-cloud-oauth2,bcy-cloud-websocket)  
     1.117.75.49(bcy-cloud-userpart,bcy-cloud-community-part,bcy-cloud-acgpart,bcy-cloud-elasticsearch,bcy-cloud-websocket)  
     PS:主服务器上有mq和mycat等进程压力比较大就不部署别的provider服务了，两个副服务器内存不够，
     测试的时候三个部分(userpart,communitypart,acgpart)通常只部署在一个服务器上  
              
# 微服务相关流程：  
     1.用统一域名请求过网关，检测token校验身份  
     2.根据业务需求转发到feign/websocket/oauth2/elasticsearch  
     3.转发到feign的请求负载均衡地向服务提供者rpc远程调用  
     4.服务提供者根据处理情况(服务高峰熔断，出错降级等)返回数据  

# mysql数据库(集群)：  
     1.binlog实现主从复制  
     2.mycat中间件实现读写分离(读策略轮询就好，服务器性能差不太多)  
     3.基于粗粒度rbac建用户表  
     4.分布式主键生成策略：snowflake  
     5.对于大张的业务表(例如用户/cos信息表)根据模块需求垂直拆分  
     6.优化的读写策略：对热点数据，优先查redis缓存，redis无数据才会查mysql同时缓存数据到redis；有数据更新时，更新mysql的同时会更新redis；
     特别地对于计数类的数据(例如点赞数，收藏数)有更改时直接更新redis，用quartz定时调度持久化到mysql  

# redis数据库(集群)：  
     1.主从复制(用原生支持的就好)  
     2.读写分离  
     3.哨兵节点  
     4.缓存穿透(用谷歌开源的guava做一个布隆过滤器，在生成用户id时存储，在服务网关处对id用布隆过滤器校验，防止恶意id攻击)  
     5.缓存雪崩(对于可能同时大面积失效的key，如每日热榜在第二天12点会失效，redis缓存失效时间加上一个随机值)  
     6.缓存击穿(对热点数据不设置过期时间)  
     7.分页缓存(对超级高频数据)  
                   
# quartz：  
     1.对redis缓存的计数统一调度持久化到mysql  
     2.每日每周根据点赞收藏加权排序更新日榜和周榜  

# rabbitmq：  
     1.对websocket的消息做fanout广播(websocket部署在多个服务器上)  
     2.对点赞 评论 关注 @ 热帖 私聊等消息推送  
     3.考虑消息可靠交付设置了手动ack  
     4.websocket消息发送时自带uuid，接收后用redis缓存并校验丢弃重复数据，确保消息的幂等性，同时向发送方返回uuid作为ack  

# elasticsearch：  
     1.集群，索引分片，副本等(腾讯云已经做好了)  
     2.cos和问答的搜索采用了拼音分词器和ik分词器并按相关度排序的策略  
     3.推荐cos和圈子收集了用户之前的搜索关键词(没有关键词就用固定的几个热词)，匹配现有的cos和圈子并随机抽取  

# oauth2：  
     1.采用了简单的密码模式(有sms和第三方登录了这里就简单一点)  
     2.用户登录时在oauth2拿到token在网关rpc校验即可  
     3.对客户端的验证，密码的加密，跨域之类的支持  
