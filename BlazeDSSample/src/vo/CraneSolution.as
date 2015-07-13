package vo
{
	[RemoteClass(alias="BridgeCrane.CraneSolution")]
	public class CraneSolution
	{
			
		private var _Dopustimo:String;
		public function CraneSolution()
		{
		}

		public function get Dopustimo():String
		{
			return _Dopustimo;
		}

		public function set Dopustimo(value:String):void
		{
			_Dopustimo = value;
		}

	}
}


