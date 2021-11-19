package dloan.common.util;

import java.util.Comparator;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class MapComparator implements Comparator<Map> {

	public MapComparator() {
		// TODO Auto-generated constructor stub
	}
	
	// 오름차순 정렬
	@Override
	public int compare(Map o1, Map o2) {
		// TODO Auto-generated method stub
		Integer i1 = new Integer(o1.get("num").toString());
		Integer i2 = new Integer(o2.get("num").toString());
		return i1.compareTo(i2);
	}
}
