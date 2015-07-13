package
{
	import flash.system.System;
	
	import mx.controls.DataGrid;
	public class supportFunc
	{
		public static var ccString:String;
		public function supportFunc()
		{
		}
		
		//Пример использования в функции:
		//mainDG.selectedIndices=[0,1,2,3]; //автоматическое выделение 4-х строк по индексам
		//supportFunc.CopyData(mainDG);
				
		//Данная функция копирует заголовки столбцов и ВЫДЕЛЕНЫЕ строки и помещает их в буфер обмена
		public static function CopyData(dg:DataGrid):void{
			ccString = new String();
			for(var i:Number=0;i<dg.columnCount;i++){
				ccString += dg.columns[i].headerText + "\t";
			}
			ccString += "\n";
			for each(var item:Object in dg.selectedItems){
				
				//!!!!!!!
				//ВАЖНО: Для копирования данных, здесь нужно выделять строки. Иначе никак.
				//!!!!!!
				for(var t:Number=0;t<dg.columnCount;t++){
					ccString += item[dg.columns[t].dataField] + "\t";
				}
				ccString += "\n";
			}
			
			System.setClipboard(ccString);
		}
					
	}
}
