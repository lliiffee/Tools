package com.fung.jsoup;


public class TestSelenum {

	 public static void main(String[] args) {
	        // 用Firefox driver创建一个新的的实例
	        //注意:其他的代码依赖于界面
	        //不执行
	        
//	        System.setProperty ( "webdriver.firefox.bin" , "C:/Program Files (x86)/Mozilla Firefox/firefox.exe" );
//	        WebDriver driver = new FirefoxDriver();// 这里我们可以使用firefox来运行测试用例
	        //WebDriver driver = new ChromeDriver(); //这是chrome浏览器的驱动
	        //WebDriver driver = new InternetExplorerDriver(); //这是IE浏览器的驱动
	       // WebDriver driver = new HtmlUnitDriver(); //这是一个无界面测试模式，不用打开浏览器，通过后台输入来判断测试用例是否通过
	        
	        // 现在用这个来访问谷歌
//	        driver.get("http://www.800pharm.com/shop/m/index.html");
	        // 也可以用下面的方式访问谷歌
	        // driver.navigate().to("http://www.google.com");

	        // 找到文本输入元件的名字
//	        WebElement element = driver.findElement(By.name("q"));

	        // 在搜索框内输入“cheese!”
//	        element.sendKeys("Cheese!");

	        // 现在递交表格. WebDriver会发现我们的形式元素
//	        element.submit();

	        // 后台打印输出，检查网页的标题
//	        System.out.println("Page title is: " + driver.getTitle());
	       // driver.quit();
//	        driver.get("http://www.800pharm.com/shop/m/threeCategoryList.html?obcid=24&twbcid=155");
	        
	        // 谷歌的搜索是渲染过的动态JavaScript. 等待页面加载，暂停10秒
//	        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
//	            public Boolean apply(WebDriver d) {
//	               // return d.getTitle().toLowerCase().startsWith("cheese!");
//	            	return true;
//	            }
//	        });

	        // Should see: "cheese! - Google Search"
	      //  System.out.println("Page title is2: " + driver.getTitle());
	        
	       
	      //  System.out.println( driver.findElement(By.className("search_list")).getText());
//	        WebElement webElement = driver.findElement(By.xpath("/html"));
//	        String content = webElement.getAttribute("outerHTML");
//	        System.out.println(content);
//	        //关闭浏览器
//	       
//	        driver.close();
	    }
}
