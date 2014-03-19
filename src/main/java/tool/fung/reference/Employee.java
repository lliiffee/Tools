package tool.fung.reference;

public class Employee {
	 
		     private  String  id ; // 雇员的标识号码
		     private  String  name ; // 雇员姓名
		     private  String  department ; // 该雇员所在部门
		     private  String  Phone ; // 该雇员联系电话
		     private  String  origin ; // 该雇员信息的来源
		     // 构造方法
		     public  Employee(String id) {
		        this . id  = id;
		       getDataFromlnfoCenter();
		     }
		     // 到数据库中取得雇员信息

		     private   void  getDataFromlnfoCenter() {

		        // 和数据库建立连接井查询该雇员的信息，将查询结果赋值
              this.name="fung";
              this.origin="test";
              this.Phone="35663557";
		        // 给 name， department， plone， salary等变量
		     }

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getDepartment() {
				return department;
			}

			public void setDepartment(String department) {
				this.department = department;
			}

			public String getPhone() {
				return Phone;
			}

			public void setPhone(String phone) {
				Phone = phone;
			}

			public String getOrigin() {
				return origin;
			}

			public void setOrigin(String origin) {
				this.origin = origin;
			}
		     
		     
}
