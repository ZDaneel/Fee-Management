# Fee-Management

## 任务描述

开发一个班费日常使用记录的APP，记账员记录班费的收支情况，每笔支出需要包含日期、金额、购买的实物照片、购物小票（如果有）、实物的验收人。班级成员可以查看和查询班费的开支情况，可以在1周内（讨论期）对当笔开支提出质疑，班委成员回复质疑，所有质疑和应答全员可见。班级成员半数以上确认且所有质疑经发起人确认可以close的记录，可以标记为已确认状态，之后任何人无权再修改。超过讨论期未close的质疑自动close。

## 使用框架

前端使用Android Studio开发，使用框架主要包括SafeArgs、Okhttp、Glide、RecycleView、Navigation和EasyPhotos。

后端使用IntelliJ IDEA开发，使用框架主要包括Spring Boot、Mybatis-Plus、Lombok、Knife4j和Hutool。

数据库使用MySQL，中间件使用Redis和RabbitMQ，JSON解析使用Jackson。

## 系统模块

![image-20231027150205737](https://cdn.jsdelivr.net/gh/ZDaneel/cloudimg@main/img/202310271502938.png)

## 登陆权限设计

登录的思路是使用Session进行存储，安卓端与Web端不同，没有Cookie存放数据，所以需要在安卓端手动存储SessionId，以便后续请求时能够带上该SessionId，如果通过该SessionId没有查询到学生，说明处于没有登录的状态。

登录成功后将当前学生的信息存入Session，之后的每次请求都会获取该学生的信息，将其存入ThreadLocal中方便业务中随时获取到该用户信息。

权限设计的思路是，不同班级内的学生可以有不同的权限，根据不同的权限区分操作。由于该系统权限明确，并且不需要拓展，就使用写死权限代码的方式。

具体的设计如下，首先是数据库的设计。

学生表：

![image-20231027150346691](https://cdn.jsdelivr.net/gh/ZDaneel/cloudimg@main/img/202310271503831.png)

班级表：

![image-20231027150403642](https://cdn.jsdelivr.net/gh/ZDaneel/cloudimg@main/img/202310271504650.png)

学生班级表：

![image-20231027150426200](https://cdn.jsdelivr.net/gh/ZDaneel/cloudimg@main/img/202310271504308.png)

登录流程如图所示：

<img src="https://cdn.jsdelivr.net/gh/ZDaneel/cloudimg@main/img/202310271504817.png" alt="image-20231027150452355" style="zoom:50%;" />

之后的每次操作请求流程如下所示：

<img src="https://cdn.jsdelivr.net/gh/ZDaneel/cloudimg@main/img/202310271507794.png" alt="image-20231027150704747" style="zoom:50%;" />

## 图片处理

下面描述如何设计图片的处理。首先前端通过调用EasyPhoto组件实现相册/拍照功能，选择好图片后会上传到后端进行处理，保存到资源目录下并返回唯一的标识图片信息，前端接收到该信息后使用Glide框架进行图片加载，提交的时候将地址信息存入数据库中存储，之后只需要通过Glide就能够加载出图片。

### 1. 使用EasyPhoto处理图片

```java
binding.addFeeCamera.setOnClickListener(view -> {
   EasyPhotos.createAlbum(this, true, false, GlideEngine.getInstance())
       .setFileProviderAuthority("com.usts.fee_front.fileprovider")
       .start(feeImageCallback);
 });
```

### 2. 保存到后端的资源路径中

 ```java
 public Result<String> upload(@RequestParam MultipartFile file) {
    if (file.isEmpty()) {
      return Result.error("图片上传失败");
    }
    String originalFilename = file.getOriginalFilename();
    String type = FileUtil.extName(originalFilename);
    String uuid = UUID.randomUUID().toString().replace("-", "");
    String fileName = uuid + "." + type;
    String path = fileUploadPath + fileName;
    file.transferTo(FileUtil.touch(path));
    return Result.success(fileName);6
  }
 ```

### 3. 使用Glide加载图片

```java
Glide.with(requireActivity())
     .load(feeImagePath)
     .into(binding.addFeeImage);
```

## 开支定时关闭

使用RabbitMQ的插件x-delayed-message实现延迟交换机

![image-20231027151127691](https://cdn.jsdelivr.net/gh/ZDaneel/cloudimg@main/img/202310271511103.png)

### 新建开支时发送消息到延迟交换机

```java
MessagePostProcessor messagePostProcessor = msg -> {
   msg.getMessageProperties().setDelay(Integer.parseInt(TTL_TIME_DELAY));
   return msg;
 };
 rabbitTemplate.convertAndSend(EXCHANGE_FEE_DELAY, ROUTING_KEY_FEE_DELAY,
     feeId.toString(), messagePostProcessor);
```

### 时间到了以后消费该消息

```java
@RabbitListener(queues = QUEUE_FEE_DELAY)
 public void receiveDelayQueue(Message message, Channel channel) {
   String msg = new String(message.getBody());
   log.info("当前时间: {}, 收到延迟队列的消息: {}", DateUtil.date(), msg);
   Fee fee = getById(Integer.parseInt(msg));
}
```
