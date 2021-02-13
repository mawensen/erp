package com.yufeng.service;

import com.yufeng.entity.Log;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 系统日志Service接口
 *
 * @author Wensen Ma
 */
public interface LogService {


    /**
     * 修改或者修改日志信息
     *
     * @param log
     */
	void save(Log log);

    /**
     * 根据条件分页查询日志信息
     *
     * @param log
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
	List<Log> list(Log log, Integer page, Integer pageSize, Direction direction, String... properties);

    /**
     * 获取总记录数
     *
     * @param user
     * @return
     */
	Long getCount(Log log);

}
