package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"factura_view.xlsx\"");
		Factura factura = (Factura) model.get("factura");

		MessageSourceAccessor mensajes = getMessageSourceAccessor();
		
		Sheet sheet = workbook.createSheet("Factura Spring");

		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);

		cell.setCellValue(mensajes.getMessage("text.factura.ver.datos.cliente"));
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());

		// Forma 1 de crear filas
		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(factura.getCliente().getEmail());

		// Forma 2 de crear filas
		sheet.createRow(4).createCell(0).setCellValue(mensajes.getMessage("text.factura.ver.datos.factura"));
		sheet.createRow(5).createCell(0).setCellValue(mensajes.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
		sheet.createRow(6).createCell(0).setCellValue(mensajes.getMessage("text.cliente.factura.descripcion") + ": " + factura.getDescripcion());
		sheet.createRow(7).createCell(0).setCellValue(mensajes.getMessage("text.cliente.factura.fecha") + ": " + factura.getCreateAt());

		CellStyle theaderStyle = workbook.createCellStyle();
		theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
		theaderStyle.setBorderTop(BorderStyle.MEDIUM);
		theaderStyle.setBorderRight(BorderStyle.MEDIUM);
		theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
		theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
		theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		CellStyle tbodyStyle = workbook.createCellStyle();
		tbodyStyle.setBorderBottom(BorderStyle.THIN);
		tbodyStyle.setBorderTop(BorderStyle.THIN);
		tbodyStyle.setBorderRight(BorderStyle.THIN);
		tbodyStyle.setBorderLeft(BorderStyle.THIN);
		
		Row headerRow = sheet.createRow(9);
		headerRow.createCell(0).setCellValue(mensajes.getMessage("text.cliente.factura.producto"));
		headerRow.createCell(1).setCellValue(mensajes.getMessage("text.cliente.factura.precio"));
		headerRow.createCell(2).setCellValue(mensajes.getMessage("text.cliente.factura.cantidad"));
		headerRow.createCell(3).setCellValue(mensajes.getMessage("text.cliente.factura.total"));

		headerRow.getCell(0).setCellStyle(theaderStyle);
		headerRow.getCell(1).setCellStyle(theaderStyle);
		headerRow.getCell(2).setCellStyle(theaderStyle);
		headerRow.getCell(3).setCellStyle(theaderStyle);
		
		int rownum = 10;
		for (ItemFactura item : factura.getItems()) {
			Row filaRow = sheet.createRow(rownum ++);
			cell = filaRow.createCell(0);
			cell.setCellValue(item.getProducto().getNombre());
			cell.setCellStyle(tbodyStyle);
			
			cell = filaRow.createCell(1);
			cell.setCellValue(item.getProducto().getPrecio());
			cell.setCellStyle(tbodyStyle);
			
			cell = filaRow.createCell(2);
			cell.setCellValue(item.getCantidad());
			cell.setCellStyle(tbodyStyle);
			
			cell = filaRow.createCell(3);
			cell.setCellValue(item.calcularImporte());
			cell.setCellStyle(tbodyStyle);
		}
		
		Row filatotal = sheet.createRow(rownum);
		filatotal.createCell(2).setCellValue(mensajes.getMessage("text.cliente.factura.total") + ": ");
		filatotal.createCell(3).setCellValue(factura.getTotal());
		
	}

}