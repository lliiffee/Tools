package tool.fung.reference;
import  java.lang.ref.ReferenceQueue;
import  java.lang.ref.SoftReference;
import  java.util.Hashtable;

public   class  EmployeeCache {

     static  private  EmployeeCache  cache ; // һ�� Cacheʵ��
     private  Hashtable<String, EmployeeRef>  employeeRefs ; // ���� Chche���ݵĴ洢
     private  ReferenceQueue<Employee>  q ; // ���� Reference�Ķ���
     // �̳� SoftReference��ʹ��ÿһ��ʵ�������п�ʶ��ı�ʶ,
     // ���Ҹñ�ʶ������ HashMap�ڵ� key��ͬ��
     private class EmployeeRef extends SoftReference<Employee> {
        private  String  _key  =  "" ;
        public  EmployeeRef(Employee em, ReferenceQueue<Employee> q) {
            super (em, q);
            _key  = em.getId();
       }
    }

     // ����һ��������ʵ��

     private  EmployeeCache() {
        employeeRefs  =  new  Hashtable<String,EmployeeRef>();
        q  =  new  ReferenceQueue<Employee>();
    }
     // ȡ�û�����ʵ��
     public   static synchronized EmployeeCache getInstance() {
       if  (cache == null){

            cache = new EmployeeCache();
       }
       return cache ;
    }
     // �������õķ�ʽ��һ�� Employee�����ʵ���������ò����������
     private   void  cacheEmployee(Employee em) {

       cleanCache(); // �����������
       EmployeeRef ref =  new  EmployeeRef(em,  q );
        employeeRefs .put(em.getId(), ref);
    }
     // ������ָ���� ID�ţ����»�ȡ��Ӧ Employee�����ʵ��
     public  Employee getEmployee(String ID) {
       Employee em =  null ;
        // �������Ƿ��и� Employeeʵ���������ã�����У�����������ȡ�á�
        if  ( employeeRefs .containsKey(ID)) {
           EmployeeRef ref = (EmployeeRef)  employeeRefs .get(ID);
           em = (Employee) ref.get();
       }
        // ���û�������ã����ߴ��������еõ���ʵ���� null�����¹���һ��ʵ����
        // �����������½�ʵ����������
        if  (em ==  null ) {
           em =  new  Employee(ID);
           System. out .println( "Retrieve From EmployeeInfoCenter. ID="  + ID);
            this .cacheEmployee(em);
       }
        return  em;
    }
     // �����Щ�������õ� Employee�����Ѿ������յ� EmployeeRef����
     private   void  cleanCache() {
       EmployeeRef ref =  null ;
        while  ((ref = (EmployeeRef)  q .poll()) !=  null ) {
            employeeRefs .remove(ref. _key );
       }
    }
}
