package com.example.demo.service;


import java.util.List;

public interface IBaseService<T> {
	/**
	 * 添加一条数据
	 * @param t
	 * @return
	 */
	public boolean insert(T t);
	/**
	 * 查询一条数据
	 * @param id
	 * @return
	 */
	public T selectById(String id);

	/**
	 * 修改一条数据
	 * @param t
	 * @return
	 */
	public boolean updateById(T t);
	/**
	 * 删除一条数据
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
	/**
	 * 查询所有数据
	 * @return
	 */
	public List<T> selectAll();
	/**
	 *
	 * <p>Title: queryByParamsWithPage</p>
	 * <p>Description: 分页查询</p>
	 * @param pageNum 页码
	 * @param pageSize 每页加载数据量
	 * @param t
	 * @return
	 * @author JobsZhang
	 * @date 2017年11月20日 上午11:44:59
	 */
	//public PageInfo<T> queryByParamsWithPage(int pageNum, int pageSize, T t);
}
