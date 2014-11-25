package com.fung.annotation.createdb;

@Table(name="TestTable")
@SuppressWarnings("unused")
public class TestTable {
	// 指定了列名字，约束为 主键，长度 以及类型
	@Column(name="id",constraints=@Constraints(primaryKey=true),length=15, type = Types.INT)
	private int id;
	
	// 列名 类型，如果都没有，只能使用默认的，也可以在后面处理类 里面 定义
	@Column(name="name", type = Types.VARCHAR)
	private String name;
	
	@Column(type = Types.BOOLEAN)
	private Boolean sex;
}