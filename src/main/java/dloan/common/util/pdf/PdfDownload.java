package dloan.common.util.pdf;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.util.StringUtils;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;
import be.quodlibet.boxable.image.Image;
import be.quodlibet.boxable.line.LineStyle;

public class PdfDownload {
	
	public String getFilename() {
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddmmmm");
		return ft.format(new Date());
	}

	public String getFilename(String pre) {
		return pre.concat("_").concat(getFilename());
	}
	
	public void createPdf(HttpServletRequest request, HttpServletResponse response, List<String> pageTitleList, List<Map<String, Object>> dataList) {
		try {
			String fileName = this.getFilename("거래명세서");
			// 응답 객체 설정
			// 바로 열기
			response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + ".pdf\"");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setContentType("application/pdf");
			
			// 전체 가격
			NumberFormat numberFormat = NumberFormat.getInstance();
			int sumPrice = 0;
			int sumCnt = 0;
			int sumRealPrice = 0;
			for(Map<String, Object> data : dataList) {
				String priceStr = (String) data.get("price");
				String cntStr = (String) data.get("cnt");
				
				int price = 0;
				int realPrice = 0;
				int cnt = 0;
				try {
					price = Integer.parseInt(priceStr);
					realPrice = price*90/100;
					cnt = Integer.parseInt(cntStr);
				} catch (Exception e) {
					price = 0;
					realPrice = 0;
					cnt = 0;
				}
				sumCnt += cnt;
				sumPrice += (realPrice * cnt);
				sumRealPrice += (price * cnt);
			}
			
			String[] kor1 = {"","일","이","삼","사","오","육","칠","팔","구"};
			String[] kor2 = {"","십","백","천"};
			String[] kor3 = {"","만","억","조","경"};
			
			// int to String
			String sumPriceStr = String.valueOf(sumPrice);
			int len = sumPriceStr.length();
			StringBuffer resultBuffer = new StringBuffer();
			for(int i = len - 1; i >= 0; i--) {
				resultBuffer.append(kor1[Integer.parseInt(sumPriceStr.substring(len - i - 1, len - i))]);
				
				if(Integer.parseInt(sumPriceStr.substring(len - i - 1, len - i)) > 0) {
					resultBuffer.append(kor2[i % 4]);
					if(i % 4 == 0) {
						resultBuffer.append(kor3[i / 4]);
					}
				}
			}
			
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String storeId = (String) dataList.get(0).get("storeId");
			
			// Create a document and add a page to it
			PDDocument document = new PDDocument();
			
			// Create a new font object selecting one of the PDF base fonts
			PDFont NanumGothicFont = PDType0Font.load(document, new FileInputStream(request.getSession().getServletContext().getRealPath("") + "/resources/font/NanumGothic.ttf"));
			
			for(String pageTitle : pageTitleList) {
				PDPage page = new PDPage(PDRectangle.A4);
				// rect can be used to get the page width and height
				document.addPage(page);

				// Start a new content stream which will "hold" the to be created content
				PDPageContentStream cos = new PDPageContentStream(document, page);

				//Dummy Table
				float margin = 20;
				// starting y position is whole page height subtracted by top and bottom margin
				float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
				// we want table across whole page width (subtracted by left and right margin ofcourse)
				float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

				boolean drawContent = true;
				float bottomMargin = 50;
				// y position is your coordinate of top left corner of the table
				float yPosition = 800;

				BaseTable table = new BaseTable(yPosition, yStartNewPage, bottomMargin, tableWidth, margin, document, page, true, drawContent);
				
				/* --------------------------------- title --------------------------------- */
				// the parameter is the row height
				Row<PDPage> headerRow = table.createRow(35);
				// the first parameter is the cell width
				Cell<PDPage> cell = headerRow.createCell(100, pageTitle);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(20);
				// vertical alignment
				cell.setValign(VerticalAlignment.MIDDLE);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				cell.setBottomBorderStyle(null);
				//table.addHeaderRow(headerRow);
				/* --------------------------------- title --------------------------------- */
				
				/* --------------------------------- blank line --------------------------------- */
				Row<PDPage> blankRow = table.createRow(0.5f);
				cell = blankRow.createCell(45, "");
				cell.setTopPadding(1f);
				cell.setBottomPadding(1f);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = blankRow.createCell(55, "");
				cell.setTopPadding(1f);
				cell.setBottomPadding(1f);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				cell.setBottomBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				/* --------------------------------- blank line --------------------------------- */
				
				/* --------------------------------- contents --------------------------------- */
				// ----------------------------- row 1 -----------------------------
				Row<PDPage> row = table.createRow(10);
				cell = this.customDefaultCell(row, 9, "작성일");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 1, ":");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 30, year + " 년  " + month + " 월  " + day + " 일");
				cell.setLeftPadding(20);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setAlign(HorizontalAlignment.LEFT);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 5, "");
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = row.createCell(15, "사업자번호");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setLeftBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				
				if(storeId.equals("nd_store")) {
					cell = this.customDefaultCell(row, 30, "202-01-93102");
				}else if (storeId.equals("bb_store")) {
					cell = this.customDefaultCell(row, 30, "206-10-94949");
				}else if (storeId.equals("nh_store")) {
					cell = this.customDefaultCell(row, 30, "131-91-95527");
				}else if (storeId.equals("nic_store")) {
					cell = this.customDefaultCell(row, 30, "121-90-91056");
				}else if (storeId.equals("sc_store")) {
					cell = this.customDefaultCell(row, 30, "315-63-00135");
				}else if (storeId.equals("sb_store")) {
					cell = this.customDefaultCell(row, 30, "784-17-00902");
				}else if (storeId.equals("ms_store")) {
					cell = this.customDefaultCell(row, 30, "401-86-00828");
				}else {
					cell = this.customDefaultCell(row, 30, "");
				}
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setAlign(HorizontalAlignment.CENTER);
				

				cell = this.customDefaultCell(row, 10, "");
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setRightBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				cell.setBottomBorderStyle(new LineStyle(Color.WHITE, 0.5f));
				// ----------------------------- row 1 -----------------------------
				
				// ----------------------------- row 2 -----------------------------
				row = table.createRow(10);
				cell = this.customDefaultCell(row, 9, "수   신");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 1, ":");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 30, "한라도서관 귀하");
				cell.setLeftPadding(20);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setAlign(HorizontalAlignment.LEFT);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 5, "");
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 15, "상          호");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setLeftBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				
				if(storeId.equals("nm_store")) {
					cell = this.customDefaultCell(row, 30, "남문도서");
				}else if (storeId.equals("nh_store")) {
					cell = this.customDefaultCell(row, 30, "노형서적");
				}else if (storeId.equals("nb_store")) {
					cell = this.customDefaultCell(row, 30, "늘벗서점");
				}else if (storeId.equals("ds_store")) {
					cell = this.customDefaultCell(row, 30, "대성서점");
				}else if (storeId.equals("my_store")) {
					cell = this.customDefaultCell(row, 30, "문예서점");
				}else if (storeId.equals("ji_store")) {
					cell = this.customDefaultCell(row, 30, "신제주제일도서");
				}else if (storeId.equals("ag_store")) {
					cell = this.customDefaultCell(row, 30, "아가페서적");
				}else if (storeId.equals("al_store")) {
					cell = this.customDefaultCell(row, 30, "아라서점");
				}else if (storeId.equals("yd_store")) {
					cell = this.customDefaultCell(row, 30, "연동서점");
				}else if (storeId.equals("us_store")) {
					cell = this.customDefaultCell(row, 30, "제주시우생당");
				}else if (storeId.equals("hl_store")) {
					cell = this.customDefaultCell(row, 30, "한라서적타운");
				}else if (storeId.equals("ig_store")) {
					cell = this.customDefaultCell(row, 30, "테스트서점");
				}else {
					cell = this.customDefaultCell(row, 30, "");
				}
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setAlign(HorizontalAlignment.CENTER);
				
				String fileloc = "logo";
				if(!StringUtils.isEmpty(storeId)) {
					fileloc += storeId.substring(0, 1).toUpperCase() + storeId.split("_")[0].substring(1);
				}
				String path = PdfDownload.class.getResource("").getPath();
				path = path.replace("%20"," ");
				Image image = new Image(ImageIO.read(new File(path+"img/"+fileloc+".png")));
				image = image.scaleByWidth(25);
				//cell = this.customDefaultCell(row, 10, "(인)");
				cell = row.createImageCell(10, image, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setValign(VerticalAlignment.MIDDLE);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 0.5f));
				cell.setLeftBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setRightBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				// ----------------------------- row 2 -----------------------------
				
				// ----------------------------- row 3 -----------------------------
				row = table.createRow(10);
				cell = this.customDefaultCell(row, 9, "제   목");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 1, ":");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 30, "희망도서구입");
				cell.setLeftPadding(20);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setAlign(HorizontalAlignment.LEFT);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 5, "");
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 15, "대   표   자");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setLeftBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				
				if(storeId.equals("nd_store")) {
					cell = this.customDefaultCell(row, 30, "오은정");
				}else if (storeId.equals("bb_store")) {
					cell = this.customDefaultCell(row, 30, "이재기");
				}else if (storeId.equals("nh_store")) {
					cell = this.customDefaultCell(row, 30, "이현자");
				}else if (storeId.equals("nic_store")) {
					cell = this.customDefaultCell(row, 30, "황경아");
				}else if (storeId.equals("sc_store")) {
					cell = this.customDefaultCell(row, 30, "고한옥");
				}else if (storeId.equals("sb_store")) {
					cell = this.customDefaultCell(row, 30, "정진현");
				}else if (storeId.equals("ig_store")) {
					cell = this.customDefaultCell(row, 30, "김학성");
					
				}else {
					cell = this.customDefaultCell(row, 30, "");
				}
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setAlign(HorizontalAlignment.CENTER);
				
				cell = this.customDefaultCell(row, 10, "");
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setTopBorderStyle(new LineStyle(Color.WHITE, 0.5f));
				cell.setRightBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				// ----------------------------- row 3 -----------------------------
				
				// ----------------------------- row 4 -----------------------------
				row = table.createRow(10);
				cell = this.customDefaultCell(row, 5, "");
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 40, "아래와 같이 견적서를 제출합니다");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 15, "주         소");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setLeftBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				
				if(storeId.equals("nd_store")) {
					cell = this.customDefaultCell(row, 40, "인천광역시 남동구 만수로 45 지층 72-49");
				}else if (storeId.equals("bb_store")) {
					cell = this.customDefaultCell(row, 40, "인천시 남동구 경원대로 971(간석동, 홈플러스 2층)");
				}else if (storeId.equals("nh_store")) {
					cell = this.customDefaultCell(row, 40, "인천광역시 남동구 논현동 631-10 에트로타워 2층 201호, 204호");
				}else if (storeId.equals("nic_store")) {
					cell = this.customDefaultCell(row, 40, "인천시 남동구 남동대로 727");
				}else if (storeId.equals("sc_store")) {
					cell = this.customDefaultCell(row, 40, "인천광역시 남동구 서창남로 51(서창동, 노블시티프라자 2층)");
				}else if (storeId.equals("sb_store")) {
					cell = this.customDefaultCell(row, 40, "인천광역시 남동구 장승남로 52");
				}else if (storeId.equals("ig_store")) {
					cell = this.customDefaultCell(row, 40, "인천광역시 남동구 소래역남로16번길 75(논현동, 비101호)");
				}else {
					cell = this.customDefaultCell(row, 40, "");
				}
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setRightBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				// ----------------------------- row 4 -----------------------------
				
				// ----------------------------- row 5 -----------------------------
				row = table.createRow(10);
				cell = this.customDefaultCell(row, 4, "합");
				cell.setBottomPadding(1);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setValign(VerticalAlignment.BOTTOM);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 4, "￦");
				cell.setTopPadding(0);
				cell.setBottomPadding(1);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(12);
				cell.setAlign(HorizontalAlignment.LEFT);
				cell.setValign(VerticalAlignment.BOTTOM);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 30, numberFormat.format(sumPrice));
				cell.setTopPadding(0);
				cell.setBottomPadding(1);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(12);
				cell.setAlign(HorizontalAlignment.RIGHT);
				cell.setValign(VerticalAlignment.BOTTOM);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 7, "원정");
				cell.setTopPadding(0);
				cell.setBottomPadding(1);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(12);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setValign(VerticalAlignment.BOTTOM);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 15, "업태및종목");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setLeftBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				
				if(storeId.equals("nd_store")) {
					cell = this.customDefaultCell(row, 40, "도소매, 서점(자료처리업)");
				}else if (storeId.equals("bb_store")) {
					cell = this.customDefaultCell(row, 40, "도, 소매(서적, 문구, 팬시 용품)");
				}else if (storeId.equals("nh_store")) {
					cell = this.customDefaultCell(row, 40, "소매업, 서적(참고서)");
				}else if (storeId.equals("nic_store")) {
					cell = this.customDefaultCell(row, 40, "소매업, 서적, 전자상거래업");
				}else if (storeId.equals("sc_store")) {
					cell = this.customDefaultCell(row, 40, "소매업, 마크장비");
				}else if (storeId.equals("sb_store")) {
					cell = this.customDefaultCell(row, 40, "도소매/서적, 도서전산화용역(마크장비)");
				}else if (storeId.equals("ig_store")) {
					cell = this.customDefaultCell(row, 40, "도소매/서비스업, 도서,문구,음반,도서정리,도서마크작업");
				}else {
					cell = this.customDefaultCell(row, 40, "");
				}
				cell.setFont(NanumGothicFont);
				cell.setFontSize(7);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setAlign(HorizontalAlignment.CENTER);
				
