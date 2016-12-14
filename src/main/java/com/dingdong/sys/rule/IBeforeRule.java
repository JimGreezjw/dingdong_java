package com.dingdong.sys.rule;

/**
 * 执行前规则
 * 
 * @author niukai
 * 
 */
public interface IBeforeRule extends IRule {
	public boolean beforeProcess(Object obj);
}
