//类-字段，方法，接口。对象-实例instance，构造器constructor
//extend-inherit, override, subclass, superclass, polymorphism
//interface-implement, supertype, subtype
//object类
//泛型类型-generic type

//1静态字段
public static point origin = new Point();
//2面向对象，数据封装，隐性初始化
	//用类的对象存储数据，用这个方法来初始化或者赋值
public void clear(){
	x=0.0;
	y=0.0;
}
//3面向对象，调用方法
	//point是对象，这样直接使用函数输入，来实现方法更简洁
public double distance(Point that){
	double xdiff=x-that.x;
	double ydiff=y-that.y;
	return Math.sqrt(xdiff*xdiff+ydiff*ydiff);
}
//4this
	//这样既实现了方法，又完成了赋值用this，相当于赋值，从构造方法理解即新建了polarordi.len和Polaroid.angle
public void changeordinate(double x, double y){
	this.len=Math.sqrt(x*x+y*y);
	this.angle=arctan(x/y);
}
public body(String name, body orbits){
	this();				//调用这个类中的其他构造方法
	this.name=name;		//给这个新建的对象初始化，设置name和orbits字段
	this.orbits=orbits;
}
//5静态方法
	//静态方法可以不用new创建对象，直接使用这个方法。只能访问类中的static字段和其他static方法
class calculate{
	public static int staticsum(int x, int y){
		return x+y;
	}
	public int sum(int x, int y){
		return x+y;
	}
}
result=calculate.staticsum(1,2);
calculate cal=new calculate();result=cal.sum(1,2);
//6extend superclass
	//超类用法，super.clear指父类的方法
class pixel extends Point{
	Color color;
	public void clear(){
		super.clear();
		color=null;
	}
}
//7强制类型转换
	//即使类型相同，在转换是也要说明是什么类型（String）
String name="mars";
Object obj=name;
name=(String)obj;
//8异常
	//try, catch, finally
class BadDataSetException extends Exception{}
class MyUtilities{
	pbulic double[] getDataSet(String setname) throw BadDataSetException{
		String file=setname+“.dset”;
		FileInputStream in=null;
		try{
			in=new FileInputStream(file);
			return readDataSet(in);
		}catch (IOException e){
			throw new BadDataSetException();
		}finally{
			try{
				if(in!=null)
					in.close();
			}catch(IOException e){
				;//ignore
			}
		}
	}
}

//9包，package-com.sun.tools

//10类
	//用类作为数据结构，存储数据，和操作
class body{
	public long idnum;
	public String name;
	public body orbits;
	public static long nextid=0;
}
//11静态字段
	//被所有对象共享（由这个类实现的对象）。通过类名访问，不应该通过对象引用访问。改变一次，类的所有对象的这个静态字端应该都改变，不应该改变每一个对象的静态字段。
System.out.println(body.nextid);	//用这个，通过类名访问
System.out.println(instance.nextid);//不用这个，通过对象引用访问

//12Final，初始化后不能改变的对象。

//访问控制，在类层次上而非对象层次上
	//private,只作用于成员，不作用于类
	//package,
	//protected,只作用于成员，不作用于类
	//public.

//构造与初始化
class body{
	public long idnum;
	public static long nextid=0;
	body(){
		idnum=nextid++;
	}
}
body sun=new body();	//nextid is 0
body earth=new body();	//nextid is 1
//初始化块
	{}
	static{}

//方法返回多个结果
	//返回对象的引用，所有结果作为字段存储在这个对象中
	//传入引用型对象参数，在其中保存返回结果
	//返回包含结果的数组
public class permissions{
	public boolean candeposit, canwithdraw, canclose;
}
public class bankaccount{
	public permissions permissionsFor(person who){
		permissions perm=new permissions();
		return perm;
	}
}

//初始化的访问控制
	//private将nextid隐藏起来，不能被外部类修改。或者设为final也能达到同样效果
	//id变为了只读，在想从外部类获得id，只能通过调用getid()方法来返回id的值
class body{
	private long idnum;
	private static long nextid=0;
	body(){
		idnum=nextid++;
	}
	public long getid(){
		return idnum;
}

//方法重载override
	//同样的方法名字，有不同的输入参数，但不考虑输出参数
	
//导入静态成员名
Math.exp(x)		//通常写法
import static java.lang.Math.exp;
exp(x)			//可以化简写法

//page 48