//				cell = this.customDefaultCell(row, 10, "전    화");
//				cell.setFont(NanumGothicFont);
//				cell.setFontSize(9);
//				cell.setAlign(HorizontalAlignment.CENTER);
//				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				
//				cell = this.customDefaultCell(row, 20, "");
//				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
//				cell.setRightBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				// ----------------------------- row 5 -----------------------------
				
				// ----------------------------- row 6 -----------------------------
				row = table.createRow(10);
				cell = this.customDefaultCell(row, 4, "계");
				cell.setTopPadding(0);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(11);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setValign(VerticalAlignment.TOP);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 41, resultBuffer.toString().concat("원 정"));
				cell.setTopPadding(1);
				cell.setBottomPadding(0);
				cell.setFont(NanumGothicFont);
				cell.setFontSize(12);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setBorderStyle(new LineStyle(Color.WHITE, 1));
				
				cell = this.customDefaultCell(row, 15, "팩  스  번  호");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setLeftBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				cell.setBottomBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				
				cell = this.customDefaultCell(row, 10, "");
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setBottomBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				
				cell = this.customDefaultCell(row, 10, "전 화 번 호");
				cell.setFont(NanumGothicFont);
				cell.setFontSize(9);
				cell.setAlign(HorizontalAlignment.CENTER);
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setBottomBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				
				if(storeId.equals("nm_store")) {
					cell = this.customDefaultCell(row, 20, "064-753-1800");
				}else if (storeId.equals("nh_store")) {
					cell = this.customDefaultCell(row, 20, "064-748-5202");
				}else if (storeId.equals("nb_store")) {
					cell = this.customDefaultCell(row, 20, "064-758-4689");
				}else if (storeId.equals("ds_store")) {
					cell = this.customDefaultCell(row, 20, "064-722-3509");
				}else if (storeId.equals("my_store")) {
					cell = this.customDefaultCell(row, 20, "064-724-7510");
				}else if (storeId.equals("ji_store")) {
					cell = this.customDefaultCell(row, 20, "064-712-9898");
				}else if (storeId.equals("ag_store")) {
					cell = this.customDefaultCell(row, 20, "064-753-6736");
				}else if (storeId.equals("al_store")) {
					cell = this.customDefaultCell(row, 20, "064-744-8341");
				}else if (storeId.equals("yd_store")) {
					cell = this.customDefaultCell(row, 20, "064-744-1114");
				}else if (storeId.equals("us_store")) {
					cell = this.customDefaultCell(row, 20, "064-722-2107");
				}else if (storeId.equals("hl_store")) {
					cell = this.customDefaultCell(row, 20, "064-758-9988");
				}else if (storeId.equals("ig_store")) {
					cell = this.customDefaultCell(row, 20, "064-753-1800");
				}else {
					cell = this.customDefaultCell(row, 20, "");
				}
				cell.setBorderStyle(new LineStyle(Color.BLACK, 0.5f));
				cell.setRightBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				cell.setBottomBorderStyle(new LineStyle(Color.BLACK, 1.5f));
				// ----------------------------- row 6 -----------------------------
				/* --------------------------------- contents --------------------------------- */
				
				/* --------------------------------- blank --------------------------------- */
				blankRow = table.createRow(1f);
				cell = this.customDefaultCell(blankRow, 100, "");
				cell.setBorderStyle(new LineStyle(Color.WHITE, 0));
				cell.setBottomBorderStyle(new LineStyle(Color.BLACK, 1));
				/* --------------------------------- blank --------------------------------- */
				
				/* --------------------------------- list --------------------------------- */
				// ----------------------------- table head -----------------------------
				row = table.createRow(1);
				this.tableCell(row.createCell(5, "연번"), NanumGothicFont);
				this.tableCell(row.createCell(40, "도서명"), NanumGothicFont);
				this.tableCell(row.createCell(10, "저자"), NanumGothicFont);
				this.tableCell(row.createCell(10, "출판사"), NanumGothicFont);
				this.tableCell(row.createCell(5, "권수"), NanumGothicFont);
				this.tableCell(row.createCell(10, "정가"), NanumGothicFont);
				this.tableCell(row.createCell(10, "정가총액"), NanumGothicFont);
				this.tableCell(row.createCell(10, "견적가"), NanumGothicFont);
				// ----------------------------- table head -----------------------------
				
				// ----------------------------- table body -----------------------------
				row = table.createRow(1);
				cell = row.createCell(45, "합 계");
				this.tableCell(cell, NanumGothicFont);
				cell = row.createCell(10, "");
				this.tableCell(cell, NanumGothicFont);
				cell = row.createCell(10, "");
				this.tableCell(cell, NanumGothicFont);
				cell = row.createCell(5, numberFormat.format(sumCnt));
				this.tableCell(cell, NanumGothicFont);
				cell = row.createCell(10, "");
				this.tableCell(cell, NanumGothicFont);
				cell.setAlign(HorizontalAlignment.RIGHT);
				cell = row.createCell(10, numberFormat.format(sumRealPrice));
				this.tableCell(cell, NanumGothicFont);
				cell.setAlign(HorizontalAlignment.RIGHT);
				cell = row.createCell(10, numberFormat.format(sumPrice));
				this.tableCell(cell, NanumGothicFont);
				// ----------------------------- table body -----------------------------
				
				// ----------------------------- table body -----------------------------
				for(Map<String, Object> data : dataList) {
					String rnum = !StringUtils.isEmpty(data.get("rnum")) ? (String) data.get("rnum") : "";
					String title = !StringUtils.isEmpty(data.get("title")) ? (String) data.get("title") : "";
					String author = !StringUtils.isEmpty(data.get("author")) ? (String) data.get("author") : "";
					String publisher = !StringUtils.isEmpty(data.get("publisher")) ? (String) data.get("publisher") : "";
					String priceStr = !StringUtils.isEmpty(data.get("price")) ? (String) data.get("price") : "";
					int realPrice = !StringUtils.isEmpty(data.get("price")) ? Integer.parseInt((String)data.get("price")) : 0; //10퍼센트 할인된 금액
					realPrice = realPrice*90/100;
					String realPriceStr = !StringUtils.isEmpty(data.get("price")) ? String.valueOf(realPrice) : "";
					String cntStr = !StringUtils.isEmpty(data.get("cnt")) ? (String) data.get("cnt") : "";
					
					int price = 0;
					int cnt = 0;
					realPrice = 0;
					if(!StringUtils.isEmpty(priceStr)) {
						try {
							price = Integer.parseInt(priceStr);
							priceStr = numberFormat.format(price);
						} catch (Exception e) {
							price = 0;
							priceStr = "0";
						}
					}
					if(!StringUtils.isEmpty(realPriceStr)) {
						try {
							realPrice = Integer.parseInt(realPriceStr);
							realPriceStr = numberFormat.format(realPrice);
						} catch (Exception e) {
							realPrice = 0;
							realPriceStr = "0";
						}
					}
					if(!StringUtils.isEmpty(cntStr)) {
						try {
							cnt = Integer.parseInt(cntStr);
							cntStr = numberFormat.format(cnt);
						} catch (Exception e) {
							cnt = 0;
							cntStr = "0";
						}
					}
					
					row = table.createRow(1);
					cell = row.createCell(5, rnum);			// 연번
					this.tableCell(cell, NanumGothicFont);
					cell = row.createCell(40, title);		// 도서명
					this.tableCell(cell, NanumGothicFont);
					cell.setAlign(HorizontalAlignment.LEFT);
					cell = row.createCell(10, author);		// 저자
					this.tableCell(cell, NanumGothicFont);
					cell = row.createCell(10, publisher);	// 출판사
					this.tableCell(cell, NanumGothicFont);
					cell.setAlign(HorizontalAlignment.LEFT);
					cell = row.createCell(5, cntStr);		// 수량
					this.tableCell(cell, NanumGothicFont);
					cell = row.createCell(10, priceStr);	// 정가
					this.tableCell(cell, NanumGothicFont);
					cell.setAlign(HorizontalAlignment.RIGHT);
					cell = row.createCell(10, numberFormat.format(price * cnt));	// 금액
					this.tableCell(cell, NanumGothicFont);
					cell.setRightPadding(2);
					cell.setAlign(HorizontalAlignment.RIGHT);
					cell = row.createCell(10, realPriceStr);	// 견적가
					this.tableCell(cell, NanumGothicFont);
					cell.setRightPadding(2);
					cell.setAlign(HorizontalAlignment.RIGHT);
					
				}
				// ----------------------------- table body -----------------------------
				/* --------------------------------- list --------------------------------- */
				
				// draw
				table.draw();
				
				// close the content stream 
				cos.close();
			}

			// Save the results and ensure that the document is properly closed:
			document.save(response.getOutputStream());
			document.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public Cell<PDPage> customDefaultCell(Row<PDPage> row, int width, String value) {
		Cell<PDPage> cell = row.createCell(width, value);
		cell.setTopPadding(4f);
		cell.setBottomPadding(4f);
		cell.setFontSize(10);
		return cell;
	}
	
	public void tableCell(Cell<PDPage> cell, PDFont font) {
		cell.setFont(font);
		cell.setTopPadding(2f);
		cell.setBottomPadding(2f);
		cell.setLeftPadding(1f);
		cell.setRightPadding(1f);
		cell.setFontSize(7);
		cell.setAlign(HorizontalAlignment.CENTER);
		cell.setValign(VerticalAlignment.MIDDLE);
	}
}
