MessageInfo(消息体)
  里面放一个 枚举, key--场景 value--所需操作(task)

策略抽象Strategy: support(msgInf) getOperation() return int:该策略需要的tasks;  taskPipeline();  build();  
Task抽象: exec(Object param):这个参数不是Object,而是具体task需要的参数 return boolean;    getOperation() 每个task都得有个operation  
TaskPipeline: getOperation() return int; 重写CURD;    属性: operation统计task的operation;
策略抽象公共层实现: support(msgInf):根据operation来匹配. afterSupport(msgInf): 默认实现setMsgInf  该抽象实现里得有个msgInf; getMsgInf(); 包含TaskPipeline; 实现taskPipeline()方法; 定义initTaskPipeline(); 包含map集合,key为操作标识符,value为linkedList的下标
策略实现ConcreteStrategy: run()  实现initTaskPipeline()初始化taskPipeline; 




分三层:  策略层 --> 通道层 --> 任务层.    尽量做到职责明确.通道层可向外扩展. 任务以及场景的标识CURD采用'与'运算和'或'运算,不了解的自行百度.需要注意的是1<<2这个值不要用.
