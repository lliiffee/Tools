//策略模式的意义是定义一系列的算法，把它们一个个封装起来，并且使它们可相互替换。


$( div ).animate( {"left: 200px"}, 1000, 'linear' );  //匀速运动
$( div ).animate( {"left: 200px"}, 1000, 'cubic' );  //三次方的缓动

　//　这2句代码都是让div在1000ms内往右移动200个像素. linear(匀速)和cubic(三次方缓动)就是一种策略模式的封装。


/*
比如姓名框里面， 需要验证   非空， 敏感词，字符过长         这几种情况。 当然是可以写3个if else来解决，
不过这样写代码的扩展性和维护性可想而知。如果表单里面的元素多一点，需要校验的情况多一点，加起来写上百个if else也不是没有可能。
　　所以更好的做法是把每种验证规则都用策略模式单独的封装起来。需要哪种验证的时候只需要提供这个策略的名字。就像这样：
　　JavaScript
*/
nameInput.addValidata({
   notNull: true,
   dirtyWords: true,
   maxLength: 30
})

//而notNull，maxLength等方法只需要统一的返回true或者false，来表示是否通过了验证。

validataList = {
  notNull: function( value ){
     return value !== '';
  },
  maxLength: function( value, maxLen ){
     return value.length() > maxLen;
  }
}

