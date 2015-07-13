package vo
{
	
	[RemoteClass(alias="BridgeCrane.CraneDescription")]
	public class CraneDescription
	{
		/*public var _id:		String;
		public var _Srok:		Number;
		public var _Progib:		Number;
		public var _Iskrivlenie:	Number;
		public var _Skruchivanie:	Number;
		public var _Viazkost:		Number;
		public var _Corrosion:		Number;*/
		
		//если оставить только public поля и убрать геттеры/сеттеры, то обмен не работает!!
		
		private var _id:		String;
		private var _Srok:		Number;
		private var _Progib:		Number;
		private var _Iskrivlenie:	Number;
		private var _Skruchivanie:	Number;
		private var _Viazkost:		Number;
		private var _Corrosion:		Number;
		
		
		
		public function CraneDescription()
		{
			//super();	//не обязательная строка!
		}

		public function get id():String
		{
			return _id;
		}

		public function set id(value:String):void
		{
			_id = value;
		}

		public function get Srok():Number
		{
			return _Srok;
		}

		public function set Srok(value:Number):void
		{
			_Srok = value;
		}

		public function get Progib():Number
		{
			return _Progib;
		}

		public function set Progib(value:Number):void
		{
			_Progib = value;
		}

		public function get Iskrivlenie():Number
		{
			return _Iskrivlenie;
		}

		public function set Iskrivlenie(value:Number):void
		{
			_Iskrivlenie = value;
		}

		public function get Skruchivanie():Number
		{
			return _Skruchivanie;
		}

		public function set Skruchivanie(value:Number):void
		{
			_Skruchivanie = value;
		}

		public function get Viazkost():Number
		{
			return _Viazkost;
		}

		public function set Viazkost(value:Number):void
		{
			_Viazkost = value;
		}

		public function get Corrosion():Number
		{
			return _Corrosion;
		}

		public function set Corrosion(value:Number):void
		{
			_Corrosion = value;
		}

		
	}
}





