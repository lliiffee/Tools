package tool.fung.reference;

public class Employee {
	 
		     private  String  id ; // ��Ա�ı�ʶ����
		     private  String  name ; // ��Ա����
		     private  String  department ; // �ù�Ա���ڲ���
		     private  String  Phone ; // �ù�Ա��ϵ�绰
		     private  String  origin ; // �ù�Ա��Ϣ����Դ
		     // ���췽��
		     public  Employee(String id) {
		        this . id  = id;
		       getDataFromlnfoCenter();
		     }
		     // �����ݿ���ȡ�ù�Ա��Ϣ

		     private   void  getDataFromlnfoCenter() {

		        // �����ݿ⽨�����Ӿ���ѯ�ù�Ա����Ϣ������ѯ�����ֵ
              this.name="fung";
              this.origin="test";
              this.Phone="35663557";
		        // �� name�� department�� plone�� salary�ȱ���
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
