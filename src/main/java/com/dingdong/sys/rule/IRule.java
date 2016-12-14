package com.dingdong.sys.rule;

/**
 * 规则接口
 * <p>
 * 将规则接口引入的本意，是想让业务逻辑更加灵活，规则接口分为执行前规则与执行后规则
 * </p>
 * 
 * <p>
 * 执行前规则主要是用来进行相应的校验，执行后规则是用来执行与当前业务相关的其它业务
 * </p>
 * 
 * <p>
 * 在此只是作为信息发送的尝试，并不在叮咚医生主体业务中应用
 * </p>
 * 
 * @author niukai
 * @December 13rd, 2015
 * 
 */
public interface IRule {
	public void process(Object obj);
}
