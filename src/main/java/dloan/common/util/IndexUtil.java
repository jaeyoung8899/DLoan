package dloan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexUtil.class);
	
	public static String readyToMakeIdx2(String strData) {

		String strIdxData    = "";
		String strAddIdxData = "";
		String[] strDataArr  = removeSpecialCharacter(strData, "space").split(" ");
		
		if (strDataArr.length == 1) {
			strIdxData = strData;
		} else {
			strIdxData = TrimData(removeSpecialCharacter(strData));

			for (int i = 0; i < strDataArr.length -1; i++) {
				strAddIdxData = "";

				if (i == strDataArr.length -2) {
					strAddIdxData = " " + strDataArr[i] + " " + strDataArr[i+1];
				} else {
					for (int j = i; j < strDataArr.length; j++) {
						if (j == i) {
							strAddIdxData = " "+strDataArr[j]+" ";
						} else {
							strAddIdxData += strDataArr[j];
						}
					}
				}
				strIdxData += strAddIdxData;
			}
		}
		return strIdxData;
	}


	public static String removeSpecialCharacter(String input) {
		return removeSpecialCharacter(input, null);
	}

	@SuppressWarnings("unused")
	public static String removeSpecialCharacter(String input, String mode) {

		char[][] DELETE_SPECIAL_CHAR = null;
		//0. 특수문자 시작~
		int INT_DELETE_SPECIAL_CHAR[][] = new int[39][2];
		INT_DELETE_SPECIAL_CHAR[0] [0] = 0x0000; INT_DELETE_SPECIAL_CHAR[0] [1] = 0x001F;
		INT_DELETE_SPECIAL_CHAR[1] [0] = 0x0020; INT_DELETE_SPECIAL_CHAR[1] [1] = 0x002F;
		INT_DELETE_SPECIAL_CHAR[2] [0] = 0x003A; INT_DELETE_SPECIAL_CHAR[2] [1] = 0x0040;
		INT_DELETE_SPECIAL_CHAR[3] [0] = 0x005B; INT_DELETE_SPECIAL_CHAR[3] [1] = 0x0060;
		INT_DELETE_SPECIAL_CHAR[4] [0] = 0x007B; INT_DELETE_SPECIAL_CHAR[4] [1] = 0x00BF;
		INT_DELETE_SPECIAL_CHAR[5] [0] = 0x0220; INT_DELETE_SPECIAL_CHAR[5] [1] = 0x0221;
		INT_DELETE_SPECIAL_CHAR[6] [0] = 0x02B9; INT_DELETE_SPECIAL_CHAR[6] [1] = 0x02BF;
		INT_DELETE_SPECIAL_CHAR[7] [0] = 0x02C2; INT_DELETE_SPECIAL_CHAR[7] [1] = 0x02DF;
		INT_DELETE_SPECIAL_CHAR[8] [0] = 0x02E5; INT_DELETE_SPECIAL_CHAR[8] [1] = 0x02EE;
		INT_DELETE_SPECIAL_CHAR[9] [0] = 0x0300; INT_DELETE_SPECIAL_CHAR[9] [1] = 0x036F;
		INT_DELETE_SPECIAL_CHAR[10][0] = 0x2000; INT_DELETE_SPECIAL_CHAR[10][1] = 0x206F;
		INT_DELETE_SPECIAL_CHAR[11][0] = 0x2070; INT_DELETE_SPECIAL_CHAR[11][1] = 0x209F;
		INT_DELETE_SPECIAL_CHAR[12][0] = 0x20A0; INT_DELETE_SPECIAL_CHAR[12][1] = 0x20CF;
		INT_DELETE_SPECIAL_CHAR[13][0] = 0x20D0; INT_DELETE_SPECIAL_CHAR[13][1] = 0x20FF;
		INT_DELETE_SPECIAL_CHAR[14][0] = 0x2190; INT_DELETE_SPECIAL_CHAR[14][1] = 0x21FF;
		INT_DELETE_SPECIAL_CHAR[15][0] = 0x2200; INT_DELETE_SPECIAL_CHAR[15][1] = 0x22FF;
		INT_DELETE_SPECIAL_CHAR[16][0] = 0x2300; INT_DELETE_SPECIAL_CHAR[16][1] = 0x23FF;
		INT_DELETE_SPECIAL_CHAR[17][0] = 0x2400; INT_DELETE_SPECIAL_CHAR[17][1] = 0x243F;
		INT_DELETE_SPECIAL_CHAR[18][0] = 0x2440; INT_DELETE_SPECIAL_CHAR[18][1] = 0x245F;
		INT_DELETE_SPECIAL_CHAR[19][0] = 0x2460; INT_DELETE_SPECIAL_CHAR[19][1] = 0x24FF;
		INT_DELETE_SPECIAL_CHAR[20][0] = 0x2500; INT_DELETE_SPECIAL_CHAR[20][1] = 0x257F;
		INT_DELETE_SPECIAL_CHAR[21][0] = 0x2580; INT_DELETE_SPECIAL_CHAR[21][1] = 0x259F;
		INT_DELETE_SPECIAL_CHAR[22][0] = 0x25A0; INT_DELETE_SPECIAL_CHAR[22][1] = 0x25FF;
		INT_DELETE_SPECIAL_CHAR[23][0] = 0x2600; INT_DELETE_SPECIAL_CHAR[23][1] = 0x26FF;
		INT_DELETE_SPECIAL_CHAR[24][0] = 0x2700; INT_DELETE_SPECIAL_CHAR[24][1] = 0x27CF;
		INT_DELETE_SPECIAL_CHAR[25][0] = 0x2800; INT_DELETE_SPECIAL_CHAR[25][1] = 0x28FF;
		INT_DELETE_SPECIAL_CHAR[26][0] = 0x3000; INT_DELETE_SPECIAL_CHAR[26][1] = 0x303F;
		INT_DELETE_SPECIAL_CHAR[27][0] = 0x3200; INT_DELETE_SPECIAL_CHAR[27][1] = 0x32FF;
		INT_DELETE_SPECIAL_CHAR[28][0] = 0xA000; INT_DELETE_SPECIAL_CHAR[28][1] = 0xA48F;
		INT_DELETE_SPECIAL_CHAR[29][0] = 0xA490; INT_DELETE_SPECIAL_CHAR[29][1] = 0xA4CF;
		INT_DELETE_SPECIAL_CHAR[30][0] = 0xF000; INT_DELETE_SPECIAL_CHAR[30][1] = 0xF0FF;
		INT_DELETE_SPECIAL_CHAR[31][0] = 0xFE20; INT_DELETE_SPECIAL_CHAR[31][1] = 0xFE2F;
		INT_DELETE_SPECIAL_CHAR[32][0] = 0xFE30; INT_DELETE_SPECIAL_CHAR[32][1] = 0xFE4F;
		INT_DELETE_SPECIAL_CHAR[33][0] = 0xFE50; INT_DELETE_SPECIAL_CHAR[33][1] = 0xFE6F;
		INT_DELETE_SPECIAL_CHAR[34][0] = 0xFF00; INT_DELETE_SPECIAL_CHAR[34][1] = 0xFF0F;
		INT_DELETE_SPECIAL_CHAR[35][0] = 0xFF1A; INT_DELETE_SPECIAL_CHAR[35][1] = 0xFF20;
		INT_DELETE_SPECIAL_CHAR[36][0] = 0xFF3B; INT_DELETE_SPECIAL_CHAR[36][1] = 0xFF40;
		INT_DELETE_SPECIAL_CHAR[37][0] = 0xFF5B; INT_DELETE_SPECIAL_CHAR[37][1] = 0xFF65;
		INT_DELETE_SPECIAL_CHAR[38][0] = 0xFFE0; INT_DELETE_SPECIAL_CHAR[38][1] = 0xFFFF;

		DELETE_SPECIAL_CHAR = new char[39][2];
		for (int index = 0; index < 39; index++) {
			DELETE_SPECIAL_CHAR[index][0] = (char)INT_DELETE_SPECIAL_CHAR[index][0];
			DELETE_SPECIAL_CHAR[index][1] = (char)INT_DELETE_SPECIAL_CHAR[index][1];
		}

		//0. 특수문자 끝
		boolean specialChar = false;
		char    tempChar;
		String  output      = "";

		// 1. 입력 데이터 확인
		if (input == null) {
			return null;
		}

		// 2. DEL_SPECIAL_CHAR 초기화 상태확인
		if (DELETE_SPECIAL_CHAR == null) {
			return null;
		}

		// 3. 입력 문자열 확인
		for (int i = 0; i < input.length(); i++) {
			tempChar = input.charAt(i);
			for (int j = 0; j < DELETE_SPECIAL_CHAR.length; j++) {
				// 4. 특수기호를 찾음
				specialChar = false;
				if ((tempChar >= DELETE_SPECIAL_CHAR[j][0]) && (tempChar <= DELETE_SPECIAL_CHAR[j][1])) {
					specialChar = true;
					break;
				}
			}
			// 5. 특수기호에 해당하지 않을 경우 결과로 저장
			// 6. 색인어 만드는 부분에서 [ 전집 1-해리포터 ] 의 경우 해리포터 검색어 잡히지 않음
			// 6. 적용은 하지만 코라스랑 다른 부분으로 인하여 다시 롤백할 가능성 있음 by zmkzmk 20151202
			if (specialChar == false) {
				output += tempChar;
			} else {
				if (mode != null && mode.equals("space")) {
					output += " ";
				}
			}
		}

		return output;
	}

	public static String TrimData (String strData) {
		String strTrimData = "";

		//Trim
		String strTrim = "";
		strTrim = strData.trim();
		strTrimData = strTrim;

		//Replace
		String strReplaceSpace = "";
		strReplaceSpace = strData.replace(" ", "");
		strTrimData = strReplaceSpace;

		return strTrimData;
	}
	
	/**
	 * 색인 데이터 생성시 컬럼의 최대 Byte수에 맞게 데이터를 보정
	 * @param columnSize 컬럼 최대 크기
	 * @param idxData 색인데이터
	 * @return 컬럼 최대 크기에 맞는 데이터
	 */
	public static String makeIdxForColumnSize(int columnSize, String idxData) {
		String returnIdx = "";
		int i = 0;
		int size = 0;
		byte[] idxByte = null;
		
		try {
			for(i=0;i < idxData.length();i++) {
				idxByte = String.valueOf(idxData.charAt(i)).getBytes("UTF-8");
				size += idxByte.length;
				
				if(size > columnSize) {
					idxData = idxData.substring(0, i - 1);
					break;
				}
			}
			returnIdx = idxData;
		} catch (Exception e) {
			// 2019.05.07 소스코드 보안취약점 조치
			//e.printStackTrace();
			LOGGER.error("색인데이터 크기 보정 오류");
			returnIdx = idxData;
		} finally {
			idxByte = null;
		}
		
		return returnIdx; 
	}
}
