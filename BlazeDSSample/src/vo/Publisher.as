package vo
{	
	
	[RemoteClass(alias="BridgeCrane.Publisher")]
	//Без [RemoteClass(alias= выдает ошибку: Cannon invoke method 'voTest'
	public class Publisher
	{
		public var name:String;
		public var price:Number;
		
		
		public function Publisher()
		{
			//this.name = name;
		}
	}
}

