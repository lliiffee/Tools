


SELECT * FROM TB_PRODUCT p INNER JOIN TB_DRUG_BASE d ON d.did = p.did AND p.alive = 1 AND p.verify = 1 
        AND d.ischufang != '1' AND p.shop_code = 100084 ORDER BY p.buys DESC LIMIT 0, 10
		
		
		
		
		
(function (w, d, undefined)
{    
    var _viewElement = null,    //element that will be used to render the view    我们需要将视图元素存储到一个变量中，这样就可以多次使用
        _defaultRoute = null,   //Object to store the default route   我们需要一个缺省的路由来应对url中没有路由信息的情况，这样就缺省的视图就可以被加载而不是展示空白页面。
        _rendered = false;      //Flag to determine if the view has been rendered from the controller or not.

    var jsMvc = function () {            //现在我们来创建我们的主要MVC对象的构造方法。我们会把路由信息存储在“_routeMap”中
        //mapping object for the routes
        this._routeMap = {};
		
    }
 
	var routeObj = function (c, r, t) {  //是时候创建路由对象了，我们会将路由、模板、控制器的信息存储在这个对象中
        this.controller = c;           //控制器的作用就是访问特定的路线
        this.route = r;                //路由的路线。这个就是url中#后面的部分。
        this.template = t;             //这是外部的html文件，它作为这个路由的视图被加载。现在我们的libs需要一个切入点来解析url，并且为相关联的html模板页面提供服务。为了完成这个，我们需要一个方法。
    }
	
	
	 //View Container Object
    /** @constructor */
    var viewContainer = function (renderDelegate)
    {
        this.render = renderDelegate;
        this.isAsync = false;
    }
/*
每一个url会有一个专门的路由对象routeObj.所有的这些对象都会被添加到_routeMap对象中，这样我们后续就可以通过key-value的方式获取它们。
为了添加路由信息到MVC libs中，我们需要曝光libs中的一个方法。所以让我们创建一个方法，这个方法可以被各自的控制器用来添加新路由。
*/
	
jsMvc.prototype.AddRoute = function (controller, route, template) {
        this._routeMap[route] = new routeObj(controller, 
route, template); 
    }
	

	 //Initialize the Mvc manager object to start functioning
    jsMvc.prototype.Initialize = function () {
        var startMvcDelegate = startMvc.bind(this);

        _viewElement = d.querySelector('[view]'); //get the html element that will be used to render the view         1）获取视图相关的元素的初始化。代码需要一个具有view属性的元素，这样可以被用来在HTML页面中查找： 
        if (!_viewElement) return; //do nothing if view element is not found       //3）验证视图元素是否合理

        //Set the default route
        _defaultRoute = this._routeMap[Object.getOwnPropertyNames(this._routeMap)[0]];   //2）设置缺省的路由

        //start the Mvc manager
        w.onhashchange = startMvcDelegate;             //4）绑定窗口哈希变更事件，当url不同哈希值发生变更时视图可以被及时更新
        startMvcDelegate();             //5）最后，启动mvc
        //this.Start();
    }


	//function to start the mvc support
    function startMvc()
    {
        var pageHash = w.location.hash.replace('#', ''),  //1）获取哈希值
            routeName = null,
            routeObj = null;                
        
        routeName = pageHash.replace('/', ''); //get the name of the route from the hash        //2）从哈希中获取路由值
        routeObj = this._routeMap[routeName]; //get the route object        //3）从路由map对象_routeMap中获取路由对象routeObj 

        //Set to default route object if no route found
        if (!routeObj)                                //4）如果url中没有路由信息，需要获取缺省的路由对象
            routeObj = _defaultRoute;
        loadTemplate(routeObj, _viewElement, pageHash); //fetch and set the view of the route  5）最后，调用跟这个路由有关的控制器并且为这个视图元素的视图提供服务  我们会传递路由对象的值和视图元素给方法loadTemplate
    }
	
	
	
	
	
	
	
	 //Start the Mvc manager object to start functioning
    jsMvc.prototype.Start = function () {
        var startMvcDelegate = startMvc.bind(this);
        startMvcDelegate();
        w.onhashchange = startMvcDelegate;
    }

    //Function to load external html data  我们需要使用XML HTTP请求异步加载合适的视图。为此，我们会传递路由对象的值和视图元素给方法loadTemplate。
    function loadTemplate(routeObject, view) {
        var xmlhttp;
        if (window.XMLHttpRequest)
        {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        }
        else
        {// code for IE6, IE5
            xmlhttp = new ActiveXObject('Microsoft.XMLHTTP');
        }
        xmlhttp.onreadystatechange = function ()
        {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
            {
			  //当前只剩加载视图和将对象模型与视图模板绑定了。我们会创建一个空的模型对象，然后传递与方法相关的模型来唤醒路由控制器。更新后的模型对象会与先前已经加载的XHR调用中的HTML模板绑定
                loadView(routeObject, view, xmlhttp.responseText);
            }
        }
        xmlhttp.open('GET', routeObject.template, true);
        xmlhttp.send();
    }

    //Function to load the view with the template  loadView 方法被用于调用控制器方法，以及准备模型对象
    function loadView(routeObject, viewElement, viewHtml) {
        var model = {},
            renderViewDelegate = renderView.bind(null, viewElement, viewHtml, model),
            view = new viewContainer(renderViewDelegate);

        routeObject.controller(view, model); //get the resultant model from the controller of the current route
        
        viewHtml = replaceToken(viewHtml, model); //bind the model with the view
        
        viewElement.innerHTML = viewHtml; //load the view into the view element

        //If the view is not in async mode and is not rendered from the controller function then render it from here
        if (!view.isAsync && !_rendered)
            renderView(viewElement, viewHtml, model)
    }

    function renderView(viewElement, viewHtml, model)
    {
        //bind the model with the view
        viewHtml = replaceToken(viewHtml, model);

        //load the view into the view element
        viewElement.innerHTML = viewHtml;

        //Set the _rendered flag to true indicating that the view has been rendered
        _rendered = true;
    }

   

    function replaceToken(viewHtml, model) {   //replaceToken方法被用于与HTML模板一起绑定模型
        var modelProps = Object.getOwnPropertyNames(model);
            
        modelProps.forEach(function (element, index, array)
        {
            viewHtml = viewHtml.replace('{{' + element + '}}', model[element]);
        });
        return viewHtml;
    }
	
	

    //attach the mvc object to the window
    w['jsMvc'] = new jsMvc();

})(window, document);
		