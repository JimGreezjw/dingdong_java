package com.dingdong.sys.rule;

/**
 * 执行后规则
 * 
 * @author niukai
 * 
 */
public interface IAfterRule extends IRule {
	public void afterProcess(Object obj);
}
