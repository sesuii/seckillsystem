# 秒杀系统

## 1. 用户登录

通过 cookie + redis 的方式实现分布式 session 存储

## 2. 缓存预热

项目启动时，将数据库的商品库存存储到 redis 中。同时，在本地缓存中记录商品是否卖完的信息。

## 3. 秒杀业务

### 3.1 验证用户是否登录

用户登录后才能购买商品

### 3.2 判断商品是否已经卖完

本地缓存中记录了商品是否卖完的信息。

> 优化内容：数据库查询商品库存 -> 本地缓存中查询

### 3.3 判断用户是否重复秒杀某一商品

用户秒杀成功某商品后，会将信息存储到 redis 中。同时在数据库中建立用户ID与商品ID的唯一索引，确保用户不会重复购买同一商品。

> 优化内容：从数据中根据用户ID查询商品ID -> redis 中查询

### 3.4 预减库存

用户秒杀商品，先预减库存。库存不足，则秒杀失败。库存减为0时，则记录该商品已经卖完。（见 3.2）

> 优化内容：数据库中查询商品库存 -> redis 中预减库存

### 3.5 将秒杀信息放入消息队列中

通过消息队列实现异步下单与流量削峰。

### 3.6 消费消息

#### 3.6.1 扣减商品库存

扣减数据库的商品库存

#### 3.6.2 生成订单

在数据库生成商品订单

#### 3.6.3 将秒杀成功的用户ID与商品ID保存到redis中

### 3.7 秒杀失败

在 3.6 中任一过程出问题时，数据将会回滚，redis 中预减的库存会加回来。

## 4. 系统测试

- 注释掉 `SeckillController # seckillGoods` 方法中的`验证用户是否登录`的代码。用`随机数字`模拟`用户ID`。

```java
public class SeckillController {
        @PostMapping("/goods")
    public String seckillGoods(@RequestParam("goodsId") Integer goodsId) {
        // 判断用户是否登录
//        SkUser skUser = isLogin();
//        if (skUser == null) {
//            log.info("用户未登录！");
//            return "login";
//        }
        
        // 使用随机字数字模拟 userId
        int userId = new Random().nextInt(2000);
    }
}
```

- 使用 Jmeter 测试

  POST 请求访问 [http://localhost:8080/seckill/goods](http://localhost:8080/seckill/goods)

  > 测试前准备工作：
  >
  > 	1. 清空数据库中的订单表  sk_order
  > 	2. 清空 redis 缓存（小心误删了自己需要的数据哦）
  > 	3. 修改 application.yml 文件

