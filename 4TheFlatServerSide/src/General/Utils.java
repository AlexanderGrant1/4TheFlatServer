package General;

public class Utils {
	
	public static String[] formatStringArray(String[] array)
	{
		int i = 0;
		for(String s : array)
		{
			s = s.replace("%20", " ");
			array[i] = s;
			i++;
		}
		return array;
	}

}
